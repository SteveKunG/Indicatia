package com.stevekung.indicatia.event;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.gui.exconfig.screen.OffsetRenderPreviewScreen;
import com.stevekung.indicatia.hud.*;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.versions.mcp.MCPVersion;

public class HUDRenderEventHandler
{
    private final Minecraft mc;

    public HUDRenderEventHandler()
    {
        this.mc = Minecraft.getInstance();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

            if (server != null)
            {
                InfoOverlays.getTPS(server);
            }
        }
    }

    @SubscribeEvent
    public void onPreInfoRender(RenderGameOverlayEvent.Pre event)
    {
        MatrixStack matrixStack = event.getMatrixStack();

        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSHEALTH)
        {
            event.setCanceled(!IndicatiaConfig.GENERAL.enableRenderBossHealthStatus.get());
            RenderSystem.enableDepthTest();
            RenderSystem.defaultBlendFunc();
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS)
        {
            if (!IndicatiaConfig.GENERAL.enableVanillaPotionHUD.get())
            {
                event.setCanceled(true);
            }
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT)
        {
            if (!this.mc.gameSettings.showDebugInfo)
            {
                if (IndicatiaConfig.GENERAL.enableRenderInfo.get() && this.mc.player != null && this.mc.world != null && !(this.mc.currentScreen instanceof OffsetRenderPreviewScreen))
                {
                    int iLeft = 0;
                    int iRight = 0;

                    for (InfoOverlay info : HUDRenderEventHandler.getInfoOverlays(this.mc))
                    {
                        if (info.isEmpty())
                        {
                            continue;
                        }

                        IFormattableTextComponent value = info.toFormatted();
                        InfoOverlay.Position pos = info.getPos();
                        float defaultPos = 3.0625F;
                        float fontHeight = this.mc.fontRenderer.FONT_HEIGHT + 1;
                        float yOffset = 3 + fontHeight * (pos == InfoOverlay.Position.LEFT ? iLeft : iRight);
                        float xOffset = this.mc.getMainWindow().getScaledWidth() - 2 - this.mc.fontRenderer.getStringWidth(value.getString());
                        this.mc.fontRenderer.func_243246_a(matrixStack, value, pos == InfoOverlay.Position.LEFT ? !ExtendedConfig.INSTANCE.swapRenderInfo ? defaultPos : xOffset : pos == InfoOverlay.Position.RIGHT ? !ExtendedConfig.INSTANCE.swapRenderInfo ? xOffset : defaultPos : defaultPos, yOffset, 16777215);

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

                if (!this.mc.player.isSpectator() && ExtendedConfig.INSTANCE.equipmentHUD)
                {
                    if (ExtendedConfig.INSTANCE.equipmentPosition == Equipments.Position.HOTBAR)
                    {
                        EquipmentOverlays.renderHotbarEquippedItems(this.mc, matrixStack);
                    }
                    else
                    {
                        if (ExtendedConfig.INSTANCE.equipmentDirection == Equipments.Direction.VERTICAL)
                        {
                            EquipmentOverlays.renderVerticalEquippedItems(this.mc, matrixStack);
                        }
                        else
                        {
                            EquipmentOverlays.renderHorizontalEquippedItems(this.mc, matrixStack);
                        }
                    }
                }

                if (ExtendedConfig.INSTANCE.potionHUD)
                {
                    EffectOverlays.renderPotionHUD(this.mc, matrixStack);
                }
            }
        }
        else
        {
            if (this.mc.currentScreen instanceof OffsetRenderPreviewScreen)
            {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event)
    {
        InfoOverlays.OVERALL_TPS = InfoOverlay.empty();
        InfoOverlays.OVERWORLD_TPS = InfoOverlay.empty();
        InfoOverlays.TPS = InfoOverlay.empty();
        InfoOverlays.ALL_TPS.clear();
    }

    public static List<InfoOverlay> getInfoOverlays(Minecraft mc)
    {
        List<InfoOverlay> infos = new ArrayList<>();
        BlockPos playerPos = new BlockPos(mc.getRenderViewEntity().getPosX(), mc.getRenderViewEntity().getBoundingBox().minY, mc.getRenderViewEntity().getPosZ());

        if (ExtendedConfig.INSTANCE.fps)
        {
            int fps = Minecraft.debugFPS;
            infos.add(new InfoOverlay("hud.fps", String.valueOf(fps), ExtendedConfig.INSTANCE.fpsColor, fps <= 25 ? ExtendedConfig.INSTANCE.fpsLow25Color : fps >= 26 && fps <= 49 ? ExtendedConfig.INSTANCE.fps26And49Color : ExtendedConfig.INSTANCE.fpsValueColor, InfoOverlay.Position.LEFT));
        }

        if (!mc.isSingleplayer())
        {
            if (ExtendedConfig.INSTANCE.ping)
            {
                int responseTime = InfoUtils.INSTANCE.getPing();
                infos.add(new InfoOverlay("hud.ping", responseTime + "ms", ExtendedConfig.INSTANCE.pingColor, InfoUtils.INSTANCE.getResponseTimeColor(responseTime), InfoOverlay.Position.RIGHT));

                if (ExtendedConfig.INSTANCE.pingToSecond)
                {
                    double responseTimeSecond = InfoUtils.INSTANCE.getPing() / 1000.0D;
                    infos.add(new InfoOverlay("hud.ping.delay", responseTimeSecond + "s", ExtendedConfig.INSTANCE.pingToSecondColor, InfoUtils.INSTANCE.getResponseTimeColor((int)(responseTimeSecond * 1000.0D)), InfoOverlay.Position.RIGHT));
                }
            }
            if (ExtendedConfig.INSTANCE.serverIP && mc.getCurrentServerData() != null)
            {
                infos.add(new InfoOverlay("IP", (mc.isConnectedToRealms() ? "Realms Server" : mc.getCurrentServerData().serverIP) + (ExtendedConfig.INSTANCE.serverIPMCVersion ? "/" + MCPVersion.getMCVersion() : ""), ExtendedConfig.INSTANCE.serverIPColor, ExtendedConfig.INSTANCE.serverIPValueColor, InfoOverlay.Position.RIGHT));
            }
        }

        if (ExtendedConfig.INSTANCE.xyz)
        {
            String stringPos = playerPos.getX() + " " + playerPos.getY() + " " + playerPos.getZ();
            String nether = mc.player.world.func_230315_m_().func_241509_i_() ? "Nether " : "";
            infos.add(new InfoOverlay(nether + "XYZ", stringPos, ExtendedConfig.INSTANCE.xyzColor, ExtendedConfig.INSTANCE.xyzValueColor, InfoOverlay.Position.LEFT));

            if (mc.player.world.func_230315_m_().func_241509_i_())
            {
                String stringNetherPos = playerPos.getX() * 8 + " " + playerPos.getY() + " " + playerPos.getZ() * 8;
                infos.add(new InfoOverlay("Overworld XYZ", stringNetherPos, ExtendedConfig.INSTANCE.xyzColor, ExtendedConfig.INSTANCE.xyzValueColor, InfoOverlay.Position.LEFT));
            }
        }

        if (ExtendedConfig.INSTANCE.direction)
        {
            infos.add(InfoOverlays.getDirection(mc));
        }

        if (ExtendedConfig.INSTANCE.biome)
        {
            ChunkPos chunkPos = new ChunkPos(playerPos);
            Chunk worldChunk = mc.world.getChunk(chunkPos.x, chunkPos.z);
            ResourceLocation biomeResource = mc.world.func_241828_r().func_243612_b(Registry.BIOME_KEY).getKey(mc.world.getBiome(playerPos));
            String biomeName = "biome." + biomeResource.getNamespace() + "." + biomeResource.getPath();
            infos.add(new InfoOverlay("hud.biome", !worldChunk.isEmpty() ? new TranslationTextComponent(biomeName).getString() : LangUtils.translateComponent("hud.biome.waiting_for_chunk").getString(), ExtendedConfig.INSTANCE.biomeColor, ExtendedConfig.INSTANCE.biomeValueColor, InfoOverlay.Position.LEFT));
        }

        if (ExtendedConfig.INSTANCE.slimeChunkFinder && !mc.player.world.func_230315_m_().func_241509_i_())
        {
            infos.add(new InfoOverlay("hud.slime_chunk", InfoUtils.INSTANCE.isSlimeChunk(mc.player.getPosition()) ? "gui.yes" : "gui.no", ExtendedConfig.INSTANCE.slimeChunkColor, ExtendedConfig.INSTANCE.slimeChunkValueColor, InfoOverlay.Position.LEFT));
        }

        if (ExtendedConfig.INSTANCE.tps)
        {
            infos.add(InfoOverlays.OVERALL_TPS);
            infos.add(InfoOverlays.OVERWORLD_TPS);
            infos.addAll(InfoOverlays.ALL_TPS);
            infos.add(InfoOverlays.TPS);
        }

        if (ExtendedConfig.INSTANCE.realTime)
        {
            infos.add(InfoOverlays.getRealWorldTime());
        }
        if (ExtendedConfig.INSTANCE.gameTime)
        {
            infos.add(InfoOverlays.getGameTime(mc));
        }
        if (ExtendedConfig.INSTANCE.gameWeather && mc.world.isRaining())
        {
            String weather = !mc.world.isThundering() ? "hud.weather.raining" : "hud.weather.thundering";
            infos.add(new InfoOverlay("hud.weather", weather, ExtendedConfig.INSTANCE.gameWeatherColor, ExtendedConfig.INSTANCE.gameWeatherValueColor, InfoOverlay.Position.RIGHT));
        }
        if (ExtendedConfig.INSTANCE.moonPhase)
        {
            infos.add(new InfoOverlay("hud.moon_phase", InfoUtils.INSTANCE.getMoonPhase(mc), ExtendedConfig.INSTANCE.moonPhaseColor, ExtendedConfig.INSTANCE.moonPhaseValueColor, InfoOverlay.Position.RIGHT));
        }
        return infos;
    }
}