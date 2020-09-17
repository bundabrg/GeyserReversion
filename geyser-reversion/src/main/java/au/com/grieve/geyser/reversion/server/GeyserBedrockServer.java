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

import au.com.grieve.reversion.api.ReversionServer;
import com.nukkitx.protocol.bedrock.BedrockServer;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Provides a Facade BedrockServer to Geyser
 */
@Getter
public class GeyserBedrockServer extends BedrockServer {
    protected final BedrockServer original;

    // List of Reversion Servers
    protected final Set<ReversionServer> servers = new HashSet<>();

    // Default Server (follows Geyser settings)
    protected ReversionServer defaultServer;

    public GeyserBedrockServer(BedrockServer original) {
        super(original.getBindAddress());

        this.original = original;
    }

    public void registerServer(ReversionServer server, boolean isDefault) {
        servers.add(server);
        if (isDefault) {
            defaultServer = server;
        }
    }

}
