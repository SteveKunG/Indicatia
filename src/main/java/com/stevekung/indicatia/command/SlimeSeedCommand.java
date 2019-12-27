package com.stevekung.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.command.arguments.SlimeSeedArgumentType;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.client.command.ClientCommands;
import com.stevekung.stevekungslib.utils.client.command.IClientCommand;
import com.stevekung.stevekungslib.utils.client.command.IClientSuggestionProvider;

public class SlimeSeedCommand implements IClientCommand
{
    @Override
    public void register(CommandDispatcher<IClientSuggestionProvider> dispatcher)
    {
        dispatcher.register(ClientCommands.literal("slimeseed").requires(requirement -> requirement.hasPermissionLevel(0)).then(ClientCommands.argument("seed", SlimeSeedArgumentType.string()).executes(requirement -> SlimeSeedCommand.setSlimeSeed(requirement.getSource(), SlimeSeedArgumentType.getString(requirement, "seed")))));
    }

    private static int setSlimeSeed(IClientSuggestionProvider source, String seed)
    {
        try
        {
            long longSeed = Long.parseLong(seed);
            ExtendedConfig.INSTANCE.slimeChunkSeed = longSeed;
            source.sendFeedback(LangUtils.translateComponent("commands.slime_seed.set", longSeed));
        }
        catch (NumberFormatException e)
        {
            ExtendedConfig.INSTANCE.slimeChunkSeed = seed.hashCode();
            source.sendFeedback(LangUtils.translateComponent("commands.slime_seed.set", seed.hashCode()));
        }
        ExtendedConfig.INSTANCE.save();
        return 1;
    }
}