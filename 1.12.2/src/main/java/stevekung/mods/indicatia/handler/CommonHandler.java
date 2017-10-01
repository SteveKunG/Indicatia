package stevekung.mods.indicatia.handler;

import java.awt.Desktop;
import java.net.InetAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.item.EnumAction;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.network.status.server.SPacketServerInfo;
import net.minecraft.util.EnumHand;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import stevekung.mods.indicatia.config.ConfigGuiFactory;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.gui.*;
import stevekung.mods.indicatia.renderer.LayerAllArmor;
import stevekung.mods.indicatia.renderer.LayerCapeNew;
import stevekung.mods.indicatia.renderer.LayerCustomCape;
import stevekung.mods.indicatia.util.*;

public class CommonHandler
{
    private JsonUtil json;
    private static final Pattern nickPattern = Pattern.compile("^You are now nicked as (?<nick>\\w+)!");
    public static GuiPlayerTabOverlayNew overlayPlayerList;
    private final Minecraft mc;
    public static final List<Long> LEFT_CLICK = new ArrayList<>();
    public static final List<Long> RIGHT_CLICK = new ArrayList<>();
    public static final GuiNewChatUtil chatGui = new GuiNewChatUtil();
    public static final GuiSleepMPNew sleepGui = new GuiSleepMPNew();
    public static final GuiNewChatUtil chatGuiSlash = new GuiNewChatUtil("/");
    public static final GuiCustomCape customCapeGui = new GuiCustomCape();
    public static final GuiDonator donatorGui = new GuiDonator();
    public static int currentServerPing;
    private static final ThreadPoolExecutor serverPinger = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Real Time Server Pinger #%d").setDaemon(true).build());
    private static int pendingPingTicks = 100;
    private static boolean initLayer = true;
    private static EnumAction[] cachedAction = EnumAction.values();

    // AFK Stuff
    public static boolean isAFK;
    public static String afkMode = "idle";
    public static String afkReason;
    public static int afkMoveTicks;
    public static int afkTicks;

    public static boolean autoClick;
    public static String autoClickMode = "right";
    public static int autoClickTicks = 0;

    private static long sneakTimeOld = 0L;
    private static boolean sneakingOld = false;

    private int closeScreenTicks;
    private static boolean printAutoGG;
    private static int printAutoGGTicks;

    public CommonHandler(Minecraft mc)
    {
        this.json = IndicatiaMod.json;
        this.mc = mc;
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event)
    {
        if (event.getModID().equalsIgnoreCase(IndicatiaMod.MOD_ID))
        {
            ConfigManager.syncConfig(false);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (IndicatiaMod.isSteveKunG())
        {
            RenderUtil.renderGlowingEntity();
        }
        if (this.mc.player != null)
        {
            if (event.phase == TickEvent.Phase.START)
            {
                CommonHandler.runAFK(this.mc.player);
                CommonHandler.printVersionMessage(this.json, this.mc.player);
                CommonHandler.processAutoGG(this.mc);
                CommonHandler.getHypixelNickedPlayer(this.mc);
                CapeUtil.loadCapeTexture();

                if (this.closeScreenTicks > 1)
                {
                    --this.closeScreenTicks;
                }
                if (this.closeScreenTicks == 1)
                {
                    this.mc.displayGuiScreen((GuiScreen)null);
                    this.closeScreenTicks = 0;
                }

                if (CommonHandler.pendingPingTicks > 0 && this.mc.getCurrentServerData() != null)
                {
                    CommonHandler.pendingPingTicks--;

                    if (CommonHandler.pendingPingTicks == 0)
                    {
                        CommonHandler.getRealTimeServerPing(this.mc.getCurrentServerData());
                        CommonHandler.pendingPingTicks = 100;
                    }
                }
                if (IndicatiaMod.isSteveKunG() && CommonHandler.autoClick)
                {
                    CommonHandler.autoClickTicks++;

                    if (CommonHandler.autoClickMode.equals("left"))
                    {
                        this.mc.sendClickBlockToController(true);
                    }
                    else
                    {
                        if (CommonHandler.autoClickTicks % 4 == 0)
                        {
                            this.mc.rightClickMouse();
                        }
                    }
                }

                for (EnumAction action : CommonHandler.getCachedAction())
                {
                    if (action != EnumAction.NONE)
                    {
                        if (ConfigManager.enableAdditionalBlockhitAnimation && IndicatiaMod.MC.gameSettings.keyBindAttack.isKeyDown() && IndicatiaMod.MC.player != null && IndicatiaMod.MC.objectMouseOver != null && IndicatiaMod.MC.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && !IndicatiaMod.MC.player.getHeldItemMainhand().isEmpty() && IndicatiaMod.MC.player.getHeldItemMainhand().getItemUseAction() == action)
                        {
                            IndicatiaMod.MC.player.swingArm(EnumHand.MAIN_HAND);
                        }
                    }
                }
            }
            if (!(this.mc.player.movementInput instanceof MovementInputFromOptionsIU))
            {
                this.mc.player.movementInput = new MovementInputFromOptionsIU(this.mc.gameSettings);
            }
            CommonHandler.replaceGui(this.mc, this.mc.currentScreen);
        }
        GuiIngameForge.renderBossHealth = ConfigManager.enableRenderBossHealthStatus;
        GuiIngameForge.renderObjective = ConfigManager.enableRenderScoreboard;
    }

    @SubscribeEvent
    public void onMouseClick(MouseEvent event)
    {
        if (event.getButton() == 0 && event.isButtonstate())
        {
            CommonHandler.LEFT_CLICK.add(Long.valueOf(System.currentTimeMillis()));
        }
        if (event.getButton() == 1 && event.isButtonstate())
        {
            CommonHandler.RIGHT_CLICK.add(Long.valueOf(System.currentTimeMillis()));
        }
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event)
    {
        RenderLivingBase renderer = event.getRenderer();
        @SuppressWarnings("unchecked")
        List<LayerRenderer> layerLists = renderer.layerRenderers;
        EntityLivingBase entity = event.getEntity();
        RenderManager manager = this.mc.getRenderManager();

        if (entity instanceof AbstractClientPlayer)
        {
            RenderPlayer renderDefault = manager.getSkinMap().get("default");
            RenderPlayer renderSlim = manager.getSkinMap().get("slim");
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor<>(renderDefault, entity), renderer, entity);
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor<>(renderSlim, entity), renderer, entity);
            CommonHandler.replaceCapeLayer(layerLists, new LayerCapeNew(renderDefault));
            CommonHandler.replaceCapeLayer(layerLists, new LayerCapeNew(renderSlim));
        }
        else if (entity instanceof EntityZombieVillager)
        {
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor<>(new RenderZombieVillager(manager), entity), renderer, entity);
        }
        else if (entity instanceof EntityGiantZombie)
        {
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor<>(new RenderGiantZombie(manager, 6.0F), entity), renderer, entity);
        }
        else if (entity instanceof EntityZombie && !(entity instanceof EntityZombieVillager))
        {
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor<>(new RenderZombie(manager), entity), renderer, entity);
        }
        else if (entity instanceof AbstractSkeleton)
        {
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor<>(new RenderSkeleton(manager), entity), renderer, entity);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event)
    {
        if (CommonHandler.initLayer)
        {
            RenderPlayer renderDefault = this.mc.getRenderManager().getSkinMap().get("default");
            RenderPlayer renderSlim = this.mc.getRenderManager().getSkinMap().get("slim");
            renderDefault.addLayer(new LayerCustomCape(renderDefault));
            renderSlim.addLayer(new LayerCustomCape(renderSlim));
            CommonHandler.initLayer = false;
        }
    }

    @SubscribeEvent
    public void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        this.mc.ingameGUI.persistantChatGUI = new GuiNewChatFast();
        CommonHandler.overlayPlayerList = new GuiPlayerTabOverlayNew();
    }

    @SubscribeEvent
    public void onDisconnectedFromServerEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        this.closeScreenTicks = 0;
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
            if (ConfigManager.enableSmoothEyeHeight)
            {
                if (this.mc.player != null)
                {
                    this.mc.player.eyeHeight = CommonHandler.getSmoothEyeHeight(this.mc.player);
                }
            }
            InfoUtil.INSTANCE.processMouseOverEntity(this.mc);
        }
    }

    @SubscribeEvent
    public void onPressKey(InputEvent.KeyInputEvent event)
    {
        if (this.mc.currentScreen == null && this.mc.gameSettings.keyBindCommand.isPressed())
        {
            this.mc.displayGuiScreen(CommonHandler.chatGuiSlash);
        }
        if (KeyBindingHandler.KEY_QUICK_CONFIG.isKeyDown())
        {
            this.mc.displayGuiScreen(new ConfigGuiFactory.GuiMainConfig(this.mc.currentScreen));
        }
        if (KeyBindingHandler.KEY_REC_COMMAND.isKeyDown())
        {
            HUDRenderHandler.recordEnable = !HUDRenderHandler.recordEnable;
        }
        if (ConfigManager.enableCustomCape && KeyBindingHandler.KEY_CUSTOM_CAPE_GUI.isKeyDown())
        {
            this.mc.displayGuiScreen(CommonHandler.customCapeGui);
        }
        if (ExtendedConfig.TOGGLE_SPRINT_USE_MODE.equals("key_binding") && KeyBindingHandler.KEY_TOGGLE_SPRINT.isKeyDown())
        {
            ExtendedConfig.TOGGLE_SPRINT = !ExtendedConfig.TOGGLE_SPRINT;
            InfoUtil.INSTANCE.setOverlayMessage(this.json.text(ExtendedConfig.TOGGLE_SPRINT ? "Toggle Sprint Enabled" : "Toggle Sprint Disabled").getFormattedText(), false);
            ExtendedConfig.save();
        }
        if (ExtendedConfig.TOGGLE_SNEAK_USE_MODE.equals("key_binding") && KeyBindingHandler.KEY_TOGGLE_SNEAK.isKeyDown())
        {
            ExtendedConfig.TOGGLE_SNEAK = !ExtendedConfig.TOGGLE_SNEAK;
            InfoUtil.INSTANCE.setOverlayMessage(this.json.text(ExtendedConfig.TOGGLE_SNEAK ? "Toggle Sneak Enabled" : "Toggle Sneak Disabled").getFormattedText(), false);
            ExtendedConfig.save();
        }
        if (ExtendedConfig.AUTO_SWIM_USE_MODE.equals("key_binding") && KeyBindingHandler.KEY_AUTO_SWIM.isKeyDown())
        {
            ExtendedConfig.AUTO_SWIM = !ExtendedConfig.AUTO_SWIM;
            InfoUtil.INSTANCE.setOverlayMessage(this.json.text(ExtendedConfig.AUTO_SWIM ? "Auto Swim Enabled" : "Auto Swim Disabled").getFormattedText(), false);
            ExtendedConfig.save();
        }
        if (KeyBindingHandler.KEY_DONATOR_GUI.isKeyDown())
        {
            this.mc.displayGuiScreen(CommonHandler.donatorGui);
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event)
    {
        String unformattedText = event.getMessage().getUnformattedText();

        if (InfoUtil.INSTANCE.isHypixel())
        {
            Matcher nickMatcher = CommonHandler.nickPattern.matcher(unformattedText);

            if (event.getType() == ChatType.CHAT)
            {
                if (nickMatcher.matches())
                {
                    ExtendedConfig.HYPIXEL_NICK_NAME = nickMatcher.group("nick");
                    ExtendedConfig.save();
                }
                if (unformattedText.contains("Your nick has been reset!"))
                {
                    ExtendedConfig.HYPIXEL_NICK_NAME = "";
                    ExtendedConfig.save();
                }
                if (IndicatiaMod.isSteveKunG())
                {
                    String dailyText = "Click the link to visit our website and claim your reward: ";
                    String votingText1 = "Today's voting link is ";
                    String votingText2 = "! Follow the instructions on the website to redeem 5,000 XP and 3,000 Arcade Coins!";

                    if (unformattedText.contains(dailyText))
                    {
                        String replacedText = unformattedText.replace(dailyText, "").replace("\n", "");
                        CommonHandler.openLink(replacedText);
                    }
                    if (unformattedText.contains(votingText1))
                    {
                        String replacedText = unformattedText.replace(votingText1, "");
                        replacedText = TextFormatting.getTextWithoutFormattingCodes(replacedText);
                        replacedText = replacedText.replace(votingText2, "");

                        if (replacedText.contains("vote.hypixel.net/0"))
                        {
                            CommonHandler.openLink("http://minecraftservers.org/vote/221843");
                        }
                        if (replacedText.contains("vote.hypixel.net/1"))
                        {
                            CommonHandler.openLink("http://minecraft-server-list.com/server/292028/vote/");
                        }
                        this.closeScreenTicks = 20;
                    }
                    if (unformattedText.contains("Get free coins by clicking"))
                    {
                        this.mc.player.sendChatMessage("/tip all");
                    }
                }

                // auto kick party player
                if (unformattedText.contains("isn't online!"))
                {
                    List<String> words = Arrays.asList(unformattedText.split("[ ]"));
                    Collections.reverse(words);
                    StringBuilder reverseString = new StringBuilder();

                    for (String word : words)
                    {
                        reverseString.append(word + " ");
                    }

                    reverseString.substring(0, reverseString.length() - 1);
                    String message = reverseString.toString().replace("online! isn't ", "");
                    String[] name = message.trim().split("\\s+");
                    this.mc.player.sendChatMessage("/p remove " + name[0]);
                }
            }
        }
    }

    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event)
    {
        if (event.getGui() instanceof GuiIngameMenu)
        {
            event.getButtonList().add(new GuiButton(200, event.getGui().width - 145, 20, 135, 20, "Paypal"));
            event.getButtonList().add(new GuiButton(201, event.getGui().width - 145, 41, 135, 20, "Truemoney"));
        }
        if (event.getGui() instanceof GuiMainMenu)
        {
            int height = event.getGui().height / 4 + 48;
            event.getButtonList().add(new GuiButtonMojangStatus(200, event.getGui().width / 2 - 124, height + 63));
        }
    }

    @SubscribeEvent
    public void onActionGui(GuiScreenEvent.ActionPerformedEvent.Post event)
    {
        if (event.getGui() instanceof GuiIngameMenu)
        {
            switch (event.getButton().id)
            {
            case 200:
                CommonHandler.openLink("https://twitch.streamlabs.com/stevekung");
                break;
            case 201:
                CommonHandler.openLink("https://tipme.in.th/stevekung");
                break;
            }
        }
        if (event.getGui() instanceof GuiMainMenu)
        {
            if (event.getButton().id == 200)
            {
                this.mc.displayGuiScreen(new GuiMojangStatusChecker(event.getGui()));
            }
        }
    }

    @SubscribeEvent
    public void onRenderGui(GuiScreenEvent.DrawScreenEvent.Post event)
    {
        if (event.getGui() instanceof GuiIngameMenu)
        {
            event.getGui().drawString(this.mc.fontRenderer, "Support Indicatia!", event.getGui().width - 120, 8, 65481);
        }
    }

    private static void getRealTimeServerPing(ServerData server)
    {
        CommonHandler.serverPinger.submit(() ->
        {
            try
            {
                ServerAddress address = ServerAddress.fromString(server.serverIP);
                NetworkManager manager = NetworkManager.createNetworkManagerAndConnect(InetAddress.getByName(address.getIP()), address.getPort(), false);

                manager.setNetHandler(new INetHandlerStatusClient()
                {
                    private long currentSystemTime = 0L;

                    @Override
                    public void handleServerInfo(SPacketServerInfo packet)
                    {
                        this.currentSystemTime = Minecraft.getSystemTime();
                        manager.sendPacket(new CPacketPing(this.currentSystemTime));
                    }

                    @Override
                    public void handlePong(SPacketPong packet)
                    {
                        long i = this.currentSystemTime;
                        long j = Minecraft.getSystemTime();
                        CommonHandler.currentServerPing = (int) (j - i);
                    }

                    @Override
                    public void onDisconnect(ITextComponent component) {}
                });
                manager.sendPacket(new C00Handshake(address.getIP(), address.getPort(), EnumConnectionState.STATUS, true));
                manager.sendPacket(new CPacketServerQuery());
            }
            catch (Exception e) {}
        });
    }

    private static void getHypixelNickedPlayer(Minecraft mc)
    {
        if (InfoUtil.INSTANCE.isHypixel() && mc.currentScreen instanceof GuiEditSign)
        {
            GuiEditSign gui = (GuiEditSign) mc.currentScreen;

            if (gui.tileSign != null)
            {
                ExtendedConfig.HYPIXEL_NICK_NAME = gui.tileSign.signText[0].getUnformattedText();
                ExtendedConfig.save();
            }
        }
    }

    private static void runAFK(EntityPlayerSP player)
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
                player.turn(angle, angle);
            }
            else if (CommonHandler.afkMode.equals("360"))
            {
                player.turn(1.0F, 0.0F);
            }
            else if (CommonHandler.afkMode.equals("360_move"))
            {
                player.turn(1.0F, 0.0F);
                CommonHandler.afkMoveTicks++;
                CommonHandler.afkMoveTicks %= 8;
            }
            else
            {
                player.turn(angle, angle);
                CommonHandler.afkMoveTicks++;
                CommonHandler.afkMoveTicks %= 8;
            }
        }
        else
        {
            CommonHandler.afkTicks = 0;
        }
    }

    private static float getSmoothEyeHeight(EntityPlayerSP player)
    {
        if (CommonHandler.sneakingOld != player.isSneaking() || CommonHandler.sneakTimeOld <= 0L)
        {
            CommonHandler.sneakTimeOld = System.currentTimeMillis();
        }

        CommonHandler.sneakingOld = player.isSneaking();
        float defaultEyeHeight = 1.62F;
        double sneakPress = 0.0004D;
        double sneakValue = 0.015D;
        int sneakTime = -50;
        long systemTime = 58L;

        if (player.isSneaking())
        {
            int sneakSystemTime = (int)(CommonHandler.sneakTimeOld + systemTime - System.currentTimeMillis());

            if (sneakSystemTime > sneakTime)
            {
                defaultEyeHeight += (float)(sneakSystemTime * sneakPress);

                if (defaultEyeHeight < 0.0F || defaultEyeHeight > 10.0F)
                {
                    defaultEyeHeight = 1.54F;
                }
            }
            else
            {
                defaultEyeHeight = (float)(defaultEyeHeight - sneakValue);
            }
        }
        else
        {
            int sneakSystemTime = (int)(CommonHandler.sneakTimeOld + systemTime - System.currentTimeMillis());

            if (sneakSystemTime > sneakTime)
            {
                defaultEyeHeight -= (float)(sneakSystemTime * sneakPress);
                defaultEyeHeight = (float)(defaultEyeHeight - sneakValue);

                if (defaultEyeHeight < 0.0F)
                {
                    defaultEyeHeight = 1.62F;
                }
            }
            else
            {
                defaultEyeHeight -= 0.0F;
            }
        }
        return defaultEyeHeight;
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
            if (currentScreen instanceof GuiSleepMPNew && !mc.player.isPlayerSleeping())
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

    private static void replaceArmorLayer(List<LayerRenderer> layerLists, LayerRenderer newLayer, RenderLivingBase render, EntityLivingBase entity)
    {
        int armorLayerIndex = -1;

        if (ConfigManager.enableOldArmorRender)
        {
            for (int i = 0; i < layerLists.size(); i++)
            {
                LayerRenderer layer = layerLists.get(i);

                if (layer instanceof LayerBipedArmor)
                {
                    armorLayerIndex = i;
                }
            }
            if (armorLayerIndex >= 0)
            {
                layerLists.set(armorLayerIndex, newLayer);
            }
        }
        else
        {
            for (int i = 0; i < layerLists.size(); i++)
            {
                LayerRenderer layer = layerLists.get(i);

                if (layer instanceof LayerAllArmor)
                {
                    armorLayerIndex = i;
                }
            }
            if (armorLayerIndex >= 0)
            {
                if (entity instanceof AbstractClientPlayer)
                {
                    layerLists.set(armorLayerIndex, new LayerBipedArmor(render));
                }
                else if ((entity instanceof EntityZombie || entity instanceof EntityGiantZombie) && !(entity instanceof EntityZombieVillager))
                {
                    layerLists.set(armorLayerIndex, new LayerBipedArmor(render)
                    {
                        @Override
                        protected void initArmor()
                        {
                            this.modelLeggings = new ModelZombie(0.5F, true);
                            this.modelArmor = new ModelZombie(1.0F, true);
                        }
                    });
                }
                else if (entity instanceof AbstractSkeleton)
                {
                    layerLists.set(armorLayerIndex, new LayerBipedArmor(render)
                    {
                        @Override
                        protected void initArmor()
                        {
                            this.modelLeggings = new ModelSkeleton(0.5F, true);
                            this.modelArmor = new ModelSkeleton(1.0F, true);
                        }
                    });
                }
                else if (entity instanceof EntityZombieVillager)
                {
                    layerLists.set(armorLayerIndex, new LayerBipedArmor(render)
                    {
                        @Override
                        protected void initArmor()
                        {
                            this.modelLeggings = new ModelZombieVillager(0.5F, 0.0F, true);
                            this.modelArmor = new ModelZombieVillager(1.0F, 0.0F, true);
                        }
                    });
                }
            }
        }
    }

    private static void replaceCapeLayer(List<LayerRenderer> layerLists, LayerRenderer newLayer)
    {
        int capeLayerIndex = -1;

        for (int i = 0; i < layerLists.size(); i++)
        {
            LayerRenderer layer = layerLists.get(i);

            if (layer instanceof LayerCape)
            {
                capeLayerIndex = i;
            }
        }
        if (capeLayerIndex >= 0)
        {
            layerLists.set(capeLayerIndex, newLayer);
        }
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

    private static void printVersionMessage(JsonUtil json, EntityPlayerSP player)
    {
        if (ConfigManager.enableVersionChecker)
        {
            if (!IndicatiaMod.CHECK_NO_CONNECTION && VersionChecker.INSTANCE.noConnection())
            {
                player.sendMessage(json.text("Unable to check latest version, Please check your internet connection").setStyle(json.red()));
                player.sendMessage(json.text(VersionChecker.INSTANCE.getExceptionMessage()).setStyle(json.red()));
                IndicatiaMod.CHECK_NO_CONNECTION = true;
                return;
            }
            if (!IndicatiaMod.FOUND_LATEST && !IndicatiaMod.CHECK_NO_CONNECTION && VersionChecker.INSTANCE.isLatestVersion())
            {
                player.sendMessage(json.text("New version of ").appendSibling(json.text("Indicatia").setStyle(json.style().setColor(TextFormatting.AQUA)).appendSibling(json.text(" is available ").setStyle(json.white()).appendSibling(json.text("v" + VersionChecker.INSTANCE.getLatestVersion().replace("[" + IndicatiaMod.MC_VERSION + "]=", "")).setStyle(json.style().setColor(TextFormatting.GREEN)).appendSibling(json.text(" for ").setStyle(json.white()).appendSibling(json.text("MC-" + IndicatiaMod.MC_VERSION).setStyle(json.style().setColor(TextFormatting.GOLD))))))));
                player.sendMessage(json.text("Download Link ").setStyle(json.style().setColor(TextFormatting.YELLOW)).appendSibling(json.text("[CLICK HERE]").setStyle(json.style().setColor(TextFormatting.BLUE).setHoverEvent(json.hover(HoverEvent.Action.SHOW_TEXT, json.text("Click Here!").setStyle(json.style().setColor(TextFormatting.DARK_GREEN)))).setClickEvent(json.click(ClickEvent.Action.OPEN_URL, IndicatiaMod.URL)))));
                IndicatiaMod.FOUND_LATEST = true;
            }
            if (!IndicatiaMod.SHOW_ANNOUNCE_MESSAGE && !IndicatiaMod.CHECK_NO_CONNECTION)
            {
                for (String log : VersionChecker.INSTANCE.getAnnounceMessage())
                {
                    if (ConfigManager.enableAnnounceMessage)
                    {
                        player.sendMessage(json.text(log).setStyle(json.style().setColor(TextFormatting.GRAY)));
                    }
                }
                player.sendMessage(json.text("To read Indicatia full change log. Use /inchangelog command!").setStyle(json.gray().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/inchangelog"))));
                IndicatiaMod.SHOW_ANNOUNCE_MESSAGE = true;
            }
        }
    }

    private static void processAutoGG(Minecraft mc)
    {
        if (mc.ingameGUI.displayedTitle.isEmpty() && !ConfigManager.endGameMessage.isEmpty())
        {
            CommonHandler.printAutoGG = true;
            CommonHandler.printAutoGGTicks = 0;
        }
        if (CommonHandler.printAutoGG && CommonHandler.printAutoGGTicks < ConfigManager.endGameTitleTime)
        {
            CommonHandler.printAutoGGTicks++;
        }

        for (String message : ConfigManager.endGameTitleMessage.split(","))
        {
            String messageToLower = TextFormatting.getTextWithoutFormattingCodes(message).toLowerCase();
            String displayTitleMessage = TextFormatting.getTextWithoutFormattingCodes(mc.ingameGUI.displayedTitle).toLowerCase();

            if (displayTitleMessage.contains(messageToLower) && CommonHandler.printAutoGGTicks == ConfigManager.endGameTitleTime)
            {
                mc.player.sendChatMessage(ConfigManager.endGameMessage);
                CommonHandler.printAutoGG = false;
                CommonHandler.printAutoGGTicks = 0;
            }
        }
    }

    private static EnumAction[] getCachedAction()
    {
        return CommonHandler.cachedAction;
    }
}