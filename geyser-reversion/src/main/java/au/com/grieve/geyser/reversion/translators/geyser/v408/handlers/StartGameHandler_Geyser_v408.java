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

package au.com.grieve.geyser.reversion.translators.geyser.v408.handlers;

import au.com.grieve.geyser.reversion.GeyserReversionExtension;
import au.com.grieve.reversion.editions.bedrock.BedrockTranslator;
import au.com.grieve.reversion.editions.bedrock.handlers.StartGameHandler_Bedrock;
import com.nukkitx.nbt.NbtList;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.packet.StartGamePacket;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
    Geyser re-arranged the block palette which will cause issues with the new baked in palette format.
    What we do here is to re-arrange it back based upon the supported bedrock palette in Geyser and keep a record
    of the mapping so we translate it in other packets
 */
public class StartGameHandler_Geyser_v408 extends StartGameHandler_Bedrock {
    protected boolean initialized = false;

    public StartGameHandler_Geyser_v408(BedrockTranslator translator) {
        super(translator);
    }

    @Override
    public boolean fromDownstream(StartGamePacket packet) {
        // Only grab runtime ID's for the first player
        if (!initialized) {
            initialized = true;

            NbtList<NbtMap> geyserTags = packet.getBlockPalette();

            Map<Integer, NbtMap> translatedTags = IntStream.range(0, getTranslator().getRegisteredTranslator().getBlockMapper().getUpstreamPalette().size())
                    .boxed()
                    .collect(Collectors.toMap(i -> i, i -> getTranslator().getRegisteredTranslator().getBlockMapper().getUpstreamPalette().get(i).getCompound("block")));

            for (int geyserId = 0; geyserId < geyserTags.size(); geyserId++) {
                NbtMap geyserTag = geyserTags.get(geyserId).getCompound("block");
                boolean found = false;
                for (Map.Entry<Integer, NbtMap> entry : translatedTags.entrySet()) {
                    if (geyserTag.equals(entry.getValue())) {
                        getTranslator().getRegisteredTranslator().getBlockMapper().registerRuntimeIdMapping(geyserId, entry.getKey());
                        found = true;
                        translatedTags.remove(entry.getKey());
                        break;
                    }
                }
                if (!found) {
                    GeyserReversionExtension.getInstance().getLogger().error("Unable to find upstream palette entry: " + geyserTag);
                    getTranslator().getRegisteredTranslator().getBlockMapper().registerRuntimeIdMapping(geyserId, 0); // Set to 0 for now
                }
                if (translatedTags.size() == 0) {
                    break;
                }
            }

            if (translatedTags.size() > 0) {
                GeyserReversionExtension.getInstance().getLogger().error("Extra upstream unmatched palette entries: \n" + translatedTags);
            }
        }

        return super.fromDownstream(packet);
    }
}
