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
import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.geysermc.connector.network.BedrockProtocol;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.crypto.SecretKey;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.function.Consumer;

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

        BedrockProtocol.DEFAULT_BEDROCK_CODEC.tryEncode(buffer, original);
        au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.BedrockPacket translated = reversionSession.getServer().getToCodec().tryDecode(buffer, BedrockProtocol.DEFAULT_BEDROCK_CODEC.getId(original.getClass()), reversionSession);
        buffer.release();
        reversionSession.sendPacket(translated);
    }

    @Override
    public void sendPacketImmediately(BedrockPacket original) {
        // Isolate Reversion protocol from Geyser Protocol in case there are overlapping differences
        ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();

        BedrockProtocol.DEFAULT_BEDROCK_CODEC.tryEncode(buffer, original);
        au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.BedrockPacket translated = reversionSession.getServer().getToCodec().tryDecode(buffer, BedrockProtocol.DEFAULT_BEDROCK_CODEC.getId(original.getClass()), reversionSession);
        buffer.release();

        reversionSession.sendPacketImmediately(translated);
    }

    @Override
    public boolean isClosed() {
        return reversionSession.isClosed();
    }

//    @Override
//    public void setPacketHandler(@Nonnull BedrockPacketHandler packetHandler) {
//        throw new UnsupportedOperationException();
//    }

//    @Override
//    public void setPacketCodec(BedrockPacketCodec packetCodec) {
//        throw new UnsupportedOperationException();
//    }

    @Override
    public void sendWrapped(Collection<BedrockPacket> packets, boolean encrypt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void sendWrapped(ByteBuf compressed, boolean encrypt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void enableEncryption(@Nonnull SecretKey secretKey) {
        reversionSession.enableEncryption(secretKey);
    }

    @Override
    public boolean isEncrypted() {
        return reversionSession.isEncrypted();
    }

    @Override
    public InetSocketAddress getAddress() {
        return reversionSession.getAddress();
    }

//    @Override
//    public BedrockPacketCodec getPacketCodec() {
//        throw new UnsupportedOperationException();
//    }

//    @Override
//    public BedrockPacketHandler getPacketHandler() {
//        throw new UnsupportedOperationException();
//    }

//    @Override
//    public BatchHandler getBatchHandler() {
//        throw new UnsupportedOperationException();
//    }

//    @Override
//    public void setBatchHandler(BatchHandler batchHandler) {
//        throw new UnsupportedOperationException();
//    }

    @Override
    public int getCompressionLevel() {
        return reversionSession.getCompressionLevel();
    }

    @Override
    public void setCompressionLevel(int compressionLevel) {
        reversionSession.setCompressionLevel(compressionLevel);
    }

    @Override
    public boolean isLogging() {
        return reversionSession.isLogging();
    }

    @Override
    public void setLogging(boolean logging) {
        reversionSession.setLogging(logging);
    }

    @Override
    public void addDisconnectHandler(Consumer<DisconnectReason> disconnectHandler) {
        reversionSession.addDisconnectHandler(disconnectHandler);
    }

    @Override
    public long getLatency() {
        return reversionSession.getLatency();
    }

    @Override
    public void disconnect(@Nullable String reason, boolean hideReason) {
        reversionSession.disconnect(reason, hideReason);
    }
}
