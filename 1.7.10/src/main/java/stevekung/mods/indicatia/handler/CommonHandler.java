package stevekung.mods.indicatia.handler;

import java.awt.Desktop;
import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import io.netty.channel.ChannelOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.*;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import stevekung.mods.indicatia.config.ConfigGuiFactory;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.gui.*;
import stevekung.mods.indicatia.renderer.ModelBipedNew;
import stevekung.mods.indicatia.util.*;

public class CommonHandler
{
    private JsonUtil json;
    private final Minecraft mc;
    public static final List<Long> LEFT_CLICK = new ArrayList<>();
    public static final List<Long> RIGHT_CLICK = new ArrayList<>();
    public static final GuiNewChatUtil chatGui = new GuiNewChatUtil();
    public static final GuiSleepMPNew sleepGui = new GuiSleepMPNew();
    public static final GuiNewChatUtil chatGuiSlash = new GuiNewChatUtil("/");
    public static final GuiCustomCape customCapeGui = new GuiCustomCape();
    public static final GuiDonator donatorGui = new GuiDonator();
    private static final ThreadPoolExecutor serverPinger = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Real Time Server Pinger #%d").setDaemon(true).build());
    public static int currentServerPing;
    private static int pendingPingTicks = 100;
    private final List<String> pausedChannels = new ArrayList<>();

    // AFK Stuff
    public static boolean isAFK;
    public static String afkMode = "idle";
    public static String afkReason;
    public static int afkMoveTicks;
    public static int afkTicks;

    public static boolean autoClick;
    public static String autoClickMode = "right";
    public static int autoClickTicks = 0;

    public static boolean setTCPNoDelay = false;

    public CommonHandler(Minecraft mc)
    {
        this.json = IndicatiaMod.json;
        this.mc = mc;
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event)
    {
        if (event.modID.equalsIgnoreCase(IndicatiaMod.MOD_ID))
        {
            ConfigManager.syncConfig(false);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (this.mc.thePlayer != null)
        {
            if (this.mc.getNetHandler() != null && CommonHandler.setTCPNoDelay)
            {
                this.mc.getNetHandler().getNetworkManager().channel().config().setOption(ChannelOption.TCP_NODELAY, true);
                ModLogger.info("Set TCP_NODELAY to true");
                CommonHandler.setTCPNoDelay = false;
            }
            if (event.phase == TickEvent.Phase.START)
            {
                CommonHandler.runAFK(this.mc.thePlayer);
                CommonHandler.printVersionMessage(this.json, this.mc.thePlayer);
                CommonHandler.replacingPlayerModel(this.mc.thePlayer);
                AutoLoginFunction.runAutoLoginFunction();
                CapeUtil.loadCapeTexture();

                if (AutoLoginFunction.functionDelay > 0)
                {
                    AutoLoginFunction.functionDelay--;
                }
                if (AutoLoginFunction.functionDelay == 0)
                {
                    AutoLoginFunction.runAutoLoginFunctionTicks(this.mc);
                }
                if (CommonHandler.pendingPingTicks > 0 && this.mc.func_147104_D() != null)
                {
                    CommonHandler.pendingPingTicks--;

                    if (CommonHandler.pendingPingTicks == 0)
                    {
                        CommonHandler.getRealTimeServerPing(this.mc.func_147104_D());
                        CommonHandler.pendingPingTicks = 100;
                    }
                }
                if (IndicatiaMod.isSteveKunG() && CommonHandler.autoClick)
                {
                    CommonHandler.autoClickTicks++;

                    if (CommonHandler.autoClickMode.equals("left"))
                    {
                        this.mc.func_147115_a(true);
                    }
                    else
                    {
                        if (CommonHandler.autoClickTicks % 4 == 0)
                        {
                            this.mc.func_147121_ag();
                        }
                    }
                }
            }
            if (!(this.mc.thePlayer.movementInput instanceof MovementInputFromOptionsIU))
            {
                this.mc.thePlayer.movementInput = new MovementInputFromOptionsIU(this.mc.gameSettings);
            }
            CommonHandler.replaceGui(this.mc, this.mc.currentScreen);
        }
        GuiIngameForge.renderBossHealth = ConfigManager.enableRenderBossHealthStatus;
        GuiIngameForge.renderObjective = ConfigManager.enableRenderScoreboard;
    }

    @SubscribeEvent
    public void onMouseClick(MouseEvent event)
    {
        if (event.button == 0 && event.buttonstate)
        {
            CommonHandler.LEFT_CLICK.add(Long.valueOf(System.currentTimeMillis()));
        }
        if (event.button == 1 && event.buttonstate)
        {
            CommonHandler.RIGHT_CLICK.add(Long.valueOf(System.currentTimeMillis()));
        }
    }

    @SubscribeEvent
    public void onPreRenderLivingSpecials(RenderLivingEvent.Specials.Pre event)
    {
        EntityLivingBase entity = event.entity;

        if (ConfigManager.enableRenderNametagFix)
        {
            event.setCanceled(true);

            if (this.canRenderName(entity))
            {
                double d0 = entity.getDistanceSqToEntity(RenderManager.instance.livingPlayer);
                float f = entity.isSneaking() ? RendererLivingEntity.NAME_TAG_RANGE_SNEAK : RendererLivingEntity.NAME_TAG_RANGE;

                if (d0 < f * f)
                {
                    String name = entity.func_145748_c_().getFormattedText();
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                    CommonHandler.renderEntityName(this.mc, entity, name, event.x, event.y, event.z);
                }
            }
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event)
    {
        if (ConfigManager.enableCustomServerSelectionGui && event.gui != null && event.gui.getClass().equals(GuiMultiplayer.class))
        {
            event.gui = new GuiMultiplayerCustom(new GuiMainMenu());
        }
    }

    @SubscribeEvent
    public void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        this.mc.ingameGUI.persistantChatGUI = new GuiNewChatFast();
        CommonHandler.setTCPNoDelay = true;
    }

    @SubscribeEvent
    public void onDisconnectedFromServerEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        CommonHandler.stopCommandTicks();
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
    {
        CommonHandler.stopCommandTicks();
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        CommonHandler.stopCommandTicks();
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            InfoUtil.INSTANCE.processMouseOverEntity(this.mc, event.renderTickTime);
        }
    }

    @SubscribeEvent
    public void onPressKey(InputEvent.KeyInputEvent event)
    {
        if (this.mc.currentScreen == null && this.mc.gameSettings.keyBindCommand.isPressed())
        {
            this.mc.displayGuiScreen(CommonHandler.chatGuiSlash);
        }
        if (KeyBindingHandler.KEY_QUICK_CONFIG.getIsKeyPressed())
        {
            this.mc.displayGuiScreen(new ConfigGuiFactory.GuiMainConfig(this.mc.currentScreen));
        }
        if (KeyBindingHandler.KEY_REC_COMMAND.getIsKeyPressed())
        {
            HUDRenderHandler.recordEnable = !HUDRenderHandler.recordEnable;
        }
        if (ConfigManager.enableCustomCape && KeyBindingHandler.KEY_CUSTOM_CAPE_GUI.getIsKeyPressed())
        {
            this.mc.displayGuiScreen(CommonHandler.customCapeGui);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_D))
        {
            this.mc.ingameGUI.getChatGUI().clearChatMessages();
        }
        if (ExtendedConfig.TOGGLE_SPRINT_USE_MODE.equals("key_binding"))
        {
            String[] keyTS = ConfigManager.keyToggleSprint.split(",");
            int keyTGCtrl = InfoUtil.INSTANCE.parseInt(keyTS[0], "Toggle Sprint");
            int keyTGOther = InfoUtil.INSTANCE.parseInt(keyTS[1], "Toggle Sprint");

            if (Keyboard.isKeyDown(keyTGCtrl) && Keyboard.isKeyDown(keyTGOther))
            {
                ExtendedConfig.TOGGLE_SPRINT = !ExtendedConfig.TOGGLE_SPRINT;
                InfoUtil.INSTANCE.setOverlayMessage(this.json.text(ExtendedConfig.TOGGLE_SPRINT ? "Toggle Sprint Enabled" : "Toggle Sprint Disabled").getFormattedText(), false);
                ExtendedConfig.save();
            }
        }
        if (ExtendedConfig.TOGGLE_SNEAK_USE_MODE.equals("key_binding"))
        {
            String[] keyTS = ConfigManager.keyToggleSneak.split(",");
            int keyTGCtrl = InfoUtil.INSTANCE.parseInt(keyTS[0], "Toggle Sneak");
            int keyTGOther = InfoUtil.INSTANCE.parseInt(keyTS[1], "Toggle Sneak");

            if (Keyboard.isKeyDown(keyTGCtrl) && Keyboard.isKeyDown(keyTGOther))
            {
                ExtendedConfig.TOGGLE_SNEAK = !ExtendedConfig.TOGGLE_SNEAK;
                InfoUtil.INSTANCE.setOverlayMessage(this.json.text(ExtendedConfig.TOGGLE_SNEAK ? "Toggle Sneak Enabled" : "Toggle Sneak Disabled").getFormattedText(), false);
                ExtendedConfig.save();
            }
        }
        if (ExtendedConfig.AUTO_SWIM_USE_MODE.equals("key_binding"))
        {
            String[] keyAW = ConfigManager.keyAutoSwim.split(",");
            int keyAWCtrl = InfoUtil.INSTANCE.parseInt(keyAW[0], "Auto Swim");
            int keyAWOther = InfoUtil.INSTANCE.parseInt(keyAW[1], "Auto Swim");

            if (Keyboard.isKeyDown(keyAWCtrl) && Keyboard.isKeyDown(keyAWOther))
            {
                ExtendedConfig.AUTO_SWIM = !ExtendedConfig.AUTO_SWIM;
                InfoUtil.INSTANCE.setOverlayMessage(this.json.text(ExtendedConfig.AUTO_SWIM ? "Auto Swim Enabled" : "Auto Swim Disabled").getFormattedText(), false);
                ExtendedConfig.save();
            }
        }
        if (KeyBindingHandler.KEY_DONATOR_GUI.getIsKeyPressed())
        {
            this.mc.displayGuiScreen(CommonHandler.donatorGui);
        }
    }

    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event)
    {
        if (event.gui instanceof GuiIngameMenu)
        {
            event.buttonList.add(new GuiButton(200, event.gui.width - 145, 20, 135, 20, "Paypal"));
            event.buttonList.add(new GuiButton(201, event.gui.width - 145, 41, 135, 20, "Truemoney"));
        }
        if (event.gui instanceof GuiMainMenu)
        {
            int height = event.gui.height / 4 + 48;
            event.buttonList.add(new GuiButtonMojangStatus(200, event.gui.width / 2 - 124, height + 63));
        }
    }

    @SubscribeEvent
    public void onPreActionPerformedGui(GuiScreenEvent.ActionPerformedEvent.Pre event)
    {
        if (ConfigManager.enableCustomServerSelectionGui)
        {
            if (event.gui instanceof GuiMainMenu)
            {
                if (event.button.id == 2)
                {
                    event.setCanceled(true);
                    event.button.func_146113_a(this.mc.getSoundHandler());
                    this.mc.displayGuiScreen(new GuiMultiplayerCustom(new GuiMainMenu()));
                }
                if (event.button.id == 14)
                {
                    event.setCanceled(true);
                    event.button.func_146113_a(this.mc.getSoundHandler());
                }
            }
        }
    }

    @SubscribeEvent
    public void onPostActionPerformedGui(GuiScreenEvent.ActionPerformedEvent.Post event)
    {
        if (event.gui instanceof GuiIngameMenu)
        {
            switch (event.button.id)
            {
            case 200:
                CommonHandler.openLink("https://twitch.streamlabs.com/stevekung");
                break;
            case 201:
                CommonHandler.openLink("https://tipme.in.th/stevekung");
                break;
            }
        }
        if (event.gui instanceof GuiMainMenu)
        {
            if (event.button.id == 200)
            {
                this.mc.displayGuiScreen(new GuiMojangStatusChecker(event.gui));
            }
        }
    }

    @SubscribeEvent
    public void onRenderGui(GuiScreenEvent.DrawScreenEvent.Post event)
    {
        if (event.gui instanceof GuiIngameMenu)
        {
            event.gui.drawString(this.mc.fontRenderer, "Support Indicatia!", event.gui.width - 120, 8, 65481);
        }
    }

    private boolean canRenderName(EntityLivingBase entity)
    {
        if (entity instanceof EntityLiving)
        {
            return this.getDefaultCanRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || ((EntityLiving) entity).hasCustomNameTag() && entity == RenderManager.instance.field_147941_i);
        }
        return this.getDefaultCanRenderName(entity);
    }

    private boolean getDefaultCanRenderName(EntityLivingBase entity)
    {
        return Minecraft.isGuiEnabled() && entity != RenderManager.instance.livingPlayer && !entity.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer) && entity.riddenByEntity == null;
    }

    private static void runAFK(EntityClientPlayerMP player)
    {
        if (CommonHandler.isAFK)
        {
            CommonHandler.afkTicks++;
            int tick = CommonHandler.afkTicks;
            int messageMin = 1200 * ConfigManager.afkMessageTime;
            String s = "s";
            float angle = tick % 2 == 0 ? 0.0001F : -0.0001F;

            if (tick == 0)
            {
                s = "";
            }
            if (ConfigManager.enableAFKMessage)
            {
                if (tick % messageMin == 0)
                {
                    String reason = CommonHandler.afkReason;
                    reason = reason.isEmpty() ? "" : ", Reason : " + reason;
                    player.sendChatMessage("AFK : " + StringUtils.ticksToElapsedTime(tick) + " minute" + s + reason);
                }
            }

            if (CommonHandler.afkMode.equals("idle"))
            {
                player.setAngles(angle, angle);
            }
            else if (CommonHandler.afkMode.equals("360"))
            {
                player.setAngles(1.0F, 0.0F);
            }
            else if (CommonHandler.afkMode.equals("360_move"))
            {
                player.setAngles(1.0F, 0.0F);
                CommonHandler.afkMoveTicks++;
                CommonHandler.afkMoveTicks %= 8;
            }
            else
            {
                player.setAngles(angle, angle);
                CommonHandler.afkMoveTicks++;
                CommonHandler.afkMoveTicks %= 8;
            }
        }
        else
        {
            CommonHandler.afkTicks = 0;
        }
    }

    private static void replaceGui(Minecraft mc, GuiScreen currentScreen)
    {
        if (currentScreen != null)
        {
            if (currentScreen instanceof GuiChat && !(currentScreen instanceof GuiNewChatUtil || currentScreen instanceof GuiSleepMP))
            {
                mc.displayGuiScreen(CommonHandler.chatGui);
            }
            if (currentScreen instanceof GuiSleepMP && !(currentScreen instanceof GuiSleepMPNew))
            {
                mc.displayGuiScreen(CommonHandler.sleepGui);
            }
            if (currentScreen instanceof GuiSleepMPNew && !mc.thePlayer.isPlayerSleeping())
            {
                mc.displayGuiScreen((GuiScreen)null);
            }
        }
    }

    private static void stopCommandTicks()
    {
        if (CommonHandler.isAFK)
        {
            CommonHandler.isAFK = false;
            CommonHandler.afkReason = "";
            CommonHandler.afkTicks = 0;
            CommonHandler.afkMoveTicks = 0;
            CommonHandler.afkMode = "idle";
            ModLogger.info("Stopping AFK Command");
        }
        if (CommonHandler.autoClick)
        {
            CommonHandler.autoClickTicks = 0;
            CommonHandler.autoClickMode = "left";
            ModLogger.info("Stopping Auto Click Command");
        }
    }

    private static void getRealTimeServerPing(ServerData server)
    {
        CommonHandler.serverPinger.submit(() ->
        {
            try
            {
                ServerAddress address = ServerAddress.func_78860_a(server.serverIP);
                NetworkManager manager = NetworkManager.provideLanClient(InetAddress.getByName(address.getIP()), address.getPort());

                manager.setNetHandler(new INetHandlerStatusClient()
                {
                    @Override
                    public void handleServerInfo(S00PacketServerInfo packet)
                    {
                        manager.scheduleOutboundPacket(new C01PacketPing(Minecraft.getSystemTime()));
                    }

                    @Override
                    public void handlePong(S01PacketPong packet)
                    {
                        long i = packet.func_149292_c();
                        long j = Minecraft.getSystemTime();
                        CommonHandler.currentServerPing = (int) (j - i);
                    }

                    @Override
                    public void onDisconnect(IChatComponent component) {}

                    @Override
                    public void onConnectionStateTransition(EnumConnectionState state1, EnumConnectionState state2) {}

                    @Override
                    public void onNetworkTick() {}
                });
                manager.scheduleOutboundPacket(new C00Handshake(5, address.getIP(), address.getPort(), EnumConnectionState.STATUS));
                manager.scheduleOutboundPacket(new C00PacketServerQuery());
            }
            catch (Exception e) {}
        });
    }

    private static void openLink(String url)
    {
        try
        {
            URI uri = new URI(url);
            Desktop.getDesktop().browse(uri);
        }
        catch (Exception e)
        {
            ModLogger.info("Couldn't open link {}", url);
            e.printStackTrace();
        }
    }

    private static void printVersionMessage(JsonUtil json, EntityClientPlayerMP player)
    {
        if (ConfigManager.enableVersionChecker)
        {
            if (!IndicatiaMod.CHECK_NO_CONNECTION && VersionChecker.INSTANCE.noConnection())
            {
                player.addChatMessage(json.text("Unable to check latest version, Please check your internet connection").setChatStyle(json.red()));
                player.addChatMessage(json.text(VersionChecker.INSTANCE.getExceptionMessage()).setChatStyle(json.red()));
                IndicatiaMod.CHECK_NO_CONNECTION = true;
                return;
            }
            if (!IndicatiaMod.FOUND_LATEST && !IndicatiaMod.CHECK_NO_CONNECTION && VersionChecker.INSTANCE.isLatestVersion())
            {
                player.addChatMessage(json.text("New version of ").appendSibling(json.text("Indicatia").setChatStyle(json.style().setColor(EnumChatFormatting.AQUA)).appendSibling(json.text(" is available ").setChatStyle(json.white()).appendSibling(json.text("v" + VersionChecker.INSTANCE.getLatestVersion().replace("[" + IndicatiaMod.MC_VERSION + "]=", "")).setChatStyle(json.style().setColor(EnumChatFormatting.GREEN)).appendSibling(json.text(" for ").setChatStyle(json.white()).appendSibling(json.text("MC-" + IndicatiaMod.MC_VERSION).setChatStyle(json.style().setColor(EnumChatFormatting.GOLD))))))));
                player.addChatMessage(json.text("Download Link ").setChatStyle(json.style().setColor(EnumChatFormatting.YELLOW)).appendSibling(json.text("[CLICK HERE]").setChatStyle(json.style().setColor(EnumChatFormatting.BLUE).setChatHoverEvent(json.hover(HoverEvent.Action.SHOW_TEXT, json.text("Click Here!").setChatStyle(json.style().setColor(EnumChatFormatting.DARK_GREEN)))).setChatClickEvent(json.click(ClickEvent.Action.OPEN_URL, IndicatiaMod.URL)))));
                IndicatiaMod.FOUND_LATEST = true;
            }
            if (!IndicatiaMod.SHOW_ANNOUNCE_MESSAGE && !IndicatiaMod.CHECK_NO_CONNECTION)
            {
                for (String log : VersionChecker.INSTANCE.getAnnounceMessage())
                {
                    if (ConfigManager.enableAnnounceMessage)
                    {
                        player.addChatMessage(json.text(log).setChatStyle(json.style().setColor(EnumChatFormatting.GRAY)));
                    }
                }
                player.addChatMessage(json.text("To read Indicatia full change log. Use /inchangelog command!").setChatStyle(json.gray().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/inchangelog"))));
                IndicatiaMod.SHOW_ANNOUNCE_MESSAGE = true;
            }
        }
    }

    private static void replacingPlayerModel(EntityClientPlayerMP player)
    {
        RenderPlayer render = (RenderPlayer) RenderManager.instance.getEntityRenderObject(player);

        if (ConfigManager.enableAlternatePlayerModel)
        {
            if (!render.mainModel.getClass().equals(ModelBipedNew.class))
            {
                render.mainModel = new ModelBipedNew(0.0F);
                render.modelBipedMain = (ModelBipedNew)render.mainModel;
                render.modelArmorChestplate = new ModelBipedNew(1.0F);
                render.modelArmor = new ModelBipedNew(0.5F);
                ModLogger.info("Set player model to {}", ModelBipedNew.class.getName());
            }
        }
        else
        {
            if (!render.mainModel.getClass().equals(ModelBiped.class))
            {
                render.mainModel = new ModelBiped(0.0F);
                render.modelBipedMain = (ModelBiped)render.mainModel;
                render.modelArmorChestplate = new ModelBiped(1.0F);
                render.modelArmor = new ModelBiped(0.5F);
                ModLogger.info("Set player model to {}", ModelBiped.class.getName());
            }
        }
    }

    private static void renderEntityName(Minecraft mc, EntityLivingBase entity, String str, double x, double y, double z)
    {
        int maxDistance = 64;
        double d0 = entity.getDistanceSqToEntity(mc.renderViewEntity);

        if (d0 <= maxDistance * maxDistance)
        {
            boolean flag = entity.isSneaking();
            float f = RenderManager.instance.playerViewY;
            float f1 = RenderManager.instance.playerViewX;
            boolean flag1 = RenderManager.instance.options.thirdPersonView == 2;
            float f2 = entity.height + 0.5F - (flag ? 0.25F : 0.0F);
            int i = "deadmau5".equals(str) ? -10 : 0;
            CommonHandler.drawNameplate(RenderManager.instance.getFontRenderer(), str, (float)x, (float)y + f2, (float)z, i, f, f1, flag1, flag);
        }
    }

    private static void drawNameplate(FontRenderer fontRenderer, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-viewerYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef((isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-0.025F, -0.025F, 0.025F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);

        if (!isSneaking)
        {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        int i = fontRenderer.getStringWidth(str) / 2;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        tessellator.addVertex(-i - 1, -1.0D, 0.0D);
        tessellator.addVertex(-i - 1, 8.0D, 0.0D);
        tessellator.addVertex(i + 1, 8.0D, 0.0D);
        tessellator.addVertex(i + 1, -1.0D, 0.0D);
        tessellator.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        if (!isSneaking)
        {
            fontRenderer.drawString(str, -fontRenderer.getStringWidth(str) / 2, verticalShift, 553648127);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        GL11.glDepthMask(true);
        fontRenderer.drawString(str, -fontRenderer.getStringWidth(str) / 2, verticalShift, isSneaking ? 553648127 : -1);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
}