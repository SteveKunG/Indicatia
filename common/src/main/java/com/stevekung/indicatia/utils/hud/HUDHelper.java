package com.stevekung.indicatia.utils.hud;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.hud.InfoOverlays;
import com.stevekung.indicatia.hud.InfoUtils;
import com.stevekung.indicatia.utils.event.InfoOverlayEvents;
import com.stevekung.stevekunglib.utils.LangUtils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.status.*;
import net.minecraft.world.level.ChunkPos;

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

    public static List<InfoOverlay> getInfoOverlays(Minecraft mc)
    {
        var infos = Lists.<InfoOverlay>newArrayList();
        var playerPos = new BlockPos(mc.getCameraEntity().getX(), mc.getCameraEntity().getBoundingBox().minY, mc.getCameraEntity().getZ());

        if (IndicatiaSettings.INSTANCE.fps)
        {
            var fps = Minecraft.fps;
            infos.add(new InfoOverlay("hud.fps", String.valueOf(fps), IndicatiaSettings.INSTANCE.fpsColor, fps <= 25 ? IndicatiaSettings.INSTANCE.fpsLow25Color : fps <= 49 ? IndicatiaSettings.INSTANCE.fps26And49Color : IndicatiaSettings.INSTANCE.fpsValueColor, InfoOverlay.Position.LEFT));
        }

        if (!mc.hasSingleplayerServer())
        {
            if (IndicatiaSettings.INSTANCE.ping)
            {
                var responseTime = InfoUtils.INSTANCE.getPing();
                infos.add(new InfoOverlay("hud.ping", responseTime + "ms", IndicatiaSettings.INSTANCE.pingColor, InfoUtils.INSTANCE.getResponseTimeColor(responseTime), InfoOverlay.Position.LEFT));

                if (IndicatiaSettings.INSTANCE.pingToSecond)
                {
                    var responseTimeSecond = InfoUtils.INSTANCE.getPing() / 1000.0D;
                    infos.add(new InfoOverlay("hud.ping.delay", responseTimeSecond + "s", IndicatiaSettings.INSTANCE.pingToSecondColor, InfoUtils.INSTANCE.getResponseTimeColor((int) (responseTimeSecond * 1000.0D)), InfoOverlay.Position.LEFT));
                }
            }
            if (IndicatiaSettings.INSTANCE.serverIP && mc.getCurrentServer() != null)
            {
                infos.add(new InfoOverlay("IP", (mc.isConnectedToRealms() ? "Realms Server" : mc.getCurrentServer().ip) + (IndicatiaSettings.INSTANCE.serverIPMCVersion ? "/" + Minecraft.getInstance().getVersionType() : ""), IndicatiaSettings.INSTANCE.serverIPColor, IndicatiaSettings.INSTANCE.serverIPValueColor, InfoOverlay.Position.LEFT));
            }
        }

        if (IndicatiaSettings.INSTANCE.xyz)
        {
            var stringPos = playerPos.getX() + " " + playerPos.getY() + " " + playerPos.getZ();
            var nether = mc.player.level.dimensionType().piglinSafe() ? "Nether " : "";
            infos.add(new InfoOverlay(nether + "XYZ", stringPos, IndicatiaSettings.INSTANCE.xyzColor, IndicatiaSettings.INSTANCE.xyzValueColor, InfoOverlay.Position.LEFT));

            if (mc.player.level.dimensionType().piglinSafe())
            {
                var stringNetherPos = playerPos.getX() * 8 + " " + playerPos.getY() + " " + playerPos.getZ() * 8;
                infos.add(new InfoOverlay("Overworld XYZ", stringNetherPos, IndicatiaSettings.INSTANCE.xyzColor, IndicatiaSettings.INSTANCE.xyzValueColor, InfoOverlay.Position.LEFT));
            }
        }

        if (IndicatiaSettings.INSTANCE.direction)
        {
            infos.add(InfoOverlays.getDirection(mc));
        }

        if (IndicatiaSettings.INSTANCE.biome)
        {
            var chunkPos = new ChunkPos(playerPos);
            var worldChunk = mc.level.getChunk(chunkPos.x, chunkPos.z);
            var biomeResource = mc.level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(mc.level.getBiome(playerPos));
            var biomeName = "biome." + biomeResource.getNamespace() + "." + biomeResource.getPath();
            infos.add(new InfoOverlay("hud.biome", !worldChunk.isEmpty() ? new TranslatableComponent(biomeName).getString() : LangUtils.translate("hud.biome.waiting_for_chunk").getString(), IndicatiaSettings.INSTANCE.biomeColor, IndicatiaSettings.INSTANCE.biomeValueColor, InfoOverlay.Position.LEFT));
        }

        if (IndicatiaSettings.INSTANCE.slimeChunkFinder && !mc.player.level.dimensionType().piglinSafe())
        {
            infos.add(new InfoOverlay("hud.slime_chunk", InfoUtils.INSTANCE.isSlimeChunk(mc.player.blockPosition()) ? "gui.yes" : "gui.no", IndicatiaSettings.INSTANCE.slimeChunkColor, IndicatiaSettings.INSTANCE.slimeChunkValueColor, InfoOverlay.Position.LEFT));
        }

        if (IndicatiaSettings.INSTANCE.tps)
        {
            infos.add(InfoOverlays.OVERALL_TPS);
            infos.add(InfoOverlays.OVERWORLD_TPS);
            infos.addAll(InfoOverlays.ALL_TPS);
            infos.add(InfoOverlays.TPS);
        }

        if (IndicatiaSettings.INSTANCE.realTime)
        {
            infos.add(InfoOverlays.getRealWorldTime());
        }
        if (IndicatiaSettings.INSTANCE.gameTime)
        {
            infos.add(InfoOverlays.getGameTime(mc));
        }
        if (IndicatiaSettings.INSTANCE.gameWeather && mc.level.isRaining())
        {
            var weather = !mc.level.isThundering() ? "hud.weather.raining" : "hud.weather.thundering";
            infos.add(new InfoOverlay("hud.weather", weather, IndicatiaSettings.INSTANCE.gameWeatherColor, IndicatiaSettings.INSTANCE.gameWeatherValueColor, InfoOverlay.Position.RIGHT));
        }
        if (IndicatiaSettings.INSTANCE.moonPhase)
        {
            infos.add(new InfoOverlay("hud.moon_phase", InfoUtils.INSTANCE.getMoonPhase(mc), IndicatiaSettings.INSTANCE.moonPhaseColor, IndicatiaSettings.INSTANCE.moonPhaseValueColor, InfoOverlay.Position.RIGHT));
        }
        InfoOverlayEvents.INFO_OVERLAY.invoker().addInfos(infos);
        return infos;
    }
}