package com.stevekung.indicatia.utils.hud;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.status.*;

public class HUDHelper
{
    private static final ThreadPoolExecutor REALTIME_PINGER = new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Real Time Server Pinger #%d").setDaemon(true).build());
    public static int currentServerPing;

    public static void getRealTimeServerPing(ServerData server)
    {
        REALTIME_PINGER.submit(() ->
        {
            try
            {
                var address = ServerAddress.parseString(server.ip);
                var optional = ServerNameResolver.DEFAULT.resolveAddress(address).map(ResolvedServerAddress::asInetSocketAddress);

                if (optional.isPresent())
                {
                    var manager = Connection.connectToServer(optional.get(), false);
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
                            var i = this.currentSystemTime;
                            var j = Util.getMillis();
                            HUDHelper.currentServerPing = (int) (j - i);
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
            }
            catch (Exception ignored)
            {
            }
        });
    }
}