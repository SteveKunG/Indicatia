package com.stevekung.indicatia.utils.neoforge;

import com.stevekung.indicatia.Indicatia;

public class PlatformKeyInputImpl
{
    public static boolean isAltChatEnabled()
    {
        return Indicatia.CONFIG.enableAlternateChatKey && Indicatia.KEY_ALT_OPEN_CHAT.consumeClick();
    }

    public static boolean isAltChatMatches(int keysym, int scancode)
    {
        return Indicatia.CONFIG.enableAlternateChatKey && Indicatia.KEY_ALT_OPEN_CHAT.matches(keysym, scancode);
    }
}