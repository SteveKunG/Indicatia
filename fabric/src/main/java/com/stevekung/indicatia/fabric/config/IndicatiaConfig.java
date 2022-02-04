package com.stevekung.indicatia.fabric.config;

import com.stevekung.indicatia.core.Indicatia;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = Indicatia.MOD_ID)
public final class IndicatiaConfig implements ConfigData
{
    @Comment("This allowed to use Numpad Enter key to open the chat.")
    public boolean enableAlternateChatKey = true;
    @Comment("Display potion duration time on top right Potion HUD.")
    public boolean timeOnVanillaPotionHUD = true;
    @Comment("Enable confirmation screen on pressing disconnect button.")
    public boolean confirmationOnDisconnect = true;
    @Comment("Enable enchant effect rendering on skull.")
    public boolean enableEnchantedRenderingOnSkull = true;
    @Comment("Enable reload resources button on a pack selection screen.")
    public boolean reloadResourcesButton = true;
}