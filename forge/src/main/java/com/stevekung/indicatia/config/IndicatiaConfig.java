package com.stevekung.indicatia.config;

import org.apache.commons.lang3.tuple.Pair;
import com.stevekung.indicatia.core.Indicatia;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class IndicatiaConfig
{
    public static final ForgeConfigSpec GENERAL_SPEC;
    public static final General GENERAL;

    static
    {
        Pair<General, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(General::new);
        GENERAL_SPEC = specPair.getRight();
        GENERAL = specPair.getLeft();
    }

    public static class General
    {
        // General
        public final ForgeConfigSpec.IntValue afkMessageTime;
        public final ForgeConfigSpec.BooleanValue enableRenderInfo;
        public final ForgeConfigSpec.BooleanValue enableBlockhitAnimation;
        public final ForgeConfigSpec.BooleanValue enableOldArmorRender;
        public final ForgeConfigSpec.BooleanValue enableVersionChecker;
        public final ForgeConfigSpec.BooleanValue enableAFKMessage;
        public final ForgeConfigSpec.BooleanValue enableCustomPlayerList;
        public final ForgeConfigSpec.BooleanValue multiplayerScreenEnhancement;
        public final ForgeConfigSpec.BooleanValue enableConfirmToDisconnect;
        public final ForgeConfigSpec.BooleanValue enableVanillaPotionHUD;
        public final ForgeConfigSpec.BooleanValue enableBossHealthBarRender;
        public final ForgeConfigSpec.BooleanValue enableRenderBossHealthStatus;
        public final ForgeConfigSpec.BooleanValue enableSidebarScoreboardRender;
        public final ForgeConfigSpec.BooleanValue enableHypixelChatMode;
        public final ForgeConfigSpec.BooleanValue enableHypixelDropdownShortcutGame;
        public final ForgeConfigSpec.BooleanValue enableAlternateChatKey;

        General(ForgeConfigSpec.Builder builder)
        {
            builder.comment("General settings");
            builder.push("general");

            this.afkMessageTime = builder.translation("text.autoconfig.indicatia.option.general.afkMessageTime").defineInRange("afkMessageTime", 5, 1, 60);

            this.enableRenderInfo = builder.translation("text.autoconfig.indicatia.option.general.enableRenderInfo").define("enableRenderInfo", true);

            this.enableBlockhitAnimation = builder.comment("Improved hand animation and restore animation when Eating/Blocking/Pulling/Drinking with left click from 1.7.").translation("text.autoconfig.indicatia.option.general.enableBlockhitAnimation").define("enableBlockhitAnimation", false);

            this.enableOldArmorRender = builder.comment("This will using old armor hurt effect render. (red overlay)").translation("text.autoconfig.indicatia.option.general.enableOldArmorRender").define("enableOldArmorRender", false);

            this.enableVersionChecker = builder.translation("text.autoconfig.indicatia.option.general.enableVersionChecker").define("enableVersionChecker", true);

            this.enableAFKMessage = builder.translation("text.autoconfig.indicatia.option.general.enableAFKMessage").define("enableAFKMessage", true);

            this.enableCustomPlayerList = builder.comment("Show response time as number instead.").translation("text.autoconfig.indicatia.option.general.enableCustomPlayerList").define("enableCustomPlayerList", false);

            this.multiplayerScreenEnhancement = builder.comment("Show response time as number instead and improved server info.").translation("text.autoconfig.indicatia.option.general.multiplayerScreenEnhancement").define("multiplayerScreenEnhancement", false);

            this.enableConfirmToDisconnect = builder.comment("This will display confirmation screen when try to disconnect.").translation("text.autoconfig.indicatia.option.general.enableConfirmToDisconnect").define("enableConfirmToDisconnect", false);

            this.enableVanillaPotionHUD = builder.comment("Show Vanilla Potion HUD in-game.").translation("text.autoconfig.indicatia.option.general.enableVanillaPotionHUD").define("enableVanillaPotionHUD", true);

            this.enableBossHealthBarRender = builder.translation("text.autoconfig.indicatia.option.general.enableBossHealthBarRender").define("enableBossHealthBarRender", true);

            this.enableRenderBossHealthStatus = builder.translation("text.autoconfig.indicatia.option.general.enableRenderBossHealthStatus").define("enableRenderBossHealthStatus", true);

            this.enableSidebarScoreboardRender = builder.translation("text.autoconfig.indicatia.option.general.enableSidebarScoreboardRender").define("enableSidebarScoreboardRender", true);

            this.enableHypixelChatMode = builder.translation("text.autoconfig.indicatia.option.general.enableHypixelChatMode").define("enableHypixelChatMode", true);

            this.enableHypixelDropdownShortcutGame = builder.translation("text.autoconfig.indicatia.option.general.enableHypixelDropdownShortcutGame").define("enableHypixelDropdownShortcutGame", true);

            this.enableAlternateChatKey = builder.comment("This allowed to use Numpad Enter key to open chat.").translation("text.autoconfig.indicatia.option.general.enableAlternateChatKey").define("enableAlternateChatKey", true);

            builder.pop();
        }
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event)
    {
        Indicatia.LOGGER.info("Loaded config file {}", event.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(ModConfigEvent.Reloading event)
    {
        Indicatia.LOGGER.info("Indicatia config just got changed on the file system");
    }
}