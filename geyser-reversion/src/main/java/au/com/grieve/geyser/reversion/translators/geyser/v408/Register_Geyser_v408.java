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

package au.com.grieve.geyser.reversion.translators.geyser.v408;

import au.com.grieve.geyser.reversion.translators.geyser.v408.handlers.StartGameHandler_Geyser_v408;
import au.com.grieve.reversion.editions.bedrock.BedrockRegisteredTranslator;
import au.com.grieve.reversion.editions.bedrock.BedrockTranslator;
import au.com.grieve.reversion.editions.bedrock.handlers.LevelChunkHandler_Bedrock;
import au.com.grieve.reversion.editions.bedrock.handlers.UpdateBlockHandler_Bedrock;
import au.com.grieve.reversion.editions.bedrock.mappers.BlockMapper;
import au.com.grieve.reversion.editions.bedrock.mappers.EntityMapper;
import au.com.grieve.reversion.editions.bedrock.mappers.ItemMapper;
import com.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket;
import com.nukkitx.protocol.bedrock.v408.Bedrock_v408;

public class Register_Geyser_v408 {
    public static BedrockRegisteredTranslator TRANSLATOR = BedrockRegisteredTranslator.builder()
            .fromEdition("bedrock")
            .fromProtocolVersion(408)
            .toEdition("geyser-bedrock")
            .toProtocolVersion(408)
            .codec(Bedrock_v408.V408_CODEC)
            .translator(BedrockTranslator.class)
            .blockMapper(BlockMapper.builder()
                    .palette(() -> Register_Geyser_v408.class.getResourceAsStream("/protocol/bedrock-v408/blockpalette.nbt"))
                    .build()
            )
            .itemMapper(ItemMapper.DEFAULT)
            .entityMapper(EntityMapper.DEFAULT)
            .registerPacketHandler(LevelChunkPacket.class, LevelChunkHandler_Bedrock.class)
            .registerPacketHandler(StartGamePacket.class, StartGameHandler_Geyser_v408.class)
            .registerPacketHandler(UpdateBlockPacket.class, UpdateBlockHandler_Bedrock.class)
            .build();
}
