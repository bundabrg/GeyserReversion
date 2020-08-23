/*
 * EduSupport - Minecraft Protocol Support for MultiVersion in Geyser
 * Copyright (C) 2020 GeyserReversion Developers
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package au.com.grieve.geyser.reversion;

import au.com.grieve.geyser.reversion.api.Edition;
import au.com.grieve.geyser.reversion.config.Configuration;
import au.com.grieve.geyser.reversion.editions.bedrock.BedrockEdition;
import au.com.grieve.geyser.reversion.editions.education.EducationEdition;
import au.com.grieve.reversion.ReversionServer;
import au.com.grieve.reversion.api.BaseTranslator;
import au.com.grieve.reversion.api.TranslatorException;
import au.com.grieve.reversion.translators.v390ee_to_v408be.Translator_v390ee_to_v408be;
import au.com.grieve.reversion.translators.v409be_to_v408be.Translator_v409be_to_v408be;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.annotations.GeyserEventHandler;
import org.geysermc.connector.event.events.geyser.GeyserStartEvent;
import org.geysermc.connector.event.handlers.EventHandler;
import org.geysermc.connector.plugin.GeyserPlugin;
import org.geysermc.connector.plugin.PluginClassLoader;
import org.geysermc.connector.plugin.PluginManager;
import org.geysermc.connector.plugin.annotations.Plugin;
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

@Plugin(
        name = "GeyserReversion",
        version = "1.1.0-dev",
        authors = {"bundabrg"},
        description = "Provides multiversion protocol support for Geyser"
)
@Getter
public class GeyserReversionPlugin extends GeyserPlugin {
    @Getter
    private static GeyserReversionPlugin instance;

    private final Map<String, Edition> registeredEditions = new HashMap<>();
    private final List<Class<? extends BaseTranslator>> registeredTranslators = new ArrayList<>();

    private Configuration config;

    public GeyserReversionPlugin(PluginManager pluginManager, PluginClassLoader pluginClassLoader) {
        super(pluginManager, pluginClassLoader);
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
        registerTranslator(Translator_v390ee_to_v408be.class);
        registerTranslator(Translator_v409be_to_v408be.class);
    }


    /**
     * Register an Edition
     */
    public void registerEdition(String name, Edition edition) {
        registeredEditions.put(name, edition);
        getLogger().debug("Registered Edition: " + name);
    }

    /**
     * Register a Translator
     */
    public void registerTranslator(Class<? extends BaseTranslator> translatorClass) {
        registeredTranslators.add(translatorClass);
        getLogger().debug("Registered Translator: " + translatorClass.getSimpleName());
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
    @GeyserEventHandler(priority = EventHandler.PRIORITY.HIGH)
    public void onGeyserStart(GeyserStartEvent event) {
        Edition edition = registeredEditions.get(config.getEdition());

        if (edition == null) {
            getLogger().error(String.format("Invalid Edition '%s'. Plugin disabled.", config.getEdition()));
            return;
        }

        InetSocketAddress address = GeyserConnector.getInstance().getBedrockServer().getBindAddress();

        try {
            Field bedrockServer = GeyserConnector.class.getDeclaredField("bedrockServer");
            bedrockServer.setAccessible(true);

            ReversionServer server = edition.createReversionServer(GeyserConnector.getInstance().getBedrockServer().getBindAddress());
            for (Class<? extends BaseTranslator> translatorClass : getRegisteredTranslators()) {
                try {
                    server.registerTranslator(translatorClass);
                    getLogger().debug("Registered Translator: " + translatorClass.getSimpleName());
                } catch (TranslatorException e) {
                    getLogger().error(String.format("Unable to register Translator: %s", translatorClass.getName()), e);
                }
            }

            GeyserConnector.getInstance().getBedrockServer().close();
            bedrockServer.set(GeyserConnector.getInstance(), server);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().error(String.format("Unable to set Edition '%s'. Plugin disabled.", config.getEdition()), e);
        }

        GeyserConnector.getInstance().getBedrockServer().bind().whenComplete((avoid, throwable) -> {
            if (throwable != null) {
                getLogger().severe(LanguageUtils.getLocaleStringLog("geyser.core.fail", address.getAddress().toString(), address.getPort()));
                throwable.printStackTrace();
            }
        }).join();
    }
}
