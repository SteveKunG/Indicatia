package stevekung.mods.indicatia.event;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.stevekung.stevekungslib.client.event.ClientEventHandler;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.client.GLConstants;

import joptsimple.internal.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import stevekung.mods.indicatia.config.*;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.gui.exconfig.screen.RenderPreviewScreen;
import stevekung.mods.indicatia.renderer.HUDInfo;
import stevekung.mods.indicatia.utils.InfoUtils;

public class HUDRenderEventHandler
{
    private final Minecraft mc;
    public static final DecimalFormat TPS = new DecimalFormat("########0.00");
    public static String currentLiveViewCount;
    private static String overallTPS;
    private static String overworldTPS;
    private static String tps;
    private static final List<String> ALL_TPS = new ArrayList<>();

    public HUDRenderEventHandler()
    {
        this.mc = Minecraft.getInstance();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
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
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR || event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
        {
            if (this.mc.currentScreen instanceof RenderPreviewScreen)
            {
                event.setCanceled(true);
            }
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT)
        {
            if (IndicatiaConfig.GENERAL.enableRenderInfo.get() && !this.mc.gameSettings.showDebugInfo && this.mc.player != null && this.mc.world != null && !(this.mc.currentScreen instanceof RenderPreviewScreen))
            {
                List<String> leftInfo = new ArrayList<>();
                List<String> rightInfo = new ArrayList<>();
                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

                // left info
                if (ExtendedConfig.INSTANCE.ping && !this.mc.isSingleplayer())
                {
                    leftInfo.add(HUDInfo.getPing());

                    if (ExtendedConfig.INSTANCE.pingToSecond)
                    {
                        leftInfo.add(HUDInfo.getPingToSecond());
                    }
                }
                if (ExtendedConfig.INSTANCE.serverIP && !this.mc.isSingleplayer())
                {
                    if (this.mc.isConnectedToRealms())
                    {
                        leftInfo.add("Realms Server");
                    }
                    if (this.mc.getCurrentServerData() != null)
                    {
                        leftInfo.add(HUDInfo.getServerIP(this.mc));
                    }
                }
                if (ExtendedConfig.INSTANCE.fps)
                {
                    leftInfo.add(HUDInfo.getFPS());
                }
                if (ExtendedConfig.INSTANCE.xyz)
                {
                    leftInfo.add(HUDInfo.getXYZ(this.mc));

                    if (this.mc.player.dimension == DimensionType.THE_NETHER)
                    {
                        leftInfo.add(HUDInfo.getOverworldXYZFromNether(this.mc));
                    }
                }
                if (ExtendedConfig.INSTANCE.direction)
                {
                    leftInfo.add(HUDInfo.renderDirection(this.mc));
                }
                if (ExtendedConfig.INSTANCE.biome)
                {
                    leftInfo.add(HUDInfo.getBiome(this.mc));
                }
                if (ExtendedConfig.INSTANCE.slimeChunkFinder && this.mc.player.dimension == DimensionType.OVERWORLD)
                {
                    String isSlimeChunk = InfoUtils.INSTANCE.isSlimeChunk(this.mc.player.getPosition()) ? "Yes" : "No";
                    leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.slimeChunkColor).toColoredFont() + "Slime Chunk: " + ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.slimeChunkValueColor).toColoredFont() + isSlimeChunk);
                }
                if (ExtendedConfig.INSTANCE.cpsPosition == CPSPosition.LEFT)
                {
                    if (ExtendedConfig.INSTANCE.cps)
                    {
                        leftInfo.add(HUDInfo.getCPS());
                    }
                    if (ExtendedConfig.INSTANCE.rcps)
                    {
                        leftInfo.add(HUDInfo.getRCPS());
                    }
                }
                // server tps
                if (ExtendedConfig.INSTANCE.tps && server != null)
                {
                    if (ClientEventHandler.ticks % 20 == 0)
                    {
                        double overallTPS = HUDRenderEventHandler.mean(server.tickTimeArray) * 1.0E-6D;
                        double overworldTPS = HUDRenderEventHandler.mean(server.getTickTime(DimensionType.OVERWORLD)) * 1.0E-6D;
                        double tps = Math.min(1000.0D / overallTPS, 20);

                        HUDRenderEventHandler.overallTPS = HUDRenderEventHandler.TPS.format(overallTPS);
                        HUDRenderEventHandler.overworldTPS = "";
                        HUDRenderEventHandler.ALL_TPS.clear();

                        if (ExtendedConfig.INSTANCE.tpsAllDims)
                        {
                            for (DimensionType dimension : DimensionType.getAll())
                            {
                                long[] values = server.getTickTime(dimension);
                                String dimensionName = DimensionType.getKey(dimension).toString();

                                if (values == null)
                                {
                                    return;
                                }
                                double dimensionTPS = HUDRenderEventHandler.mean(values) * 1.0E-6D;
                                HUDRenderEventHandler.ALL_TPS.add(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.tpsColor).toColoredFont() + "Dimension " + dimensionName.substring(dimensionName.indexOf(":") + 1) + ": " + ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.tpsValueColor).toColoredFont() + HUDRenderEventHandler.TPS.format(dimensionTPS));
                            }
                        }
                        else
                        {
                            HUDRenderEventHandler.overworldTPS = HUDRenderEventHandler.TPS.format(overworldTPS);
                        }
                        HUDRenderEventHandler.tps = HUDRenderEventHandler.TPS.format(tps);
                    }
                    // overall tps
                    leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.tpsColor).toColoredFont() + "Overall TPS: " + ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.tpsValueColor).toColoredFont() + HUDRenderEventHandler.overallTPS);
                    // all dimension tps
                    leftInfo.addAll(HUDRenderEventHandler.ALL_TPS);

                    // overworld tps
                    if (!HUDRenderEventHandler.overworldTPS.isEmpty())
                    {
                        leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.tpsColor).toColoredFont() + "Overworld TPS: " + ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.tpsValueColor).toColoredFont() + HUDRenderEventHandler.overworldTPS);
                    }

                    // tps
                    leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.tpsColor).toColoredFont() + "TPS: " + ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.tpsValueColor).toColoredFont() + HUDRenderEventHandler.tps);
                }

                // right info
                if (ExtendedConfig.INSTANCE.realTime)
                {
                    rightInfo.add(HUDInfo.getCurrentTime());
                }
                if (ExtendedConfig.INSTANCE.gameTime)
                {
                    rightInfo.add(HUDInfo.getCurrentGameTime(this.mc));
                }
                if (ExtendedConfig.INSTANCE.gameWeather && this.mc.world.isRaining())
                {
                    rightInfo.add(HUDInfo.getGameWeather(this.mc));
                }
                if (ExtendedConfig.INSTANCE.moonPhase)
                {
                    rightInfo.add(InfoUtils.INSTANCE.getMoonPhase(this.mc));
                }
                if (ExtendedConfig.INSTANCE.cpsPosition == CPSPosition.RIGHT)
                {
                    if (ExtendedConfig.INSTANCE.cps)
                    {
                        rightInfo.add(HUDInfo.getCPS());
                    }
                    if (ExtendedConfig.INSTANCE.rcps)
                    {
                        rightInfo.add(HUDInfo.getRCPS());
                    }
                }
                if (IndicatiaMod.isYoutubeChatLoaded && !Strings.isNullOrEmpty(HUDRenderEventHandler.currentLiveViewCount))
                {
                    rightInfo.add(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.ytChatViewCountColor).toColoredFont() + "Current watched: " + ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.ytChatViewCountValueColor).toColoredFont() + HUDRenderEventHandler.currentLiveViewCount);
                }

                // equipments
                if (!this.mc.player.isSpectator() && ExtendedConfig.INSTANCE.equipmentHUD)
                {
                    if (ExtendedConfig.INSTANCE.equipmentPosition == Equipments.Position.HOTBAR)
                    {
                        HUDInfo.renderHotbarEquippedItems(this.mc);
                    }
                    else
                    {
                        if (ExtendedConfig.INSTANCE.equipmentDirection == Equipments.Direction.VERTICAL)
                        {
                            HUDInfo.renderVerticalEquippedItems(this.mc);
                        }
                        else
                        {
                            HUDInfo.renderHorizontalEquippedItems(this.mc);
                        }
                    }
                }

                if (ExtendedConfig.INSTANCE.potionHUD)
                {
                    HUDInfo.renderPotionHUD(this.mc);
                }

                // left info
                for (int i = 0; i < leftInfo.size(); ++i)
                {
                    String string = leftInfo.get(i);
                    float fontHeight = this.mc.fontRenderer.FONT_HEIGHT + 1;
                    float yOffset = 3 + fontHeight * i;
                    float xOffset = this.mc.mainWindow.getScaledWidth() - 2 - this.mc.fontRenderer.getStringWidth(string);

                    if (!StringUtils.isNullOrEmpty(string))
                    {
                        this.mc.fontRenderer.drawStringWithShadow(string, ExtendedConfig.INSTANCE.swapRenderInfo ? xOffset : 3.0625F, yOffset, 16777215);
                    }
                }

                // right info
                for (int i = 0; i < rightInfo.size(); ++i)
                {
                    String string = rightInfo.get(i);
                    float fontHeight = this.mc.fontRenderer.FONT_HEIGHT + 1;
                    float yOffset = 3 + fontHeight * i;
                    float xOffset = this.mc.mainWindow.getScaledWidth() - 2 - this.mc.fontRenderer.getStringWidth(string);

                    if (!StringUtils.isNullOrEmpty(string))
                    {
                        this.mc.fontRenderer.drawStringWithShadow(string, ExtendedConfig.INSTANCE.swapRenderInfo ? 3.0625F : xOffset, yOffset, 16777215);
                    }
                }
            }

            if (!this.mc.gameSettings.hideGUI && !this.mc.gameSettings.showDebugInfo)
            {
                if (IndicatiaConfig.GENERAL.enableRenderInfo.get() && ExtendedConfig.INSTANCE.cps && ExtendedConfig.INSTANCE.cpsPosition == CPSPosition.CUSTOM && (this.mc.currentScreen == null || this.mc.currentScreen instanceof ChatScreen))
                {
                    String rcps = ExtendedConfig.INSTANCE.rcps ? " " + HUDInfo.getRCPS() : "";
                    HUDRenderEventHandler.drawRect(ExtendedConfig.INSTANCE.cpsCustomXOffset, ExtendedConfig.INSTANCE.cpsCustomYOffset, ExtendedConfig.INSTANCE.cpsCustomXOffset + this.mc.fontRenderer.getStringWidth(HUDInfo.getCPS() + rcps) + 4, ExtendedConfig.INSTANCE.cpsCustomYOffset + 11, 16777216, (float)ExtendedConfig.INSTANCE.cpsOpacity / 100.0F);
                    this.mc.fontRenderer.drawStringWithShadow(HUDInfo.getCPS() + rcps, ExtendedConfig.INSTANCE.cpsCustomXOffset + 2, ExtendedConfig.INSTANCE.cpsCustomYOffset + 2, 16777215);
                }
            }
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.CHAT)
        {
            if (this.mc.currentScreen instanceof RenderPreviewScreen)
            {
                event.setCanceled(true);
                return;
            }
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS)
        {
            if (!IndicatiaConfig.GENERAL.enableVanillaPotionHUD.get() || this.mc.currentScreen instanceof RenderPreviewScreen)
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

        HealthStatusMode mode = ExtendedConfig.INSTANCE.healthStatusMode;
        boolean flag = mode != HealthStatusMode.DISABLED && (mode != HealthStatusMode.POINTED || entity == InfoUtils.INSTANCE.extendedPointedEntity);
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

    public static long mean(long[] values)
    {
        long sum = 0L;

        for (long value : values)
        {
            sum += value;
        }
        return sum / values.length;
    }

    private static void drawRect(int left, int top, int right, int bottom, int color, float alpha)
    {
        if (alpha > 0.1F)
        {
            if (left < right)
            {
                int i = left;
                left = right;
                right = i;
            }
            if (top < bottom)
            {
                int j = top;
                top = bottom;
                bottom = j;
            }
            float r = (color >> 16 & 255) / 255.0F;
            float g = (color >> 8 & 255) / 255.0F;
            float b = (color & 255) / 255.0F;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder vertexbuffer = tessellator.getBuffer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture();
            GlStateManager.blendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color4f(r, g, b, alpha);
            vertexbuffer.begin(GLConstants.QUADS, DefaultVertexFormats.POSITION);
            vertexbuffer.pos(left, bottom, 0.0D).endVertex();
            vertexbuffer.pos(right, bottom, 0.0D).endVertex();
            vertexbuffer.pos(right, top, 0.0D).endVertex();
            vertexbuffer.pos(left, top, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture();
            GlStateManager.disableBlend();
        }
    }
}