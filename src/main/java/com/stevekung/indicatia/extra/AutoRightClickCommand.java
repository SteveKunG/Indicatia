package com.stevekung.indicatia.extra;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.stevekungslib.utils.JsonUtils;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;

public class AutoRightClickCommand implements ClientCommandPlugin
{
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("autorclick")
                .then(ArgumentBuilders.literal("enable").executes(requirement -> AutoRightClickCommand.enableAutoRightClick(requirement.getSource())))
                .then(ArgumentBuilders.literal("disable").executes(requirement -> AutoRightClickCommand.disableAutoRightClick(requirement.getSource())))
                .then(ArgumentBuilders.literal("mode").then(ArgumentBuilders.literal("normal").executes(requirement -> AutoRightClickCommand.changeAutoRightClickMode(requirement.getSource(), "Normal")))
                        .then(ArgumentBuilders.literal("fastest").executes(requirement -> AutoRightClickCommand.changeAutoRightClickMode(requirement.getSource(), "Fastest")))));
    }

    private static int enableAutoRightClick(CottonClientCommandSource source)
    {
        ExtraEventHandler.autoRightClick = true;
        source.sendFeedback(JsonUtils.create("Enabled Auto Right Click"));
        return 0;
    }

    private static int disableAutoRightClick(CottonClientCommandSource source)
    {
        ExtraEventHandler.autoRightClick = false;
        ExtraEventHandler.autoRightClickTick = 0;
        source.sendFeedback(JsonUtils.create("Disabled Auto Right Click"));
        return 0;
    }

    private static int changeAutoRightClickMode(CottonClientCommandSource source, String mode)
    {
        ExtraEventHandler.autoRightClickMode = mode;
        source.sendFeedback(JsonUtils.create("Set to " + mode));
        ExtraExtendedConfig.save();
        return 0;
    }
}