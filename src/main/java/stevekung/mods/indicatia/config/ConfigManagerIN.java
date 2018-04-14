package stevekung.mods.indicatia.config;

import net.minecraftforge.common.config.Config;
import stevekung.mods.indicatia.core.IndicatiaMod;

@Config(modid = IndicatiaMod.MOD_ID)
public class ConfigManagerIN
{
    @Config.LangKey(value = "indicatia_general")
    public static final General indicatia_general = new General();

    @Config.LangKey(value = "indicatia_donation")
    public static final Donation indicatia_donation = new Donation();

    // Main Settings
    public static class General
    {
        @Config.Name(value = "AFK Message Time (minutes)")
        @Config.RangeInt(min = 1, max = 60)
        public int afkMessageTime = 5;

        @Config.Name(value = "End Game Message Time (ticks)")
        @Config.RangeInt(min = 20, max = 100)
        public int endGameTitleTime = 20;

        @Config.Comment("gui.config.indicatia.end_game_title_message")
        @Config.Name(value = "End Game Title Message")
        public String endGameTitleMessage = "you lose,you win,game end,victory";

        @Config.Comment("gui.config.indicatia.end_game_message")
        @Config.Name(value = "End Game Message")
        public String endGameMessage = "";

        @Config.Name(value = "Confirm Disconnect Mode")
        public DisconnectMode confirmDisconnectMode = DisconnectMode.GUI;

        @Config.Name(value = "Enable Render Info")
        public boolean enableRenderInfo = true;

        @Config.Comment("gui.config.indicatia.blockhit_animation")
        @Config.Name(value = "Enable Blockhit Animation")
        public boolean enableBlockhitAnimation = false;

        @Config.Comment("gui.config.indicatia.additional_blockhit_animation")
        @Config.Name(value = "Enable Additional Blockhit Animation")
        public boolean enableAdditionalBlockhitAnimation = false;

        @Config.Comment("gui.config.indicatia.old_fish_render")
        @Config.Name(value = "Enable Fishing Rod Old Render")
        @Config.RequiresMcRestart
        public boolean enableFishingRodOldRender = false;

        @Config.Comment("gui.config.indicatia.old_armor_render")
        @Config.Name(value = "Enable Old Armor Render")
        public boolean enableOldArmorRender = false;

        @Config.Name(value = "Enable Version Checker")
        public boolean enableVersionChecker = true;

        @Config.Name(value = "Enable Announce Message")
        public boolean enableAnnounceMessage = true;

        @Config.Name(value = "Enable AFK Message")
        public boolean enableAFKMessage = true;

        @Config.Comment("gui.config.indicatia.fast_chat")
        @Config.Name(value = "Enable Fast Chat Render")
        public boolean enableFastChatRender = false;

        @Config.Comment("gui.config.indicatia.custom_player_list")
        @Config.Name(value = "Enable Custom Player List")
        public boolean enableCustomPlayerList = false;

        @Config.Comment("gui.config.indicatia.custom_server_selection")
        @Config.Name(value = "Enable Custom Server Selection GUI")
        public boolean enableCustomServerSelectionGui = false;

        @Config.Comment("gui.config.indicatia.confirm_disconnect_button")
        @Config.Name(value = "Enable Confirm Disconnect Button")
        public boolean enableConfirmDisconnectButton = false;

        @Config.Comment("gui.config.indicatia.chat_depth_render")
        @Config.Name(value = "Enable Fix Chat Depth Render")
        public boolean enableFixChatDepthRender = true;

        @Config.Comment("gui.config.indicatia.potion_hud")
        @Config.Name(value = "Enable Ingame Potion HUD")
        public boolean enableIngamePotionHUD = true;

        @Config.Name(value = "Enable Boss Health Bar")
        public boolean enableRenderBossHealthBar = true;

        @Config.Name(value = "Enable Boss Health Status")
        public boolean enableRenderBossHealthStatus = true;

        @Config.Name(value = "Enable Scoreboard Sidebar Render")
        public boolean enableRenderScoreboard = true;

        @Config.Comment("gui.config.indicatia.custom_movement_handler")
        @Config.Name(value = "Enable Custom Movement Handler")
        public boolean enableCustomMovementHandler = true;

        @Config.Comment("gui.config.indicatia.custom_cape")
        @Config.Name(value = "Enable Custom Cape")
        public boolean enableCustomCape = false;

        public static enum DisconnectMode
        {
            GUI, CLICK
        }
    }

    // Donation Settings
    public static class Donation
    {
        @Config.Name(value = "Donator Message Position")
        public DonatorMessagePos donatorMessagePosition = DonatorMessagePos.RIGHT;

        @Config.Name(value = "Read File Interval")
        public int readFileInterval = 200;

        public static enum DonatorMessagePos
        {
            LEFT, RIGHT
        }
    }
}