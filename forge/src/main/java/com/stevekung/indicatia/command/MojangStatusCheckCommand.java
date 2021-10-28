package com.stevekung.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.utils.ThreadCheckMojangStatus;
import com.stevekung.stevekungslib.utils.CommonUtils;
import com.stevekung.stevekungslib.utils.forge.client.command.ClientCommands;
import com.stevekung.stevekungslib.utils.forge.client.command.IClientCommand;
import com.stevekung.stevekungslib.utils.forge.client.command.IClientSharedSuggestionProvider;

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