//package stevekung.mods.indicatia.profile;
//
//import net.minecraftforge.common.config.Property;
//import stevekung.mods.indicatia.config.ConfigManager;
//import stevekung.mods.indicatia.profile.ProfileData.ProfileSettingData;
//
//public class ProfileConfigData
//{
//    public void load(ProfileSettingData data)
//    {
//        Property prop = ConfigManager.getConfig().get(ConfigManager.RENDER_SETTINGS, "Enable Ping", true);
//        this.set(prop, data.getObjects()[0]);
//        ConfigManager.enablePing = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.RENDER_SETTINGS, "Enable Server IP", true);
//        this.set(prop, data.getObjects()[1]);
//        ConfigManager.enableServerIP = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.RENDER_SETTINGS, "Enable FPS", true);
//        this.set(prop, data.getObjects()[2]);
//        ConfigManager.enableFPS = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.RENDER_SETTINGS, "Enable XYZ", true);
//        this.set(prop, data.getObjects()[3]);
//        ConfigManager.enableXYZ = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.RENDER_SETTINGS, "Enable Looking at Block", false);
//        this.set(prop, data.getObjects()[4]);
//        ConfigManager.enableLookingAtBlock = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.RENDER_SETTINGS, "Enable Direction", true);
//        this.set(prop, data.getObjects()[5]);
//        ConfigManager.enableDirection = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.RENDER_SETTINGS, "Enable Biome", true);
//        this.set(prop, data.getObjects()[6]);
//        ConfigManager.enableBiome = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.RENDER_SETTINGS, "Enable Armor Status", true);
//        this.set(prop, data.getObjects()[7]);
//        ConfigManager.enableArmorStatus = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.RENDER_SETTINGS, "Enable Potion Status", false);
//        this.set(prop, data.getObjects()[8]);
//        ConfigManager.enablePotionStatus = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.RENDER_SETTINGS, "Enable Keystroke", false);
//        this.set(prop, data.getObjects()[9]);
//        ConfigManager.enableKeystroke = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.RENDER_SETTINGS, "Enable CPS", false);
//        this.set(prop, data.getObjects()[10]);
//        ConfigManager.enableCPS = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.RENDER_SETTINGS, "Enable Held Item", false);
//        this.set(prop, data.getObjects()[11]);
//        ConfigManager.enableHeldItemInHand = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.RENDER_SETTINGS, "Armor Status", "PERCENT");
//        prop.set((String) data.getObjects()[12]);
//        ConfigManager.armorStatusMode = prop.getString();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.RENDER_SETTINGS, "Held Item Status", "NORMAL");
//        prop.set((String) data.getObjects()[13]);
//        ConfigManager.heldItemStatusMode = prop.getString();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.OFFSET_SETTINGS, "Armor Status Position", "LEFT");
//        prop.set((String) data.getObjects()[14]);
//        ConfigManager.armorStatusPosition = prop.getString();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.OFFSET_SETTINGS, "Potion Status Position", "LEFT");
//        prop.set((String) data.getObjects()[15]);
//        ConfigManager.potionStatusPosition = prop.getString();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.OFFSET_SETTINGS, "Keystroke Position", "RIGHT");
//        prop.set((String) data.getObjects()[16]);
//        ConfigManager.keystrokePosition = prop.getString();
//
//        ExtendedModSettings.ARMOR_STATUS_OFFSET = (int) data.getObjects()[17];
//        ExtendedModSettings.POTION_STATUS_OFFSET = (int) data.getObjects()[18];
//        ExtendedModSettings.KEYSTROKE_Y_OFFSET = (int) data.getObjects()[19];
//
//        prop = ConfigManager.getConfig().get(ConfigManager.TIME_INFO_SETTINGS, "Enable Current Time", true);
//        this.set(prop, data.getObjects()[20]);
//        ConfigManager.enableCurrentTime = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.TIME_INFO_SETTINGS, "Enable Game Time", true);
//        this.set(prop, data.getObjects()[21]);
//        ConfigManager.enableGameTime = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.TIME_INFO_SETTINGS, "Enable Moon Phase", false);
//        this.set(prop, data.getObjects()[22]);
//        ConfigManager.enableMoonPhase = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.TIME_INFO_SETTINGS, "Enable Weather Status", true);
//        this.set(prop, data.getObjects()[23]);
//        ConfigManager.enableWeatherStatus = prop.getBoolean();
//
//        prop = ConfigManager.getConfig().get(ConfigManager.TIME_INFO_SETTINGS, "Enable Slime Chunk Finder", false);
//        this.set(prop, data.getObjects()[24]);
//        ConfigManager.enableSlimeChunkFinder = prop.getBoolean();
//    }
//
//    public void set(Property prop, Object object)
//    {
//        prop.set(object.toString());
//    }
//}
