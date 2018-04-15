package stevekung.mods.indicatia.config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.gui.config.GuiExtendedConfig;
import stevekung.mods.indicatia.utils.AutoLogin;
import stevekung.mods.indicatia.utils.HideNameData;
import stevekung.mods.indicatia.utils.ModLogger;
import stevekung.mods.stevekunglib.util.GameProfileUtils;
import stevekung.mods.stevekunglib.util.LangUtils;

public class ExtendedConfig
{
    public static final ExtendedConfig instance = new ExtendedConfig();
    public static final AutoLogin loginData = new AutoLogin();
    private static final String defaultWhite = "255,255,255";
    public static final File indicatiaDir = new File(Minecraft.getMinecraft().mcDataDir, "indicatia");
    public static final File userDir = new File(indicatiaDir, GameProfileUtils.getUUID().toString());
    public static final File defaultConfig = new File(userDir, "default.dat");
    public static String currentProfile = "";
    private static final String[] HEALTH_STATUS = new String[] {"indicatia.disabled", "health_status.always", "health_status.pointed"};
    private static final String[] POSITION = new String[] {"indicatia.left", "indicatia.right"};
    private static final String[] EQUIPMENT_ORDERING = new String[] {"indicatia.default", "equipment.reverse"};
    private static final String[] EQUIPMENT_DIRECTION = new String[] {"equipment.vertical", "equipment.horizontal"};
    private static final String[] EQUIPMENT_STATUS = new String[] {"equipment.damage_and_max_damage", "equipment.percent", "equipment.only_damage", "indicatia.none"};
    private static final String[] EQUIPMENT_POSITION = new String[] {"indicatia.left", "indicatia.right", "indicatia.hotbar"};
    private static final String[] POTION_STATUS_HUD_STYLE = new String[] {"indicatia.default", "potion_hud.icon_and_time"};
    private static final String[] POTION_STATUS_HUD_POSITION = new String[] {"indicatia.left", "indicatia.right", "indicatia.hotbar_left", "indicatia.hotbar_right"};
    private static final String[] CPS_POSITION = new String[] {"indicatia.left", "indicatia.right", "cps.keystroke", "cps.custom"};
    private static File file;

    // Render Info
    public static boolean fps = true;
    public static boolean xyz = true;
    public static boolean direction = true;
    public static boolean biome = true;
    public static boolean ping = true;
    public static boolean pingToSecond = false;
    public static boolean serverIP = false;
    public static boolean serverIPMCVersion = false;
    public static boolean equipmentHUD = false;
    public static boolean potionHUD = false;
    public static boolean keystroke = false;
    public static boolean keystrokeMouse = true;
    public static boolean keystrokeSprintSneak = true;
    public static boolean keystrokeBlocking = true;
    public static boolean cps = false;
    public static boolean rcps = false;
    public static boolean slimeChunkFinder = false;
    public static boolean realTime = true;
    public static boolean gameTime = true;
    public static boolean gameWeather = true;
    public static boolean moonPhase = true;
    public static boolean potionHUDIcon = false;
    public static boolean tps = false;
    public static boolean tpsAllDims = false;
    public static boolean alternatePotionHUDTextColor = false;
    public static boolean toggleSprint = false;
    public static boolean toggleSneak = false;

    // Main
    public static boolean swapRenderInfo = false;
    public static int healthStatusMode = 0;
    public static int keystrokePosition = 0;
    public static int equipmentOrdering = 0;
    public static int equipmentDirection = 0;
    public static int equipmentStatus = 0;
    public static int equipmentPosition = 2;
    public static int potionHUDStyle = 0;
    public static int potionHUDPosition = 0;
    public static int cpsPosition = 2;
    public static float cpsOpacity = 50.0F;

    // Offset
    public static int keystrokeYOffset = 0;
    public static int armorHUDYOffset = 0;
    public static int potionHUDYOffset = 0;
    public static int maximumPotionDisplay = 2;
    public static int potionLengthYOffset = 23;
    public static int potionLengthYOffsetOverlap = 45;

    // Custom Color
    public static String fpsColor = defaultWhite;
    public static String xyzColor = defaultWhite;
    public static String biomeColor = defaultWhite;
    public static String directionColor = defaultWhite;
    public static String pingColor = defaultWhite;
    public static String pingToSecondColor = defaultWhite;
    public static String serverIPColor = defaultWhite;
    public static String equipmentStatusColor = defaultWhite;
    public static String arrowCountColor = defaultWhite;
    public static String cpsColor = defaultWhite;
    public static String rcpsColor = defaultWhite;
    public static String slimeChunkColor = defaultWhite;
    public static String topDonatorNameColor = defaultWhite;
    public static String recentDonatorNameColor = defaultWhite;
    public static String tpsColor = defaultWhite;
    public static String realTimeColor = defaultWhite;
    public static String gameTimeColor = defaultWhite;
    public static String gameWeatherColor = defaultWhite;
    public static String moonPhaseColor = defaultWhite;

    // Custom Color : Value
    public static String fpsValueColor = "85,255,85";
    public static String fps26And49Color = "255,255,85";
    public static String fpsLow25Color = "255,85,85";
    public static String xyzValueColor = defaultWhite;
    public static String directionValueColor = defaultWhite;
    public static String biomeValueColor = defaultWhite;
    public static String pingValueColor = "85,255,85";
    public static String ping200And300Color = "255,255,85";
    public static String ping300And500Color = "255,85,85";
    public static String pingMax500Color = "170,0,0";
    public static String serverIPValueColor = defaultWhite;
    public static String cpsValueColor = defaultWhite;
    public static String rcpsValueColor = defaultWhite;
    public static String slimeChunkValueColor = defaultWhite;
    public static String topDonatorValueColor = defaultWhite;
    public static String recentDonatorValueColor = defaultWhite;
    public static String tpsValueColor = defaultWhite;
    public static String realTimeHHMMSSValueColor = defaultWhite;
    public static String realTimeDDMMYYValueColor = defaultWhite;
    public static String gameTimeValueColor = defaultWhite;
    public static String gameWeatherValueColor = defaultWhite;
    public static String moonPhaseValueColor = defaultWhite;

    // Custom Color : Keystroke
    public static String keystrokeWASDColor = defaultWhite;
    public static String keystrokeMouseButtonColor = defaultWhite;
    public static String keystrokeSprintColor = defaultWhite;
    public static String keystrokeSneakColor = defaultWhite;
    public static String keystrokeBlockingColor = defaultWhite;
    public static String keystrokeCPSColor = defaultWhite;
    public static String keystrokeRCPSColor = defaultWhite;
    public static boolean keystrokeWASDRainbow = false;
    public static boolean keystrokeMouseButtonRainbow = false;
    public static boolean keystrokeSprintRainbow = false;
    public static boolean keystrokeSneakRainbow = false;
    public static boolean keystrokeBlockingRainbow = false;
    public static boolean keystrokeCPSRainbow = false;
    public static boolean keystrokeRCPSRainbow = false;

    // Misc
    public static boolean showCustomCape = false;
    public static String toggleSprintUseMode = "command";
    public static String toggleSneakUseMode = "command";
    public static int cpsCustomXOffset = 3;
    public static int cpsCustomYOffset = 2;
    public static int selectedHypixelMinigame;
    public static long slimeChunkSeed = 0L;
    public static String topDonatorFilePath = "";
    public static String recentDonatorFilePath = "";
    public static String topDonatorText = "";
    public static String recentDonatorText = "";
    public static String hypixelNickName = "";
    public static String realmsMessage = "";

    private ExtendedConfig() {}

    public static void setCurrentProfile(String profileName)
    {
        ExtendedConfig.file = new File(userDir, profileName + ".dat");
        currentProfile = profileName;
    }

    public static void load()
    {
        try
        {
            NBTTagCompound nbt = CompressedStreamTools.read(ExtendedConfig.file);

            if (nbt == null)
            {
                return;
            }

            // Render Info
            ExtendedConfig.fps = ExtendedConfig.getBoolean(nbt, "FPS", ExtendedConfig.fps);
            ExtendedConfig.xyz = ExtendedConfig.getBoolean(nbt, "XYZ", ExtendedConfig.xyz);
            ExtendedConfig.direction = ExtendedConfig.getBoolean(nbt, "Direction", ExtendedConfig.direction);
            ExtendedConfig.biome = ExtendedConfig.getBoolean(nbt, "Biome", ExtendedConfig.biome);
            ExtendedConfig.ping = ExtendedConfig.getBoolean(nbt, "Ping", ExtendedConfig.ping);
            ExtendedConfig.pingToSecond = ExtendedConfig.getBoolean(nbt, "PingToSecond", ExtendedConfig.pingToSecond);
            ExtendedConfig.serverIP = ExtendedConfig.getBoolean(nbt, "ServerIP", ExtendedConfig.serverIP);
            ExtendedConfig.serverIPMCVersion = ExtendedConfig.getBoolean(nbt, "ServerIPMCVersion", ExtendedConfig.serverIPMCVersion);
            ExtendedConfig.equipmentHUD = ExtendedConfig.getBoolean(nbt, "EquipmentHUD", ExtendedConfig.equipmentHUD);
            ExtendedConfig.potionHUD = ExtendedConfig.getBoolean(nbt, "PotionHUD", ExtendedConfig.potionHUD);
            ExtendedConfig.keystroke = ExtendedConfig.getBoolean(nbt, "Keystroke", ExtendedConfig.keystroke);
            ExtendedConfig.keystrokeMouse = ExtendedConfig.getBoolean(nbt, "KeystrokeMouse", ExtendedConfig.keystrokeMouse);
            ExtendedConfig.keystrokeSprintSneak = ExtendedConfig.getBoolean(nbt, "KeystrokeSprintSneak", ExtendedConfig.keystrokeSprintSneak);
            ExtendedConfig.keystrokeBlocking = ExtendedConfig.getBoolean(nbt, "KeystrokeBlocking", ExtendedConfig.keystrokeBlocking);
            ExtendedConfig.cps = ExtendedConfig.getBoolean(nbt, "CPS", ExtendedConfig.cps);
            ExtendedConfig.rcps = ExtendedConfig.getBoolean(nbt, "RCPS", ExtendedConfig.rcps);
            ExtendedConfig.slimeChunkFinder = ExtendedConfig.getBoolean(nbt, "SlimeChunkFinder", ExtendedConfig.slimeChunkFinder);
            ExtendedConfig.realTime = ExtendedConfig.getBoolean(nbt, "RealTime", ExtendedConfig.realTime);
            ExtendedConfig.gameTime = ExtendedConfig.getBoolean(nbt, "GameTime", ExtendedConfig.gameTime);
            ExtendedConfig.gameWeather = ExtendedConfig.getBoolean(nbt, "GameWeather", ExtendedConfig.gameWeather);
            ExtendedConfig.moonPhase = ExtendedConfig.getBoolean(nbt, "MoonPhase", ExtendedConfig.moonPhase);
            ExtendedConfig.potionHUDIcon = ExtendedConfig.getBoolean(nbt, "PotionHUDIcon", ExtendedConfig.potionHUDIcon);
            ExtendedConfig.tps = ExtendedConfig.getBoolean(nbt, "TPS", ExtendedConfig.tps);
            ExtendedConfig.tpsAllDims = ExtendedConfig.getBoolean(nbt, "TPSAllDimensions", ExtendedConfig.tpsAllDims);
            ExtendedConfig.alternatePotionHUDTextColor = ExtendedConfig.getBoolean(nbt, "AlternatePotionHUDTextColor", ExtendedConfig.alternatePotionHUDTextColor);

            // Main
            ExtendedConfig.swapRenderInfo = ExtendedConfig.getBoolean(nbt, "SwapRenderInfo", ExtendedConfig.swapRenderInfo);
            ExtendedConfig.showCustomCape = ExtendedConfig.getBoolean(nbt, "ShowCustomCape", ExtendedConfig.showCustomCape);
            ExtendedConfig.healthStatusMode = ExtendedConfig.getInteger(nbt, "HealthStatusMode", ExtendedConfig.healthStatusMode);
            ExtendedConfig.keystrokePosition = ExtendedConfig.getInteger(nbt, "KeystrokePosition", ExtendedConfig.keystrokePosition);
            ExtendedConfig.equipmentOrdering = ExtendedConfig.getInteger(nbt, "EquipmentOrdering", ExtendedConfig.equipmentOrdering);
            ExtendedConfig.equipmentDirection = ExtendedConfig.getInteger(nbt, "EquipmentDirection", ExtendedConfig.equipmentDirection);
            ExtendedConfig.equipmentStatus = ExtendedConfig.getInteger(nbt, "EquipmentStatus", ExtendedConfig.equipmentStatus);
            ExtendedConfig.equipmentPosition = ExtendedConfig.getInteger(nbt, "EquipmentPosition", ExtendedConfig.equipmentPosition);
            ExtendedConfig.potionHUDStyle = ExtendedConfig.getInteger(nbt, "PotionHUDStyle", ExtendedConfig.potionHUDStyle);
            ExtendedConfig.potionHUDPosition = ExtendedConfig.getInteger(nbt, "PotionHUDPosition", ExtendedConfig.potionHUDPosition);
            ExtendedConfig.cpsPosition = ExtendedConfig.getInteger(nbt, "CPSPosition", ExtendedConfig.cpsPosition);
            ExtendedConfig.cpsOpacity = ExtendedConfig.getFloat(nbt, "CPSOpacity", ExtendedConfig.cpsOpacity);

            // Movement
            ExtendedConfig.toggleSprint = ExtendedConfig.getBoolean(nbt, "ToggleSprint", ExtendedConfig.toggleSprint);
            ExtendedConfig.toggleSneak = ExtendedConfig.getBoolean(nbt, "ToggleSneak", ExtendedConfig.toggleSneak);

            // Offset
            ExtendedConfig.keystrokeYOffset = ExtendedConfig.getInteger(nbt, "KeystrokeYOffset", ExtendedConfig.keystrokeYOffset);
            ExtendedConfig.armorHUDYOffset = ExtendedConfig.getInteger(nbt, "ArmorHUDYOffset", ExtendedConfig.armorHUDYOffset);
            ExtendedConfig.potionHUDYOffset = ExtendedConfig.getInteger(nbt, "PotionHUDYOffset", ExtendedConfig.potionHUDYOffset);
            ExtendedConfig.maximumPotionDisplay = ExtendedConfig.getInteger(nbt, "MaximumPotionDisplay", ExtendedConfig.maximumPotionDisplay);
            ExtendedConfig.potionLengthYOffset = ExtendedConfig.getInteger(nbt, "PotionLengthYOffset", ExtendedConfig.potionLengthYOffset);
            ExtendedConfig.potionLengthYOffsetOverlap = ExtendedConfig.getInteger(nbt, "PotionLengthYOffsetOverlap", ExtendedConfig.potionLengthYOffsetOverlap);

            // Custom Color
            ExtendedConfig.fpsColor = ExtendedConfig.getString(nbt, "FPSColor", ExtendedConfig.fpsColor);
            ExtendedConfig.xyzColor = ExtendedConfig.getString(nbt, "XYZColor", ExtendedConfig.xyzColor);
            ExtendedConfig.biomeColor = ExtendedConfig.getString(nbt, "BiomeColor", ExtendedConfig.biomeColor);
            ExtendedConfig.directionColor = ExtendedConfig.getString(nbt, "DirectionColor", ExtendedConfig.directionColor);
            ExtendedConfig.pingColor = ExtendedConfig.getString(nbt, "PingColor", ExtendedConfig.pingColor);
            ExtendedConfig.pingToSecondColor = ExtendedConfig.getString(nbt, "PingToSecondColor", ExtendedConfig.pingToSecondColor);
            ExtendedConfig.serverIPColor = ExtendedConfig.getString(nbt, "ServerIPColor", ExtendedConfig.serverIPColor);
            ExtendedConfig.equipmentStatusColor = ExtendedConfig.getString(nbt, "EquipmentStatusColor", ExtendedConfig.equipmentStatusColor);
            ExtendedConfig.arrowCountColor = ExtendedConfig.getString(nbt, "ArrowCountColor", ExtendedConfig.arrowCountColor);
            ExtendedConfig.cpsColor = ExtendedConfig.getString(nbt, "CPSColor", ExtendedConfig.cpsColor);
            ExtendedConfig.rcpsColor = ExtendedConfig.getString(nbt, "RCPSColor", ExtendedConfig.rcpsColor);
            ExtendedConfig.slimeChunkColor = ExtendedConfig.getString(nbt, "SlimeChunkColor", ExtendedConfig.slimeChunkColor);
            ExtendedConfig.topDonatorNameColor = ExtendedConfig.getString(nbt, "TopDonatorNameColor", ExtendedConfig.topDonatorNameColor);
            ExtendedConfig.recentDonatorNameColor = ExtendedConfig.getString(nbt, "RecentDonatorNameColor", ExtendedConfig.recentDonatorNameColor);
            ExtendedConfig.tpsColor = ExtendedConfig.getString(nbt, "TPSColor", ExtendedConfig.tpsColor);
            ExtendedConfig.realTimeColor = ExtendedConfig.getString(nbt, "RealTimeColor", ExtendedConfig.realTimeColor);
            ExtendedConfig.gameTimeColor = ExtendedConfig.getString(nbt, "GameTimeColor", ExtendedConfig.gameTimeColor);
            ExtendedConfig.gameWeatherColor = ExtendedConfig.getString(nbt, "GameWeatherColor", ExtendedConfig.gameWeatherColor);
            ExtendedConfig.moonPhaseColor = ExtendedConfig.getString(nbt, "MoonPhaseColor", ExtendedConfig.moonPhaseColor);

            // Custom Color : Value
            ExtendedConfig.fpsValueColor = ExtendedConfig.getString(nbt, "FPSValueColor", ExtendedConfig.fpsValueColor);
            ExtendedConfig.fps26And49Color = ExtendedConfig.getString(nbt, "FPS26And49Color", ExtendedConfig.fps26And49Color);
            ExtendedConfig.fpsLow25Color = ExtendedConfig.getString(nbt, "FPSLow25Color", ExtendedConfig.fpsLow25Color);
            ExtendedConfig.xyzValueColor = ExtendedConfig.getString(nbt, "XYZValueColor", ExtendedConfig.xyzValueColor);
            ExtendedConfig.biomeValueColor = ExtendedConfig.getString(nbt, "BiomeValueColor", ExtendedConfig.biomeValueColor);
            ExtendedConfig.directionValueColor = ExtendedConfig.getString(nbt, "DirectionValueColor", ExtendedConfig.directionValueColor);
            ExtendedConfig.pingValueColor = ExtendedConfig.getString(nbt, "PingValueColor", ExtendedConfig.pingValueColor);
            ExtendedConfig.ping200And300Color = ExtendedConfig.getString(nbt, "Ping200And300Color", ExtendedConfig.ping200And300Color);
            ExtendedConfig.ping300And500Color = ExtendedConfig.getString(nbt, "Ping300And500Color", ExtendedConfig.ping300And500Color);
            ExtendedConfig.pingMax500Color = ExtendedConfig.getString(nbt, "PingMax500Color", ExtendedConfig.pingMax500Color);
            ExtendedConfig.serverIPValueColor = ExtendedConfig.getString(nbt, "ServerIPValueColor", ExtendedConfig.serverIPValueColor);
            ExtendedConfig.cpsValueColor = ExtendedConfig.getString(nbt, "CPSValueColor", ExtendedConfig.cpsValueColor);
            ExtendedConfig.rcpsValueColor = ExtendedConfig.getString(nbt, "RCPSValueColor", ExtendedConfig.rcpsValueColor);
            ExtendedConfig.slimeChunkValueColor = ExtendedConfig.getString(nbt, "SlimeChunkValueColor", ExtendedConfig.slimeChunkValueColor);
            ExtendedConfig.topDonatorValueColor = ExtendedConfig.getString(nbt, "TopDonatorValueColor", ExtendedConfig.topDonatorValueColor);
            ExtendedConfig.recentDonatorValueColor = ExtendedConfig.getString(nbt, "RecentDonatorValueColor", ExtendedConfig.recentDonatorValueColor);
            ExtendedConfig.tpsValueColor = ExtendedConfig.getString(nbt, "TPSValueColor", ExtendedConfig.tpsValueColor);
            ExtendedConfig.realTimeHHMMSSValueColor = ExtendedConfig.getString(nbt, "RealTimeHHMMSSValueColor", ExtendedConfig.realTimeHHMMSSValueColor);
            ExtendedConfig.realTimeDDMMYYValueColor = ExtendedConfig.getString(nbt, "RealTimeDDMMYYValueColor", ExtendedConfig.realTimeDDMMYYValueColor);
            ExtendedConfig.gameTimeValueColor = ExtendedConfig.getString(nbt, "GameTimeValueColor", ExtendedConfig.gameTimeValueColor);
            ExtendedConfig.gameWeatherValueColor = ExtendedConfig.getString(nbt, "GameWeatherValueColor", ExtendedConfig.gameWeatherValueColor);
            ExtendedConfig.moonPhaseValueColor = ExtendedConfig.getString(nbt, "MoonPhaseValueColor", ExtendedConfig.moonPhaseValueColor);

            // Custom Color : Keystroke
            ExtendedConfig.keystrokeWASDColor = ExtendedConfig.getString(nbt, "KeystrokeWASDColor", ExtendedConfig.keystrokeWASDColor);
            ExtendedConfig.keystrokeMouseButtonColor = ExtendedConfig.getString(nbt, "KeystrokeMouseButtonColor", ExtendedConfig.keystrokeMouseButtonColor);
            ExtendedConfig.keystrokeSprintColor = ExtendedConfig.getString(nbt, "KeystrokeSprintColor", ExtendedConfig.keystrokeSprintColor);
            ExtendedConfig.keystrokeSneakColor = ExtendedConfig.getString(nbt, "KeystrokeSneakColor", ExtendedConfig.keystrokeSneakColor);
            ExtendedConfig.keystrokeBlockingColor = ExtendedConfig.getString(nbt, "KeystrokeBlockingColor", ExtendedConfig.keystrokeBlockingColor);
            ExtendedConfig.keystrokeCPSColor = ExtendedConfig.getString(nbt, "KeystrokeCPSColor", ExtendedConfig.keystrokeCPSColor);
            ExtendedConfig.keystrokeRCPSColor = ExtendedConfig.getString(nbt, "KeystrokeRCPSColor", ExtendedConfig.keystrokeRCPSColor);
            ExtendedConfig.keystrokeWASDRainbow = ExtendedConfig.getBoolean(nbt, "KeystrokeWASDRainbow", ExtendedConfig.keystrokeWASDRainbow);
            ExtendedConfig.keystrokeMouseButtonRainbow = ExtendedConfig.getBoolean(nbt, "KeystrokeMouseButtonRainbow", ExtendedConfig.keystrokeMouseButtonRainbow);
            ExtendedConfig.keystrokeSprintRainbow = ExtendedConfig.getBoolean(nbt, "KeystrokeSprintRainbow", ExtendedConfig.keystrokeSprintRainbow);
            ExtendedConfig.keystrokeSneakRainbow = ExtendedConfig.getBoolean(nbt, "KeystrokeSneakRainbow", ExtendedConfig.keystrokeSneakRainbow);
            ExtendedConfig.keystrokeBlockingRainbow = ExtendedConfig.getBoolean(nbt, "KeystrokeBlockingRainbow", ExtendedConfig.keystrokeBlockingRainbow);
            ExtendedConfig.keystrokeCPSRainbow = ExtendedConfig.getBoolean(nbt, "KeystrokeCPSRainbow", ExtendedConfig.keystrokeCPSRainbow);
            ExtendedConfig.keystrokeRCPSRainbow = ExtendedConfig.getBoolean(nbt, "KeystrokeRCPSRainbow", ExtendedConfig.keystrokeRCPSRainbow);

            // Misc
            ExtendedConfig.toggleSprintUseMode = ExtendedConfig.getString(nbt, "ToggleSprintUseMode", ExtendedConfig.toggleSprintUseMode);
            ExtendedConfig.toggleSneakUseMode = ExtendedConfig.getString(nbt, "ToggleSneakUseMode", ExtendedConfig.toggleSneakUseMode);
            ExtendedConfig.cpsCustomXOffset = ExtendedConfig.getInteger(nbt, "CPSCustomOffsetX", ExtendedConfig.cpsCustomXOffset);
            ExtendedConfig.cpsCustomYOffset = ExtendedConfig.getInteger(nbt, "CPSCustomOffsetY", ExtendedConfig.cpsCustomYOffset);
            ExtendedConfig.selectedHypixelMinigame = ExtendedConfig.getInteger(nbt, "SelectedHypixelMinigame", ExtendedConfig.selectedHypixelMinigame);
            ExtendedConfig.slimeChunkSeed = ExtendedConfig.getLong(nbt, "SlimeChunkSeed", ExtendedConfig.slimeChunkSeed);
            ExtendedConfig.topDonatorFilePath = ExtendedConfig.getString(nbt, "TopDonatorFilePath", ExtendedConfig.topDonatorFilePath);
            ExtendedConfig.recentDonatorFilePath = ExtendedConfig.getString(nbt, "RecentDonatorFilePath", ExtendedConfig.recentDonatorFilePath);
            ExtendedConfig.topDonatorText = ExtendedConfig.getString(nbt, "TopDonatorText", ExtendedConfig.topDonatorText);
            ExtendedConfig.recentDonatorText = ExtendedConfig.getString(nbt, "RecentDonatorText", ExtendedConfig.recentDonatorText);
            ExtendedConfig.hypixelNickName = ExtendedConfig.getString(nbt, "HypixelNickName", ExtendedConfig.hypixelNickName);
            ExtendedConfig.realmsMessage = ExtendedConfig.getString(nbt, "RealmsMessage", ExtendedConfig.realmsMessage);

            ExtendedConfig.readAutoLoginData(nbt.getTagList("AutoLoginData", 10));
            HideNameData.load(nbt.getTagList("HideNameList", 10));

            ModLogger.info("Loading extended config {}", ExtendedConfig.file.getPath());
        }
        catch (Exception e) {}
    }

    public static void save()
    {
        save(!ExtendedConfig.currentProfile.isEmpty() ? ExtendedConfig.currentProfile : "default");
    }

    public static void save(String profileName)
    {
        try
        {
            NBTTagCompound nbt = new NBTTagCompound();

            // Render Info
            nbt.setBoolean("FPS", ExtendedConfig.fps);
            nbt.setBoolean("XYZ", ExtendedConfig.xyz);
            nbt.setBoolean("Direction", ExtendedConfig.direction);
            nbt.setBoolean("Biome", ExtendedConfig.biome);
            nbt.setBoolean("Ping", ExtendedConfig.ping);
            nbt.setBoolean("PingToSecond", ExtendedConfig.pingToSecond);
            nbt.setBoolean("ServerIP", ExtendedConfig.serverIP);
            nbt.setBoolean("ServerIPMCVersion", ExtendedConfig.serverIPMCVersion);
            nbt.setBoolean("EquipmentHUD", ExtendedConfig.equipmentHUD);
            nbt.setBoolean("PotionHUD", ExtendedConfig.potionHUD);
            nbt.setBoolean("Keystroke", ExtendedConfig.keystroke);
            nbt.setBoolean("KeystrokeMouse", ExtendedConfig.keystrokeMouse);
            nbt.setBoolean("KeystrokeSprintSneak", ExtendedConfig.keystrokeSprintSneak);
            nbt.setBoolean("KeystrokeBlocking", ExtendedConfig.keystrokeBlocking);
            nbt.setBoolean("CPS", ExtendedConfig.cps);
            nbt.setBoolean("RCPS", ExtendedConfig.rcps);
            nbt.setBoolean("SlimeChunkFinder", ExtendedConfig.slimeChunkFinder);
            nbt.setBoolean("RealTime", ExtendedConfig.realTime);
            nbt.setBoolean("GameTime", ExtendedConfig.gameTime);
            nbt.setBoolean("GameWeather", ExtendedConfig.gameWeather);
            nbt.setBoolean("MoonPhase", ExtendedConfig.moonPhase);
            nbt.setBoolean("PotionHUDIcon", ExtendedConfig.potionHUDIcon);
            nbt.setBoolean("TPS", ExtendedConfig.tps);
            nbt.setBoolean("TPSAllDimensions", ExtendedConfig.tpsAllDims);
            nbt.setBoolean("AlternatePotionHUDTextColor", ExtendedConfig.alternatePotionHUDTextColor);

            // Main
            nbt.setBoolean("ShowCustomCape", ExtendedConfig.showCustomCape);
            nbt.setBoolean("SwapRenderInfo", ExtendedConfig.swapRenderInfo);
            nbt.setInteger("HealthStatusMode", ExtendedConfig.healthStatusMode);
            nbt.setInteger("KeystrokePosition", ExtendedConfig.keystrokePosition);
            nbt.setInteger("EquipmentOrdering", ExtendedConfig.equipmentOrdering);
            nbt.setInteger("EquipmentDirection", ExtendedConfig.equipmentDirection);
            nbt.setInteger("EquipmentStatus", ExtendedConfig.equipmentStatus);
            nbt.setInteger("EquipmentPosition", ExtendedConfig.equipmentPosition);
            nbt.setInteger("PotionHUDStyle", ExtendedConfig.potionHUDStyle);
            nbt.setInteger("PotionHUDPosition", ExtendedConfig.potionHUDPosition);
            nbt.setInteger("CPSPosition", ExtendedConfig.cpsPosition);
            nbt.setFloat("CPSOpacity", ExtendedConfig.cpsOpacity);

            // Movement
            nbt.setBoolean("ToggleSprint", ExtendedConfig.toggleSprint);
            nbt.setBoolean("ToggleSneak", ExtendedConfig.toggleSneak);

            // Offset
            nbt.setInteger("KeystrokeYOffset", ExtendedConfig.keystrokeYOffset);
            nbt.setInteger("ArmorHUDYOffset", ExtendedConfig.armorHUDYOffset);
            nbt.setInteger("PotionHUDYOffset", ExtendedConfig.potionHUDYOffset);
            nbt.setInteger("MaximumPotionDisplay", ExtendedConfig.maximumPotionDisplay);
            nbt.setInteger("PotionLengthYOffset", ExtendedConfig.potionLengthYOffset);
            nbt.setInteger("PotionLengthYOffsetOverlap", ExtendedConfig.potionLengthYOffsetOverlap);

            // Custom Color
            nbt.setString("FPSColor", ExtendedConfig.fpsColor);
            nbt.setString("XYZColor", ExtendedConfig.xyzColor);
            nbt.setString("BiomeColor", ExtendedConfig.biomeColor);
            nbt.setString("DirectionColor", ExtendedConfig.directionColor);
            nbt.setString("PingColor", ExtendedConfig.pingColor);
            nbt.setString("PingToSecondColor", ExtendedConfig.pingToSecondColor);
            nbt.setString("ServerIPColor", ExtendedConfig.serverIPColor);
            nbt.setString("EquipmentStatusColor", ExtendedConfig.equipmentStatusColor);
            nbt.setString("ArrowCountColor", ExtendedConfig.arrowCountColor);
            nbt.setString("CPSColor", ExtendedConfig.cpsColor);
            nbt.setString("RCPSColor", ExtendedConfig.rcpsColor);
            nbt.setString("SlimeChunkColor", ExtendedConfig.slimeChunkColor);
            nbt.setString("TopDonatorNameColor", ExtendedConfig.topDonatorNameColor);
            nbt.setString("RecentDonatorNameColor", ExtendedConfig.recentDonatorNameColor);
            nbt.setString("TPSColor", ExtendedConfig.tpsColor);
            nbt.setString("RealTimeColor", ExtendedConfig.realTimeColor);
            nbt.setString("GameTimeColor", ExtendedConfig.gameTimeColor);
            nbt.setString("GameWeatherColor", ExtendedConfig.gameWeatherColor);
            nbt.setString("MoonPhaseColor", ExtendedConfig.moonPhaseColor);

            // Custom Color : Value
            nbt.setString("FPSValueColor", ExtendedConfig.fpsValueColor);
            nbt.setString("FPS26And49Color", ExtendedConfig.fps26And49Color);
            nbt.setString("FPSLow25Color", ExtendedConfig.fpsLow25Color);
            nbt.setString("XYZValueColor", ExtendedConfig.xyzValueColor);
            nbt.setString("BiomeValueColor", ExtendedConfig.biomeValueColor);
            nbt.setString("DirectionValueColor", ExtendedConfig.directionValueColor);
            nbt.setString("PingValueColor", ExtendedConfig.pingValueColor);
            nbt.setString("Ping200And300Color", ExtendedConfig.ping200And300Color);
            nbt.setString("Ping300And500Color", ExtendedConfig.ping300And500Color);
            nbt.setString("PingMax500Color", ExtendedConfig.pingMax500Color);
            nbt.setString("ServerIPValueColor", ExtendedConfig.serverIPValueColor);
            nbt.setString("CPSValueColor", ExtendedConfig.cpsValueColor);
            nbt.setString("RCPSValueColor", ExtendedConfig.rcpsValueColor);
            nbt.setString("SlimeChunkValueColor", ExtendedConfig.slimeChunkValueColor);
            nbt.setString("TopDonatorValueColor", ExtendedConfig.topDonatorValueColor);
            nbt.setString("RecentDonatorValueColor", ExtendedConfig.recentDonatorValueColor);
            nbt.setString("TPSValueColor", ExtendedConfig.tpsValueColor);
            nbt.setString("RealTimeHHMMSSValueColor", ExtendedConfig.realTimeHHMMSSValueColor);
            nbt.setString("RealTimeDDMMYYValueColor", ExtendedConfig.realTimeDDMMYYValueColor);
            nbt.setString("GameTimeValueColor", ExtendedConfig.gameTimeValueColor);
            nbt.setString("GameWeatherValueColor", ExtendedConfig.gameWeatherValueColor);
            nbt.setString("MoonPhaseValueColor", ExtendedConfig.moonPhaseValueColor);

            // Custom Color : Keystroke
            nbt.setString("KeystrokeWASDColor", ExtendedConfig.keystrokeWASDColor);
            nbt.setString("KeystrokeMouseButtonColor", ExtendedConfig.keystrokeMouseButtonColor);
            nbt.setString("KeystrokeSprintColor", ExtendedConfig.keystrokeSprintColor);
            nbt.setString("KeystrokeSneakColor", ExtendedConfig.keystrokeSneakColor);
            nbt.setString("KeystrokeBlockingColor", ExtendedConfig.keystrokeBlockingColor);
            nbt.setString("KeystrokeCPSColor", ExtendedConfig.keystrokeCPSColor);
            nbt.setString("KeystrokeRCPSColor", ExtendedConfig.keystrokeRCPSColor);
            nbt.setBoolean("KeystrokeWASDRainbow", ExtendedConfig.keystrokeWASDRainbow);
            nbt.setBoolean("KeystrokeMouseButtonRainbow", ExtendedConfig.keystrokeMouseButtonRainbow);
            nbt.setBoolean("KeystrokeSprintRainbow", ExtendedConfig.keystrokeSprintRainbow);
            nbt.setBoolean("KeystrokeSneakRainbow", ExtendedConfig.keystrokeSneakRainbow);
            nbt.setBoolean("KeystrokeBlockingRainbow", ExtendedConfig.keystrokeBlockingRainbow);
            nbt.setBoolean("KeystrokeCPSRainbow", ExtendedConfig.keystrokeCPSRainbow);
            nbt.setBoolean("KeystrokeRCPSRainbow", ExtendedConfig.keystrokeRCPSRainbow);

            // Misc
            nbt.setString("ToggleSprintUseMode", ExtendedConfig.toggleSprintUseMode);
            nbt.setString("ToggleSneakUseMode", ExtendedConfig.toggleSneakUseMode);
            nbt.setInteger("CPSCustomOffsetX", ExtendedConfig.cpsCustomXOffset);
            nbt.setInteger("CPSCustomOffsetY", ExtendedConfig.cpsCustomYOffset);
            nbt.setInteger("SelectedHypixelMinigame", ExtendedConfig.selectedHypixelMinigame);
            nbt.setLong("SlimeChunkSeed", ExtendedConfig.slimeChunkSeed);
            nbt.setString("TopDonatorFilePath", ExtendedConfig.topDonatorFilePath);
            nbt.setString("RecentDonatorFilePath", ExtendedConfig.recentDonatorFilePath);
            nbt.setString("TopDonatorText", ExtendedConfig.topDonatorText);
            nbt.setString("RecentDonatorText", ExtendedConfig.recentDonatorText);
            nbt.setString("HypixelNickName", ExtendedConfig.hypixelNickName);
            nbt.setString("RealmsMessage", ExtendedConfig.realmsMessage);

            nbt.setTag("AutoLoginData", ExtendedConfig.writeAutoLoginData());
            nbt.setTag("HideNameList", HideNameData.save());

            CompressedStreamTools.safeWrite(nbt, !profileName.equalsIgnoreCase("default") ? new File(userDir, profileName + ".dat") : ExtendedConfig.file);
        }
        catch (Exception e) {}
    }

    public static void saveProfileFile(String profileName)
    {
        File profile = new File(ExtendedConfig.userDir, "profile.txt");

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(profile), StandardCharsets.UTF_8)))
        {
            writer.println("profile:" + profileName);
            ModLogger.info("Saving profile name!");
        }
        catch (IOException e)
        {
            ModLogger.error("Failed to save profiles", (Throwable)e);
        }
    }

    private static NBTTagList writeAutoLoginData()
    {
        NBTTagList list = new NBTTagList();

        ExtendedConfig.loginData.getAutoLoginList().forEach(data ->
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("ServerIP", data.getServerIP());
            nbt.setString("CommandName", data.getCommand());
            nbt.setString("Value", data.getValue());
            nbt.setString("UUID", data.getUUID().toString());
            nbt.setString("Function", data.getFunction());
            list.appendTag(nbt);
        });
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

    public String getKeyBinding(ExtendedConfig.Options options)
    {
        String name = LangUtils.translate(options.getTranslation()) + ": ";

        if (options.isFloat())
        {
            float value = this.getOptionFloatValue(options);
            return name + (int)value;
        }
        else if (options.isBoolean())
        {
            boolean flag = this.getOptionOrdinalValue(options);
            return flag ? name + TextFormatting.GREEN + "true" : name + TextFormatting.RED + "false";
        }
        else if (options.isTextbox())
        {
            return this.getOptionStringValue(options);
        }
        else if (options == ExtendedConfig.Options.HEALTH_STATUS)
        {
            return name + this.getTranslation(HEALTH_STATUS, ExtendedConfig.healthStatusMode);
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_POSITION)
        {
            return name + this.getTranslation(POSITION, ExtendedConfig.keystrokePosition);
        }
        else if (options == ExtendedConfig.Options.EQUIPMENT_ORDERING)
        {
            return name + this.getTranslation(EQUIPMENT_ORDERING, ExtendedConfig.equipmentOrdering);
        }
        else if (options == ExtendedConfig.Options.EQUIPMENT_DIRECTION)
        {
            return name + this.getTranslation(EQUIPMENT_DIRECTION, ExtendedConfig.equipmentDirection);
        }
        else if (options == ExtendedConfig.Options.EQUIPMENT_STATUS)
        {
            return name + this.getTranslation(EQUIPMENT_STATUS, ExtendedConfig.equipmentStatus);
        }
        else if (options == ExtendedConfig.Options.EQUIPMENT_POSITION)
        {
            return name + this.getTranslation(EQUIPMENT_POSITION, ExtendedConfig.equipmentPosition);
        }
        else if (options == ExtendedConfig.Options.POTION_HUD_STYLE)
        {
            return name + this.getTranslation(POTION_STATUS_HUD_STYLE, ExtendedConfig.potionHUDStyle);
        }
        else if (options == ExtendedConfig.Options.POTION_HUD_POSITION)
        {
            return name + this.getTranslation(POTION_STATUS_HUD_POSITION, ExtendedConfig.potionHUDPosition);
        }
        else if (options == ExtendedConfig.Options.CPS_POSITION)
        {
            return name + this.getTranslation(CPS_POSITION, ExtendedConfig.cpsPosition);
        }
        else
        {
            return name;
        }
    }

    public void setOptionValue(ExtendedConfig.Options options, int value)
    {
        if (options == ExtendedConfig.Options.PREVIEW)
        {
            GuiExtendedConfig.preview = !GuiExtendedConfig.preview;
        }
        if (options == ExtendedConfig.Options.SWAP_INFO_POS)
        {
            ExtendedConfig.swapRenderInfo = !ExtendedConfig.swapRenderInfo;
        }
        if (options == ExtendedConfig.Options.HEALTH_STATUS)
        {
            ExtendedConfig.healthStatusMode = (ExtendedConfig.healthStatusMode + value) % 3;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_POSITION)
        {
            ExtendedConfig.keystrokePosition = (ExtendedConfig.keystrokePosition + value) % 2;
        }
        if (options == ExtendedConfig.Options.EQUIPMENT_ORDERING)
        {
            ExtendedConfig.equipmentOrdering = (ExtendedConfig.equipmentOrdering + value) % 2;
        }
        if (options == ExtendedConfig.Options.EQUIPMENT_DIRECTION)
        {
            ExtendedConfig.equipmentDirection = (ExtendedConfig.equipmentDirection + value) % 2;
        }
        if (options == ExtendedConfig.Options.EQUIPMENT_STATUS)
        {
            ExtendedConfig.equipmentStatus = (ExtendedConfig.equipmentStatus + value) % 4;
        }
        if (options == ExtendedConfig.Options.EQUIPMENT_POSITION)
        {
            ExtendedConfig.equipmentPosition = (ExtendedConfig.equipmentPosition + value) % 3;
        }
        if (options == ExtendedConfig.Options.POTION_HUD_STYLE)
        {
            ExtendedConfig.potionHUDStyle = (ExtendedConfig.potionHUDStyle + value) % 2;
        }
        if (options == ExtendedConfig.Options.POTION_HUD_POSITION)
        {
            ExtendedConfig.potionHUDPosition = (ExtendedConfig.potionHUDPosition + value) % 4;
        }
        if (options == ExtendedConfig.Options.CPS_POSITION)
        {
            ExtendedConfig.cpsPosition = (ExtendedConfig.cpsPosition + value) % 4;
        }

        if (options == ExtendedConfig.Options.FPS)
        {
            ExtendedConfig.fps = !ExtendedConfig.fps;
        }
        if (options == ExtendedConfig.Options.XYZ)
        {
            ExtendedConfig.xyz = !ExtendedConfig.xyz;
        }
        if (options == ExtendedConfig.Options.DIRECTION)
        {
            ExtendedConfig.direction = !ExtendedConfig.direction;
        }
        if (options == ExtendedConfig.Options.BIOME)
        {
            ExtendedConfig.biome = !ExtendedConfig.biome;
        }
        if (options == ExtendedConfig.Options.PING)
        {
            ExtendedConfig.ping = !ExtendedConfig.ping;
        }
        if (options == ExtendedConfig.Options.PING_TO_SECOND)
        {
            ExtendedConfig.pingToSecond = !ExtendedConfig.pingToSecond;
        }
        if (options == ExtendedConfig.Options.SERVER_IP)
        {
            ExtendedConfig.serverIP = !ExtendedConfig.serverIP;
        }
        if (options == ExtendedConfig.Options.SERVER_IP_MC)
        {
            ExtendedConfig.serverIPMCVersion = !ExtendedConfig.serverIPMCVersion;
        }
        if (options == ExtendedConfig.Options.EQUIPMENT_HUD)
        {
            ExtendedConfig.equipmentHUD = !ExtendedConfig.equipmentHUD;
        }
        if (options == ExtendedConfig.Options.POTION_HUD)
        {
            ExtendedConfig.potionHUD = !ExtendedConfig.potionHUD;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE)
        {
            ExtendedConfig.keystroke = !ExtendedConfig.keystroke;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_LRMB)
        {
            ExtendedConfig.keystrokeMouse = !ExtendedConfig.keystrokeMouse;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_SS)
        {
            ExtendedConfig.keystrokeSprintSneak = !ExtendedConfig.keystrokeSprintSneak;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_BLOCKING)
        {
            ExtendedConfig.keystrokeBlocking = !ExtendedConfig.keystrokeBlocking;
        }
        if (options == ExtendedConfig.Options.CPS)
        {
            ExtendedConfig.cps = !ExtendedConfig.cps;
        }
        if (options == ExtendedConfig.Options.RCPS)
        {
            ExtendedConfig.rcps = !ExtendedConfig.rcps;
        }
        if (options == ExtendedConfig.Options.SLIME_CHUNK)
        {
            ExtendedConfig.slimeChunkFinder = !ExtendedConfig.slimeChunkFinder;
        }
        if (options == ExtendedConfig.Options.REAL_TIME)
        {
            ExtendedConfig.realTime = !ExtendedConfig.realTime;
        }
        if (options == ExtendedConfig.Options.GAME_TIME)
        {
            ExtendedConfig.gameTime = !ExtendedConfig.gameTime;
        }
        if (options == ExtendedConfig.Options.GAME_WEATHER)
        {
            ExtendedConfig.gameWeather = !ExtendedConfig.gameWeather;
        }
        if (options == ExtendedConfig.Options.MOON_PHASE)
        {
            ExtendedConfig.moonPhase = !ExtendedConfig.moonPhase;
        }
        if (options == ExtendedConfig.Options.POTION_ICON)
        {
            ExtendedConfig.potionHUDIcon = !ExtendedConfig.potionHUDIcon;
        }
        if (options == ExtendedConfig.Options.TPS)
        {
            ExtendedConfig.tps = !ExtendedConfig.tps;
        }
        if (options == ExtendedConfig.Options.TPS_ALL_DIMS)
        {
            ExtendedConfig.tpsAllDims = !ExtendedConfig.tpsAllDims;
        }
        if (options == ExtendedConfig.Options.ALTERNATE_POTION_COLOR)
        {
            ExtendedConfig.alternatePotionHUDTextColor = !ExtendedConfig.alternatePotionHUDTextColor;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_WASD_RAINBOW)
        {
            ExtendedConfig.keystrokeWASDRainbow = !ExtendedConfig.keystrokeWASDRainbow;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_MOUSE_BUTTON_RAINBOW)
        {
            ExtendedConfig.keystrokeMouseButtonRainbow = !ExtendedConfig.keystrokeMouseButtonRainbow;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_SPRINT_RAINBOW)
        {
            ExtendedConfig.keystrokeSprintRainbow = !ExtendedConfig.keystrokeSprintRainbow;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_SNEAK_RAINBOW)
        {
            ExtendedConfig.keystrokeSneakRainbow = !ExtendedConfig.keystrokeSneakRainbow;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_BLOCKING_RAINBOW)
        {
            ExtendedConfig.keystrokeBlockingRainbow = !ExtendedConfig.keystrokeBlockingRainbow;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_CPS_RAINBOW)
        {
            ExtendedConfig.keystrokeCPSRainbow = !ExtendedConfig.keystrokeCPSRainbow;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_RCPS_RAINBOW)
        {
            ExtendedConfig.keystrokeRCPSRainbow = !ExtendedConfig.keystrokeRCPSRainbow;
        }
    }

    public void setOptionFloatValue(ExtendedConfig.Options options, float value)
    {
        if (options == ExtendedConfig.Options.ARMOR_HUD_Y)
        {
            ExtendedConfig.armorHUDYOffset = (int) value;
        }
        if (options == ExtendedConfig.Options.POTION_HUD_Y)
        {
            ExtendedConfig.potionHUDYOffset = (int) value;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_Y)
        {
            ExtendedConfig.keystrokeYOffset = (int) value;
        }
        if (options == ExtendedConfig.Options.MAXIMUM_POTION_DISPLAY)
        {
            ExtendedConfig.maximumPotionDisplay = (int) value;
        }
        if (options == ExtendedConfig.Options.POTION_LENGTH_Y_OFFSET)
        {
            ExtendedConfig.potionLengthYOffset = (int) value;
        }
        if (options == ExtendedConfig.Options.POTION_LENGTH_Y_OFFSET_OVERLAP)
        {
            ExtendedConfig.potionLengthYOffsetOverlap = (int) value;
        }
        if (options == ExtendedConfig.Options.CPS_OPACITY)
        {
            ExtendedConfig.cpsOpacity = value;
        }
    }

    public void setOptionStringValue(ExtendedConfig.Options options, String value)
    {
        if (options == ExtendedConfig.Options.FPS_COLOR)
        {
            ExtendedConfig.fpsColor = value;
        }
        if (options == ExtendedConfig.Options.XYZ_COLOR)
        {
            ExtendedConfig.xyzColor = value;
        }
        if (options == ExtendedConfig.Options.BIOME_COLOR)
        {
            ExtendedConfig.biomeColor = value;
        }
        if (options == ExtendedConfig.Options.DIRECTION_COLOR)
        {
            ExtendedConfig.directionColor = value;
        }
        if (options == ExtendedConfig.Options.PING_COLOR)
        {
            ExtendedConfig.pingColor = value;
        }
        if (options == ExtendedConfig.Options.PING_TO_SECOND_COLOR)
        {
            ExtendedConfig.pingToSecondColor = value;
        }
        if (options == ExtendedConfig.Options.SERVER_IP_COLOR)
        {
            ExtendedConfig.serverIPColor = value;
        }
        if (options == ExtendedConfig.Options.EQUIPMENT_STATUS_COLOR)
        {
            ExtendedConfig.equipmentStatusColor = value;
        }
        if (options == ExtendedConfig.Options.ARROW_COUNT_COLOR)
        {
            ExtendedConfig.arrowCountColor = value;
        }
        if (options == ExtendedConfig.Options.CPS_COLOR)
        {
            ExtendedConfig.cpsColor = value;
        }
        if (options == ExtendedConfig.Options.RCPS_COLOR)
        {
            ExtendedConfig.rcpsColor = value;
        }
        if (options == ExtendedConfig.Options.SLIME_CHUNK_COLOR)
        {
            ExtendedConfig.slimeChunkColor = value;
        }
        if (options == ExtendedConfig.Options.TOP_DONATOR_NAME_COLOR)
        {
            ExtendedConfig.topDonatorNameColor = value;
        }
        if (options == ExtendedConfig.Options.RECENT_DONATOR_NAME_COLOR)
        {
            ExtendedConfig.recentDonatorNameColor = value;
        }
        if (options == ExtendedConfig.Options.TPS_COLOR)
        {
            ExtendedConfig.tpsColor = value;
        }
        if (options == ExtendedConfig.Options.REAL_TIME_COLOR)
        {
            ExtendedConfig.realTimeColor = value;
        }
        if (options == ExtendedConfig.Options.GAME_TIME_COLOR)
        {
            ExtendedConfig.gameTimeColor = value;
        }
        if (options == ExtendedConfig.Options.GAME_WEATHER_COLOR)
        {
            ExtendedConfig.gameWeatherColor = value;
        }
        if (options == ExtendedConfig.Options.MOON_PHASE_COLOR)
        {
            ExtendedConfig.moonPhaseColor = value;
        }

        if (options == ExtendedConfig.Options.FPS_VALUE_COLOR)
        {
            ExtendedConfig.fpsValueColor = value;
        }
        if (options == ExtendedConfig.Options.FPS_26_AND_40_COLOR)
        {
            ExtendedConfig.fps26And49Color = value;
        }
        if (options == ExtendedConfig.Options.FPS_LOW_25_COLOR)
        {
            ExtendedConfig.fpsLow25Color = value;
        }
        if (options == ExtendedConfig.Options.XYZ_VALUE_COLOR)
        {
            ExtendedConfig.xyzValueColor = value;
        }
        if (options == ExtendedConfig.Options.DIRECTION_VALUE_COLOR)
        {
            ExtendedConfig.directionValueColor = value;
        }
        if (options == ExtendedConfig.Options.BIOME_VALUE_COLOR)
        {
            ExtendedConfig.biomeValueColor = value;
        }
        if (options == ExtendedConfig.Options.PING_VALUE_COLOR)
        {
            ExtendedConfig.pingValueColor = value;
        }
        if (options == ExtendedConfig.Options.PING_200_AND_300_COLOR)
        {
            ExtendedConfig.ping200And300Color = value;
        }
        if (options == ExtendedConfig.Options.PING_300_AND_500_COLOR)
        {
            ExtendedConfig.ping300And500Color = value;
        }
        if (options == ExtendedConfig.Options.PING_MAX_500_COLOR)
        {
            ExtendedConfig.pingMax500Color = value;
        }
        if (options == ExtendedConfig.Options.SERVER_IP_VALUE_COLOR)
        {
            ExtendedConfig.serverIPValueColor = value;
        }
        if (options == ExtendedConfig.Options.CPS_VALUE_COLOR)
        {
            ExtendedConfig.cpsValueColor = value;
        }
        if (options == ExtendedConfig.Options.RCPS_VALUE_COLOR)
        {
            ExtendedConfig.rcpsValueColor = value;
        }
        if (options == ExtendedConfig.Options.SLIME_CHUNK_VALUE_COLOR)
        {
            ExtendedConfig.slimeChunkValueColor = value;
        }
        if (options == ExtendedConfig.Options.TOP_DONATOR_VALUE_COLOR)
        {
            ExtendedConfig.topDonatorValueColor = value;
        }
        if (options == ExtendedConfig.Options.RECENT_DONATOR_VALUE_COLOR)
        {
            ExtendedConfig.recentDonatorValueColor = value;
        }
        if (options == ExtendedConfig.Options.TPS_VALUE_COLOR)
        {
            ExtendedConfig.tpsValueColor = value;
        }
        if (options == ExtendedConfig.Options.REAL_TIME_HHMMSS_VALUE_COLOR)
        {
            ExtendedConfig.realTimeHHMMSSValueColor = value;
        }
        if (options == ExtendedConfig.Options.REAL_TIME_DDMMYY_VALUE_COLOR)
        {
            ExtendedConfig.realTimeDDMMYYValueColor = value;
        }
        if (options == ExtendedConfig.Options.GAME_TIME_VALUE_COLOR)
        {
            ExtendedConfig.gameTimeValueColor = value;
        }
        if (options == ExtendedConfig.Options.GAME_WEATHER_VALUE_COLOR)
        {
            ExtendedConfig.gameWeatherValueColor = value;
        }
        if (options == ExtendedConfig.Options.MOON_PHASE_VALUE_COLOR)
        {
            ExtendedConfig.moonPhaseValueColor = value;
        }

        if (options == ExtendedConfig.Options.KEYSTROKE_WASD_COLOR)
        {
            ExtendedConfig.keystrokeWASDColor = value;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_MOUSE_BUTTON_COLOR)
        {
            ExtendedConfig.keystrokeMouseButtonColor = value;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_SPRINT_COLOR)
        {
            ExtendedConfig.keystrokeSprintColor = value;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_SNEAK_COLOR)
        {
            ExtendedConfig.keystrokeSneakColor = value;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_BLOCKING_COLOR)
        {
            ExtendedConfig.keystrokeBlockingColor = value;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_CPS_COLOR)
        {
            ExtendedConfig.keystrokeCPSColor = value;
        }
        if (options == ExtendedConfig.Options.KEYSTROKE_RCPS_COLOR)
        {
            ExtendedConfig.keystrokeRCPSColor = value;
        }
    }

    public float getOptionFloatValue(ExtendedConfig.Options settingOption)
    {
        if (settingOption == ExtendedConfig.Options.ARMOR_HUD_Y)
        {
            return ExtendedConfig.armorHUDYOffset;
        }
        if (settingOption == ExtendedConfig.Options.POTION_HUD_Y)
        {
            return ExtendedConfig.potionHUDYOffset;
        }
        if (settingOption == ExtendedConfig.Options.KEYSTROKE_Y)
        {
            return ExtendedConfig.keystrokeYOffset;
        }
        if (settingOption == ExtendedConfig.Options.MAXIMUM_POTION_DISPLAY)
        {
            return ExtendedConfig.maximumPotionDisplay;
        }
        if (settingOption == ExtendedConfig.Options.POTION_LENGTH_Y_OFFSET)
        {
            return ExtendedConfig.potionLengthYOffset;
        }
        if (settingOption == ExtendedConfig.Options.POTION_LENGTH_Y_OFFSET_OVERLAP)
        {
            return ExtendedConfig.potionLengthYOffsetOverlap;
        }
        if (settingOption == ExtendedConfig.Options.CPS_OPACITY)
        {
            return ExtendedConfig.cpsOpacity;
        }
        return 0.0F;
    }

    public boolean getOptionOrdinalValue(ExtendedConfig.Options options)
    {
        switch (options)
        {
        case PREVIEW:
            return GuiExtendedConfig.preview;
        case SWAP_INFO_POS:
            return ExtendedConfig.swapRenderInfo;
        case FPS:
            return ExtendedConfig.fps;
        case XYZ:
            return ExtendedConfig.xyz;
        case DIRECTION:
            return ExtendedConfig.direction;
        case BIOME:
            return ExtendedConfig.biome;
        case PING:
            return ExtendedConfig.ping;
        case PING_TO_SECOND:
            return ExtendedConfig.pingToSecond;
        case SERVER_IP:
            return ExtendedConfig.serverIP;
        case SERVER_IP_MC:
            return ExtendedConfig.serverIPMCVersion;
        case EQUIPMENT_HUD:
            return ExtendedConfig.equipmentHUD;
        case POTION_HUD:
            return ExtendedConfig.potionHUD;
        case KEYSTROKE:
            return ExtendedConfig.keystroke;
        case KEYSTROKE_LRMB:
            return ExtendedConfig.keystrokeMouse;
        case KEYSTROKE_SS:
            return ExtendedConfig.keystrokeSprintSneak;
        case KEYSTROKE_BLOCKING:
            return ExtendedConfig.keystrokeBlocking;
        case CPS:
            return ExtendedConfig.cps;
        case RCPS:
            return ExtendedConfig.rcps;
        case SLIME_CHUNK:
            return ExtendedConfig.slimeChunkFinder;
        case REAL_TIME:
            return ExtendedConfig.realTime;
        case GAME_TIME:
            return ExtendedConfig.gameTime;
        case GAME_WEATHER:
            return ExtendedConfig.gameWeather;
        case MOON_PHASE:
            return ExtendedConfig.moonPhase;
        case POTION_ICON:
            return ExtendedConfig.potionHUDIcon;
        case TPS:
            return ExtendedConfig.tps;
        case TPS_ALL_DIMS:
            return ExtendedConfig.tpsAllDims;
        case ALTERNATE_POTION_COLOR:
            return ExtendedConfig.alternatePotionHUDTextColor;
        case KEYSTROKE_WASD_RAINBOW:
            return ExtendedConfig.keystrokeWASDRainbow;
        case KEYSTROKE_MOUSE_BUTTON_RAINBOW:
            return ExtendedConfig.keystrokeMouseButtonRainbow;
        case KEYSTROKE_SPRINT_RAINBOW:
            return ExtendedConfig.keystrokeSprintRainbow;
        case KEYSTROKE_SNEAK_RAINBOW:
            return ExtendedConfig.keystrokeSneakRainbow;
        case KEYSTROKE_BLOCKING_RAINBOW:
            return ExtendedConfig.keystrokeBlockingRainbow;
        case KEYSTROKE_CPS_RAINBOW:
            return ExtendedConfig.keystrokeCPSRainbow;
        case KEYSTROKE_RCPS_RAINBOW:
            return ExtendedConfig.keystrokeRCPSRainbow;
        default:
            return false;
        }
    }

    public String getOptionStringValue(ExtendedConfig.Options options)
    {
        switch (options)
        {
        case FPS_COLOR:
            return ExtendedConfig.fpsColor;
        case XYZ_COLOR:
            return ExtendedConfig.xyzColor;
        case BIOME_COLOR:
            return ExtendedConfig.biomeColor;
        case DIRECTION_COLOR:
            return ExtendedConfig.directionColor;
        case PING_COLOR:
            return ExtendedConfig.pingColor;
        case PING_TO_SECOND_COLOR:
            return ExtendedConfig.pingToSecondColor;
        case SERVER_IP_COLOR:
            return ExtendedConfig.serverIPColor;
        case EQUIPMENT_STATUS_COLOR:
            return ExtendedConfig.equipmentStatusColor;
        case ARROW_COUNT_COLOR:
            return ExtendedConfig.arrowCountColor;
        case CPS_COLOR:
            return ExtendedConfig.cpsColor;
        case RCPS_COLOR:
            return ExtendedConfig.rcpsColor;
        case SLIME_CHUNK_COLOR:
            return ExtendedConfig.slimeChunkColor;
        case TOP_DONATOR_NAME_COLOR:
            return ExtendedConfig.topDonatorNameColor;
        case RECENT_DONATOR_NAME_COLOR:
            return ExtendedConfig.recentDonatorNameColor;
        case TPS_COLOR:
            return ExtendedConfig.tpsColor;
        case REAL_TIME_COLOR:
            return ExtendedConfig.realTimeColor;
        case GAME_TIME_COLOR:
            return ExtendedConfig.gameTimeColor;
        case GAME_WEATHER_COLOR:
            return ExtendedConfig.gameWeatherColor;
        case MOON_PHASE_COLOR:
            return ExtendedConfig.moonPhaseColor;

        case FPS_VALUE_COLOR:
            return ExtendedConfig.fpsValueColor;
        case FPS_26_AND_40_COLOR:
            return ExtendedConfig.fps26And49Color;
        case FPS_LOW_25_COLOR:
            return ExtendedConfig.fpsLow25Color;
        case XYZ_VALUE_COLOR:
            return ExtendedConfig.xyzValueColor;
        case DIRECTION_VALUE_COLOR:
            return ExtendedConfig.directionValueColor;
        case BIOME_VALUE_COLOR:
            return ExtendedConfig.biomeValueColor;
        case PING_VALUE_COLOR:
            return ExtendedConfig.pingValueColor;
        case PING_200_AND_300_COLOR:
            return ExtendedConfig.ping200And300Color;
        case PING_300_AND_500_COLOR:
            return ExtendedConfig.ping300And500Color;
        case PING_MAX_500_COLOR:
            return ExtendedConfig.pingMax500Color;
        case SERVER_IP_VALUE_COLOR:
            return ExtendedConfig.serverIPValueColor;
        case CPS_VALUE_COLOR:
            return ExtendedConfig.cpsValueColor;
        case RCPS_VALUE_COLOR:
            return ExtendedConfig.rcpsValueColor;
        case SLIME_CHUNK_VALUE_COLOR:
            return ExtendedConfig.slimeChunkValueColor;
        case TOP_DONATOR_VALUE_COLOR:
            return ExtendedConfig.topDonatorValueColor;
        case RECENT_DONATOR_VALUE_COLOR:
            return ExtendedConfig.recentDonatorValueColor;
        case TPS_VALUE_COLOR:
            return ExtendedConfig.tpsValueColor;
        case REAL_TIME_HHMMSS_VALUE_COLOR:
            return ExtendedConfig.realTimeHHMMSSValueColor;
        case REAL_TIME_DDMMYY_VALUE_COLOR:
            return ExtendedConfig.realTimeDDMMYYValueColor;
        case GAME_TIME_VALUE_COLOR:
            return ExtendedConfig.gameTimeValueColor;
        case GAME_WEATHER_VALUE_COLOR:
            return ExtendedConfig.gameWeatherValueColor;
        case MOON_PHASE_VALUE_COLOR:
            return ExtendedConfig.moonPhaseValueColor;

        case KEYSTROKE_WASD_COLOR:
            return ExtendedConfig.keystrokeWASDColor;
        case KEYSTROKE_MOUSE_BUTTON_COLOR:
            return ExtendedConfig.keystrokeMouseButtonColor;
        case KEYSTROKE_SPRINT_COLOR:
            return ExtendedConfig.keystrokeSprintColor;
        case KEYSTROKE_SNEAK_COLOR:
            return ExtendedConfig.keystrokeSneakColor;
        case KEYSTROKE_BLOCKING_COLOR:
            return ExtendedConfig.keystrokeBlockingColor;
        case KEYSTROKE_CPS_COLOR:
            return ExtendedConfig.keystrokeCPSColor;
        case KEYSTROKE_RCPS_COLOR:
            return ExtendedConfig.keystrokeRCPSColor;
        default:
            return "";
        }
    }

    private String getTranslation(String[] strArray, int index)
    {
        if (index < 0 || index >= strArray.length)
        {
            index = 0;
        }
        return LangUtils.translate(strArray[index]);
    }

    @SideOnly(Side.CLIENT)
    public static enum Options
    {
        PREVIEW(false, true),

        SWAP_INFO_POS(false, true),
        HEALTH_STATUS(false, false),
        KEYSTROKE_POSITION(false, false),
        EQUIPMENT_ORDERING(false, false),
        EQUIPMENT_DIRECTION(false, false),
        EQUIPMENT_STATUS(false, false),
        EQUIPMENT_POSITION(false, false),
        POTION_HUD_STYLE(false, false),
        POTION_HUD_POSITION(false, false),
        CPS_POSITION(false, false),
        CPS_OPACITY(true, false, 0.0F, 100.0F, 1.0F),

        FPS(false, true),
        XYZ(false, true),
        DIRECTION(false, true),
        BIOME(false, true),
        PING(false, true),
        PING_TO_SECOND(false, true),
        SERVER_IP(false, true),
        SERVER_IP_MC(false, true),
        EQUIPMENT_HUD(false, true),
        POTION_HUD(false, true),
        KEYSTROKE(false, true),
        KEYSTROKE_LRMB(false, true),
        KEYSTROKE_SS(false, true),
        KEYSTROKE_BLOCKING(false, true),
        CPS(false, true),
        RCPS(false, true),
        SLIME_CHUNK(false, true),
        REAL_TIME(false, true),
        GAME_TIME(false, true),
        GAME_WEATHER(false, true),
        MOON_PHASE(false, true),
        POTION_ICON(false, true),
        TPS(false, true, false, "extended_config.render_info.tps.info"),
        TPS_ALL_DIMS(false, true, false, "extended_config.render_info.tps_all_dims.info"),
        ALTERNATE_POTION_COLOR(false, true),

        ARMOR_HUD_Y(true, false, -512.0F, 512.0F, 1.0F),
        POTION_HUD_Y(true, false, -512.0F, 512.0F, 1.0F),
        KEYSTROKE_Y(true, false, -512.0F, 512.0F, 1.0F),
        MAXIMUM_POTION_DISPLAY(true, false, 2.0F, 8.0F, 1.0F),
        POTION_LENGTH_Y_OFFSET(true, false, 1.0F, 256.0F, 1.0F),
        POTION_LENGTH_Y_OFFSET_OVERLAP(true, false, 1.0F, 256.0F, 1.0F),

        FPS_COLOR(false, false, true, null),
        XYZ_COLOR(false, false, true, null),
        BIOME_COLOR(false, false, true, null),
        DIRECTION_COLOR(false, false, true, null),
        PING_COLOR(false, false, true, null),
        PING_TO_SECOND_COLOR(false, false, true, null),
        SERVER_IP_COLOR(false, false, true, null),
        EQUIPMENT_STATUS_COLOR(false, false, true, null),
        ARROW_COUNT_COLOR(false, false, true, null),
        CPS_COLOR(false, false, true, null),
        RCPS_COLOR(false, false, true, null),
        SLIME_CHUNK_COLOR(false, false, true, null),
        TOP_DONATOR_NAME_COLOR(false, false, true, null),
        RECENT_DONATOR_NAME_COLOR(false, false, true, null),
        TPS_COLOR(false, false, true, null),
        REAL_TIME_COLOR(false, false, true, null),
        GAME_TIME_COLOR(false, false, true, null),
        GAME_WEATHER_COLOR(false, false, true, null),
        MOON_PHASE_COLOR(false, false, true, null),

        FPS_VALUE_COLOR(false, false, true, null),
        FPS_26_AND_40_COLOR(false, false, true, null),
        FPS_LOW_25_COLOR(false, false, true, null),
        XYZ_VALUE_COLOR(false, false, true, null),
        DIRECTION_VALUE_COLOR(false, false, true, null),
        BIOME_VALUE_COLOR(false, false, true, null),
        PING_VALUE_COLOR(false, false, true, null),
        PING_200_AND_300_COLOR(false, false, true, null),
        PING_300_AND_500_COLOR(false, false, true, null),
        PING_MAX_500_COLOR(false, false, true, null),
        SERVER_IP_VALUE_COLOR(false, false, true, null),
        CPS_VALUE_COLOR(false, false, true, null),
        RCPS_VALUE_COLOR(false, false, true, null),
        SLIME_CHUNK_VALUE_COLOR(false, false, true, null),
        TOP_DONATOR_VALUE_COLOR(false, false, true, null),
        RECENT_DONATOR_VALUE_COLOR(false, false, true, null),
        TPS_VALUE_COLOR(false, false, true, null),
        REAL_TIME_HHMMSS_VALUE_COLOR(false, false, true, null),
        REAL_TIME_DDMMYY_VALUE_COLOR(false, false, true, null),
        GAME_TIME_VALUE_COLOR(false, false, true, null),
        GAME_WEATHER_VALUE_COLOR(false, false, true, null),
        MOON_PHASE_VALUE_COLOR(false, false, true, null),

        KEYSTROKE_WASD_COLOR(false, false, true, null),
        KEYSTROKE_MOUSE_BUTTON_COLOR(false, false, true, null),
        KEYSTROKE_SPRINT_COLOR(false, false, true, null),
        KEYSTROKE_SNEAK_COLOR(false, false, true, null),
        KEYSTROKE_BLOCKING_COLOR(false, false, true, null),
        KEYSTROKE_CPS_COLOR(false, false, true, null),
        KEYSTROKE_RCPS_COLOR(false, false, true, null),
        KEYSTROKE_WASD_RAINBOW(false, true),
        KEYSTROKE_MOUSE_BUTTON_RAINBOW(false, true),
        KEYSTROKE_SPRINT_RAINBOW(false, true),
        KEYSTROKE_SNEAK_RAINBOW(false, true),
        KEYSTROKE_BLOCKING_RAINBOW(false, true),
        KEYSTROKE_CPS_RAINBOW(false, true),
        KEYSTROKE_RCPS_RAINBOW(false, true),
        ;

        private final boolean isFloat;
        private final boolean isBoolean;
        private final float valueStep;
        private boolean isTextbox;
        private String comment;
        private float valueMin;
        private float valueMax;
        private static final Options[] values = Options.values();

        public static Options byOrdinal(int ordinal)
        {
            for (Options options : values)
            {
                if (options.getOrdinal() == ordinal)
                {
                    return options;
                }
            }
            return null;
        }

        private Options(boolean isFloat, boolean isBoolean)
        {
            this(isFloat, isBoolean, false, null, 0.0F, 1.0F, 0.0F);
        }

        private Options(boolean isFloat, boolean isBoolean, float valMin, float valMax, float valStep)
        {
            this(isFloat, isBoolean, false, null, valMin, valMax, valStep);
        }

        private Options(boolean isFloat, boolean isBoolean, boolean isTextbox, String comment)
        {
            this(isFloat, isBoolean, isTextbox, comment, 0.0F, 1.0F, 0.0F);
        }

        private Options(boolean isFloat, boolean isBoolean, boolean isTextbox, String comment, float valMin, float valMax, float valStep)
        {
            this.isFloat = isFloat;
            this.isBoolean = isBoolean;
            this.isTextbox = isTextbox;
            this.comment = comment;
            this.valueMin = valMin;
            this.valueMax = valMax;
            this.valueStep = valStep;
        }

        public boolean isFloat()
        {
            return this.isFloat;
        }

        public boolean isBoolean()
        {
            return this.isBoolean;
        }

        public boolean isTextbox()
        {
            return this.isTextbox;
        }

        public int getOrdinal()
        {
            return this.ordinal();
        }

        public String getTranslation()
        {
            return LangUtils.translate(this.name().toLowerCase() + ".extended_config");
        }

        public String getComment()
        {
            return LangUtils.translate(this.comment);
        }

        public float getValueMin()
        {
            return this.valueMin;
        }

        public float getValueMax()
        {
            return this.valueMax;
        }

        public void setValueMax(float value)
        {
            this.valueMax = value;
        }

        public float normalizeValue(float value)
        {
            return MathHelper.clamp((this.snapToStepClamp(value) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
        }

        public float denormalizeValue(float value)
        {
            return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp(value, 0.0F, 1.0F));
        }

        public float snapToStepClamp(float value)
        {
            value = this.snapToStep(value);
            return MathHelper.clamp(value, this.valueMin, this.valueMax);
        }

        private float snapToStep(float value)
        {
            if (this.valueStep > 0.0F)
            {
                value = this.valueStep * Math.round(value / this.valueStep);
            }
            return value;
        }
    }
}