package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import stevekung.mods.indicatia.config.ExtendedConfig;

public class SetSlimeChunkSeedCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("slimeseed").requires(requirement -> requirement.hasPermissionLevel(0)).then(Commands.argument("seed", StringArgumentType.string()).executes(requirement -> SetSlimeChunkSeedCommand.setSlimeSeed(requirement.getSource(), StringArgumentType.getString(requirement, "seed")))));
    }

    private static int setSlimeSeed(CommandSource source, String seed)
    {
        if (seed.equals("0"))
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.set_slime_seed.not_allow_zero"));
            return 0;
        }

        try
        {
            long longSeed = Long.parseLong(seed);
            ExtendedConfig.INSTANCE.slimeChunkSeed = longSeed;
            source.sendFeedback(LangUtils.translateComponent("commands.set_slime_seed.set", longSeed), false);
        }
        catch (NumberFormatException e)
        {
            ExtendedConfig.INSTANCE.slimeChunkSeed = seed.hashCode();
            source.sendFeedback(LangUtils.translateComponent("commands.set_slime_seed.set", seed.hashCode()), false);
        }
        ExtendedConfig.INSTANCE.save();
        return 1;
    }
}