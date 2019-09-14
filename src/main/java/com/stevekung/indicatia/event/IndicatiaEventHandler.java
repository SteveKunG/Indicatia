package com.stevekung.indicatia.event;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Nonnull;

import org.lwjgl.glfw.GLFW;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.core.IndicatiaMod;
import com.stevekung.indicatia.gui.exconfig.screen.ExtendedConfigScreen;
import com.stevekung.indicatia.gui.screen.ConfirmDisconnectScreen;
import com.stevekung.indicatia.gui.screen.MojangStatusScreen;
import com.stevekung.indicatia.gui.widget.MojangStatusButton;
import com.stevekung.indicatia.handler.KeyBindingHandler;
import com.stevekung.indicatia.utils.InfoUtils;
import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;
import com.stevekung.stevekungslib.utils.enums.CachedEnum;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.status.IClientStatusNetHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.ForgeIngameGui;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class IndicatiaEventHandler
{
    private final Minecraft mc;
    public static final List<Long> LEFT_CLICK = new ArrayList<>();
    public static final List<Long> RIGHT_CLICK = new ArrayList<>();
    public static int currentServerPing;
    private static final ThreadPoolExecutor REALTIME_PINGER = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Real Time Server Pinger #%d").setDaemon(true).build());
    private static int pendingPingTicks = 100;
    private int disconnectClickCount;
    private int disconnectClickCooldown;
    private boolean initVersionCheck;

    public static boolean isAFK;
    public static String afkMode = "idle";
    public static String afkReason;
    public static int afkMoveTicks;
    public static int afkTicks;

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

                if (this.disconnectClickCooldown > 0)
                {
                    this.disconnectClickCooldown--;
                }
                if (this.mc.currentScreen != null && this.mc.currentScreen instanceof IngameMenuScreen)//TODO Testing
                {
                    if (IndicatiaConfig.GENERAL.enableConfirmDisconnectButton.get() && !this.mc.isSingleplayer())
                    {
                        for (Widget button : this.mc.currentScreen.buttons)
                        {
                            if (button.getMessage().equals(LangUtils.translate("menu.disconnect")) && IndicatiaConfig.GENERAL.confirmDisconnectMode.get() == IndicatiaConfig.DisconnectMode.CLICK)
                            {
                                if (this.disconnectClickCooldown < 60)
                                {
                                    int cooldownSec = 1 + this.disconnectClickCooldown / 20;
                                    button.setMessage(TextFormatting.RED + LangUtils.translate("menu.click_to_disconnect") + " in " + cooldownSec + "...");
                                }
                                if (this.disconnectClickCooldown == 0)
                                {
                                    button.setMessage(LangUtils.translate("menu.disconnect"));
                                    this.disconnectClickCount = 0;
                                }
                            }
                        }
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

                for (UseAction action : CachedEnum.USE_ACTION)
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
        PlayerEntity player = event.getPlayer();

        if (IndicatiaConfig.GENERAL.enableCustomMovementHandler.get())
        {
            // canceled turn back
            if (ExtendedConfig.INSTANCE.toggleSprintUseMode.equalsIgnoreCase("key_binding") && KeyBindingHandler.KEY_TOGGLE_SPRINT.isKeyDown())
            {
                ++movement.moveForward;
            }

            // toggle sneak
            movement.sneak = this.mc.gameSettings.keyBindSneak.isKeyDown() || ExtendedConfig.INSTANCE.toggleSneak && !player.isSpectator();

            if (ExtendedConfig.INSTANCE.toggleSneak && !player.isSpectator() && !player.isCreative())
            {
                movement.moveStrafe = (float)(movement.moveStrafe * 0.3D);
                movement.moveForward = (float)(movement.moveForward * 0.3D);
            }

            // toggle sprint
            if (ExtendedConfig.INSTANCE.toggleSprint && !player.isPotionActive(Effects.BLINDNESS) && !ExtendedConfig.INSTANCE.toggleSneak)
            {
                player.setSprinting(true);
            }

            // afk stuff
            if (IndicatiaEventHandler.afkMode.equals("360_move"))
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

    @SubscribeEvent
    public void onLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event)
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
            ExtendedConfigScreen config = new ExtendedConfigScreen();
            this.mc.displayGuiScreen(config);
        }
        if (ExtendedConfig.INSTANCE.toggleSprintUseMode.equals("key_binding") && KeyBindingHandler.KEY_TOGGLE_SPRINT.isActiveAndMatches(input))
        {
            ExtendedConfig.INSTANCE.toggleSprint = !ExtendedConfig.INSTANCE.toggleSprint;
            ClientUtils.setOverlayMessage(JsonUtils.create(ExtendedConfig.INSTANCE.toggleSprint ? LangUtils.translate("commands.indicatia.toggle_sprint.enable") : LangUtils.translate("commands.indicatia.toggle_sprint.disable")).getFormattedText());
            ExtendedConfig.INSTANCE.save();
        }
        if (ExtendedConfig.INSTANCE.toggleSneakUseMode.equals("key_binding") && KeyBindingHandler.KEY_TOGGLE_SNEAK.isActiveAndMatches(input))
        {
            ExtendedConfig.INSTANCE.toggleSneak = !ExtendedConfig.INSTANCE.toggleSneak;
            ClientUtils.setOverlayMessage(JsonUtils.create(ExtendedConfig.INSTANCE.toggleSneak ? LangUtils.translate("commands.indicatia.toggle_sneak.enable") : LangUtils.translate("commands.indicatia.toggle_sneak.disable")).getFormattedText());
            ExtendedConfig.INSTANCE.save();
        }
    }

    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event)
    {
        if (event.getGui() instanceof MainMenuScreen)
        {
            int height = event.getGui().height / 4 + 48;
            event.addWidget(new MojangStatusButton(event.getGui().width / 2 + 104, height + 63, button -> this.mc.displayGuiScreen(new MojangStatusScreen(event.getGui()))));
        }
    }

    @SubscribeEvent
    public void onPreActionPerformedGui(GuiScreenEvent.ActionPerformedEvent.Pre event)
    {
        if (IndicatiaConfig.GENERAL.enableConfirmDisconnectButton.get() && event.getGui() instanceof IngameMenuScreen && !this.mc.isSingleplayer())
        {
            if (event.getButton().getMessage().equals(I18n.format("menu.disconnect")))//TODO Testing
            {
                event.setCanceled(true);
                event.getButton().playDownSound(this.mc.getSoundHandler());

                if (IndicatiaConfig.GENERAL.confirmDisconnectMode.get() == IndicatiaConfig.DisconnectMode.GUI)
                {
                    this.mc.displayGuiScreen(new ConfirmDisconnectScreen());
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
                            this.mc.displayGuiScreen(new MultiplayerScreen(new MainMenuScreen()));
                        }
                        this.disconnectClickCount = 0;
                    }
                }
            }
        }
    }

    private static void getRealTimeServerPing(ServerData server)
    {
        IndicatiaEventHandler.REALTIME_PINGER.submit(() ->
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

                    @Override
                    public NetworkManager getNetworkManager()
                    {
                        return manager;
                    }
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

    private static void processAutoFish(Minecraft mc)
    {
        if (IndicatiaEventHandler.autoFish)
        {
            ++IndicatiaEventHandler.autoFishTick;
            IndicatiaEventHandler.autoFishTick %= 4;

            if (mc.objectMouseOver != null && mc.world != null && mc.playerController != null && mc.gameRenderer != null)
            {
                if (IndicatiaEventHandler.autoFishTick % 4 == 0)
                {
                    for (Hand hand : CachedEnum.HAND)
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
                                    ActionResultType result = mc.playerController.func_217292_a(mc.player, mc.world, hand, blockRayTrace);

                                    if (result == ActionResultType.SUCCESS)
                                    {
                                        mc.player.swingArm(hand);

                                        if (!itemStack.isEmpty() && (itemStack.getCount() != count || mc.playerController.isInCreativeMode()))
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
                            mc.player.sendMessage(JsonUtils.create(LangUtils.translate("commands.auto_fish.not_equipped_fishing_rod")).setStyle(JsonUtils.RED));
                            return;
                        }

                        if (itemStack.isEmpty() && (mc.objectMouseOver == null || mc.objectMouseOver.getType() == RayTraceResult.Type.MISS))
                        {
                            ForgeHooks.onEmptyClick(mc.player, hand);
                        }
                        if (!itemStack.isEmpty() && mc.playerController.processRightClick(mc.player, mc.world, hand) == ActionResultType.SUCCESS)
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