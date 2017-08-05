package stevekung.mods.indicatia.handler;

import java.awt.Desktop;
import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
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
import stevekung.mods.indicatia.renderer.*;
import stevekung.mods.indicatia.util.*;

public class CommonHandler
{
    private JsonUtil json;
    public static final Map<String, Integer> PLAYER_PING_MAP = Maps.newHashMap();
    private static final List<String> HYPIXEL_PLAYER_LIST = new ArrayList<>();
    private final Pattern nickPattern = Pattern.compile("^You are now nicked as (?<nick>\\w+)!");
    public static GuiPlayerTabOverlayNew overlayPlayerList;
    private final Minecraft mc;
    public static final List<Long> LEFT_CLICK = new ArrayList<>();
    public static final List<Long> RIGHT_CLICK = new ArrayList<>();
    public static final GuiNewChatUtil chatGui = new GuiNewChatUtil();
    public static final GuiSleepMPNew sleepGui = new GuiSleepMPNew();
    public static final GuiNewChatUtil chatGuiSlash = new GuiNewChatUtil("/");
    public static final GuiCustomCape customCapeGui = new GuiCustomCape();
    public static final GuiDonator donatorGui = new GuiDonator();
    private static boolean foundUnnick;

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
    private static boolean setNewRender;
    private static boolean setNewRenderInitial;
    private int setNewRenderTicks;

    private int closeScreenTicks;
    private static boolean printAutoGG;
    private static int printAutoGGTicks;

    public CommonHandler(Minecraft mc)
    {
        this.json = new JsonUtil();
        this.mc = mc;
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event)
    {
        if (event.modID.equalsIgnoreCase(IndicatiaMod.MOD_ID))
        {
            ConfigManager.syncConfig(false);
            CommonHandler.setNewRender = false;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (!CommonHandler.setNewRender && this.mc.thePlayer != null || this.setNewRenderTicks == 20)
        {
            Render render = this.mc.getRenderManager().getEntityRenderObject(this.mc.thePlayer);
            RenderPlayer renderPlayer = (RenderPlayer) render;

            if (ConfigManager.enableAlternatePlayerModel)
            {
                renderPlayer.mainModel = new ModelPlayerNew(0.0F, this.mc.thePlayer.getSkinType().equalsIgnoreCase("slim"));
                ModLogger.info("Set player model to {}", ModelPlayerNew.class.getName());
            }
            else
            {
                renderPlayer.mainModel = new ModelPlayer(0.0F, this.mc.thePlayer.getSkinType().equalsIgnoreCase("slim"));
                ModLogger.info("Set player model to {}", ModelPlayer.class.getName());
            }
            CommonHandler.setNewRender = true;
            this.setNewRenderTicks = 0;
            CommonHandler.setNewRenderInitial = false;
        }
        if (this.mc.thePlayer != null)
        {
            if (InfoUtil.INSTANCE.isHypixel())
            {
                if (CommonHandler.HYPIXEL_PLAYER_LIST.contains(GameProfileUtil.getUsername()) && !CommonHandler.foundUnnick)
                {
                    ExtendedConfig.HYPIXEL_NICK_NAME = GameProfileUtil.getUsername();
                    ExtendedConfig.save();
                    CommonHandler.foundUnnick = true;
                }
                CommonHandler.getPingForNickedPlayer(this.mc);
            }
            if (event.phase == TickEvent.Phase.START)
            {
                CommonHandler.runAFK(this.mc.thePlayer);
                CommonHandler.printVersionMessage(this.json, this.mc.thePlayer);
                CommonHandler.processAutoGG(this.mc);
                CapeUtil.loadCapeTexture();

                if (this.setNewRenderTicks < 20 && CommonHandler.setNewRenderInitial)
                {
                    this.setNewRenderTicks++;
                }
                if (this.closeScreenTicks > 1)
                {
                    --this.closeScreenTicks;
                }
                if (this.closeScreenTicks == 1)
                {
                    this.mc.displayGuiScreen((GuiScreen)null);
                    this.closeScreenTicks = 0;
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
    public void onPreRenderLiving(RenderLivingEvent.Pre event)
    {
        RendererLivingEntity renderer = event.renderer;
        List<LayerRenderer> layerLists = renderer.layerRenderers;
        EntityLivingBase entity = event.entity;
        RenderManager manager = this.mc.getRenderManager();

        if (entity instanceof AbstractClientPlayer)
        {
            RenderPlayer renderDefault = manager.getSkinMap().get("default");
            RenderPlayer renderSlim = manager.getSkinMap().get("slim");
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor(renderDefault, entity), renderer, entity);
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor(renderSlim, entity), renderer, entity);
            CommonHandler.replaceCustomHeadLayer(layerLists, manager);
        }
        else if (entity instanceof EntityZombie && ((EntityZombie)entity).isVillager())
        {
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor(new RenderVillager(manager), entity), renderer, entity);
        }
        else if (entity instanceof EntityGiantZombie)
        {
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor(new RenderGiantZombie(manager, new ModelZombie(), 0.5F, 6.0F), entity), renderer, entity);
        }
        else if (entity instanceof EntityZombie && !((EntityZombie)entity).isVillager())
        {
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor(new RenderZombie(manager), entity), renderer, entity);
        }
        else if (entity instanceof EntitySkeleton)
        {
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor(new RenderSkeleton(manager), entity), renderer, entity);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event)
    {
        RenderPlayer renderDefault = this.mc.getRenderManager().getSkinMap().get("default");
        RenderPlayer renderSlim = this.mc.getRenderManager().getSkinMap().get("slim");
        renderDefault.addLayer(new LayerCustomCape(renderDefault));
        renderSlim.addLayer(new LayerCustomCape(renderSlim));
    }

    @SubscribeEvent
    public void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        this.mc.ingameGUI.persistantChatGUI = new GuiNewChatFast();
        CommonHandler.overlayPlayerList = new GuiPlayerTabOverlayNew();
        CommonHandler.setNewRenderInitial = true;
    }

    @SubscribeEvent
    public void onDisconnectedFromServerEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        CommonHandler.PLAYER_PING_MAP.clear();
        CommonHandler.HYPIXEL_PLAYER_LIST.clear();
        CommonHandler.foundUnnick = false;
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
                if (this.mc.thePlayer != null)
                {
                    this.mc.thePlayer.eyeHeight = CommonHandler.getSmoothEyeHeight(this.mc.thePlayer);
                }
            }
        }
        if (event.phase == TickEvent.Phase.END)
        {
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
        if (KeyBindingHandler.KEY_DONATOR_GUI.isKeyDown())
        {
            this.mc.displayGuiScreen(CommonHandler.donatorGui);
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event)
    {
        String unformattedText = event.message.getUnformattedText();

        if (InfoUtil.INSTANCE.isHypixel())
        {
            Matcher nickMatcher = this.nickPattern.matcher(unformattedText);

            if (event.type == 0)
            {
                if (nickMatcher.matches())
                {
                    ExtendedConfig.HYPIXEL_NICK_NAME = nickMatcher.group("nick");
                    CommonHandler.foundUnnick = false;
                    ExtendedConfig.save();
                }
                if (unformattedText.contains("Your nick has been reset!"))
                {
                    ExtendedConfig.HYPIXEL_NICK_NAME = GameProfileUtil.getUsername();
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
                        replacedText = InfoUtil.INSTANCE.removeFormattingCodes(replacedText);
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
                        this.mc.thePlayer.sendChatMessage("/tip all");
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
                    this.mc.thePlayer.sendChatMessage("/p remove " + name[0]);
                }
            }
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
    public void onActionGui(GuiScreenEvent.ActionPerformedEvent.Post event)
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
            event.gui.drawString(this.mc.fontRendererObj, "Support Indicatia!", event.gui.width - 120, 8, 65481);
        }
    }

    private static void getPingForNickedPlayer(Minecraft mc)
    {
        List<NetworkPlayerInfo> list = Lists.newArrayList(mc.thePlayer.sendQueue.getPlayerInfoMap());
        int maxPlayers = list.size();

        for (int i = 0; i < maxPlayers; ++i)
        {
            if (i < list.size())
            {
                NetworkPlayerInfo connection = list.get(i);
                GameProfile profile = connection.getGameProfile();
                CommonHandler.PLAYER_PING_MAP.put(profile.getName(), connection.getResponseTime());

                if (!CommonHandler.foundUnnick)
                {
                    CommonHandler.HYPIXEL_PLAYER_LIST.add(profile.getName());
                    Set<String> sets = Sets.newHashSet();
                    sets.addAll(CommonHandler.HYPIXEL_PLAYER_LIST);
                    CommonHandler.HYPIXEL_PLAYER_LIST.clear();
                    CommonHandler.HYPIXEL_PLAYER_LIST.addAll(sets);
                }
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

    private static void replaceArmorLayer(List<LayerRenderer> layerLists, LayerRenderer newLayer, RendererLivingEntity render, EntityLivingBase entity)
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
                else if (entity instanceof EntityZombie && !((EntityZombie)entity).isVillager() || entity instanceof EntityGiantZombie)
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
                else if (entity instanceof EntitySkeleton)
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
                else if (entity instanceof EntityZombie && ((EntityZombie)entity).isVillager())
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

    private static void replaceCustomHeadLayer(List<LayerRenderer> layerLists, RenderManager manager)
    {
        int customHeadIndex = -1;

        for (int i = 0; i < layerLists.size(); i++)
        {
            LayerRenderer layer = layerLists.get(i);

            if (layer instanceof LayerCustomHead)
            {
                customHeadIndex = i;
            }
        }
        if (customHeadIndex >= 0)
        {
            layerLists.set(customHeadIndex, new LayerCustomHead(manager.playerRenderer.getMainModel().bipedHead));
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
                player.addChatMessage(json.text("To read Indicatia full change log. Use /inchangelog command!").setChatStyle(json.colorFromConfig("gray").setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/inchangelog"))));
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
            String messageToLower = InfoUtil.INSTANCE.removeFormattingCodes(message).toLowerCase();
            String displayTitleMessage = InfoUtil.INSTANCE.removeFormattingCodes(mc.ingameGUI.displayedTitle).toLowerCase();

            if (displayTitleMessage.contains(messageToLower) && CommonHandler.printAutoGGTicks == ConfigManager.endGameTitleTime)
            {
                mc.thePlayer.sendChatMessage(ConfigManager.endGameMessage);
                CommonHandler.printAutoGG = false;
                CommonHandler.printAutoGGTicks = 0;
            }
        }
    }
}