package com.stevekung.indicatia.utils;

import com.stevekung.indicatia.Indicatia;

public class EnchantedSkullItemCache
{
    public static boolean playerHead;
    public static boolean glintNext;

    public static void preCache(boolean playerHead, boolean hasFoil)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnSkulls)
        {
            EnchantedSkullItemCache.playerHead = playerHead;
            EnchantedSkullItemCache.glintNext = hasFoil;
        }
    }

    public static void postCache()
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnSkulls)
        {
            EnchantedSkullItemCache.playerHead = false;
            EnchantedSkullItemCache.glintNext = false;
        }
    }
}