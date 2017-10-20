package stevekung.mods.indicatia.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.util.AutoLogin.AutoLoginData;
import stevekung.mods.indicatia.util.AutoLoginFunction;
import stevekung.mods.indicatia.util.Base64Util;
import stevekung.mods.indicatia.util.GameProfileUtil;

public class PlayerChatHandler
{
    private final Minecraft mc;

    public PlayerChatHandler(Minecraft mc)
    {
        this.mc = mc;
    }

    @SubscribeEvent
    public void onClientConnectedToServer(ClientConnectedToServerEvent event)
    {
        this.mc.func_152344_a(() -> IndicatiaMod.registerForgeEvent(new PlayerRunCommandHandler()));
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
                this.runAutoLoginFunction(player, data);
                IndicatiaMod.unregisterForgeEvent(this);
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
                        player.sendChatMessage(login.getCommand() + Base64Util.decode(login.getValue()));
                    }
                }
            }
        }

        private void runAutoLoginFunction(EntityPlayerSP player, ServerData data)
        {
            if (data != null)
            {
                for (AutoLoginData login : ExtendedConfig.loginData.getAutoLoginList())
                {
                    if (data.serverIP.equalsIgnoreCase(login.getServerIP()) && GameProfileUtil.getUUID().equals(login.getUUID()) && !login.getFunction().isEmpty())
                    {
                        AutoLoginFunction.functionValue = login.getFunction();
                        AutoLoginFunction.run = true;
                    }
                }
            }
        }
    }
}