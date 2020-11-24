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

package au.com.grieve.geyser.reversion.editions.bedrock.handlers;

import au.com.grieve.geyser.reversion.GeyserReversionExtension;
import au.com.grieve.geyser.reversion.server.GeyserServerSession;
import au.com.grieve.geyser.reversion.server.GeyserUpstreamPacketHandler;
import au.com.grieve.reversion.editions.bedrock.BedrockReversionSession;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.BedrockPong;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.BedrockServerEventHandler;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.BedrockServerSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import lombok.Getter;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.InetSocketAddress;

@Getter
@ParametersAreNonnullByDefault
public class BedrockEditionServerEventHandler implements BedrockServerEventHandler {

    private final GeyserReversionExtension extension;

    public BedrockEditionServerEventHandler(GeyserReversionExtension extension) {
        this.extension = extension;
    }


    @Override
    public boolean onConnectionRequest(InetSocketAddress inetSocketAddress) {
        return extension.getServer().getHandler().onConnectionRequest(inetSocketAddress);
    }

    @Override
    public BedrockPong onQuery(InetSocketAddress inetSocketAddress) {
        com.nukkitx.protocol.bedrock.BedrockPong originalPong = extension.getServer().getHandler().onQuery(inetSocketAddress);
        if (originalPong == null) {
            return null;
        }

        BedrockPong translatedPong = new BedrockPong();
        translatedPong.setEdition(originalPong.getEdition());
        translatedPong.setExtras(originalPong.getExtras());
        translatedPong.setGameType(originalPong.getGameType());
        translatedPong.setIpv4Port(originalPong.getIpv4Port());
        translatedPong.setIpv6Port(originalPong.getIpv6Port());
        translatedPong.setMaximumPlayerCount(originalPong.getMaximumPlayerCount());
        translatedPong.setMotd(originalPong.getMotd());
        translatedPong.setProtocolVersion(originalPong.getProtocolVersion());
        translatedPong.setSubMotd(originalPong.getSubMotd());
        translatedPong.setVersion(originalPong.getVersion());
        return translatedPong;
    }

    @Override
    public void onSessionCreation(BedrockServerSession bedrockServerSession) {
        bedrockServerSession.setLogging(true);
        GeyserServerSession facadeSession = new GeyserServerSession((BedrockReversionSession) bedrockServerSession);
        GeyserSession geyserSession = new GeyserSession(GeyserConnector.getInstance(), facadeSession);

        facadeSession.setPacketHandler(new GeyserUpstreamPacketHandler((BedrockReversionSession) bedrockServerSession, GeyserConnector.getInstance(), geyserSession));
        bedrockServerSession.setPacketHandler(new BedrockEditionUpstreamPacketHandler((BedrockReversionSession) bedrockServerSession,
                geyserSession, facadeSession));
    }

    @Override
    public void onUnhandledDatagram(ChannelHandlerContext ctx, DatagramPacket packet) {
        extension.getServer().getHandler().onUnhandledDatagram(ctx, packet);
    }
}
