package com.stevekung.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.command.arguments.SlimeSeedArgumentType;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class SlimeSeedCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("slimeseed").requires(requirement -> requirement.hasPermissionLevel(0)).then(Commands.argument("seed", SlimeSeedArgumentType.string()).executes(requirement -> SlimeSeedCommand.setSlimeSeed(requirement.getSource(), SlimeSeedArgumentType.getString(requirement, "seed")))));
    }

    private static int setSlimeSeed(CommandSource source, String seed)
    {
        try
        {
            long longSeed = Long.parseLong(seed);
            ExtendedConfig.INSTANCE.slimeChunkSeed = longSeed;
            source.sendFeedback(LangUtils.translateComponent("commands.slime_seed.set", longSeed), false);
        }
        catch (NumberFormatException e)
        {
            ExtendedConfig.INSTANCE.slimeChunkSeed = seed.hashCode();
            source.sendFeedback(LangUtils.translateComponent("commands.slime_seed.set", seed.hashCode()), false);
        }
        ExtendedConfig.INSTANCE.save();
        return 1;
    }
}