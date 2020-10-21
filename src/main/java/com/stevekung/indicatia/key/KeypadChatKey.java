package com.stevekung.indicatia.key;

import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.core.IndicatiaMod;

import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeypadChatKey implements IKeyConflictContext
{
    @Override
    public boolean isActive()
    {
        return !KeyConflictContext.GUI.isActive();
    }

    @Override
    public boolean conflicts(IKeyConflictContext other)
    {
        return false;
    }

    public static boolean isAltChatEnabled()
    {
        return IndicatiaConfig.GENERAL.enableAlternateChatKey.get() && IndicatiaMod.keyBindAltChat.isPressed();
    }

    public static boolean isAltChatMatches(int keysym, int scancode)
    {
        return IndicatiaConfig.GENERAL.enableAlternateChatKey.get() && IndicatiaMod.keyBindAltChat.matchesKey(keysym, scancode);
    }
}