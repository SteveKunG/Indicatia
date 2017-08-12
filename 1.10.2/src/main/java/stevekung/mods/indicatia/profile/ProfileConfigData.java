package stevekung.mods.indicatia.profile;

import net.minecraftforge.common.config.Property;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.profile.ProfileData.ProfileSettingData;

public class ProfileConfigData
{
    public void load(ProfileSettingData data)
    {
        Property prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable FPS", true);
        this.set(prop, data.getObjects()[0]);
        ConfigManager.enableFPS = prop.getBoolean();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable XYZ", true);
        this.set(prop, data.getObjects()[1]);
        ConfigManager.enableXYZ = prop.getBoolean();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Biome", true);
        this.set(prop, data.getObjects()[2]);
        ConfigManager.enableBiome = prop.getBoolean();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Ping", true);
        this.set(prop, data.getObjects()[3]);
        ConfigManager.enablePing = prop.getBoolean();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Server IP", true);
        this.set(prop, data.getObjects()[4]);
        ConfigManager.enableServerIP = prop.getBoolean();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Render Equipped Item", true);
        this.set(prop, data.getObjects()[5]);
        ConfigManager.enableRenderEquippedItem = prop.getBoolean();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Potion Status HUD", false);
        this.set(prop, data.getObjects()[6]);
        ConfigManager.enablePotionStatusHUD = prop.getBoolean();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Keystroke", false);
        this.set(prop, data.getObjects()[7]);
        ConfigManager.enableKeystroke = prop.getBoolean();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable CPS", false);
        this.set(prop, data.getObjects()[8]);
        ConfigManager.enableCPS = prop.getBoolean();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable RCPS", false);
        this.set(prop, data.getObjects()[9]);
        ConfigManager.enableRCPS = prop.getBoolean();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Slime Chunk Finder", false);
        this.set(prop, data.getObjects()[10]);
        ConfigManager.enableSlimeChunkFinder = prop.getBoolean();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Current Real Time", true);
        this.set(prop, data.getObjects()[11]);
        ConfigManager.enableCurrentRealTime = prop.getBoolean();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Current Game Time", true);
        this.set(prop, data.getObjects()[12]);
        ConfigManager.enableCurrentGameTime = prop.getBoolean();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Game Weather", true);
        this.set(prop, data.getObjects()[13]);
        ConfigManager.enableGameWeather = prop.getBoolean();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Enable Moon Phase", false);
        this.set(prop, data.getObjects()[14]);
        ConfigManager.enableMoonPhase = prop.getBoolean();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Keystroke Position", "right");
        prop.set((String) data.getObjects()[15]);
        prop.setValidValues(new String[] { "right", "left" });
        ConfigManager.keystrokePosition = prop.getString();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Equipment Ordering", "default");
        prop.set((String) data.getObjects()[16]);
        prop.setValidValues(new String[] { "default", "reverse" });
        ConfigManager.equipmentOrdering = prop.getString();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Equipment Direction", "vertical");
        prop.set((String) data.getObjects()[17]);
        prop.setValidValues(new String[] { "vertical", "horizontal" });
        ConfigManager.equipmentDirection = prop.getString();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Equipment Status", "damage/max_damage");
        prop.set((String) data.getObjects()[18]);
        prop.setValidValues(new String[] { "damage/max_damage", "percent", "damage", "none" });
        ConfigManager.equipmentStatus = prop.getString();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Equipment Position", "left");
        prop.set((String) data.getObjects()[19]);
        prop.setValidValues(new String[] { "left", "right", "hotbar" });
        ConfigManager.equipmentPosition = prop.getString();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Potion Status HUD Style", "default");
        prop.set((String) data.getObjects()[20]);
        prop.setValidValues(new String[] { "default", "icon_and_time" });
        ConfigManager.potionStatusHUDStyle = prop.getString();

        prop = ConfigManager.getProperty(ConfigManager.RENDER_SETTINGS, "Potion Status HUD Position", "left");
        prop.set((String) data.getObjects()[21]);
        prop.setValidValues(new String[] { "left", "right", "hotbar_left", "hotbar_right" });
        ConfigManager.potionStatusHUDPosition = prop.getString();

        ExtendedConfig.ARMOR_STATUS_OFFSET = (int) data.getObjects()[22];
        ExtendedConfig.POTION_STATUS_OFFSET = (int) data.getObjects()[23];
        ExtendedConfig.KEYSTROKE_Y_OFFSET = (int) data.getObjects()[24];
        ExtendedConfig.CPS_X_OFFSET = (int) data.getObjects()[25];
        ExtendedConfig.CPS_Y_OFFSET =  (int) data.getObjects()[26];
        ExtendedConfig.TOP_DONATOR_FILE_PATH =  (String) data.getObjects()[27];
        ExtendedConfig.RECENT_DONATOR_FILE_PATH =  (String) data.getObjects()[28];
    }

    public void set(Property prop, Object object)
    {
        prop.set(object.toString());
    }
}