package com.stevekung.indicatia.utils;

import java.util.Arrays;

import com.stevekung.stevekungslib.utils.JsonUtils;

import net.minecraft.client.MinecraftClient;

public class ThreadCheckMojangStatus extends Thread
{
    public ThreadCheckMojangStatus()
    {
        super("Mojang Status Check Thread");
    }

    @Override
    public void run()
    {
        Arrays.stream(MojangStatusChecker.values).forEach(checker ->
        {
            MojangServerStatus status = checker.getServiceStatus();
            MinecraftClient.getInstance().player.addChatMessage(JsonUtils.create(checker.getName() + ": ").append(JsonUtils.create(status.getColor() + status.getStatus())), false);
        });
    }
}