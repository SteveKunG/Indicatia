package com.stevekung.indicatia.utils.fabric;

import com.stevekung.indicatia.fabric.core.IndicatiaFabric;

public class PlatformConfigImpl
{
    public static boolean enableEnchantedRenderingOnSkull()
    {
        return IndicatiaFabric.CONFIG.enableEnchantedRenderingOnSkull;
    }

    public static boolean confirmationOnDisconnect()
    {
        return IndicatiaFabric.CONFIG.confirmationOnDisconnect;
    }
}