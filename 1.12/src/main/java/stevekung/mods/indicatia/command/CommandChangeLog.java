package stevekung.mods.indicatia.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import stevekung.mods.indicatia.gui.GuiFullChangeLog;

public class CommandChangeLog extends ClientCommandBase
{
    private static final GuiFullChangeLog gui = new GuiFullChangeLog();

    @Override
    public String getName()
    {
        return "inchangelog";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        CommandChangeLog.gui.display();
    }
}