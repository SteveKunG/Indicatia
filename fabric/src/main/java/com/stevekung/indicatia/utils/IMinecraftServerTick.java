package com.stevekung.indicatia.utils;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface IMinecraftServerTick
{
    long[] getTickTime(ResourceKey<Level> dim);
}