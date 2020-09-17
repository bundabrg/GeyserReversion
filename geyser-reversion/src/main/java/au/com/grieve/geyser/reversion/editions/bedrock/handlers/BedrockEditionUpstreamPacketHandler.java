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

import au.com.grieve.geyser.reversion.server.GeyserServerSession;
import au.com.grieve.reversion.api.LoginData;
import au.com.grieve.reversion.editions.bedrock.BedrockReversionSession;
import au.com.grieve.reversion.exceptions.LoginException;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.BedrockPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.AddBehaviorTreePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.AddEntityPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.AddHangingEntityPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.AddItemEntityPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.AddPaintingPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.AddPlayerPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.AdventureSettingsPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.AnimateEntityPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.AnimatePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.AnvilDamagePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.AutomationClientConnectPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.AvailableCommandsPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.AvailableEntityIdentifiersPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.BiomeDefinitionListPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.BlockEntityDataPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.BlockEventPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.BlockPickRequestPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.BookEditPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.BossEventPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.CameraPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.CameraShakePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ChangeDimensionPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ChunkRadiusUpdatedPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ClientCacheBlobStatusPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ClientCacheMissResponsePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ClientCacheStatusPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ClientToServerHandshakePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ClientboundMapItemDataPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.CodeBuilderPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.CommandBlockUpdatePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.CommandOutputPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.CommandRequestPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.CompletedUsingItemPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ContainerClosePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ContainerOpenPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ContainerSetDataPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.CorrectPlayerMovePredictionPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.CraftingDataPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.CraftingEventPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.CreativeContentPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.DebugInfoPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.DisconnectPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.EducationSettingsPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.EmoteListPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.EmotePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.EntityEventPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.EntityFallPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.EntityPickRequestPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.EventPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ExplodePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.GameRulesChangedPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.GuiDataPickItemPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.HurtArmorPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.InteractPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.InventoryContentPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.InventorySlotPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.InventoryTransactionPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ItemFrameDropItemPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ItemStackRequestPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ItemStackResponsePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.LabTablePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.LecternUpdatePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.LevelChunkPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.LevelEventGenericPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.LevelEventPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.LevelSoundEvent1Packet;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.LevelSoundEvent2Packet;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.LevelSoundEventPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.LoginPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.MapCreateLockedCopyPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.MapInfoRequestPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.MobArmorEquipmentPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.MobEffectPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.MobEquipmentPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ModalFormRequestPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ModalFormResponsePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.MoveEntityAbsolutePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.MoveEntityDeltaPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.MovePlayerPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.MultiplayerSettingsPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.NetworkChunkPublisherUpdatePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.NetworkSettingsPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.NetworkStackLatencyPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.NpcRequestPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.OnScreenTextureAnimationPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PacketViolationWarningPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PhotoTransferPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PlaySoundPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PlayerActionPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PlayerArmorDamagePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PlayerAuthInputPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PlayerEnchantOptionsPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PlayerFogPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PlayerHotbarPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PlayerInputPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PlayerListPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PlayerSkinPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PositionTrackingDBClientRequestPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PositionTrackingDBServerBroadcastPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PurchaseReceiptPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.RemoveEntityPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.RemoveObjectivePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.RequestChunkRadiusPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ResourcePackChunkDataPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ResourcePackChunkRequestPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ResourcePackClientResponsePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ResourcePackDataInfoPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ResourcePackStackPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.RespawnPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.RiderJumpPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ScriptCustomEventPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ServerSettingsRequestPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ServerSettingsResponsePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetCommandsEnabledPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetDefaultGameTypePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetDifficultyPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetDisplayObjectivePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetEntityDataPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetEntityLinkPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetEntityMotionPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetEntityMotionPlusPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetHealthPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetLastHurtByPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetLocalPlayerAsInitializedPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetPlayerGameTypePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetScorePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetScoreboardIdentityPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetSpawnPositionPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetTimePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SetTitlePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SettingsCommandPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ShowCreditsPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ShowProfilePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ShowStoreOfferPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SimpleEventPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SpawnExperienceOrbPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SpawnParticleEffectPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.StartGamePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.StopSoundPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.StructureBlockUpdatePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.StructureTemplateDataRequestPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.StructureTemplateDataResponsePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.SubClientLoginPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.TakeItemEntityPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.TextPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.TickSyncPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.TransferPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.UpdateAttributesPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.UpdateBlockPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.UpdateBlockPropertiesPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.UpdateBlockSyncedPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.UpdateEquipPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.UpdatePlayerGameTypePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.UpdateSoftEnumPacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.UpdateTradePacket;
import au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.VideoStreamConnectPacket;
import com.fasterxml.jackson.databind.JsonNode;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket;
import com.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket;
import com.nukkitx.protocol.bedrock.packet.ServerToClientHandshakePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.Getter;
import org.geysermc.connector.network.BedrockProtocol;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.connector.network.session.auth.AuthData;
import org.geysermc.connector.network.session.auth.BedrockClientData;
import org.geysermc.connector.utils.LanguageUtils;

import java.util.UUID;


@Getter
public class BedrockEditionUpstreamPacketHandler implements BedrockPacketHandler {
    private final BedrockReversionSession serverSession;
    private final GeyserSession geyserSession;
    private final GeyserServerSession facadeSession;

    public BedrockEditionUpstreamPacketHandler(BedrockReversionSession serverSession, GeyserSession session, GeyserServerSession facadeSession) {
        this.serverSession = serverSession;
        this.geyserSession = session;
        this.facadeSession = facadeSession;
    }

    @Override
    public boolean handle(LoginPacket loginPacket) {
        // Check that we support the codec
        BedrockPacketCodec packetCodec = BedrockProtocol.getBedrockCodec(loginPacket.getProtocolVersion());
        if (packetCodec == null) {
            if (loginPacket.getProtocolVersion() > BedrockProtocol.DEFAULT_BEDROCK_CODEC.getProtocolVersion()) {
                geyserSession.disconnect(LanguageUtils.getLocaleStringLog("geyser.network.outdated.server", BedrockProtocol.DEFAULT_BEDROCK_CODEC.getMinecraftVersion()));
                return true;
            }

            if (loginPacket.getProtocolVersion() < BedrockProtocol.DEFAULT_BEDROCK_CODEC.getProtocolVersion()) {
                geyserSession.disconnect(LanguageUtils.getLocaleStringLog("geyser.network.outdated.client", BedrockProtocol.DEFAULT_BEDROCK_CODEC.getMinecraftVersion()));
                return true;
            }
        }

        // Encrypt Connection
        serverSession.enableEncryption(serverSession.getLoginData().getEncryptionKey());

        try {
            ServerToClientHandshakePacket packet = new ServerToClientHandshakePacket();
            packet.setJwt(serverSession.getLoginData().getHandshakeJwt().serialize());
            geyserSession.sendUpstreamPacketImmediately(packet);
        } catch (LoginException e) {
            geyserSession.disconnect("You are not able to connect. Please make sure your account is authorized to connect or contact the server administrator.");
            geyserSession.getConnector().getLogger().error("Failed to encrypt connection: " + e.getMessage());
            return true;
        }

        // Setup Session
        JsonNode extraData = serverSession.getLoginData().getPayload().get("extraData");
        geyserSession.setAuthenticationData(new AuthData(
                extraData.get("displayName").asText(),
                UUID.fromString(extraData.get("identity").asText()),
                extraData.get("XUID").asText()
        ));

        serverSession.getLoginData();
        geyserSession.setClientData(LoginData.JSON_MAPPER.convertValue(serverSession.getLoginData().getClientData(), BedrockClientData.class));

        PlayStatusPacket playStatus = new PlayStatusPacket();
        playStatus.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
        geyserSession.sendUpstreamPacket(playStatus);

        ResourcePacksInfoPacket resourcePacksInfo = new ResourcePacksInfoPacket();
        geyserSession.sendUpstreamPacket(resourcePacksInfo);
        return true;
    }

    public boolean handlePacket(BedrockPacket original) {
        // Isolate Reversion protocol from Geyser Protocol in case there are overlapping differences
        ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();

        serverSession.getServer().getToCodec().tryEncode(buffer, original);
        com.nukkitx.protocol.bedrock.BedrockPacket translated = BedrockProtocol.DEFAULT_BEDROCK_CODEC.tryDecode(buffer, original.getPacketId());

        return translated.handle(facadeSession.getPacketHandler());
    }

    @Override
    public boolean handle(AdventureSettingsPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(AnimatePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(AnvilDamagePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(AvailableEntityIdentifiersPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(BlockEntityDataPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(BlockPickRequestPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(BookEditPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ClientCacheBlobStatusPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ClientCacheMissResponsePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ClientCacheStatusPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ClientToServerHandshakePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(CommandBlockUpdatePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(CommandRequestPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(CompletedUsingItemPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ContainerClosePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(CraftingEventPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(EducationSettingsPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(EmotePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(EntityEventPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(EntityFallPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(EntityPickRequestPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(EventPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(InteractPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(InventoryContentPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(InventorySlotPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(InventoryTransactionPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ItemFrameDropItemPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(LabTablePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(LecternUpdatePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(LevelEventGenericPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(LevelSoundEvent1Packet packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(LevelSoundEventPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(MapInfoRequestPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(MobArmorEquipmentPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(MobEquipmentPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ModalFormResponsePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(MoveEntityAbsolutePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(MovePlayerPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(MultiplayerSettingsPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(NetworkStackLatencyPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(PhotoTransferPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(PlayerActionPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(PlayerAuthInputPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(PlayerHotbarPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(PlayerInputPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(PlayerSkinPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(PurchaseReceiptPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(RequestChunkRadiusPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ResourcePackChunkRequestPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ResourcePackClientResponsePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(RiderJumpPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ServerSettingsRequestPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetDefaultGameTypePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetLocalPlayerAsInitializedPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetPlayerGameTypePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SubClientLoginPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(AddBehaviorTreePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(AddEntityPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(AddHangingEntityPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(AddItemEntityPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(AddPaintingPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(AddPlayerPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(AvailableCommandsPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(BlockEventPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(BossEventPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(CameraPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ChangeDimensionPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ChunkRadiusUpdatedPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ClientboundMapItemDataPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(CommandOutputPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ContainerOpenPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ContainerSetDataPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(CraftingDataPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(DisconnectPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ExplodePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(LevelChunkPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(GameRulesChangedPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(GuiDataPickItemPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(HurtArmorPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(AutomationClientConnectPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(LevelEventPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(MapCreateLockedCopyPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(MobEffectPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ModalFormRequestPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(MoveEntityDeltaPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(NetworkSettingsPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(NpcRequestPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(OnScreenTextureAnimationPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(PlayerListPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(PlaySoundPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.PlayStatusPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(RemoveEntityPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(RemoveObjectivePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ResourcePackChunkDataPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ResourcePackDataInfoPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ResourcePackStackPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(RespawnPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ScriptCustomEventPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ServerSettingsResponsePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(au.com.grieve.reversion.shaded.nukkitx.protocol.bedrock.packet.ServerToClientHandshakePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetCommandsEnabledPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetDifficultyPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetDisplayObjectivePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetEntityDataPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetEntityLinkPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetEntityMotionPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetHealthPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetLastHurtByPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetScoreboardIdentityPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetScorePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetSpawnPositionPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetTimePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SettingsCommandPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetTitlePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ShowCreditsPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ShowProfilePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ShowStoreOfferPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SimpleEventPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SpawnExperienceOrbPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SpawnParticleEffectPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(StartGamePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(StopSoundPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(StructureBlockUpdatePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(StructureTemplateDataRequestPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(StructureTemplateDataResponsePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(TakeItemEntityPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(TextPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(TickSyncPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(TransferPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(UpdateAttributesPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(UpdateBlockPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(UpdateBlockPropertiesPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(UpdateBlockSyncedPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(UpdateEquipPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(UpdateSoftEnumPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(UpdateTradePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(BiomeDefinitionListPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(LevelSoundEvent2Packet packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(NetworkChunkPublisherUpdatePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(VideoStreamConnectPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(CodeBuilderPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(EmoteListPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ItemStackRequestPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(ItemStackResponsePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(PlayerArmorDamagePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(PlayerEnchantOptionsPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(CreativeContentPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(UpdatePlayerGameTypePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(PositionTrackingDBServerBroadcastPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(PositionTrackingDBClientRequestPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(PacketViolationWarningPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(DebugInfoPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(SetEntityMotionPlusPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(AnimateEntityPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(CameraShakePacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(CorrectPlayerMovePredictionPacket packet) {
        return handlePacket(packet);
    }

    @Override
    public boolean handle(PlayerFogPacket packet) {
        return handlePacket(packet);
    }
}
