package stevekung.mods.indicatia.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.gui.GuiBossOverlayNew;
import stevekung.mods.indicatia.gui.GuiKeystrokeColorSettings;
import stevekung.mods.indicatia.gui.GuiRenderStatusSettings;
import stevekung.mods.indicatia.renderer.ColoredFontRenderer;
import stevekung.mods.indicatia.renderer.HUDInfo;
import stevekung.mods.indicatia.renderer.KeystrokeRenderer;
import stevekung.mods.indicatia.util.InfoUtil;
import stevekung.mods.indicatia.util.JsonUtil;
import stevekung.mods.indicatia.util.ModLogger;
import stevekung.mods.indicatia.util.RenderUtil;

public class HUDRenderHandler
{
    private final Minecraft mc;
    private GuiBossOverlayNew overlayBoss;
    public static boolean recordEnable;
    private int recTick;
    private static int readFileTicks;
    public static String topDonator = "";
    public static String recentDonator = "";
    private static String topDonatorName = "";
    private static String topDonatorCount = "";
    private static String recentDonatorName = "";
    private static String recentDonatorCount = "";
    private static final DecimalFormat tpsFormat = new DecimalFormat("########0.00");

    public HUDRenderHandler(Minecraft mc)
    {
        this.mc = mc;
        this.overlayBoss = new GuiBossOverlayNew();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            HUDRenderHandler.readFileTicks++;

            if (HUDRenderHandler.recordEnable)
            {
                this.recTick++;
            }
            else
            {
                this.recTick = 0;
            }

            if (!ExtendedConfig.TOP_DONATOR_FILE_PATH.isEmpty())
            {
                HUDRenderHandler.readTopDonatorFile();
            }
            else
            {
                HUDRenderHandler.topDonator = "";
            }

            if (!ExtendedConfig.RECENT_DONATOR_FILE_PATH.isEmpty())
            {
                HUDRenderHandler.readRecentDonatorFile();
            }
            else
            {
                HUDRenderHandler.recentDonator = "";
            }
        }
    }

    @SubscribeEvent
    public void onPreInfoRender(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT)
        {
            if (ConfigManager.enableRenderInfo && !this.mc.gameSettings.hideGUI && !this.mc.gameSettings.showDebugInfo)
            {
                List<String> leftInfo = new ArrayList<>();
                List<String> rightInfo = new ArrayList<>();
                MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

                // left info
                if (ConfigManager.enablePing && !this.mc.isSingleplayer())
                {
                    leftInfo.add(HUDInfo.getPing());
                }
                if (ConfigManager.enableFPS)
                {
                    leftInfo.add(HUDInfo.getFPS());
                }
                if (ConfigManager.enableXYZ)
                {
                    leftInfo.add(HUDInfo.getXYZ(this.mc));

                    if (this.mc.thePlayer.dimension == -1)
                    {
                        leftInfo.add(HUDInfo.getOverworldXYZFromNether(this.mc));
                    }
                }
                if (ConfigManager.enableBiome)
                {
                    leftInfo.add(HUDInfo.getBiome(this.mc));
                }
                if (ConfigManager.enableServerIP && this.mc.getCurrentServerData() != null && !this.mc.isSingleplayer())
                {
                    leftInfo.add(HUDInfo.getServerIP(this.mc));
                }
                if (ConfigManager.enableSlimeChunkFinder && this.mc.thePlayer.dimension == 0)
                {
                    String isSlimeChunk = InfoUtil.INSTANCE.isSlimeChunk(this.mc.thePlayer.getPosition()) ? "Yes" : "No";
                    leftInfo.add(ColoredFontRenderer.color(ExtendedConfig.SLIME_COLOR_R, ExtendedConfig.SLIME_COLOR_G, ExtendedConfig.SLIME_COLOR_B) + "Slime Chunk: " + ColoredFontRenderer.color(ExtendedConfig.SLIME_VALUE_COLOR_R, ExtendedConfig.SLIME_VALUE_COLOR_G, ExtendedConfig.SLIME_VALUE_COLOR_B) + isSlimeChunk);
                }
                if (ExtendedConfig.CPS_POSITION.equals("left"))
                {
                    if (ConfigManager.enableCPS)
                    {
                        leftInfo.add(HUDInfo.getCPS());
                    }
                    if (ConfigManager.enableRCPS)
                    {
                        leftInfo.add(HUDInfo.getRCPS());
                    }
                }
                if (ConfigManager.donatorMessagePosition.equals("left"))
                {
                    if (!HUDRenderHandler.topDonator.isEmpty())
                    {
                        String text = ExtendedConfig.TOP_DONATOR_TEXT.isEmpty() ? "" : ExtendedConfig.TOP_DONATOR_TEXT + TextFormatting.RESET + " ";
                        leftInfo.add(text + HUDRenderHandler.topDonator);
                    }
                    if (!HUDRenderHandler.recentDonator.isEmpty())
                    {
                        String text = ExtendedConfig.RECENT_DONATOR_TEXT.isEmpty() ? "" : ExtendedConfig.RECENT_DONATOR_TEXT + TextFormatting.RESET + " ";
                        leftInfo.add(text + HUDRenderHandler.recentDonator);
                    }
                }
                // server tps
                if (ConfigManager.enableServerTPS && server != null)
                {
                    int dimension = this.mc.thePlayer.dimension;
                    double overallTPS = HUDRenderHandler.mean(server.tickTimeArray) * 1.0E-6D;
                    double dimensionTPS = HUDRenderHandler.mean(server.worldTickTimes.get(dimension)) * 1.0E-6D;
                    double tps = Math.min(1000.0D / overallTPS, 20);
                    leftInfo.add("Overall TPS: " + HUDRenderHandler.tpsFormat.format(overallTPS));
                    leftInfo.add("Dimension " + dimension + " TPS: " + HUDRenderHandler.tpsFormat.format(dimensionTPS));
                    leftInfo.add("TPS: " + HUDRenderHandler.tpsFormat.format(tps));
                }

                // right info
                if (ConfigManager.enableCurrentRealTime)
                {
                    rightInfo.add(HUDInfo.getCurrentTime());
                }
                if (ConfigManager.enableCurrentGameTime)
                {
                    rightInfo.add(HUDInfo.getCurrentGameTime(this.mc));
                }
                if (ConfigManager.enableGameWeather && this.mc.theWorld.isRaining())
                {
                    rightInfo.add(HUDInfo.getGameWeather(this.mc));
                }
                if (ConfigManager.enableMoonPhase)
                {
                    rightInfo.add(InfoUtil.INSTANCE.getMoonPhase(this.mc));
                }
                if (ExtendedConfig.CPS_POSITION.equals("right"))
                {
                    if (ConfigManager.enableCPS)
                    {
                        rightInfo.add(HUDInfo.getCPS());
                    }
                    if (ConfigManager.enableRCPS)
                    {
                        rightInfo.add(HUDInfo.getRCPS());
                    }
                }
                if (ConfigManager.donatorMessagePosition.equals("right"))
                {
                    if (!HUDRenderHandler.topDonator.isEmpty())
                    {
                        String text = ExtendedConfig.TOP_DONATOR_TEXT.isEmpty() ? "" : ExtendedConfig.TOP_DONATOR_TEXT + TextFormatting.RESET + " ";
                        rightInfo.add(text + HUDRenderHandler.topDonator);
                    }
                    if (!HUDRenderHandler.recentDonator.isEmpty())
                    {
                        String text = ExtendedConfig.RECENT_DONATOR_TEXT.isEmpty() ? "" : ExtendedConfig.RECENT_DONATOR_TEXT + TextFormatting.RESET + " ";
                        rightInfo.add(text + HUDRenderHandler.recentDonator);
                    }
                }

                // equipments
                if (!this.mc.thePlayer.isSpectator() && ConfigManager.enableRenderEquippedItem)
                {
                    if (ConfigManager.equipmentPosition.equals("hotbar"))
                    {
                        HUDInfo.renderHotbarEquippedItems(this.mc);
                    }
                    else
                    {
                        if (ConfigManager.equipmentDirection.equals("vertical"))
                        {
                            HUDInfo.renderVerticalEquippedItems(this.mc);
                        }
                        else
                        {
                            HUDInfo.renderHorizontalEquippedItems(this.mc);
                        }
                    }
                }

                if (ConfigManager.enablePotionStatusHUD)
                {
                    HUDInfo.renderPotionStatusHUD(this.mc);
                }

                // left info
                for (int i = 0; i < leftInfo.size(); ++i)
                {
                    ScaledResolution res = new ScaledResolution(this.mc);
                    String string = leftInfo.get(i);
                    float fontHeight = IndicatiaMod.coloredFontRenderer.FONT_HEIGHT + 1;
                    float yOffset = 3 + fontHeight * i;
                    float xOffset = res.getScaledWidth() - 2 - IndicatiaMod.coloredFontRenderer.getStringWidth(string);

                    if (!string.isEmpty())
                    {
                        this.mc.mcProfiler.startSection("indicatia_info");
                        IndicatiaMod.coloredFontRenderer.drawString(string, ConfigManager.swapRenderInfoToRight ? xOffset : 3.0625F, yOffset, 16777215, true);
                        this.mc.mcProfiler.endSection();
                    }
                }

                // right info
                for (int i = 0; i < rightInfo.size(); ++i)
                {
                    ScaledResolution res = new ScaledResolution(this.mc);
                    String string = rightInfo.get(i);
                    float fontHeight = IndicatiaMod.coloredFontRenderer.FONT_HEIGHT + 1;
                    float yOffset = 3 + fontHeight * i;
                    float xOffset = res.getScaledWidth() - 2 - IndicatiaMod.coloredFontRenderer.getStringWidth(string);

                    if (!string.isEmpty())
                    {
                        this.mc.mcProfiler.startSection("indicatia_info");
                        IndicatiaMod.coloredFontRenderer.drawString(string, ConfigManager.swapRenderInfoToRight ? 3.0625F : xOffset, yOffset, 16777215, true);
                        this.mc.mcProfiler.endSection();
                    }
                }
            }

            if (HUDRenderHandler.recordEnable)
            {
                ScaledResolution res = new ScaledResolution(this.mc);
                int color = 16777215;

                if (this.recTick % 24 >= 0 && this.recTick % 24 <= 12)
                {
                    color = 16733525;
                }
                IndicatiaMod.coloredFontRenderer.drawString("REC: " + StringUtils.ticksToElapsedTime(this.recTick), res.getScaledWidth() - IndicatiaMod.coloredFontRenderer.getStringWidth("REC: " + StringUtils.ticksToElapsedTime(this.recTick)) - 2, res.getScaledHeight() - 10, color, true);
            }

            if (!this.mc.gameSettings.hideGUI && !this.mc.gameSettings.showDebugInfo)
            {
                if (ConfigManager.enableKeystroke)
                {
                    if (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiChat || this.mc.currentScreen instanceof GuiRenderStatusSettings || this.mc.currentScreen instanceof GuiKeystrokeColorSettings)
                    {
                        KeystrokeRenderer.init(this.mc);
                    }
                }
                if (ConfigManager.enableRenderInfo && ExtendedConfig.CPS_POSITION.equalsIgnoreCase("custom") && (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiChat || this.mc.currentScreen instanceof GuiRenderStatusSettings || this.mc.currentScreen instanceof GuiKeystrokeColorSettings))
                {
                    String space = ConfigManager.enableRCPS ? " " : "";
                    RenderUtil.drawRect(ExtendedConfig.CPS_X_OFFSET, ExtendedConfig.CPS_Y_OFFSET, ExtendedConfig.CPS_X_OFFSET + this.mc.fontRendererObj.getStringWidth(HUDInfo.getCPS() + space + HUDInfo.getRCPS()) + 4, ExtendedConfig.CPS_Y_OFFSET + 11, 16777216, ExtendedConfig.CPS_OPACITY);
                    this.mc.fontRendererObj.drawString(HUDInfo.getCPS() + space + HUDInfo.getRCPS(), ExtendedConfig.CPS_X_OFFSET + 2, ExtendedConfig.CPS_Y_OFFSET + 2, 16777215, true);
                }
            }
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.PLAYER_LIST)
        {
            if (CommonHandler.overlayPlayerList != null)
            {
                event.setCanceled(true);
                ScoreObjective scoreobjective = this.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(0);
                NetHandlerPlayClient handler = this.mc.thePlayer.connection;

                if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || handler.getPlayerInfoMap().size() > 1 || scoreobjective != null))
                {
                    CommonHandler.overlayPlayerList.updatePlayerList(true);
                    CommonHandler.overlayPlayerList.renderPlayerlist(event.getResolution().getScaledWidth(), this.mc.theWorld.getScoreboard(), scoreobjective);
                }
                else
                {
                    CommonHandler.overlayPlayerList.updatePlayerList(false);
                }
            }
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.CHAT)
        {
            if (ConfigManager.enableChatDepthRender)
            {
                event.setCanceled(true);
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, event.getResolution().getScaledHeight() - 48, 0.0F);
                GlStateManager.disableDepth();
                this.mc.ingameGUI.getChatGUI().drawChat(this.mc.ingameGUI.getUpdateCounter());
                GlStateManager.enableDepth();
                GlStateManager.popMatrix();
            }
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS)
        {
            if (!ConfigManager.enableIngamePotionHUD)
            {
                event.setCanceled(true);
            }
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSHEALTH)
        {
            event.setCanceled(true);
            this.mc.getTextureManager().bindTexture(Gui.ICONS);
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.enableBlend();
            this.overlayBoss.renderBossHealth();
            GlStateManager.disableBlend();
        }
    }

    @SubscribeEvent
    public void onRenderHealthStatus(RenderLivingEvent.Specials.Post event)
    {
        EntityLivingBase entity = event.getEntity();
        float health = entity.getHealth();
        boolean halfHealth = health <= entity.getMaxHealth() / 2F;
        boolean halfHealth1 = health <= entity.getMaxHealth() / 4F;
        float range = entity.isSneaking() ? RenderLivingBase.NAME_TAG_RANGE_SNEAK : RenderLivingBase.NAME_TAG_RANGE;
        double distance = entity.getDistanceSqToEntity(this.mc.getRenderViewEntity());
        String mode = ConfigManager.healthStatusMode;
        boolean flag = mode.equals("disable") ? false : mode.equals("pointed") ? entity == InfoUtil.INSTANCE.extendedPointedEntity : true;
        JsonUtil json = IndicatiaMod.json;
        Style color = halfHealth ? json.red() : halfHealth1 ? json.darkRed() : json.green();

        if (distance < range * range)
        {
            if (!this.mc.gameSettings.hideGUI && !entity.isInvisible() && flag && !(entity instanceof EntityPlayerSP || entity instanceof EntityArmorStand) && !InfoUtil.INSTANCE.isHypixel())
            {
                String heart = json.text("\u2764 ").setStyle(color).getFormattedText();
                RenderUtil.renderEntityHealth(entity, heart + String.format("%.1f", health), event.getX(), event.getY(), event.getZ());
            }
        }
    }

    private static void readTopDonatorFile()
    {
        File file = new File("/" + ExtendedConfig.TOP_DONATOR_FILE_PATH);
        String text = "";

        if (HUDRenderHandler.readFileTicks % ConfigManager.readFileInterval == 0)
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(file)))
            {
                String line;

                while ((line = reader.readLine()) != null)
                {
                    if (!line.trim().equals(""))
                    {
                        text = line.replace("\r", "");
                    }
                }
                String[] textSplit = text.split(" ");
                HUDRenderHandler.topDonatorName = textSplit[0];
                HUDRenderHandler.topDonatorCount = textSplit[1];
            }
            catch (Exception e)
            {
                ModLogger.error("Couldn't read text file from path {}", file.getPath());
                e.printStackTrace();
                HUDRenderHandler.topDonator = TextFormatting.RED + "Cannot read text file!";
            }
        }
        HUDRenderHandler.topDonator = ColoredFontRenderer.color(ExtendedConfig.TOP_DONATE_NAME_COLOR_R, ExtendedConfig.TOP_DONATE_NAME_COLOR_G, ExtendedConfig.TOP_DONATE_NAME_COLOR_B) + HUDRenderHandler.topDonatorName + ColoredFontRenderer.color(ExtendedConfig.TOP_DONATE_COUNT_COLOR_R, ExtendedConfig.TOP_DONATE_COUNT_COLOR_G, ExtendedConfig.TOP_DONATE_COUNT_COLOR_B) + " " + HUDRenderHandler.topDonatorCount.replace("THB", "") + "THB";
    }

    private static void readRecentDonatorFile()
    {
        File file = new File("/" + ExtendedConfig.RECENT_DONATOR_FILE_PATH);
        String text = "";

        if (HUDRenderHandler.readFileTicks % ConfigManager.readFileInterval == 0)
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(file)))
            {
                String line;

                while ((line = reader.readLine()) != null)
                {
                    if (!line.trim().equals(""))
                    {
                        text = line.replace("\r", "");
                    }
                }
                String[] textSplit = text.split(" ");
                HUDRenderHandler.recentDonatorName = textSplit[0];
                HUDRenderHandler.recentDonatorCount = textSplit[1];
            }
            catch (Exception e)
            {
                ModLogger.error("Couldn't read text file from path {}", file.getPath());
                e.printStackTrace();
                HUDRenderHandler.recentDonator = TextFormatting.RED + "Cannot read text file!";
            }
        }
        HUDRenderHandler.recentDonator = ColoredFontRenderer.color(ExtendedConfig.RECENT_DONATE_NAME_COLOR_R, ExtendedConfig.RECENT_DONATE_NAME_COLOR_G, ExtendedConfig.RECENT_DONATE_NAME_COLOR_B) + HUDRenderHandler.recentDonatorName + ColoredFontRenderer.color(ExtendedConfig.RECENT_DONATE_COUNT_COLOR_R, ExtendedConfig.RECENT_DONATE_COUNT_COLOR_G, ExtendedConfig.RECENT_DONATE_COUNT_COLOR_B) + " " + HUDRenderHandler.recentDonatorCount.replace("THB", "") + "THB";
    }

    private static long mean(long[] values)
    {
        long sum = 0L;

        for (long value : values)
        {
            sum += value;
        }
        return sum / values.length;
    }
}