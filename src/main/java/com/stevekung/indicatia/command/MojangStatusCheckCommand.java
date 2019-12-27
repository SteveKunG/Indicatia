package com.stevekung.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.utils.ThreadCheckMojangStatus;
import com.stevekung.stevekungslib.utils.CommonUtils;
import com.stevekung.stevekungslib.utils.client.command.ClientCommands;
import com.stevekung.stevekungslib.utils.client.command.IClientCommand;
import com.stevekung.stevekungslib.utils.client.command.IClientSuggestionProvider;

public class MojangStatusCheckCommand implements IClientCommand
{
    private static final ThreadCheckMojangStatus THREAD = new ThreadCheckMojangStatus();

    @Override
    public void register(CommandDispatcher<IClientSuggestionProvider> dispatcher)
    {
        dispatcher.register(ClientCommands.literal("mojangstatus").executes(command -> MojangStatusCheckCommand.runThread()));
    }

    private static int runThread()
    {
        CommonUtils.execute(MojangStatusCheckCommand.THREAD);
        return 1;
    }
}