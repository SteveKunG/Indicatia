package stevekung.mods.indicatia.command;

import java.util.Collection;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import stevekung.mods.indicatia.core.IndicatiaMod;

public class CommandPingAll extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "pingall";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        Collection<NetworkPlayerInfo> infolist = IndicatiaMod.MC.player.connection.getPlayerInfoMap();

        for (NetworkPlayerInfo info : infolist)
        {
            IndicatiaMod.MC.player.sendChatMessage(info.getGameProfile().getName() + ": Ping " + info.getResponseTime());
        }
    }
}