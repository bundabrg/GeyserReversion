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

package au.com.grieve.geyser.reversion.editions.bedrock;

import au.com.grieve.geyser.reversion.GeyserReversionExtension;
import au.com.grieve.geyser.reversion.api.Edition;
import au.com.grieve.geyser.reversion.editions.bedrock.handlers.BedrockEditionServerEventHandler;
import au.com.grieve.reversion.Build;
import au.com.grieve.reversion.api.RegisteredTranslator;
import au.com.grieve.reversion.api.ReversionServer;
import au.com.grieve.reversion.editions.bedrock.BedrockRegisteredTranslator;
import au.com.grieve.reversion.editions.bedrock.BedrockReversionServer;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.BedrockPacketCodec;
import lombok.RequiredArgsConstructor;
import org.geysermc.connector.network.BedrockProtocol;

import java.net.InetSocketAddress;
import java.util.Arrays;

@RequiredArgsConstructor
public class BedrockEdition implements Edition {
    private final GeyserReversionExtension extension;

    @Override
    public ReversionServer createReversionServer(InetSocketAddress address) {
        extension.getLogger().info("BedrockServer listening on " + address.toString());

        BedrockPacketCodec defaultCodec = Arrays.stream(Build.PROTOCOLS)
                .filter(p -> p.getProtocolVersion() == BedrockProtocol.DEFAULT_BEDROCK_CODEC.getProtocolVersion())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unsupported Geyser"));

        BedrockReversionServer server = new BedrockReversionServer(defaultCodec, address);
        server.setHandler(new BedrockEditionServerEventHandler(extension));

        for (RegisteredTranslator translator : extension.getRegisteredTranslators()) {
            if (translator instanceof BedrockRegisteredTranslator) {
                server.registerTranslator((BedrockRegisteredTranslator) translator);
            }
            extension.getLogger().debug("Registered Translator: " + translator.getName());
        }

        return server;
    }
}
