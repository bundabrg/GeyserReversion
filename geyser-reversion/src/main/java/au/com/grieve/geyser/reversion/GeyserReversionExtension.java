/*
 * MIT License
 *
 * Copyright (c) 2020 GeyserReversion Developers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package au.com.grieve.geyser.reversion;

import au.com.grieve.geyser.reversion.api.Edition;
import au.com.grieve.geyser.reversion.config.Configuration;
import au.com.grieve.geyser.reversion.editions.bedrock.BedrockEdition;
import au.com.grieve.geyser.reversion.editions.education.EducationEdition;
import au.com.grieve.reversion.Build;
import au.com.grieve.reversion.api.RegisteredTranslator;
import au.com.grieve.reversion.api.ReversionServer;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.annotations.GeyserEventHandler;
import org.geysermc.connector.event.events.geyser.GeyserStartEvent;
import org.geysermc.connector.event.handlers.EventHandler;
import org.geysermc.connector.extension.ExtensionClassLoader;
import org.geysermc.connector.extension.ExtensionManager;
import org.geysermc.connector.extension.GeyserExtension;
import org.geysermc.connector.extension.annotations.Extension;
import org.geysermc.connector.utils.LanguageUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Extension(
        name = "GeyserReversion",
        version = "1.1.0-dev",
        authors = {"bundabrg"},
        description = "Provides multiversion protocol support for Geyser"
)
@Getter
public class GeyserReversionExtension extends GeyserExtension {
    @Getter
    private static GeyserReversionExtension instance;

    private final Map<String, Edition> registeredEditions = new HashMap<>();
    private final List<RegisteredTranslator> registeredTranslators = new ArrayList<>();

    private ReversionServer server;

    private Configuration config;

    public GeyserReversionExtension(ExtensionManager extensionManager, ExtensionClassLoader extensionClassLoader) {
        super(extensionManager, extensionClassLoader);
        instance = this;

        loadConfig();
        registerEditions();
        registerTranslators();
    }

    /**
     * Register built-in editions
     */
    private void registerEditions() {
        registerEdition("bedrock", new BedrockEdition(this));
        registerEdition("education", new EducationEdition(this));
    }

    /**
     * Register built-in translators
     */
    private void registerTranslators() {
        for (RegisteredTranslator translator : Build.TRANSLATORS) {
            registerTranslator(translator);
        }
    }


    /**
     * Register an Edition
     */
    public void registerEdition(String name, Edition edition) {
        registeredEditions.put(name, edition);
    }

    /**
     * Register a Translator
     */
    public void registerTranslator(RegisteredTranslator translator) {
        registeredTranslators.add(translator);
    }

    /**
     * Load our config, generating it if necessary
     */
    private void loadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            configFile.getParentFile().mkdirs();

            try (InputStream fis = getResourceAsStream("config.yml")) {
                Files.copy(
                        fis,
                        configFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = Configuration.loadFromFile(configFile);
    }

    /**
     * Replace Geyser BedrockServer with one provided by an edition
     */
    @GeyserEventHandler(priority = EventHandler.PRIORITY.LOW)
    public void onGeyserStart(GeyserStartEvent event) {
        Edition edition = registeredEditions.get(config.getEdition());

        if (edition == null) {
            getLogger().error(String.format("Invalid Edition '%s'. Extension disabled.", config.getEdition()));
            return;
        }

        InetSocketAddress address = GeyserConnector.getInstance().getBedrockServer().getBindAddress();

        // Create a new server on the same address/port as the default server
        server = edition.createReversionServer(GeyserConnector.getInstance().getBedrockServer().getBindAddress());

        try {
            Field bedrockServer = GeyserConnector.class.getDeclaredField("bedrockServer");
            bedrockServer.setAccessible(true);

            GeyserConnector.getInstance().getBedrockServer().close();
//            bedrockServer.set(GeyserConnector.getInstance(), server);

        } catch (NoSuchFieldException e) {
            getLogger().error(String.format("Unable to set Edition '%s'. Extension disabled.", config.getEdition()), e);
        }

        // Give the old BedrockServer time to close down then bind our default server
        GeyserConnector.getInstance().getGeneralThreadPool().schedule(() -> {
            server.bind().whenComplete((avoid, throwable) -> {
                if (throwable != null) {
                    getLogger().severe(LanguageUtils.getLocaleStringLog("geyser.core.fail", address.getAddress().toString(), address.getPort()));
                    throwable.printStackTrace();
                }
            }).join();
        }, 1, TimeUnit.SECONDS);
    }
}
