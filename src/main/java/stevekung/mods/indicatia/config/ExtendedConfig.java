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
import stevekung.mods.indicatia.utils.LoggerIN;
import stevekung.mods.stevekunglib.utils.GameProfileUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;
import stevekung.mods.stevekunglib.utils.client.ClientUtils;

public class ExtendedConfig
{
    public static ExtendedConfig instance = new ExtendedConfig();
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
    private static final String[] PING_MODE = new String[] {"indicatia.only_ping", "indicatia.ping_and_delay"};
    private static File file;

    // Render Info
    public boolean fps = true;
    public boolean xyz = true;
    public boolean direction = true;
    public boolean biome = true;
    public boolean ping = true;
    public boolean pingToSecond = false;
    public boolean serverIP = false;
    public boolean serverIPMCVersion = false;
    public boolean equipmentHUD = false;
    public boolean potionHUD = false;
    public boolean keystroke = false;
    public boolean keystrokeMouse = true;
    public boolean keystrokeSprintSneak = true;
    public boolean keystrokeBlocking = true;
    public boolean cps = false;
    public boolean rcps = false;
    public boolean slimeChunkFinder = false;
    public boolean realTime = true;
    public boolean gameTime = true;
    public boolean gameWeather = true;
    public boolean moonPhase = true;
    public boolean potionHUDIcon = false;
    public boolean tps = false;
    public boolean tpsAllDims = false;
    public boolean alternatePotionHUDTextColor = false;
    public boolean toggleSprint = false;
    public boolean toggleSneak = false;

    // Main
    public boolean swapRenderInfo = false;
    public int healthStatusMode = 0;
    public int keystrokePosition = 0;
    public int equipmentOrdering = 0;
    public int equipmentDirection = 0;
    public int equipmentStatus = 0;
    public int equipmentPosition = 2;
    public int potionHUDStyle = 0;
    public int potionHUDPosition = 0;
    public int cpsPosition = 2;
    public int pingMode = 0;
    public float cpsOpacity = 50.0F;

    // Offset
    public int keystrokeYOffset = 0;
    public int armorHUDYOffset = 0;
    public int potionHUDYOffset = 0;
    public int maximumPotionDisplay = 2;
    public int potionLengthYOffset = 23;
    public int potionLengthYOffsetOverlap = 45;

    // Custom Color
    public String fpsColor = defaultWhite;
    public String xyzColor = defaultWhite;
    public String biomeColor = defaultWhite;
    public String directionColor = defaultWhite;
    public String pingColor = defaultWhite;
    public String pingToSecondColor = defaultWhite;
    public String serverIPColor = defaultWhite;
    public String equipmentStatusColor = defaultWhite;
    public String arrowCountColor = defaultWhite;
    public String cpsColor = defaultWhite;
    public String rcpsColor = defaultWhite;
    public String slimeChunkColor = defaultWhite;
    public String topDonatorNameColor = defaultWhite;
    public String recentDonatorNameColor = defaultWhite;
    public String tpsColor = defaultWhite;
    public String realTimeColor = defaultWhite;
    public String gameTimeColor = defaultWhite;
    public String gameWeatherColor = defaultWhite;
    public String moonPhaseColor = defaultWhite;
    public String ytChatViewCountColor = defaultWhite;

    // Custom Color : Value
    public String fpsValueColor = "85,255,85";
    public String fps26And49Color = "255,255,85";
    public String fpsLow25Color = "255,85,85";
    public String xyzValueColor = defaultWhite;
    public String directionValueColor = defaultWhite;
    public String biomeValueColor = defaultWhite;
    public String pingValueColor = "85,255,85";
    public String ping200And300Color = "255,255,85";
    public String ping300And500Color = "255,85,85";
    public String pingMax500Color = "170,0,0";
    public String serverIPValueColor = defaultWhite;
    public String cpsValueColor = defaultWhite;
    public String rcpsValueColor = defaultWhite;
    public String slimeChunkValueColor = defaultWhite;
    public String topDonatorValueColor = defaultWhite;
    public String recentDonatorValueColor = defaultWhite;
    public String tpsValueColor = defaultWhite;
    public String realTimeHHMMSSValueColor = defaultWhite;
    public String realTimeDDMMYYValueColor = defaultWhite;
    public String gameTimeValueColor = defaultWhite;
    public String gameWeatherValueColor = defaultWhite;
    public String moonPhaseValueColor = defaultWhite;
    public String ytChatViewCountValueColor = defaultWhite;

    // Custom Color : Keystroke
    public String keystrokeWASDColor = defaultWhite;
    public String keystrokeMouseButtonColor = defaultWhite;
    public String keystrokeSprintColor = defaultWhite;
    public String keystrokeSneakColor = defaultWhite;
    public String keystrokeBlockingColor = defaultWhite;
    public String keystrokeCPSColor = defaultWhite;
    public String keystrokeRCPSColor = defaultWhite;
    public boolean keystrokeWASDRainbow = false;
    public boolean keystrokeMouseButtonRainbow = false;
    public boolean keystrokeSprintRainbow = false;
    public boolean keystrokeSneakRainbow = false;
    public boolean keystrokeBlockingRainbow = false;
    public boolean keystrokeCPSRainbow = false;
    public boolean keystrokeRCPSRainbow = false;

    // Misc
    public boolean showCustomCape = false;
    public String toggleSprintUseMode = "command";
    public String toggleSneakUseMode = "command";
    public int cpsCustomXOffset = 3;
    public int cpsCustomYOffset = 2;
    public long slimeChunkSeed = 0L;
    public String topDonatorFilePath = "";
    public String recentDonatorFilePath = "";
    public String topDonatorText = "";
    public String recentDonatorText = "";
    public String realmsMessage = "";

    // Hypixel
    public boolean rightClickToAddParty = false;
    public String hypixelNickName = "";
    public int selectedHypixelMinigame = 0;
    public int hypixelMinigameScrollPos = 0;

    private ExtendedConfig() {}

    public void setCurrentProfile(String profileName)
    {
        ExtendedConfig.file = new File(userDir, profileName + ".dat");
        currentProfile = profileName;
    }

    public void load()
    {
        try
        {
            NBTTagCompound nbt = CompressedStreamTools.read(ExtendedConfig.file);

            if (nbt == null)
            {
                return;
            }

            // Render Info
            this.fps = ExtendedConfig.getBoolean(nbt, "FPS", this.fps);
            this.xyz = ExtendedConfig.getBoolean(nbt, "XYZ", this.xyz);
            this.direction = ExtendedConfig.getBoolean(nbt, "Direction", this.direction);
            this.biome = ExtendedConfig.getBoolean(nbt, "Biome", this.biome);
            this.ping = ExtendedConfig.getBoolean(nbt, "Ping", this.ping);
            this.pingToSecond = ExtendedConfig.getBoolean(nbt, "PingToSecond", this.pingToSecond);
            this.serverIP = ExtendedConfig.getBoolean(nbt, "ServerIP", this.serverIP);
            this.serverIPMCVersion = ExtendedConfig.getBoolean(nbt, "ServerIPMCVersion", this.serverIPMCVersion);
            this.equipmentHUD = ExtendedConfig.getBoolean(nbt, "EquipmentHUD", this.equipmentHUD);
            this.potionHUD = ExtendedConfig.getBoolean(nbt, "PotionHUD", this.potionHUD);
            this.keystroke = ExtendedConfig.getBoolean(nbt, "Keystroke", this.keystroke);
            this.keystrokeMouse = ExtendedConfig.getBoolean(nbt, "KeystrokeMouse", this.keystrokeMouse);
            this.keystrokeSprintSneak = ExtendedConfig.getBoolean(nbt, "KeystrokeSprintSneak", this.keystrokeSprintSneak);
            this.keystrokeBlocking = ExtendedConfig.getBoolean(nbt, "KeystrokeBlocking", this.keystrokeBlocking);
            this.cps = ExtendedConfig.getBoolean(nbt, "CPS", this.cps);
            this.rcps = ExtendedConfig.getBoolean(nbt, "RCPS", this.rcps);
            this.slimeChunkFinder = ExtendedConfig.getBoolean(nbt, "SlimeChunkFinder", this.slimeChunkFinder);
            this.realTime = ExtendedConfig.getBoolean(nbt, "RealTime", this.realTime);
            this.gameTime = ExtendedConfig.getBoolean(nbt, "GameTime", this.gameTime);
            this.gameWeather = ExtendedConfig.getBoolean(nbt, "GameWeather", this.gameWeather);
            this.moonPhase = ExtendedConfig.getBoolean(nbt, "MoonPhase", this.moonPhase);
            this.potionHUDIcon = ExtendedConfig.getBoolean(nbt, "PotionHUDIcon", this.potionHUDIcon);
            this.tps = ExtendedConfig.getBoolean(nbt, "TPS", this.tps);
            this.tpsAllDims = ExtendedConfig.getBoolean(nbt, "TPSAllDimensions", this.tpsAllDims);
            this.alternatePotionHUDTextColor = ExtendedConfig.getBoolean(nbt, "AlternatePotionHUDTextColor", this.alternatePotionHUDTextColor);

            // Main
            this.swapRenderInfo = ExtendedConfig.getBoolean(nbt, "SwapRenderInfo", this.swapRenderInfo);
            this.showCustomCape = ExtendedConfig.getBoolean(nbt, "ShowCustomCape", this.showCustomCape);
            this.healthStatusMode = ExtendedConfig.getInteger(nbt, "HealthStatusMode", this.healthStatusMode);
            this.keystrokePosition = ExtendedConfig.getInteger(nbt, "KeystrokePosition", this.keystrokePosition);
            this.equipmentOrdering = ExtendedConfig.getInteger(nbt, "EquipmentOrdering", this.equipmentOrdering);
            this.equipmentDirection = ExtendedConfig.getInteger(nbt, "EquipmentDirection", this.equipmentDirection);
            this.equipmentStatus = ExtendedConfig.getInteger(nbt, "EquipmentStatus", this.equipmentStatus);
            this.equipmentPosition = ExtendedConfig.getInteger(nbt, "EquipmentPosition", this.equipmentPosition);
            this.potionHUDStyle = ExtendedConfig.getInteger(nbt, "PotionHUDStyle", this.potionHUDStyle);
            this.potionHUDPosition = ExtendedConfig.getInteger(nbt, "PotionHUDPosition", this.potionHUDPosition);
            this.cpsPosition = ExtendedConfig.getInteger(nbt, "CPSPosition", this.cpsPosition);
            this.pingMode = ExtendedConfig.getInteger(nbt, "PingMode", this.pingMode);
            this.cpsOpacity = ExtendedConfig.getFloat(nbt, "CPSOpacity", this.cpsOpacity);

            // Movement
            this.toggleSprint = ExtendedConfig.getBoolean(nbt, "ToggleSprint", this.toggleSprint);
            this.toggleSneak = ExtendedConfig.getBoolean(nbt, "ToggleSneak", this.toggleSneak);

            // Offset
            this.keystrokeYOffset = ExtendedConfig.getInteger(nbt, "KeystrokeYOffset", this.keystrokeYOffset);
            this.armorHUDYOffset = ExtendedConfig.getInteger(nbt, "ArmorHUDYOffset", this.armorHUDYOffset);
            this.potionHUDYOffset = ExtendedConfig.getInteger(nbt, "PotionHUDYOffset", this.potionHUDYOffset);
            this.maximumPotionDisplay = ExtendedConfig.getInteger(nbt, "MaximumPotionDisplay", this.maximumPotionDisplay);
            this.potionLengthYOffset = ExtendedConfig.getInteger(nbt, "PotionLengthYOffset", this.potionLengthYOffset);
            this.potionLengthYOffsetOverlap = ExtendedConfig.getInteger(nbt, "PotionLengthYOffsetOverlap", this.potionLengthYOffsetOverlap);

            // Custom Color
            this.fpsColor = ExtendedConfig.getString(nbt, "FPSColor", this.fpsColor);
            this.xyzColor = ExtendedConfig.getString(nbt, "XYZColor", this.xyzColor);
            this.biomeColor = ExtendedConfig.getString(nbt, "BiomeColor", this.biomeColor);
            this.directionColor = ExtendedConfig.getString(nbt, "DirectionColor", this.directionColor);
            this.pingColor = ExtendedConfig.getString(nbt, "PingColor", this.pingColor);
            this.pingToSecondColor = ExtendedConfig.getString(nbt, "PingToSecondColor", this.pingToSecondColor);
            this.serverIPColor = ExtendedConfig.getString(nbt, "ServerIPColor", this.serverIPColor);
            this.equipmentStatusColor = ExtendedConfig.getString(nbt, "EquipmentStatusColor", this.equipmentStatusColor);
            this.arrowCountColor = ExtendedConfig.getString(nbt, "ArrowCountColor", this.arrowCountColor);
            this.cpsColor = ExtendedConfig.getString(nbt, "CPSColor", this.cpsColor);
            this.rcpsColor = ExtendedConfig.getString(nbt, "RCPSColor", this.rcpsColor);
            this.slimeChunkColor = ExtendedConfig.getString(nbt, "SlimeChunkColor", this.slimeChunkColor);
            this.topDonatorNameColor = ExtendedConfig.getString(nbt, "TopDonatorNameColor", this.topDonatorNameColor);
            this.recentDonatorNameColor = ExtendedConfig.getString(nbt, "RecentDonatorNameColor", this.recentDonatorNameColor);
            this.tpsColor = ExtendedConfig.getString(nbt, "TPSColor", this.tpsColor);
            this.realTimeColor = ExtendedConfig.getString(nbt, "RealTimeColor", this.realTimeColor);
            this.gameTimeColor = ExtendedConfig.getString(nbt, "GameTimeColor", this.gameTimeColor);
            this.gameWeatherColor = ExtendedConfig.getString(nbt, "GameWeatherColor", this.gameWeatherColor);
            this.moonPhaseColor = ExtendedConfig.getString(nbt, "MoonPhaseColor", this.moonPhaseColor);
            this.ytChatViewCountColor = ExtendedConfig.getString(nbt, "YTChatViewCountColor", this.ytChatViewCountColor);

            // Custom Color : Value
            this.fpsValueColor = ExtendedConfig.getString(nbt, "FPSValueColor", this.fpsValueColor);
            this.fps26And49Color = ExtendedConfig.getString(nbt, "FPS26And49Color", this.fps26And49Color);
            this.fpsLow25Color = ExtendedConfig.getString(nbt, "FPSLow25Color", this.fpsLow25Color);
            this.xyzValueColor = ExtendedConfig.getString(nbt, "XYZValueColor", this.xyzValueColor);
            this.biomeValueColor = ExtendedConfig.getString(nbt, "BiomeValueColor", this.biomeValueColor);
            this.directionValueColor = ExtendedConfig.getString(nbt, "DirectionValueColor", this.directionValueColor);
            this.pingValueColor = ExtendedConfig.getString(nbt, "PingValueColor", this.pingValueColor);
            this.ping200And300Color = ExtendedConfig.getString(nbt, "Ping200And300Color", this.ping200And300Color);
            this.ping300And500Color = ExtendedConfig.getString(nbt, "Ping300And500Color", this.ping300And500Color);
            this.pingMax500Color = ExtendedConfig.getString(nbt, "PingMax500Color", this.pingMax500Color);
            this.serverIPValueColor = ExtendedConfig.getString(nbt, "ServerIPValueColor", this.serverIPValueColor);
            this.cpsValueColor = ExtendedConfig.getString(nbt, "CPSValueColor", this.cpsValueColor);
            this.rcpsValueColor = ExtendedConfig.getString(nbt, "RCPSValueColor", this.rcpsValueColor);
            this.slimeChunkValueColor = ExtendedConfig.getString(nbt, "SlimeChunkValueColor", this.slimeChunkValueColor);
            this.topDonatorValueColor = ExtendedConfig.getString(nbt, "TopDonatorValueColor", this.topDonatorValueColor);
            this.recentDonatorValueColor = ExtendedConfig.getString(nbt, "RecentDonatorValueColor", this.recentDonatorValueColor);
            this.tpsValueColor = ExtendedConfig.getString(nbt, "TPSValueColor", this.tpsValueColor);
            this.realTimeHHMMSSValueColor = ExtendedConfig.getString(nbt, "RealTimeHHMMSSValueColor", this.realTimeHHMMSSValueColor);
            this.realTimeDDMMYYValueColor = ExtendedConfig.getString(nbt, "RealTimeDDMMYYValueColor", this.realTimeDDMMYYValueColor);
            this.gameTimeValueColor = ExtendedConfig.getString(nbt, "GameTimeValueColor", this.gameTimeValueColor);
            this.gameWeatherValueColor = ExtendedConfig.getString(nbt, "GameWeatherValueColor", this.gameWeatherValueColor);
            this.moonPhaseValueColor = ExtendedConfig.getString(nbt, "MoonPhaseValueColor", this.moonPhaseValueColor);
            this.ytChatViewCountValueColor = ExtendedConfig.getString(nbt, "YTChatViewCountValueColor", this.ytChatViewCountValueColor);

            // Custom Color : Keystroke
            this.keystrokeWASDColor = ExtendedConfig.getString(nbt, "KeystrokeWASDColor", this.keystrokeWASDColor);
            this.keystrokeMouseButtonColor = ExtendedConfig.getString(nbt, "KeystrokeMouseButtonColor", this.keystrokeMouseButtonColor);
            this.keystrokeSprintColor = ExtendedConfig.getString(nbt, "KeystrokeSprintColor", this.keystrokeSprintColor);
            this.keystrokeSneakColor = ExtendedConfig.getString(nbt, "KeystrokeSneakColor", this.keystrokeSneakColor);
            this.keystrokeBlockingColor = ExtendedConfig.getString(nbt, "KeystrokeBlockingColor", this.keystrokeBlockingColor);
            this.keystrokeCPSColor = ExtendedConfig.getString(nbt, "KeystrokeCPSColor", this.keystrokeCPSColor);
            this.keystrokeRCPSColor = ExtendedConfig.getString(nbt, "KeystrokeRCPSColor", this.keystrokeRCPSColor);
            this.keystrokeWASDRainbow = ExtendedConfig.getBoolean(nbt, "KeystrokeWASDRainbow", this.keystrokeWASDRainbow);
            this.keystrokeMouseButtonRainbow = ExtendedConfig.getBoolean(nbt, "KeystrokeMouseButtonRainbow", this.keystrokeMouseButtonRainbow);
            this.keystrokeSprintRainbow = ExtendedConfig.getBoolean(nbt, "KeystrokeSprintRainbow", this.keystrokeSprintRainbow);
            this.keystrokeSneakRainbow = ExtendedConfig.getBoolean(nbt, "KeystrokeSneakRainbow", this.keystrokeSneakRainbow);
            this.keystrokeBlockingRainbow = ExtendedConfig.getBoolean(nbt, "KeystrokeBlockingRainbow", this.keystrokeBlockingRainbow);
            this.keystrokeCPSRainbow = ExtendedConfig.getBoolean(nbt, "KeystrokeCPSRainbow", this.keystrokeCPSRainbow);
            this.keystrokeRCPSRainbow = ExtendedConfig.getBoolean(nbt, "KeystrokeRCPSRainbow", this.keystrokeRCPSRainbow);

            // Misc
            this.toggleSprintUseMode = ExtendedConfig.getString(nbt, "ToggleSprintUseMode", this.toggleSprintUseMode);
            this.toggleSneakUseMode = ExtendedConfig.getString(nbt, "ToggleSneakUseMode", this.toggleSneakUseMode);
            this.cpsCustomXOffset = ExtendedConfig.getInteger(nbt, "CPSCustomOffsetX", this.cpsCustomXOffset);
            this.cpsCustomYOffset = ExtendedConfig.getInteger(nbt, "CPSCustomOffsetY", this.cpsCustomYOffset);
            this.slimeChunkSeed = ExtendedConfig.getLong(nbt, "SlimeChunkSeed", this.slimeChunkSeed);
            this.topDonatorFilePath = ExtendedConfig.getString(nbt, "TopDonatorFilePath", this.topDonatorFilePath);
            this.recentDonatorFilePath = ExtendedConfig.getString(nbt, "RecentDonatorFilePath", this.recentDonatorFilePath);
            this.topDonatorText = ExtendedConfig.getString(nbt, "TopDonatorText", this.topDonatorText);
            this.recentDonatorText = ExtendedConfig.getString(nbt, "RecentDonatorText", this.recentDonatorText);
            this.realmsMessage = ExtendedConfig.getString(nbt, "RealmsMessage", this.realmsMessage);

            // Hypixel
            this.rightClickToAddParty = ExtendedConfig.getBoolean(nbt, "RightClickToAddParty", this.rightClickToAddParty);
            this.hypixelNickName = ExtendedConfig.getString(nbt, "HypixelNickName", this.hypixelNickName);
            this.selectedHypixelMinigame = ExtendedConfig.getInteger(nbt, "SelectedHypixelMinigame", this.selectedHypixelMinigame);
            this.hypixelMinigameScrollPos = ExtendedConfig.getInteger(nbt, "HypixelMinigameScrollPos", this.hypixelMinigameScrollPos);

            ExtendedConfig.readAutoLoginData(nbt.getTagList("AutoLoginData", 10));
            HideNameData.load(nbt.getTagList("HideNameList", 10));

            LoggerIN.info("Loading extended config {}", ExtendedConfig.file.getPath());
        }
        catch (Exception e) {}
    }

    public void save()
    {
        this.save(!ExtendedConfig.currentProfile.isEmpty() ? ExtendedConfig.currentProfile : "default");
    }

    public void save(String profileName)
    {
        try
        {
            NBTTagCompound nbt = new NBTTagCompound();

            // Render Info
            nbt.setBoolean("FPS", this.fps);
            nbt.setBoolean("XYZ", this.xyz);
            nbt.setBoolean("Direction", this.direction);
            nbt.setBoolean("Biome", this.biome);
            nbt.setBoolean("Ping", this.ping);
            nbt.setBoolean("PingToSecond", this.pingToSecond);
            nbt.setBoolean("ServerIP", this.serverIP);
            nbt.setBoolean("ServerIPMCVersion", this.serverIPMCVersion);
            nbt.setBoolean("EquipmentHUD", this.equipmentHUD);
            nbt.setBoolean("PotionHUD", this.potionHUD);
            nbt.setBoolean("Keystroke", this.keystroke);
            nbt.setBoolean("KeystrokeMouse", this.keystrokeMouse);
            nbt.setBoolean("KeystrokeSprintSneak", this.keystrokeSprintSneak);
            nbt.setBoolean("KeystrokeBlocking", this.keystrokeBlocking);
            nbt.setBoolean("CPS", this.cps);
            nbt.setBoolean("RCPS", this.rcps);
            nbt.setBoolean("SlimeChunkFinder", this.slimeChunkFinder);
            nbt.setBoolean("RealTime", this.realTime);
            nbt.setBoolean("GameTime", this.gameTime);
            nbt.setBoolean("GameWeather", this.gameWeather);
            nbt.setBoolean("MoonPhase", this.moonPhase);
            nbt.setBoolean("PotionHUDIcon", this.potionHUDIcon);
            nbt.setBoolean("TPS", this.tps);
            nbt.setBoolean("TPSAllDimensions", this.tpsAllDims);
            nbt.setBoolean("AlternatePotionHUDTextColor", this.alternatePotionHUDTextColor);

            // Main
            nbt.setBoolean("ShowCustomCape", this.showCustomCape);
            nbt.setBoolean("SwapRenderInfo", this.swapRenderInfo);
            nbt.setInteger("HealthStatusMode", this.healthStatusMode);
            nbt.setInteger("KeystrokePosition", this.keystrokePosition);
            nbt.setInteger("EquipmentOrdering", this.equipmentOrdering);
            nbt.setInteger("EquipmentDirection", this.equipmentDirection);
            nbt.setInteger("EquipmentStatus", this.equipmentStatus);
            nbt.setInteger("EquipmentPosition", this.equipmentPosition);
            nbt.setInteger("PotionHUDStyle", this.potionHUDStyle);
            nbt.setInteger("PotionHUDPosition", this.potionHUDPosition);
            nbt.setInteger("CPSPosition", this.cpsPosition);
            nbt.setInteger("PingMode", this.pingMode);
            nbt.setFloat("CPSOpacity", this.cpsOpacity);

            // Movement
            nbt.setBoolean("ToggleSprint", this.toggleSprint);
            nbt.setBoolean("ToggleSneak", this.toggleSneak);

            // Offset
            nbt.setInteger("KeystrokeYOffset", this.keystrokeYOffset);
            nbt.setInteger("ArmorHUDYOffset", this.armorHUDYOffset);
            nbt.setInteger("PotionHUDYOffset", this.potionHUDYOffset);
            nbt.setInteger("MaximumPotionDisplay", this.maximumPotionDisplay);
            nbt.setInteger("PotionLengthYOffset", this.potionLengthYOffset);
            nbt.setInteger("PotionLengthYOffsetOverlap", this.potionLengthYOffsetOverlap);

            // Custom Color
            nbt.setString("FPSColor", this.fpsColor);
            nbt.setString("XYZColor", this.xyzColor);
            nbt.setString("BiomeColor", this.biomeColor);
            nbt.setString("DirectionColor", this.directionColor);
            nbt.setString("PingColor", this.pingColor);
            nbt.setString("PingToSecondColor", this.pingToSecondColor);
            nbt.setString("ServerIPColor", this.serverIPColor);
            nbt.setString("EquipmentStatusColor", this.equipmentStatusColor);
            nbt.setString("ArrowCountColor", this.arrowCountColor);
            nbt.setString("CPSColor", this.cpsColor);
            nbt.setString("RCPSColor", this.rcpsColor);
            nbt.setString("SlimeChunkColor", this.slimeChunkColor);
            nbt.setString("TopDonatorNameColor", this.topDonatorNameColor);
            nbt.setString("RecentDonatorNameColor", this.recentDonatorNameColor);
            nbt.setString("TPSColor", this.tpsColor);
            nbt.setString("RealTimeColor", this.realTimeColor);
            nbt.setString("GameTimeColor", this.gameTimeColor);
            nbt.setString("GameWeatherColor", this.gameWeatherColor);
            nbt.setString("MoonPhaseColor", this.moonPhaseColor);

            // Custom Color : Value
            nbt.setString("FPSValueColor", this.fpsValueColor);
            nbt.setString("FPS26And49Color", this.fps26And49Color);
            nbt.setString("FPSLow25Color", this.fpsLow25Color);
            nbt.setString("XYZValueColor", this.xyzValueColor);
            nbt.setString("BiomeValueColor", this.biomeValueColor);
            nbt.setString("DirectionValueColor", this.directionValueColor);
            nbt.setString("PingValueColor", this.pingValueColor);
            nbt.setString("Ping200And300Color", this.ping200And300Color);
            nbt.setString("Ping300And500Color", this.ping300And500Color);
            nbt.setString("PingMax500Color", this.pingMax500Color);
            nbt.setString("ServerIPValueColor", this.serverIPValueColor);
            nbt.setString("CPSValueColor", this.cpsValueColor);
            nbt.setString("RCPSValueColor", this.rcpsValueColor);
            nbt.setString("SlimeChunkValueColor", this.slimeChunkValueColor);
            nbt.setString("TopDonatorValueColor", this.topDonatorValueColor);
            nbt.setString("RecentDonatorValueColor", this.recentDonatorValueColor);
            nbt.setString("TPSValueColor", this.tpsValueColor);
            nbt.setString("RealTimeHHMMSSValueColor", this.realTimeHHMMSSValueColor);
            nbt.setString("RealTimeDDMMYYValueColor", this.realTimeDDMMYYValueColor);
            nbt.setString("GameTimeValueColor", this.gameTimeValueColor);
            nbt.setString("GameWeatherValueColor", this.gameWeatherValueColor);
            nbt.setString("MoonPhaseValueColor", this.moonPhaseValueColor);

            // Custom Color : Keystroke
            nbt.setString("KeystrokeWASDColor", this.keystrokeWASDColor);
            nbt.setString("KeystrokeMouseButtonColor", this.keystrokeMouseButtonColor);
            nbt.setString("KeystrokeSprintColor", this.keystrokeSprintColor);
            nbt.setString("KeystrokeSneakColor", this.keystrokeSneakColor);
            nbt.setString("KeystrokeBlockingColor", this.keystrokeBlockingColor);
            nbt.setString("KeystrokeCPSColor", this.keystrokeCPSColor);
            nbt.setString("KeystrokeRCPSColor", this.keystrokeRCPSColor);
            nbt.setBoolean("KeystrokeWASDRainbow", this.keystrokeWASDRainbow);
            nbt.setBoolean("KeystrokeMouseButtonRainbow", this.keystrokeMouseButtonRainbow);
            nbt.setBoolean("KeystrokeSprintRainbow", this.keystrokeSprintRainbow);
            nbt.setBoolean("KeystrokeSneakRainbow", this.keystrokeSneakRainbow);
            nbt.setBoolean("KeystrokeBlockingRainbow", this.keystrokeBlockingRainbow);
            nbt.setBoolean("KeystrokeCPSRainbow", this.keystrokeCPSRainbow);
            nbt.setBoolean("KeystrokeRCPSRainbow", this.keystrokeRCPSRainbow);

            // Misc
            nbt.setString("ToggleSprintUseMode", this.toggleSprintUseMode);
            nbt.setString("ToggleSneakUseMode", this.toggleSneakUseMode);
            nbt.setInteger("CPSCustomOffsetX", this.cpsCustomXOffset);
            nbt.setInteger("CPSCustomOffsetY", this.cpsCustomYOffset);
            nbt.setLong("SlimeChunkSeed", this.slimeChunkSeed);
            nbt.setString("TopDonatorFilePath", this.topDonatorFilePath);
            nbt.setString("RecentDonatorFilePath", this.recentDonatorFilePath);
            nbt.setString("TopDonatorText", this.topDonatorText);
            nbt.setString("RecentDonatorText", this.recentDonatorText);
            nbt.setString("RealmsMessage", this.realmsMessage);

            // Hypixel
            nbt.setBoolean("RightClickToAddParty", this.rightClickToAddParty);
            nbt.setString("HypixelNickName", this.hypixelNickName);
            nbt.setInteger("SelectedHypixelMinigame", this.selectedHypixelMinigame);
            nbt.setInteger("HypixelMinigameScrollPos", this.hypixelMinigameScrollPos);

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
            LoggerIN.info("Saving profile name!");
        }
        catch (IOException e)
        {
            LoggerIN.error("Failed to save profiles", (Throwable)e);
        }
    }

    public static void resetConfig()
    {
        ExtendedConfig.instance = new ExtendedConfig();
        ExtendedConfig.instance.save(ExtendedConfig.currentProfile);
        ClientUtils.printClientMessage(LangUtils.translate("message.reset_config", ExtendedConfig.currentProfile));
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
            return name + this.getTranslation(HEALTH_STATUS, this.healthStatusMode);
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_POSITION)
        {
            return name + this.getTranslation(POSITION, this.keystrokePosition);
        }
        else if (options == ExtendedConfig.Options.EQUIPMENT_ORDERING)
        {
            return name + this.getTranslation(EQUIPMENT_ORDERING, this.equipmentOrdering);
        }
        else if (options == ExtendedConfig.Options.EQUIPMENT_DIRECTION)
        {
            return name + this.getTranslation(EQUIPMENT_DIRECTION, this.equipmentDirection);
        }
        else if (options == ExtendedConfig.Options.EQUIPMENT_STATUS)
        {
            return name + this.getTranslation(EQUIPMENT_STATUS, this.equipmentStatus);
        }
        else if (options == ExtendedConfig.Options.EQUIPMENT_POSITION)
        {
            return name + this.getTranslation(EQUIPMENT_POSITION, this.equipmentPosition);
        }
        else if (options == ExtendedConfig.Options.POTION_HUD_STYLE)
        {
            return name + this.getTranslation(POTION_STATUS_HUD_STYLE, this.potionHUDStyle);
        }
        else if (options == ExtendedConfig.Options.POTION_HUD_POSITION)
        {
            return name + this.getTranslation(POTION_STATUS_HUD_POSITION, this.potionHUDPosition);
        }
        else if (options == ExtendedConfig.Options.CPS_POSITION)
        {
            return name + this.getTranslation(CPS_POSITION, this.cpsPosition);
        }
        else if (options == ExtendedConfig.Options.PING_MODE)
        {
            return name + this.getTranslation(PING_MODE, this.pingMode);
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
        else if (options == ExtendedConfig.Options.SWAP_INFO_POS)
        {
            this.swapRenderInfo = !this.swapRenderInfo;
        }
        else if (options == ExtendedConfig.Options.HEALTH_STATUS)
        {
            this.healthStatusMode = (this.healthStatusMode + value) % 3;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_POSITION)
        {
            this.keystrokePosition = (this.keystrokePosition + value) % 2;
        }
        else if (options == ExtendedConfig.Options.EQUIPMENT_ORDERING)
        {
            this.equipmentOrdering = (this.equipmentOrdering + value) % 2;
        }
        else if (options == ExtendedConfig.Options.EQUIPMENT_DIRECTION)
        {
            this.equipmentDirection = (this.equipmentDirection + value) % 2;
        }
        else if (options == ExtendedConfig.Options.EQUIPMENT_STATUS)
        {
            this.equipmentStatus = (this.equipmentStatus + value) % 4;
        }
        else if (options == ExtendedConfig.Options.EQUIPMENT_POSITION)
        {
            this.equipmentPosition = (this.equipmentPosition + value) % 3;
        }
        else if (options == ExtendedConfig.Options.POTION_HUD_STYLE)
        {
            this.potionHUDStyle = (this.potionHUDStyle + value) % 2;
        }
        else if (options == ExtendedConfig.Options.POTION_HUD_POSITION)
        {
            this.potionHUDPosition = (this.potionHUDPosition + value) % 4;
        }
        else if (options == ExtendedConfig.Options.CPS_POSITION)
        {
            this.cpsPosition = (this.cpsPosition + value) % 4;
        }
        else if (options == ExtendedConfig.Options.PING_MODE)
        {
            this.pingMode = (this.pingMode + value) % 2;
        }

        else if (options == ExtendedConfig.Options.FPS)
        {
            this.fps = !this.fps;
        }
        else if (options == ExtendedConfig.Options.XYZ)
        {
            this.xyz = !this.xyz;
        }
        else if (options == ExtendedConfig.Options.DIRECTION)
        {
            this.direction = !this.direction;
        }
        else if (options == ExtendedConfig.Options.BIOME)
        {
            this.biome = !this.biome;
        }
        else if (options == ExtendedConfig.Options.PING)
        {
            this.ping = !this.ping;
        }
        else if (options == ExtendedConfig.Options.PING_TO_SECOND)
        {
            this.pingToSecond = !this.pingToSecond;
        }
        else if (options == ExtendedConfig.Options.SERVER_IP)
        {
            this.serverIP = !this.serverIP;
        }
        else if (options == ExtendedConfig.Options.SERVER_IP_MC)
        {
            this.serverIPMCVersion = !this.serverIPMCVersion;
        }
        else if (options == ExtendedConfig.Options.EQUIPMENT_HUD)
        {
            this.equipmentHUD = !this.equipmentHUD;
        }
        else if (options == ExtendedConfig.Options.POTION_HUD)
        {
            this.potionHUD = !this.potionHUD;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE)
        {
            this.keystroke = !this.keystroke;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_LRMB)
        {
            this.keystrokeMouse = !this.keystrokeMouse;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_SS)
        {
            this.keystrokeSprintSneak = !this.keystrokeSprintSneak;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_BLOCKING)
        {
            this.keystrokeBlocking = !this.keystrokeBlocking;
        }
        else if (options == ExtendedConfig.Options.CPS)
        {
            this.cps = !this.cps;
        }
        else if (options == ExtendedConfig.Options.RCPS)
        {
            this.rcps = !this.rcps;
        }
        else if (options == ExtendedConfig.Options.SLIME_CHUNK)
        {
            this.slimeChunkFinder = !this.slimeChunkFinder;
        }
        else if (options == ExtendedConfig.Options.REAL_TIME)
        {
            this.realTime = !this.realTime;
        }
        else if (options == ExtendedConfig.Options.GAME_TIME)
        {
            this.gameTime = !this.gameTime;
        }
        else if (options == ExtendedConfig.Options.GAME_WEATHER)
        {
            this.gameWeather = !this.gameWeather;
        }
        else if (options == ExtendedConfig.Options.MOON_PHASE)
        {
            this.moonPhase = !this.moonPhase;
        }
        else if (options == ExtendedConfig.Options.POTION_ICON)
        {
            this.potionHUDIcon = !this.potionHUDIcon;
        }
        else if (options == ExtendedConfig.Options.TPS)
        {
            this.tps = !this.tps;
        }
        else if (options == ExtendedConfig.Options.TPS_ALL_DIMS)
        {
            this.tpsAllDims = !this.tpsAllDims;
        }
        else if (options == ExtendedConfig.Options.ALTERNATE_POTION_COLOR)
        {
            this.alternatePotionHUDTextColor = !this.alternatePotionHUDTextColor;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_WASD_RAINBOW)
        {
            this.keystrokeWASDRainbow = !this.keystrokeWASDRainbow;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_MOUSE_BUTTON_RAINBOW)
        {
            this.keystrokeMouseButtonRainbow = !this.keystrokeMouseButtonRainbow;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_SPRINT_RAINBOW)
        {
            this.keystrokeSprintRainbow = !this.keystrokeSprintRainbow;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_SNEAK_RAINBOW)
        {
            this.keystrokeSneakRainbow = !this.keystrokeSneakRainbow;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_BLOCKING_RAINBOW)
        {
            this.keystrokeBlockingRainbow = !this.keystrokeBlockingRainbow;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_CPS_RAINBOW)
        {
            this.keystrokeCPSRainbow = !this.keystrokeCPSRainbow;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_RCPS_RAINBOW)
        {
            this.keystrokeRCPSRainbow = !this.keystrokeRCPSRainbow;
        }

        else if (options == ExtendedConfig.Options.RIGHT_CLICK_ADD_PARTY)
        {
            this.rightClickToAddParty = !this.rightClickToAddParty;
        }
    }

    public void setOptionFloatValue(ExtendedConfig.Options options, float value)
    {
        if (options == ExtendedConfig.Options.ARMOR_HUD_Y)
        {
            this.armorHUDYOffset = (int) value;
        }
        else if (options == ExtendedConfig.Options.POTION_HUD_Y)
        {
            this.potionHUDYOffset = (int) value;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_Y)
        {
            this.keystrokeYOffset = (int) value;
        }
        else if (options == ExtendedConfig.Options.MAXIMUM_POTION_DISPLAY)
        {
            this.maximumPotionDisplay = (int) value;
        }
        else if (options == ExtendedConfig.Options.POTION_LENGTH_Y_OFFSET)
        {
            this.potionLengthYOffset = (int) value;
        }
        else if (options == ExtendedConfig.Options.POTION_LENGTH_Y_OFFSET_OVERLAP)
        {
            this.potionLengthYOffsetOverlap = (int) value;
        }
        else if (options == ExtendedConfig.Options.CPS_OPACITY)
        {
            this.cpsOpacity = value;
        }
    }

    public void setOptionStringValue(ExtendedConfig.Options options, String value)
    {
        if (options == ExtendedConfig.Options.FPS_COLOR)
        {
            this.fpsColor = value;
        }
        else if (options == ExtendedConfig.Options.XYZ_COLOR)
        {
            this.xyzColor = value;
        }
        else if (options == ExtendedConfig.Options.BIOME_COLOR)
        {
            this.biomeColor = value;
        }
        else if (options == ExtendedConfig.Options.DIRECTION_COLOR)
        {
            this.directionColor = value;
        }
        else if (options == ExtendedConfig.Options.PING_COLOR)
        {
            this.pingColor = value;
        }
        else if (options == ExtendedConfig.Options.PING_TO_SECOND_COLOR)
        {
            this.pingToSecondColor = value;
        }
        else if (options == ExtendedConfig.Options.SERVER_IP_COLOR)
        {
            this.serverIPColor = value;
        }
        else if (options == ExtendedConfig.Options.EQUIPMENT_STATUS_COLOR)
        {
            this.equipmentStatusColor = value;
        }
        else if (options == ExtendedConfig.Options.ARROW_COUNT_COLOR)
        {
            this.arrowCountColor = value;
        }
        else if (options == ExtendedConfig.Options.CPS_COLOR)
        {
            this.cpsColor = value;
        }
        else if (options == ExtendedConfig.Options.RCPS_COLOR)
        {
            this.rcpsColor = value;
        }
        else if (options == ExtendedConfig.Options.SLIME_CHUNK_COLOR)
        {
            this.slimeChunkColor = value;
        }
        else if (options == ExtendedConfig.Options.TOP_DONATOR_NAME_COLOR)
        {
            this.topDonatorNameColor = value;
        }
        else if (options == ExtendedConfig.Options.RECENT_DONATOR_NAME_COLOR)
        {
            this.recentDonatorNameColor = value;
        }
        else if (options == ExtendedConfig.Options.TPS_COLOR)
        {
            this.tpsColor = value;
        }
        else if (options == ExtendedConfig.Options.REAL_TIME_COLOR)
        {
            this.realTimeColor = value;
        }
        else if (options == ExtendedConfig.Options.GAME_TIME_COLOR)
        {
            this.gameTimeColor = value;
        }
        else if (options == ExtendedConfig.Options.GAME_WEATHER_COLOR)
        {
            this.gameWeatherColor = value;
        }
        else if (options == ExtendedConfig.Options.MOON_PHASE_COLOR)
        {
            this.moonPhaseColor = value;
        }
        else if (options == ExtendedConfig.Options.YTCHAT_VIEW_COUNT_COLOR)
        {
            this.ytChatViewCountColor = value;
        }

        else if (options == ExtendedConfig.Options.FPS_VALUE_COLOR)
        {
            this.fpsValueColor = value;
        }
        else if (options == ExtendedConfig.Options.FPS_26_AND_40_COLOR)
        {
            this.fps26And49Color = value;
        }
        else if (options == ExtendedConfig.Options.FPS_LOW_25_COLOR)
        {
            this.fpsLow25Color = value;
        }
        else if (options == ExtendedConfig.Options.XYZ_VALUE_COLOR)
        {
            this.xyzValueColor = value;
        }
        else if (options == ExtendedConfig.Options.DIRECTION_VALUE_COLOR)
        {
            this.directionValueColor = value;
        }
        else if (options == ExtendedConfig.Options.BIOME_VALUE_COLOR)
        {
            this.biomeValueColor = value;
        }
        else if (options == ExtendedConfig.Options.PING_VALUE_COLOR)
        {
            this.pingValueColor = value;
        }
        else if (options == ExtendedConfig.Options.PING_200_AND_300_COLOR)
        {
            this.ping200And300Color = value;
        }
        else if (options == ExtendedConfig.Options.PING_300_AND_500_COLOR)
        {
            this.ping300And500Color = value;
        }
        else if (options == ExtendedConfig.Options.PING_MAX_500_COLOR)
        {
            this.pingMax500Color = value;
        }
        else if (options == ExtendedConfig.Options.SERVER_IP_VALUE_COLOR)
        {
            this.serverIPValueColor = value;
        }
        else if (options == ExtendedConfig.Options.CPS_VALUE_COLOR)
        {
            this.cpsValueColor = value;
        }
        else if (options == ExtendedConfig.Options.RCPS_VALUE_COLOR)
        {
            this.rcpsValueColor = value;
        }
        else if (options == ExtendedConfig.Options.SLIME_CHUNK_VALUE_COLOR)
        {
            this.slimeChunkValueColor = value;
        }
        else if (options == ExtendedConfig.Options.TOP_DONATOR_VALUE_COLOR)
        {
            this.topDonatorValueColor = value;
        }
        else if (options == ExtendedConfig.Options.RECENT_DONATOR_VALUE_COLOR)
        {
            this.recentDonatorValueColor = value;
        }
        else if (options == ExtendedConfig.Options.TPS_VALUE_COLOR)
        {
            this.tpsValueColor = value;
        }
        else if (options == ExtendedConfig.Options.REAL_TIME_HHMMSS_VALUE_COLOR)
        {
            this.realTimeHHMMSSValueColor = value;
        }
        else if (options == ExtendedConfig.Options.REAL_TIME_DDMMYY_VALUE_COLOR)
        {
            this.realTimeDDMMYYValueColor = value;
        }
        else if (options == ExtendedConfig.Options.GAME_TIME_VALUE_COLOR)
        {
            this.gameTimeValueColor = value;
        }
        else if (options == ExtendedConfig.Options.GAME_WEATHER_VALUE_COLOR)
        {
            this.gameWeatherValueColor = value;
        }
        else if (options == ExtendedConfig.Options.MOON_PHASE_VALUE_COLOR)
        {
            this.moonPhaseValueColor = value;
        }
        else if (options == ExtendedConfig.Options.YTCHAT_VIEW_COUNT_VALUE_COLOR)
        {
            this.ytChatViewCountValueColor = value;
        }

        else if (options == ExtendedConfig.Options.KEYSTROKE_WASD_COLOR)
        {
            this.keystrokeWASDColor = value;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_MOUSE_BUTTON_COLOR)
        {
            this.keystrokeMouseButtonColor = value;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_SPRINT_COLOR)
        {
            this.keystrokeSprintColor = value;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_SNEAK_COLOR)
        {
            this.keystrokeSneakColor = value;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_BLOCKING_COLOR)
        {
            this.keystrokeBlockingColor = value;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_CPS_COLOR)
        {
            this.keystrokeCPSColor = value;
        }
        else if (options == ExtendedConfig.Options.KEYSTROKE_RCPS_COLOR)
        {
            this.keystrokeRCPSColor = value;
        }
    }

    public float getOptionFloatValue(ExtendedConfig.Options settingOption)
    {
        if (settingOption == ExtendedConfig.Options.ARMOR_HUD_Y)
        {
            return this.armorHUDYOffset;
        }
        else if (settingOption == ExtendedConfig.Options.POTION_HUD_Y)
        {
            return this.potionHUDYOffset;
        }
        else if (settingOption == ExtendedConfig.Options.KEYSTROKE_Y)
        {
            return this.keystrokeYOffset;
        }
        else if (settingOption == ExtendedConfig.Options.MAXIMUM_POTION_DISPLAY)
        {
            return this.maximumPotionDisplay;
        }
        else if (settingOption == ExtendedConfig.Options.POTION_LENGTH_Y_OFFSET)
        {
            return this.potionLengthYOffset;
        }
        else if (settingOption == ExtendedConfig.Options.POTION_LENGTH_Y_OFFSET_OVERLAP)
        {
            return this.potionLengthYOffsetOverlap;
        }
        else if (settingOption == ExtendedConfig.Options.CPS_OPACITY)
        {
            return this.cpsOpacity;
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
            return this.swapRenderInfo;
        case FPS:
            return this.fps;
        case XYZ:
            return this.xyz;
        case DIRECTION:
            return this.direction;
        case BIOME:
            return this.biome;
        case PING:
            return this.ping;
        case PING_TO_SECOND:
            return this.pingToSecond;
        case SERVER_IP:
            return this.serverIP;
        case SERVER_IP_MC:
            return this.serverIPMCVersion;
        case EQUIPMENT_HUD:
            return this.equipmentHUD;
        case POTION_HUD:
            return this.potionHUD;
        case KEYSTROKE:
            return this.keystroke;
        case KEYSTROKE_LRMB:
            return this.keystrokeMouse;
        case KEYSTROKE_SS:
            return this.keystrokeSprintSneak;
        case KEYSTROKE_BLOCKING:
            return this.keystrokeBlocking;
        case CPS:
            return this.cps;
        case RCPS:
            return this.rcps;
        case SLIME_CHUNK:
            return this.slimeChunkFinder;
        case REAL_TIME:
            return this.realTime;
        case GAME_TIME:
            return this.gameTime;
        case GAME_WEATHER:
            return this.gameWeather;
        case MOON_PHASE:
            return this.moonPhase;
        case POTION_ICON:
            return this.potionHUDIcon;
        case TPS:
            return this.tps;
        case TPS_ALL_DIMS:
            return this.tpsAllDims;
        case ALTERNATE_POTION_COLOR:
            return this.alternatePotionHUDTextColor;
        case KEYSTROKE_WASD_RAINBOW:
            return this.keystrokeWASDRainbow;
        case KEYSTROKE_MOUSE_BUTTON_RAINBOW:
            return this.keystrokeMouseButtonRainbow;
        case KEYSTROKE_SPRINT_RAINBOW:
            return this.keystrokeSprintRainbow;
        case KEYSTROKE_SNEAK_RAINBOW:
            return this.keystrokeSneakRainbow;
        case KEYSTROKE_BLOCKING_RAINBOW:
            return this.keystrokeBlockingRainbow;
        case KEYSTROKE_CPS_RAINBOW:
            return this.keystrokeCPSRainbow;
        case KEYSTROKE_RCPS_RAINBOW:
            return this.keystrokeRCPSRainbow;

        case RIGHT_CLICK_ADD_PARTY:
            return this.rightClickToAddParty;
        default:
            return false;
        }
    }

    public String getOptionStringValue(ExtendedConfig.Options options)
    {
        switch (options)
        {
        case FPS_COLOR:
            return this.fpsColor;
        case XYZ_COLOR:
            return this.xyzColor;
        case BIOME_COLOR:
            return this.biomeColor;
        case DIRECTION_COLOR:
            return this.directionColor;
        case PING_COLOR:
            return this.pingColor;
        case PING_TO_SECOND_COLOR:
            return this.pingToSecondColor;
        case SERVER_IP_COLOR:
            return this.serverIPColor;
        case EQUIPMENT_STATUS_COLOR:
            return this.equipmentStatusColor;
        case ARROW_COUNT_COLOR:
            return this.arrowCountColor;
        case CPS_COLOR:
            return this.cpsColor;
        case RCPS_COLOR:
            return this.rcpsColor;
        case SLIME_CHUNK_COLOR:
            return this.slimeChunkColor;
        case TOP_DONATOR_NAME_COLOR:
            return this.topDonatorNameColor;
        case RECENT_DONATOR_NAME_COLOR:
            return this.recentDonatorNameColor;
        case TPS_COLOR:
            return this.tpsColor;
        case REAL_TIME_COLOR:
            return this.realTimeColor;
        case GAME_TIME_COLOR:
            return this.gameTimeColor;
        case GAME_WEATHER_COLOR:
            return this.gameWeatherColor;
        case MOON_PHASE_COLOR:
            return this.moonPhaseColor;
        case YTCHAT_VIEW_COUNT_COLOR:
            return this.ytChatViewCountColor;

        case FPS_VALUE_COLOR:
            return this.fpsValueColor;
        case FPS_26_AND_40_COLOR:
            return this.fps26And49Color;
        case FPS_LOW_25_COLOR:
            return this.fpsLow25Color;
        case XYZ_VALUE_COLOR:
            return this.xyzValueColor;
        case DIRECTION_VALUE_COLOR:
            return this.directionValueColor;
        case BIOME_VALUE_COLOR:
            return this.biomeValueColor;
        case PING_VALUE_COLOR:
            return this.pingValueColor;
        case PING_200_AND_300_COLOR:
            return this.ping200And300Color;
        case PING_300_AND_500_COLOR:
            return this.ping300And500Color;
        case PING_MAX_500_COLOR:
            return this.pingMax500Color;
        case SERVER_IP_VALUE_COLOR:
            return this.serverIPValueColor;
        case CPS_VALUE_COLOR:
            return this.cpsValueColor;
        case RCPS_VALUE_COLOR:
            return this.rcpsValueColor;
        case SLIME_CHUNK_VALUE_COLOR:
            return this.slimeChunkValueColor;
        case TOP_DONATOR_VALUE_COLOR:
            return this.topDonatorValueColor;
        case RECENT_DONATOR_VALUE_COLOR:
            return this.recentDonatorValueColor;
        case TPS_VALUE_COLOR:
            return this.tpsValueColor;
        case REAL_TIME_HHMMSS_VALUE_COLOR:
            return this.realTimeHHMMSSValueColor;
        case REAL_TIME_DDMMYY_VALUE_COLOR:
            return this.realTimeDDMMYYValueColor;
        case GAME_TIME_VALUE_COLOR:
            return this.gameTimeValueColor;
        case GAME_WEATHER_VALUE_COLOR:
            return this.gameWeatherValueColor;
        case MOON_PHASE_VALUE_COLOR:
            return this.moonPhaseValueColor;
        case YTCHAT_VIEW_COUNT_VALUE_COLOR:
            return this.ytChatViewCountValueColor;

        case KEYSTROKE_WASD_COLOR:
            return this.keystrokeWASDColor;
        case KEYSTROKE_MOUSE_BUTTON_COLOR:
            return this.keystrokeMouseButtonColor;
        case KEYSTROKE_SPRINT_COLOR:
            return this.keystrokeSprintColor;
        case KEYSTROKE_SNEAK_COLOR:
            return this.keystrokeSneakColor;
        case KEYSTROKE_BLOCKING_COLOR:
            return this.keystrokeBlockingColor;
        case KEYSTROKE_CPS_COLOR:
            return this.keystrokeCPSColor;
        case KEYSTROKE_RCPS_COLOR:
            return this.keystrokeRCPSColor;
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
        PING_MODE(false, false),
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

        FPS_COLOR(false, false, true),
        XYZ_COLOR(false, false, true),
        BIOME_COLOR(false, false, true),
        DIRECTION_COLOR(false, false, true),
        PING_COLOR(false, false, true),
        PING_TO_SECOND_COLOR(false, false, true),
        SERVER_IP_COLOR(false, false, true),
        EQUIPMENT_STATUS_COLOR(false, false, true),
        ARROW_COUNT_COLOR(false, false, true),
        CPS_COLOR(false, false, true),
        RCPS_COLOR(false, false, true),
        SLIME_CHUNK_COLOR(false, false, true),
        TOP_DONATOR_NAME_COLOR(false, false, true),
        RECENT_DONATOR_NAME_COLOR(false, false, true),
        TPS_COLOR(false, false, true),
        REAL_TIME_COLOR(false, false, true),
        GAME_TIME_COLOR(false, false, true),
        GAME_WEATHER_COLOR(false, false, true),
        MOON_PHASE_COLOR(false, false, true),
        YTCHAT_VIEW_COUNT_COLOR(false, false, true),

        FPS_VALUE_COLOR(false, false, true),
        FPS_26_AND_40_COLOR(false, false, true),
        FPS_LOW_25_COLOR(false, false, true),
        XYZ_VALUE_COLOR(false, false, true),
        DIRECTION_VALUE_COLOR(false, false, true),
        BIOME_VALUE_COLOR(false, false, true),
        PING_VALUE_COLOR(false, false, true),
        PING_200_AND_300_COLOR(false, false, true),
        PING_300_AND_500_COLOR(false, false, true),
        PING_MAX_500_COLOR(false, false, true),
        SERVER_IP_VALUE_COLOR(false, false, true),
        CPS_VALUE_COLOR(false, false, true),
        RCPS_VALUE_COLOR(false, false, true),
        SLIME_CHUNK_VALUE_COLOR(false, false, true),
        TOP_DONATOR_VALUE_COLOR(false, false, true),
        RECENT_DONATOR_VALUE_COLOR(false, false, true),
        TPS_VALUE_COLOR(false, false, true),
        REAL_TIME_HHMMSS_VALUE_COLOR(false, false, true),
        REAL_TIME_DDMMYY_VALUE_COLOR(false, false, true),
        GAME_TIME_VALUE_COLOR(false, false, true),
        GAME_WEATHER_VALUE_COLOR(false, false, true),
        MOON_PHASE_VALUE_COLOR(false, false, true),
        YTCHAT_VIEW_COUNT_VALUE_COLOR(false, false, true),

        KEYSTROKE_WASD_COLOR(false, false, true),
        KEYSTROKE_MOUSE_BUTTON_COLOR(false, false, true),
        KEYSTROKE_SPRINT_COLOR(false, false, true),
        KEYSTROKE_SNEAK_COLOR(false, false, true),
        KEYSTROKE_BLOCKING_COLOR(false, false, true),
        KEYSTROKE_CPS_COLOR(false, false, true),
        KEYSTROKE_RCPS_COLOR(false, false, true),
        KEYSTROKE_WASD_RAINBOW(false, true),
        KEYSTROKE_MOUSE_BUTTON_RAINBOW(false, true),
        KEYSTROKE_SPRINT_RAINBOW(false, true),
        KEYSTROKE_SNEAK_RAINBOW(false, true),
        KEYSTROKE_BLOCKING_RAINBOW(false, true),
        KEYSTROKE_CPS_RAINBOW(false, true),
        KEYSTROKE_RCPS_RAINBOW(false, true),

        RIGHT_CLICK_ADD_PARTY(false, true),
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

        private Options(boolean isFloat, boolean isBoolean, boolean isTextbox)
        {
            this(isFloat, isBoolean, isTextbox, null, 0.0F, 1.0F, 0.0F);
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