package com.stevekung.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.indicatia.gui.exconfig.screen.ExtendedConfigScreen;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import net.minecraft.client.MinecraftClient;

public class IndicatiaCommand implements ClientCommandPlugin
{
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("indicatia").executes(command -> IndicatiaCommand.openGui()));
        dispatcher.register(ArgumentBuilders.literal("in").executes(command -> IndicatiaCommand.openGui()));
    }

    private static int openGui()
    {
        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().openScreen(new ExtendedConfigScreen()));
        return 1;
    }
}