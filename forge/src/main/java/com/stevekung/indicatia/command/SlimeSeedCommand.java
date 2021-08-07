package com.stevekung.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.command.arguments.SlimeSeedArgumentType;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.client.command.ClientCommands;
import com.stevekung.stevekungslib.utils.client.command.IClientCommand;
import com.stevekung.stevekungslib.utils.client.command.IClientSharedSuggestionProvider;

public class SlimeSeedCommand implements IClientCommand
{
    @Override
    public void register(CommandDispatcher<IClientSharedSuggestionProvider> dispatcher)
    {
        dispatcher.register(ClientCommands.literal("slimeseed").then(ClientCommands.argument("seed", SlimeSeedArgumentType.string()).executes(requirement -> SlimeSeedCommand.setSlimeSeed(requirement.getSource(), SlimeSeedArgumentType.getString(requirement, "seed")))));
    }

    private static int setSlimeSeed(IClientSharedSuggestionProvider source, String seed)
    {
        try
        {
            var longSeed = Long.parseLong(seed);
            IndicatiaSettings.INSTANCE.slimeChunkSeed = longSeed;
            source.sendFeedback(LangUtils.translate("commands.slime_seed.set", longSeed));
        }
        catch (NumberFormatException e)
        {
            IndicatiaSettings.INSTANCE.slimeChunkSeed = seed.hashCode();
            source.sendFeedback(LangUtils.translate("commands.slime_seed.set", seed.hashCode()));
        }
        IndicatiaSettings.INSTANCE.save();
        return 1;
    }
}