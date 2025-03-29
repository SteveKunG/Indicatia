package com.stevekung.indicatia.utils;

import com.stevekung.indicatia.Indicatia;
import net.minecraft.world.item.component.ResolvableProfile;

public class EnchantedSkullItemCache
{
    public static ResolvableProfile lastResolvableProfile;
    public static boolean glintNext;

    public static void preCache(ResolvableProfile resolvableProfile, boolean hasFoil)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnSkulls)
        {
            EnchantedSkullItemCache.lastResolvableProfile = resolvableProfile;
            EnchantedSkullItemCache.glintNext = hasFoil;
        }
    }

    public static void postCache()
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnSkulls)
        {
            EnchantedSkullItemCache.lastResolvableProfile = null;
            EnchantedSkullItemCache.glintNext = false;
        }
    }
}