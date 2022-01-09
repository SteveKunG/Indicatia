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
    public static boolean isAltChatMatches(int keysym, int scancode)
    {
        throw new AssertionError();
    }
}