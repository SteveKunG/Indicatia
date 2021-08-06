package com.stevekung.indicatia.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "indicatia")
@Config.Gui.Background("minecraft:textures/block/end_stone.png")
public final class IndicatiaConfig implements ConfigData
{
    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public GeneralCategory general;

    public IndicatiaConfig()
    {
        this.general = new GeneralCategory();
    }

    public static class GeneralCategory
    {
        @ConfigEntry.BoundedDiscrete(min = 5, max = 60)
        public int afkMessageTime = 5;
        public boolean enableRenderInfo = true;
        @Comment("Improved hand animation and restore animation when Eating/Blocking/Pulling/Drinking with left click from 1.7.\n" + "(default value: false)")
        public boolean enableBlockhitAnimation = false;
        @Comment("This will using old armor hurt effect render. (red overlay)\n" + "(default value: false)")
        public boolean enableOldArmorRender = false;
        public boolean enableAFKMessage = true;
        @Comment("Show response time as number instead.\n" + "(default value: true)")
        public boolean enableCustomPlayerList = true;
        @Comment("Show response time as number instead and improved server info.\n" + "(default value: true)")
        public boolean multiplayerScreenEnhancement = true;
        @Comment("This will display confirmation screen when try to disconnect.\n" + "(default value: true)")
        public boolean enableConfirmToDisconnect = true;
        @Comment("Show Vanilla Potion HUD in-game.\n" + "(default value: true)")
        public boolean enableVanillaPotionHUD = true;
        public boolean enableBossHealthBarRender = true;
        public boolean enableRenderBossHealthStatus = true;
        public boolean enableHypixelChatMode = true;
        public boolean enableHypixelDropdownShortcutGame = true;
        @Comment("This allowed to use Numpad Enter key to open chat.\n" + "(default value: true)")
        public boolean enableAlternateChatKey = true;
    }
}