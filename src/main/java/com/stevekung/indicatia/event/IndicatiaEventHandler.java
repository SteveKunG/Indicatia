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
import com.stevekung.indicatia.gui.exconfig.screen.OffsetRenderPreviewScreen;
import com.stevekung.indicatia.gui.screen.ConfirmDisconnectScreen;
import com.stevekung.indicatia.gui.screen.MojangStatusScreen;
import com.stevekung.indicatia.gui.widget.MojangStatusButton;
import com.stevekung.indicatia.handler.KeyBindingHandler;
import com.stevekung.indicatia.hud.InfoUtils;
import com.stevekung.indicatia.utils.AFKMode;
import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;
import com.stevekung.stevekungslib.utils.enums.CachedEnum;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.status.IClientStatusNetHandler;
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
import net.minecraftforge.client.event.*;
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
    private long lastPinger = -1L;
    private int disconnectClickCount;
    private int disconnectClickCooldown;
    private boolean initVersionCheck;

    public static boolean START_AFK;
    public static AFKMode AFK_MODE = AFKMode.IDLE;
    public static String AFK_REASON;
    public static int afkMoveTicks;
    public static int afkTicks;

    public static boolean START_AUTO_FISH;
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
                IndicatiaEventHandler.afkTick(this.mc.player);
                IndicatiaEventHandler.autoFishTick(this.mc);

                if (this.disconnectClickCooldown > 0)
                {
                    this.disconnectClickCooldown--;
                }
                if (this.mc.currentScreen != null && this.mc.currentScreen instanceof IngameMenuScreen && IndicatiaConfig.GENERAL.enableConfirmDisconnectButton.get() && IndicatiaConfig.GENERAL.confirmDisconnectMode.get() == IndicatiaConfig.DisconnectMode.CLICK && !this.mc.isSingleplayer())
                {
                    for (Widget button : this.mc.currentScreen.buttons)
                    {
                        if (button.getMessage().contains(LangUtils.translate("menu.click_to_disconnect")))
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
                else
                {
                    this.disconnectClickCount = 0;
                    this.disconnectClickCooldown = 0;
                }

                if (this.mc.getCurrentServerData() != null)
                {
                    long now = Util.milliTime();

                    if (this.lastPinger == -1L || now - this.lastPinger > 5000L)
                    {
                        this.lastPinger = now;
                        IndicatiaEventHandler.getRealTimeServerPing(this.mc.getCurrentServerData());
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
        ForgeIngameGui.renderObjective = IndicatiaConfig.GENERAL.enableSidebarScoreboardRender.get();
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event)
    {
        MovementInput movement = event.getMovementInput();
        PlayerEntity player = event.getPlayer();

        // canceled turn back
        if (KeyBindingHandler.KEY_TOGGLE_SPRINT.isKeyDown())
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
        if (IndicatiaEventHandler.AFK_MODE == AFKMode.RANDOM_MOVE_360)
        {
            int afkMoveTick = IndicatiaEventHandler.afkMoveTicks;

            if (afkMoveTick > 0 && afkMoveTick < 2)
            {
                movement.moveForward += Math.random();
                movement.forwardKeyDown = true;
            }
            else if (afkMoveTick > 2 && afkMoveTick < 4)
            {
                movement.moveStrafe += Math.random();
                movement.leftKeyDown = true;
            }
            else if (afkMoveTick > 4 && afkMoveTick < 6)
            {
                movement.moveForward -= Math.random();
                movement.backKeyDown = true;
            }
            else if (afkMoveTick > 6 && afkMoveTick < 8)
            {
                movement.moveStrafe -= Math.random();
                movement.rightKeyDown = true;
            }
        }
    }

    @SubscribeEvent
    public void onMouseClick(InputEvent.MouseInputEvent event)
    {
        if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_1 && event.getAction() == GLFW.GLFW_PRESS)
        {
            IndicatiaEventHandler.LEFT_CLICK.add(Util.milliTime());
        }
        if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_2 && event.getAction() == GLFW.GLFW_PRESS)
        {
            IndicatiaEventHandler.RIGHT_CLICK.add(Util.milliTime());
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
            InfoUtils.INSTANCE.getMouseOverEntityExtended(this.mc);
        }
    }

    @SubscribeEvent
    public void onPressKey(InputEvent.KeyInputEvent event)
    {
        if (KeyBindingHandler.KEY_QUICK_CONFIG.isKeyDown())
        {
            ExtendedConfigScreen config = new ExtendedConfigScreen();
            this.mc.displayGuiScreen(config);
        }
        if (KeyBindingHandler.KEY_TOGGLE_SPRINT.isKeyDown())
        {
            ExtendedConfig.INSTANCE.toggleSprint = !ExtendedConfig.INSTANCE.toggleSprint;
            ClientUtils.setOverlayMessage(JsonUtils.create(ExtendedConfig.INSTANCE.toggleSprint ? LangUtils.translate("commands.indicatia.toggle_sprint.enable") : LangUtils.translate("commands.indicatia.toggle_sprint.disable")).getFormattedText());
            ExtendedConfig.INSTANCE.save();
        }
        if (KeyBindingHandler.KEY_TOGGLE_SNEAK.isKeyDown())
        {
            ExtendedConfig.INSTANCE.toggleSneak = !ExtendedConfig.INSTANCE.toggleSneak;
            ClientUtils.setOverlayMessage(JsonUtils.create(ExtendedConfig.INSTANCE.toggleSneak ? LangUtils.translate("commands.indicatia.toggle_sneak.enable") : LangUtils.translate("commands.indicatia.toggle_sneak.disable")).getFormattedText());
            ExtendedConfig.INSTANCE.save();
        }
    }

    @SubscribeEvent
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event)
    {
        Screen screen = event.getGui();

        if (screen instanceof MainMenuScreen)
        {
            int height = screen.height / 4 + 48;
            event.addWidget(new MojangStatusButton(screen.width / 2 + 104, height + 63, button -> this.mc.displayGuiScreen(new MojangStatusScreen(screen))));
        }
    }

    @SubscribeEvent
    public void onScreenMouseClicked(GuiScreenEvent.MouseClickedEvent.Pre event)
    {
        Screen screen = event.getGui();

        if (IndicatiaConfig.GENERAL.enableConfirmDisconnectButton.get() && screen instanceof IngameMenuScreen && !this.mc.isSingleplayer())
        {
            IGuiEventListener listener = screen.children().get(7);

            if (listener instanceof Button && listener.isMouseOver(event.getMouseX(), event.getMouseY()))
            {
                Button button = (Button)listener;

                if (button.getMessage().equals(LangUtils.translate("menu.disconnect")))
                {
                    button.playDownSound(this.mc.getSoundHandler());

                    if (IndicatiaConfig.GENERAL.confirmDisconnectMode.get() == IndicatiaConfig.DisconnectMode.GUI)
                    {
                        this.mc.displayGuiScreen(new ConfirmDisconnectScreen(screen));
                    }
                    else
                    {
                        this.disconnectClickCount++;
                        button.setMessage(TextFormatting.RED + LangUtils.translate("menu.click_to_disconnect"));

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
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event)
    {
        if (this.mc.currentScreen instanceof OffsetRenderPreviewScreen)
        {
            event.setCanceled(true);
            return;
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

    private static void afkTick(ClientPlayerEntity player)
    {
        if (IndicatiaEventHandler.START_AFK)
        {
            IndicatiaEventHandler.afkTicks++;
            int tick = IndicatiaEventHandler.afkTicks;
            int messageMin = 1200 * IndicatiaConfig.GENERAL.afkMessageTime.get();
            float angle = tick % 2 == 0 ? 0.0001F : -0.0001F;

            if (IndicatiaConfig.GENERAL.enableAFKMessage.get())
            {
                if (tick % messageMin == 0)
                {
                    String reason = IndicatiaEventHandler.AFK_REASON;
                    reason = reason.isEmpty() ? "" : ", Reason : " + reason;
                    player.sendChatMessage("AFK : " + StringUtils.ticksToElapsedTime(tick) + " minute" + (tick == 0 ? "" : "s") + reason);
                }
            }

            switch (IndicatiaEventHandler.AFK_MODE)
            {
            case IDLE:
                player.rotateTowards(angle, angle);
                break;
            case RANDOM_MOVE:
                player.rotateTowards(angle, angle);
                IndicatiaEventHandler.afkMoveTicks++;
                IndicatiaEventHandler.afkMoveTicks %= 8;
                break;
            case RANDOM_360:
                player.rotateTowards((float)(Math.random() + 1.0F), 0.0F);
                break;
            case RANDOM_MOVE_360:
                player.rotateTowards((float)(Math.random() + 1.0F), 0.0F);
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
        if (IndicatiaEventHandler.START_AFK)
        {
            IndicatiaEventHandler.START_AFK = false;
            IndicatiaEventHandler.AFK_REASON = "";
            IndicatiaEventHandler.afkTicks = 0;
            IndicatiaEventHandler.afkMoveTicks = 0;
            IndicatiaEventHandler.AFK_MODE = AFKMode.IDLE;
            IndicatiaMod.LOGGER.info("Stopping AFK Command");
        }
        if (IndicatiaEventHandler.START_AUTO_FISH)
        {
            IndicatiaEventHandler.START_AUTO_FISH = false;
            IndicatiaEventHandler.autoFishTick = 0;
            IndicatiaMod.LOGGER.info("Stopping Autofish Command");
        }
    }

    private static void autoFishTick(Minecraft mc)
    {
        if (IndicatiaEventHandler.START_AUTO_FISH)
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
                            IndicatiaEventHandler.START_AUTO_FISH = false;
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