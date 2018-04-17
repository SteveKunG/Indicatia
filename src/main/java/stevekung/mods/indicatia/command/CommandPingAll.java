package stevekung.mods.indicatia.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import stevekung.mods.stevekunglib.utils.ClientCommandBase;
import stevekung.mods.stevekunglib.utils.LangUtils;

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
        List<NetworkPlayerInfo> infoList = new ArrayList<>(Minecraft.getMinecraft().player.connection.getPlayerInfoMap());
        Collections.sort(infoList, (info1, info2) -> Integer.compare(info2.getResponseTime(), info1.getResponseTime()));

        infoList.forEach(info ->
        {
            Minecraft.getMinecraft().player.sendChatMessage(info.getGameProfile().getName() + ": " + LangUtils.translate("message.ping") + " " + info.getResponseTime());
        });
    }
}