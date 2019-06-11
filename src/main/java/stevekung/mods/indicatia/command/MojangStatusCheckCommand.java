package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import stevekung.mods.indicatia.utils.ThreadCheckMojangStatus;

public class MojangStatusCheckCommand implements ClientCommandPlugin
{
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("mojangstatus").requires(requirement -> requirement.hasPermissionLevel(0)).executes(command -> MojangStatusCheckCommand.runThread()));
    }

    private static int runThread()
    {
        ThreadCheckMojangStatus thread = new ThreadCheckMojangStatus();
        thread.start();
        return 1;
    }
}