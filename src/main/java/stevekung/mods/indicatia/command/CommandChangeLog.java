package stevekung.mods.indicatia.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import stevekung.mods.indicatia.gui.GuiFullChangeLog;
import stevekung.mods.stevekunglib.util.ClientCommandBase;

public class CommandChangeLog extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "inchangelog";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        GuiFullChangeLog gui = new GuiFullChangeLog();
        gui.display();
    }
}