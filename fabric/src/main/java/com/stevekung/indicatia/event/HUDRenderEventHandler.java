package com.stevekung.indicatia.event;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.core.IndicatiaFabric;
import com.stevekung.indicatia.gui.exconfig.screens.OffsetRenderPreviewScreen;
import com.stevekung.indicatia.hud.EffectOverlays;
import com.stevekung.indicatia.hud.EquipmentOverlays;
import com.stevekung.indicatia.hud.InfoOverlays;
import com.stevekung.indicatia.hud.InfoUtils;
import com.stevekung.indicatia.utils.event.InfoOverlayEvents;
import com.stevekung.indicatia.utils.hud.InfoOverlay;
import com.stevekung.stevekungslib.utils.LangUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.level.ChunkPos;

public class HUDRenderEventHandler
{
    public static final HUDRenderEventHandler INSTANCE = new HUDRenderEventHandler();

    public void onClientTick(ServerLevel level)
    {
        InfoOverlays.getTPS(level.getServer());
    }

    public void onPreInfoRender(Minecraft mc, PoseStack poseStack)
    {
        if (!mc.options.renderDebug && !mc.options.hideGui)
        {
            if (IndicatiaFabric.CONFIG.general.enableRenderInfo && mc.player != null && mc.level != null && !(mc.screen instanceof OffsetRenderPreviewScreen))
            {
                var iLeft = 0;
                var iRight = 0;

                for (var info : HUDRenderEventHandler.getInfoOverlays(mc))
                {
                    if (info == null || info.isEmpty())
                    {
                        continue;
                    }

                    var collection = mc.player.getActiveEffects();
                    var goodCount = (int) Ordering.natural().reverse().sortedCopy(collection).stream().filter(mobEffectInstance -> mobEffectInstance.showIcon() && (mobEffectInstance.getEffect().isBeneficial() || mobEffectInstance.getEffect().getCategory() == MobEffectCategory.NEUTRAL)).count();
                    var badCount = (int) Ordering.natural().reverse().sortedCopy(collection).stream().filter(mobEffectInstance -> mobEffectInstance.showIcon() && !mobEffectInstance.getEffect().isBeneficial()).count();
                    var state = 0;

                    if (goodCount > 0)
                    {
                        state = 1;
                    }
                    if (badCount > 0)
                    {
                        state = 2;
                    }

                    var value = info.toFormatted();
                    var pos = info.getPos();
                    var defaultPos = 3.0625F;
                    var fontHeight = mc.font.lineHeight + 1;
                    var yOffset = 3 + fontHeight * (pos == InfoOverlay.Position.LEFT ? iLeft : iRight);

                    if (pos == InfoOverlay.Position.RIGHT && !IndicatiaSettings.INSTANCE.swapRenderInfo || pos == InfoOverlay.Position.LEFT && IndicatiaSettings.INSTANCE.swapRenderInfo)
                    {
                        yOffset += state == 1 ? 24 : state == 2 ? 49 : 0;
                    }

                    var xOffset = mc.getWindow().getGuiScaledWidth() - 2 - mc.font.width(value.getString());
                    mc.font.drawShadow(poseStack, value, pos == InfoOverlay.Position.LEFT ? !IndicatiaSettings.INSTANCE.swapRenderInfo ? defaultPos : xOffset : pos == InfoOverlay.Position.RIGHT ? !IndicatiaSettings.INSTANCE.swapRenderInfo ? xOffset : defaultPos : defaultPos, yOffset, 16777215);

                    if (pos == InfoOverlay.Position.LEFT)
                    {
                        ++iLeft;
                    }
                    else
                    {
                        ++iRight;
                    }
                }
            }

            if (!mc.player.isSpectator() && IndicatiaSettings.INSTANCE.equipmentHUD)
            {
                if (IndicatiaSettings.INSTANCE.equipmentPosition == Equipments.Position.HOTBAR)
                {
                    EquipmentOverlays.renderHotbarEquippedItems(mc, poseStack);
                }
                else
                {
                    if (IndicatiaSettings.INSTANCE.equipmentDirection == Equipments.Direction.VERTICAL)
                    {
                        EquipmentOverlays.renderVerticalEquippedItems(mc, poseStack);
                    }
                    else
                    {
                        EquipmentOverlays.renderHorizontalEquippedItems(mc, poseStack);
                    }
                }
            }

            if (IndicatiaSettings.INSTANCE.potionHUD)
            {
                EffectOverlays.renderPotionHUD(mc, poseStack);
            }
        }
    }

    public void onLoggedOut()
    {
        InfoOverlays.OVERALL_TPS = InfoOverlay.empty();
        InfoOverlays.OVERWORLD_TPS = InfoOverlay.empty();
        InfoOverlays.TPS = InfoOverlay.empty();
        InfoOverlays.ALL_TPS.clear();
    }

    public static List<InfoOverlay> getInfoOverlays(Minecraft mc)
    {
        var infos = Lists.<InfoOverlay>newArrayList();
        var playerPos = new BlockPos(mc.getCameraEntity().getX(), mc.getCameraEntity().getBoundingBox().minY, mc.getCameraEntity().getZ());

        if (IndicatiaSettings.INSTANCE.fps)
        {
            var fps = Minecraft.fps;
            infos.add(new InfoOverlay("hud.fps", String.valueOf(fps), IndicatiaSettings.INSTANCE.fpsColor, fps <= 25 ? IndicatiaSettings.INSTANCE.fpsLow25Color : fps <= 49 ? IndicatiaSettings.INSTANCE.fps26And49Color : IndicatiaSettings.INSTANCE.fpsValueColor, InfoOverlay.Position.LEFT));
        }

        if (!mc.hasSingleplayerServer())
        {
            if (IndicatiaSettings.INSTANCE.ping)
            {
                var responseTime = InfoUtils.INSTANCE.getPing();
                infos.add(new InfoOverlay("hud.ping", responseTime + "ms", IndicatiaSettings.INSTANCE.pingColor, InfoUtils.INSTANCE.getResponseTimeColor(responseTime), InfoOverlay.Position.LEFT));

                if (IndicatiaSettings.INSTANCE.pingToSecond)
                {
                    var responseTimeSecond = InfoUtils.INSTANCE.getPing() / 1000.0D;
                    infos.add(new InfoOverlay("hud.ping.delay", responseTimeSecond + "s", IndicatiaSettings.INSTANCE.pingToSecondColor, InfoUtils.INSTANCE.getResponseTimeColor((int) (responseTimeSecond * 1000.0D)), InfoOverlay.Position.LEFT));
                }
            }
            if (IndicatiaSettings.INSTANCE.serverIP && mc.getCurrentServer() != null)
            {
                infos.add(new InfoOverlay("IP", (mc.isConnectedToRealms() ? "Realms Server" : mc.getCurrentServer().ip) + (IndicatiaSettings.INSTANCE.serverIPMCVersion ? "/" + Minecraft.getInstance().getVersionType() : ""), IndicatiaSettings.INSTANCE.serverIPColor, IndicatiaSettings.INSTANCE.serverIPValueColor, InfoOverlay.Position.LEFT));
            }
        }

        if (IndicatiaSettings.INSTANCE.xyz)
        {
            var stringPos = playerPos.getX() + " " + playerPos.getY() + " " + playerPos.getZ();
            var nether = mc.player.level.dimensionType().piglinSafe() ? "Nether " : "";
            infos.add(new InfoOverlay(nether + "XYZ", stringPos, IndicatiaSettings.INSTANCE.xyzColor, IndicatiaSettings.INSTANCE.xyzValueColor, InfoOverlay.Position.LEFT));

            if (mc.player.level.dimensionType().piglinSafe())
            {
                var stringNetherPos = playerPos.getX() * 8 + " " + playerPos.getY() + " " + playerPos.getZ() * 8;
                infos.add(new InfoOverlay("Overworld XYZ", stringNetherPos, IndicatiaSettings.INSTANCE.xyzColor, IndicatiaSettings.INSTANCE.xyzValueColor, InfoOverlay.Position.LEFT));
            }
        }

        if (IndicatiaSettings.INSTANCE.direction)
        {
            infos.add(InfoOverlays.getDirection(mc));
        }

        if (IndicatiaSettings.INSTANCE.biome)
        {
            var chunkPos = new ChunkPos(playerPos);
            var worldChunk = mc.level.getChunk(chunkPos.x, chunkPos.z);
            var biomeResource = mc.level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(mc.level.getBiome(playerPos));
            var biomeName = "biome." + biomeResource.getNamespace() + "." + biomeResource.getPath();
            infos.add(new InfoOverlay("hud.biome", !worldChunk.isEmpty() ? new TranslatableComponent(biomeName).getString() : LangUtils.translate("hud.biome.waiting_for_chunk").getString(), IndicatiaSettings.INSTANCE.biomeColor, IndicatiaSettings.INSTANCE.biomeValueColor, InfoOverlay.Position.LEFT));
        }

        if (IndicatiaSettings.INSTANCE.slimeChunkFinder && !mc.player.level.dimensionType().piglinSafe())
        {
            infos.add(new InfoOverlay("hud.slime_chunk", InfoUtils.INSTANCE.isSlimeChunk(mc.player.blockPosition()) ? "gui.yes" : "gui.no", IndicatiaSettings.INSTANCE.slimeChunkColor, IndicatiaSettings.INSTANCE.slimeChunkValueColor, InfoOverlay.Position.LEFT));
        }

        if (IndicatiaSettings.INSTANCE.tps)
        {
            infos.add(InfoOverlays.OVERALL_TPS);
            infos.add(InfoOverlays.OVERWORLD_TPS);
            infos.addAll(InfoOverlays.ALL_TPS);
            infos.add(InfoOverlays.TPS);
        }

        if (IndicatiaSettings.INSTANCE.realTime)
        {
            infos.add(InfoOverlays.getRealWorldTime());
        }
        if (IndicatiaSettings.INSTANCE.gameTime)
        {
            infos.add(InfoOverlays.getGameTime(mc));
        }
        if (IndicatiaSettings.INSTANCE.gameWeather && mc.level.isRaining())
        {
            var weather = !mc.level.isThundering() ? "hud.weather.raining" : "hud.weather.thundering";
            infos.add(new InfoOverlay("hud.weather", weather, IndicatiaSettings.INSTANCE.gameWeatherColor, IndicatiaSettings.INSTANCE.gameWeatherValueColor, InfoOverlay.Position.RIGHT));
        }
        if (IndicatiaSettings.INSTANCE.moonPhase)
        {
            infos.add(new InfoOverlay("hud.moon_phase", InfoUtils.INSTANCE.getMoonPhase(mc), IndicatiaSettings.INSTANCE.moonPhaseColor, IndicatiaSettings.INSTANCE.moonPhaseValueColor, InfoOverlay.Position.RIGHT));
        }
        InfoOverlayEvents.INFO_OVERLAY.invoker().addInfos(infos);
        return infos;
    }
}