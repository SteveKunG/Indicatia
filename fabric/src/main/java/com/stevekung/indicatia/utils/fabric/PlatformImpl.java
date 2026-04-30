package com.stevekung.indicatia.utils.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class PlatformImpl
{
    public static boolean isModLoaded(String modId)
    {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}