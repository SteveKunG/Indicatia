package com.stevekung.indicatia.utils.forge;

import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.core.IndicatiaMod;

public class KeypadHandlerImpl
{
    public static boolean isAltChatEnabled()
    {
        return IndicatiaConfig.GENERAL.enableAlternateChatKey.get() && IndicatiaMod.keyBindAltChat.consumeClick();
    }

    public static boolean isAltChatMatches(int keysym, int scancode)
    {
        return IndicatiaConfig.GENERAL.enableAlternateChatKey.get() && IndicatiaMod.keyBindAltChat.matches(keysym, scancode);
    }
}