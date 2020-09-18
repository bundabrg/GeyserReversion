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

package au.com.grieve.geyser.reversion.editions.education;

import au.com.grieve.geyser.reversion.GeyserReversionExtension;
import au.com.grieve.geyser.reversion.api.Edition;
import au.com.grieve.geyser.reversion.editions.bedrock.handlers.BedrockEditionServerEventHandler;
import au.com.grieve.geyser.reversion.editions.education.commands.EducationCommand;
import au.com.grieve.reversion.Build;
import au.com.grieve.reversion.api.RegisteredTranslator;
import au.com.grieve.reversion.api.ReversionServer;
import au.com.grieve.reversion.editions.bedrock.BedrockRegisteredTranslator;
import au.com.grieve.reversion.editions.education.EducationReversionServer;
import au.com.grieve.reversion.editions.education.utils.TokenManager;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.BedrockPacketCodec;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.annotations.GeyserEventHandler;
import org.geysermc.connector.event.events.geyser.GeyserStartEvent;
import org.geysermc.connector.network.BedrockProtocol;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Arrays;

@Getter
public class EducationEdition implements Edition {
    private final GeyserReversionExtension extension;
    private final TokenManager tokenManager;

    public EducationEdition(GeyserReversionExtension extension) {
        this.extension = extension;

        this.tokenManager = new TokenManager(new File(extension.getDataFolder(), "tokens.yml"));

        // Register Events
        extension.registerEvents(this);
    }

    @GeyserEventHandler
    public void onGeyserStart(GeyserStartEvent event) {
        // Register Education command
        GeyserConnector.getInstance().getBootstrap().getGeyserCommandManager().registerCommand(
                new EducationCommand("education", "Education Commands", "geyser.command.education", this));
    }

    @Override
    public ReversionServer createReversionServer(InetSocketAddress address) {
        extension.getLogger().info("EducationServer listening on " + address.toString());

        BedrockPacketCodec defaultCodec = Arrays.stream(Build.PROTOCOLS)
                .filter(p -> p.getProtocolVersion() == BedrockProtocol.DEFAULT_BEDROCK_CODEC.getProtocolVersion())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unsupported Geyser"));

        EducationReversionServer server = new EducationReversionServer(defaultCodec, tokenManager, address);
        server.setHandler(new BedrockEditionServerEventHandler(extension));

        for (RegisteredTranslator translator : extension.getRegisteredTranslators()) {
            if (translator instanceof BedrockRegisteredTranslator) {
                server.registerTranslator((BedrockRegisteredTranslator) translator);
            }
            extension.getLogger().debug("Registered Translator: " + translator.getName());
        }

        return server;
    }
}
