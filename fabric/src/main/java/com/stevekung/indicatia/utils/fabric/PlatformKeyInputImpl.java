package com.stevekung.indicatia.utils.fabric;

import com.stevekung.indicatia.Indicatia;

public class PlatformKeyInputImpl
{
    public static boolean isAltChatEnabled()
    {
        return Indicatia.CONFIG.enableAlternateChatKey && Indicatia.KEY_ALT_OPEN_CHAT.consumeClick();
    }

    public static boolean isAltChatMatches(int keyCode, int scanCode)
    {
        return Indicatia.CONFIG.enableAlternateChatKey && Indicatia.KEY_ALT_OPEN_CHAT.matches(keyCode, scanCode);
    }
}