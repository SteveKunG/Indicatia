package stevekung.mods.indicatia.command;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class PingAllCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("pingall").requires(requirement -> requirement.hasPermissionLevel(0)).executes(command -> PingAllCommand.getAllLatency()));
    }

    private static int getAllLatency()
    {
        List<NetworkPlayerInfo> infoList = new ArrayList<>(Minecraft.getInstance().player.field_71174_a.getPlayerInfoMap());
        infoList.sort((info1, info2) -> Integer.compare(info2.getResponseTime(), info1.getResponseTime()));
        infoList.forEach(info -> Minecraft.getInstance().player.sendChatMessage(LangUtils.translate("commands.ping_all.result", info.getGameProfile().getName(), info.getResponseTime())));
        return 1;
    }
}