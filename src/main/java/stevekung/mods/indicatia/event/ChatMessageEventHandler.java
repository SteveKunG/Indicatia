package stevekung.mods.indicatia.event;

import org.apache.logging.log4j.util.Strings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.utils.AutoLoginFunction;
import stevekung.mods.indicatia.utils.Base64Utils;
import stevekung.mods.stevekunglib.utils.CommonUtils;
import stevekung.mods.stevekunglib.utils.GameProfileUtils;

public class ChatMessageEventHandler
{
    private Minecraft mc;

    public ChatMessageEventHandler()
    {
        this.mc = Minecraft.getMinecraft();
    }

    @SubscribeEvent
    public void onClientConnectedToServer(ClientConnectedToServerEvent event)
    {
        this.mc.addScheduledTask(() -> { MinecraftForge.EVENT_BUS.register(new PlayerSendMessageHandler()); });
    }

    public class PlayerSendMessageHandler
    {
        @SubscribeEvent
        public void onEntityJoinWorld(EntityJoinWorldEvent event)
        {
            if (event.getEntity() instanceof EntityPlayerSP)
            {
                EntityPlayerSP player = (EntityPlayerSP) event.getEntity();
                ServerData data = ChatMessageEventHandler.this.mc.getCurrentServerData();
                this.runAutoLoginCommand(player, data);
                this.runRealmsCommand(player);
                this.runAutoLoginFunction(player, data);
                CommonUtils.unregisterEventHandler(this);
            }
        }

        private void runAutoLoginCommand(EntityPlayerSP player, ServerData data)
        {
            if (data != null)
            {
                ExtendedConfig.loginData.getAutoLoginList().forEach(login ->
                {
                    if (data.serverIP.equalsIgnoreCase(login.getServerIP()) && GameProfileUtils.getUUID().equals(login.getUUID()))
                    {
                        player.sendChatMessage(login.getCommand() + Base64Utils.decode(login.getValue()));
                    }
                });
            }
        }

        private void runRealmsCommand(EntityPlayerSP player)
        {
            if (Minecraft.getMinecraft().isConnectedToRealms() && Strings.isNotEmpty(ExtendedConfig.realmsMessage))
            {
                player.sendChatMessage(ExtendedConfig.realmsMessage);
            }
        }

        private void runAutoLoginFunction(EntityPlayerSP player, ServerData data)
        {
            if (data != null)
            {
                ExtendedConfig.loginData.getAutoLoginList().forEach(login ->
                {
                    if (data.serverIP.equalsIgnoreCase(login.getServerIP()) && GameProfileUtils.getUUID().equals(login.getUUID()) && !login.getFunction().isEmpty())
                    {
                        AutoLoginFunction.functionValue = login.getFunction();
                        AutoLoginFunction.run = true;
                    }
                });
            }
        }
    }
}