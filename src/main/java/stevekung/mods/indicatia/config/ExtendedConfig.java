package stevekung.mods.indicatia.config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import com.google.common.base.Strings;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.math.MathHelper;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.gui.config.GuiExtendedConfig;
import stevekung.mods.indicatia.utils.AutoLogin;
import stevekung.mods.indicatia.utils.HideNameData;
import stevekung.mods.stevekungslib.utils.GameProfileUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class ExtendedConfig
{
    public static final ExtendedConfig instance = new ExtendedConfig();
    public static final AutoLogin loginData = new AutoLogin();
    private static final String defaultWhite = "255,255,255";
    public static final File indicatiaDir = new File(MinecraftClient.getInstance().runDirectory, "indicatia");
    public static final File userDir = new File(indicatiaDir, GameProfileUtils.getUUID().toString());
    public static final File defaultConfig = new File(userDir, "default.dat");
    public static String currentProfile = "";
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
    public HealthStatusMode healthStatusMode = HealthStatusMode.DISABLED;
    public KeystrokePosition keystrokePosition = KeystrokePosition.RIGHT;
    public Equipments.Ordering equipmentOrdering = Equipments.Ordering.DEFAULT;
    public Equipments.Direction equipmentDirection = Equipments.Direction.VERTICAL;
    public Equipments.Status equipmentStatus = Equipments.Status.DAMAGE_AND_MAX_DAMAGE;
    public Equipments.Position equipmentPosition = Equipments.Position.HOTBAR;
    public StatusEffects.Style potionHUDStyle = StatusEffects.Style.DEFAULT;
    public StatusEffects.Position potionHUDPosition = StatusEffects.Position.LEFT;
    public CPSPosition cpsPosition = CPSPosition.KEYSTROKE;
    public double cpsOpacity = 50.0D;

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

    public static final DoubleConfigOption CPS_OPACITY = new DoubleConfigOption("cps_opacity", 0.0D, 100.0D, 1.0F, config -> config.cpsOpacity, (config, value) -> config.cpsOpacity = value, (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + doubleOpt.normalizeValue(doubleOpt.get(config)));
    public static final DoubleConfigOption ARMOR_HUD_Y = new DoubleConfigOption("armor_hud_y", -512.0D, 512.0D, 1.0F, config -> (double)config.armorHUDYOffset, (config, value) -> config.armorHUDYOffset = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + (int)doubleOpt.get(config));
    public static final DoubleConfigOption POTION_HUD_Y = new DoubleConfigOption("potion_hud_y", -512.0D, 512.0D, 1.0F, config -> (double)config.potionHUDYOffset, (config, value) -> config.potionHUDYOffset = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + (int)doubleOpt.get(config));
    public static final DoubleConfigOption KEYSTROKE_Y = new DoubleConfigOption("keystroke_y", -512.0D, 512.0D, 1.0F, config -> (double)config.keystrokeYOffset, (config, value) -> config.keystrokeYOffset = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + (int)doubleOpt.get(config));
    public static final DoubleConfigOption MAXIMUM_POTION_DISPLAY = new DoubleConfigOption("maximum_potion_display", 2.0D, 8.0D, 0.0F, config -> (double)config.maximumPotionDisplay, (config, value) -> config.maximumPotionDisplay = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + (int)doubleOpt.get(config));
    public static final DoubleConfigOption POTION_LENGTH_Y_OFFSET = new DoubleConfigOption("potion_length_y_offset", 1.0D, 256.0D, 1.0F, config -> (double)config.potionLengthYOffset, (config, value) -> config.potionLengthYOffset = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + (int)doubleOpt.get(config));
    public static final DoubleConfigOption POTION_LENGTH_Y_OFFSET_OVERLAP = new DoubleConfigOption("potion_length_y_offset_overlap", 1.0D, 256.0D, 1.0F, config -> (double)config.potionLengthYOffsetOverlap, (config, value) -> config.potionLengthYOffsetOverlap = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + (int)doubleOpt.get(config));


    public static final BooleanConfigOption PREVIEW = new BooleanConfigOption("preview", config -> GuiExtendedConfig.preview, (config, value) -> GuiExtendedConfig.preview = value);
    public static final BooleanConfigOption SWAP_INFO_POS = new BooleanConfigOption("swap_info_pos", config -> config.swapRenderInfo, (config, value) -> config.swapRenderInfo = value);
    public static final BooleanConfigOption FPS = new BooleanConfigOption("fps", config -> config.fps, (config, value) -> config.fps = value);
    public static final BooleanConfigOption XYZ = new BooleanConfigOption("xyz", config -> config.xyz, (config, value) -> config.xyz = value);
    public static final BooleanConfigOption DIRECTION = new BooleanConfigOption("direction", config -> config.direction, (config, value) -> config.direction = value);
    public static final BooleanConfigOption BIOME = new BooleanConfigOption("biome", config -> config.biome, (config, value) -> config.biome = value);
    public static final BooleanConfigOption PING = new BooleanConfigOption("ping", config -> config.ping, (config, value) -> config.ping = value);
    public static final BooleanConfigOption PING_TO_SECOND = new BooleanConfigOption("ping_to_second", config -> config.pingToSecond, (config, value) -> config.pingToSecond = value);
    public static final BooleanConfigOption SERVER_IP = new BooleanConfigOption("server_ip", config -> config.serverIP, (config, value) -> config.serverIP = value);
    public static final BooleanConfigOption SERVER_IP_MC = new BooleanConfigOption("server_ip_mc", config -> config.serverIPMCVersion, (config, value) -> config.serverIPMCVersion = value);
    public static final BooleanConfigOption EQUIPMENT_HUD = new BooleanConfigOption("equipment_hud", config -> config.equipmentHUD, (config, value) -> config.equipmentHUD = value);
    public static final BooleanConfigOption POTION_HUD = new BooleanConfigOption("potion_hud", config -> config.potionHUD, (config, value) -> config.potionHUD = value);
    public static final BooleanConfigOption KEYSTROKE = new BooleanConfigOption("keystroke", config -> config.keystroke, (config, value) -> config.keystroke = value);
    public static final BooleanConfigOption KEYSTROKE_LRMB = new BooleanConfigOption("keystroke_lrmb", config -> config.keystrokeMouse, (config, value) -> config.keystrokeMouse = value);
    public static final BooleanConfigOption KEYSTROKE_SS = new BooleanConfigOption("keystroke_ss", config -> config.keystrokeSprintSneak, (config, value) -> config.keystrokeSprintSneak = value);
    public static final BooleanConfigOption KEYSTROKE_BLOCKING = new BooleanConfigOption("keystroke_blocking", config -> config.keystrokeBlocking, (config, value) -> config.keystrokeBlocking = value);
    public static final BooleanConfigOption CPS = new BooleanConfigOption("cps", config -> config.cps, (config, value) -> config.cps = value);
    public static final BooleanConfigOption RCPS = new BooleanConfigOption("rcps", config -> config.rcps, (config, value) -> config.rcps = value);
    public static final BooleanConfigOption SLIME_CHUNK = new BooleanConfigOption("slime_chunk", config -> config.slimeChunkFinder, (config, value) -> config.slimeChunkFinder = value);
    public static final BooleanConfigOption REAL_TIME = new BooleanConfigOption("real_time", config -> config.realTime, (config, value) -> config.realTime = value);
    public static final BooleanConfigOption GAME_TIME = new BooleanConfigOption("game_time", config -> config.gameTime, (config, value) -> config.gameTime = value);
    public static final BooleanConfigOption GAME_WEATHER = new BooleanConfigOption("game_weather", config -> config.gameWeather, (config, value) -> config.gameWeather = value);
    public static final BooleanConfigOption MOON_PHASE = new BooleanConfigOption("moon_phase", config -> config.moonPhase, (config, value) -> config.moonPhase = value);
    public static final BooleanConfigOption POTION_ICON = new BooleanConfigOption("potion_icon", config -> config.potionHUDIcon, (config, value) -> config.potionHUDIcon = value);
    public static final BooleanConfigOption TPS = new BooleanConfigOption("tps", config -> config.tps, (config, value) -> config.tps = value);
    public static final BooleanConfigOption TPS_ALL_DIMS = new BooleanConfigOption("tps_all_dims", config -> config.tpsAllDims, (config, value) -> config.tpsAllDims = value);
    public static final BooleanConfigOption ALTERNATE_POTION_COLOR = new BooleanConfigOption("alternate_potion_color", config -> config.alternatePotionHUDTextColor, (config, value) -> config.alternatePotionHUDTextColor = value);


    public static final BooleanConfigOption KEYSTROKE_WASD_RAINBOW = new BooleanConfigOption("keystroke_wasd_rainbow", config -> config.keystrokeWASDRainbow, (config, value) -> config.keystrokeWASDRainbow = value);
    public static final BooleanConfigOption KEYSTROKE_MOUSE_BUTTON_RAINBOW = new BooleanConfigOption("keystroke_mouse_button_rainbow", config -> config.keystrokeMouseButtonRainbow, (config, value) -> config.keystrokeMouseButtonRainbow = value);
    public static final BooleanConfigOption KEYSTROKE_SPRINT_RAINBOW = new BooleanConfigOption("keystroke_sprint_rainbow", config -> config.keystrokeSprintRainbow, (config, value) -> config.keystrokeSprintRainbow = value);
    public static final BooleanConfigOption KEYSTROKE_SNEAK_RAINBOW = new BooleanConfigOption("keystroke_sneak_rainbow", config -> config.keystrokeSneakRainbow, (config, value) -> config.keystrokeSneakRainbow = value);
    public static final BooleanConfigOption KEYSTROKE_BLOCKING_RAINBOW = new BooleanConfigOption("keystroke_blocking_rainbow", config -> config.keystrokeBlockingRainbow, (config, value) -> config.keystrokeBlockingRainbow = value);
    public static final BooleanConfigOption KEYSTROKE_CPS_RAINBOW = new BooleanConfigOption("keystroke_cps_rainbow", config -> config.keystrokeCPSRainbow, (config, value) -> config.keystrokeCPSRainbow = value);
    public static final BooleanConfigOption KEYSTROKE_RCPS_RAINBOW = new BooleanConfigOption("keystroke_rcps_rainbow", config -> config.keystrokeRCPSRainbow, (config, value) -> config.keystrokeRCPSRainbow = value);


    public static final BooleanConfigOption RIGHT_CLICK_ADD_PARTY = new BooleanConfigOption("right_click_add_party", config -> config.rightClickToAddParty, (config, value) -> config.rightClickToAddParty = value);


    public static final StringConfigOption HEALTH_STATUS = new StringConfigOption("health_status", (config, value) -> config.healthStatusMode = HealthStatusMode.byId(config.healthStatusMode.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.healthStatusMode.getTranslationKey()));
    public static final StringConfigOption KEYSTROKE_POSITION = new StringConfigOption("keystroke_position", (config, value) -> config.keystrokePosition = KeystrokePosition.byId(config.keystrokePosition.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.keystrokePosition.getTranslationKey()));
    public static final StringConfigOption EQUIPMENT_ORDERING = new StringConfigOption("equipment_ordering", (config, value) -> config.equipmentOrdering = Equipments.Ordering.byId(config.equipmentOrdering.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.equipmentOrdering.getTranslationKey()));
    public static final StringConfigOption EQUIPMENT_DIRECTION = new StringConfigOption("equipment_direction", (config, value) -> config.equipmentDirection = Equipments.Direction.byId(config.equipmentDirection.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.equipmentDirection.getTranslationKey()));
    public static final StringConfigOption EQUIPMENT_STATUS = new StringConfigOption("equipment_status", (config, value) -> config.equipmentStatus = Equipments.Status.byId(config.equipmentStatus.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.equipmentStatus.getTranslationKey()));
    public static final StringConfigOption EQUIPMENT_POSITION = new StringConfigOption("equipment_position", (config, value) -> config.equipmentPosition = Equipments.Position.byId(config.equipmentPosition.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.equipmentPosition.getTranslationKey()));
    public static final StringConfigOption POTION_HUD_STYLE = new StringConfigOption("potion_hud_style", (config, value) -> config.potionHUDStyle = StatusEffects.Style.byId(config.potionHUDStyle.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.potionHUDStyle.getTranslationKey()));
    public static final StringConfigOption POTION_HUD_POSITION = new StringConfigOption("potion_hud_position", (config, value) -> config.potionHUDPosition = StatusEffects.Position.byId(config.potionHUDPosition.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.potionHUDPosition.getTranslationKey()));
    public static final StringConfigOption CPS_POSITION = new StringConfigOption("cps_position", (config, value) -> config.cpsPosition = CPSPosition.byId(config.cpsPosition.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.cpsPosition.getTranslationKey()));

    private ExtendedConfig() {}

    public static void setCurrentProfile(String profileName)
    {
        ExtendedConfig.file = new File(userDir, profileName + ".dat");
        currentProfile = profileName;
    }

    public void load()
    {
        try
        {
            CompoundTag nbt = NbtIo.read(ExtendedConfig.file);

            if (nbt == null)
            {
                return;
            }

            // Render Info
            this.fps = this.getBoolean(nbt, "FPS", this.fps);
            this.xyz = this.getBoolean(nbt, "XYZ", this.xyz);
            this.direction = this.getBoolean(nbt, "Direction", this.direction);
            this.biome = this.getBoolean(nbt, "Biome", this.biome);
            this.ping = this.getBoolean(nbt, "Ping", this.ping);
            this.pingToSecond = this.getBoolean(nbt, "PingToSecond", this.pingToSecond);
            this.serverIP = this.getBoolean(nbt, "ServerIP", this.serverIP);
            this.serverIPMCVersion = this.getBoolean(nbt, "ServerIPMCVersion", this.serverIPMCVersion);
            this.equipmentHUD = this.getBoolean(nbt, "EquipmentHUD", this.equipmentHUD);
            this.potionHUD = this.getBoolean(nbt, "PotionHUD", this.potionHUD);
            this.keystroke = this.getBoolean(nbt, "Keystroke", this.keystroke);
            this.keystrokeMouse = this.getBoolean(nbt, "KeystrokeMouse", this.keystrokeMouse);
            this.keystrokeSprintSneak = this.getBoolean(nbt, "KeystrokeSprintSneak", this.keystrokeSprintSneak);
            this.keystrokeBlocking = this.getBoolean(nbt, "KeystrokeBlocking", this.keystrokeBlocking);
            this.cps = this.getBoolean(nbt, "CPS", this.cps);
            this.rcps = this.getBoolean(nbt, "RCPS", this.rcps);
            this.slimeChunkFinder = this.getBoolean(nbt, "SlimeChunkFinder", this.slimeChunkFinder);
            this.realTime = this.getBoolean(nbt, "RealTime", this.realTime);
            this.gameTime = this.getBoolean(nbt, "GameTime", this.gameTime);
            this.gameWeather = this.getBoolean(nbt, "GameWeather", this.gameWeather);
            this.moonPhase = this.getBoolean(nbt, "MoonPhase", this.moonPhase);
            this.potionHUDIcon = this.getBoolean(nbt, "PotionHUDIcon", this.potionHUDIcon);
            this.tps = this.getBoolean(nbt, "TPS", this.tps);
            this.tpsAllDims = this.getBoolean(nbt, "TPSAllDimensions", this.tpsAllDims);
            this.alternatePotionHUDTextColor = this.getBoolean(nbt, "AlternatePotionHUDTextColor", this.alternatePotionHUDTextColor);

            // Main
            this.swapRenderInfo = this.getBoolean(nbt, "SwapRenderInfo", this.swapRenderInfo);
            this.showCustomCape = this.getBoolean(nbt, "ShowCustomCape", this.showCustomCape);
            this.healthStatusMode = HealthStatusMode.byId(this.getInteger(nbt, "HealthStatusMode", this.healthStatusMode.getId()));
            this.keystrokePosition = KeystrokePosition.byId(this.getInteger(nbt, "KeystrokePosition", this.keystrokePosition.getId()));
            this.equipmentOrdering = Equipments.Ordering.byId(this.getInteger(nbt, "EquipmentOrdering", this.equipmentOrdering.getId()));
            this.equipmentDirection = Equipments.Direction.byId(this.getInteger(nbt, "EquipmentDirection", this.equipmentDirection.getId()));
            this.equipmentStatus = Equipments.Status.byId(this.getInteger(nbt, "EquipmentStatus", this.equipmentStatus.getId()));
            this.equipmentPosition = Equipments.Position.byId(this.getInteger(nbt, "EquipmentPosition", this.equipmentPosition.getId()));
            this.potionHUDStyle = StatusEffects.Style.byId(this.getInteger(nbt, "PotionHUDStyle", this.potionHUDStyle.getId()));
            this.potionHUDPosition = StatusEffects.Position.byId(this.getInteger(nbt, "PotionHUDPosition", this.potionHUDPosition.getId()));
            this.cpsPosition = CPSPosition.byId(this.getInteger(nbt, "CPSPosition", this.cpsPosition.getId()));
            this.cpsOpacity = this.getDouble(nbt, "CPSOpacity", this.cpsOpacity);

            // Movement
            this.toggleSprint = this.getBoolean(nbt, "ToggleSprint", this.toggleSprint);
            this.toggleSneak = this.getBoolean(nbt, "ToggleSneak", this.toggleSneak);

            // Offset
            this.keystrokeYOffset = this.getInteger(nbt, "KeystrokeYOffset", this.keystrokeYOffset);
            this.armorHUDYOffset = this.getInteger(nbt, "ArmorHUDYOffset", this.armorHUDYOffset);
            this.potionHUDYOffset = this.getInteger(nbt, "PotionHUDYOffset", this.potionHUDYOffset);
            this.maximumPotionDisplay = this.getInteger(nbt, "MaximumPotionDisplay", this.maximumPotionDisplay);
            this.potionLengthYOffset = this.getInteger(nbt, "PotionLengthYOffset", this.potionLengthYOffset);
            this.potionLengthYOffsetOverlap = this.getInteger(nbt, "PotionLengthYOffsetOverlap", this.potionLengthYOffsetOverlap);

            // Custom Color
            this.fpsColor = this.getString(nbt, "FPSColor", this.fpsColor);
            this.xyzColor = this.getString(nbt, "XYZColor", this.xyzColor);
            this.biomeColor = this.getString(nbt, "BiomeColor", this.biomeColor);
            this.directionColor = this.getString(nbt, "DirectionColor", this.directionColor);
            this.pingColor = this.getString(nbt, "PingColor", this.pingColor);
            this.pingToSecondColor = this.getString(nbt, "PingToSecondColor", this.pingToSecondColor);
            this.serverIPColor = this.getString(nbt, "ServerIPColor", this.serverIPColor);
            this.equipmentStatusColor = this.getString(nbt, "EquipmentStatusColor", this.equipmentStatusColor);
            this.arrowCountColor = this.getString(nbt, "ArrowCountColor", this.arrowCountColor);
            this.cpsColor = this.getString(nbt, "CPSColor", this.cpsColor);
            this.rcpsColor = this.getString(nbt, "RCPSColor", this.rcpsColor);
            this.slimeChunkColor = this.getString(nbt, "SlimeChunkColor", this.slimeChunkColor);
            this.topDonatorNameColor = this.getString(nbt, "TopDonatorNameColor", this.topDonatorNameColor);
            this.recentDonatorNameColor = this.getString(nbt, "RecentDonatorNameColor", this.recentDonatorNameColor);
            this.tpsColor = this.getString(nbt, "TPSColor", this.tpsColor);
            this.realTimeColor = this.getString(nbt, "RealTimeColor", this.realTimeColor);
            this.gameTimeColor = this.getString(nbt, "GameTimeColor", this.gameTimeColor);
            this.gameWeatherColor = this.getString(nbt, "GameWeatherColor", this.gameWeatherColor);
            this.moonPhaseColor = this.getString(nbt, "MoonPhaseColor", this.moonPhaseColor);
            this.ytChatViewCountColor = this.getString(nbt, "YTChatViewCountColor", this.ytChatViewCountColor);

            // Custom Color : Value
            this.fpsValueColor = this.getString(nbt, "FPSValueColor", this.fpsValueColor);
            this.fps26And49Color = this.getString(nbt, "FPS26And49Color", this.fps26And49Color);
            this.fpsLow25Color = this.getString(nbt, "FPSLow25Color", this.fpsLow25Color);
            this.xyzValueColor = this.getString(nbt, "XYZValueColor", this.xyzValueColor);
            this.biomeValueColor = this.getString(nbt, "BiomeValueColor", this.biomeValueColor);
            this.directionValueColor = this.getString(nbt, "DirectionValueColor", this.directionValueColor);
            this.pingValueColor = this.getString(nbt, "PingValueColor", this.pingValueColor);
            this.ping200And300Color = this.getString(nbt, "Ping200And300Color", this.ping200And300Color);
            this.ping300And500Color = this.getString(nbt, "Ping300And500Color", this.ping300And500Color);
            this.pingMax500Color = this.getString(nbt, "PingMax500Color", this.pingMax500Color);
            this.serverIPValueColor = this.getString(nbt, "ServerIPValueColor", this.serverIPValueColor);
            this.cpsValueColor = this.getString(nbt, "CPSValueColor", this.cpsValueColor);
            this.rcpsValueColor = this.getString(nbt, "RCPSValueColor", this.rcpsValueColor);
            this.slimeChunkValueColor = this.getString(nbt, "SlimeChunkValueColor", this.slimeChunkValueColor);
            this.topDonatorValueColor = this.getString(nbt, "TopDonatorValueColor", this.topDonatorValueColor);
            this.recentDonatorValueColor = this.getString(nbt, "RecentDonatorValueColor", this.recentDonatorValueColor);
            this.tpsValueColor = this.getString(nbt, "TPSValueColor", this.tpsValueColor);
            this.realTimeHHMMSSValueColor = this.getString(nbt, "RealTimeHHMMSSValueColor", this.realTimeHHMMSSValueColor);
            this.realTimeDDMMYYValueColor = this.getString(nbt, "RealTimeDDMMYYValueColor", this.realTimeDDMMYYValueColor);
            this.gameTimeValueColor = this.getString(nbt, "GameTimeValueColor", this.gameTimeValueColor);
            this.gameWeatherValueColor = this.getString(nbt, "GameWeatherValueColor", this.gameWeatherValueColor);
            this.moonPhaseValueColor = this.getString(nbt, "MoonPhaseValueColor", this.moonPhaseValueColor);
            this.ytChatViewCountValueColor = this.getString(nbt, "YTChatViewCountValueColor", this.ytChatViewCountValueColor);

            // Custom Color : Keystroke
            this.keystrokeWASDColor = this.getString(nbt, "KeystrokeWASDColor", this.keystrokeWASDColor);
            this.keystrokeMouseButtonColor = this.getString(nbt, "KeystrokeMouseButtonColor", this.keystrokeMouseButtonColor);
            this.keystrokeSprintColor = this.getString(nbt, "KeystrokeSprintColor", this.keystrokeSprintColor);
            this.keystrokeSneakColor = this.getString(nbt, "KeystrokeSneakColor", this.keystrokeSneakColor);
            this.keystrokeBlockingColor = this.getString(nbt, "KeystrokeBlockingColor", this.keystrokeBlockingColor);
            this.keystrokeCPSColor = this.getString(nbt, "KeystrokeCPSColor", this.keystrokeCPSColor);
            this.keystrokeRCPSColor = this.getString(nbt, "KeystrokeRCPSColor", this.keystrokeRCPSColor);
            this.keystrokeWASDRainbow = this.getBoolean(nbt, "KeystrokeWASDRainbow", this.keystrokeWASDRainbow);
            this.keystrokeMouseButtonRainbow = this.getBoolean(nbt, "KeystrokeMouseButtonRainbow", this.keystrokeMouseButtonRainbow);
            this.keystrokeSprintRainbow = this.getBoolean(nbt, "KeystrokeSprintRainbow", this.keystrokeSprintRainbow);
            this.keystrokeSneakRainbow = this.getBoolean(nbt, "KeystrokeSneakRainbow", this.keystrokeSneakRainbow);
            this.keystrokeBlockingRainbow = this.getBoolean(nbt, "KeystrokeBlockingRainbow", this.keystrokeBlockingRainbow);
            this.keystrokeCPSRainbow = this.getBoolean(nbt, "KeystrokeCPSRainbow", this.keystrokeCPSRainbow);
            this.keystrokeRCPSRainbow = this.getBoolean(nbt, "KeystrokeRCPSRainbow", this.keystrokeRCPSRainbow);

            // Misc
            this.toggleSprintUseMode = this.getString(nbt, "ToggleSprintUseMode", this.toggleSprintUseMode);
            this.toggleSneakUseMode = this.getString(nbt, "ToggleSneakUseMode", this.toggleSneakUseMode);
            this.cpsCustomXOffset = this.getInteger(nbt, "CPSCustomOffsetX", this.cpsCustomXOffset);
            this.cpsCustomYOffset = this.getInteger(nbt, "CPSCustomOffsetY", this.cpsCustomYOffset);
            this.slimeChunkSeed = this.getLong(nbt, "SlimeChunkSeed", this.slimeChunkSeed);
            this.topDonatorFilePath = this.getString(nbt, "TopDonatorFilePath", this.topDonatorFilePath);
            this.recentDonatorFilePath = this.getString(nbt, "RecentDonatorFilePath", this.recentDonatorFilePath);
            this.topDonatorText = this.getString(nbt, "TopDonatorText", this.topDonatorText);
            this.recentDonatorText = this.getString(nbt, "RecentDonatorText", this.recentDonatorText);
            this.realmsMessage = this.getString(nbt, "RealmsMessage", this.realmsMessage);

            // Hypixel
            this.rightClickToAddParty = this.getBoolean(nbt, "RightClickToAddParty", this.rightClickToAddParty);
            this.hypixelNickName = this.getString(nbt, "HypixelNickName", this.hypixelNickName);
            this.selectedHypixelMinigame = this.getInteger(nbt, "SelectedHypixelMinigame", this.selectedHypixelMinigame);
            this.hypixelMinigameScrollPos = this.getInteger(nbt, "HypixelMinigameScrollPos", this.hypixelMinigameScrollPos);

            ExtendedConfig.readAutoLoginData(nbt.getList("AutoLoginData", 10));
            HideNameData.load(nbt.getList("HideNameList", 10));

            IndicatiaMod.LOGGER.info("Loading extended config {}", ExtendedConfig.file.getPath());
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
            CompoundTag nbt = new CompoundTag();

            // Render Info
            nbt.putBoolean("FPS", this.fps);
            nbt.putBoolean("XYZ", this.xyz);
            nbt.putBoolean("Direction", this.direction);
            nbt.putBoolean("Biome", this.biome);
            nbt.putBoolean("Ping", this.ping);
            nbt.putBoolean("PingToSecond", this.pingToSecond);
            nbt.putBoolean("ServerIP", this.serverIP);
            nbt.putBoolean("ServerIPMCVersion", this.serverIPMCVersion);
            nbt.putBoolean("EquipmentHUD", this.equipmentHUD);
            nbt.putBoolean("PotionHUD", this.potionHUD);
            nbt.putBoolean("Keystroke", this.keystroke);
            nbt.putBoolean("KeystrokeMouse", this.keystrokeMouse);
            nbt.putBoolean("KeystrokeSprintSneak", this.keystrokeSprintSneak);
            nbt.putBoolean("KeystrokeBlocking", this.keystrokeBlocking);
            nbt.putBoolean("CPS", this.cps);
            nbt.putBoolean("RCPS", this.rcps);
            nbt.putBoolean("SlimeChunkFinder", this.slimeChunkFinder);
            nbt.putBoolean("RealTime", this.realTime);
            nbt.putBoolean("GameTime", this.gameTime);
            nbt.putBoolean("GameWeather", this.gameWeather);
            nbt.putBoolean("MoonPhase", this.moonPhase);
            nbt.putBoolean("PotionHUDIcon", this.potionHUDIcon);
            nbt.putBoolean("TPS", this.tps);
            nbt.putBoolean("TPSAllDimensions", this.tpsAllDims);
            nbt.putBoolean("AlternatePotionHUDTextColor", this.alternatePotionHUDTextColor);

            // Main
            nbt.putBoolean("ShowCustomCape", this.showCustomCape);
            nbt.putBoolean("SwapRenderInfo", this.swapRenderInfo);
            nbt.putInt("HealthStatusMode", this.healthStatusMode.getId());
            nbt.putInt("KeystrokePosition", this.keystrokePosition.getId());
            nbt.putInt("EquipmentOrdering", this.equipmentOrdering.getId());
            nbt.putInt("EquipmentDirection", this.equipmentDirection.getId());
            nbt.putInt("EquipmentStatus", this.equipmentStatus.getId());
            nbt.putInt("EquipmentPosition", this.equipmentPosition.getId());
            nbt.putInt("PotionHUDStyle", this.potionHUDStyle.getId());
            nbt.putInt("PotionHUDPosition", this.potionHUDPosition.getId());
            nbt.putInt("CPSPosition", this.cpsPosition.getId());
            nbt.putDouble("CPSOpacity", this.cpsOpacity);

            // Movement
            nbt.putBoolean("ToggleSprint", this.toggleSprint);
            nbt.putBoolean("ToggleSneak", this.toggleSneak);

            // Offset
            nbt.putInt("KeystrokeYOffset", this.keystrokeYOffset);
            nbt.putInt("ArmorHUDYOffset", this.armorHUDYOffset);
            nbt.putInt("PotionHUDYOffset", this.potionHUDYOffset);
            nbt.putInt("MaximumPotionDisplay", this.maximumPotionDisplay);
            nbt.putInt("PotionLengthYOffset", this.potionLengthYOffset);
            nbt.putInt("PotionLengthYOffsetOverlap", this.potionLengthYOffsetOverlap);

            // Custom Color
            nbt.putString("FPSColor", this.fpsColor);
            nbt.putString("XYZColor", this.xyzColor);
            nbt.putString("BiomeColor", this.biomeColor);
            nbt.putString("DirectionColor", this.directionColor);
            nbt.putString("PingColor", this.pingColor);
            nbt.putString("PingToSecondColor", this.pingToSecondColor);
            nbt.putString("ServerIPColor", this.serverIPColor);
            nbt.putString("EquipmentStatusColor", this.equipmentStatusColor);
            nbt.putString("ArrowCountColor", this.arrowCountColor);
            nbt.putString("CPSColor", this.cpsColor);
            nbt.putString("RCPSColor", this.rcpsColor);
            nbt.putString("SlimeChunkColor", this.slimeChunkColor);
            nbt.putString("TopDonatorNameColor", this.topDonatorNameColor);
            nbt.putString("RecentDonatorNameColor", this.recentDonatorNameColor);
            nbt.putString("TPSColor", this.tpsColor);
            nbt.putString("RealTimeColor", this.realTimeColor);
            nbt.putString("GameTimeColor", this.gameTimeColor);
            nbt.putString("GameWeatherColor", this.gameWeatherColor);
            nbt.putString("MoonPhaseColor", this.moonPhaseColor);

            // Custom Color : Value
            nbt.putString("FPSValueColor", this.fpsValueColor);
            nbt.putString("FPS26And49Color", this.fps26And49Color);
            nbt.putString("FPSLow25Color", this.fpsLow25Color);
            nbt.putString("XYZValueColor", this.xyzValueColor);
            nbt.putString("BiomeValueColor", this.biomeValueColor);
            nbt.putString("DirectionValueColor", this.directionValueColor);
            nbt.putString("PingValueColor", this.pingValueColor);
            nbt.putString("Ping200And300Color", this.ping200And300Color);
            nbt.putString("Ping300And500Color", this.ping300And500Color);
            nbt.putString("PingMax500Color", this.pingMax500Color);
            nbt.putString("ServerIPValueColor", this.serverIPValueColor);
            nbt.putString("CPSValueColor", this.cpsValueColor);
            nbt.putString("RCPSValueColor", this.rcpsValueColor);
            nbt.putString("SlimeChunkValueColor", this.slimeChunkValueColor);
            nbt.putString("TopDonatorValueColor", this.topDonatorValueColor);
            nbt.putString("RecentDonatorValueColor", this.recentDonatorValueColor);
            nbt.putString("TPSValueColor", this.tpsValueColor);
            nbt.putString("RealTimeHHMMSSValueColor", this.realTimeHHMMSSValueColor);
            nbt.putString("RealTimeDDMMYYValueColor", this.realTimeDDMMYYValueColor);
            nbt.putString("GameTimeValueColor", this.gameTimeValueColor);
            nbt.putString("GameWeatherValueColor", this.gameWeatherValueColor);
            nbt.putString("MoonPhaseValueColor", this.moonPhaseValueColor);

            // Custom Color : Keystroke
            nbt.putString("KeystrokeWASDColor", this.keystrokeWASDColor);
            nbt.putString("KeystrokeMouseButtonColor", this.keystrokeMouseButtonColor);
            nbt.putString("KeystrokeSprintColor", this.keystrokeSprintColor);
            nbt.putString("KeystrokeSneakColor", this.keystrokeSneakColor);
            nbt.putString("KeystrokeBlockingColor", this.keystrokeBlockingColor);
            nbt.putString("KeystrokeCPSColor", this.keystrokeCPSColor);
            nbt.putString("KeystrokeRCPSColor", this.keystrokeRCPSColor);
            nbt.putBoolean("KeystrokeWASDRainbow", this.keystrokeWASDRainbow);
            nbt.putBoolean("KeystrokeMouseButtonRainbow", this.keystrokeMouseButtonRainbow);
            nbt.putBoolean("KeystrokeSprintRainbow", this.keystrokeSprintRainbow);
            nbt.putBoolean("KeystrokeSneakRainbow", this.keystrokeSneakRainbow);
            nbt.putBoolean("KeystrokeBlockingRainbow", this.keystrokeBlockingRainbow);
            nbt.putBoolean("KeystrokeCPSRainbow", this.keystrokeCPSRainbow);
            nbt.putBoolean("KeystrokeRCPSRainbow", this.keystrokeRCPSRainbow);

            // Misc
            nbt.putString("ToggleSprintUseMode", this.toggleSprintUseMode);
            nbt.putString("ToggleSneakUseMode", this.toggleSneakUseMode);
            nbt.putInt("CPSCustomOffsetX", this.cpsCustomXOffset);
            nbt.putInt("CPSCustomOffsetY", this.cpsCustomYOffset);
            nbt.putLong("SlimeChunkSeed", this.slimeChunkSeed);
            nbt.putString("TopDonatorFilePath", this.topDonatorFilePath);
            nbt.putString("RecentDonatorFilePath", this.recentDonatorFilePath);
            nbt.putString("TopDonatorText", this.topDonatorText);
            nbt.putString("RecentDonatorText", this.recentDonatorText);
            nbt.putString("RealmsMessage", this.realmsMessage);

            // Hypixel
            nbt.putBoolean("RightClickToAddParty", this.rightClickToAddParty);
            nbt.putString("HypixelNickName", this.hypixelNickName);
            nbt.putInt("SelectedHypixelMinigame", this.selectedHypixelMinigame);
            nbt.putInt("HypixelMinigameScrollPos", this.hypixelMinigameScrollPos);

            nbt.put("AutoLoginData", ExtendedConfig.writeAutoLoginData());
            nbt.put("HideNameList", HideNameData.save());

            NbtIo.safeWrite(nbt, !profileName.equalsIgnoreCase("default") ? new File(userDir, profileName + ".dat") : ExtendedConfig.file);
        }
        catch (Exception e) {}
    }

    public static void saveProfileFile(String profileName)
    {
        File profile = new File(ExtendedConfig.userDir, "profile.txt");

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(profile), StandardCharsets.UTF_8)))
        {
            writer.println("profile:" + profileName);
            IndicatiaMod.LOGGER.info("Saving profile name!");
        }
        catch (IOException e)
        {
            IndicatiaMod.LOGGER.error("Failed to save profiles", (Throwable)e);
        }
    }

    private static ListTag writeAutoLoginData()
    {
        ListTag list = new ListTag();

        ExtendedConfig.loginData.getAutoLoginList().forEach(data ->
        {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("ServerIP", data.getServerIP());
            nbt.putString("CommandName", data.getCommand());
            nbt.putString("Value", data.getValue());
            nbt.putString("UUID", data.getUUID().toString());
            nbt.putString("Function", data.getFunction());
            list.add(nbt);
        });
        return list;
    }

    private static void readAutoLoginData(ListTag list)
    {
        for (int i = 0; i < list.size(); ++i)
        {
            CompoundTag nbt = list.getCompoundTag(i);
            ExtendedConfig.loginData.addAutoLogin(nbt.getString("ServerIP"), nbt.getString("CommandName"), nbt.getString("Value"), UUID.fromString(nbt.getString("UUID")), nbt.getString("Function"));
        }
    }

    private boolean getBoolean(CompoundTag nbt, String key, boolean defaultValue)
    {
        if (nbt.containsKey(key, 99))
        {
            return nbt.getBoolean(key);
        }
        else
        {
            return defaultValue;
        }
    }

    private int getInteger(CompoundTag nbt, String key, int defaultValue)
    {
        if (nbt.containsKey(key, 99))
        {
            return nbt.getInt(key);
        }
        else
        {
            return defaultValue;
        }
    }

    private double getDouble(CompoundTag nbt, String key, double defaultValue)
    {
        if (nbt.containsKey(key, 99))
        {
            return nbt.getDouble(key);
        }
        else
        {
            return defaultValue;
        }
    }

    private String getString(CompoundTag nbt, String key, String defaultValue)
    {
        if (nbt.containsKey(key, 8))
        {
            return nbt.getString(key);
        }
        else
        {
            return defaultValue;
        }
    }

    private long getLong(CompoundTag nbt, String key, long defaultValue)
    {
        if (nbt.containsKey(key, 99))
        {
            return nbt.getLong(key);
        }
        else
        {
            return defaultValue;
        }
    }

    /*public String getKeyBinding(ExtendedConfig.Options options)
    {
        String name = options.getTranslation() + ": ";

        if (options.isDouble())
        {
            double value = this.getOptionDoubleValue(options);
            return name + (int)value;
        }
        else if (options.isBoolean())
        {
            boolean flag = this.getOptionOrdinalValue(options);
            return flag ? name + TextFormat.GREEN + "true" : name + TextFormat.RED + "false";
        }
        else if (options.isTextbox())
        {
            return this.getOptionStringValue(options);
        }
        else if (options == this.Options.HEALTH_STATUS)
        {
            return name + this.getTranslation(HEALTH_STATUS, this.healthStatusMode);
        }
        else if (options == this.Options.KEYSTROKE_POSITION)
        {
            return name + this.getTranslation(POSITION, this.keystrokePosition);
        }
        else if (options == this.Options.EQUIPMENT_ORDERING)
        {
            return name + this.getTranslation(EQUIPMENT_ORDERING, this.equipmentOrdering);
        }
        else if (options == this.Options.EQUIPMENT_DIRECTION)
        {
            return name + this.getTranslation(EQUIPMENT_DIRECTION, this.equipmentDirection);
        }
        else if (options == this.Options.EQUIPMENT_STATUS)
        {
            return name + this.getTranslation(EQUIPMENT_STATUS, this.equipmentStatus);
        }
        else if (options == this.Options.EQUIPMENT_POSITION)
        {
            return name + this.getTranslation(EQUIPMENT_POSITION, this.equipmentPosition);
        }
        else if (options == this.Options.POTION_HUD_STYLE)
        {
            return name + this.getTranslation(POTION_STATUS_HUD_STYLE, this.potionHUDStyle);
        }
        else if (options == this.Options.POTION_HUD_POSITION)
        {
            return name + this.getTranslation(POTION_STATUS_HUD_POSITION, this.potionHUDPosition);
        }
        else if (options == this.Options.CPS_POSITION)
        {
            return name + this.getTranslation(CPS_POSITION, this.cpsPosition);
        }
        else
        {
            return name;
        }
    }

    public void setOptionValue(this.Options options, int value)
    {
        if (options == this.Options.PREVIEW)
        {
            Guithis.preview = !Guithis.preview;
        }
        else if (options == this.Options.SWAP_INFO_POS)
        {
            this.swapRenderInfo = !this.swapRenderInfo;
        }
        else if (options == this.Options.HEALTH_STATUS)
        {
            this.healthStatusMode = (this.healthStatusMode + value) % 3;
        }
        else if (options == this.Options.KEYSTROKE_POSITION)
        {
            this.keystrokePosition = (this.keystrokePosition + value) % 2;
        }
        else if (options == this.Options.EQUIPMENT_ORDERING)
        {
            this.equipmentOrdering = (this.equipmentOrdering + value) % 2;
        }
        else if (options == this.Options.EQUIPMENT_DIRECTION)
        {
            this.equipmentDirection = (this.equipmentDirection + value) % 2;
        }
        else if (options == this.Options.EQUIPMENT_STATUS)
        {
            this.equipmentStatus = (this.equipmentStatus + value) % 4;
        }
        else if (options == this.Options.EQUIPMENT_POSITION)
        {
            this.equipmentPosition = (this.equipmentPosition + value) % 3;
        }
        else if (options == this.Options.POTION_HUD_STYLE)
        {
            this.potionHUDStyle = (this.potionHUDStyle + value) % 2;
        }
        else if (options == this.Options.POTION_HUD_POSITION)
        {
            this.potionHUDPosition = (this.potionHUDPosition + value) % 4;
        }
        else if (options == this.Options.CPS_POSITION)
        {
            this.cpsPosition = (this.cpsPosition + value) % 4;
        }

        else if (options == this.Options.FPS)
        {
            this.fps = !this.fps;
        }
        else if (options == this.Options.XYZ)
        {
            this.xyz = !this.xyz;
        }
        else if (options == this.Options.DIRECTION)
        {
            this.direction = !this.direction;
        }
        else if (options == this.Options.BIOME)
        {
            this.biome = !this.biome;
        }
        else if (options == this.Options.PING)
        {
            this.ping = !this.ping;
        }
        else if (options == this.Options.PING_TO_SECOND)
        {
            this.pingToSecond = !this.pingToSecond;
        }
        else if (options == this.Options.SERVER_IP)
        {
            this.serverIP = !this.serverIP;
        }
        else if (options == this.Options.SERVER_IP_MC)
        {
            this.serverIPMCVersion = !this.serverIPMCVersion;
        }
        else if (options == this.Options.EQUIPMENT_HUD)
        {
            this.equipmentHUD = !this.equipmentHUD;
        }
        else if (options == this.Options.POTION_HUD)
        {
            this.potionHUD = !this.potionHUD;
        }
        else if (options == this.Options.KEYSTROKE)
        {
            this.keystroke = !this.keystroke;
        }
        else if (options == this.Options.KEYSTROKE_LRMB)
        {
            this.keystrokeMouse = !this.keystrokeMouse;
        }
        else if (options == this.Options.KEYSTROKE_SS)
        {
            this.keystrokeSprintSneak = !this.keystrokeSprintSneak;
        }
        else if (options == this.Options.KEYSTROKE_BLOCKING)
        {
            this.keystrokeBlocking = !this.keystrokeBlocking;
        }
        else if (options == this.Options.CPS)
        {
            this.cps = !this.cps;
        }
        else if (options == this.Options.RCPS)
        {
            this.rcps = !this.rcps;
        }
        else if (options == this.Options.SLIME_CHUNK)
        {
            this.slimeChunkFinder = !this.slimeChunkFinder;
        }
        else if (options == this.Options.REAL_TIME)
        {
            this.realTime = !this.realTime;
        }
        else if (options == this.Options.GAME_TIME)
        {
            this.gameTime = !this.gameTime;
        }
        else if (options == this.Options.GAME_WEATHER)
        {
            this.gameWeather = !this.gameWeather;
        }
        else if (options == this.Options.MOON_PHASE)
        {
            this.moonPhase = !this.moonPhase;
        }
        else if (options == this.Options.POTION_ICON)
        {
            this.potionHUDIcon = !this.potionHUDIcon;
        }
        else if (options == this.Options.TPS)
        {
            this.tps = !this.tps;
        }
        else if (options == this.Options.TPS_ALL_DIMS)
        {
            this.tpsAllDims = !this.tpsAllDims;
        }
        else if (options == this.Options.ALTERNATE_POTION_COLOR)
        {
            this.alternatePotionHUDTextColor = !this.alternatePotionHUDTextColor;
        }
        else if (options == this.Options.KEYSTROKE_WASD_RAINBOW)
        {
            this.keystrokeWASDRainbow = !this.keystrokeWASDRainbow;
        }
        else if (options == this.Options.KEYSTROKE_MOUSE_BUTTON_RAINBOW)
        {
            this.keystrokeMouseButtonRainbow = !this.keystrokeMouseButtonRainbow;
        }
        else if (options == this.Options.KEYSTROKE_SPRINT_RAINBOW)
        {
            this.keystrokeSprintRainbow = !this.keystrokeSprintRainbow;
        }
        else if (options == this.Options.KEYSTROKE_SNEAK_RAINBOW)
        {
            this.keystrokeSneakRainbow = !this.keystrokeSneakRainbow;
        }
        else if (options == this.Options.KEYSTROKE_BLOCKING_RAINBOW)
        {
            this.keystrokeBlockingRainbow = !this.keystrokeBlockingRainbow;
        }
        else if (options == this.Options.KEYSTROKE_CPS_RAINBOW)
        {
            this.keystrokeCPSRainbow = !this.keystrokeCPSRainbow;
        }
        else if (options == this.Options.KEYSTROKE_RCPS_RAINBOW)
        {
            this.keystrokeRCPSRainbow = !this.keystrokeRCPSRainbow;
        }

        else if (options == this.Options.RIGHT_CLICK_ADD_PARTY)
        {
            this.rightClickToAddParty = !this.rightClickToAddParty;
        }
    }

    public void setOptionDoubleValue(this.Options options, double value)
    {
        if (options == this.Options.ARMOR_HUD_Y)
        {
            this.armorHUDYOffset = (int) value;
        }
        else if (options == this.Options.POTION_HUD_Y)
        {
            this.potionHUDYOffset = (int) value;
        }
        else if (options == this.Options.KEYSTROKE_Y)
        {
            this.keystrokeYOffset = (int) value;
        }
        else if (options == this.Options.MAXIMUM_POTION_DISPLAY)
        {
            this.maximumPotionDisplay = (int) value;
        }
        else if (options == this.Options.POTION_LENGTH_Y_OFFSET)
        {
            this.potionLengthYOffset = (int) value;
        }
        else if (options == this.Options.POTION_LENGTH_Y_OFFSET_OVERLAP)
        {
            this.potionLengthYOffsetOverlap = (int) value;
        }
        else if (options == this.Options.CPS_OPACITY)
        {
            this.cpsOpacity = value;
        }
    }

    public void setOptionStringValue(this.Options options, String value)
    {
        if (options == this.Options.FPS_COLOR)
        {
            this.fpsColor = value;
        }
        else if (options == this.Options.XYZ_COLOR)
        {
            this.xyzColor = value;
        }
        else if (options == this.Options.BIOME_COLOR)
        {
            this.biomeColor = value;
        }
        else if (options == this.Options.DIRECTION_COLOR)
        {
            this.directionColor = value;
        }
        else if (options == this.Options.PING_COLOR)
        {
            this.pingColor = value;
        }
        else if (options == this.Options.PING_TO_SECOND_COLOR)
        {
            this.pingToSecondColor = value;
        }
        else if (options == this.Options.SERVER_IP_COLOR)
        {
            this.serverIPColor = value;
        }
        else if (options == this.Options.EQUIPMENT_STATUS_COLOR)
        {
            this.equipmentStatusColor = value;
        }
        else if (options == this.Options.ARROW_COUNT_COLOR)
        {
            this.arrowCountColor = value;
        }
        else if (options == this.Options.CPS_COLOR)
        {
            this.cpsColor = value;
        }
        else if (options == this.Options.RCPS_COLOR)
        {
            this.rcpsColor = value;
        }
        else if (options == this.Options.SLIME_CHUNK_COLOR)
        {
            this.slimeChunkColor = value;
        }
        else if (options == this.Options.TOP_DONATOR_NAME_COLOR)
        {
            this.topDonatorNameColor = value;
        }
        else if (options == this.Options.RECENT_DONATOR_NAME_COLOR)
        {
            this.recentDonatorNameColor = value;
        }
        else if (options == this.Options.TPS_COLOR)
        {
            this.tpsColor = value;
        }
        else if (options == this.Options.REAL_TIME_COLOR)
        {
            this.realTimeColor = value;
        }
        else if (options == this.Options.GAME_TIME_COLOR)
        {
            this.gameTimeColor = value;
        }
        else if (options == this.Options.GAME_WEATHER_COLOR)
        {
            this.gameWeatherColor = value;
        }
        else if (options == this.Options.MOON_PHASE_COLOR)
        {
            this.moonPhaseColor = value;
        }
        else if (options == this.Options.YTCHAT_VIEW_COUNT_COLOR)
        {
            this.ytChatViewCountColor = value;
        }

        else if (options == this.Options.FPS_VALUE_COLOR)
        {
            this.fpsValueColor = value;
        }
        else if (options == this.Options.FPS_26_AND_40_COLOR)
        {
            this.fps26And49Color = value;
        }
        else if (options == this.Options.FPS_LOW_25_COLOR)
        {
            this.fpsLow25Color = value;
        }
        else if (options == this.Options.XYZ_VALUE_COLOR)
        {
            this.xyzValueColor = value;
        }
        else if (options == this.Options.DIRECTION_VALUE_COLOR)
        {
            this.directionValueColor = value;
        }
        else if (options == this.Options.BIOME_VALUE_COLOR)
        {
            this.biomeValueColor = value;
        }
        else if (options == this.Options.PING_VALUE_COLOR)
        {
            this.pingValueColor = value;
        }
        else if (options == this.Options.PING_200_AND_300_COLOR)
        {
            this.ping200And300Color = value;
        }
        else if (options == this.Options.PING_300_AND_500_COLOR)
        {
            this.ping300And500Color = value;
        }
        else if (options == this.Options.PING_MAX_500_COLOR)
        {
            this.pingMax500Color = value;
        }
        else if (options == this.Options.SERVER_IP_VALUE_COLOR)
        {
            this.serverIPValueColor = value;
        }
        else if (options == this.Options.CPS_VALUE_COLOR)
        {
            this.cpsValueColor = value;
        }
        else if (options == this.Options.RCPS_VALUE_COLOR)
        {
            this.rcpsValueColor = value;
        }
        else if (options == this.Options.SLIME_CHUNK_VALUE_COLOR)
        {
            this.slimeChunkValueColor = value;
        }
        else if (options == this.Options.TOP_DONATOR_VALUE_COLOR)
        {
            this.topDonatorValueColor = value;
        }
        else if (options == this.Options.RECENT_DONATOR_VALUE_COLOR)
        {
            this.recentDonatorValueColor = value;
        }
        else if (options == this.Options.TPS_VALUE_COLOR)
        {
            this.tpsValueColor = value;
        }
        else if (options == this.Options.REAL_TIME_HHMMSS_VALUE_COLOR)
        {
            this.realTimeHHMMSSValueColor = value;
        }
        else if (options == this.Options.REAL_TIME_DDMMYY_VALUE_COLOR)
        {
            this.realTimeDDMMYYValueColor = value;
        }
        else if (options == this.Options.GAME_TIME_VALUE_COLOR)
        {
            this.gameTimeValueColor = value;
        }
        else if (options == this.Options.GAME_WEATHER_VALUE_COLOR)
        {
            this.gameWeatherValueColor = value;
        }
        else if (options == this.Options.MOON_PHASE_VALUE_COLOR)
        {
            this.moonPhaseValueColor = value;
        }
        else if (options == this.Options.YTCHAT_VIEW_COUNT_VALUE_COLOR)
        {
            this.ytChatViewCountValueColor = value;
        }

        else if (options == this.Options.KEYSTROKE_WASD_COLOR)
        {
            this.keystrokeWASDColor = value;
        }
        else if (options == this.Options.KEYSTROKE_MOUSE_BUTTON_COLOR)
        {
            this.keystrokeMouseButtonColor = value;
        }
        else if (options == this.Options.KEYSTROKE_SPRINT_COLOR)
        {
            this.keystrokeSprintColor = value;
        }
        else if (options == this.Options.KEYSTROKE_SNEAK_COLOR)
        {
            this.keystrokeSneakColor = value;
        }
        else if (options == this.Options.KEYSTROKE_BLOCKING_COLOR)
        {
            this.keystrokeBlockingColor = value;
        }
        else if (options == this.Options.KEYSTROKE_CPS_COLOR)
        {
            this.keystrokeCPSColor = value;
        }
        else if (options == this.Options.KEYSTROKE_RCPS_COLOR)
        {
            this.keystrokeRCPSColor = value;
        }
    }

    public double getOptionDoubleValue(this.Options settingOption)
    {
        if (settingOption == this.Options.ARMOR_HUD_Y)
        {
            return this.armorHUDYOffset;
        }
        else if (settingOption == this.Options.POTION_HUD_Y)
        {
            return this.potionHUDYOffset;
        }
        else if (settingOption == this.Options.KEYSTROKE_Y)
        {
            return this.keystrokeYOffset;
        }
        else if (settingOption == this.Options.MAXIMUM_POTION_DISPLAY)
        {
            return this.maximumPotionDisplay;
        }
        else if (settingOption == this.Options.POTION_LENGTH_Y_OFFSET)
        {
            return this.potionLengthYOffset;
        }
        else if (settingOption == this.Options.POTION_LENGTH_Y_OFFSET_OVERLAP)
        {
            return this.potionLengthYOffsetOverlap;
        }
        else if (settingOption == this.Options.CPS_OPACITY)
        {
            return this.cpsOpacity;
        }
        return 0.0F;
    }*/

    /*private boolean getOptionOrdinalValue(this.Options options)
    {
        switch (options)
        {
        case PREVIEW:
            return Guithis.preview;
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

    private String getOptionStringValue(this.Options options)
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
    }*/

    @Environment(EnvType.CLIENT)
    public enum Options
    {
        //        FPS_COLOR(false, ),
        //        XYZ_COLOR(false, ),
        //        BIOME_COLOR(false, ),
        //        DIRECTION_COLOR(false, ),
        //        PING_COLOR(false, ),
        //        PING_TO_SECOND_COLOR(false, ),
        //        SERVER_IP_COLOR(false, ),
        //        EQUIPMENT_STATUS_COLOR(false, ),
        //        ARROW_COUNT_COLOR(false, ),
        //        CPS_COLOR(false, ),
        //        RCPS_COLOR(false, ),
        //        SLIME_CHUNK_COLOR(false, ),
        //        TOP_DONATOR_NAME_COLOR(false, ),
        //        RECENT_DONATOR_NAME_COLOR(false, ),
        //        TPS_COLOR(false, ),
        //        REAL_TIME_COLOR(false, ),
        //        GAME_TIME_COLOR(false, ),
        //        GAME_WEATHER_COLOR(false, ),
        //        MOON_PHASE_COLOR(false, ),
        //        YTCHAT_VIEW_COUNT_COLOR(false, ),
        //
        //        FPS_VALUE_COLOR(false, ),
        //        FPS_26_AND_40_COLOR(false, ),
        //        FPS_LOW_25_COLOR(false, ),
        //        XYZ_VALUE_COLOR(false, ),
        //        DIRECTION_VALUE_COLOR(false, ),
        //        BIOME_VALUE_COLOR(false, ),
        //        PING_VALUE_COLOR(false, ),
        //        PING_200_AND_300_COLOR(false, ),
        //        PING_300_AND_500_COLOR(false, ),
        //        PING_MAX_500_COLOR(false, ),
        //        SERVER_IP_VALUE_COLOR(false, ),
        //        CPS_VALUE_COLOR(false, ),
        //        RCPS_VALUE_COLOR(false, ),
        //        SLIME_CHUNK_VALUE_COLOR(false, ),
        //        TOP_DONATOR_VALUE_COLOR(false, ),
        //        RECENT_DONATOR_VALUE_COLOR(false, ),
        //        TPS_VALUE_COLOR(false, ),
        //        REAL_TIME_HHMMSS_VALUE_COLOR(false, ),
        //        REAL_TIME_DDMMYY_VALUE_COLOR(false, ),
        //        GAME_TIME_VALUE_COLOR(false, ),
        //        GAME_WEATHER_VALUE_COLOR(false, ),
        //        MOON_PHASE_VALUE_COLOR(false, ),
        //        YTCHAT_VIEW_COUNT_VALUE_COLOR(false, ),
        //
        //        KEYSTROKE_WASD_COLOR(false, ),
        //        KEYSTROKE_MOUSE_BUTTON_COLOR(false, ),
        //        KEYSTROKE_SPRINT_COLOR(false, ),
        //        KEYSTROKE_SNEAK_COLOR(false, ),
        //        KEYSTROKE_BLOCKING_COLOR(false, ),
        //        KEYSTROKE_CPS_COLOR(false, ),
        //        KEYSTROKE_RCPS_COLOR(false, ),

        ;

        private final boolean isDouble;
        private final boolean isBoolean;
        private final double valueStep;
        private boolean isTextbox;
        private String comment;
        private double valueMin;
        private double valueMax;
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

        Options(boolean isDouble, boolean isBoolean)
        {
            this(isDouble, isBoolean, false, null, 0.0F, 1.0F, 0.0F);
        }

        Options(boolean isDouble, boolean isBoolean, double valMin, double valMax, double valStep)
        {
            this(isDouble, isBoolean, false, null, valMin, valMax, valStep);
        }

        Options(boolean isDouble, boolean isBoolean, boolean isTextbox)
        {
            this(isDouble, isBoolean, isTextbox, null, 0.0F, 1.0F, 0.0F);
        }

        Options(boolean isDouble, boolean isBoolean, boolean isTextbox, String comment)
        {
            this(isDouble, isBoolean, isTextbox, comment, 0.0F, 1.0F, 0.0F);
        }

        Options(boolean isDouble, boolean isBoolean, boolean isTextbox, String comment, double valMin, double valMax, double valStep)
        {
            this.isDouble = isDouble;
            this.isBoolean = isBoolean;
            this.isTextbox = isTextbox;
            this.comment = comment;
            this.valueMin = valMin;
            this.valueMax = valMax;
            this.valueStep = valStep;
        }

        public boolean isDouble()
        {
            return this.isDouble;
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
            return LangUtils.translate("extended_config." + this.name().toLowerCase());
        }

        public String getComment()
        {
            return Strings.isNullOrEmpty(this.comment) ? "" : LangUtils.translate(this.comment);
        }

        public double normalizeValue(double value)
        {
            return MathHelper.clamp((this.snapToStepClamp(value) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
        }

        public double denormalizeValue(double value)
        {
            return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp(value, 0.0F, 1.0F));
        }

        public double snapToStepClamp(double value)
        {
            value = this.snapToStep(value);
            return MathHelper.clamp(value, this.valueMin, this.valueMax);
        }

        private double snapToStep(double value)
        {
            if (this.valueStep > 0.0F)
            {
                value = this.valueStep * Math.round(value / this.valueStep);
            }
            return value;
        }
    }
}