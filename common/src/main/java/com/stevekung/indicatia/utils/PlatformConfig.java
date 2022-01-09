package com.stevekung.indicatia.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class PlatformConfig
{
    @ExpectPlatform
    public static boolean enableEnchantedRenderingOnSkull()
    {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean confirmationOnDisconnect()
    {
        throw new AssertionError();
    }
}