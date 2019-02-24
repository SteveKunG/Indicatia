package stevekung.mods.indicatia.event;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.model.ModelZombie;
import net.minecraft.client.renderer.entity.model.ModelZombieVillager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.CPacketHandshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.network.status.server.SPacketServerInfo;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.config.IndicatiaConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.gui.*;
import stevekung.mods.indicatia.gui.config.GuiExtendedConfig;
import stevekung.mods.indicatia.gui.hack.GuiMultiplayerIN;
import stevekung.mods.indicatia.handler.KeyBindingHandler;
import stevekung.mods.indicatia.renderer.*;
import stevekung.mods.indicatia.utils.AutoLoginFunction;
import stevekung.mods.indicatia.utils.CapeUtils;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.indicatia.utils.LoggerIN;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;
import stevekung.mods.stevekungslib.utils.client.ClientUtils;
import stevekung.mods.stevekungslib.utils.enums.CachedEnum;

public class IndicatiaEventHandler
{
    private Minecraft mc;
    public static final List<Long> LEFT_CLICK = new ArrayList<>();
    public static final List<Long> RIGHT_CLICK = new ArrayList<>();
    public static int currentServerPing;
    private static final ThreadPoolExecutor serverPinger = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Real Time Server Pinger #%d").setDaemon(true).build());
    private static int pendingPingTicks = 100;
    private static boolean initLayer = true;
    private int disconnectClickCount;
    private int disconnectClickCooldown;
    private boolean initVersionCheck;

    public static boolean isAFK;
    public static String afkMode = "idle";
    public static String afkReason;
    public static int afkMoveTicks;
    public static int afkTicks;

    static boolean printAutoGG;
    private static int printAutoGGTicks;

    public static boolean autoFish;
    private static int autoFishTick;

    public IndicatiaEventHandler()
    {
        this.mc = Minecraft.getInstance();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (this.mc.player != null)
        {
            if (!this.initVersionCheck)
            {
                IndicatiaMod.CHECKER.startCheckIfFailed();

                if (IndicatiaConfig.GENERAL.enableVersionChecker.get())
                {
                    IndicatiaMod.CHECKER.printInfo(this.mc.player);
                }
                this.initVersionCheck = true;
            }
            if (event.phase == TickEvent.Phase.START)
            {
                IndicatiaEventHandler.runAFK(this.mc.player);
                IndicatiaEventHandler.processAutoFish(this.mc);
                AutoLoginFunction.runAutoLoginFunction();
                CapeUtils.loadCapeTexture();

                if (IndicatiaEventHandler.printAutoGG && IndicatiaEventHandler.printAutoGGTicks < IndicatiaConfig.GENERAL.autoGGDelay.get())
                {
                    IndicatiaEventHandler.printAutoGGTicks++;

                    if (IndicatiaEventHandler.printAutoGGTicks == IndicatiaConfig.GENERAL.autoGGDelay.get())
                    {
                        this.mc.player.sendChatMessage(IndicatiaConfig.GENERAL.autoGGMessage.get());
                        IndicatiaEventHandler.printAutoGG = false;
                        IndicatiaEventHandler.printAutoGGTicks = 0;
                    }
                }
                if (AutoLoginFunction.functionDelay > 0)
                {
                    AutoLoginFunction.functionDelay--;
                }
                if (AutoLoginFunction.functionDelay == 0)
                {
                    AutoLoginFunction.runAutoLoginFunctionTicks(this.mc);
                }
                if (this.disconnectClickCooldown > 0)
                {
                    this.disconnectClickCooldown--;
                }
                if (this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiIngameMenu)
                {
                    if (IndicatiaConfig.GENERAL.enableConfirmDisconnectButton.get() && !this.mc.isSingleplayer())
                    {
                        this.mc.currentScreen.buttons.forEach(button ->
                        {
                            if (button.id == 1 && IndicatiaConfig.GENERAL.confirmDisconnectMode.get().equals(IndicatiaConfig.DisconnectMode.CLICK.name()))
                            {
                                if (this.disconnectClickCooldown < 60)
                                {
                                    int cooldownSec = 1 + this.disconnectClickCooldown / 20;
                                    button.displayString = TextFormatting.RED + LangUtils.translate("menu.click_to_disconnect") + " in " + cooldownSec + "...";
                                }
                                if (this.disconnectClickCooldown == 0)
                                {
                                    button.displayString = LangUtils.translate("menu.disconnect");
                                    this.disconnectClickCount = 0;
                                }
                            }
                        });
                    }
                }
                else
                {
                    this.disconnectClickCount = 0;
                    this.disconnectClickCooldown = 0;
                }

                if (IndicatiaEventHandler.pendingPingTicks > 0 && this.mc.getCurrentServerData() != null)
                {
                    IndicatiaEventHandler.pendingPingTicks--;

                    if (IndicatiaEventHandler.pendingPingTicks == 0)
                    {
                        IndicatiaEventHandler.getRealTimeServerPing(this.mc.getCurrentServerData());
                        IndicatiaEventHandler.pendingPingTicks = 100;
                    }
                }

                for (EnumAction action : CachedEnum.actionValues)
                {
                    if (action != EnumAction.NONE)
                    {
                        if (IndicatiaConfig.GENERAL.enableAdditionalBlockhitAnimation.get() && this.mc.gameSettings.keyBindAttack.isKeyDown() && this.mc.objectMouseOver != null && this.mc.objectMouseOver.type == RayTraceResult.Type.BLOCK && !this.mc.player.getHeldItemMainhand().isEmpty() && this.mc.player.getHeldItemMainhand().getUseAction() == action)
                        {
                            this.mc.player.swingArm(EnumHand.MAIN_HAND);
                        }
                    }
                }
            }
        }
        GuiIngameForge.renderBossHealth = IndicatiaConfig.GENERAL.enableRenderBossHealthStatus.get();
        GuiIngameForge.renderObjective = IndicatiaConfig.GENERAL.enableSidebarScoreboardRender.get();
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event)
    {
        MovementInput movement = event.getMovementInput();
        EntityPlayer player = event.getEntityPlayer();

        if (IndicatiaConfig.GENERAL.enableCustomMovementHandler.get())
        {
            // canceled turn back
            if (ExtendedConfig.toggleSprintUseMode.equalsIgnoreCase("key_binding") && KeyBindingHandler.KEY_TOGGLE_SPRINT.isKeyDown())
            {
                ++movement.moveForward;
            }

            // toggle sneak
            movement.sneak = this.mc.gameSettings.keyBindSneak.isKeyDown() || ExtendedConfig.toggleSneak && !event.getEntityPlayer().isSpectator();

            if (ExtendedConfig.toggleSneak && !player.isSpectator() && !player.isCreative())
            {
                movement.moveStrafe = (float)(movement.moveStrafe * 0.3D);
                movement.moveForward = (float)(movement.moveForward * 0.3D);
            }

            // toggle sprint
            if (ExtendedConfig.toggleSprint && !player.isPotionActive(MobEffects.BLINDNESS) && !ExtendedConfig.toggleSneak)
            {
                player.setSprinting(true);
            }

            // afk stuff
            if (!IndicatiaEventHandler.afkMode.equals("360"))
            {
                int afkMoveTick = IndicatiaEventHandler.afkMoveTicks;

                if (afkMoveTick > 0 && afkMoveTick < 2)
                {
                    ++movement.moveForward;
                    movement.forwardKeyDown = true;
                }
                else if (afkMoveTick > 2 && afkMoveTick < 4)
                {
                    ++movement.moveStrafe;
                    movement.leftKeyDown = true;
                }
                else if (afkMoveTick > 4 && afkMoveTick < 6)
                {
                    --movement.moveForward;
                    movement.backKeyDown = true;
                }
                else if (afkMoveTick > 6 && afkMoveTick < 8)
                {
                    --movement.moveStrafe;
                    movement.rightKeyDown = true;
                }
            }

            // auto login function
            if (AutoLoginFunction.functionDelay == 0)
            {
                if (AutoLoginFunction.forwardTicks > 0 || AutoLoginFunction.forwardAfterCommandTicks > 0)
                {
                    movement.moveForward++;
                }
            }
        }
    }

    //    @SubscribeEvent
    //    public void onMouseClick(MouseEvent event)TODO
    //    {
    //        if (event.getButton() == 0 && event.isButtonstate())
    //        {
    //            IndicatiaEventHandler.LEFT_CLICK.add(System.currentTimeMillis());
    //        }
    //        if (event.getButton() == 1 && event.isButtonstate())
    //        {
    //            IndicatiaEventHandler.RIGHT_CLICK.add(System.currentTimeMillis());
    //        }
    //    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public void onPreRenderLiving(RenderLivingEvent.Pre<?> event)
    {
        @SuppressWarnings("rawtypes")
        RenderLivingBase renderer = event.getRenderer();
        List<LayerRenderer<?>> layerLists = renderer.layerRenderers;
        EntityLivingBase entity = event.getEntity();
        RenderManager manager = this.mc.getRenderManager();
        IndicatiaEventHandler.replaceArrowLayer(layerLists, new LayerArrowNew(renderer));

        if (entity instanceof AbstractClientPlayer)
        {
            AbstractClientPlayer player = (AbstractClientPlayer) entity;

            if (player.getSkinType().equals("default"))
            {
                RenderPlayer renderDefault = manager.getSkinMap().get("default");
                IndicatiaEventHandler.replaceArmorLayer(layerLists, new LayerAllArmor(renderDefault), renderer, entity);
                IndicatiaEventHandler.replaceCapeLayer(layerLists, new LayerCapeNew(renderDefault));
                IndicatiaEventHandler.replaceElytraLayer(layerLists, new LayerElytraNew(renderDefault));
            }
            else
            {
                RenderPlayer renderSlim = manager.getSkinMap().get("slim");
                IndicatiaEventHandler.replaceArmorLayer(layerLists, new LayerAllArmor(renderSlim), renderer, entity);
                IndicatiaEventHandler.replaceCapeLayer(layerLists, new LayerCapeNew(renderSlim));
                IndicatiaEventHandler.replaceElytraLayer(layerLists, new LayerElytraNew(renderSlim));
            }
        }
        else if (entity instanceof EntityZombieVillager)
        {
            IndicatiaEventHandler.replaceArmorLayer(layerLists, new LayerAllArmor(new RenderZombieVillager(manager)), renderer, entity);
        }
        else if (entity instanceof EntityGiantZombie)
        {
            IndicatiaEventHandler.replaceArmorLayer(layerLists, new LayerAllArmor(new RenderGiantZombie(manager, 6.0F)), renderer, entity);
        }
        else if (entity instanceof EntityZombie)
        {
            IndicatiaEventHandler.replaceArmorLayer(layerLists, new LayerAllArmor(new RenderZombie(manager)), renderer, entity);
        }
        else if (entity instanceof AbstractSkeleton)
        {
            IndicatiaEventHandler.replaceArmorLayer(layerLists, new LayerAllArmor(new RenderSkeleton(manager)), renderer, entity);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event)
    {
        if (event.getGui() != null && event.getGui().getClass().equals(GuiMainMenu.class) && IndicatiaEventHandler.initLayer)
        {
            RenderPlayer renderDefault = this.mc.getRenderManager().getSkinMap().get("default");
            RenderPlayer renderSlim = this.mc.getRenderManager().getSkinMap().get("slim");
            renderDefault.addLayer(new LayerCustomCape(renderDefault));
            renderSlim.addLayer(new LayerCustomCape(renderSlim));
            IndicatiaEventHandler.initLayer = false;
        }
        if (IndicatiaConfig.GENERAL.enableCustomServerSelectionGui.get() && event.getGui() != null && event.getGui().getClass().equals(GuiMultiplayer.class))
        {
            event.setGui(new GuiMultiplayerIN(new GuiMainMenu()));
        }
    }

    //    @SubscribeEvent TODO
    //    public void onDisconnectedFromServerEvent(NetworkEvent.ClientDisconnectionFromServerEvent event)
    //    {
    //        IndicatiaEventHandler.stopCommandTicks();
    //    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
    {
        IndicatiaEventHandler.stopCommandTicks();
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        IndicatiaEventHandler.stopCommandTicks();
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            InfoUtils.INSTANCE.processMouseOverEntity(this.mc);
        }
    }

    @SubscribeEvent
    public void onPressKey(InputEvent.KeyInputEvent event)
    {
        if (KeyBindingHandler.KEY_QUICK_CONFIG.isKeyDown())
        {
            GuiExtendedConfig config = new GuiExtendedConfig();
            this.mc.displayGuiScreen(config);
        }
        if (KeyBindingHandler.KEY_REC_OVERLAY.isKeyDown())
        {
            HUDRenderEventHandler.recordEnable = !HUDRenderEventHandler.recordEnable;
        }
        if (IndicatiaConfig.GENERAL.enableCustomCape.get() && KeyBindingHandler.KEY_CUSTOM_CAPE_GUI.isKeyDown())
        {
            GuiCustomCape customCapeGui = new GuiCustomCape();
            this.mc.displayGuiScreen(customCapeGui);
        }
        if (ExtendedConfig.toggleSprintUseMode.equals("key_binding") && KeyBindingHandler.KEY_TOGGLE_SPRINT.isKeyDown())
        {
            ExtendedConfig.toggleSprint = !ExtendedConfig.toggleSprint;
            ClientUtils.setOverlayMessage(JsonUtils.create(ExtendedConfig.toggleSprint ? LangUtils.translate("commands.indicatia.toggle_sprint.enable") : LangUtils.translate("commands.indicatia.toggle_sprint.disable")).getFormattedText());
            ExtendedConfig.save();
        }
        if (ExtendedConfig.toggleSneakUseMode.equals("key_binding") && KeyBindingHandler.KEY_TOGGLE_SNEAK.isKeyDown())
        {
            ExtendedConfig.toggleSneak = !ExtendedConfig.toggleSneak;
            ClientUtils.setOverlayMessage(JsonUtils.create(ExtendedConfig.toggleSneak ? LangUtils.translate("commands.indicatia.toggle_sneak.enable") : LangUtils.translate("commands.indicatia.toggle_sneak.disable")).getFormattedText());
            ExtendedConfig.save();
        }
        if (KeyBindingHandler.KEY_DONATOR_GUI.isKeyDown())
        {
            GuiDonator donatorGui = new GuiDonator();
            this.mc.displayGuiScreen(donatorGui);
        }
    }

    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event)
    {
        if (event.getGui() instanceof GuiMainMenu)
        {
            int height = event.getGui().height / 4 + 48;
            event.addButton(new GuiButtonMojangStatus(200, event.getGui().width / 2 - 124, height + 63));
        }
    }

    @SubscribeEvent
    public void onPreActionPerformedGui(GuiScreenEvent.ActionPerformedEvent.Pre event)
    {
        if (event.getGui() instanceof GuiMainMenu)
        {
            if (IndicatiaConfig.GENERAL.enableCustomServerSelectionGui.get() && event.getButton().id == 2)
            {
                event.setCanceled(true);
                event.getButton().playPressSound(this.mc.getSoundHandler());
                this.mc.displayGuiScreen(new GuiMultiplayerIN(new GuiMainMenu()));
            }
        }
        if (IndicatiaConfig.GENERAL.enableConfirmDisconnectButton.get() && event.getGui() instanceof GuiIngameMenu && !this.mc.isSingleplayer())
        {
            if (event.getButton().id == 1)
            {
                event.setCanceled(true);
                event.getButton().playPressSound(this.mc.getSoundHandler());

                if (IndicatiaConfig.GENERAL.confirmDisconnectMode.get().equals(IndicatiaConfig.DisconnectMode.GUI.name()))
                {
                    this.mc.displayGuiScreen(new GuiConfirmDisconnect());
                }
                else
                {
                    this.disconnectClickCount++;
                    event.getButton().displayString = TextFormatting.RED + LangUtils.translate("menu.click_to_disconnect");

                    if (this.disconnectClickCount == 1)
                    {
                        this.disconnectClickCooldown = 100;
                    }
                    if (this.disconnectClickCount == 2)
                    {
                        if (this.mc.isConnectedToRealms())
                        {
                            this.mc.world.sendQuittingDisconnectingPacket();
                            this.mc.loadWorld(null);
                            RealmsBridge bridge = new RealmsBridge();
                            bridge.switchToRealms(new GuiMainMenu());
                        }
                        else
                        {
                            this.mc.world.sendQuittingDisconnectingPacket();
                            this.mc.loadWorld(null);

                            if (IndicatiaConfig.GENERAL.enableCustomServerSelectionGui.get())
                            {
                                this.mc.displayGuiScreen(new GuiMultiplayerIN(new GuiMainMenu()));
                            }
                            else
                            {
                                this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
                            }
                        }
                        this.disconnectClickCount = 0;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPostActionPerformedGui(GuiScreenEvent.ActionPerformedEvent.Post event)
    {
        if (event.getGui() instanceof GuiMainMenu)
        {
            if (event.getButton().id == 200)
            {
                this.mc.displayGuiScreen(new GuiMojangStatusChecker(event.getGui()));
            }
        }
    }

    private static void getRealTimeServerPing(ServerData server)
    {
        IndicatiaEventHandler.serverPinger.submit(() ->
        {
            try
            {
                ServerAddress address = ServerAddress.fromString(server.serverIP);
                NetworkManager manager = NetworkManager.createNetworkManagerAndConnect(InetAddress.getByName(address.getIP()), address.getPort(), false);

                manager.setNetHandler(new INetHandlerStatusClient()
                {
                    private long currentSystemTime = 0L;

                    @Override
                    public void handleServerInfo(@Nonnull SPacketServerInfo packet)
                    {
                        this.currentSystemTime = Util.milliTime();
                        manager.sendPacket(new CPacketPing(this.currentSystemTime));
                    }

                    @Override
                    public void handlePong(@Nonnull SPacketPong packet)
                    {
                        long i = this.currentSystemTime;
                        long j = Util.milliTime();
                        IndicatiaEventHandler.currentServerPing = (int) (j - i);
                    }

                    @Override
                    public void onDisconnect(@Nonnull ITextComponent component) {}
                });
                manager.sendPacket(new CPacketHandshake(address.getIP(), address.getPort(), EnumConnectionState.STATUS));
                manager.sendPacket(new CPacketServerQuery());
            }
            catch (Exception e) {}
        });
    }

    private static void runAFK(EntityPlayerSP player)
    {
        if (IndicatiaEventHandler.isAFK)
        {
            IndicatiaEventHandler.afkTicks++;
            int tick = IndicatiaEventHandler.afkTicks;
            int messageMin = 1200 * IndicatiaConfig.GENERAL.afkMessageTime.get();
            String s = "s";
            float angle = tick % 2 == 0 ? 0.0001F : -0.0001F;

            if (tick == 0)
            {
                s = "";
            }
            if (IndicatiaConfig.GENERAL.enableAFKMessage.get())
            {
                if (tick % messageMin == 0)
                {
                    String reason = IndicatiaEventHandler.afkReason;
                    reason = reason.isEmpty() ? "" : ", Reason : " + reason;
                    player.sendChatMessage("AFK : " + StringUtils.ticksToElapsedTime(tick) + " minute" + s + reason);
                }
            }

            switch (IndicatiaEventHandler.afkMode)
            {
            case "idle":
                player.rotateTowards(angle, angle);
                break;
            case "360":
                player.rotateTowards(1.0F, 0.0F);
                break;
            case "360_move":
                player.rotateTowards(1.0F, 0.0F);
                IndicatiaEventHandler.afkMoveTicks++;
                IndicatiaEventHandler.afkMoveTicks %= 8;
                break;
            case "move":
                player.rotateTowards(angle, angle);
                IndicatiaEventHandler.afkMoveTicks++;
                IndicatiaEventHandler.afkMoveTicks %= 8;
                break;
            }
        }
        else
        {
            IndicatiaEventHandler.afkTicks = 0;
        }
    }

    private static void stopCommandTicks()
    {
        if (IndicatiaEventHandler.isAFK)
        {
            IndicatiaEventHandler.isAFK = false;
            IndicatiaEventHandler.afkReason = "";
            IndicatiaEventHandler.afkTicks = 0;
            IndicatiaEventHandler.afkMoveTicks = 0;
            IndicatiaEventHandler.afkMode = "idle";
            LoggerIN.info("Stopping AFK Command");
        }
        if (IndicatiaEventHandler.autoFish)
        {
            IndicatiaEventHandler.autoFish = false;
            IndicatiaEventHandler.autoFishTick = 0;
            LoggerIN.info("Stopping Autofish Command");
        }
    }

    private static void replaceArmorLayer(List<LayerRenderer<?>> layerLists, LayerRenderer<?> newLayer, RenderLivingBase<?> render, EntityLivingBase entity)
    {
        int armorLayerIndex = -1;

        if (IndicatiaConfig.GENERAL.enableOldArmorRender.get())
        {
            for (int i = 0; i < layerLists.size(); i++)
            {
                LayerRenderer<?> layer = layerLists.get(i);

                if (layer.getClass().equals(LayerBipedArmor.class))
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
                LayerRenderer<?> layer = layerLists.get(i);

                if (layer.getClass().equals(LayerAllArmor.class))
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

    private static void replaceCapeLayer(List<LayerRenderer<?>> layerLists, LayerRenderer<?> newLayer)
    {
        int capeLayerIndex = -1;

        for (int i = 0; i < layerLists.size(); i++)
        {
            LayerRenderer<?> layer = layerLists.get(i);

            if (layer.getClass().equals(LayerCape.class))
            {
                capeLayerIndex = i;
            }
        }
        if (capeLayerIndex >= 0)
        {
            layerLists.set(capeLayerIndex, newLayer);
        }
    }

    private static void replaceElytraLayer(List<LayerRenderer<?>> layerLists, LayerRenderer<?> newLayer)
    {
        int elytraLayerIndex = -1;

        if (IndicatiaConfig.GENERAL.enableOldArmorRender.get())
        {
            for (int i = 0; i < layerLists.size(); i++)
            {
                LayerRenderer<?> layer = layerLists.get(i);

                if (layer.getClass().equals(LayerElytra.class))
                {
                    elytraLayerIndex = i;
                }
            }
            if (elytraLayerIndex >= 0)
            {
                layerLists.set(elytraLayerIndex, newLayer);
            }
        }
    }

    private static void replaceArrowLayer(List<LayerRenderer<?>> layerLists, LayerRenderer<?> newLayer)
    {
        int arrowLayerIndex = -1;

        for (int i = 0; i < layerLists.size(); i++)
        {
            LayerRenderer<?> layer = layerLists.get(i);

            if (layer.getClass().equals(LayerArrow.class))
            {
                arrowLayerIndex = i;
            }
        }
        if (arrowLayerIndex >= 0)
        {
            layerLists.set(arrowLayerIndex, newLayer);
        }
    }

    private static void processAutoFish(Minecraft mc)
    {
        if (IndicatiaEventHandler.autoFish)
        {
            ++IndicatiaEventHandler.autoFishTick;
            IndicatiaEventHandler.autoFishTick %= 4;

            if (mc.objectMouseOver != null && mc.world != null && mc.playerController != null && mc.entityRenderer != null)
            {
                if (IndicatiaEventHandler.autoFishTick % 4 == 0)
                {
                    for (EnumHand hand : CachedEnum.handValues)
                    {
                        ItemStack itemStack = mc.player.getHeldItem(hand);
                        boolean fishingRod = mc.player.getHeldItem(hand).getItem() instanceof ItemFishingRod;

                        if (fishingRod)
                        {
                            if (mc.objectMouseOver.type == RayTraceResult.Type.BLOCK)
                            {
                                BlockPos pos = mc.objectMouseOver.getBlockPos();

                                if (!mc.world.getBlockState(pos).isAir(mc.world, pos))
                                {
                                    int count = itemStack.getCount();
                                    EnumActionResult result = mc.playerController.processRightClickBlock(mc.player, mc.world, pos, mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec, hand);

                                    if (result == EnumActionResult.SUCCESS)
                                    {
                                        mc.player.swingArm(hand);

                                        if (!itemStack.isEmpty() && (itemStack.getCount() != count || mc.playerController.isInCreativeMode()))
                                        {
                                            mc.entityRenderer.itemRenderer.resetEquippedProgress(hand);
                                        }
                                        return;
                                    }
                                    if (result == EnumActionResult.FAIL)
                                    {
                                        return;
                                    }
                                }
                            }
                        }
                        else
                        {
                            IndicatiaEventHandler.autoFish = false;
                            IndicatiaEventHandler.autoFishTick = 0;
                            mc.player.sendMessage(JsonUtils.create(LangUtils.translate("commands.auto_fish.not_equipped_fishing_rod")));
                            return;
                        }

                        if (itemStack.isEmpty() && (mc.objectMouseOver == null || mc.objectMouseOver.type == RayTraceResult.Type.MISS))
                        {
                            ForgeHooks.onEmptyClick(mc.player, hand);
                        }
                        if (!itemStack.isEmpty() && mc.playerController.processRightClick(mc.player, mc.world, hand) == EnumActionResult.SUCCESS)
                        {
                            mc.entityRenderer.itemRenderer.resetEquippedProgress(hand);
                            return;
                        }
                    }
                }
            }
        }
        else
        {
            IndicatiaEventHandler.autoFishTick = 0;
        }
    }
}