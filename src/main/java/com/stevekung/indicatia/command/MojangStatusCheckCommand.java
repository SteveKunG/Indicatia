package com.stevekung.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.utils.ThreadCheckMojangStatus;
import com.stevekung.stevekungslib.utils.CommonUtils;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class MojangStatusCheckCommand
{
    private static final ThreadCheckMojangStatus THREAD = new ThreadCheckMojangStatus();

    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("mojangstatus").requires(requirement -> requirement.hasPermissionLevel(0)).executes(command -> MojangStatusCheckCommand.runThread()));
    }

    private static int runThread()
    {
        CommonUtils.execute(MojangStatusCheckCommand.THREAD);
        return 1;
    }
}