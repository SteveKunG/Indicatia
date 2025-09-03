package com.stevekung.indicatia.utils;

import net.minecraft.client.input.KeyEvent;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class PlatformKeyInput
{
    @ExpectPlatform
    public static boolean isAltChatEnabled()
    {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isAltChatMatches(KeyEvent keyEvent)
    {
        throw new AssertionError();
    }
}