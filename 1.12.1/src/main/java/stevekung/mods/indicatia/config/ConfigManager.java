package stevekung.mods.indicatia.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfigEntries.NumberSliderEntry;
import net.minecraftforge.fml.client.config.IConfigElement;
import stevekung.mods.indicatia.gui.ConfigColorEntry;
import stevekung.mods.indicatia.util.InfoUtil;
import stevekung.mods.indicatia.util.LangUtil;

public class ConfigManager
{
    private static Configuration config;
    public static final String MAIN_SETTINGS = "indicatia_main_settings";
    public static final String RENDER_SETTINGS = "indicatia_render_settings";
    public static final String COLOR_SETTINGS = "indicatia_color_settings";
    public static final String DONATION_SETTINGS = "indicatia_donation_settings";

    // Main Settings
    public static int afkMessageTime;
    public static int endGameTitleTime;
    public static String endGameTitleMessage;
    public static String endGameMessage;
    public static boolean enableRenderInfo;
    public static boolean enableBlockhitAnimation;
    public static boolean enableFishingRodOldRender;
    public static boolean enableOldArmorRender;
    public static boolean enableVersionChecker;
    public static boolean enableAnnounceMessage;
    public static boolean enableAFKMessage;
    public static boolean enableFastChatRender;
    public static boolean enableCustomPlayerList;
    public static boolean enableChatDepthRender;
    public static boolean enableIngamePotionHUD;
    public static boolean enableRenderBossHealthBar;
    public static boolean enableRenderBossHealthStatus;
    public static boolean enableRenderScoreboard;
    public static boolean enableSmoothEyeHeight;
    public static boolean enableCustomMovementHandler;
    public static boolean enableCustomCape;

    // Render Settings
    public static boolean swapRenderInfoToRight;
    public static boolean enableFPS;
    public static boolean enableXYZ;
    public static boolean enableBiome;
    public static boolean enablePing;
    public static boolean enableServerIP;
    public static boolean enableServerIPMCVersion;
    public static boolean enableRenderEquippedItem;
    public static boolean enablePotionStatusHUD;
    public static boolean enableKeystroke;
    public static boolean enableKeystrokeLMBRMB;
    public static boolean enableKeystrokeSprintSneak;
    public static boolean enableKeystrokeBlocking;
    public static boolean enableCPS;
    public static boolean enableRCPS;
    public static boolean enableSlimeChunkFinder;
    public static boolean enableCurrentRealTime;
    public static boolean enableCurrentGameTime;
    public static boolean enableGameWeather;
    public static boolean enableMoonPhase;
    public static boolean enablePotionHUDIcon;
    public static boolean alternatePotionHUDTextColor;
    public static boolean enableServerTPS;
    public static String healthStatusMode;
    public static String keystrokePosition;
    public static String equipmentOrdering;
    public static String equipmentDirection;
    public static String equipmentStatus;
    public static String equipmentPosition;
    public static String potionStatusHUDStyle;
    public static String potionStatusHUDPosition;

    // Custom Text Color Settings
    public static String customColorXYZ;
    public static String customColorBiome;
    public static String customColorServerIP;
    public static String customColorCPS;
    public static String customColorRCPS;
    public static String customColorTopDonateName;
    public static String customColorRecentDonateName;
    public static String customColorTopDonateCount;
    public static String customColorRecentDonateCount;

    // Donation Settings
    public static String donatorMessagePosition;
    public static int readFileInterval;

    public static void init(File file)
    {
        ConfigManager.config = new Configuration(file);
        ConfigManager.syncConfig(true);
    }

    public static void syncConfig(boolean load)
    {
        if (!ConfigManager.config.isChild)
        {
            if (load)
            {
                ConfigManager.config.load();
            }
        }

        ConfigManager.config.setCategoryPropertyOrder(ConfigManager.MAIN_SETTINGS, ConfigManager.addMainSetting());
        ConfigManager.config.setCategoryPropertyOrder(ConfigManager.RENDER_SETTINGS, ConfigManager.addRenderSetting());
        ConfigManager.config.setCategoryPropertyOrder(ConfigManager.COLOR_SETTINGS, ConfigManager.addCustomColorSetting());
        ConfigManager.config.setCategoryPropertyOrder(ConfigManager.DONATION_SETTINGS, ConfigManager.addDonationSetting());

        if (ConfigManager.config.hasChanged())
        {
            ConfigManager.config.save();
        }
    }

    private static List<String> addMainSetting()
    {
        Property prop;
        List<String> propOrder = new ArrayList<>();

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Render Info", true);
        ConfigManager.enableRenderInfo = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "AFK Message Time (minute)", 5);
        prop.setMinValue(1).setMaxValue(60).setConfigEntryClass(NumberSliderEntry.class);
        ConfigManager.afkMessageTime = prop.getInt();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "End Game Message Time (game tick)", 20);
        prop.setMinValue(20).setMaxValue(100).setConfigEntryClass(NumberSliderEntry.class);
        ConfigManager.endGameTitleTime = prop.getInt();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "End Game Title Message", "you lose,you win,game end,victory!");
        ConfigManager.endGameTitleMessage = prop.getString().toLowerCase();
        prop.setComment(LangUtil.translate("gui.config.indicatia.end_game_title_message"));
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "End Game Message", "");
        ConfigManager.endGameMessage = prop.getString();
        prop.setComment(LangUtil.translate("gui.config.indicatia.end_game_message"));
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Blockhit Animation", false);
        ConfigManager.enableBlockhitAnimation = prop.getBoolean();
        prop.setComment(LangUtil.translate("gui.config.indicatia.blockhit_animation"));
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Fishing Rod Old Render", false);
        ConfigManager.enableFishingRodOldRender = prop.getBoolean();
        prop.setComment(LangUtil.translate("gui.config.indicatia.old_fish_render"));
        prop.setRequiresMcRestart(true);
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Old Armor Render", false);
        ConfigManager.enableOldArmorRender = prop.getBoolean();
        prop.setComment(LangUtil.translate("gui.config.indicatia.old_armor_render"));
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Fast Chat Render", false);
        ConfigManager.enableFastChatRender = prop.getBoolean();
        prop.setComment(LangUtil.translate("gui.config.indicatia.fast_chat"));
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Custom Player List", false);
        ConfigManager.enableCustomPlayerList = prop.getBoolean();
        prop.setComment(LangUtil.translate("gui.config.indicatia.custom_player_list"));
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Chat Depth Render", true);
        ConfigManager.enableChatDepthRender = prop.getBoolean();
        prop.setComment(LangUtil.translate("gui.config.indicatia.chat_depth_render"));
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Ingame Potion HUD", true);
        ConfigManager.enableIngamePotionHUD = prop.getBoolean();
        prop.setComment(LangUtil.translate("gui.config.indicatia.potion_hud"));
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Boss Health Bar", true);
        ConfigManager.enableRenderBossHealthBar = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Boss Health Status", true);
        ConfigManager.enableRenderBossHealthStatus = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Scoreboard Sidebar Render", true);
        ConfigManager.enableRenderScoreboard = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable AFK Message", true);
        ConfigManager.enableAFKMessage = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Smooth Eye Height", false);
        ConfigManager.enableSmoothEyeHeight = prop.getBoolean();
        prop.setComment(LangUtil.translate("gui.config.indicatia.smooth_eye_height"));
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Custom Movement Handler", true);
        ConfigManager.enableCustomMovementHandler = prop.getBoolean();
        prop.setComment(LangUtil.translate("gui.config.indicatia.custom_movement_handler"));
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Custom Cape", false);
        ConfigManager.enableCustomCape = prop.getBoolean();
        prop.setComment(LangUtil.translate("gui.config.indicatia.custom_cape"));
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Version Checker", true);
        ConfigManager.enableVersionChecker = prop.getBoolean();
        prop.setRequiresMcRestart(true);
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Announce Message", true);
        ConfigManager.enableAnnounceMessage = prop.getBoolean();
        propOrder.add(prop.getName());

        return propOrder;
    }

    private static List<String> addRenderSetting()
    {
        Property prop;
        List<String> propOrder = new ArrayList<>();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Swap Render Info to Right", false);
        ConfigManager.swapRenderInfoToRight = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Health Status Mode", "disable");
        prop.setValidValues(new String[] { "disable", "always", "pointed" });
        ConfigManager.healthStatusMode = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Keystroke Position", "right");
        prop.setValidValues(new String[] { "right", "left" });
        ConfigManager.keystrokePosition = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Equipment Ordering", "default");
        prop.setValidValues(new String[] { "default", "reverse" });
        ConfigManager.equipmentOrdering = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Equipment Direction", "vertical");
        prop.setValidValues(new String[] { "vertical", "horizontal" });
        ConfigManager.equipmentDirection = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Equipment Status", "damage/max_damage");
        prop.setValidValues(new String[] { "damage/max_damage", "percent", "damage", "none" });
        ConfigManager.equipmentStatus = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Equipment Position", "left");
        prop.setValidValues(new String[] { "left", "right", "hotbar" });
        ConfigManager.equipmentPosition = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Potion Status HUD Style", "default");
        prop.setValidValues(new String[] { "default", "icon_and_time" });
        ConfigManager.potionStatusHUDStyle = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Potion Status HUD Position", "left");
        prop.setValidValues(new String[] { "left", "right", "hotbar_left", "hotbar_right" });
        ConfigManager.potionStatusHUDPosition = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable FPS", true);
        ConfigManager.enableFPS = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable XYZ", true);
        ConfigManager.enableXYZ = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Biome", true);
        ConfigManager.enableBiome = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Ping", true);
        ConfigManager.enablePing = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Server IP", true);
        ConfigManager.enableServerIP = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Server IP with Minecraft Version", false);
        ConfigManager.enableServerIPMCVersion = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Render Equipped Item", true);
        ConfigManager.enableRenderEquippedItem = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Potion Status HUD", false);
        ConfigManager.enablePotionStatusHUD = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Keystroke", false);
        ConfigManager.enableKeystroke = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable LMB/RMB on Keystroke", false);
        ConfigManager.enableKeystrokeLMBRMB = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Sprint/Sneak on Keystroke", false);
        ConfigManager.enableKeystrokeSprintSneak = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Blocking on Keystroke", false);
        ConfigManager.enableKeystrokeBlocking = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable CPS", false);
        ConfigManager.enableCPS = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable RCPS", false);
        ConfigManager.enableRCPS = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Slime Chunk Finder", false);
        ConfigManager.enableSlimeChunkFinder = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Current Real Time", true);
        ConfigManager.enableCurrentRealTime = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Current Game Time", true);
        ConfigManager.enableCurrentGameTime = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Game Weather", true);
        ConfigManager.enableGameWeather = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Moon Phase", false);
        ConfigManager.enableMoonPhase = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Potion HUD Icon", false);
        ConfigManager.enablePotionHUDIcon = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Alternate Potion HUD Text Color", false);
        ConfigManager.alternatePotionHUDTextColor = prop.getBoolean();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Server TPS", false);
        ConfigManager.enableServerTPS = prop.getBoolean();
        prop.setComment(LangUtil.translate("gui.config.indicatia.server_tps"));
        propOrder.add(prop.getName());

        return propOrder;
    }

    private static List<String> addCustomColorSetting()
    {
        Property prop;
        List<String> propOrder = new ArrayList<>();

        prop = ConfigManager.getColorConfig("XYZ Color", "white");
        ConfigManager.customColorXYZ = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getColorConfig("Biome Color", "white");
        ConfigManager.customColorBiome = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getColorConfig("Server IP Color", "white");
        ConfigManager.customColorServerIP = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getColorConfig("CPS Color", "white");
        ConfigManager.customColorCPS = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getColorConfig("RCPS Color", "white");
        ConfigManager.customColorRCPS = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getColorConfig("Top Donate Name Color", "white");
        ConfigManager.customColorTopDonateName = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getColorConfig("Recent Donate Name Color", "white");
        ConfigManager.customColorRecentDonateName = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getColorConfig("Top Donate Count Color", "white");
        ConfigManager.customColorTopDonateCount = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getColorConfig("Recent Donate Count Color", "white");
        ConfigManager.customColorRecentDonateCount = prop.getString();
        propOrder.add(prop.getName());

        return propOrder;
    }

    private static List<String> addDonationSetting()
    {
        Property prop;
        List<String> propOrder = new ArrayList<>();

        prop = ConfigManager.getProperty(ConfigManager.DONATION_SETTINGS, "Donator Message Position", "right");
        prop.setValidValues(new String[] { "left", "right" });
        ConfigManager.donatorMessagePosition = prop.getString();
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.DONATION_SETTINGS, "Read File Interval", 200);
        prop.setMinValue(200).setMaxValue(1200).setConfigEntryClass(NumberSliderEntry.class);
        ConfigManager.readFileInterval = prop.getInt();
        propOrder.add(prop.getName());

        return propOrder;
    }

    public static Property getProperty(String category, String name, boolean defaultValue)
    {
        return ConfigManager.config.get(category, name, defaultValue);
    }

    public static Property getProperty(String category, String name, String defaultValue)
    {
        return ConfigManager.config.get(category, name, defaultValue);
    }

    private static Property getProperty(String category, String name, int defaultValue)
    {
        return ConfigManager.config.get(category, name, defaultValue);
    }

    private static Property getColorConfig(String name, String defaultValue)
    {
        Property prop = ConfigManager.getProperty(ConfigManager.COLOR_SETTINGS, name, defaultValue);
        prop.setConfigEntryClass(ConfigColorEntry.class);
        prop.setValidValues(InfoUtil.INSTANCE.getJsonColor());
        return prop;
    }

    public static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<>();
        list.add(new ConfigElement(ConfigManager.config.getCategory(ConfigManager.MAIN_SETTINGS)));
        list.add(new ConfigElement(ConfigManager.config.getCategory(ConfigManager.RENDER_SETTINGS)));
        list.add(new ConfigElement(ConfigManager.config.getCategory(ConfigManager.COLOR_SETTINGS)));
        list.add(new ConfigElement(ConfigManager.config.getCategory(ConfigManager.DONATION_SETTINGS)));
        return list;
    }

    public static Configuration getConfig()
    {
        return ConfigManager.config;
    }
}