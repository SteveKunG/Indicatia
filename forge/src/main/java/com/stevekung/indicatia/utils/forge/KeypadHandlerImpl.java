package com.stevekung.indicatia.utils.forge;

import com.stevekung.indicatia.core.Indicatia;
import com.stevekung.indicatia.forge.config.IndicatiaConfig;

public class KeypadHandlerImpl
{
    public static boolean isAltChatEnabled()
    {
        return IndicatiaConfig.GENERAL.enableAlternateChatKey.get() && Indicatia.KEY_ALT_OPEN_CHAT.consumeClick();
    }

    public static boolean isAltChatMatches(int keysym, int scancode)
    {
        return IndicatiaConfig.GENERAL.enableAlternateChatKey.get() && Indicatia.KEY_ALT_OPEN_CHAT.matches(keysym, scancode);
    }
}