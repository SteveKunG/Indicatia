package com.stevekung.indicatia.utils.fabric;

import com.stevekung.indicatia.core.IndicatiaFabric;

public class PlatformConfigImpl
{
    public static boolean getOldArmorRender()
    {
        return IndicatiaFabric.CONFIG.general.enableOldArmorRender;
    }

    public static boolean getBlockHitAnimation()
    {
        return IndicatiaFabric.CONFIG.general.enableBlockhitAnimation;
    }

    public static boolean getCustomPlayerList()
    {
        return IndicatiaFabric.CONFIG.general.enableCustomPlayerList;
    }

    public static boolean getConfirmToDisconnect()
    {
        return IndicatiaFabric.CONFIG.general.enableConfirmToDisconnect;
    }

    public static boolean getHypixelChatMode()
    {
        return IndicatiaFabric.CONFIG.general.enableHypixelChatMode;
    }

    public static boolean getHypixelDropdownShortcut()
    {
        return IndicatiaFabric.CONFIG.general.enableHypixelDropdownShortcutGame;
    }

    public static boolean getRenderBossHealthBar()
    {
        return IndicatiaFabric.CONFIG.general.enableBossHealthBarRender;
    }

    public static boolean getAFKMessage()
    {
        return IndicatiaFabric.CONFIG.general.enableAFKMessage;
    }

    public static int getAFKMessageTime()
    {
        return IndicatiaFabric.CONFIG.general.afkMessageTime;
    }
}