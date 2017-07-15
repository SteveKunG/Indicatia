package stevekung.mods.indicatia.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.gui.ForgeGuiFactory.ForgeConfigGui.AddModOverrideEntry;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.BooleanEntry;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.GuiConfigEntries.IConfigEntry;
import net.minecraftforge.fml.client.config.GuiConfigEntries.NumberSliderEntry;
import net.minecraftforge.fml.client.config.IConfigElement;
import stevekung.mods.indicatia.gui.ConfigColorEntry;
import stevekung.mods.indicatia.utils.InfoUtil;
import stevekung.mods.indicatia.utils.LangUtil;

public class ConfigManager
{
    private static Configuration config;
    public static final String MAIN_SETTINGS = "indicatia_main_settings";
    public static final String RENDER_SETTINGS = "indicatia_render_settings";
    public static final String COLOR_SETTINGS = "indicatia_color_settings";

    // Main Settings
    public static int afkMessageTime;
    public static boolean enableRenderInfo;
    public static boolean enableBlockhitAnimation;
    public static boolean enableFishingRodOldRender;
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
    public static boolean enableFPS;
    public static boolean enableXYZ;
    public static boolean enableBiome;
    public static boolean enablePing;
    public static boolean enableServerIP;
    public static boolean enableServerIPMCVersion;
    public static boolean enableRenderEquippedItem;
    public static boolean enableKeystroke;
    public static boolean enableKeystrokeLMBRMB;
    public static boolean enableKeystrokeSprintSneak;
    public static boolean enableKeystrokeBlocking;
    public static boolean enableCPS;
    public static boolean enableRCPS;
    public static String healthStatusMode;
    public static String keystrokePosition;
    public static String equipmentOrdering;
    public static String equipmentDirection;
    public static String equipmentStatus;

    // Custom Text Color Settings
    public static String customColorFPS;
    public static String customColorXYZ;
    public static String customColorBiome;
    public static String customColorServerIP;
    public static String customColorCPS;
    public static String customColorRCPS;

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

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Blockhit Animation", false);
        ConfigManager.enableBlockhitAnimation = prop.getBoolean();
        prop.setComment(LangUtil.translate("gui.config.indicatia.blockhit_animation"));
        propOrder.add(prop.getName());

        prop = ConfigManager.getProperty(ConfigManager.MAIN_SETTINGS, "Enable Fishing Rod Old Render", false);
        ConfigManager.enableFishingRodOldRender = prop.getBoolean();
        prop.setComment(LangUtil.translate("gui.config.indicatia.old_fish_render"));
        prop.setRequiresMcRestart(true);
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
        prop.setRequiresMcRestart(true);
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

        return propOrder;
    }

    private static List<String> addCustomColorSetting()
    {
        Property prop;
        List<String> propOrder = new ArrayList<>();

        prop = ConfigManager.getColorConfig("FPS Color", "white");
        ConfigManager.customColorFPS = prop.getString();
        propOrder.add(prop.getName());

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

        return propOrder;
    }

    private static Property getProperty(String category, String name, boolean defaultValue)
    {
        return ConfigManager.config.get(category, name, defaultValue);
    }

    private static Property getProperty(String category, String name, String defaultValue)
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
        //list.add(new ConfigElement(ConfigManager.config.getCategory(ConfigManager.COLOR_SETTINGS)));

        list.add(new DummyCategoryElement(ConfigManager.COLOR_SETTINGS + "55", "forge.configgui.ctgfhghggy.forgeClientConfig", ChunkLoaderEntry.class));
        return list;
    }

    public static Configuration getConfig()
    {
        return ConfigManager.config;
    }

    public static class ChunkLoaderEntry extends CategoryEntry
    {
        public ChunkLoaderEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
        {
            super(owningScreen, owningEntryList, prop);
        }

        @Override
        protected GuiScreen buildChildScreen()
        {
            List<IConfigElement> list = new ArrayList<IConfigElement>();

            list.add(new DummyCategoryElement(ConfigManager.COLOR_SETTINGS + "55", "forge.confisddsfggui.ctgy.forgeChunkLoadingModConfig", ModOverridesEntry.class));
            list.addAll(new ConfigElement(ConfigManager.config.getCategory(ConfigManager.COLOR_SETTINGS)).getChildElements());

            // This GuiConfig object specifies the configID of the object and as such will force-save when it is closed. The parent
            // GuiConfig object's propertyList will also be refreshed to reflect the changes.
            return new GuiConfig(this.owningScreen, list, this.owningScreen.modID, "chunkLoader", this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart, this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart, GuiConfig.getAbridgedConfigPath(ForgeChunkManager.getConfig().toString()), I18n.format("forge.configgui.ctgy.forgeChunkLoadingConfig"));
        }
    }

    public static class ModOverridesEntry extends CategoryEntry
    {
        public ModOverridesEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
        {
            super(owningScreen, owningEntryList, prop);
        }

        /**
         * This method is called in the constructor and is used to set the childScreen field.
         */
        @Override
        protected GuiScreen buildChildScreen()
        {
            List<IConfigElement> list = new ArrayList<IConfigElement>();

            list.add(new DummyCategoryElement("addForgeChunkLoadingModCfg", "forge.configgui.ctgy.forgeChunkLoadingAddModConfig",
                    AddModOverrideEntry.class));
            for (ConfigCategory cc : ForgeChunkManager.getModCategories())
            {
                list.add(new ConfigElement(cc));
            }

            return new GuiConfig(this.owningScreen, list, this.owningScreen.modID,
                    this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                    this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart, this.owningScreen.title,
                    I18n.format("forge.configgui.ctgy.forgeChunkLoadingModConfig"));
        }

        /**
         * By overriding the enabled() method and checking the value of the "enabled" entry this entry is enabled/disabled based on the value of
         * the other entry.
         */
        @Override
        public boolean enabled()
        {
            for (IConfigEntry entry : this.owningEntryList.listEntries)
            {
                if (entry.getName().equals("enabled") && entry instanceof BooleanEntry)
                {
                    return Boolean.valueOf(entry.getCurrentValue().toString());
                }
            }

            return true;
        }

        /**
         * Check to see if the child screen's entry list has changed.
         */
        @Override
        public boolean isChanged()
        {
            if (this.childScreen instanceof GuiConfig)
            {
                GuiConfig child = (GuiConfig) this.childScreen;
                return child.entryList.listEntries.size() != child.initEntries.size() || child.entryList.hasChangedEntry(true);
            }
            return false;
        }

        /**
         * Since adding a new entry to the child screen is what constitutes a change here, reset the child
         * screen listEntries to the saved list.
         */
        @Override
        public void undoChanges()
        {
            if (this.childScreen instanceof GuiConfig)
            {
                GuiConfig child = (GuiConfig) this.childScreen;
                for (IConfigEntry ice : child.entryList.listEntries)
                {
                    if (!child.initEntries.contains(ice) && ForgeChunkManager.getConfig().hasCategory(ice.getName()))
                    {
                        ForgeChunkManager.getConfig().removeCategory(ForgeChunkManager.getConfig().getCategory(ice.getName()));
                    }
                }

                child.entryList.listEntries = new ArrayList<IConfigEntry>(child.initEntries);
            }
        }
    }
}