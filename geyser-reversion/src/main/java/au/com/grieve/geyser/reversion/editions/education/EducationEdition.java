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

import au.com.grieve.geyser.reversion.api.Edition;
import au.com.grieve.geyser.reversion.editions.bedrock.handlers.BedrockServerEventHandler;
import au.com.grieve.geyser.reversion.editions.education.commands.EducationCommand;
import au.com.grieve.reversion.ReversionServer;
import au.com.grieve.reversion.editions.education.EducationReversionServer;
import au.com.grieve.reversion.editions.education.utils.TokenManager;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.annotations.GeyserEventHandler;
import org.geysermc.connector.event.events.geyser.GeyserStartEvent;
import org.geysermc.connector.network.BedrockProtocol;
import org.geysermc.connector.plugin.GeyserPlugin;

import java.io.File;
import java.net.InetSocketAddress;

@Getter
public class EducationEdition implements Edition {
    private final GeyserPlugin plugin;
    private final TokenManager tokenManager;

    public EducationEdition(GeyserPlugin plugin) {
        this.plugin = plugin;

        this.tokenManager = new TokenManager(new File(plugin.getDataFolder(), "tokens.yml"));

        // Register Events
        plugin.registerEvents(this);
    }

    @GeyserEventHandler
    public void onGeyserStart(GeyserStartEvent event) {
        // Register Education command
        GeyserConnector.getInstance().getBootstrap().getGeyserCommandManager().registerCommand(
                new EducationCommand("education", "Education Commands", "geyser.command.education", this));
    }

    @Override
    public ReversionServer createReversionServer(InetSocketAddress address) {
        plugin.getLogger().info("EducationServer listening on " + address.toString());
        ReversionServer server = new EducationReversionServer("bedrock", BedrockProtocol.DEFAULT_BEDROCK_CODEC.getProtocolVersion(), tokenManager, address);
        server.setHandler(new BedrockServerEventHandler(GeyserConnector.getInstance()));
        return server;
    }
}
