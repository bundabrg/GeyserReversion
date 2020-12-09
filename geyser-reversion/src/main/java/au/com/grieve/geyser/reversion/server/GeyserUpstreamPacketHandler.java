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

package au.com.grieve.geyser.reversion.server;

import au.com.grieve.geyser.reversion.GeyserReversionExtension;
import au.com.grieve.reversion.api.LoginData;
import au.com.grieve.reversion.api.Translator;
import au.com.grieve.reversion.editions.bedrock.BedrockReversionSession;
import au.com.grieve.reversion.exceptions.LoginException;
import com.fasterxml.jackson.databind.JsonNode;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket;
import com.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket;
import com.nukkitx.protocol.bedrock.packet.ServerToClientHandshakePacket;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.event.EventResult;
import org.geysermc.connector.event.events.packet.UpstreamPacketReceiveEvent;
import org.geysermc.connector.event.events.packet.upstream.LoginPacketReceive;
import org.geysermc.connector.network.BedrockProtocol;
import org.geysermc.connector.network.UpstreamPacketHandler;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.session.auth.AuthData;
import org.geysermc.connector.network.session.auth.BedrockClientData;
import org.geysermc.connector.utils.LanguageUtils;
import org.geysermc.connector.utils.ResourcePack;
import org.geysermc.connector.utils.ResourcePackManifest;

import java.util.UUID;

public class GeyserUpstreamPacketHandler extends UpstreamPacketHandler {
    private final BedrockReversionSession serverSession;

    public GeyserUpstreamPacketHandler(BedrockReversionSession serverSession, GeyserConnector connector, GeyserSession session) {
        super(connector, session);

        this.serverSession = serverSession;
    }

    @Override
    public boolean handle(LoginPacket loginPacket) {
        EventResult<LoginPacketReceive> result = connector.getEventManager().triggerEvent(UpstreamPacketReceiveEvent.of(session, loginPacket));
        if (result.isCancelled()) {
            return true;
        }

        loginPacket = result.getEvent().getPacket();

        // Check that we support the codec
        BedrockPacketCodec packetCodec = BedrockProtocol.getBedrockCodec(loginPacket.getProtocolVersion());
        if (packetCodec == null) {
            if (loginPacket.getProtocolVersion() > BedrockProtocol.DEFAULT_BEDROCK_CODEC.getProtocolVersion()) {
                // Too early to determine session locale
                session.getConnector().getLogger().info(LanguageUtils.getLocaleStringLog("geyser.network.outdated.server", BedrockProtocol.DEFAULT_BEDROCK_CODEC.getMinecraftVersion()));
                session.disconnect(LanguageUtils.getLocaleStringLog("geyser.network.outdated.server", BedrockProtocol.DEFAULT_BEDROCK_CODEC.getMinecraftVersion()));
                return true;
            } else if (loginPacket.getProtocolVersion() < BedrockProtocol.DEFAULT_BEDROCK_CODEC.getProtocolVersion()) {
                session.getConnector().getLogger().info(LanguageUtils.getLocaleStringLog("geyser.network.outdated.client", BedrockProtocol.DEFAULT_BEDROCK_CODEC.getMinecraftVersion()));
                session.disconnect(LanguageUtils.getLocaleStringLog("geyser.network.outdated.client", BedrockProtocol.DEFAULT_BEDROCK_CODEC.getMinecraftVersion()));
                return true;
            }
        }

        // Provide some debug about our translation chain
        Translator translator = serverSession.getTranslator();
        if (translator != null) {
            GeyserReversionExtension.getInstance().getLogger().debug("Translator Chain:");
            while (translator != null) {
                GeyserReversionExtension.getInstance().getLogger().debug("  " + translator);
                translator = translator.getDownstreamTranslator();
            }
        }

        // Encrypt Connection
        serverSession.enableEncryption(serverSession.getLoginData().getEncryptionKey());

        try {
            ServerToClientHandshakePacket packet = new ServerToClientHandshakePacket();
            packet.setJwt(serverSession.getLoginData().getHandshakeJwt().serialize());
            session.sendUpstreamPacketImmediately(packet);
        } catch (LoginException e) {
            session.disconnect("You are not able to connect. Please make sure your account is authorized to connect or contact the server administrator.");
            session.getConnector().getLogger().error("Failed to encrypt connection: " + e.getMessage());
            return true;
        }

        // Setup Session
        JsonNode extraData = serverSession.getLoginData().getPayload().get("extraData");
        session.setAuthenticationData(new AuthData(
                extraData.get("displayName").asText(),
                UUID.fromString(extraData.get("identity").asText()),
                extraData.get("XUID").asText().isEmpty() ? String.valueOf(UUID.fromString(extraData.get("identity").asText()).hashCode()) : extraData.get("XUID").asText()
        ));

        serverSession.getLoginData();
        session.setClientData(LoginData.JSON_MAPPER.convertValue(serverSession.getLoginData().getClientData(), BedrockClientData.class));

        PlayStatusPacket playStatus = new PlayStatusPacket();
        playStatus.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
        session.sendUpstreamPacket(playStatus);

        ResourcePacksInfoPacket resourcePacksInfo = new ResourcePacksInfoPacket();
        for (ResourcePack resourcePack : ResourcePack.PACKS.values()) {
            ResourcePackManifest.Header header = resourcePack.getManifest().getHeader();
            resourcePacksInfo.getResourcePackInfos().add(new ResourcePacksInfoPacket.Entry(header.getUuid().toString(), header.getVersionString(), resourcePack.getFile().length(), "", "", "", false, false));
        }
        resourcePacksInfo.setForcedToAccept(GeyserConnector.getInstance().getConfig().isForceResourcePacks());
        session.sendUpstreamPacket(resourcePacksInfo);
        return true;
    }
}
