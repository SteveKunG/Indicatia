package com.stevekung.indicatia.event;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
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
            RenderSystem.enableDepthTest();
            RenderSystem.defaultBlendFunc();
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
                        float xOffset = this.mc.func_228018_at_().getScaledWidth() - 2 - this.mc.fontRenderer.getStringWidth(value);
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
        MatrixStack stack = event.getMatrixStack();
        IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.func_228455_a_(Tessellator.getInstance().getBuffer());
        float health = entity.getHealth();
        boolean halfHealth = health <= entity.getMaxHealth() / 2F;
        boolean halfHealth1 = health <= entity.getMaxHealth() / 4F;
        double maxDistance = 32.0D;
        double distance = this.mc.getRenderManager().func_229099_b_(entity);
        boolean flag = ExtendedConfig.INSTANCE.healthStatusMode != HealthStatusMode.DISABLED && (ExtendedConfig.INSTANCE.healthStatusMode != HealthStatusMode.POINTED || entity == InfoUtils.INSTANCE.extendedPointedEntity);
        Style color = halfHealth ? JsonUtils.RED : halfHealth1 ? JsonUtils.DARK_RED : JsonUtils.GREEN;

        if (!(distance > maxDistance * maxDistance))
        {
            if (!this.mc.gameSettings.hideGUI && !entity.isInvisible() && flag && !(entity instanceof ClientPlayerEntity || entity instanceof ArmorStandEntity) && !InfoUtils.INSTANCE.isHypixel())
            {
                String heartText = JsonUtils.create("\u2764 ").setStyle(color).getFormattedText() + String.format("%.1f", health);
                float height = entity.getHeight() + 0.5F;
                stack.func_227860_a_();//push
                stack.func_227861_a_(0.0D, height, 0.0D);//translate
                stack.func_227863_a_(this.mc.getRenderManager().func_229098_b_());
                stack.func_227862_a_(-0.025F, -0.025F, 0.025F);//size
                Matrix4f matrix4f = stack.func_227866_c_().func_227870_a_();
                float textBackgroundOpacity = Minecraft.getInstance().gameSettings.func_216840_a(0.25F);
                int textColor = (int)(textBackgroundOpacity * 255.0F) << 24;
                FontRenderer fontrenderer = this.mc.getRenderManager().getFontRenderer();
                float textX = -fontrenderer.getStringWidth(heartText) / 2;
                fontrenderer.func_228079_a_(heartText, textX, 0, 553648127, false, matrix4f, irendertypebuffer$impl, false, textColor, 0);
                stack.func_227865_b_();//pop
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
        BlockPos playerPos = new BlockPos(mc.getRenderViewEntity().func_226277_ct_(), mc.getRenderViewEntity().getBoundingBox().minY, mc.getRenderViewEntity().func_226281_cx_());

        if (ExtendedConfig.INSTANCE.fps)
        {
            int fps = Minecraft.debugFPS;
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
            String biomeName = mc.world.func_226691_t_(playerPos).getDisplayName().getFormattedText();
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