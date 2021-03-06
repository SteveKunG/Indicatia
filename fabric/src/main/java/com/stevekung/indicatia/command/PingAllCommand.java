package com.stevekung.indicatia.command;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.stevekungslib.utils.LangUtils;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;

public class PingAllCommand
{
    public PingAllCommand(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        dispatcher.register(ClientCommandManager.literal("pingall").executes(command -> PingAllCommand.getAllLatency()));
    }

    private static int getAllLatency()
    {
        List<PlayerInfo> infoList = Lists.newArrayList(Minecraft.getInstance().player.connection.getOnlinePlayers());
        infoList.sort((info1, info2) -> Integer.compare(info2.getLatency(), info1.getLatency()));
        infoList.stream().limit(5).forEach(info -> Minecraft.getInstance().player.chat(LangUtils.translate("commands.ping_all.result", info.getProfile().getName(), info.getLatency()).getString()));
        return 1;
    }
}