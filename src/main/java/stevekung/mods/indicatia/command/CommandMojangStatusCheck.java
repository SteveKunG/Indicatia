package stevekung.mods.indicatia.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import stevekung.mods.indicatia.utils.ThreadCheckMojangStatus;
import stevekung.mods.stevekunglib.utils.ClientCommandBase;

public class CommandMojangStatusCheck extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "mojangstatus";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        ThreadCheckMojangStatus thread = new ThreadCheckMojangStatus();
        thread.start();
    }
}