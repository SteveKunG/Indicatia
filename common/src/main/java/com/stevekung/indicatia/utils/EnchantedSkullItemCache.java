package com.stevekung.indicatia.utils;

import com.mojang.authlib.GameProfile;
import com.stevekung.indicatia.Indicatia;

public class EnchantedSkullItemCache
{
    public static GameProfile lastGameProfile;
    public static boolean glintNext;

    public static void preCache(GameProfile gameProfile, boolean hasFoil)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnSkulls)
        {
            EnchantedSkullItemCache.lastGameProfile = gameProfile;
            EnchantedSkullItemCache.glintNext = hasFoil;
        }
    }

    public static void postCache()
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnSkulls)
        {
            EnchantedSkullItemCache.lastGameProfile = null;
            EnchantedSkullItemCache.glintNext = false;
        }
    }
}