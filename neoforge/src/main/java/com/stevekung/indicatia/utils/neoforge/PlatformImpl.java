package com.stevekung.indicatia.utils.neoforge;

import net.neoforged.fml.ModList;

public class PlatformImpl
{
    public static boolean isModLoaded(String modId)
    {
        return ModList.get().isLoaded(modId);
    }
}