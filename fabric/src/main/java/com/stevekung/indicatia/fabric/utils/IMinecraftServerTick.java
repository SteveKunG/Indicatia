package com.stevekung.indicatia.fabric.utils;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface IMinecraftServerTick
{
    long[] indicatia$getTickTime(ResourceKey<Level> dim);
}