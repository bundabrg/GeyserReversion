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
import com.nukkitx.protocol.bedrock.v408.Bedrock_v408;
import com.nukkitx.protocol.bedrock.wrapper.BedrockWrapperSerializer;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;

import java.net.InetSocketAddress;

public class GeyserReversionServer extends BedrockReversionServer {
    public GeyserReversionServer(String toEdition, int toCodec, InetSocketAddress address) {
        super(toEdition, Bedrock_v408.V408_CODEC, address);
    }

    public GeyserReversionServer(String toEdition, int toCodec, InetSocketAddress address, int maxThreads) {
        super(toEdition, Bedrock_v408.V408_CODEC, address, maxThreads);
    }

    public GeyserReversionServer(String toEdition, int toCodec, InetSocketAddress address, int maxThreads, EventLoopGroup eventLoopGroup) {
        super(toEdition, Bedrock_v408.V408_CODEC, address, maxThreads, eventLoopGroup);
    }

    @Override
    public BedrockReversionSession createSession(RakNetSession connection, EventLoop eventLoop, BedrockWrapperSerializer serializer) {
        return new GeyserReversionSession(this, connection, eventLoop, serializer);
    }
}
