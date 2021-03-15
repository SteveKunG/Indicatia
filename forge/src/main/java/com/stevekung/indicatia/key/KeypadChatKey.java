package com.stevekung.indicatia.key;

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
}