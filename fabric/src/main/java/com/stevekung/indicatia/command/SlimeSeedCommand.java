package com.stevekung.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.command.arguments.SlimeSeedArgumentType;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.stevekungslib.utils.LangUtils;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

public class SlimeSeedCommand
{
    public SlimeSeedCommand(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        dispatcher.register(ClientCommandManager.literal("slimeseed").then(ClientCommandManager.argument("seed", SlimeSeedArgumentType.string()).executes(requirement -> SlimeSeedCommand.setSlimeSeed(requirement.getSource(), SlimeSeedArgumentType.getString(requirement, "seed")))));
    }

    private static int setSlimeSeed(FabricClientCommandSource source, String seed)
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