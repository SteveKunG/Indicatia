package stevekung.mods.indicatia.event;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Nonnull;

import org.lwjgl.glfw.GLFW;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.status.IClientStatusNetHandler;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.model.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.GiantEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.ProtocolType;
import net.minecraft.network.handshake.client.CHandshakePacket;
import net.minecraft.network.status.client.CPingPacket;
import net.minecraft.network.status.client.CServerQueryPacket;
import net.minecraft.network.status.server.SPongPacket;
import net.minecraft.network.status.server.SServerInfoPacket;
import net.minecraft.potion.Effects;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ForgeIngameGui;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
                /*if (this.mc.field_71462_r != null && this.mc.field_71462_r instanceof GuiIngameMenu) TODO
                {
                    if (IndicatiaConfig.GENERAL.enableConfirmDisconnectButton.get() && !this.mc.isSingleplayer())
                    {
                        this.mc.field_71462_r.buttons.forEach(button ->
                        {
                            if (button.id == 1 && IndicatiaConfig.GENERAL.confirmDisconnectMode.get() == IndicatiaConfig.DisconnectMode.CLICK)
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
                else*/
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

                for (UseAction action : CachedEnum.actionValues)
                {
                    if (action != UseAction.NONE)
                    {
                        if (IndicatiaConfig.GENERAL.enableAdditionalBlockhitAnimation.get() && this.mc.gameSettings.keyBindAttack.isKeyDown() && this.mc.objectMouseOver != null && this.mc.objectMouseOver.getType() == RayTraceResult.Type.BLOCK && !this.mc.player.getHeldItemMainhand().isEmpty() && this.mc.player.getHeldItemMainhand().getUseAction() == action)
                        {
                            this.mc.player.swingArm(Hand.MAIN_HAND);
                        }
                    }
                }
            }
        }
        ForgeIngameGui.renderBossHealth = IndicatiaConfig.GENERAL.enableRenderBossHealthStatus.get();
        ForgeIngameGui.renderObjective = IndicatiaConfig.GENERAL.enableSidebarScoreboardRender.get();
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event)
    {
        MovementInput movement = event.getMovementInput();
        PlayerEntity player = event.getEntityPlayer();

        if (IndicatiaConfig.GENERAL.enableCustomMovementHandler.get())
        {
            // canceled turn back
            if (ExtendedConfig.instance.toggleSprintUseMode.equalsIgnoreCase("key_binding") && KeyBindingHandler.KEY_TOGGLE_SPRINT.isKeyDown())
            {
                ++movement.moveForward;
            }

            // toggle sneak
            movement.sneak = this.mc.gameSettings.keyBindSneak.isKeyDown() || ExtendedConfig.instance.toggleSneak && !event.getEntityPlayer().isSpectator();

            if (ExtendedConfig.instance.toggleSneak && !player.isSpectator() && !player.isCreative())
            {
                movement.moveStrafe = (float)(movement.moveStrafe * 0.3D);
                movement.moveForward = (float)(movement.moveForward * 0.3D);
            }

            // toggle sprint
            if (ExtendedConfig.instance.toggleSprint && !player.isPotionActive(Effects.field_76440_q) && !ExtendedConfig.instance.toggleSneak)
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

    @SubscribeEvent
    public void onMouseClick(InputEvent.MouseInputEvent event)
    {
        if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_1 && event.getAction() == GLFW.GLFW_PRESS)
        {
            IndicatiaEventHandler.LEFT_CLICK.add(System.currentTimeMillis());
        }
        if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_2 && event.getAction() == GLFW.GLFW_PRESS)
        {
            IndicatiaEventHandler.RIGHT_CLICK.add(System.currentTimeMillis());
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event)
    {
        LivingRenderer<LivingEntity, EntityModel<LivingEntity>> renderer = event.getRenderer();
        List<LayerRenderer<LivingEntity, EntityModel<LivingEntity>>> layerLists = renderer.layerRenderers;
        LivingEntity entity = event.getEntity();
        IReloadableResourceManager resource = (IReloadableResourceManager)Minecraft.getInstance().getResourceManager();
        EntityRendererManager manager = this.mc.getRenderManager();
        IndicatiaEventHandler.replaceArrowLayer(layerLists, new LayerArrowNew<>(renderer));

        if (entity instanceof AbstractClientPlayerEntity)
        {
            AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) entity;

            if (player.getSkinType().equals("default"))
            {
                PlayerRenderer renderDefault = manager.getSkinMap().get("default");
                IndicatiaEventHandler.replaceArmorLayer(layerLists, new LayerAllArmor(renderDefault, new BipedModel<>(0.5F), new BipedModel<>(1.0F)), renderDefault, entity);
                IndicatiaEventHandler.replaceCapeLayer(layerLists, new LayerCapeNew(renderDefault));
                IndicatiaEventHandler.replaceElytraLayer(layerLists, new LayerElytraNew<>(renderDefault));
            }
            else
            {
                PlayerRenderer renderSlim = manager.getSkinMap().get("slim");
                IndicatiaEventHandler.replaceArmorLayer(layerLists, new LayerAllArmor(renderSlim, new BipedModel<>(0.5F), new BipedModel<>(1.0F)), renderer, entity);
                IndicatiaEventHandler.replaceCapeLayer(layerLists, new LayerCapeNew(renderSlim));
                IndicatiaEventHandler.replaceElytraLayer(layerLists, new LayerElytraNew<>(renderSlim));
            }
        }
        else if (entity instanceof ZombieVillagerEntity)
        {
            IndicatiaEventHandler.replaceArmorLayer(layerLists, new LayerAllArmor(new ZombieVillagerRenderer(manager, resource), new ZombieVillagerModel<>(0.5F, true), new ZombieVillagerModel<>(1.0F, true)), renderer, entity);
        }
        else if (entity instanceof GiantEntity)
        {
            IndicatiaEventHandler.replaceArmorLayer(layerLists, new LayerAllArmor(new GiantZombieRenderer(manager, 6.0F), new GiantModel(0.5F, true), new GiantModel(1.0F, true)), renderer, entity);
        }
        else if (entity instanceof ZombieEntity)
        {
            IndicatiaEventHandler.replaceArmorLayer(layerLists, new LayerAllArmor(new ZombieRenderer(manager), new ZombieModel<>(0.5F, true), new ZombieModel<>(1.0F, true)), renderer, entity);
        }
        else if (entity instanceof AbstractSkeletonEntity)
        {
            IndicatiaEventHandler.replaceArmorLayer(layerLists, new LayerAllArmor(new SkeletonRenderer(manager), new SkeletonModel<>(0.5F, true), new SkeletonModel<>(1.0F, true)), renderer, entity);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event)
    {
        if (event.getGui() != null && event.getGui().getClass().equals(MainMenuScreen.class) && IndicatiaEventHandler.initLayer)
        {
            PlayerRenderer renderDefault = this.mc.getRenderManager().getSkinMap().get("default");
            PlayerRenderer renderSlim = this.mc.getRenderManager().getSkinMap().get("slim");
            renderDefault.addLayer(new LayerCustomCape(renderDefault));
            renderSlim.addLayer(new LayerCustomCape(renderSlim));
            IndicatiaEventHandler.initLayer = false;
        }
        if (IndicatiaConfig.GENERAL.enableCustomServerSelectionGui.get() && event.getGui() != null && event.getGui().getClass().equals(MultiplayerScreen.class))
        {
            event.setGui(new GuiMultiplayerIN(new MainMenuScreen()));
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
    public void onPressKey(InputEvent.KeyInputEvent event)//TODO Figure out how to fix vanilla key repeating
    {
        InputMappings.Input input = InputMappings.Type.KEYSYM.getOrMakeInput(event.getKey());

        if (KeyBindingHandler.KEY_QUICK_CONFIG.isActiveAndMatches(input))
        {
            GuiExtendedConfig config = new GuiExtendedConfig();
            this.mc.displayGuiScreen(config);
        }
        if (KeyBindingHandler.KEY_REC_OVERLAY.isActiveAndMatches(input))
        {
            HUDRenderEventHandler.recordEnable = !HUDRenderEventHandler.recordEnable;
        }
        if (IndicatiaConfig.GENERAL.enableCustomCape.get() && KeyBindingHandler.KEY_CUSTOM_CAPE_GUI.isActiveAndMatches(input))
        {
            GuiCustomCape customCapeGui = new GuiCustomCape();
            this.mc.displayGuiScreen(customCapeGui);
        }
        if (ExtendedConfig.instance.toggleSprintUseMode.equals("key_binding") && KeyBindingHandler.KEY_TOGGLE_SPRINT.isActiveAndMatches(input))
        {
            ExtendedConfig.instance.toggleSprint = !ExtendedConfig.instance.toggleSprint;
            ClientUtils.setOverlayMessage(JsonUtils.create(ExtendedConfig.instance.toggleSprint ? LangUtils.translate("commands.indicatia.toggle_sprint.enable") : LangUtils.translate("commands.indicatia.toggle_sprint.disable")).getFormattedText());
            ExtendedConfig.instance.save();
        }
        if (ExtendedConfig.instance.toggleSneakUseMode.equals("key_binding") && KeyBindingHandler.KEY_TOGGLE_SNEAK.isActiveAndMatches(input))
        {
            ExtendedConfig.instance.toggleSneak = !ExtendedConfig.instance.toggleSneak;
            ClientUtils.setOverlayMessage(JsonUtils.create(ExtendedConfig.instance.toggleSneak ? LangUtils.translate("commands.indicatia.toggle_sneak.enable") : LangUtils.translate("commands.indicatia.toggle_sneak.disable")).getFormattedText());
            ExtendedConfig.instance.save();
        }
        if (KeyBindingHandler.KEY_DONATOR_GUI.isActiveAndMatches(input))
        {
            GuiDonator donatorGui = new GuiDonator();
            this.mc.displayGuiScreen(donatorGui);
        }
    }

    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event)
    {
        if (event.getGui() instanceof MainMenuScreen)
        {
            int height = event.getGui().height / 4 + 48;
            event.addWidget(new GuiButtonMojangStatus(event.getGui().width / 2 - 124, height + 63, button -> IndicatiaEventHandler.this.mc.displayGuiScreen(new GuiMojangStatusChecker(event.getGui()))));
        }
    }

    @SubscribeEvent
    public void onPreActionPerformedGui(GuiScreenEvent.ActionPerformedEvent.Pre event)
    {
        if (event.getGui() instanceof MainMenuScreen)
        {
            if (IndicatiaConfig.GENERAL.enableCustomServerSelectionGui.get() && event.getButton().getMessage().equals(I18n.format("menu.multiplayer")))//TODO Testing
            {
                event.setCanceled(true);
                event.getButton().playDownSound(this.mc.getSoundHandler());
                this.mc.displayGuiScreen(new GuiMultiplayerIN(new MainMenuScreen()));
            }
        }
        if (IndicatiaConfig.GENERAL.enableConfirmDisconnectButton.get() && event.getGui() instanceof IngameMenuScreen && !this.mc.isSingleplayer())
        {
            if (event.getButton().getMessage().equals(I18n.format("menu.disconnect")))//TODO Testing
            {
                event.setCanceled(true);
                event.getButton().playDownSound(this.mc.getSoundHandler());

                if (IndicatiaConfig.GENERAL.confirmDisconnectMode.get() == IndicatiaConfig.DisconnectMode.GUI)
                {
                    this.mc.displayGuiScreen(new GuiConfirmDisconnect());
                }
                else
                {
                    this.disconnectClickCount++;
                    event.getButton().setMessage(TextFormatting.RED + LangUtils.translate("menu.click_to_disconnect"));

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
                            bridge.switchToRealms(new MainMenuScreen());
                        }
                        else
                        {
                            this.mc.world.sendQuittingDisconnectingPacket();
                            this.mc.loadWorld(null);

                            if (IndicatiaConfig.GENERAL.enableCustomServerSelectionGui.get())
                            {
                                this.mc.displayGuiScreen(new GuiMultiplayerIN(new MainMenuScreen()));
                            }
                            else
                            {
                                this.mc.displayGuiScreen(new MultiplayerScreen(new MainMenuScreen()));
                            }
                        }
                        this.disconnectClickCount = 0;
                    }
                }
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

                manager.setNetHandler(new IClientStatusNetHandler()
                {
                    private long currentSystemTime = 0L;

                    @Override
                    public void handleServerInfo(@Nonnull SServerInfoPacket packet)
                    {
                        this.currentSystemTime = Util.milliTime();
                        manager.sendPacket(new CPingPacket(this.currentSystemTime));
                    }

                    @Override
                    public void handlePong(@Nonnull SPongPacket packet)
                    {
                        long i = this.currentSystemTime;
                        long j = Util.milliTime();
                        IndicatiaEventHandler.currentServerPing = (int) (j - i);
                    }

                    @Override
                    public void onDisconnect(@Nonnull ITextComponent component) {}
                });
                manager.sendPacket(new CHandshakePacket(address.getIP(), address.getPort(), ProtocolType.STATUS));
                manager.sendPacket(new CServerQueryPacket());
            }
            catch (Exception e) {}
        });
    }

    private static void runAFK(ClientPlayerEntity player)
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
            IndicatiaMod.LOGGER.info("Stopping AFK Command");
        }
        if (IndicatiaEventHandler.autoFish)
        {
            IndicatiaEventHandler.autoFish = false;
            IndicatiaEventHandler.autoFishTick = 0;
            IndicatiaMod.LOGGER.info("Stopping Autofish Command");
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void replaceArmorLayer(List<LayerRenderer<LivingEntity, EntityModel<LivingEntity>>> layerLists, LayerRenderer<LivingEntity, EntityModel<LivingEntity>> newLayer, LivingRenderer<?, ?> render, LivingEntity entity)
    {
        int armorLayerIndex = -1;

        if (IndicatiaConfig.GENERAL.enableOldArmorRender.get())
        {
            for (int i = 0; i < layerLists.size(); i++)
            {
                LayerRenderer<LivingEntity, EntityModel<LivingEntity>> layer = layerLists.get(i);

                if (layer.getClass().equals(BipedArmorLayer.class))
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
                LayerRenderer<LivingEntity, EntityModel<LivingEntity>> layer = layerLists.get(i);

                if (layer.getClass().equals(LayerAllArmor.class))
                {
                    armorLayerIndex = i;
                }
            }
            if (armorLayerIndex >= 0)
            {
                if (entity instanceof AbstractClientPlayerEntity)
                {
                    layerLists.set(armorLayerIndex, new BipedArmorLayer(render, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));
                }
                else if ((entity instanceof ZombieEntity || entity instanceof GiantEntity) && !(entity instanceof ZombieVillagerEntity))
                {
                    layerLists.set(armorLayerIndex, new BipedArmorLayer(render, new ZombieModel<>(0.5F, true), new ZombieModel<>(1.0F, true)));
                }
                else if (entity instanceof AbstractSkeletonEntity)
                {
                    layerLists.set(armorLayerIndex, new BipedArmorLayer(render, new SkeletonModel<>(0.5F, true), new SkeletonModel<>(1.0F, true)));
                }
                else if (entity instanceof ZombieVillagerEntity)
                {
                    layerLists.set(armorLayerIndex, new BipedArmorLayer(render, new ZombieVillagerModel<>(0.5F, true), new ZombieVillagerModel<>(1.0F, true)));
                }
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void replaceCapeLayer(List<LayerRenderer<LivingEntity, EntityModel<LivingEntity>>> layerLists, LayerRenderer newLayer)
    {
        int capeLayerIndex = -1;

        for (int i = 0; i < layerLists.size(); i++)
        {
            LayerRenderer<LivingEntity, EntityModel<LivingEntity>> layer = layerLists.get(i);

            if (layer.getClass().equals(CapeLayer.class))
            {
                capeLayerIndex = i;
            }
        }
        if (capeLayerIndex >= 0)
        {
            layerLists.set(capeLayerIndex, newLayer);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void replaceElytraLayer(List<LayerRenderer<LivingEntity, EntityModel<LivingEntity>>> layerLists, LayerRenderer newLayer)
    {
        int elytraLayerIndex = -1;

        if (IndicatiaConfig.GENERAL.enableOldArmorRender.get())
        {
            for (int i = 0; i < layerLists.size(); i++)
            {
                LayerRenderer<LivingEntity, EntityModel<LivingEntity>> layer = layerLists.get(i);

                if (layer.getClass().equals(ElytraLayer.class))
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

    private static void replaceArrowLayer(List<LayerRenderer<LivingEntity, EntityModel<LivingEntity>>> layerLists, LayerRenderer<LivingEntity, EntityModel<LivingEntity>> newLayer)
    {
        int arrowLayerIndex = -1;

        for (int i = 0; i < layerLists.size(); i++)
        {
            LayerRenderer<LivingEntity, EntityModel<LivingEntity>> layer = layerLists.get(i);

            if (layer.getClass().equals(ArrowLayer.class))
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

            if (mc.objectMouseOver != null && mc.world != null && mc.field_71442_b != null && mc.gameRenderer != null)
            {
                if (IndicatiaEventHandler.autoFishTick % 4 == 0)
                {
                    for (Hand hand : CachedEnum.handValues)
                    {
                        ItemStack itemStack = mc.player.getHeldItem(hand);
                        boolean fishingRod = mc.player.getHeldItem(hand).getItem() instanceof FishingRodItem;

                        if (fishingRod)
                        {
                            if (mc.objectMouseOver.getType() == RayTraceResult.Type.BLOCK)
                            {
                                BlockRayTraceResult blockRayTrace = (BlockRayTraceResult)mc.objectMouseOver;
                                BlockPos pos = blockRayTrace.getPos();

                                if (!mc.world.getBlockState(pos).isAir(mc.world, pos))
                                {
                                    int count = itemStack.getCount();
                                    ActionResultType result = mc.field_71442_b.func_217292_a(mc.player, mc.world, hand, blockRayTrace);

                                    if (result == ActionResultType.SUCCESS)
                                    {
                                        mc.player.swingArm(hand);

                                        if (!itemStack.isEmpty() && (itemStack.getCount() != count || mc.field_71442_b.isInCreativeMode()))
                                        {
                                            mc.gameRenderer.itemRenderer.resetEquippedProgress(hand);
                                        }
                                        return;
                                    }
                                    if (result == ActionResultType.FAIL)
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
                            mc.player.sendMessage(JsonUtils.create(LangUtils.translate("commands.auto_fish.not_equipped_fishing_rod")).setStyle(JsonUtils.red()));
                            return;
                        }

                        if (itemStack.isEmpty() && (mc.objectMouseOver == null || mc.objectMouseOver.getType() == RayTraceResult.Type.MISS))
                        {
                            ForgeHooks.onEmptyClick(mc.player, hand);
                        }
                        if (!itemStack.isEmpty() && mc.field_71442_b.processRightClick(mc.player, mc.world, hand) == ActionResultType.SUCCESS)
                        {
                            mc.gameRenderer.itemRenderer.resetEquippedProgress(hand);
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