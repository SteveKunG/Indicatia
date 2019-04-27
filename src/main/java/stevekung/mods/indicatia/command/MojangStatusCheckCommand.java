package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import net.minecraft.server.command.CommandSource;
import stevekung.mods.indicatia.utils.ThreadCheckMojangStatus;

public class MojangStatusCheckCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
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