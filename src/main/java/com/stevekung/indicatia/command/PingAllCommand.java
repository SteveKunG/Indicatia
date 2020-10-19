package com.stevekung.indicatia.command;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.client.command.ClientCommands;
import com.stevekung.stevekungslib.utils.client.command.IClientCommand;
import com.stevekung.stevekungslib.utils.client.command.IClientSuggestionProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;

public class PingAllCommand implements IClientCommand
{
    @Override
    public void register(CommandDispatcher<IClientSuggestionProvider> dispatcher)
    {
        dispatcher.register(ClientCommands.literal("pingall").executes(command -> PingAllCommand.getAllLatency()));
    }

    private static int getAllLatency()
    {
        List<NetworkPlayerInfo> infoList = new ArrayList<>(Minecraft.getInstance().player.connection.getPlayerInfoMap());
        infoList.sort((info1, info2) -> Integer.compare(info2.getResponseTime(), info1.getResponseTime()));
        infoList.forEach(info -> Minecraft.getInstance().player.sendChatMessage(LangUtils.translate("commands.ping_all.result", info.getGameProfile().getName(), info.getResponseTime()).getString()));
        return 1;
    }
}