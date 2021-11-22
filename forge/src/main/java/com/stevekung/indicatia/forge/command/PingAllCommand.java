package com.stevekung.indicatia.forge.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.stevekunglib.forge.utils.client.command.ClientCommands;
import com.stevekung.stevekunglib.forge.utils.client.command.IClientCommand;
import com.stevekung.stevekunglib.forge.utils.client.command.IClientSharedSuggestionProvider;
import com.stevekung.stevekunglib.utils.LangUtils;
import net.minecraft.client.Minecraft;

public class PingAllCommand implements IClientCommand
{
    @Override
    public void register(CommandDispatcher<IClientSharedSuggestionProvider> dispatcher)
    {
        dispatcher.register(ClientCommands.literal("pingall").executes(command -> PingAllCommand.getAllLatency()));
    }

    private static int getAllLatency()
    {
        var infoList = Lists.newArrayList(Minecraft.getInstance().player.connection.getOnlinePlayers());
        infoList.sort((info1, info2) -> Integer.compare(info2.getLatency(), info1.getLatency()));
        infoList.stream().limit(5).forEach(info -> Minecraft.getInstance().player.chat(LangUtils.translate("commands.ping_all.result", info.getProfile().getName(), info.getLatency()).getString()));
        return 1;
    }
}