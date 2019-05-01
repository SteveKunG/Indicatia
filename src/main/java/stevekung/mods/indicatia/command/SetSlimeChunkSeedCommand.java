package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.Feedback;
import net.minecraft.server.command.CommandSource;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class SetSlimeChunkSeedCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("slimeseed").requires(requirement -> requirement.hasPermissionLevel(0)).then(ArgumentBuilders.argument("seed", StringArgumentType.string()).executes(requirement -> SetSlimeChunkSeedCommand.setSlimeSeed(StringArgumentType.getString(requirement, "seed")))));
    }

    private static int setSlimeSeed(String seed)
    {
        if (seed.equals("0"))
        {
            Feedback.sendError(LangUtils.translateComponent("commands.set_slime_seed.not_allow_zero"));
            return 0;
        }

        try
        {
            long longSeed = Long.parseLong(seed);
            ExtendedConfig.instance.slimeChunkSeed = longSeed;
            Feedback.sendFeedback(LangUtils.translateComponent("commands.set_slime_seed.set", longSeed));
        }
        catch (NumberFormatException e)
        {
            ExtendedConfig.instance.slimeChunkSeed = seed.hashCode();
            Feedback.sendFeedback(LangUtils.translateComponent("commands.set_slime_seed.set", seed.hashCode()));
        }
        ExtendedConfig.instance.save();
        return 1;
    }
}