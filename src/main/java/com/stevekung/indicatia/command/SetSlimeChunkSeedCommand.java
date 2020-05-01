package com.stevekung.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.stevekungslib.utils.LangUtils;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;

public class SetSlimeChunkSeedCommand implements ClientCommandPlugin
{
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("slimeseed").then(ArgumentBuilders.argument("seed", StringArgumentType.string()).executes(requirement -> SetSlimeChunkSeedCommand.setSlimeSeed(StringArgumentType.getString(requirement, "seed"), requirement.getSource()))));
    }

    private static int setSlimeSeed(String seed, CottonClientCommandSource source)
    {
        if (seed.equals("0"))
        {
            source.sendError(LangUtils.translateComponent("commands.set_slime_seed.not_allow_zero"));
            return 1;
        }

        try
        {
            long longSeed = Long.parseLong(seed);
            ExtendedConfig.instance.slimeChunkSeed = longSeed;
            source.sendFeedback(LangUtils.translateComponent("commands.set_slime_seed.set", longSeed));
        }
        catch (NumberFormatException e)
        {
            ExtendedConfig.instance.slimeChunkSeed = seed.hashCode();
            source.sendFeedback(LangUtils.translateComponent("commands.set_slime_seed.set", seed.hashCode()));
        }
        ExtendedConfig.instance.save();
        return 1;
    }
}