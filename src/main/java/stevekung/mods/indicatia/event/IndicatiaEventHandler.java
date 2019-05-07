package stevekung.mods.indicatia.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import org.lwjgl.glfw.GLFW;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.extra.GuiRenderWeather;
import stevekung.mods.indicatia.gui.config.GuiExtendedConfig;
import stevekung.mods.indicatia.handler.KeyBindingHandler;
import stevekung.mods.indicatia.utils.AutoLoginFunction;
import stevekung.mods.indicatia.utils.CapeUtils;
import stevekung.mods.stevekungslib.utils.CommonUtils;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;
import stevekung.mods.stevekungslib.utils.client.ClientUtils;
import stevekung.mods.stevekungslib.utils.enums.CachedEnum;

public class IndicatiaEventHandler
{
    private MinecraftClient mc;
    public static final List<Long> LEFT_CLICK = new ArrayList<>();
    public static final List<Long> RIGHT_CLICK = new ArrayList<>();
    public static int currentServerPing;
    private static final ThreadPoolExecutor serverPinger = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Real Time Server Pinger #%d").setDaemon(true).build());
    private static int pendingPingTicks = 100;
    private static boolean initLayer = true;
    private int disconnectClickCount;
    private int disconnectClickCooldown;

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
        this.mc = MinecraftClient.getInstance();
    }

    public void onClientTick()
    {
        if (this.mc.player != null)
        {
            IndicatiaEventHandler.runAFK(this.mc.player);
            IndicatiaEventHandler.processAutoFish(this.mc);
            AutoLoginFunction.runAutoLoginFunction();
            CapeUtils.loadCapeTexture();

            this.onPressKey();

            /*if (IndicatiaEventHandler.printAutoGG && IndicatiaEventHandler.printAutoGGTicks < IndicatiaConfig.GENERAL.autoGGDelay.get())
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
            }*/
            /*if (this.disconnectClickCooldown > 0) TODO
            {
                this.disconnectClickCooldown--;
            }
            if (this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiIngameMenu)
            {
                if (IndicatiaConfig.GENERAL.enableConfirmDisconnectButton.get() && !this.mc.isSingleplayer())
                {
                    this.mc.currentScreen.buttons.forEach(button ->
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
            else
            {
                this.disconnectClickCount = 0;
                this.disconnectClickCooldown = 0;
            }*/

            /*if (IndicatiaEventHandler.pendingPingTicks > 0 && this.mc.getCurrentIntegratedServer() != null)
            {
                IndicatiaEventHandler.pendingPingTicks--;

                if (IndicatiaEventHandler.pendingPingTicks == 0)
                {
                    IndicatiaEventHandler.getRealTimeServerPing(this.mc.getCurrentIntegratedServer());
                    IndicatiaEventHandler.pendingPingTicks = 100;
                }
            }

            for (EnumAction action : CachedEnum.actionValues)
            {
                if (action != EnumAction.NONE)
                {
                    if (IndicatiaConfig.GENERAL.enableAdditionalBlockhitAnimation.get() && this.mc.gameSettings.keyBindAttack.isKeyDown() && this.mc.hitResult != null && this.mc.hitResult.type == HitResult.Type.BLOCK && !this.mc.player.getHeldItemMainhand().isEmpty() && this.mc.player.getHeldItemMainhand().getUseAction() == action)
                    {
                        this.mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                }
            }*/
        }
    }

    /*public void onInputUpdate(InputUpdateEvent event)
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
    }*/

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

    /*@SuppressWarnings("unchecked")
    public void onPreRenderLiving(RenderLivingEvent.Pre<?> event)
    {
        @SuppressWarnings("rawtypes")
        RenderLivingBase renderer = event.getRenderer();
        List<FeatureRenderer<?>> layerLists = renderer.FeatureRenderers;
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
    }*/

    /*@SubscribeEvent
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
    }*/

    //    @SubscribeEvent TODO
    //    public void onDisconnectedFromServerEvent(NetworkEvent.ClientDisconnectionFromServerEvent event)
    //    {
    //        IndicatiaEventHandler.stopCommandTicks();
    //    }

    /*@SubscribeEvent
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
    }*/

    private void onPressKey()
    {
        if (KeyBindingHandler.CONFIG.isPressed())
        {
            GuiExtendedConfig config = new GuiExtendedConfig();
            this.mc.openScreen(config);
        }
        if (KeyBindingHandler.WEATHER.isPressed())
        {
            GuiRenderWeather config = new GuiRenderWeather();
            this.mc.openScreen(config);
        }
        if (KeyBindingHandler.REC.isPressed())
        {
            //HUDRenderEventHandler.recordEnable = !HUDRenderEventHandler.recordEnable; TODO
        }
        if (/*IndicatiaConfig.GENERAL.enableCustomCape.get() && */KeyBindingHandler.CUSTOM_CAPE.isPressed())
        {
            /*GuiCustomCape customCapeGui = new GuiCustomCape();
            this.mc.displayGuiScreen(customCapeGui);*/
        }
        if (ExtendedConfig.instance.toggleSprintUseMode.equals("key_binding") && InputUtil.isKeyPressed(this.mc.window.getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) && KeyBindingHandler.TOGGLE_SPRINT.isPressed())
        {
            ExtendedConfig.instance.toggleSprint = !ExtendedConfig.instance.toggleSprint;
            ClientUtils.setOverlayMessage(JsonUtils.create(ExtendedConfig.instance.toggleSprint ? LangUtils.translate("commands.indicatia.toggle_sprint.enable") : LangUtils.translate("commands.indicatia.toggle_sprint.disable")).getFormattedText());
            ExtendedConfig.instance.save();
        }
        if (ExtendedConfig.instance.toggleSneakUseMode.equals("key_binding") && InputUtil.isKeyPressed(this.mc.window.getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) && KeyBindingHandler.TOGGLE_SNEAK.isPressed())
        {
            ExtendedConfig.instance.toggleSneak = !ExtendedConfig.instance.toggleSneak;
            ClientUtils.setOverlayMessage(JsonUtils.create(ExtendedConfig.instance.toggleSneak ? LangUtils.translate("commands.indicatia.toggle_sneak.enable") : LangUtils.translate("commands.indicatia.toggle_sneak.disable")).getFormattedText());
            ExtendedConfig.instance.save();
        }
        if (KeyBindingHandler.DONATOR.isPressed())
        {
            /*GuiDonator donatorGui = new GuiDonator();
            this.mc.displayGuiScreen(donatorGui);*/
        }
    }

    /*@SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event)
    {
        if (event.getGui() instanceof GuiMainMenu)
        {
            int height = event.getGui().height / 4 + 48;
            event.addButton(new GuiButtonMojangStatus(200, event.getGui().width / 2 - 124, height + 63)
            {
                @Override
                public void onClick(double mouseX, double mouseY)
                {
                    IndicatiaEventHandler.this.mc.displayGuiScreen(new GuiMojangStatusChecker(event.getGui()));
                }
            });
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

                if (IndicatiaConfig.GENERAL.confirmDisconnectMode.get() == IndicatiaConfig.DisconnectMode.GUI)
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
    }*/

//    private static void getRealTimeServerPing(IntegratedServer server)
//    {
//        IndicatiaEventHandler.serverPinger.submit(() ->
//        {
//            try
//            {
//                ServerAddress address = ServerAddress.parse(server.getServerIp());
//                ClientConnection manager = ClientConnection.connect(InetAddress.getByName(address.getAddress()), address.getPort(), false);
//
//                manager.setPacketListener(new ClientQueryPacketListener()
//                {
//                    private long currentSystemTime = 0L;
//
//                    @Override
//                    public void onResponse(QueryResponseS2CPacket packet)
//                    {
//                        this.currentSystemTime = SystemUtil.getMeasuringTimeMs();
//                        manager.send(new QueryPingC2SPacket(this.currentSystemTime));
//                    }
//
//                    @Override
//                    public void onPong(QueryPongS2CPacket packet)
//                    {
//                        long i = this.currentSystemTime;
//                        long j = SystemUtil.getMeasuringTimeMs();
//                        IndicatiaEventHandler.currentServerPing = (int) (j - i);
//                    }
//
//                    @Override
//                    public void onDisconnected(TextComponent component) {}
//                });
//                manager.send(new HandshakeC2SPacket(address.getAddress(), address.getPort(), NetworkState.STATUS));
//                manager.send(new QueryRequestC2SPacket());
//            }
//            catch (Exception e) {}
//        });
//    }

    private static void runAFK(ClientPlayerEntity player)
    {
        if (IndicatiaEventHandler.isAFK)
        {
            IndicatiaEventHandler.afkTicks++;
            int tick = IndicatiaEventHandler.afkTicks;
            int messageMin = 1200 * 5;//TODO IndicatiaConfig.GENERAL.afkMessageTime.get()
            String s = "s";
            float angle = tick % 2 == 0 ? 0.0001F : -0.0001F;

            if (tick == 0)
            {
                s = "";
            }
            //if (IndicatiaConfig.GENERAL.enableAFKMessage.get()) TODO
            {
                if (tick % messageMin == 0)
                {
                    String reason = IndicatiaEventHandler.afkReason;
                    reason = reason.isEmpty() ? "" : ", Reason : " + reason;
                    player.sendChatMessage("AFK : " + CommonUtils.ticksToElapsedTime(tick) + " minute" + s + reason);
                }
            }

            switch (IndicatiaEventHandler.afkMode)
            {
            case "idle":
                player.changeLookDirection(angle, angle);
                break;
            case "360":
                player.changeLookDirection(1.0F, 0.0F);
                break;
            case "360_move":
                player.changeLookDirection(1.0F, 0.0F);
                IndicatiaEventHandler.afkMoveTicks++;
                IndicatiaEventHandler.afkMoveTicks %= 8;
                break;
            case "move":
                player.changeLookDirection(angle, angle);
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

    /*private static void replaceArmorLayer(List<FeatureRenderer<?>> layerLists, FeatureRenderer<?> newLayer, RenderLivingBase<?> render, EntityLivingBase entity)
    {
        int armorLayerIndex = -1;

        if (IndicatiaConfig.GENERAL.enableOldArmorRender.get())
        {
            for (int i = 0; i < layerLists.size(); i++)
            {
                FeatureRenderer<?> layer = layerLists.get(i);

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
                FeatureRenderer<?> layer = layerLists.get(i);

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

    private static void replaceCapeLayer(List<FeatureRenderer<?>> layerLists, FeatureRenderer<?> newLayer)
    {
        int capeLayerIndex = -1;

        for (int i = 0; i < layerLists.size(); i++)
        {
            FeatureRenderer<?> layer = layerLists.get(i);

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

    private static void replaceElytraLayer(List<FeatureRenderer<?>> layerLists, FeatureRenderer<?> newLayer)
    {
        int elytraLayerIndex = -1;

        if (IndicatiaConfig.GENERAL.enableOldArmorRender.get())
        {
            for (int i = 0; i < layerLists.size(); i++)
            {
                FeatureRenderer<?> layer = layerLists.get(i);

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

    private static void replaceArrowLayer(List<FeatureRenderer<?>> layerLists, FeatureRenderer<?> newLayer)
    {
        int arrowLayerIndex = -1;

        for (int i = 0; i < layerLists.size(); i++)
        {
            FeatureRenderer<?> layer = layerLists.get(i);

            if (layer.getClass().equals(LayerArrow.class))
            {
                arrowLayerIndex = i;
            }
        }
        if (arrowLayerIndex >= 0)
        {
            layerLists.set(arrowLayerIndex, newLayer);
        }
    }*/

    private static void processAutoFish(MinecraftClient mc)
    {
        if (IndicatiaEventHandler.autoFish)
        {
            ++IndicatiaEventHandler.autoFishTick;
            IndicatiaEventHandler.autoFishTick %= 4;

            if (mc.hitResult != null && mc.world != null && mc.interactionManager != null && mc.getEntityRenderManager() != null)
            {
                if (IndicatiaEventHandler.autoFishTick % 4 == 0)
                {
                    for (Hand hand : CachedEnum.handValues)
                    {
                        ItemStack itemStack = mc.player.getStackInHand(hand);
                        boolean fishingRod = mc.player.getStackInHand(hand).getItem() instanceof FishingRodItem;

                        if (fishingRod)
                        {
                            if (mc.hitResult.getType() == HitResult.Type.BLOCK)
                            {
                                BlockHitResult hitResult = (BlockHitResult)mc.hitResult;
                                int amount = itemStack.getAmount();
                                ActionResult actionResult = mc.interactionManager.interactBlock(mc.player, mc.world, hand, hitResult);

                                if (actionResult == ActionResult.SUCCESS)
                                {
                                    mc.player.swingHand(hand);

                                    if (!itemStack.isEmpty() && (itemStack.getAmount() != amount || mc.interactionManager.hasCreativeInventory()))
                                    {
                                        mc.gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
                                    }
                                    return;
                                }
                                if (actionResult == ActionResult.FAIL)
                                {
                                    return;
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

                        if (!itemStack.isEmpty() && mc.interactionManager.interactItem(mc.player, mc.world, hand) == ActionResult.SUCCESS)
                        {
                            mc.gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
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