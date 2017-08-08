package stevekung.mods.indicatia.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.renderer.ColoredFontRenderer;
import stevekung.mods.indicatia.renderer.HUDInfo;
import stevekung.mods.indicatia.util.InfoUtil;
import stevekung.mods.indicatia.util.JsonUtil;
import stevekung.mods.indicatia.util.ModLogger;
import stevekung.mods.indicatia.util.RenderUtil;

public class HUDRenderHandler
{
    private final Minecraft mc;
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
            if (!ExtendedConfig.RECENT_DONATOR_FILE_PATH.isEmpty())
            {
                HUDRenderHandler.readRecentDonatorFile();
            }
        }
    }

    @SubscribeEvent
    public void onPreInfoRender(RenderGameOverlayEvent.Pre event)
    {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT)
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
                    String isSlimeChunk = InfoUtil.INSTANCE.isSlimeChunk(MathHelper.floor_double(this.mc.thePlayer.posX), MathHelper.floor_double(this.mc.thePlayer.posZ)) ? "Yes" : "No";
                    leftInfo.add("Slime Chunk: " + isSlimeChunk);
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
                        String text = ExtendedConfig.TOP_DONATOR_TEXT.isEmpty() ? "" : ExtendedConfig.TOP_DONATOR_TEXT + EnumChatFormatting.RESET + " ";
                        leftInfo.add(text + HUDRenderHandler.topDonator);
                    }
                    if (!HUDRenderHandler.recentDonator.isEmpty())
                    {
                        String text = ExtendedConfig.RECENT_DONATOR_TEXT.isEmpty() ? "" : ExtendedConfig.RECENT_DONATOR_TEXT + EnumChatFormatting.RESET + " ";
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
                        String text = ExtendedConfig.TOP_DONATOR_TEXT.isEmpty() ? "" : ExtendedConfig.TOP_DONATOR_TEXT + EnumChatFormatting.RESET + " ";
                        rightInfo.add(text + HUDRenderHandler.topDonator);
                    }
                    if (!HUDRenderHandler.recentDonator.isEmpty())
                    {
                        String text = ExtendedConfig.RECENT_DONATOR_TEXT.isEmpty() ? "" : ExtendedConfig.RECENT_DONATOR_TEXT + EnumChatFormatting.RESET + " ";
                        rightInfo.add(text + HUDRenderHandler.recentDonator);
                    }
                }

                // equipments
                if (ConfigManager.enableRenderEquippedItem)
                {
                    if (ConfigManager.equipmentPosition.equals("hotbar"))
                    {
                        HUDInfo.renderHotbarEquippedItems(this.mc);
                    }
                    else
                    {
                        HUDInfo.renderEquippedItems(this.mc);
                    }
                }

                if (ConfigManager.enablePotionStatusHUD)
                {
                    HUDInfo.renderPotionStatusHUD(this.mc);
                }

                // left info
                for (int i = 0; i < leftInfo.size(); ++i)
                {
                    ScaledResolution res = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                    String string = leftInfo.get(i);
                    float fontHeight = IndicatiaMod.coloredFontRenderer.FONT_HEIGHT + 1;
                    float yOffset = 3 + fontHeight * i;
                    float xOffset = res.getScaledWidth() - 2 - IndicatiaMod.coloredFontRenderer.getStringWidth(string);

                    if (!string.isEmpty())
                    {
                        this.mc.mcProfiler.startSection("indicatia_info");
                        IndicatiaMod.coloredFontRenderer.drawString(string, ConfigManager.swapRenderInfoToRight ? (int) xOffset : (int) 3.0625F, (int) yOffset, 16777215, true);
                        this.mc.mcProfiler.endSection();
                    }
                }

                // right info
                for (int i = 0; i < rightInfo.size(); ++i)
                {
                    ScaledResolution res = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                    String string = rightInfo.get(i);
                    float fontHeight = IndicatiaMod.coloredFontRenderer.FONT_HEIGHT + 1;
                    float yOffset = 3 + fontHeight * i;
                    float xOffset = res.getScaledWidth() - 2 - IndicatiaMod.coloredFontRenderer.getStringWidth(string);

                    if (!string.isEmpty())
                    {
                        this.mc.mcProfiler.startSection("indicatia_info");
                        IndicatiaMod.coloredFontRenderer.drawString(string, ConfigManager.swapRenderInfoToRight ? (int) 3.0625F : (int) xOffset, (int) yOffset, 16777215, true);
                        this.mc.mcProfiler.endSection();
                    }
                }
            }

            if (HUDRenderHandler.recordEnable)
            {
                ScaledResolution res = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                int color = 16777215;

                if (this.recTick % 24 >= 0 && this.recTick % 24 <= 12)
                {
                    color = 16733525;
                }
                IndicatiaMod.coloredFontRenderer.drawString("REC: " + StringUtils.ticksToElapsedTime(this.recTick), res.getScaledWidth() - IndicatiaMod.coloredFontRenderer.getStringWidth("REC: " + StringUtils.ticksToElapsedTime(this.recTick)) - 2, res.getScaledHeight() - 10, color, true);
            }
        }
        if (event.type == RenderGameOverlayEvent.ElementType.PLAYER_LIST)
        {
            event.setCanceled(true);
            ScoreObjective scoreobjective = this.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(0);
            NetHandlerPlayClient handler = this.mc.thePlayer.sendQueue;
            @SuppressWarnings("unchecked")
            List<GuiPlayerInfo> players = handler.playerInfoList;
            int maxPlayers = handler.currentServerMaxPlayers;
            int width = event.resolution.getScaledWidth();

            if (this.mc.gameSettings.keyBindPlayerList.getIsKeyPressed() && (!this.mc.isIntegratedServerRunning() || handler.playerInfoList.size() > 1 || scoreobjective != null))
            {
                int rows = maxPlayers;
                int columns = 1;

                for (columns = 1; rows > 20; rows = (maxPlayers + columns - 1) / columns)
                {
                    columns++;
                }

                int columnWidth = 300 / columns;

                if (columnWidth > 150)
                {
                    columnWidth = 150;
                }

                int left = (width - columns * columnWidth) / 2;
                byte border = 10;
                Gui.drawRect(left - 1, border - 1, left + columnWidth * columns, border + 9 * rows, Integer.MIN_VALUE);

                for (int i = 0; i < maxPlayers; i++)
                {
                    int xPos = left + i % columns * columnWidth;
                    int yPos = border + i / columns * 9;
                    Gui.drawRect(xPos, yPos, xPos + columnWidth - 1, yPos + 8, 553648127);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);

                    if (i < players.size())
                    {
                        GuiPlayerInfo player = players.get(i);
                        ScorePlayerTeam team = this.mc.theWorld.getScoreboard().getPlayersTeam(player.name);
                        String displayName = ScorePlayerTeam.formatPlayerName(team, player.name);
                        IndicatiaMod.coloredFontRenderer.drawStringWithShadow(displayName, xPos, yPos, 16777215);

                        if (scoreobjective != null)
                        {
                            int endX = xPos + IndicatiaMod.coloredFontRenderer.getStringWidth(displayName) + 5;
                            int maxX = xPos + columnWidth - 12 - 5;

                            if (maxX - endX > 5)
                            {
                                Score score = scoreobjective.getScoreboard().getValueFromObjective(player.name, scoreobjective);
                                String scoreDisplay = EnumChatFormatting.YELLOW + "" + score.getScorePoints();
                                IndicatiaMod.coloredFontRenderer.drawStringWithShadow(scoreDisplay, maxX - IndicatiaMod.coloredFontRenderer.getStringWidth(scoreDisplay), yPos, 16777215);
                            }
                        }

                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        int ping = player.responseTime;

                        if (!ConfigManager.enableCustomPlayerList)
                        {
                            this.mc.getTextureManager().bindTexture(Gui.icons);
                            int pingIndex = 4;

                            if (ping < 0)
                            {
                                pingIndex = 5;
                            }
                            else if (ping < 150)
                            {
                                pingIndex = 0;
                            }
                            else if (ping < 300)
                            {
                                pingIndex = 1;
                            }
                            else if (ping < 600)
                            {
                                pingIndex = 2;
                            }
                            else if (ping < 1000)
                            {
                                pingIndex = 3;
                            }
                            this.mc.ingameGUI.zLevel = 100.0F;
                            this.mc.ingameGUI.drawTexturedModalRect(xPos + columnWidth - 12, yPos, 0, 176 + pingIndex * 8, 10, 8);
                            this.mc.ingameGUI.zLevel = -100.0F;
                        }
                        else
                        {
                            int color = 5635925;

                            if (ping >= 200 && ping < 301)
                            {
                                color = 16777045;
                            }
                            else if (ping >= 300 && ping < 500)
                            {
                                color = 16733525;
                            }
                            else if (ping >= 500)
                            {
                                color = 11141120;
                            }
                            IndicatiaMod.coloredFontRenderer.drawString(String.valueOf(ping), xPos + columnWidth - 1 - IndicatiaMod.coloredFontRenderer.getStringWidth(String.valueOf(ping)), (int) (yPos + 0.5F), color, true);
                        }
                    }
                }
            }
        }
        if (event.type == RenderGameOverlayEvent.ElementType.CHAT)
        {
            if (ConfigManager.enableChatDepthRender)
            {
                event.setCanceled(true);
                GL11.glPushMatrix();
                GL11.glTranslatef(0, event.resolution.getScaledHeight() - 48, 0.0F);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                IndicatiaMod.MC.ingameGUI.getChatGUI().drawChat(IndicatiaMod.MC.ingameGUI.getUpdateCounter());
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glPopMatrix();
            }
        }
        if (event.type == RenderGameOverlayEvent.ElementType.BOSSHEALTH)
        {
            event.setCanceled(true);
            GL11.glEnable(GL11.GL_BLEND);

            if (BossStatus.bossName != null && BossStatus.statusBarTime > 0)
            {
                --BossStatus.statusBarTime;
                FontRenderer fontrenderer = IndicatiaMod.coloredFontRenderer;
                ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                int i = scaledresolution.getScaledWidth();
                short short1 = 182;
                int j = i / 2 - short1 / 2;
                int k = (int)(BossStatus.healthScale * (short1 + 1));
                byte b0 = 12;

                if (ConfigManager.enableRenderBossHealthBar)
                {
                    this.mc.ingameGUI.drawTexturedModalRect(j, b0, 0, 74, short1, 5);
                    this.mc.ingameGUI.drawTexturedModalRect(j, b0, 0, 74, short1, 5);

                    if (k > 0)
                    {
                        this.mc.ingameGUI.drawTexturedModalRect(j, b0, 0, 79, k, 5);
                    }
                }
                String s = BossStatus.bossName;
                fontrenderer.drawStringWithShadow(s, i / 2 - fontrenderer.getStringWidth(s) / 2, b0 - 10, 16777215);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.mc.getTextureManager().bindTexture(Gui.icons);
            }
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    @SubscribeEvent
    public void onRenderHealthStatus(RenderLivingEvent.Specials.Post event)
    {
        EntityLivingBase entity = event.entity;
        float health = entity.getHealth();
        boolean halfHealth = health <= entity.getMaxHealth() / 2F;
        boolean halfHealth1 = health <= entity.getMaxHealth() / 4F;
        float range = entity.isSneaking() ? RendererLivingEntity.NAME_TAG_RANGE_SNEAK : RendererLivingEntity.NAME_TAG_RANGE;
        double distance = entity.getDistanceSqToEntity(this.mc.renderViewEntity);
        String mode = ConfigManager.healthStatusMode;
        boolean flag = mode.equals("disable") ? false : mode.equals("pointed") ? entity == InfoUtil.INSTANCE.extendedPointedEntity : true;
        JsonUtil json = new JsonUtil();
        ChatStyle color = halfHealth ? json.red() : halfHealth1 ? json.darkRed() : json.green();

        if (distance < range * range)
        {
            if (!this.mc.gameSettings.hideGUI && !entity.isInvisible() && flag && !(entity instanceof EntityPlayerSP))
            {
                String heart = json.text("\u2764 ").setChatStyle(color).getFormattedText();
                RenderUtil.renderEntityHealth(entity, heart + String.format("%.1f", health), event.x, event.y, event.z);
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
                HUDRenderHandler.topDonator = EnumChatFormatting.RED + "Cannot read text file!";
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
                HUDRenderHandler.recentDonator = EnumChatFormatting.RED + "Cannot read text file!";
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