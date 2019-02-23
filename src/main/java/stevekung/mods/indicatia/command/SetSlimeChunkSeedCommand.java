package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

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
            throw new CommandException(LangUtils.translateComponent("commands.set_slime_seed.not_allow_zero").setStyle(JsonUtils.red()));
        }

        try
        {
            long longSeed = Long.parseLong(seed);
            ExtendedConfig.slimeChunkSeed = longSeed;
            source.sendFeedback(LangUtils.translateComponent("commands.set_slime_seed.set", longSeed), false);
        }
        catch (NumberFormatException e)
        {
            ExtendedConfig.slimeChunkSeed = seed.hashCode();
            source.sendFeedback(LangUtils.translateComponent("commands.set_slime_seed.set", seed.hashCode()), false);
        }
        ExtendedConfig.save();
        return 0;
    }
}