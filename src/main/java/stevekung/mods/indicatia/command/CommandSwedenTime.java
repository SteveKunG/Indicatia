package stevekung.mods.indicatia.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import stevekung.mods.stevekunglib.utils.ClientCommandBase;
import stevekung.mods.stevekunglib.utils.ClientUtils;

public class CommandSwedenTime extends ClientCommandBase
{
    @Override
    public String getName()
    {
        return "swedentime";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));
        ClientUtils.printClientMessage("Current Sweden Time is : " + dateFormat.format(date));
    }
}