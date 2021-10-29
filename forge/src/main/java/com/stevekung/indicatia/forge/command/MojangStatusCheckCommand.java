package com.stevekung.indicatia.forge.command;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.utils.ThreadCheckMojangStatus;
import com.stevekung.stevekungslib.forge.utils.client.command.ClientCommands;
import com.stevekung.stevekungslib.forge.utils.client.command.IClientCommand;
import com.stevekung.stevekungslib.forge.utils.client.command.IClientSharedSuggestionProvider;
import com.stevekung.stevekungslib.utils.CommonUtils;

public class MojangStatusCheckCommand implements IClientCommand
{
    @Override
    public void register(CommandDispatcher<IClientSharedSuggestionProvider> dispatcher)
    {
        dispatcher.register(ClientCommands.literal("mojangstatus").executes(command -> MojangStatusCheckCommand.runThread()));
    }

    private static int runThread()
    {
        CommonUtils.runAsync(ThreadCheckMojangStatus::new);
        return 1;
    }
}