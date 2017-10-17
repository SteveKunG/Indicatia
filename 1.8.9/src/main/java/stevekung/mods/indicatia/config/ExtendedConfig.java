package stevekung.mods.indicatia.config;

import java.io.File;
import java.util.UUID;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.util.AutoLogin;
import stevekung.mods.indicatia.util.AutoLogin.AutoLoginData;
import stevekung.mods.indicatia.util.HideNameData;
import stevekung.mods.indicatia.util.ModLogger;

public class ExtendedConfig
{
    public static final AutoLogin loginData = new AutoLogin();
    private static final File FILE = new File(IndicatiaMod.MC.mcDataDir, "indicatia_data.dat");

    public static boolean TOGGLE_SPRINT = false;
    public static boolean TOGGLE_SNEAK = false;
    public static boolean AUTO_SWIM = false;
    public static boolean SHOW_CAPE = true;

    public static int KEYSTROKE_Y_OFFSET = 0;
    public static int ARMOR_STATUS_OFFSET = 0;
    public static int POTION_STATUS_OFFSET = 0;
    public static int CPS_X_OFFSET = 3;
    public static int CPS_Y_OFFSET = 2;
    public static int MAX_POTION_DISPLAY = 2;
    public static int POTION_LENGTH_Y_OFFSET = 23;
    public static int POTION_LENGTH_Y_OFFSET_OVERLAP = 45;
    public static int PREV_SELECT_DROPDOWN;
    public static float CPS_OPACITY = 0.5F;
    public static long SLIME_CHUNK_SEED = 0L;

    public static int KEYSTROKE_BLOCK_RED = 255;
    public static int KEYSTROKE_BLOCK_GREEN = 255;
    public static int KEYSTROKE_BLOCK_BLUE = 255;
    public static int KEYSTROKE_CPS_RED = 255;
    public static int KEYSTROKE_CPS_GREEN = 255;
    public static int KEYSTROKE_CPS_BLUE = 255;
    public static int KEYSTROKE_WASD_RED = 255;
    public static int KEYSTROKE_WASD_GREEN = 255;
    public static int KEYSTROKE_WASD_BLUE = 255;
    public static int KEYSTROKE_LMBRMB_RED = 255;
    public static int KEYSTROKE_LMBRMB_GREEN = 255;
    public static int KEYSTROKE_LMBRMB_BLUE = 255;
    public static int KEYSTROKE_SPRINT_RED = 255;
    public static int KEYSTROKE_SPRINT_GREEN = 255;
    public static int KEYSTROKE_SPRINT_BLUE = 255;
    public static int KEYSTROKE_SNEAK_RED = 255;
    public static int KEYSTROKE_SNEAK_GREEN = 255;
    public static int KEYSTROKE_SNEAK_BLUE = 255;

    public static boolean KEYSTROKE_BLOCK_RAINBOW = false;
    public static boolean KEYSTROKE_CPS_RAINBOW = false;
    public static boolean KEYSTROKE_WASD_RAINBOW = false;
    public static boolean KEYSTROKE_LMBRMB_RAINBOW = false;
    public static boolean KEYSTROKE_SPRINT_RAINBOW = false;
    public static boolean KEYSTROKE_SNEAK_RAINBOW = false;

    public static String CPS_POSITION = "left";
    public static String HYPIXEL_NICK_NAME = "";
    public static String TOP_DONATOR_FILE_PATH = "";
    public static String RECENT_DONATOR_FILE_PATH = "";
    public static String TOP_DONATOR_TEXT = "";
    public static String RECENT_DONATOR_TEXT = "";

    public static int FPS_COLOR_R = 255;
    public static int FPS_COLOR_G = 255;
    public static int FPS_COLOR_B = 255;
    public static int FPS_M40_COLOR_R = 85;
    public static int FPS_M40_COLOR_G = 255;
    public static int FPS_M40_COLOR_B = 85;
    public static int FPS_26_40_COLOR_R = 255;
    public static int FPS_26_40_COLOR_G = 255;
    public static int FPS_26_40_COLOR_B = 85;
    public static int FPS_L25_COLOR_R = 255;
    public static int FPS_L25_COLOR_G = 85;
    public static int FPS_L25_COLOR_B = 85;
    public static int XYZ_COLOR_R = 255;
    public static int XYZ_COLOR_G = 255;
    public static int XYZ_COLOR_B = 255;
    public static int XYZ_VALUE_COLOR_R = 255;
    public static int XYZ_VALUE_COLOR_G = 255;
    public static int XYZ_VALUE_COLOR_B = 255;
    public static int BIOME_COLOR_R = 255;
    public static int BIOME_COLOR_G = 255;
    public static int BIOME_COLOR_B = 255;
    public static int BIOME_VALUE_COLOR_R = 255;
    public static int BIOME_VALUE_COLOR_G = 255;
    public static int BIOME_VALUE_COLOR_B = 255;
    public static int CPS_COLOR_R = 255;
    public static int CPS_COLOR_G = 255;
    public static int CPS_COLOR_B = 255;
    public static int CPS_VALUE_COLOR_R = 255;
    public static int CPS_VALUE_COLOR_G = 255;
    public static int CPS_VALUE_COLOR_B = 255;
    public static int RCPS_COLOR_R = 255;
    public static int RCPS_COLOR_G = 255;
    public static int RCPS_COLOR_B = 255;
    public static int RCPS_VALUE_COLOR_R = 255;
    public static int RCPS_VALUE_COLOR_G = 255;
    public static int RCPS_VALUE_COLOR_B = 255;
    public static int TOP_DONATE_NAME_COLOR_R = 255;
    public static int TOP_DONATE_NAME_COLOR_G = 255;
    public static int TOP_DONATE_NAME_COLOR_B = 255;
    public static int RECENT_DONATE_NAME_COLOR_R = 255;
    public static int RECENT_DONATE_NAME_COLOR_G = 255;
    public static int RECENT_DONATE_NAME_COLOR_B = 255;
    public static int TOP_DONATE_COUNT_COLOR_R = 255;
    public static int TOP_DONATE_COUNT_COLOR_G = 255;
    public static int TOP_DONATE_COUNT_COLOR_B = 255;
    public static int RECENT_DONATE_COUNT_COLOR_R = 255;
    public static int RECENT_DONATE_COUNT_COLOR_G = 255;
    public static int RECENT_DONATE_COUNT_COLOR_B = 255;
    public static int PING_COLOR_R = 255;
    public static int PING_COLOR_G = 255;
    public static int PING_COLOR_B = 255;
    public static int PING_L200_COLOR_R = 85;
    public static int PING_L200_COLOR_G = 255;
    public static int PING_L200_COLOR_B = 85;
    public static int PING_200_300_COLOR_R = 255;
    public static int PING_200_300_COLOR_G = 255;
    public static int PING_200_300_COLOR_B = 85;
    public static int PING_300_500_COLOR_R = 255;
    public static int PING_300_500_COLOR_G = 85;
    public static int PING_300_500_COLOR_B = 85;
    public static int PING_M500_COLOR_R = 170;
    public static int PING_M500_COLOR_G = 0;
    public static int PING_M500_COLOR_B = 0;
    public static int IP_COLOR_R = 255;
    public static int IP_COLOR_G = 255;
    public static int IP_COLOR_B = 255;
    public static int IP_VALUE_COLOR_R = 255;
    public static int IP_VALUE_COLOR_G = 255;
    public static int IP_VALUE_COLOR_B = 255;
    public static int SLIME_COLOR_R = 255;
    public static int SLIME_COLOR_G = 255;
    public static int SLIME_COLOR_B = 255;
    public static int SLIME_VALUE_COLOR_R = 255;
    public static int SLIME_VALUE_COLOR_G = 255;
    public static int SLIME_VALUE_COLOR_B = 255;
    public static int EQUIPMENT_COLOR_R = 255;
    public static int EQUIPMENT_COLOR_G = 255;
    public static int EQUIPMENT_COLOR_B = 255;
    public static int ARROW_COUNT_COLOR_R = 255;
    public static int ARROW_COUNT_COLOR_G = 255;
    public static int ARROW_COUNT_COLOR_B = 255;

    public static String TOGGLE_SPRINT_USE_MODE = "command";
    public static String TOGGLE_SNEAK_USE_MODE = "command";
    public static String AUTO_SWIM_USE_MODE = "command";

    public static void load()
    {
        try
        {
            NBTTagCompound nbt = CompressedStreamTools.read(ExtendedConfig.FILE);

            if (nbt == null)
            {
                return;
            }

            ExtendedConfig.TOGGLE_SPRINT = ExtendedConfig.getBoolean(nbt, "ToggleSprint", ExtendedConfig.TOGGLE_SPRINT);
            ExtendedConfig.TOGGLE_SNEAK = ExtendedConfig.getBoolean(nbt, "ToggleSneak", ExtendedConfig.TOGGLE_SNEAK);
            ExtendedConfig.AUTO_SWIM = ExtendedConfig.getBoolean(nbt, "AutoSwim", ExtendedConfig.AUTO_SWIM);
            ExtendedConfig.SHOW_CAPE = ExtendedConfig.getBoolean(nbt, "ShowCape", ExtendedConfig.SHOW_CAPE);

            ExtendedConfig.KEYSTROKE_Y_OFFSET = ExtendedConfig.getInteger(nbt, "KeystrokeY", ExtendedConfig.KEYSTROKE_Y_OFFSET);
            ExtendedConfig.ARMOR_STATUS_OFFSET = ExtendedConfig.getInteger(nbt, "ArmorStatusOffset", ExtendedConfig.ARMOR_STATUS_OFFSET);
            ExtendedConfig.POTION_STATUS_OFFSET = ExtendedConfig.getInteger(nbt, "PotionStatusOffset", ExtendedConfig.POTION_STATUS_OFFSET);
            ExtendedConfig.CPS_X_OFFSET = ExtendedConfig.getInteger(nbt, "CpsX", ExtendedConfig.CPS_X_OFFSET);
            ExtendedConfig.CPS_Y_OFFSET = ExtendedConfig.getInteger(nbt, "CpsY", ExtendedConfig.CPS_Y_OFFSET);
            ExtendedConfig.MAX_POTION_DISPLAY = ExtendedConfig.getInteger(nbt, "MaxPotionDisplay", ExtendedConfig.MAX_POTION_DISPLAY);
            ExtendedConfig.POTION_LENGTH_Y_OFFSET = ExtendedConfig.getInteger(nbt, "PotionLengthYOffset", ExtendedConfig.POTION_LENGTH_Y_OFFSET);
            ExtendedConfig.POTION_LENGTH_Y_OFFSET_OVERLAP = ExtendedConfig.getInteger(nbt, "PotionLengthYOffsetOverlap", ExtendedConfig.POTION_LENGTH_Y_OFFSET_OVERLAP);
            ExtendedConfig.PREV_SELECT_DROPDOWN = ExtendedConfig.getInteger(nbt, "PrevSelectDropdown", ExtendedConfig.PREV_SELECT_DROPDOWN);
            ExtendedConfig.CPS_OPACITY = ExtendedConfig.getFloat(nbt, "CpsOpacity", ExtendedConfig.CPS_OPACITY);
            ExtendedConfig.SLIME_CHUNK_SEED = ExtendedConfig.getLong(nbt, "SlimeChunkSeed", ExtendedConfig.SLIME_CHUNK_SEED);

            ExtendedConfig.KEYSTROKE_BLOCK_RED = ExtendedConfig.getInteger(nbt, "KSBlockR", ExtendedConfig.KEYSTROKE_BLOCK_RED);
            ExtendedConfig.KEYSTROKE_BLOCK_GREEN = ExtendedConfig.getInteger(nbt, "KSBlockG", ExtendedConfig.KEYSTROKE_BLOCK_GREEN);
            ExtendedConfig.KEYSTROKE_BLOCK_BLUE = ExtendedConfig.getInteger(nbt, "KSBlockB", ExtendedConfig.KEYSTROKE_BLOCK_BLUE);
            ExtendedConfig.KEYSTROKE_CPS_RED = ExtendedConfig.getInteger(nbt, "KSCPSR", ExtendedConfig.KEYSTROKE_CPS_RED);
            ExtendedConfig.KEYSTROKE_CPS_GREEN = ExtendedConfig.getInteger(nbt, "KSCPSG", ExtendedConfig.KEYSTROKE_CPS_GREEN);
            ExtendedConfig.KEYSTROKE_CPS_BLUE = ExtendedConfig.getInteger(nbt, "KSCPSB", ExtendedConfig.KEYSTROKE_CPS_BLUE);
            ExtendedConfig.KEYSTROKE_WASD_RED = ExtendedConfig.getInteger(nbt, "KSWASDR", ExtendedConfig.KEYSTROKE_WASD_RED);
            ExtendedConfig.KEYSTROKE_WASD_GREEN = ExtendedConfig.getInteger(nbt, "KSWASDG", ExtendedConfig.KEYSTROKE_WASD_GREEN);
            ExtendedConfig.KEYSTROKE_WASD_BLUE = ExtendedConfig.getInteger(nbt, "KSWASDB", ExtendedConfig.KEYSTROKE_WASD_BLUE);
            ExtendedConfig.KEYSTROKE_LMBRMB_RED = ExtendedConfig.getInteger(nbt, "KSLMBRMBR", ExtendedConfig.KEYSTROKE_LMBRMB_RED);
            ExtendedConfig.KEYSTROKE_LMBRMB_GREEN = ExtendedConfig.getInteger(nbt, "KSLMBRMBG", ExtendedConfig.KEYSTROKE_LMBRMB_GREEN);
            ExtendedConfig.KEYSTROKE_LMBRMB_BLUE = ExtendedConfig.getInteger(nbt, "KSLMBRMBB", ExtendedConfig.KEYSTROKE_LMBRMB_BLUE);
            ExtendedConfig.KEYSTROKE_SPRINT_RED = ExtendedConfig.getInteger(nbt, "KSSprintR", ExtendedConfig.KEYSTROKE_SPRINT_RED);
            ExtendedConfig.KEYSTROKE_SPRINT_GREEN = ExtendedConfig.getInteger(nbt, "KSSprintG", ExtendedConfig.KEYSTROKE_SPRINT_GREEN);
            ExtendedConfig.KEYSTROKE_SPRINT_BLUE = ExtendedConfig.getInteger(nbt, "KSSprintB", ExtendedConfig.KEYSTROKE_SPRINT_BLUE);
            ExtendedConfig.KEYSTROKE_SNEAK_RED = ExtendedConfig.getInteger(nbt, "KSSneakR", ExtendedConfig.KEYSTROKE_SNEAK_RED);
            ExtendedConfig.KEYSTROKE_SNEAK_GREEN = ExtendedConfig.getInteger(nbt, "KSSneakG", ExtendedConfig.KEYSTROKE_SNEAK_GREEN);
            ExtendedConfig.KEYSTROKE_SNEAK_BLUE = ExtendedConfig.getInteger(nbt, "KSSneakB", ExtendedConfig.KEYSTROKE_SNEAK_BLUE);

            ExtendedConfig.KEYSTROKE_BLOCK_RAINBOW = ExtendedConfig.getBoolean(nbt, "KSBlockRB", ExtendedConfig.KEYSTROKE_BLOCK_RAINBOW);
            ExtendedConfig.KEYSTROKE_CPS_RAINBOW = ExtendedConfig.getBoolean(nbt, "KSCPSRB", ExtendedConfig.KEYSTROKE_CPS_RAINBOW);
            ExtendedConfig.KEYSTROKE_WASD_RAINBOW = ExtendedConfig.getBoolean(nbt, "KSWASDRB", ExtendedConfig.KEYSTROKE_WASD_RAINBOW);
            ExtendedConfig.KEYSTROKE_LMBRMB_RAINBOW = ExtendedConfig.getBoolean(nbt, "KSLMBRMBRB", ExtendedConfig.KEYSTROKE_LMBRMB_RAINBOW);
            ExtendedConfig.KEYSTROKE_SPRINT_RAINBOW = ExtendedConfig.getBoolean(nbt, "KSSprintRB", ExtendedConfig.KEYSTROKE_SPRINT_RAINBOW);
            ExtendedConfig.KEYSTROKE_SNEAK_RAINBOW = ExtendedConfig.getBoolean(nbt, "KSSneakRB", ExtendedConfig.KEYSTROKE_SNEAK_RAINBOW);

            ExtendedConfig.CPS_POSITION = ExtendedConfig.getString(nbt, "CpsPosition", ExtendedConfig.CPS_POSITION);
            ExtendedConfig.HYPIXEL_NICK_NAME = ExtendedConfig.getString(nbt, "HypixelNickName", ExtendedConfig.HYPIXEL_NICK_NAME);
            ExtendedConfig.TOP_DONATOR_FILE_PATH = ExtendedConfig.getString(nbt, "TopDonatorFilePath", ExtendedConfig.TOP_DONATOR_FILE_PATH);
            ExtendedConfig.RECENT_DONATOR_FILE_PATH = ExtendedConfig.getString(nbt, "RecentDonatorFilePath", ExtendedConfig.RECENT_DONATOR_FILE_PATH);
            ExtendedConfig.TOP_DONATOR_TEXT = ExtendedConfig.getString(nbt, "TopDonatorText", ExtendedConfig.TOP_DONATOR_TEXT);
            ExtendedConfig.RECENT_DONATOR_TEXT = ExtendedConfig.getString(nbt, "RecentDonatorText", ExtendedConfig.RECENT_DONATOR_TEXT);

            ExtendedConfig.FPS_COLOR_R = ExtendedConfig.getInteger(nbt, "FPSColorR", ExtendedConfig.FPS_COLOR_R);
            ExtendedConfig.FPS_COLOR_G = ExtendedConfig.getInteger(nbt, "FPSColorG", ExtendedConfig.FPS_COLOR_G);
            ExtendedConfig.FPS_COLOR_B = ExtendedConfig.getInteger(nbt, "FPSColorB", ExtendedConfig.FPS_COLOR_B);
            ExtendedConfig.FPS_M40_COLOR_R = ExtendedConfig.getInteger(nbt, "FPSM40ColorR", ExtendedConfig.FPS_M40_COLOR_R);
            ExtendedConfig.FPS_M40_COLOR_G = ExtendedConfig.getInteger(nbt, "FPSM40ColorG", ExtendedConfig.FPS_M40_COLOR_G);
            ExtendedConfig.FPS_M40_COLOR_B = ExtendedConfig.getInteger(nbt, "FPSM40ColorB", ExtendedConfig.FPS_M40_COLOR_B);
            ExtendedConfig.FPS_26_40_COLOR_R = ExtendedConfig.getInteger(nbt, "FPS2640ColorR", ExtendedConfig.FPS_26_40_COLOR_R);
            ExtendedConfig.FPS_26_40_COLOR_G = ExtendedConfig.getInteger(nbt, "FPS2640ColorG", ExtendedConfig.FPS_26_40_COLOR_G);
            ExtendedConfig.FPS_26_40_COLOR_B = ExtendedConfig.getInteger(nbt, "FPS2640ColorB", ExtendedConfig.FPS_26_40_COLOR_B);
            ExtendedConfig.FPS_L25_COLOR_R = ExtendedConfig.getInteger(nbt, "FPSL25ColorR", ExtendedConfig.FPS_L25_COLOR_R);
            ExtendedConfig.FPS_L25_COLOR_G = ExtendedConfig.getInteger(nbt, "FPSL25ColorG", ExtendedConfig.FPS_L25_COLOR_G);
            ExtendedConfig.FPS_L25_COLOR_B = ExtendedConfig.getInteger(nbt, "FPSL25ColorB", ExtendedConfig.FPS_L25_COLOR_B);
            ExtendedConfig.XYZ_COLOR_R = ExtendedConfig.getInteger(nbt, "XYZColorR", ExtendedConfig.XYZ_COLOR_R);
            ExtendedConfig.XYZ_COLOR_G = ExtendedConfig.getInteger(nbt, "XYZColorG", ExtendedConfig.XYZ_COLOR_G);
            ExtendedConfig.XYZ_COLOR_B = ExtendedConfig.getInteger(nbt, "XYZColorB", ExtendedConfig.XYZ_COLOR_B);
            ExtendedConfig.XYZ_VALUE_COLOR_R = ExtendedConfig.getInteger(nbt, "XYZValueColorR", ExtendedConfig.XYZ_VALUE_COLOR_R);
            ExtendedConfig.XYZ_VALUE_COLOR_G = ExtendedConfig.getInteger(nbt, "XYZValueColorG", ExtendedConfig.XYZ_VALUE_COLOR_G);
            ExtendedConfig.XYZ_VALUE_COLOR_B = ExtendedConfig.getInteger(nbt, "XYZValueColorB", ExtendedConfig.XYZ_VALUE_COLOR_B);
            ExtendedConfig.BIOME_COLOR_R = ExtendedConfig.getInteger(nbt, "BiomeColorR", ExtendedConfig.BIOME_COLOR_R);
            ExtendedConfig.BIOME_COLOR_G = ExtendedConfig.getInteger(nbt, "BiomeColorG", ExtendedConfig.BIOME_COLOR_G);
            ExtendedConfig.BIOME_COLOR_B = ExtendedConfig.getInteger(nbt, "BiomeColorB", ExtendedConfig.BIOME_COLOR_B);
            ExtendedConfig.BIOME_VALUE_COLOR_R = ExtendedConfig.getInteger(nbt, "BiomeValueColorR", ExtendedConfig.BIOME_VALUE_COLOR_R);
            ExtendedConfig.BIOME_VALUE_COLOR_G = ExtendedConfig.getInteger(nbt, "BiomeValueColorG", ExtendedConfig.BIOME_VALUE_COLOR_G);
            ExtendedConfig.BIOME_VALUE_COLOR_B = ExtendedConfig.getInteger(nbt, "BiomeValueColorB", ExtendedConfig.BIOME_VALUE_COLOR_B);
            ExtendedConfig.PING_COLOR_R = ExtendedConfig.getInteger(nbt, "PingColorR", ExtendedConfig.PING_COLOR_R);
            ExtendedConfig.PING_COLOR_G = ExtendedConfig.getInteger(nbt, "PingColorG", ExtendedConfig.PING_COLOR_G);
            ExtendedConfig.PING_COLOR_B = ExtendedConfig.getInteger(nbt, "PingColorB", ExtendedConfig.PING_COLOR_B);
            ExtendedConfig.PING_L200_COLOR_R = ExtendedConfig.getInteger(nbt, "PingL200ColorR", ExtendedConfig.PING_L200_COLOR_R);
            ExtendedConfig.PING_L200_COLOR_G = ExtendedConfig.getInteger(nbt, "PingL200ColorG", ExtendedConfig.PING_L200_COLOR_G);
            ExtendedConfig.PING_L200_COLOR_B = ExtendedConfig.getInteger(nbt, "PingL200ColorB", ExtendedConfig.PING_L200_COLOR_B);
            ExtendedConfig.PING_200_300_COLOR_R = ExtendedConfig.getInteger(nbt, "Ping200300ColorR", ExtendedConfig.PING_200_300_COLOR_R);
            ExtendedConfig.PING_200_300_COLOR_G = ExtendedConfig.getInteger(nbt, "Ping200300ColorG", ExtendedConfig.PING_200_300_COLOR_G);
            ExtendedConfig.PING_200_300_COLOR_B = ExtendedConfig.getInteger(nbt, "Ping200300ColorB", ExtendedConfig.PING_200_300_COLOR_B);
            ExtendedConfig.PING_300_500_COLOR_R = ExtendedConfig.getInteger(nbt, "Ping300500ColorR", ExtendedConfig.PING_300_500_COLOR_R);
            ExtendedConfig.PING_300_500_COLOR_G = ExtendedConfig.getInteger(nbt, "Ping300500ColorG", ExtendedConfig.PING_300_500_COLOR_G);
            ExtendedConfig.PING_300_500_COLOR_B = ExtendedConfig.getInteger(nbt, "Ping300500ColorB", ExtendedConfig.PING_300_500_COLOR_B);
            ExtendedConfig.PING_M500_COLOR_R = ExtendedConfig.getInteger(nbt, "PingM500ColorR", ExtendedConfig.PING_M500_COLOR_R);
            ExtendedConfig.PING_M500_COLOR_G = ExtendedConfig.getInteger(nbt, "PingM500ColorG", ExtendedConfig.PING_M500_COLOR_G);
            ExtendedConfig.PING_M500_COLOR_B = ExtendedConfig.getInteger(nbt, "PingM500ColorB", ExtendedConfig.PING_M500_COLOR_B);
            ExtendedConfig.IP_COLOR_R = ExtendedConfig.getInteger(nbt, "IPColorR", ExtendedConfig.IP_COLOR_R);
            ExtendedConfig.IP_COLOR_G = ExtendedConfig.getInteger(nbt, "IPColorG", ExtendedConfig.IP_COLOR_G);
            ExtendedConfig.IP_COLOR_B = ExtendedConfig.getInteger(nbt, "IPColorB", ExtendedConfig.IP_COLOR_B);
            ExtendedConfig.IP_VALUE_COLOR_R = ExtendedConfig.getInteger(nbt, "IPValueColorR", ExtendedConfig.IP_VALUE_COLOR_R);
            ExtendedConfig.IP_VALUE_COLOR_G = ExtendedConfig.getInteger(nbt, "IPValueColorG", ExtendedConfig.IP_VALUE_COLOR_G);
            ExtendedConfig.IP_VALUE_COLOR_B = ExtendedConfig.getInteger(nbt, "IPValueColorB", ExtendedConfig.IP_VALUE_COLOR_B);
            ExtendedConfig.CPS_COLOR_R = ExtendedConfig.getInteger(nbt, "CPSColorR", ExtendedConfig.CPS_COLOR_R);
            ExtendedConfig.CPS_COLOR_G = ExtendedConfig.getInteger(nbt, "CPSColorG", ExtendedConfig.CPS_COLOR_G);
            ExtendedConfig.CPS_COLOR_B = ExtendedConfig.getInteger(nbt, "CPSColorB", ExtendedConfig.CPS_COLOR_B);
            ExtendedConfig.CPS_VALUE_COLOR_R = ExtendedConfig.getInteger(nbt, "CPSValueColorR", ExtendedConfig.CPS_VALUE_COLOR_R);
            ExtendedConfig.CPS_VALUE_COLOR_G = ExtendedConfig.getInteger(nbt, "CPSValueColorG", ExtendedConfig.CPS_VALUE_COLOR_G);
            ExtendedConfig.CPS_VALUE_COLOR_B = ExtendedConfig.getInteger(nbt, "CPSValueColorB", ExtendedConfig.CPS_VALUE_COLOR_B);
            ExtendedConfig.RCPS_COLOR_R = ExtendedConfig.getInteger(nbt, "RCPSColorR", ExtendedConfig.RCPS_COLOR_R);
            ExtendedConfig.RCPS_COLOR_G = ExtendedConfig.getInteger(nbt, "RCPSColorG", ExtendedConfig.RCPS_COLOR_G);
            ExtendedConfig.RCPS_COLOR_B = ExtendedConfig.getInteger(nbt, "RCPSColorB", ExtendedConfig.RCPS_COLOR_B);
            ExtendedConfig.RCPS_VALUE_COLOR_R = ExtendedConfig.getInteger(nbt, "RCPSValueColorR", ExtendedConfig.RCPS_VALUE_COLOR_R);
            ExtendedConfig.RCPS_VALUE_COLOR_G = ExtendedConfig.getInteger(nbt, "RCPSValueColorG", ExtendedConfig.RCPS_VALUE_COLOR_G);
            ExtendedConfig.RCPS_VALUE_COLOR_B = ExtendedConfig.getInteger(nbt, "RCPSValueColorB", ExtendedConfig.RCPS_VALUE_COLOR_B);
            ExtendedConfig.TOP_DONATE_NAME_COLOR_R = ExtendedConfig.getInteger(nbt, "TopDonateNameColorR", ExtendedConfig.TOP_DONATE_NAME_COLOR_R);
            ExtendedConfig.TOP_DONATE_NAME_COLOR_G = ExtendedConfig.getInteger(nbt, "TopDonateNameColorG", ExtendedConfig.TOP_DONATE_NAME_COLOR_G);
            ExtendedConfig.TOP_DONATE_NAME_COLOR_B = ExtendedConfig.getInteger(nbt, "TopDonateNameColorB", ExtendedConfig.TOP_DONATE_NAME_COLOR_B);
            ExtendedConfig.RECENT_DONATE_NAME_COLOR_R = ExtendedConfig.getInteger(nbt, "RecentDonateNameColorR", ExtendedConfig.RECENT_DONATE_NAME_COLOR_R);
            ExtendedConfig.RECENT_DONATE_NAME_COLOR_G = ExtendedConfig.getInteger(nbt, "RecentDonateNameColorG", ExtendedConfig.RECENT_DONATE_NAME_COLOR_G);
            ExtendedConfig.RECENT_DONATE_NAME_COLOR_B = ExtendedConfig.getInteger(nbt, "RecentDonateNameColorB", ExtendedConfig.RECENT_DONATE_NAME_COLOR_B);
            ExtendedConfig.TOP_DONATE_COUNT_COLOR_R = ExtendedConfig.getInteger(nbt, "TopDonateCountColorR", ExtendedConfig.TOP_DONATE_COUNT_COLOR_R);
            ExtendedConfig.TOP_DONATE_COUNT_COLOR_G = ExtendedConfig.getInteger(nbt, "TopDonateCountColorG", ExtendedConfig.TOP_DONATE_COUNT_COLOR_G);
            ExtendedConfig.TOP_DONATE_COUNT_COLOR_B = ExtendedConfig.getInteger(nbt, "TopDonateCountColorB", ExtendedConfig.TOP_DONATE_COUNT_COLOR_B);
            ExtendedConfig.RECENT_DONATE_COUNT_COLOR_R = ExtendedConfig.getInteger(nbt, "RecentDonateCountColorR", ExtendedConfig.RECENT_DONATE_COUNT_COLOR_R);
            ExtendedConfig.RECENT_DONATE_COUNT_COLOR_G = ExtendedConfig.getInteger(nbt, "RecentDonateCountColorG", ExtendedConfig.RECENT_DONATE_COUNT_COLOR_G);
            ExtendedConfig.RECENT_DONATE_COUNT_COLOR_B = ExtendedConfig.getInteger(nbt, "RecentDonateCountColorB", ExtendedConfig.RECENT_DONATE_COUNT_COLOR_B);
            ExtendedConfig.SLIME_COLOR_R = ExtendedConfig.getInteger(nbt, "SlimeColorR", ExtendedConfig.SLIME_COLOR_R);
            ExtendedConfig.SLIME_COLOR_G = ExtendedConfig.getInteger(nbt, "SlimeColorG", ExtendedConfig.SLIME_COLOR_G);
            ExtendedConfig.SLIME_COLOR_B = ExtendedConfig.getInteger(nbt, "SlimeColorB", ExtendedConfig.SLIME_COLOR_B);
            ExtendedConfig.SLIME_VALUE_COLOR_R = ExtendedConfig.getInteger(nbt, "SlimeValueColorR", ExtendedConfig.SLIME_VALUE_COLOR_R);
            ExtendedConfig.SLIME_VALUE_COLOR_G = ExtendedConfig.getInteger(nbt, "SlimeValueColorG", ExtendedConfig.SLIME_VALUE_COLOR_G);
            ExtendedConfig.SLIME_VALUE_COLOR_B = ExtendedConfig.getInteger(nbt, "SlimeValueColorB", ExtendedConfig.SLIME_VALUE_COLOR_B);
            ExtendedConfig.EQUIPMENT_COLOR_R = ExtendedConfig.getInteger(nbt, "EquipmentColorR", ExtendedConfig.EQUIPMENT_COLOR_R);
            ExtendedConfig.EQUIPMENT_COLOR_G = ExtendedConfig.getInteger(nbt, "EquipmentColorG", ExtendedConfig.EQUIPMENT_COLOR_G);
            ExtendedConfig.EQUIPMENT_COLOR_B = ExtendedConfig.getInteger(nbt, "EquipmentColorB", ExtendedConfig.EQUIPMENT_COLOR_B);
            ExtendedConfig.ARROW_COUNT_COLOR_R = ExtendedConfig.getInteger(nbt, "ArrowCountColorR", ExtendedConfig.ARROW_COUNT_COLOR_R);
            ExtendedConfig.ARROW_COUNT_COLOR_G = ExtendedConfig.getInteger(nbt, "ArrowCountColorG", ExtendedConfig.ARROW_COUNT_COLOR_G);
            ExtendedConfig.ARROW_COUNT_COLOR_B = ExtendedConfig.getInteger(nbt, "ArrowCountColorB", ExtendedConfig.ARROW_COUNT_COLOR_B);

            ExtendedConfig.TOGGLE_SPRINT_USE_MODE = ExtendedConfig.getString(nbt, "ToggleSprintUseMode", ExtendedConfig.TOGGLE_SPRINT_USE_MODE);
            ExtendedConfig.TOGGLE_SNEAK_USE_MODE = ExtendedConfig.getString(nbt, "ToggleSneakUseMode", ExtendedConfig.TOGGLE_SNEAK_USE_MODE);
            ExtendedConfig.AUTO_SWIM_USE_MODE = ExtendedConfig.getString(nbt, "AutoSwimUseMode", ExtendedConfig.AUTO_SWIM_USE_MODE);

            ExtendedConfig.readAutoLoginData(nbt.getTagList("AutoLoginData", 10));
            HideNameData.load(nbt.getTagList("HideNameList", 10));

            ModLogger.info("Loading extended config {}", ExtendedConfig.FILE.getPath());
        }
        catch (Exception e) {}
    }

    public static void save()
    {
        try
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setBoolean("ToggleSprint", ExtendedConfig.TOGGLE_SPRINT);
            nbt.setBoolean("ToggleSneak", ExtendedConfig.TOGGLE_SNEAK);
            nbt.setBoolean("AutoSwim", ExtendedConfig.AUTO_SWIM);
            nbt.setBoolean("ShowCape", ExtendedConfig.SHOW_CAPE);

            nbt.setInteger("KeystrokeY", ExtendedConfig.KEYSTROKE_Y_OFFSET);
            nbt.setInteger("ArmorStatusOffset", ExtendedConfig.ARMOR_STATUS_OFFSET);
            nbt.setInteger("PotionStatusOffset", ExtendedConfig.POTION_STATUS_OFFSET);
            nbt.setInteger("CpsX", ExtendedConfig.CPS_X_OFFSET);
            nbt.setInteger("CpsY", ExtendedConfig.CPS_Y_OFFSET);
            nbt.setInteger("MaxPotionDisplay", ExtendedConfig.MAX_POTION_DISPLAY);
            nbt.setInteger("PotionLengthYOffset", ExtendedConfig.POTION_LENGTH_Y_OFFSET);
            nbt.setInteger("PotionLengthYOffsetOverlap", ExtendedConfig.POTION_LENGTH_Y_OFFSET_OVERLAP);
            nbt.setInteger("PrevSelectDropdown", ExtendedConfig.PREV_SELECT_DROPDOWN);
            nbt.setFloat("CpsOpacity", ExtendedConfig.CPS_OPACITY);
            nbt.setLong("SlimeChunkSeed", ExtendedConfig.SLIME_CHUNK_SEED);

            nbt.setInteger("KSBlockR", ExtendedConfig.KEYSTROKE_BLOCK_RED);
            nbt.setInteger("KSBlockG", ExtendedConfig.KEYSTROKE_BLOCK_GREEN);
            nbt.setInteger("KSBlockB", ExtendedConfig.KEYSTROKE_BLOCK_BLUE);
            nbt.setInteger("KSCPSR", ExtendedConfig.KEYSTROKE_CPS_RED);
            nbt.setInteger("KSCPSG", ExtendedConfig.KEYSTROKE_CPS_GREEN);
            nbt.setInteger("KSCPSB", ExtendedConfig.KEYSTROKE_CPS_BLUE);
            nbt.setInteger("KSWASDR", ExtendedConfig.KEYSTROKE_WASD_RED);
            nbt.setInteger("KSWASDG", ExtendedConfig.KEYSTROKE_WASD_GREEN);
            nbt.setInteger("KSWASDB", ExtendedConfig.KEYSTROKE_WASD_BLUE);
            nbt.setInteger("KSLMBRMBR", ExtendedConfig.KEYSTROKE_LMBRMB_RED);
            nbt.setInteger("KSLMBRMBG", ExtendedConfig.KEYSTROKE_LMBRMB_GREEN);
            nbt.setInteger("KSLMBRMBB", ExtendedConfig.KEYSTROKE_LMBRMB_BLUE);
            nbt.setInteger("KSSprintR", ExtendedConfig.KEYSTROKE_SPRINT_RED);
            nbt.setInteger("KSSprintG", ExtendedConfig.KEYSTROKE_SPRINT_GREEN);
            nbt.setInteger("KSSprintB", ExtendedConfig.KEYSTROKE_SPRINT_BLUE);
            nbt.setInteger("KSSneakR", ExtendedConfig.KEYSTROKE_SNEAK_RED);
            nbt.setInteger("KSSneakG", ExtendedConfig.KEYSTROKE_SNEAK_GREEN);
            nbt.setInteger("KSSneakB", ExtendedConfig.KEYSTROKE_SNEAK_BLUE);

            nbt.setBoolean("KSBlockRB", ExtendedConfig.KEYSTROKE_BLOCK_RAINBOW);
            nbt.setBoolean("KSCPSRB", ExtendedConfig.KEYSTROKE_CPS_RAINBOW);
            nbt.setBoolean("KSWASDRB", ExtendedConfig.KEYSTROKE_WASD_RAINBOW);
            nbt.setBoolean("KSLMBRMBRB", ExtendedConfig.KEYSTROKE_LMBRMB_RAINBOW);
            nbt.setBoolean("KSSprintRB", ExtendedConfig.KEYSTROKE_SPRINT_RAINBOW);
            nbt.setBoolean("KSSneakRB", ExtendedConfig.KEYSTROKE_SNEAK_RAINBOW);

            nbt.setString("CpsPosition", ExtendedConfig.CPS_POSITION);
            nbt.setString("HypixelNickName", ExtendedConfig.HYPIXEL_NICK_NAME);
            nbt.setString("TopDonatorFilePath", ExtendedConfig.TOP_DONATOR_FILE_PATH);
            nbt.setString("RecentDonatorFilePath", ExtendedConfig.RECENT_DONATOR_FILE_PATH);
            nbt.setString("TopDonatorText", ExtendedConfig.TOP_DONATOR_TEXT);
            nbt.setString("RecentDonatorText", ExtendedConfig.RECENT_DONATOR_TEXT);

            nbt.setInteger("FPSColorR", ExtendedConfig.FPS_COLOR_R);
            nbt.setInteger("FPSColorG", ExtendedConfig.FPS_COLOR_G);
            nbt.setInteger("FPSColorB", ExtendedConfig.FPS_COLOR_B);
            nbt.setInteger("FPSM40ColorR", ExtendedConfig.FPS_M40_COLOR_R);
            nbt.setInteger("FPSM40ColorG", ExtendedConfig.FPS_M40_COLOR_G);
            nbt.setInteger("FPSM40ColorB", ExtendedConfig.FPS_M40_COLOR_B);
            nbt.setInteger("FPS2640ColorR", ExtendedConfig.FPS_26_40_COLOR_R);
            nbt.setInteger("FPS2640ColorG", ExtendedConfig.FPS_26_40_COLOR_G);
            nbt.setInteger("FPS2640ColorB", ExtendedConfig.FPS_26_40_COLOR_B);
            nbt.setInteger("FPSL25ColorR", ExtendedConfig.FPS_L25_COLOR_R);
            nbt.setInteger("FPSL25ColorG", ExtendedConfig.FPS_L25_COLOR_G);
            nbt.setInteger("FPSL25ColorB", ExtendedConfig.FPS_L25_COLOR_B);
            nbt.setInteger("XYZColorR", ExtendedConfig.XYZ_COLOR_R);
            nbt.setInteger("XYZColorG", ExtendedConfig.XYZ_COLOR_G);
            nbt.setInteger("XYZColorB", ExtendedConfig.XYZ_COLOR_B);
            nbt.setInteger("XYZValueColorR", ExtendedConfig.XYZ_VALUE_COLOR_R);
            nbt.setInteger("XYZValueColorG", ExtendedConfig.XYZ_VALUE_COLOR_G);
            nbt.setInteger("XYZValueColorB", ExtendedConfig.XYZ_VALUE_COLOR_B);
            nbt.setInteger("BiomeColorR", ExtendedConfig.BIOME_COLOR_R);
            nbt.setInteger("BiomeColorG", ExtendedConfig.BIOME_COLOR_G);
            nbt.setInteger("BiomeColorB", ExtendedConfig.BIOME_COLOR_B);
            nbt.setInteger("BiomeValueColorR", ExtendedConfig.BIOME_VALUE_COLOR_R);
            nbt.setInteger("BiomeValueColorG", ExtendedConfig.BIOME_VALUE_COLOR_G);
            nbt.setInteger("BiomeValueColorB", ExtendedConfig.BIOME_VALUE_COLOR_B);
            nbt.setInteger("PingColorR", ExtendedConfig.PING_COLOR_R);
            nbt.setInteger("PingColorG", ExtendedConfig.PING_COLOR_G);
            nbt.setInteger("PingColorB", ExtendedConfig.PING_COLOR_B);
            nbt.setInteger("PingL200ColorR", ExtendedConfig.PING_L200_COLOR_R);
            nbt.setInteger("PingL200ColorG", ExtendedConfig.PING_L200_COLOR_G);
            nbt.setInteger("PingL200ColorB", ExtendedConfig.PING_L200_COLOR_B);
            nbt.setInteger("Ping200300ColorR", ExtendedConfig.PING_200_300_COLOR_R);
            nbt.setInteger("Ping200300ColorG", ExtendedConfig.PING_200_300_COLOR_G);
            nbt.setInteger("Ping200300ColorB", ExtendedConfig.PING_200_300_COLOR_B);
            nbt.setInteger("Ping300500ColorR", ExtendedConfig.PING_300_500_COLOR_R);
            nbt.setInteger("Ping300500ColorG", ExtendedConfig.PING_300_500_COLOR_G);
            nbt.setInteger("Ping300500ColorB", ExtendedConfig.PING_300_500_COLOR_B);
            nbt.setInteger("PingM500ColorR", ExtendedConfig.PING_M500_COLOR_R);
            nbt.setInteger("PingM500ColorG", ExtendedConfig.PING_M500_COLOR_G);
            nbt.setInteger("PingM500ColorB", ExtendedConfig.PING_M500_COLOR_B);
            nbt.setInteger("IPColorR", ExtendedConfig.IP_COLOR_R);
            nbt.setInteger("IPColorG", ExtendedConfig.IP_COLOR_G);
            nbt.setInteger("IPColorB", ExtendedConfig.IP_COLOR_B);
            nbt.setInteger("IPValueColorR", ExtendedConfig.IP_VALUE_COLOR_R);
            nbt.setInteger("IPValueColorG", ExtendedConfig.IP_VALUE_COLOR_G);
            nbt.setInteger("IPValueColorB", ExtendedConfig.IP_VALUE_COLOR_B);
            nbt.setInteger("CPSColorR", ExtendedConfig.CPS_COLOR_R);
            nbt.setInteger("CPSColorG", ExtendedConfig.CPS_COLOR_G);
            nbt.setInteger("CPSColorB", ExtendedConfig.CPS_COLOR_B);
            nbt.setInteger("CPSValueColorR", ExtendedConfig.CPS_VALUE_COLOR_R);
            nbt.setInteger("CPSValueColorG", ExtendedConfig.CPS_VALUE_COLOR_G);
            nbt.setInteger("CPSValueColorB", ExtendedConfig.CPS_VALUE_COLOR_B);
            nbt.setInteger("RCPSColorR", ExtendedConfig.RCPS_COLOR_R);
            nbt.setInteger("RCPSColorG", ExtendedConfig.RCPS_COLOR_G);
            nbt.setInteger("RCPSColorB", ExtendedConfig.RCPS_COLOR_B);
            nbt.setInteger("RCPSValueColorR", ExtendedConfig.RCPS_VALUE_COLOR_R);
            nbt.setInteger("RCPSValueColorG", ExtendedConfig.RCPS_VALUE_COLOR_G);
            nbt.setInteger("RCPSValueColorB", ExtendedConfig.RCPS_VALUE_COLOR_B);
            nbt.setInteger("TopDonateNameColorR", ExtendedConfig.TOP_DONATE_NAME_COLOR_R);
            nbt.setInteger("TopDonateNameColorG", ExtendedConfig.TOP_DONATE_NAME_COLOR_G);
            nbt.setInteger("TopDonateNameColorB", ExtendedConfig.TOP_DONATE_NAME_COLOR_B);
            nbt.setInteger("RecentDonateNameColorR", ExtendedConfig.RECENT_DONATE_NAME_COLOR_R);
            nbt.setInteger("RecentDonateNameColorG", ExtendedConfig.RECENT_DONATE_NAME_COLOR_G);
            nbt.setInteger("RecentDonateNameColorB", ExtendedConfig.RECENT_DONATE_NAME_COLOR_B);
            nbt.setInteger("TopDonateCountColorR", ExtendedConfig.TOP_DONATE_COUNT_COLOR_R);
            nbt.setInteger("TopDonateCountColorG", ExtendedConfig.TOP_DONATE_COUNT_COLOR_G);
            nbt.setInteger("TopDonateCountColorB", ExtendedConfig.TOP_DONATE_COUNT_COLOR_B);
            nbt.setInteger("RecentDonateCountColorR", ExtendedConfig.RECENT_DONATE_COUNT_COLOR_R);
            nbt.setInteger("RecentDonateCountColorG", ExtendedConfig.RECENT_DONATE_COUNT_COLOR_G);
            nbt.setInteger("RecentDonateCountColorB", ExtendedConfig.RECENT_DONATE_COUNT_COLOR_B);
            nbt.setInteger("SlimeColorR", ExtendedConfig.SLIME_COLOR_R);
            nbt.setInteger("SlimeColorG", ExtendedConfig.SLIME_COLOR_G);
            nbt.setInteger("SlimeColorB", ExtendedConfig.SLIME_COLOR_B);
            nbt.setInteger("SlimeValueColorR", ExtendedConfig.SLIME_VALUE_COLOR_R);
            nbt.setInteger("SlimeValueColorG", ExtendedConfig.SLIME_VALUE_COLOR_G);
            nbt.setInteger("SlimeValueColorB", ExtendedConfig.SLIME_VALUE_COLOR_B);
            nbt.setInteger("EquipmentColorR", ExtendedConfig.EQUIPMENT_COLOR_R);
            nbt.setInteger("EquipmentColorG", ExtendedConfig.EQUIPMENT_COLOR_G);
            nbt.setInteger("EquipmentColorB", ExtendedConfig.EQUIPMENT_COLOR_B);
            nbt.setInteger("ArrowCountColorR", ExtendedConfig.ARROW_COUNT_COLOR_R);
            nbt.setInteger("ArrowCountColorG", ExtendedConfig.ARROW_COUNT_COLOR_G);
            nbt.setInteger("ArrowCountColorB", ExtendedConfig.ARROW_COUNT_COLOR_B);

            nbt.setString("ToggleSprintUseMode", ExtendedConfig.TOGGLE_SPRINT_USE_MODE);
            nbt.setString("ToggleSneakUseMode", ExtendedConfig.TOGGLE_SNEAK_USE_MODE);
            nbt.setString("AutoSwimUseMode", ExtendedConfig.AUTO_SWIM_USE_MODE);

            nbt.setTag("AutoLoginData", ExtendedConfig.writeAutoLoginData());
            nbt.setTag("HideNameList", HideNameData.save());

            CompressedStreamTools.safeWrite(nbt, ExtendedConfig.FILE);
        }
        catch (Exception e) {}
    }

    private static NBTTagList writeAutoLoginData()
    {
        NBTTagList list = new NBTTagList();

        for (AutoLoginData login : ExtendedConfig.loginData.getAutoLoginList())
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("ServerIP", login.getServerIP());
            nbt.setString("CommandName", login.getCommand());
            nbt.setString("Value", login.getValue());
            nbt.setString("UUID", login.getUUID().toString());
            nbt.setString("Function", login.getFunction());
            list.appendTag(nbt);
        }
        return list;
    }

    private static void readAutoLoginData(NBTTagList list)
    {
        for (int i = 0; i < list.tagCount(); ++i)
        {
            NBTTagCompound nbt = list.getCompoundTagAt(i);
            ExtendedConfig.loginData.addAutoLogin(nbt.getString("ServerIP"), nbt.getString("CommandName"), nbt.getString("Value"), UUID.fromString(nbt.getString("UUID")), nbt.getString("Function"));
        }
    }

    private static boolean getBoolean(NBTTagCompound nbt, String key, boolean defaultValue)
    {
        if (nbt.hasKey(key, 99))
        {
            return nbt.getBoolean(key);
        }
        else
        {
            return defaultValue;
        }
    }

    private static int getInteger(NBTTagCompound nbt, String key, int defaultValue)
    {
        if (nbt.hasKey(key, 99))
        {
            return nbt.getInteger(key);
        }
        else
        {
            return defaultValue;
        }
    }

    private static float getFloat(NBTTagCompound nbt, String key, float defaultValue)
    {
        if (nbt.hasKey(key, 99))
        {
            return nbt.getFloat(key);
        }
        else
        {
            return defaultValue;
        }
    }

    private static String getString(NBTTagCompound nbt, String key, String defaultValue)
    {
        if (nbt.hasKey(key, 8))
        {
            return nbt.getString(key);
        }
        else
        {
            return defaultValue;
        }
    }

    private static long getLong(NBTTagCompound nbt, String key, long defaultValue)
    {
        if (nbt.hasKey(key, 99))
        {
            return nbt.getLong(key);
        }
        else
        {
            return defaultValue;
        }
    }
}