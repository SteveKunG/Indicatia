//package stevekung.mods.indicatia.event;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.text.DecimalFormat;
//import java.util.LinkedList;
//import java.util.List;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.entity.EntityPlayerSP;
//import net.minecraft.client.gui.Gui;
//import net.minecraft.client.gui.GuiChat;
//import net.minecraft.client.network.NetHandlerPlayClient;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.item.EntityArmorStand;
//import net.minecraft.scoreboard.ScoreObjective;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.util.StringUtils;
//import net.minecraft.util.text.Style;
//import net.minecraft.util.text.TextFormatting;
//import net.minecraft.world.dimension.DimensionType;
//import net.minecraftforge.client.event.RenderGameOverlayEvent;
//import net.minecraftforge.client.event.RenderLivingEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.gameevent.TickEvent;
//import net.minecraftforge.fml.server.ServerLifecycleHooks;
//import stevekung.mods.indicatia.config.*;
//import stevekung.mods.indicatia.core.IndicatiaMod;
//import stevekung.mods.indicatia.gui.config.GuiRenderPreview;
//import stevekung.mods.indicatia.gui.overlay.GuiBossOverlayNew;
//import stevekung.mods.indicatia.gui.overlay.GuiPlayerTabOverlayNew;
//import stevekung.mods.indicatia.renderer.HUDInfo;
//import stevekung.mods.indicatia.renderer.KeystrokeRenderer;
//import stevekung.mods.indicatia.utils.InfoUtils;
//import stevekung.mods.indicatia.utils.RenderUtilsIN;
//import stevekung.mods.stevekungslib.client.event.ClientEventHandler;
//import stevekung.mods.stevekungslib.utils.ColorUtils;
//import stevekung.mods.stevekungslib.utils.JsonUtils;
//
//public class HUDRenderEventHandler
//{
//    private Minecraft mc;
//    private final GuiBossOverlayNew overlayBoss;
//    private GuiPlayerTabOverlayNew overlayPlayerList;
//    static boolean recordEnable;
//    private int recTick;
//    private static int readFileTicks;
//    public static String topDonator = "";
//    public static String recentDonator = "";
//    private static String topDonatorName = "";
//    private static String topDonatorCount = "";
//    private static String recentDonatorName = "";
//    private static String recentDonatorCount = "";
//    public static final DecimalFormat tpsFormat = new DecimalFormat("########0.00");
//    public static String currentLiveViewCount = "";
//
//    private static String overallTPS = "";
//    private static String overworldTPS = "";
//    private static String tps = "";
//    private static List<String> allDimensionTPS = new LinkedList<>();
//
//    public HUDRenderEventHandler()
//    {
//        this.mc = Minecraft.getInstance();
//        this.overlayBoss = new GuiBossOverlayNew();
//    }
//
//    /*@SubscribeEvent TODO
//    public void onClientConnectedToServer(NetworkEvent.ClientCustomPayloadLoginEvent event)
//    {
//        this.overlayPlayerList = new GuiPlayerTabOverlayNew();
//    }*/
//
//    @SubscribeEvent
//    public void onClientTick(TickEvent.ClientTickEvent event)
//    {
//        if (event.phase == TickEvent.Phase.START)
//        {
//            if (this.overlayPlayerList == null)//TODO Temp fix
//            {
//                this.overlayPlayerList = new GuiPlayerTabOverlayNew();
//            }
//
//            HUDRenderEventHandler.readFileTicks++;
//
//            if (HUDRenderEventHandler.recordEnable)
//            {
//                this.recTick++;
//            }
//            else
//            {
//                this.recTick = 0;
//            }
//
//            if (!ExtendedConfig.topDonatorFilePath.isEmpty())
//            {
//                HUDRenderEventHandler.readTopDonatorFile();
//            }
//            else
//            {
//                HUDRenderEventHandler.topDonator = "";
//            }
//
//            if (!ExtendedConfig.recentDonatorFilePath.isEmpty())
//            {
//                HUDRenderEventHandler.readRecentDonatorFile();
//            }
//            else
//            {
//                HUDRenderEventHandler.recentDonator = "";
//            }
//
//            if (IndicatiaMod.isYoutubeChatLoaded)
//            {
//                try
//                {
//                    Class<?> clazz = Class.forName("stevekung.mods.ytchat.utils.YouTubeChatService");
//                    HUDRenderEventHandler.currentLiveViewCount = (String)clazz.getField("currentLiveViewCount").get(null);
//                }
//                catch (Exception e)
//                {
//                    HUDRenderEventHandler.currentLiveViewCount = TextFormatting.RED + "unavailable";
//                }
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public void onPreInfoRender(RenderGameOverlayEvent.Pre event)
//    {
//        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR || event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
//        {
//            if (this.mc.currentScreen instanceof GuiRenderPreview)
//            {
//                event.setCanceled(true);
//            }
//        }
//        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT)
//        {
//            if (IndicatiaConfig.GENERAL.enableRenderInfo.get() && !this.mc.gameSettings.hideGUI && !this.mc.gameSettings.showDebugInfo && this.mc.player != null && this.mc.world != null && !(this.mc.currentScreen instanceof GuiRenderPreview))
//            {
//                List<String> leftInfo = new LinkedList<>();
//                List<String> rightInfo = new LinkedList<>();
//                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
//
//                // left info
//                if (ExtendedConfig.ping && !this.mc.isSingleplayer())
//                {
//                    leftInfo.add(HUDInfo.getPing());
//
//                    if (ExtendedConfig.pingToSecond)
//                    {
//                        leftInfo.add(HUDInfo.getPingToSecond());
//                    }
//                }
//                if (ExtendedConfig.serverIP && !this.mc.isSingleplayer())
//                {
//                    if (this.mc.isConnectedToRealms())
//                    {
//                        leftInfo.add(HUDInfo.getRealmName(this.mc));
//                    }
//                    if (this.mc.getCurrentServerData() != null)
//                    {
//                        leftInfo.add(HUDInfo.getServerIP(this.mc));
//                    }
//                }
//                if (ExtendedConfig.fps)
//                {
//                    leftInfo.add(HUDInfo.getFPS());
//                }
//                if (ExtendedConfig.xyz)
//                {
//                    leftInfo.add(HUDInfo.getXYZ(this.mc));
//
//                    if (this.mc.player.dimension == DimensionType.NETHER)
//                    {
//                        leftInfo.add(HUDInfo.getOverworldXYZFromNether(this.mc));
//                    }
//                }
//                if (ExtendedConfig.direction)
//                {
//                    leftInfo.add(HUDInfo.renderDirection(this.mc));
//                }
//                if (ExtendedConfig.biome)
//                {
//                    leftInfo.add(HUDInfo.getBiome(this.mc));
//                }
//                if (ExtendedConfig.slimeChunkFinder && this.mc.player.dimension == DimensionType.OVERWORLD)
//                {
//                    String isSlimeChunk = InfoUtils.INSTANCE.isSlimeChunk(this.mc.player.getPosition()) ? "Yes" : "No";
//                    leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.slimeChunkColor).toColoredFont() + "Slime Chunk: " + ColorUtils.stringToRGB(ExtendedConfig.slimeChunkValueColor).toColoredFont() + isSlimeChunk);
//                }
//                if (CPSPosition.getById(ExtendedConfig.cpsPosition).equalsIgnoreCase("left"))
//                {
//                    if (ExtendedConfig.cps)
//                    {
//                        leftInfo.add(HUDInfo.getCPS());
//                    }
//                    if (ExtendedConfig.rcps)
//                    {
//                        leftInfo.add(HUDInfo.getRCPS());
//                    }
//                }
//                if (IndicatiaConfig.GENERAL.donatorMessagePosition.get() == IndicatiaConfig.DonatorMessagePos.LEFT)
//                {
//                    if (!HUDRenderEventHandler.topDonator.isEmpty())
//                    {
//                        String text = ExtendedConfig.topDonatorText.isEmpty() ? "" : ExtendedConfig.topDonatorText + TextFormatting.RESET + " ";
//                        leftInfo.add(text + HUDRenderEventHandler.topDonator);
//                    }
//                    if (!HUDRenderEventHandler.recentDonator.isEmpty())
//                    {
//                        String text = ExtendedConfig.recentDonatorText.isEmpty() ? "" : ExtendedConfig.recentDonatorText + TextFormatting.RESET + " ";
//                        leftInfo.add(text + HUDRenderEventHandler.recentDonator);
//                    }
//                }
//                // server tps
//                if (ExtendedConfig.tps && server != null)
//                {
//                    if (ClientEventHandler.ticks % 20 == 0)
//                    {
//                        double overallTPS = HUDRenderEventHandler.mean(server.tickTimeArray) * 1.0E-6D;
//                        double overworldTPS = HUDRenderEventHandler.mean(server.getTickTime(DimensionType.OVERWORLD)) * 1.0E-6D;
//                        double tps = Math.min(1000.0D / overallTPS, 20);
//
//                        HUDRenderEventHandler.overallTPS = HUDRenderEventHandler.tpsFormat.format(overallTPS);
//                        HUDRenderEventHandler.overworldTPS = "";
//                        HUDRenderEventHandler.allDimensionTPS.clear();
//
//                        if (ExtendedConfig.tpsAllDims)
//                        {
//                            for (DimensionType dimension : DimensionType.func_212681_b())
//                            {
//                                long[] values = server.getTickTime(dimension);
//                                String dimensionName = DimensionType.func_212678_a(dimension).toString();
//
//                                if (values == null)
//                                {
//                                    return;
//                                }
//                                double dimensionTPS = HUDRenderEventHandler.mean(values) * 1.0E-6D;
//                                HUDRenderEventHandler.allDimensionTPS.add(ColorUtils.stringToRGB(ExtendedConfig.tpsColor).toColoredFont() + "Dimension " + dimensionName.substring(dimensionName.indexOf(":") + 1) + ": " + ColorUtils.stringToRGB(ExtendedConfig.tpsValueColor).toColoredFont() + HUDRenderEventHandler.tpsFormat.format(dimensionTPS));
//                            }
//                        }
//                        else
//                        {
//                            HUDRenderEventHandler.overworldTPS = HUDRenderEventHandler.tpsFormat.format(overworldTPS);
//                        }
//                        HUDRenderEventHandler.tps = HUDRenderEventHandler.tpsFormat.format(tps);
//                    }
//                    // overall tps
//                    leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.tpsColor).toColoredFont() + "Overall TPS: " + ColorUtils.stringToRGB(ExtendedConfig.tpsValueColor).toColoredFont() + HUDRenderEventHandler.overallTPS);
//                    // all dimension tps
//                    leftInfo.addAll(HUDRenderEventHandler.allDimensionTPS);
//
//                    // overworld tps
//                    if (!HUDRenderEventHandler.overworldTPS.isEmpty())
//                    {
//                        leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.tpsColor).toColoredFont() + "Overworld TPS: " + ColorUtils.stringToRGB(ExtendedConfig.tpsValueColor).toColoredFont() + HUDRenderEventHandler.overworldTPS);
//                    }
//
//                    // tps
//                    leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.tpsColor).toColoredFont() + "TPS: " + ColorUtils.stringToRGB(ExtendedConfig.tpsValueColor).toColoredFont() + HUDRenderEventHandler.tps);
//                }
//
//                // right info
//                if (ExtendedConfig.realTime)
//                {
//                    rightInfo.add(HUDInfo.getCurrentTime());
//                }
//                if (ExtendedConfig.gameTime)
//                {
//                    rightInfo.add(HUDInfo.getCurrentGameTime(this.mc));
//                }
//                if (ExtendedConfig.gameWeather && this.mc.world.isRaining())
//                {
//                    rightInfo.add(HUDInfo.getGameWeather(this.mc));
//                }
//                if (ExtendedConfig.moonPhase)
//                {
//                    rightInfo.add(InfoUtils.INSTANCE.getMoonPhase(this.mc));
//                }
//                if (CPSPosition.getById(ExtendedConfig.cpsPosition).equalsIgnoreCase("right"))
//                {
//                    if (ExtendedConfig.cps)
//                    {
//                        rightInfo.add(HUDInfo.getCPS());
//                    }
//                    if (ExtendedConfig.rcps)
//                    {
//                        rightInfo.add(HUDInfo.getRCPS());
//                    }
//                }
//                if (IndicatiaConfig.GENERAL.donatorMessagePosition.get() == IndicatiaConfig.DonatorMessagePos.RIGHT)
//                {
//                    if (!HUDRenderEventHandler.topDonator.isEmpty())
//                    {
//                        String text = ExtendedConfig.topDonatorText.isEmpty() ? "" : ExtendedConfig.topDonatorText + TextFormatting.RESET + " ";
//                        rightInfo.add(text + HUDRenderEventHandler.topDonator);
//                    }
//                    if (!HUDRenderEventHandler.recentDonator.isEmpty())
//                    {
//                        String text = ExtendedConfig.recentDonatorText.isEmpty() ? "" : ExtendedConfig.recentDonatorText + TextFormatting.RESET + " ";
//                        rightInfo.add(text + HUDRenderEventHandler.recentDonator);
//                    }
//                }
//                if (IndicatiaMod.isYoutubeChatLoaded && !HUDRenderEventHandler.currentLiveViewCount.isEmpty())
//                {
//                    rightInfo.add(ColorUtils.stringToRGB(ExtendedConfig.ytChatViewCountColor).toColoredFont() + "Current watched: " + ColorUtils.stringToRGB(ExtendedConfig.ytChatViewCountValueColor).toColoredFont() + HUDRenderEventHandler.currentLiveViewCount);
//                }
//
//                // equipments
//                if (!this.mc.player.isSpectator() && ExtendedConfig.equipmentHUD)
//                {
//                    if (EnumEquipment.Position.getById(ExtendedConfig.equipmentPosition).equalsIgnoreCase("hotbar"))
//                    {
//                        HUDInfo.renderHotbarEquippedItems(this.mc);
//                    }
//                    else
//                    {
//                        if (EnumEquipment.Direction.getById(ExtendedConfig.equipmentDirection).equalsIgnoreCase("vertical"))
//                        {
//                            HUDInfo.renderVerticalEquippedItems(this.mc);
//                        }
//                        else
//                        {
//                            HUDInfo.renderHorizontalEquippedItems(this.mc);
//                        }
//                    }
//                }
//
//                if (ExtendedConfig.potionHUD)
//                {
//                    HUDInfo.renderPotionHUD(this.mc);
//                }
//
//                // left info
//                for (int i = 0; i < leftInfo.size(); ++i)
//                {
//                    String string = leftInfo.get(i);
//                    float fontHeight = ColorUtils.coloredFontRenderer.FONT_HEIGHT + 1;
//                    float yOffset = 3 + fontHeight * i;
//                    float xOffset = this.mc.mainWindow.getScaledWidth() - 2 - ColorUtils.coloredFontRenderer.getStringWidth(string);
//
//                    if (!StringUtils.isNullOrEmpty(string))
//                    {
//                        ColorUtils.coloredFontRenderer.drawStringWithShadow(string, ExtendedConfig.swapRenderInfo ? xOffset : 3.0625F, yOffset, 16777215);
//                    }
//                }
//
//                // right info
//                for (int i = 0; i < rightInfo.size(); ++i)
//                {
//                    String string = rightInfo.get(i);
//                    float fontHeight = ColorUtils.coloredFontRenderer.FONT_HEIGHT + 1;
//                    float yOffset = 3 + fontHeight * i;
//                    float xOffset = this.mc.mainWindow.getScaledWidth() - 2 - ColorUtils.coloredFontRenderer.getStringWidth(string);
//
//                    if (!StringUtils.isNullOrEmpty(string))
//                    {
//                        ColorUtils.coloredFontRenderer.drawStringWithShadow(string, ExtendedConfig.swapRenderInfo ? 3.0625F : xOffset, yOffset, 16777215);
//                    }
//                }
//            }
//
//            if (HUDRenderEventHandler.recordEnable)
//            {
//                int color = 16777215;
//
//                if (this.recTick % 24 >= 0 && this.recTick % 24 <= 12)
//                {
//                    color = 16733525;
//                }
//                ColorUtils.coloredFontRenderer.drawStringWithShadow("REC: " + StringUtils.ticksToElapsedTime(this.recTick), this.mc.mainWindow.getScaledWidth() - ColorUtils.coloredFontRenderer.getStringWidth("REC: " + StringUtils.ticksToElapsedTime(this.recTick)) - 2, this.mc.mainWindow.getScaledHeight() - 10, color);
//            }
//
//            if (!this.mc.gameSettings.hideGUI && !this.mc.gameSettings.showDebugInfo)
//            {
//                if (ExtendedConfig.keystroke)
//                {
//                    if (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiChat)
//                    {
//                        KeystrokeRenderer.render(this.mc);
//                    }
//                }
//                if (IndicatiaConfig.GENERAL.enableRenderInfo.get() && ExtendedConfig.cps && CPSPosition.getById(ExtendedConfig.cpsPosition).equalsIgnoreCase("custom") && (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiChat))
//                {
//                    String rcps = ExtendedConfig.rcps ? " " + HUDInfo.getRCPS() : "";
//                    RenderUtilsIN.drawRect(ExtendedConfig.cpsCustomXOffset, ExtendedConfig.cpsCustomYOffset, ExtendedConfig.cpsCustomXOffset + this.mc.fontRenderer.getStringWidth(HUDInfo.getCPS() + rcps) + 4, ExtendedConfig.cpsCustomYOffset + 11, 16777216, (float)ExtendedConfig.cpsOpacity / 100.0F);
//                    this.mc.fontRenderer.drawStringWithShadow(HUDInfo.getCPS() + rcps, ExtendedConfig.cpsCustomXOffset + 2, ExtendedConfig.cpsCustomYOffset + 2, 16777215);
//                }
//            }
//        }
//        if (event.getType() == RenderGameOverlayEvent.ElementType.PLAYER_LIST)
//        {
//            event.setCanceled(true);
//            ScoreObjective scoreobjective = this.mc.world.getScoreboard().getObjectiveInDisplaySlot(0);
//            NetHandlerPlayClient handler = this.mc.player.connection;
//
//            if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || handler.getPlayerInfoMap().size() > 1 || scoreobjective != null))
//            {
//                this.overlayPlayerList.setVisible(true);
//                this.overlayPlayerList.renderPlayerlist(this.mc.mainWindow.getScaledWidth(), this.mc.world.getScoreboard(), scoreobjective);
//            }
//            else
//            {
//                this.overlayPlayerList.setVisible(false);
//            }
//        }
//        if (event.getType() == RenderGameOverlayEvent.ElementType.CHAT)
//        {
//            if (this.mc.currentScreen instanceof GuiRenderPreview)
//            {
//                event.setCanceled(true);
//                return;
//            }
//            if (IndicatiaConfig.GENERAL.enableFixChatDepthRender.get())
//            {
//                event.setCanceled(true);
//                GlStateManager.pushMatrix();
//                GlStateManager.translatef(0, this.mc.mainWindow.getScaledHeight() - 48, 0.0F);
//                GlStateManager.disableDepthTest();
//                this.mc.ingameGUI.getChatGUI().drawChat(this.mc.ingameGUI.getTicks());
//                GlStateManager.enableDepthTest();
//                GlStateManager.popMatrix();
//            }
//        }
//        if (event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS)
//        {
//            if (!IndicatiaConfig.GENERAL.enableVanillaPotionHUD.get() || this.mc.currentScreen instanceof GuiRenderPreview)
//            {
//                event.setCanceled(true);
//            }
//        }
//        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSHEALTH)
//        {
//            event.setCanceled(true);
//            this.mc.getTextureManager().bindTexture(Gui.ICONS);
//            GlStateManager.blendFuncSeparate(770, 771, 1, 0);
//            GlStateManager.enableBlend();
//            this.overlayBoss.renderBossHealth();
//            GlStateManager.disableBlend();
//        }
//    }
//
//    @SubscribeEvent
//    public void onRenderHealthStatus(RenderLivingEvent.Specials.Post<EntityLivingBase> event)
//    {
//        EntityLivingBase entity = event.getEntity();
//        float health = entity.getHealth();
//        boolean halfHealth = health <= entity.getMaxHealth() / 2F;
//        boolean halfHealth1 = health <= entity.getMaxHealth() / 4F;
//        double range = entity.getAttribute(EntityLivingBase.NAMETAG_DISTANCE).getValue();
//        double distance = entity.getDistanceSq(this.mc.getRenderViewEntity());
//
//        if (entity.isSneaking())
//        {
//            distance /= 2;
//        }
//
//        String mode = HealthStatusMode.getById(ExtendedConfig.healthStatusMode);
//        boolean flag = !mode.equalsIgnoreCase("disabled") && (!mode.equalsIgnoreCase("pointed") || entity == InfoUtils.INSTANCE.extendedPointedEntity);
//        Style color = halfHealth ? JsonUtils.red() : halfHealth1 ? JsonUtils.darkRed() : JsonUtils.green();
//
//        if (distance < range * range)
//        {
//            if (!this.mc.gameSettings.hideGUI && !entity.isInvisible() && flag && !(entity instanceof EntityPlayerSP || entity instanceof EntityArmorStand) && !InfoUtils.INSTANCE.isHypixel())
//            {
//                String heart = JsonUtils.create("\u2764 ").setStyle(color).getFormattedText();
//                RenderUtilsIN.renderEntityHealth(entity, heart + String.format("%.1f", health), event.getX(), event.getY(), event.getZ());
//            }
//        }
//    }
//
//    private static void readTopDonatorFile()
//    {
//        File file = new File("/" + ExtendedConfig.topDonatorFilePath);
//        String text = "";
//
//        if (HUDRenderEventHandler.readFileTicks % IndicatiaConfig.GENERAL.readFileInterval.get() == 0)
//        {
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)))
//            {
//                String line;
//
//                while ((line = reader.readLine()) != null)
//                {
//                    if (!line.trim().equals(""))
//                    {
//                        text = line.replace("\r", "");
//                    }
//                }
//                String[] textSplit = text.split(" ");
//                HUDRenderEventHandler.topDonatorName = textSplit[0];
//                HUDRenderEventHandler.topDonatorCount = textSplit[1];
//            }
//            catch (Exception e)
//            {
//                IndicatiaMod.LOGGER.error("Couldn't read text file from path {}", file.getPath());
//                e.printStackTrace();
//                HUDRenderEventHandler.topDonator = TextFormatting.RED + "Cannot read text file!";
//            }
//        }
//        HUDRenderEventHandler.topDonator = ColorUtils.stringToRGB(ExtendedConfig.topDonatorNameColor).toColoredFont() + HUDRenderEventHandler.topDonatorName + ColorUtils.stringToRGB(ExtendedConfig.topDonatorValueColor).toColoredFont() + " " + HUDRenderEventHandler.topDonatorCount.replace("THB", "") + "THB";
//    }
//
//    private static void readRecentDonatorFile()
//    {
//        File file = new File("/" + ExtendedConfig.recentDonatorFilePath);
//        String text = "";
//
//        if (HUDRenderEventHandler.readFileTicks % IndicatiaConfig.GENERAL.readFileInterval.get() == 0)
//        {
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)))
//            {
//                String line;
//
//                while ((line = reader.readLine()) != null)
//                {
//                    if (!line.trim().equals(""))
//                    {
//                        text = line.replace("\r", "");
//                    }
//                }
//                String[] textSplit = text.split(" ");
//                HUDRenderEventHandler.recentDonatorName = textSplit[0];
//                HUDRenderEventHandler.recentDonatorCount = textSplit[1];
//            }
//            catch (Exception e)
//            {
//                IndicatiaMod.LOGGER.error("Couldn't read text file from path {}", file.getPath());
//                e.printStackTrace();
//                HUDRenderEventHandler.recentDonator = TextFormatting.RED + "Cannot read text file!";
//            }
//        }
//        HUDRenderEventHandler.recentDonator = ColorUtils.stringToRGB(ExtendedConfig.recentDonatorNameColor).toColoredFont() + HUDRenderEventHandler.recentDonatorName + ColorUtils.stringToRGB(ExtendedConfig.recentDonatorValueColor).toColoredFont() + " " + HUDRenderEventHandler.recentDonatorCount.replace("THB", "") + "THB";
//    }
//
//    public static long mean(long[] values)
//    {
//        long sum = 0L;
//
//        for (long value : values)
//        {
//            sum += value;
//        }
//        return sum / values.length;
//    }
//}