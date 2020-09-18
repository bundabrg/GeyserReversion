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

import au.com.grieve.reversion.editions.bedrock.BedrockReversionSession;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.geysermc.connector.network.BedrockProtocol;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GeyserServerSession extends BedrockServerSession {
    protected final BedrockReversionSession reversionSession;

    public GeyserServerSession(BedrockReversionSession reversionSession) {
        super(null, null, null);

        this.reversionSession = reversionSession;

    }

    @Override
    public void sendPacket(BedrockPacket original) {
        // Isolate Reversion protocol from Geyser Protocol in case there are overlapping differences
        ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();


        System.err.println("original: " + original + " - " + BedrockProtocol.DEFAULT_BEDROCK_CODEC.getId(original.getClass()));
        BedrockProtocol.DEFAULT_BEDROCK_CODEC.tryEncode(buffer, original);
        au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.BedrockPacket translated = reversionSession.getServer().getToCodec().tryDecode(buffer, BedrockProtocol.DEFAULT_BEDROCK_CODEC.getId(original.getClass()));

        reversionSession.sendPacket(translated);
    }

    @Override
    public void sendPacketImmediately(BedrockPacket original) {
        // Isolate Reversion protocol from Geyser Protocol in case there are overlapping differences
        ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();


        System.err.println("original: " + original + " - " + BedrockProtocol.DEFAULT_BEDROCK_CODEC.getId(original.getClass()));
        BedrockProtocol.DEFAULT_BEDROCK_CODEC.tryEncode(buffer, original);
        au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.BedrockPacket translated = reversionSession.getServer().getToCodec().tryDecode(buffer, BedrockProtocol.DEFAULT_BEDROCK_CODEC.getId(original.getClass()));

        reversionSession.sendPacketImmediately(translated);
    }

    @Override
    public boolean isClosed() {
        return reversionSession.isClosed();
    }

    @Override
    public void disconnect(@Nullable String reason, boolean hideReason) {
        System.err.println("Diconnect: " + reason);
        super.disconnect(reason, hideReason);
    }
}
