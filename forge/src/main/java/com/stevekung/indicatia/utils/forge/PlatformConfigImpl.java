package com.stevekung.indicatia.utils.forge;

import com.stevekung.indicatia.forge.config.IndicatiaConfig;

public class PlatformConfigImpl
{
    public static boolean getRenderInfo()
    {
        return IndicatiaConfig.GENERAL.enableRenderInfo.get();
    }

    public static boolean getOldArmorRender()
    {
        return IndicatiaConfig.GENERAL.enableOldArmorRender.get();
    }

    public static boolean getBlockHitAnimation()
    {
        return IndicatiaConfig.GENERAL.enableBlockhitAnimation.get();
    }

    public static boolean getCustomPlayerList()
    {
        return IndicatiaConfig.GENERAL.enableCustomPlayerList.get();
    }

    public static boolean getConfirmToDisconnect()
    {
        return IndicatiaConfig.GENERAL.enableConfirmToDisconnect.get();
    }

    public static boolean getHypixelChatMode()
    {
        return IndicatiaConfig.GENERAL.enableHypixelChatMode.get();
    }

    public static boolean getHypixelDropdownShortcut()
    {
        return IndicatiaConfig.GENERAL.enableHypixelDropdownShortcutGame.get();
    }

    public static boolean getRenderBossHealthBar()
    {
        return IndicatiaConfig.GENERAL.enableBossHealthBarRender.get();
    }
}