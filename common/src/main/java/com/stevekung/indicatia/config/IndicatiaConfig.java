package com.stevekung.indicatia.config;

import com.stevekung.indicatia.Indicatia;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Indicatia.MOD_ID)
public final class IndicatiaConfig implements ConfigData
{
    @ConfigEntry.Gui.Tooltip
    public boolean enableAlternateChatKey = true;

    @ConfigEntry.Gui.Tooltip
    public boolean displayPotionDurationOnTopRightPotionHUD = true;

    @ConfigEntry.Gui.Tooltip
    public boolean confirmationOnDisconnect = true;

    @ConfigEntry.Gui.Tooltip
    public boolean enableEnchantedRenderingOnSkulls = true;

    @ConfigEntry.Gui.Tooltip
    public boolean enableReloadResourcesButton = true;

    @ConfigEntry.Gui.Tooltip
    public boolean saveLastSearchInRecipeBook = true;
}