package com.stevekung.indicatia.event;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.config.HealthStatusMode;
import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.core.IndicatiaMod;
import com.stevekung.indicatia.gui.exconfig.screen.OffsetRenderPreviewScreen;
import com.stevekung.indicatia.hud.*;
import com.stevekung.stevekungslib.utils.JsonUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.versions.mcp.MCPVersion;

public class HUDRenderEventHandler
{
    private final Minecraft mc;
    public static final DecimalFormat TPS_FORMAT = new DecimalFormat("########0.00");
    public static String currentLiveViewCount;

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
                InfoOverlays.getTPS(ServerLifecycleHooks.getCurrentServer());
            }

            if (IndicatiaMod.isYoutubeChatLoaded)
            {
                try
                {
                    Class<?> clazz = Class.forName("stevekung.mods.ytchat.utils.YouTubeChatService");
                    HUDRenderEventHandler.currentLiveViewCount = (String)clazz.getField("currentLiveViewCount").get(null);
                }
                catch (Exception e)
                {
                    HUDRenderEventHandler.currentLiveViewCount = TextFormatting.RED + "unavailable";
                }
            }
        }
    }

    @SubscribeEvent
    public void onPreInfoRender(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSHEALTH)
        {
            event.setCanceled(!IndicatiaConfig.GENERAL.enableRenderBossHealthStatus.get());
            GlStateManager.enableDepthTest();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR || event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
        {
            if (this.mc.currentScreen instanceof OffsetRenderPreviewScreen)
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

                        String value = info.toString();
                        InfoOverlay.Position pos = info.getPos();
                        float defaultPos = 3.0625F;
                        float fontHeight = this.mc.fontRenderer.FONT_HEIGHT + 1;
                        float yOffset = 3 + fontHeight * (pos == InfoOverlay.Position.LEFT ? iLeft : iRight);
                        float xOffset = this.mc.mainWindow.getScaledWidth() - 2 - this.mc.fontRenderer.getStringWidth(value);
                        this.mc.fontRenderer.drawStringWithShadow(value, pos == InfoOverlay.Position.LEFT ? !ExtendedConfig.INSTANCE.swapRenderInfo ? defaultPos : xOffset : pos == InfoOverlay.Position.RIGHT ? !ExtendedConfig.INSTANCE.swapRenderInfo ? xOffset : defaultPos : defaultPos, yOffset, 16777215);

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
                        EquipmentOverlays.renderHotbarEquippedItems(this.mc);
                    }
                    else
                    {
                        if (ExtendedConfig.INSTANCE.equipmentDirection == Equipments.Direction.VERTICAL)
                        {
                            EquipmentOverlays.renderVerticalEquippedItems(this.mc);
                        }
                        else
                        {
                            EquipmentOverlays.renderHorizontalEquippedItems(this.mc);
                        }
                    }
                }

                if (ExtendedConfig.INSTANCE.potionHUD)
                {
                    EffectOverlays.renderPotionHUD(this.mc);
                }
            }
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.CHAT)
        {
            if (this.mc.currentScreen instanceof OffsetRenderPreviewScreen)
            {
                event.setCanceled(true);
                return;
            }
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS)
        {
            if (!IndicatiaConfig.GENERAL.enableVanillaPotionHUD.get() || this.mc.currentScreen instanceof OffsetRenderPreviewScreen)
            {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onRenderHealthStatus(RenderLivingEvent.Specials.Post<LivingEntity, EntityModel<LivingEntity>> event)
    {
        LivingEntity entity = event.getEntity();
        float health = entity.getHealth();
        boolean halfHealth = health <= entity.getMaxHealth() / 2F;
        boolean halfHealth1 = health <= entity.getMaxHealth() / 4F;
        double maxDistance = 32.0D;
        double distance = entity.getDistanceSq(this.mc.getRenderManager().info.getProjectedView());
        boolean flag = ExtendedConfig.INSTANCE.healthStatusMode != HealthStatusMode.DISABLED && (ExtendedConfig.INSTANCE.healthStatusMode != HealthStatusMode.POINTED || entity == InfoUtils.INSTANCE.extendedPointedEntity);
        Style color = halfHealth ? JsonUtils.RED : halfHealth1 ? JsonUtils.DARK_RED : JsonUtils.GREEN;

        if (!(distance > maxDistance * maxDistance))
        {
            if (!this.mc.gameSettings.hideGUI && !entity.isInvisible() && flag && !(entity instanceof ClientPlayerEntity || entity instanceof ArmorStandEntity) && !InfoUtils.INSTANCE.isHypixel())
            {
                String heart = JsonUtils.create("\u2764 ").setStyle(color).getFormattedText();
                GameRenderer.drawNameplate(this.mc.fontRenderer, heart + String.format("%.1f", health), (float)event.getX(), (float)event.getY(), (float)event.getZ(), 0, this.mc.getRenderManager().playerViewY, this.mc.getRenderManager().playerViewX, entity.shouldRenderSneaking());
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
        BlockPos playerPos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getBoundingBox().minY, mc.getRenderViewEntity().posZ);

        if (ExtendedConfig.INSTANCE.fps)
        {
            int fps = Minecraft.getDebugFPS();
            infos.add(new InfoOverlay("FPS", String.valueOf(fps), ExtendedConfig.INSTANCE.fpsColor, fps <= 25 ? ExtendedConfig.INSTANCE.fpsLow25Color : fps >= 26 && fps <= 49 ? ExtendedConfig.INSTANCE.fps26And49Color : ExtendedConfig.INSTANCE.fpsValueColor, InfoOverlay.Position.LEFT));
        }

        if (!mc.isSingleplayer())
        {
            if (ExtendedConfig.INSTANCE.ping)
            {
                int responseTime = InfoUtils.INSTANCE.getPing();
                infos.add(new InfoOverlay("Ping", responseTime + "ms", ExtendedConfig.INSTANCE.pingColor, InfoUtils.INSTANCE.getResponseTimeColor(responseTime), InfoOverlay.Position.RIGHT));

                if (ExtendedConfig.INSTANCE.pingToSecond)
                {
                    double responseTimeSecond = InfoUtils.INSTANCE.getPing() / 1000.0D;
                    infos.add(new InfoOverlay("Delay", responseTimeSecond + "s", ExtendedConfig.INSTANCE.pingToSecondColor, InfoUtils.INSTANCE.getResponseTimeColor((int)(responseTimeSecond * 1000.0D)), InfoOverlay.Position.RIGHT));
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
            String nether = mc.player.dimension == DimensionType.THE_NETHER ? "Nether " : "";
            infos.add(new InfoOverlay(nether + "XYZ", stringPos, ExtendedConfig.INSTANCE.xyzColor, ExtendedConfig.INSTANCE.xyzValueColor, InfoOverlay.Position.LEFT));

            if (mc.player.dimension == DimensionType.THE_NETHER)
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
            String biomeName = worldChunk.getBiome(playerPos).getDisplayName().getFormattedText();
            infos.add(new InfoOverlay("Biome", !worldChunk.isEmpty() ? biomeName : "Waiting for chunk...", ExtendedConfig.INSTANCE.biomeColor, ExtendedConfig.INSTANCE.biomeValueColor, InfoOverlay.Position.LEFT));
        }

        if (ExtendedConfig.INSTANCE.slimeChunkFinder && mc.player.dimension == DimensionType.OVERWORLD)
        {
            infos.add(new InfoOverlay("Slime Chunk", InfoUtils.INSTANCE.isSlimeChunk(mc.player.getPosition()) ? "Yes" : "No", ExtendedConfig.INSTANCE.slimeChunkColor, ExtendedConfig.INSTANCE.slimeChunkValueColor, InfoOverlay.Position.LEFT));
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
            String weather = !mc.world.isThundering() ? "Raining" : "Thundering";
            infos.add(new InfoOverlay("Weather", weather, ExtendedConfig.INSTANCE.gameWeatherColor, ExtendedConfig.INSTANCE.gameWeatherValueColor, InfoOverlay.Position.RIGHT));
        }
        if (ExtendedConfig.INSTANCE.moonPhase)
        {
            infos.add(new InfoOverlay("Moon Phase", InfoUtils.INSTANCE.getMoonPhase(mc), ExtendedConfig.INSTANCE.moonPhaseColor, ExtendedConfig.INSTANCE.moonPhaseValueColor, InfoOverlay.Position.RIGHT));
        }

        if (IndicatiaMod.isYoutubeChatLoaded && !StringUtils.isNullOrEmpty(HUDRenderEventHandler.currentLiveViewCount))
        {
            infos.add(new InfoOverlay("Current watched", HUDRenderEventHandler.currentLiveViewCount, ExtendedConfig.INSTANCE.ytChatViewCountColor, ExtendedConfig.INSTANCE.ytChatViewCountValueColor, InfoOverlay.Position.RIGHT));
        }
        return infos;
    }
}