package stevekung.mods.indicatia.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import stevekung.mods.indicatia.utils.ThreadCheckMojangStatus;

public class CommandMojangStatusCheck extends ClientCommandBase
{
    private static final ThreadCheckMojangStatus check = new ThreadCheckMojangStatus(false);

    @Override
    public String getName()
    {
        return "mojangstatus";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        CommandMojangStatusCheck.check.start();
    }
}