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

package au.com.grieve.geyser.reversion.editions.bedrock.handlers;

import au.com.grieve.reversion.ReversionServerSession;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.ConnectorServerEventHandler;
import org.geysermc.connector.network.session.GeyserSession;

@Getter
public class BedrockServerEventHandler extends ConnectorServerEventHandler {
    private final GeyserConnector connector;

    public BedrockServerEventHandler(GeyserConnector connector) {
        super(connector);
        this.connector = connector;
    }

    @Override
    public void onSessionCreation(BedrockServerSession bedrockServerSession) {
        bedrockServerSession.setLogging(true);
        bedrockServerSession.setPacketHandler(new BedrockUpstreamPacketHandler((ReversionServerSession) bedrockServerSession, this.connector, new GeyserSession(this.connector, bedrockServerSession)));
    }
}
