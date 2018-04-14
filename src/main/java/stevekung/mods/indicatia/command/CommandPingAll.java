package stevekung.mods.indicatia.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import stevekung.mods.stevekunglib.util.ClientCommandBase;
import stevekung.mods.stevekunglib.util.LangUtils;

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
        List<NetworkPlayerInfo> infolist = new ArrayList<>(Minecraft.getMinecraft().player.connection.getPlayerInfoMap());
        Collections.sort(infolist, (info1, info2) -> Integer.compare(info2.getResponseTime(), info1.getResponseTime()));

        for (NetworkPlayerInfo info : infolist)
        {
            Minecraft.getMinecraft().player.sendChatMessage(info.getGameProfile().getName() + ": " + LangUtils.translate("message.ping") + " " + info.getResponseTime());
        }
    }
}