package stevekung.mods.indicatia.handler;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.utils.AutoLogin.AutoLoginData;
import stevekung.mods.indicatia.utils.Base64Utils;
import stevekung.mods.indicatia.utils.GameProfileUtil;

public class PlayerChatHandler
{
    private final Minecraft mc;
    private final Thread currentThread = Thread.currentThread();
    private final Queue<ListenableFutureTask> queue = Queues.newArrayDeque();

    public PlayerChatHandler(Minecraft mc)
    {
        this.mc = mc;
    }

    @SubscribeEvent
    public void onClientConnectedToServer(ClientConnectedToServerEvent event)
    {
        this.run(new Runnable()
        {
            @Override
            public void run()
            {
                MinecraftForge.EVENT_BUS.register(new PlayerRunCommandHandler());
            }
        });
    }

    private ListenableFuture run(Runnable runnable)
    {
        Validate.notNull(runnable);
        return this.callListenable(Executors.callable(runnable));
    }

    private ListenableFuture callListenable(Callable callable)
    {
        Validate.notNull(callable);

        if (!this.isCurrentThread())
        {
            ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(callable);

            synchronized (this.queue)
            {
                this.queue.add(listenablefuturetask);
                return listenablefuturetask;
            }
        }
        else
        {
            try
            {
                return Futures.immediateFuture(callable.call());
            }
            catch (Exception e)
            {
                return Futures.immediateFailedCheckedFuture(e);
            }
        }
    }

    private boolean isCurrentThread()
    {
        return Thread.currentThread() == this.currentThread;
    }

    public class PlayerRunCommandHandler
    {
        @SubscribeEvent
        public void onEntityJoinWorld(EntityJoinWorldEvent event)
        {
            if (event.entity instanceof EntityClientPlayerMP)
            {
                EntityClientPlayerMP player = (EntityClientPlayerMP) event.entity;
                ServerData data = PlayerChatHandler.this.mc.func_147104_D();
                this.runAutoLoginCommand(player, data);
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }

        private void runAutoLoginCommand(EntityClientPlayerMP player, ServerData data)
        {
            if (data != null)
            {
                for (AutoLoginData login : ExtendedConfig.loginData.getAutoLoginList())
                {
                    if (data.serverIP.equalsIgnoreCase(login.getServerIP()) && GameProfileUtil.getUUID().equals(login.getUUID()))
                    {
                        player.sendChatMessage(login.getCommand() + Base64Utils.decode(login.getValue()));
                    }
                }
            }
        }
    }
}