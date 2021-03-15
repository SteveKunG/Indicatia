package com.stevekung.indicatia.utils;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

public class MinecraftServerTick
{
    @ExpectPlatform
    public static long[] getTickTime(MinecraftServer server, ResourceKey<Level> dim)
    {
        throw new Error();
    }
}