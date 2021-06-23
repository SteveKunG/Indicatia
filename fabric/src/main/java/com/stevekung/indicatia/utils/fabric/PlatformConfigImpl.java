package com.stevekung.indicatia.utils.fabric;

import com.stevekung.indicatia.core.IndicatiaFabric;

public class PlatformConfigImpl
{
    public static boolean getOldArmorRender()
    {
        return IndicatiaFabric.CONFIG.getConfig().enableOldArmorRender;
    }

    public static boolean getBlockHitAnimation()
    {
        return IndicatiaFabric.CONFIG.getConfig().enableBlockhitAnimation;
    }

    public static boolean getCustomPlayerList()
    {
        return IndicatiaFabric.CONFIG.getConfig().enableCustomPlayerList;
    }

    public static boolean getConfirmToDisconnect()
    {
        return IndicatiaFabric.CONFIG.getConfig().enableConfirmToDisconnect;
    }

    public static boolean getHypixelChatMode()
    {
        return IndicatiaFabric.CONFIG.getConfig().enableHypixelChatMode;
    }

    public static boolean getHypixelDropdownShortcut()
    {
        return IndicatiaFabric.CONFIG.getConfig().enableHypixelDropdownShortcutGame;
    }

    public static boolean getRenderBossHealthBar()
    {
        return IndicatiaFabric.CONFIG.getConfig().enableBossHealthBarRender;
    }

    public static boolean getAFKMessage()
    {
        return IndicatiaFabric.CONFIG.getConfig().enableAFKMessage;
    }

    public static int getAFKMessageTime()
    {
        return IndicatiaFabric.CONFIG.getConfig().afkMessageTime;
    }
}