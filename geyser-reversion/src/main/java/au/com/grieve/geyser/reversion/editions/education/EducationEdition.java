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

package au.com.grieve.geyser.reversion.editions.education;

import au.com.grieve.geyser.reversion.GeyserReversionExtension;
import au.com.grieve.geyser.reversion.api.Edition;
import au.com.grieve.geyser.reversion.editions.bedrock.handlers.BedrockServerEventHandler;
import au.com.grieve.geyser.reversion.editions.education.commands.EducationCommand;
import au.com.grieve.reversion.api.RegisteredTranslator;
import au.com.grieve.reversion.api.ReversionServer;
import au.com.grieve.reversion.editions.bedrock.BedrockRegisteredTranslator;
import au.com.grieve.reversion.editions.education.EducationReversionServer;
import au.com.grieve.reversion.editions.education.utils.TokenManager;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.annotations.GeyserEventHandler;
import org.geysermc.connector.event.events.geyser.GeyserStartEvent;
import org.geysermc.connector.network.BedrockProtocol;

import java.io.File;
import java.net.InetSocketAddress;

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
        EducationReversionServer server = new EducationReversionServer(BedrockProtocol.DEFAULT_BEDROCK_CODEC, tokenManager, address);
        server.setHandler(new BedrockServerEventHandler(GeyserConnector.getInstance()));

        for (RegisteredTranslator translator : extension.getRegisteredTranslators()) {
            if (translator instanceof BedrockRegisteredTranslator) {
                server.registerTranslator((BedrockRegisteredTranslator) translator);
            }
            extension.getLogger().debug("Registered Translator: " + translator.getName());
        }

        return server;
    }
}
