package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import stevekung.mods.indicatia.utils.ThreadCheckMojangStatus;

public class MojangStatusCheckCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("mojangstatus").requires(requirement -> requirement.hasPermissionLevel(0)).executes(command -> MojangStatusCheckCommand.runThread()));
    }

    private static int runThread()
    {
        ThreadCheckMojangStatus thread = new ThreadCheckMojangStatus();
        thread.start();
        return 0;
    }
}