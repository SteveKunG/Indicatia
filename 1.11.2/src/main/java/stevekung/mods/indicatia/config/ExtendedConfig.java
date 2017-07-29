package stevekung.mods.indicatia.config;

import java.io.File;
import java.util.UUID;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.util.AutoLogin;
import stevekung.mods.indicatia.util.AutoLogin.AutoLoginData;
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
    public static String ENTITY_DETECT_TYPE = "reset";
    public static String HYPIXEL_NICK_NAME = "";
    public static String TOP_DONATOR_FILE_PATH = "";
    public static String RECENT_DONATOR_FILE_PATH = "";
    public static String TOP_DONATOR_TEXT = "";
    public static String RECENT_DONATOR_TEXT = "";

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
            ExtendedConfig.ENTITY_DETECT_TYPE = ExtendedConfig.getString(nbt, "EntityDetectType", ExtendedConfig.ENTITY_DETECT_TYPE);
            ExtendedConfig.HYPIXEL_NICK_NAME = ExtendedConfig.getString(nbt, "HypixelNickName", ExtendedConfig.HYPIXEL_NICK_NAME);
            ExtendedConfig.TOP_DONATOR_FILE_PATH = ExtendedConfig.getString(nbt, "TopDonatorFilePath", ExtendedConfig.TOP_DONATOR_FILE_PATH);
            ExtendedConfig.RECENT_DONATOR_FILE_PATH = ExtendedConfig.getString(nbt, "RecentDonatorFilePath", ExtendedConfig.RECENT_DONATOR_FILE_PATH);
            ExtendedConfig.TOP_DONATOR_TEXT = ExtendedConfig.getString(nbt, "TopDonatorText", ExtendedConfig.TOP_DONATOR_TEXT);
            ExtendedConfig.RECENT_DONATOR_TEXT = ExtendedConfig.getString(nbt, "RecentDonatorText", ExtendedConfig.RECENT_DONATOR_TEXT);

            ExtendedConfig.TOGGLE_SPRINT_USE_MODE = ExtendedConfig.getString(nbt, "ToggleSprintUseMode", ExtendedConfig.TOGGLE_SPRINT_USE_MODE);
            ExtendedConfig.TOGGLE_SNEAK_USE_MODE = ExtendedConfig.getString(nbt, "ToggleSneakUseMode", ExtendedConfig.TOGGLE_SNEAK_USE_MODE);
            ExtendedConfig.AUTO_SWIM_USE_MODE = ExtendedConfig.getString(nbt, "AutoSwimUseMode", ExtendedConfig.AUTO_SWIM_USE_MODE);

            ExtendedConfig.readAutoLoginData(nbt.getTagList("AutoLoginData", 10));

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
            nbt.setString("EntityDetectType", ExtendedConfig.ENTITY_DETECT_TYPE);
            nbt.setString("HypixelNickName", ExtendedConfig.HYPIXEL_NICK_NAME);
            nbt.setString("TopDonatorFilePath", ExtendedConfig.TOP_DONATOR_FILE_PATH);
            nbt.setString("RecentDonatorFilePath", ExtendedConfig.RECENT_DONATOR_FILE_PATH);
            nbt.setString("TopDonatorText", ExtendedConfig.TOP_DONATOR_TEXT);
            nbt.setString("RecentDonatorText", ExtendedConfig.RECENT_DONATOR_TEXT);

            nbt.setString("ToggleSprintUseMode", ExtendedConfig.TOGGLE_SPRINT_USE_MODE);
            nbt.setString("ToggleSneakUseMode", ExtendedConfig.TOGGLE_SNEAK_USE_MODE);
            nbt.setString("AutoSwimUseMode", ExtendedConfig.AUTO_SWIM_USE_MODE);

            nbt.setTag("AutoLoginData", ExtendedConfig.writeAutoLoginData());

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
            list.appendTag(nbt);
        }
        return list;
    }

    private static void readAutoLoginData(NBTTagList list)
    {
        for (int i = 0; i < list.tagCount(); ++i)
        {
            NBTTagCompound nbt = list.getCompoundTagAt(i);
            ExtendedConfig.loginData.addAutoLogin(nbt.getString("ServerIP"), nbt.getString("CommandName"), nbt.getString("Value"), UUID.fromString(nbt.getString("UUID")));
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