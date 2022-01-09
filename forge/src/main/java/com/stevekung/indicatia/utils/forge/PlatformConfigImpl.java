package com.stevekung.indicatia.utils.forge;

import com.stevekung.indicatia.forge.config.IndicatiaConfig;

public class PlatformConfigImpl
{
    public static boolean enableEnchantedRenderingOnSkull()
    {
        return IndicatiaConfig.CONFIG.enableEnchantedRenderingOnSkull.get();
    }

    public static boolean confirmationOnDisconnect()
    {
        return IndicatiaConfig.CONFIG.confirmationOnDisconnect.get();
    }
}