package com.stevekung.indicatia.config;

import com.stevekung.indicatia.core.Indicatia;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

public class IndicatiaConfig
{
    public static final ForgeConfigSpec.Builder GENERAL_BUILDER = new ForgeConfigSpec.Builder();
    public static final IndicatiaConfig.General GENERAL = new IndicatiaConfig.General(IndicatiaConfig.GENERAL_BUILDER);

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
            builder.comment("General settings").push("general");

            this.afkMessageTime = builder.translation("indicatia.configgui.afk_message_time").defineInRange("afkMessageTime", 5, 1, 60);

            this.enableRenderInfo = builder.translation("indicatia.configgui.enable_render_info").define("enableRenderInfo", true);

            this.enableBlockhitAnimation = builder.comment("Improved hand animation and restore animation when Eating/Blocking/Pulling/Drinking with left click from 1.7.").translation("indicatia.configgui.enable_blockhit_animation").define("enableBlockhitAnimation", false);

            this.enableOldArmorRender = builder.comment("This will using old armor hurt effect render. (red overlay)").translation("indicatia.configgui.enable_old_armor_render").define("enableOldArmorRender", false);

            this.enableVersionChecker = builder.translation("indicatia.configgui.enable_version_checker").define("enableVersionChecker", true);

            this.enableAFKMessage = builder.translation("indicatia.configgui.enable_afk_message").define("enableAFKMessage", true);

            this.enableCustomPlayerList = builder.comment("Show response time as number instead.").translation("indicatia.configgui.enable_custom_player_list").define("enableCustomPlayerList", false);

            this.multiplayerScreenEnhancement = builder.comment("Show response time as number instead and improved server info.").translation("indicatia.configgui.multiplayer_screen_enhancement").define("multiplayerScreenEnhancement", false);

            this.enableConfirmToDisconnect = builder.comment("This will display confirmation screen when try to disconnect.").translation("indicatia.configgui.enable_confirm_to_disconnect").define("enableConfirmToDisconnect", false);

            this.enableVanillaPotionHUD = builder.comment("Show Vanilla Potion HUD in-game.").translation("indicatia.configgui.enable_vanilla_potion_hud").define("enableVanillaPotionHUD", true);

            this.enableBossHealthBarRender = builder.translation("indicatia.configgui.enable_boss_health_bar_render").define("enableBossHealthBarRender", true);

            this.enableRenderBossHealthStatus = builder.translation("indicatia.configgui.enable_boss_health_status_render").define("enableRenderBossHealthStatus", true);

            this.enableSidebarScoreboardRender = builder.translation("indicatia.configgui.enable_sidebar_scoreboard_render").define("enableSidebarScoreboardRender", true);

            this.enableHypixelChatMode = builder.translation("indicatia.configgui.enable_hypixel_chat_mode").define("enableHypixelChatMode", true);

            this.enableHypixelDropdownShortcutGame = builder.translation("indicatia.configgui.enable_hypixel_dropdown_shortcut_game").define("enableHypixelDropdownShortcutGame", true);

            this.enableAlternateChatKey = builder.comment("This allowed to use Numpad Enter key to open chat.").translation("indicatia.configgui.enable_alternate_chat_key").define("enableAlternateChatKey", true);

            builder.pop();
        }
    }

    @SubscribeEvent
    public static void onLoad(ModConfig.Loading event)
    {
        Indicatia.LOGGER.info("Loaded config file {}", event.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(ModConfig.Reloading event)
    {
        Indicatia.LOGGER.info("Indicatia config just got changed on the file system");
    }
}