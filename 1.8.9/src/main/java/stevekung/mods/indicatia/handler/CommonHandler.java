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

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.model.*;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.EnumAction;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;
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
            if (event.phase == TickEvent.Phase.START)
            {
                CommonHandler.runAFK(this.mc.thePlayer);
                CommonHandler.printVersionMessage(this.json, this.mc.thePlayer);
                CommonHandler.processAutoGG(this.mc);
                CommonHandler.replacingPlayerModel(this.mc, this.mc.thePlayer);
                CommonHandler.replacingOthersPlayerModel(this.mc);
                CommonHandler.getHypixelNickedPlayer(this.mc);
                AutoLoginFunction.runAutoLoginFunction();
                AutoLoginFunction.runAutoLoginFunctionTicks(this.mc);
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
                        if (ConfigManager.enableAdditionalBlockhitAnimation && IndicatiaMod.MC.gameSettings.keyBindAttack.isKeyDown() && IndicatiaMod.MC.thePlayer != null && IndicatiaMod.MC.objectMouseOver != null && IndicatiaMod.MC.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && IndicatiaMod.MC.thePlayer.getCurrentEquippedItem() != null && IndicatiaMod.MC.thePlayer.getCurrentEquippedItem().getItemUseAction() == action)
                        {
                            IndicatiaMod.MC.thePlayer.swingItem();
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
        @SuppressWarnings("unchecked")
        List<LayerRenderer> layerLists = renderer.layerRenderers;
        EntityLivingBase entity = event.entity;
        RenderManager manager = this.mc.getRenderManager();

        if (entity == this.mc.thePlayer)
        {
            if (this.mc.thePlayer.getSkinType().equals("slim"))
            {
                RenderPlayer render = manager.getSkinMap().get("slim");
                CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor<>(render, entity), renderer, entity);
                CommonHandler.replaceCustomHeadLayer(layerLists, render);
                CommonHandler.replaceCapeLayer(layerLists, new LayerCapeNew(render));
            }
            else
            {
                RenderPlayer render = manager.getSkinMap().get("default");
                CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor<>(render, entity), renderer, entity);
                CommonHandler.replaceCustomHeadLayer(layerLists, render);
                CommonHandler.replaceCapeLayer(layerLists, new LayerCapeNew(render));
            }
        }
        else if (entity instanceof EntityOtherPlayerMP)
        {
            if (((EntityOtherPlayerMP) entity).getSkinType().equals("slim"))
            {
                RenderPlayer render = manager.getSkinMap().get("slim");
                CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor<>(render, entity), renderer, entity);
                CommonHandler.replaceCustomHeadLayer(layerLists, render);
                CommonHandler.replaceCapeLayer(layerLists, new LayerCapeNew(render));
            }
            else
            {
                RenderPlayer render = manager.getSkinMap().get("default");
                CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor<>(render, entity), renderer, entity);
                CommonHandler.replaceCustomHeadLayer(layerLists, render);
                CommonHandler.replaceCapeLayer(layerLists, new LayerCapeNew(render));
            }
        }
        else if (entity instanceof EntityZombie && ((EntityZombie)entity).isVillager())
        {
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor<>(new RenderVillager(manager), entity), renderer, entity);

            if (renderer instanceof RenderZombie)
            {
                RenderZombie renderZombie = (RenderZombie) renderer;
                CommonHandler.replaceTransparentHeadLayer(layerLists, renderZombie.zombieVillagerModel.bipedHead);
            }
        }
        else if (entity instanceof EntityGiantZombie)
        {
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor<>(new RenderGiantZombie(manager, new ModelZombie(), 0.5F, 6.0F), entity), renderer, entity);
        }
        else if (entity instanceof EntityZombie && !((EntityZombie)entity).isVillager())
        {
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor<>(new RenderZombie(manager), entity), renderer, entity);

            if (renderer instanceof RenderBiped)
            {
                RenderBiped renderBiped = (RenderBiped) renderer;
                CommonHandler.replaceTransparentHeadLayer(layerLists, renderBiped.modelBipedMain.bipedHead);
            }
        }
        else if (entity instanceof EntitySkeleton)
        {
            CommonHandler.replaceArmorLayer(layerLists, new LayerAllArmor<>(new RenderSkeleton(manager), entity), renderer, entity);

            if (renderer instanceof RenderBiped)
            {
                RenderBiped renderBiped = (RenderBiped) renderer;
                CommonHandler.replaceTransparentHeadLayer(layerLists, renderBiped.modelBipedMain.bipedHead);
            }
        }
        else if (entity instanceof EntityVillager)
        {
            if (renderer instanceof RenderVillager)
            {
                RenderVillager renderVillager = (RenderVillager) renderer;
                CommonHandler.replaceTransparentHeadLayer(layerLists, renderVillager.getMainModel().villagerHead);
            }
        }
    }

    @SubscribeEvent
    public void onPreRenderLivingSpecials(RenderLivingEvent.Specials.Pre<EntityLivingBase> event)
    {
        EntityLivingBase entity = event.entity;

        if (ConfigManager.enableRenderNametagFix)
        {
            event.setCanceled(true);

            if (this.canRenderName(entity))
            {
                double d0 = entity.getDistanceSqToEntity(this.mc.getRenderManager().livingPlayer);
                float f = entity.isSneaking() ? RendererLivingEntity.NAME_TAG_RANGE_SNEAK : RendererLivingEntity.NAME_TAG_RANGE;

                if (d0 < f * f)
                {
                    String name = entity.getDisplayName().getFormattedText();
                    GlStateManager.alphaFunc(516, 0.1F);
                    CommonHandler.renderEntityName(this.mc, entity, name, event.x, event.y, event.z);
                }
            }
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
                if (this.mc.thePlayer != null)
                {
                    this.mc.thePlayer.eyeHeight = CommonHandler.getSmoothEyeHeight(this.mc.thePlayer);
                }
            }
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
            Matcher nickMatcher = CommonHandler.nickPattern.matcher(unformattedText);

            if (event.type == 0)
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
                        this.closeScreenTicks = 20;
                    }
                    if (unformattedText.contains(votingText1))
                    {
                        String replacedText = unformattedText.replace(votingText1, "");
                        replacedText = EnumChatFormatting.getTextWithoutFormattingCodes(replacedText);
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

    private boolean canRenderName(EntityLivingBase entity)
    {
        if (entity instanceof EntityLiving)
        {
            return this.getDefaultCanRenderName(entity) && (entity.getAlwaysRenderNameTagForRender() || entity.hasCustomName() && entity == this.mc.getRenderManager().pointedEntity);
        }
        else if (entity instanceof EntityArmorStand)
        {
            return entity.getAlwaysRenderNameTag();
        }
        return this.getDefaultCanRenderName(entity);
    }

    private boolean getDefaultCanRenderName(EntityLivingBase entity)
    {
        EntityPlayerSP playerSP = this.mc.thePlayer;

        if (entity instanceof EntityPlayer && entity != playerSP)
        {
            Team team = entity.getTeam();
            Team team1 = playerSP.getTeam();

            if (team != null)
            {
                Team.EnumVisible visible = team.getNameTagVisibility();

                switch (visible)
                {
                case ALWAYS:
                    return true;
                case NEVER:
                    return false;
                case HIDE_FOR_OTHER_TEAMS:
                    return team1 == null || team.isSameTeam(team1);
                case HIDE_FOR_OWN_TEAM:
                    return team1 == null || !team.isSameTeam(team1);
                default:
                    return true;
                }
            }
        }
        return Minecraft.isGuiEnabled() && entity != this.mc.getRenderManager().livingPlayer && !entity.isInvisibleToPlayer(playerSP) && entity.riddenByEntity == null;
    }

    private static void getRealTimeServerPing(ServerData server)
    {
        CommonHandler.serverPinger.submit(() ->
        {
            try
            {
                ServerAddress address = ServerAddress.fromString(server.serverIP);
                NetworkManager manager = NetworkManager.func_181124_a(InetAddress.getByName(address.getIP()), address.getPort(), false);

                manager.setNetHandler(new INetHandlerStatusClient()
                {
                    private long currentSystemTime = 0L;

                    @Override
                    public void handleServerInfo(S00PacketServerInfo packet)
                    {
                        this.currentSystemTime = Minecraft.getSystemTime();
                        manager.sendPacket(new C01PacketPing(this.currentSystemTime));
                    }

                    @Override
                    public void handlePong(S01PacketPong packet)
                    {
                        long i = this.currentSystemTime;
                        long j = Minecraft.getSystemTime();
                        CommonHandler.currentServerPing = (int) (j - i);
                    }

                    @Override
                    public void onDisconnect(IChatComponent component) {}
                });
                manager.sendPacket(new C00Handshake(47, address.getIP(), address.getPort(), EnumConnectionState.STATUS));
                manager.sendPacket(new C00PacketServerQuery());
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
                            this.field_177189_c = new ModelZombie(0.5F, true);
                            this.field_177186_d = new ModelZombie(1.0F, true);
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
                            this.field_177189_c = new ModelSkeleton(0.5F, true);
                            this.field_177186_d = new ModelSkeleton(1.0F, true);
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
                            this.field_177189_c = new ModelZombieVillager(0.5F, 0.0F, true);
                            this.field_177186_d = new ModelZombieVillager(1.0F, 0.0F, true);
                        }
                    });
                }
            }
        }
    }

    private static void replaceCustomHeadLayer(List<LayerRenderer> layerLists, RenderPlayer render)
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
            layerLists.set(customHeadIndex, new LayerCustomHead(render.getMainModel().bipedHead));
        }
    }

    private static void replaceTransparentHeadLayer(List<LayerRenderer> layerLists, ModelRenderer modelRenderer)
    {
        int customHeadIndex = -1;

        if (ConfigManager.enableTransparentSkullRender)
        {
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
                layerLists.set(customHeadIndex, new LayerCustomHeadNew(modelRenderer));
            }
        }
        else
        {
            for (int i = 0; i < layerLists.size(); i++)
            {
                LayerRenderer layer = layerLists.get(i);

                if (layer instanceof LayerCustomHeadNew)
                {
                    customHeadIndex = i;
                }
            }
            if (customHeadIndex >= 0)
            {
                layerLists.set(customHeadIndex, new LayerCustomHead(modelRenderer));
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

    private static void processAutoGG(Minecraft mc)
    {
        if (mc.ingameGUI.field_175201_x.isEmpty() && !ConfigManager.endGameMessage.isEmpty())
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
            String messageToLower = EnumChatFormatting.getTextWithoutFormattingCodes(message).toLowerCase();
            String displayTitleMessage = EnumChatFormatting.getTextWithoutFormattingCodes(mc.ingameGUI.field_175201_x).toLowerCase();

            if (displayTitleMessage.contains(messageToLower) && CommonHandler.printAutoGGTicks == ConfigManager.endGameTitleTime)
            {
                mc.thePlayer.sendChatMessage(ConfigManager.endGameMessage);
                CommonHandler.printAutoGG = false;
                CommonHandler.printAutoGGTicks = 0;
            }
        }
    }

    private static void replacingPlayerModel(Minecraft mc, EntityPlayerSP player)
    {
        Render render = mc.getRenderManager().getEntityRenderObject(player);
        RenderPlayer renderPlayer = (RenderPlayer) render;

        if (ConfigManager.enableAlternatePlayerModel)
        {
            if (!renderPlayer.mainModel.getClass().equals(ModelPlayerNew.class))
            {
                renderPlayer.mainModel = new ModelPlayerNew(0.0F, player.getSkinType().equalsIgnoreCase("slim"));
                ModLogger.info("Set player model to {}", ModelPlayerNew.class.getName());
            }
        }
        else
        {
            if (!renderPlayer.mainModel.getClass().equals(ModelPlayer.class))
            {
                renderPlayer.mainModel = new ModelPlayer(0.0F, player.getSkinType().equalsIgnoreCase("slim"));
                ModLogger.info("Set player model to {}", ModelPlayer.class.getName());
            }
        }
    }

    private static void replacingOthersPlayerModel(Minecraft mc)
    {
        for (Entity entity : mc.theWorld.playerEntities)
        {
            if (entity instanceof EntityOtherPlayerMP)
            {
                EntityOtherPlayerMP player = (EntityOtherPlayerMP) entity;
                Render render = mc.getRenderManager().getEntityRenderObject(player);
                RenderPlayer renderPlayer = (RenderPlayer) render;

                if (ConfigManager.enableAlternatePlayerModel)
                {
                    if (!renderPlayer.mainModel.getClass().equals(ModelPlayerNew.class))
                    {
                        renderPlayer.mainModel = new ModelPlayerNew(0.0F, player.getSkinType().equalsIgnoreCase("slim"));
                        ModLogger.info("Set player model to {}", ModelPlayerNew.class.getName());
                    }
                }
                else
                {
                    if (!renderPlayer.mainModel.getClass().equals(ModelPlayer.class))
                    {
                        renderPlayer.mainModel = new ModelPlayer(0.0F, player.getSkinType().equalsIgnoreCase("slim"));
                        ModLogger.info("Set player model to {}", ModelPlayer.class.getName());
                    }
                }
            }
        }
    }

    private static void renderEntityName(Minecraft mc, EntityLivingBase entity, String str, double x, double y, double z)
    {
        int maxDistance = 64;
        double d0 = entity.getDistanceSqToEntity(mc.getRenderViewEntity());

        if (d0 <= maxDistance * maxDistance)
        {
            boolean flag = entity.isSneaking();
            float f = mc.getRenderManager().playerViewY;
            float f1 = mc.getRenderManager().playerViewX;
            boolean flag1 = mc.getRenderManager().options.thirdPersonView == 2;
            float f2 = entity.height + 0.5F - (flag ? 0.25F : 0.0F);
            int i = "deadmau5".equals(str) ? -10 : 0;
            CommonHandler.drawNameplate(mc.getRenderManager().getFontRenderer(), str, (float)x, (float)y + f2, (float)z, i, f, f1, flag1, flag);
        }
    }

    private static void drawNameplate(FontRenderer fontRenderer, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-0.025F, -0.025F, 0.025F);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);

        if (!isSneaking)
        {
            GlStateManager.disableDepth();
        }

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int i = fontRenderer.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer vertexbuffer = tessellator.getWorldRenderer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(-i - 1, -1 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        vertexbuffer.pos(-i - 1, 8 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        vertexbuffer.pos(i + 1, 8 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        vertexbuffer.pos(i + 1, -1 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        if (!isSneaking)
        {
            fontRenderer.drawString(str, -fontRenderer.getStringWidth(str) / 2, verticalShift, 553648127);
            GlStateManager.enableDepth();
        }

        GlStateManager.depthMask(true);
        fontRenderer.drawString(str, -fontRenderer.getStringWidth(str) / 2, verticalShift, isSneaking ? 553648127 : -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private static EnumAction[] getCachedAction()
    {
        return CommonHandler.cachedAction;
    }
}