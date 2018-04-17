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

        @Config.Comment("This will check message from Title if equal to it. Split text by \",\" Example: Goodbye,You Died")
        @Config.Name(value = "End Game Title Message")
        public String endGameTitleMessage = "you lose,you win,game end,victory";

        @Config.Comment("This message will be printed to in-game chat when game is ending or finished. Disable by leave it blank.")
        @Config.Name(value = "End Game Message")
        public String endGameMessage = "";

        @Config.Name(value = "Confirm Disconnect Mode")
        public DisconnectMode confirmDisconnectMode = DisconnectMode.GUI;

        @Config.Name(value = "Enable Render Info")
        public boolean enableRenderInfo = true;

        @Config.Comment("Improve smooth hand action animation from 1.7.")
        @Config.Name(value = "Enable Blockhit Animation")
        public boolean enableBlockhitAnimation = false;

        @Config.Comment("When Eating/Blocking/Bowing/Drinking and use left click on the block, your hand will be swing just like in 1.7. (But not start break a block!)")
        @Config.Name(value = "Enable Additional Blockhit Animation")
        public boolean enableAdditionalBlockhitAnimation = false;

        @Config.Comment("Replacing vanilla fishing line and fishing rod model rendering.")
        @Config.Name(value = "Enable Fishing Rod Old Render")
        @Config.RequiresMcRestart
        public boolean enableFishingRodOldRender = false;

        @Config.Comment("This will using old armor hurt effect render. (red overlay)")
        @Config.Name(value = "Enable Old Armor Render")
        public boolean enableOldArmorRender = false;

        @Config.Name(value = "Enable Version Checker")
        public boolean enableVersionChecker = true;

        @Config.Name(value = "Enable Announce Message")
        public boolean enableAnnounceMessage = true;

        @Config.Name(value = "Enable AFK Message")
        public boolean enableAFKMessage = true;

        @Config.Comment("Disable chat background rendering.")
        @Config.Name(value = "Enable Fast Chat Render")
        public boolean enableFastChatRender = false;

        @Config.Comment("Show response time as number instead.")
        @Config.Name(value = "Enable Custom Player List")
        public boolean enableCustomPlayerList = false;

        @Config.Comment("Show response time as number instead and improved server info.")
        @Config.Name(value = "Enable Custom Server Selection GUI")
        public boolean enableCustomServerSelectionGui = false;

        @Config.Comment("This will display confirmation button if you want to disconnect from the server.")
        @Config.Name(value = "Enable Confirm Disconnect Button")
        public boolean enableConfirmDisconnectButton = false;

        @Config.Comment("Fix chat rendering if equipment icons is over Chat GUI.")
        @Config.Name(value = "Enable Fix Chat Depth Render")
        public boolean enableFixChatDepthRender = true;

        @Config.Comment("Show Vanilla Potion HUD in-game.")
        @Config.Name(value = "Enable Vanilla Potion HUD")
        public boolean enableVanillaPotionHUD = true;

        @Config.Name(value = "Enable Boss Health Bar")
        public boolean enableRenderBossHealthBar = true;

        @Config.Name(value = "Enable Boss Health Status")
        public boolean enableRenderBossHealthStatus = true;

        @Config.Name(value = "Enable Scoreboard Sidebar Render")
        public boolean enableRenderScoreboard = true;

        @Config.Comment("Allowed control by custom player movement (Toggle Sprint/Sneak, AFK Stuff).")
        @Config.Name(value = "Enable Custom Movement Handler")
        public boolean enableCustomMovementHandler = true;

        @Config.Comment("Allowed to use Custom Cape by upload your Custom Cape texture to any website.")
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