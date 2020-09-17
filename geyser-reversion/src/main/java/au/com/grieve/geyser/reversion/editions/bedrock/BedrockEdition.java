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

import au.com.grieve.geyser.reversion.GeyserReversionExtension;
import au.com.grieve.geyser.reversion.api.Edition;
import au.com.grieve.geyser.reversion.editions.bedrock.handlers.BedrockServerEventHandler;
import au.com.grieve.reversion.api.RegisteredTranslator;
import au.com.grieve.reversion.api.ReversionServer;
import au.com.grieve.reversion.editions.bedrock.BedrockRegisteredTranslator;
import lombok.RequiredArgsConstructor;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.BedrockProtocol;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class BedrockEdition implements Edition {
    private final GeyserReversionExtension extension;

    @Override
    public ReversionServer createReversionServer(InetSocketAddress address) {
        extension.getLogger().info("BedrockServer listening on " + address.toString());

        int version = 0;
        try {
            Object toCodec = BedrockProtocol.SUPPORTED_BEDROCK_CODECS.get(BedrockProtocol.SUPPORTED_BEDROCK_CODECS.size() - 1);
            Method getProtocolVersion = toCodec.getClass().getMethod("getProtocolVersion");
            version = (int) getProtocolVersion.invoke(toCodec);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        GeyserReversionServer server = new GeyserReversionServer("bedrock", version, address);
        server.setHandler(new BedrockServerEventHandler(GeyserConnector.getInstance()));

        for (RegisteredTranslator translator : extension.getRegisteredTranslators()) {
            if (translator instanceof BedrockRegisteredTranslator) {
                server.registerTranslator((BedrockRegisteredTranslator) translator);
            }
            extension.getLogger().debug("Registered Translator: " + translator.getName());
        }

        return server;
    }
}
