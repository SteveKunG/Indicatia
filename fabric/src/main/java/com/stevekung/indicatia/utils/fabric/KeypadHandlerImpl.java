package com.stevekung.indicatia.utils.fabric;

import com.stevekung.indicatia.core.IndicatiaFabricMod;
import com.stevekung.indicatia.core.IndicatiaMod;

public class KeypadHandlerImpl
{
    public static boolean isAltChatEnabled()
    {
        return IndicatiaFabricMod.CONFIG.getConfig().enableAlternateChatKey && IndicatiaMod.keyBindAltChat.consumeClick();
    }

    public static boolean isAltChatMatches(int keysym, int scancode)
    {
        return IndicatiaFabricMod.CONFIG.getConfig().enableAlternateChatKey && IndicatiaMod.keyBindAltChat.matches(keysym, scancode);
    }
}