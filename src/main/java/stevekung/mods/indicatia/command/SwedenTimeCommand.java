package stevekung.mods.indicatia.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import stevekung.mods.stevekungslib.utils.client.ClientUtils;

public class SwedenTimeCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("swedentime").requires(requirement -> requirement.hasPermissionLevel(0)).executes(command -> SwedenTimeCommand.checkTime()));
    }

    private static int checkTime()
    {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));
        ClientUtils.printClientMessage("Current Sweden Time is : " + dateFormat.format(date));
        return 1;
    }
}