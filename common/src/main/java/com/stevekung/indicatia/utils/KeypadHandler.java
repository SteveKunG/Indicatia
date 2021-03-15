package com.stevekung.indicatia.utils;

import me.shedaniel.architectury.annotations.ExpectPlatform;

public class KeypadHandler
{
    @ExpectPlatform
    public static boolean isAltChatEnabled()
    {
        throw new Error();
    }

    @ExpectPlatform
    public static boolean isAltChatMatches(int keysym, int scancode)
    {
        throw new Error();
    }
}