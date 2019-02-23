package stevekung.mods.indicatia.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import stevekung.mods.indicatia.utils.LoggerIN;

public class IndicatiaConfig
{
    public static final ForgeConfigSpec.Builder GENERAL_BUILDER = new ForgeConfigSpec.Builder();
    public static final IndicatiaConfig.General GENERAL = new IndicatiaConfig.General(IndicatiaConfig.GENERAL_BUILDER);

    public static class General
    {
        // General
        public final ForgeConfigSpec.IntValue afkMessageTime;
        public final ForgeConfigSpec.IntValue autoGGDelay;
        public final ForgeConfigSpec.ConfigValue<String> autoGGMessage;
        public final ForgeConfigSpec.ConfigValue<String> confirmDisconnectMode;
        public final ForgeConfigSpec.BooleanValue enableRenderInfo;
        public final ForgeConfigSpec.BooleanValue enableBlockhitAnimation;
        public final ForgeConfigSpec.BooleanValue enableAdditionalBlockhitAnimation;
        public final ForgeConfigSpec.BooleanValue enableOldFishingRodRender;
        public final ForgeConfigSpec.BooleanValue enableOldArmorRender;
        public final ForgeConfigSpec.BooleanValue enableVersionChecker;
        public final ForgeConfigSpec.BooleanValue enableAFKMessage;
        public final ForgeConfigSpec.BooleanValue enableCustomPlayerList;
        public final ForgeConfigSpec.BooleanValue enableCustomServerSelectionGui;
        public final ForgeConfigSpec.BooleanValue enableConfirmDisconnectButton;
        public final ForgeConfigSpec.BooleanValue enableFixChatDepthRender;
        public final ForgeConfigSpec.BooleanValue enableVanillaPotionHUD;
        public final ForgeConfigSpec.BooleanValue enableBossHealthBarRender;
        public final ForgeConfigSpec.BooleanValue enableRenderBossHealthStatus;
        public final ForgeConfigSpec.BooleanValue enableSidebarScoreboardRender;
        public final ForgeConfigSpec.BooleanValue enableCustomMovementHandler;
        public final ForgeConfigSpec.BooleanValue enableCustomCape;

        // Donation
        public final ForgeConfigSpec.ConfigValue<String> donatorMessagePosition;
        public final ForgeConfigSpec.IntValue readFileInterval;

        General(ForgeConfigSpec.Builder builder)
        {
            builder.comment("General settings")
            .push("general");

            this.afkMessageTime = builder
                    .comment("")
                    .translation("indicatia.configgui.afk_message_time")
                    .defineInRange("afkMessageTime", 5, 1, 60);

            this.autoGGDelay = builder
                    .comment("")
                    .translation("indicatia.configgui.auto_gg_delay")
                    .defineInRange("autoGGDelay", 20, 20, 100);

            this.autoGGMessage = builder
                    .comment("This message will be printed to in-game chat when game is ending or finished. Disable by leave it blank.")
                    .translation("indicatia.configgui.auto_gg_message")
                    .define("autoGGMessage", "");

            this.confirmDisconnectMode = builder
                    .comment("")
                    .translation("indicatia.configgui.confirm_disconnect_mode")
                    .define("confirmDisconnectMode", DisconnectMode.GUI.name());

            this.enableRenderInfo = builder
                    .comment("")
                    .translation("indicatia.configgui.enable_render_info")
                    .define("enableRenderInfo", true);

            this.enableBlockhitAnimation = builder
                    .comment("Improve smooth hand action animation from 1.7.")
                    .translation("indicatia.configgui.enable_blockhit_animation")
                    .define("enableBlockhitAnimation", false);

            this.enableAdditionalBlockhitAnimation = builder
                    .comment("When Eating/Blocking/Bowing/Drinking and use left click on the block, your hand will be swing just like in 1.7. (Not breaking block)")
                    .translation("indicatia.configgui.enable_additional_blockhit_animation")
                    .define("enableAdditionalBlockhitAnimation", false);

            this.enableOldFishingRodRender = builder
                    .comment("Replacing vanilla fishing line and fishing rod model rendering.")
                    .translation("indicatia.configgui.enable_old_fishing_rod_render")
                    .worldRestart() //TODO MC Restart
                    .define("enableOldFishingRodRender", false);

            this.enableOldArmorRender = builder
                    .comment("This will using old armor hurt effect render. (red overlay)")
                    .translation("indicatia.configgui.enable_old_armor_render")
                    .define("enableOldArmorRender", false);

            this.enableVersionChecker = builder
                    .comment("")
                    .translation("indicatia.configgui.enable_version_checker")
                    .define("enableVersionChecker", true);

            this.enableAFKMessage = builder
                    .comment("")
                    .translation("indicatia.configgui.enable_afk_message")
                    .define("enableAFKMessage", true);

            this.enableCustomPlayerList = builder
                    .comment("Show response time as number instead.")
                    .translation("indicatia.configgui.enable_custom_player_list")
                    .define("enableCustomPlayerList", false);

            this.enableCustomServerSelectionGui = builder
                    .comment("Show response time as number instead and improved server info.")
                    .translation("indicatia.configgui.enable_custom_server_selection_gui")
                    .define("enableCustomServerSelectionGui", false);

            this.enableConfirmDisconnectButton = builder
                    .comment("This will display confirmation button if you clicked disconnect button.")
                    .translation("indicatia.configgui.enable_confirm_disconnect_button")
                    .define("enableConfirmDisconnectButton", false);

            this.enableFixChatDepthRender = builder
                    .comment("Fix chat rendering if equipment icons is over Chat GUI.")
                    .translation("indicatia.configgui.enable_fix_chat_depth_render")
                    .define("enableFixChatDepthRender", true);

            this.enableVanillaPotionHUD = builder
                    .comment("Show Vanilla Potion HUD in-game.")
                    .translation("indicatia.configgui.enable_vanilla_potion_hud")
                    .define("enableVanillaPotionHUD", true);

            this.enableBossHealthBarRender = builder
                    .comment("")
                    .translation("indicatia.configgui.enable_boss_health_bar_render")
                    .define("enableBossHealthBarRender", true);

            this.enableRenderBossHealthStatus = builder
                    .comment("")
                    .translation("indicatia.configgui.enable_boss_health_status_render")
                    .define("enableRenderBossHealthStatus", true);

            this.enableSidebarScoreboardRender = builder
                    .comment("")
                    .translation("indicatia.configgui.enable_sidebar_scoreboard_render")
                    .define("enableSidebarScoreboardRender", true);

            this.enableCustomMovementHandler = builder
                    .comment("Allow controlled by custom player movement (Toggle Sprint/Sneak, AFK Stuff).")
                    .translation("indicatia.configgui.enable_custom_movement_handler")
                    .define("enableCustomMovementHandler", true);

            this.enableCustomCape = builder
                    .comment("Allow to use Custom Cape by upload your Custom Cape texture to any website.")
                    .translation("indicatia.configgui.enable_custom_cape")
                    .define("enableCustomCape", false);

            builder.pop();

            builder.comment("Donation settings")
            .push("donation");

            this.donatorMessagePosition = builder
                    .comment("")
                    .translation("indicatia.configgui.donator_message_position")
                    .define("donatorMessagePosition", DonatorMessagePos.RIGHT.name());

            this.readFileInterval = builder
                    .comment("")
                    .translation("indicatia.configgui.read_file_interval")
                    .defineInRange("readFileInterval", 200, 200, 1200);

            builder.pop();
        }
    }

    public enum DisconnectMode
    {
        GUI, CLICK
    }

    public enum DonatorMessagePos
    {
        LEFT, RIGHT
    }

    @SubscribeEvent
    public static void onLoad(ModConfig.Loading event)
    {
        LoggerIN.info("Loaded config file {}", event.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(ModConfig.ConfigReloading event)
    {
        LoggerIN.info("Indicatia config just got changed on the file system");
    }
}