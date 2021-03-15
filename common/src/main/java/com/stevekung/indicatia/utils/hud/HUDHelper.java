package com.stevekung.indicatia.utils.hud;

import java.net.InetAddress;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.stevekung.indicatia.core.IndicatiaMod;
import com.stevekung.indicatia.utils.AFKMode;
import com.stevekung.indicatia.utils.PlatformConfig;
import com.stevekung.stevekungslib.utils.LangUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.status.*;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class HUDHelper
{
    private static final ThreadPoolExecutor REALTIME_PINGER = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Real Time Server Pinger #%d").setDaemon(true).build());
    public static int currentServerPing;

    public static boolean START_AFK;
    public static AFKMode AFK_MODE = AFKMode.IDLE;
    public static String AFK_REASON;
    public static int afkMoveTicks;
    public static int afkTicks;

    public static boolean START_AUTO_FISH;
    private static int autoFishTick;


    public static void getRealTimeServerPing(ServerData server)
    {
        REALTIME_PINGER.submit(() ->
        {
            try
            {
                ServerAddress address = ServerAddress.parseString(server.ip);
                Connection manager = Connection.connectToServer(InetAddress.getByName(address.getHost()), address.getPort(), false);

                manager.setListener(new ClientStatusPacketListener()
                {
                    private long currentSystemTime = 0L;

                    @Override
                    public void handleStatusResponse(ClientboundStatusResponsePacket packet)
                    {
                        this.currentSystemTime = Util.getMillis();
                        manager.send(new ServerboundPingRequestPacket(this.currentSystemTime));
                    }

                    @Override
                    public void handlePongResponse(ClientboundPongResponsePacket packet)
                    {
                        long i = this.currentSystemTime;
                        long j = Util.getMillis();
                        HUDHelper.currentServerPing = (int)(j - i);
                    }

                    @Override
                    public void onDisconnect(Component component) {}

                    @Override
                    public Connection getConnection()
                    {
                        return manager;
                    }
                });
                manager.send(new ClientIntentionPacket(address.getHost(), address.getPort(), ConnectionProtocol.STATUS));
                manager.send(new ServerboundStatusRequestPacket());
            }
            catch (Exception ignored) {}
        });
    }

    public static void afkTick(LocalPlayer player)
    {
        if (START_AFK)
        {
            afkTicks++;
            int tick = afkTicks;
            int messageMin = 1200 * PlatformConfig.getAFKMessageTime();
            float angle = tick % 2 == 0 ? 0.0001F : -0.0001F;

            if (PlatformConfig.getAFKMessage())
            {
                if (tick % messageMin == 0)
                {
                    String reason = AFK_REASON;
                    reason = StringUtil.isNullOrEmpty(reason) ? "" : ", " + LangUtils.translate("commands.afk.reason") + ": " + reason;
                    player.chat("AFK : " + StringUtil.formatTickDuration(tick) + " minute" + (tick == 0 ? "" : "s") + reason);
                }
            }

            switch (AFK_MODE)
            {
                case IDLE:
                    player.turn(angle, angle);
                    break;
                case RANDOM_MOVE:
                    player.turn(angle, angle);
                    afkMoveTicks++;
                    afkMoveTicks %= 8;
                    break;
                case RANDOM_360:
                    player.turn((float)(Math.random() + 1.0F), 0.0F);
                    break;
                case RANDOM_MOVE_360:
                    player.turn((float)(Math.random() + 1.0F), 0.0F);
                    afkMoveTicks++;
                    afkMoveTicks %= 8;
                    break;
            }
        }
        else
        {
            afkTicks = 0;
        }
    }

    public static void stopCommandTicks()
    {
        if (START_AFK)
        {
            START_AFK = false;
            AFK_REASON = "";
            afkTicks = 0;
            afkMoveTicks = 0;
            AFK_MODE = AFKMode.IDLE;
            IndicatiaMod.LOGGER.info("Stopping AFK Command");
        }
        if (START_AUTO_FISH)
        {
            START_AUTO_FISH = false;
            autoFishTick = 0;
            IndicatiaMod.LOGGER.info("Stopping Autofish Command");
        }
    }

    public static void autoFishTick(Minecraft mc)
    {
        if (START_AUTO_FISH)
        {
            autoFishTick++;

            if (mc.hitResult != null)
            {
                if (autoFishTick % 4 == 0)
                {
                    for (InteractionHand hand : InteractionHand.values())
                    {
                        ItemStack itemStack = mc.player.getItemInHand(hand);
                        boolean mainHand = mc.player.getMainHandItem().getItem() instanceof FishingRodItem;
                        boolean offHand = mc.player.getOffhandItem().getItem() instanceof FishingRodItem;

                        if (mc.player.getMainHandItem().getItem() instanceof FishingRodItem)
                        {
                            offHand = false;
                        }

                        if (mainHand || offHand)
                        {
                            if (mc.hitResult.getType() == HitResult.Type.BLOCK)
                            {
                                BlockHitResult blockRayTrace = (BlockHitResult)mc.hitResult;
                                InteractionResult result = mc.gameMode.useItemOn(mc.player, mc.level, hand, blockRayTrace);

                                if (result.consumesAction())
                                {
                                    if (result.shouldSwing())
                                    {
                                        mc.player.swing(hand);
                                    }
                                    return;
                                }
                                if (result == InteractionResult.FAIL)
                                {
                                    return;
                                }
                            }
                        }
                        else
                        {
                            START_AUTO_FISH = false;
                            autoFishTick = 0;
                            mc.player.sendMessage(LangUtils.translate("commands.auto_fish.not_equipped_fishing_rod").copy().withStyle(ChatFormatting.RED), Util.NIL_UUID);
                            return;
                        }

                        if (!itemStack.isEmpty())
                        {
                            InteractionResult result = mc.gameMode.useItem(mc.player, mc.level, hand);

                            if (result.consumesAction())
                            {
                                if (result.shouldSwing())
                                {
                                    mc.player.swing(hand);
                                }
                                mc.gameRenderer.itemInHandRenderer.itemUsed(hand);
                                return;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            autoFishTick = 0;
        }
    }
}