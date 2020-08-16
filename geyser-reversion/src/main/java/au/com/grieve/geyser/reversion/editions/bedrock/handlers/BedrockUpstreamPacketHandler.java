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

import au.com.grieve.reversion.LoginData;
import au.com.grieve.reversion.ReversionServerSession;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket;
import com.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket;
import com.nukkitx.protocol.bedrock.packet.ServerToClientHandshakePacket;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.BedrockProtocol;
import org.geysermc.connector.network.UpstreamPacketHandler;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.utils.LanguageUtils;


public class BedrockUpstreamPacketHandler extends UpstreamPacketHandler {
    private final ReversionServerSession serverSession;

    public BedrockUpstreamPacketHandler(ReversionServerSession serverSession, GeyserConnector connector, GeyserSession session) {
        super(connector, session);
        this.serverSession = serverSession;
    }

    @Override
    public boolean handle(LoginPacket loginPacket) {
        // Check that we support the codec
        BedrockPacketCodec packetCodec = BedrockProtocol.getBedrockCodec(loginPacket.getProtocolVersion());
        if (packetCodec == null) {
            if (loginPacket.getProtocolVersion() > BedrockProtocol.DEFAULT_BEDROCK_CODEC.getProtocolVersion()) {
                this.session.disconnect(LanguageUtils.getLocaleStringLog("geyser.network.outdated.server", BedrockProtocol.DEFAULT_BEDROCK_CODEC.getMinecraftVersion()));
                return true;
            }

            if (loginPacket.getProtocolVersion() < BedrockProtocol.DEFAULT_BEDROCK_CODEC.getProtocolVersion()) {
                this.session.disconnect(LanguageUtils.getLocaleStringLog("geyser.network.outdated.client", BedrockProtocol.DEFAULT_BEDROCK_CODEC.getMinecraftVersion()));
                return true;
            }
        }

        // Encrypt Connection
        serverSession.enableEncryption(serverSession.getLoginData().getEncryptionKey());

        try {
            ServerToClientHandshakePacket packet = new ServerToClientHandshakePacket();
            packet.setJwt(serverSession.getLoginData().getHandshakeJwt().serialize());
            session.sendUpstreamPacketImmediately(packet);
        } catch (LoginData.LoginException e) {
            session.disconnect("disconnectionScreen.internalError.cantConnect");
            session.getConnector().getLogger().error("Failed to encrypt connection", e);
        }

        PlayStatusPacket playStatus = new PlayStatusPacket();
        playStatus.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
        session.sendUpstreamPacket(playStatus);

        ResourcePacksInfoPacket resourcePacksInfo = new ResourcePacksInfoPacket();
        session.sendUpstreamPacket(resourcePacksInfo);
        return true;
    }
}
