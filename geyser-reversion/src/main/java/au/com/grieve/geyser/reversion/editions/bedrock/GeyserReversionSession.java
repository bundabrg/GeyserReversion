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

import au.com.grieve.reversion.editions.bedrock.BedrockReversionServer;
import au.com.grieve.reversion.editions.bedrock.BedrockReversionSession;
import com.nukkitx.network.raknet.RakNetSession;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.wrapper.BedrockWrapperSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.EventLoop;
import org.geysermc.connector.network.BedrockProtocol;

public class GeyserReversionSession extends BedrockReversionSession {
    public GeyserReversionSession(BedrockReversionServer server, RakNetSession connection, EventLoop eventLoop, BedrockWrapperSerializer serializer) {
        super(server, connection, eventLoop, serializer);
    }

    @Override
    public boolean toServer(BedrockPacket packet) {
        // Isolate Reversion protocol from Geyser Protocol in case there are overlapping differences
        ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();

        getPacketCodec().tryEncode(buffer, packet);
        BedrockPacket convertedPacket = BedrockProtocol.DEFAULT_BEDROCK_CODEC.tryDecode(buffer, packet.getPacketId());

        return super.toServer(convertedPacket);
    }

    @Override
    public void sendPacket(BedrockPacket packet) {
        // Isolate Reversion protocol from Geyser Protocol in case there are overlapping differences
        ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();

        BedrockProtocol.DEFAULT_BEDROCK_CODEC.tryEncode(buffer, packet);
        BedrockPacket convertedPacket = getPacketCodec().tryDecode(buffer, packet.getPacketId());

        super.sendPacket(convertedPacket);
    }


}
