package com.stevekung.indicatia.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class Platform
{
    @ExpectPlatform
    public static boolean isModLoaded(String modId)
    {
        throw new AssertionError();
    }
}