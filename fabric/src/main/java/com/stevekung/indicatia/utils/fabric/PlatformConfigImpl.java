package com.stevekung.indicatia.utils.fabric;

import com.stevekung.indicatia.core.IndicatiaFabricMod;

public class PlatformConfigImpl
{
    public static boolean getOldArmorRender()
    {
        return IndicatiaFabricMod.CONFIG.getConfig().enableOldArmorRender;
    }

    public static boolean getBlockHitAnimation()
    {
        return IndicatiaFabricMod.CONFIG.getConfig().enableBlockhitAnimation;
    }

    public static boolean getCustomPlayerList()
    {
        return IndicatiaFabricMod.CONFIG.getConfig().enableCustomPlayerList;
    }

    public static boolean getConfirmToDisconnect()
    {
        return IndicatiaFabricMod.CONFIG.getConfig().enableConfirmToDisconnect;
    }

    public static boolean getHypixelChatMode()
    {
        return IndicatiaFabricMod.CONFIG.getConfig().enableHypixelChatMode;
    }

    public static boolean getHypixelDropdownShortcut()
    {
        return IndicatiaFabricMod.CONFIG.getConfig().enableHypixelDropdownShortcutGame;
    }

    public static boolean getRenderBossHealthBar()
    {
        return IndicatiaFabricMod.CONFIG.getConfig().enableBossHealthBarRender;
    }

    public static boolean getAFKMessage()
    {
        return IndicatiaFabricMod.CONFIG.getConfig().enableAFKMessage;
    }

    public static int getAFKMessageTime()
    {
        return IndicatiaFabricMod.CONFIG.getConfig().afkMessageTime;
    }
}