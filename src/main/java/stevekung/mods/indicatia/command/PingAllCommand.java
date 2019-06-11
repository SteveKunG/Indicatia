package stevekung.mods.indicatia.command;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class PingAllCommand implements ClientCommandPlugin
{
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("pingall").requires(requirement -> requirement.hasPermissionLevel(0)).executes(command -> PingAllCommand.getAllLatency()));
    }

    private static int getAllLatency()
    {
        List<PlayerListEntry> infoList = new ArrayList<>(MinecraftClient.getInstance().player.networkHandler.getPlayerList());
        infoList.sort((info1, info2) -> Integer.compare(info2.getLatency(), info1.getLatency()));
        infoList.forEach(info -> MinecraftClient.getInstance().player.sendChatMessage(LangUtils.translate("commands.ping_all.result", info.getProfile().getName(), info.getLatency())));
        return 1;
    }
}