package com.stevekung.indicatia.utils.fabric;

import com.stevekung.indicatia.utils.IMinecraftServerTick;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

public class MinecraftServerTickImpl
{
    public static long[] getTickTime(MinecraftServer server, ResourceKey<Level> dim)
    {
        return ((IMinecraftServerTick)server).getTickTime(dim);
    }
}