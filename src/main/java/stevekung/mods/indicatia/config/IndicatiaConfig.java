package stevekung.mods.indicatia.config;

import net.minecraftforge.common.config.ForgeConfigSpec;

public class IndicatiaConfig
{
    public static final ForgeConfigSpec BUILDER = new ForgeConfigSpec.Builder()
            //General
            .comment("General settings")
            .push("general")

            .comment("temp")//TODO Comment
            .translation("indicatia.configgui.afk_message_time")
            .defineInRange("afkMessageTime", 5, 1, 60)

            .comment("temp")//TODO Comment
            .translation("indicatia.configgui.auto_gg_delay")
            .defineInRange("autoGGDelay", 20, 20, 100)

            .comment("This message will be printed to in-game chat when game is ending or finished. Disable by leave it blank.")
            .translation("indicatia.configgui.auto_gg_message")
            .define("autoGGMessage", "")

            .comment("temp")//TODO Comment
            .translation("indicatia.configgui.confirm_disconnect_mode")
            .define("confirmDisconnectMode", DisconnectMode.GUI.name())

            .comment("temp")//TODO Comment
            .translation("indicatia.configgui.enable_render_info")
            .define("enableRenderInfo", true)

            .comment("Improve smooth hand action animation from 1.7.")
            .translation("indicatia.configgui.enable_blockhit_animation")
            .define("enableBlockhitAnimation", false)

            .comment("When Eating/Blocking/Bowing/Drinking and use left click on the block, your hand will be swing just like in 1.7. (Not breaking block)")
            .translation("indicatia.configgui.enable_additional_blockhit_animation")
            .define("enableAdditionalBlockhitAnimation", false)

            .comment("Replacing vanilla fishing line and fishing rod model rendering.")
            .translation("indicatia.configgui.enable_old_fishing_rod_render")
            .define("enableOldFishingRodRender", false)
            .worldRestart() //TODO MC Restart

            .comment("This will using old armor hurt effect render. (red overlay)")
            .translation("indicatia.configgui.enable_old_armor_render")
            .define("enableOldArmorRender", false)

            .comment("temp")//TODO Comment
            .translation("indicatia.configgui.enable_version_checker")
            .define("enableVersionChecker", true)

            .comment("temp")//TODO Comment
            .translation("indicatia.configgui.enable_afk_message")
            .define("enableAFKMessage", true)

            .comment("Show response time as number instead.")
            .translation("indicatia.configgui.enable_custom_player_list")
            .define("enableCustomPlayerList", false)

            .comment("Show response time as number instead and improved server info.")
            .translation("indicatia.configgui.enable_custom_server_selection_gui")
            .define("enableCustomServerSelectionGui", false)

            .comment("This will display confirmation button if you clicked disconnect button.")
            .translation("indicatia.configgui.enable_confirm_disconnect_button")
            .define("enableConfirmDisconnectButton", false)

            .comment("Fix chat rendering if equipment icons is over Chat GUI.")
            .translation("indicatia.configgui.enable_fix_chat_depth_render")
            .define("enableFixChatDepthRender", true)

            .comment("Show Vanilla Potion HUD in-game.")
            .translation("indicatia.configgui.enable_vanilla_potion_hud")
            .define("enableVanillaPotionHUD", true)

            .comment("temp")//TODO Comment
            .translation("indicatia.configgui.enable_boss_health_bar_render")
            .define("enableBossHealthBarRender", true)

            .comment("temp")//TODO Comment
            .translation("indicatia.configgui.enable_boss_health_status_render")
            .define("enableRenderBossHealthStatus", true)

            .comment("temp")//TODO Comment
            .translation("indicatia.configgui.enable_sidebar_scoreboard_render")
            .define("enableSidebarScoreboardRender", true)

            .comment("Allow controlled by custom player movement (Toggle Sprint/Sneak, AFK Stuff).")
            .translation("indicatia.configgui.enable_custom_movement_handler")
            .define("enableCustomMovementHandler", true)

            .comment("Allow to use Custom Cape by upload your Custom Cape texture to any website.")
            .translation("indicatia.configgui.enable_custom_cape")
            .define("enableCustomCape", false)

            .pop()

            //Donation
            .comment("Donation settings")
            .push("donation")

            .comment("temp")//TODO Comment
            .translation("indicatia.configgui.donator_message_position")
            .define("donatorMessagePosition", DonatorMessagePos.RIGHT.name())

            .comment("temp")//TODO Comment
            .translation("indicatia.configgui.read_file_interval")
            .define("readFileInterval", 200)

            .pop()

            .build();

    public enum DisconnectMode
    {
        GUI, CLICK
    }

    public enum DonatorMessagePos
    {
        LEFT, RIGHT
    }
}