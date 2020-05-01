package com.stevekung.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.utils.ThreadCheckMojangStatus;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;

public class MojangStatusCheckCommand implements ClientCommandPlugin
{
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("mojangstatus").executes(command -> MojangStatusCheckCommand.runThread()));
    }

    private static int runThread()
    {
        ThreadCheckMojangStatus thread = new ThreadCheckMojangStatus();
        thread.start();
        return 1;
    }
}