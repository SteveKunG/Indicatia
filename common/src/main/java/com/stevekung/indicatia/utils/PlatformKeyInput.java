package com.stevekung.indicatia.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class PlatformKeyInput
{
    @ExpectPlatform
    public static boolean isAltChatEnabled()
    {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isAltChatMatches(int keyCode, int scanCode)
    {
        throw new AssertionError();
    }
}