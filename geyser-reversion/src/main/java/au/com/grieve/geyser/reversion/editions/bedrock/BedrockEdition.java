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

package au.com.grieve.geyser.reversion.editions.bedrock;

import au.com.grieve.geyser.reversion.api.Edition;
import au.com.grieve.geyser.reversion.editions.bedrock.handlers.BedrockServerEventHandler;
import au.com.grieve.reversion.ReversionServer;
import au.com.grieve.reversion.editions.bedrock.BedrockReversionServer;
import lombok.RequiredArgsConstructor;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.BedrockProtocol;
import org.geysermc.connector.plugin.GeyserPlugin;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class BedrockEdition implements Edition {
    private final GeyserPlugin plugin;

    @Override
    public ReversionServer createReversionServer(InetSocketAddress address) {
        plugin.getLogger().info("BedrockServer listening on " + address.toString());
        ReversionServer server = new BedrockReversionServer("bedrock", BedrockProtocol.DEFAULT_BEDROCK_CODEC.getProtocolVersion(), address);
        server.setHandler(new BedrockServerEventHandler(GeyserConnector.getInstance()));
        return server;
    }
}
