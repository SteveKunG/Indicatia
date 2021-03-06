package com.stevekung.indicatia.utils.fabric;

import com.stevekung.indicatia.core.Indicatia;
import com.stevekung.indicatia.core.IndicatiaFabric;

public class KeypadHandlerImpl
{
    public static boolean isAltChatEnabled()
    {
        return IndicatiaFabric.CONFIG.getConfig().enableAlternateChatKey && Indicatia.keyBindAltChat.consumeClick();
    }

    public static boolean isAltChatMatches(int keysym, int scancode)
    {
        return IndicatiaFabric.CONFIG.getConfig().enableAlternateChatKey && Indicatia.keyBindAltChat.matches(keysym, scancode);
    }
}