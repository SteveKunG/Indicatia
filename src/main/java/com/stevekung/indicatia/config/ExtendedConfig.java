package com.stevekung.indicatia.config;

import java.io.*;
import java.nio.charset.StandardCharsets;

import com.stevekung.indicatia.core.IndicatiaMod;
import com.stevekung.indicatia.gui.exconfig.BooleanConfigOption;
import com.stevekung.indicatia.gui.exconfig.DoubleConfigOption;
import com.stevekung.indicatia.gui.exconfig.StringConfigOption;
import com.stevekung.indicatia.gui.exconfig.TextFieldConfigOption;
import com.stevekung.indicatia.gui.exconfig.screen.ExtendedConfigScreen;
import com.stevekung.stevekungslib.utils.GameProfileUtils;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

public class ExtendedConfig
{
    public static ExtendedConfig instance = new ExtendedConfig();
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
    public boolean slimeChunkFinder = false;
    public boolean realTime = true;
    public boolean gameTime = true;
    public boolean gameWeather = true;
    public boolean moonPhase = true;
    public boolean potionHUDIcon = false;
    public boolean alternatePotionHUDTextColor = false;
    public boolean toggleSprint = false;
    public boolean toggleSneak = false;

    // Main
    public boolean swapRenderInfo = false;
    public Equipments.Ordering equipmentOrdering = Equipments.Ordering.DEFAULT;
    public Equipments.Direction equipmentDirection = Equipments.Direction.VERTICAL;
    public Equipments.Status equipmentStatus = Equipments.Status.DAMAGE_AND_MAX_DAMAGE;
    public Equipments.Position equipmentPosition = Equipments.Position.HOTBAR;
    public StatusEffects.Style potionHUDStyle = StatusEffects.Style.DEFAULT;
    public StatusEffects.Position potionHUDPosition = StatusEffects.Position.LEFT;

    // Offset
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
    public String slimeChunkColor = defaultWhite;
    public String realTimeColor = defaultWhite;
    public String gameTimeColor = defaultWhite;
    public String gameWeatherColor = defaultWhite;
    public String moonPhaseColor = defaultWhite;

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
    public String slimeChunkValueColor = defaultWhite;
    public String realTimeHHMMSSValueColor = defaultWhite;
    public String realTimeDDMMYYValueColor = defaultWhite;
    public String gameTimeValueColor = defaultWhite;
    public String gameWeatherValueColor = defaultWhite;
    public String moonPhaseValueColor = defaultWhite;

    // Misc
    public boolean showCustomCape = false;
    public String toggleSprintUseMode = "command";
    public String toggleSneakUseMode = "command";
    public long slimeChunkSeed = 0L;

    public static final DoubleConfigOption ARMOR_HUD_Y = new DoubleConfigOption("armor_hud_y", -512.0D, 512.0D, 1.0F, config -> (double)config.armorHUDYOffset, (config, value) -> config.armorHUDYOffset = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + (int)doubleOpt.get(config));
    public static final DoubleConfigOption POTION_HUD_Y = new DoubleConfigOption("potion_hud_y", -512.0D, 512.0D, 1.0F, config -> (double)config.potionHUDYOffset, (config, value) -> config.potionHUDYOffset = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + (int)doubleOpt.get(config));
    public static final DoubleConfigOption MAXIMUM_POTION_DISPLAY = new DoubleConfigOption("maximum_potion_display", 2.0D, 8.0D, 0.0F, config -> (double)config.maximumPotionDisplay, (config, value) -> config.maximumPotionDisplay = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + (int)doubleOpt.get(config));
    public static final DoubleConfigOption POTION_LENGTH_Y_OFFSET = new DoubleConfigOption("potion_length_y_offset", 1.0D, 256.0D, 1.0F, config -> (double)config.potionLengthYOffset, (config, value) -> config.potionLengthYOffset = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + (int)doubleOpt.get(config));
    public static final DoubleConfigOption POTION_LENGTH_Y_OFFSET_OVERLAP = new DoubleConfigOption("potion_length_y_offset_overlap", 1.0D, 256.0D, 1.0F, config -> (double)config.potionLengthYOffsetOverlap, (config, value) -> config.potionLengthYOffsetOverlap = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + (int)doubleOpt.get(config));


    public static final BooleanConfigOption PREVIEW = new BooleanConfigOption("preview", config -> ExtendedConfigScreen.preview, (config, value) -> ExtendedConfigScreen.preview = value);
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
    public static final BooleanConfigOption SLIME_CHUNK = new BooleanConfigOption("slime_chunk", config -> config.slimeChunkFinder, (config, value) -> config.slimeChunkFinder = value);
    public static final BooleanConfigOption REAL_TIME = new BooleanConfigOption("real_time", config -> config.realTime, (config, value) -> config.realTime = value);
    public static final BooleanConfigOption GAME_TIME = new BooleanConfigOption("game_time", config -> config.gameTime, (config, value) -> config.gameTime = value);
    public static final BooleanConfigOption GAME_WEATHER = new BooleanConfigOption("game_weather", config -> config.gameWeather, (config, value) -> config.gameWeather = value);
    public static final BooleanConfigOption MOON_PHASE = new BooleanConfigOption("moon_phase", config -> config.moonPhase, (config, value) -> config.moonPhase = value);
    public static final BooleanConfigOption POTION_ICON = new BooleanConfigOption("potion_icon", config -> config.potionHUDIcon, (config, value) -> config.potionHUDIcon = value);
    public static final BooleanConfigOption ALTERNATE_POTION_COLOR = new BooleanConfigOption("alternate_potion_color", config -> config.alternatePotionHUDTextColor, (config, value) -> config.alternatePotionHUDTextColor = value);


    public static final StringConfigOption EQUIPMENT_ORDERING = new StringConfigOption("equipment_ordering", (config, value) -> config.equipmentOrdering = Equipments.Ordering.byId(config.equipmentOrdering.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.equipmentOrdering.getTranslationKey()));
    public static final StringConfigOption EQUIPMENT_DIRECTION = new StringConfigOption("equipment_direction", (config, value) -> config.equipmentDirection = Equipments.Direction.byId(config.equipmentDirection.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.equipmentDirection.getTranslationKey()));
    public static final StringConfigOption EQUIPMENT_STATUS = new StringConfigOption("equipment_status", (config, value) -> config.equipmentStatus = Equipments.Status.byId(config.equipmentStatus.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.equipmentStatus.getTranslationKey()));
    public static final StringConfigOption EQUIPMENT_POSITION = new StringConfigOption("equipment_position", (config, value) -> config.equipmentPosition = Equipments.Position.byId(config.equipmentPosition.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.equipmentPosition.getTranslationKey()));
    public static final StringConfigOption POTION_HUD_STYLE = new StringConfigOption("potion_hud_style", (config, value) -> config.potionHUDStyle = StatusEffects.Style.byId(config.potionHUDStyle.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.potionHUDStyle.getTranslationKey()));
    public static final StringConfigOption POTION_HUD_POSITION = new StringConfigOption("potion_hud_position", (config, value) -> config.potionHUDPosition = StatusEffects.Position.byId(config.potionHUDPosition.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.potionHUDPosition.getTranslationKey()));


    public static final TextFieldConfigOption FPS_COLOR = new TextFieldConfigOption("fps_color", config -> config.fpsColor, (config, value) -> config.fpsColor = value);
    public static final TextFieldConfigOption XYZ_COLOR = new TextFieldConfigOption("xyz_color", config -> config.xyzColor, (config, value) -> config.xyzColor = value);
    public static final TextFieldConfigOption BIOME_COLOR = new TextFieldConfigOption("biome_color", config -> config.biomeColor, (config, value) -> config.biomeColor = value);
    public static final TextFieldConfigOption DIRECTION_COLOR = new TextFieldConfigOption("direction_color", config -> config.directionColor, (config, value) -> config.directionColor = value);
    public static final TextFieldConfigOption PING_COLOR = new TextFieldConfigOption("ping_color", config -> config.pingColor, (config, value) -> config.pingColor = value);
    public static final TextFieldConfigOption PING_TO_SECOND_COLOR = new TextFieldConfigOption("ping_to_second_color", config -> config.pingToSecondColor, (config, value) -> config.pingToSecondColor = value);
    public static final TextFieldConfigOption SERVER_IP_COLOR = new TextFieldConfigOption("server_ip_color", config -> config.serverIPColor, (config, value) -> config.serverIPColor = value);
    public static final TextFieldConfigOption EQUIPMENT_STATUS_COLOR = new TextFieldConfigOption("equipment_status_color", config -> config.equipmentStatusColor, (config, value) -> config.equipmentStatusColor = value);
    public static final TextFieldConfigOption ARROW_COUNT_COLOR = new TextFieldConfigOption("arrow_count_color", config -> config.arrowCountColor, (config, value) -> config.arrowCountColor = value);
    public static final TextFieldConfigOption SLIME_CHUNK_COLOR = new TextFieldConfigOption("slime_chunk_color", config -> config.slimeChunkColor, (config, value) -> config.slimeChunkColor = value);
    public static final TextFieldConfigOption REAL_TIME_COLOR = new TextFieldConfigOption("real_time_color", config -> config.realTimeColor, (config, value) -> config.realTimeColor = value);
    public static final TextFieldConfigOption GAME_TIME_COLOR = new TextFieldConfigOption("game_time_color", config -> config.gameTimeColor, (config, value) -> config.gameTimeColor = value);
    public static final TextFieldConfigOption GAME_WEATHER_COLOR = new TextFieldConfigOption("game_weather_color", config -> config.gameWeatherColor, (config, value) -> config.gameWeatherColor = value);
    public static final TextFieldConfigOption MOON_PHASE_COLOR = new TextFieldConfigOption("moon_phase_color", config -> config.moonPhaseColor, (config, value) -> config.moonPhaseColor = value);


    public static final TextFieldConfigOption FPS_VALUE_COLOR = new TextFieldConfigOption("fps_value_color", config -> config.fpsValueColor, (config, value) -> config.fpsValueColor = value);
    public static final TextFieldConfigOption FPS_26_AND_40_COLOR = new TextFieldConfigOption("fps_26_and_40_color", config -> config.fps26And49Color, (config, value) -> config.fps26And49Color = value);
    public static final TextFieldConfigOption FPS_LOW_25_COLOR = new TextFieldConfigOption("fps_low_25_color", config -> config.fpsLow25Color, (config, value) -> config.fpsLow25Color = value);
    public static final TextFieldConfigOption XYZ_VALUE_COLOR = new TextFieldConfigOption("xyz_value_color", config -> config.xyzValueColor, (config, value) -> config.xyzValueColor = value);
    public static final TextFieldConfigOption DIRECTION_VALUE_COLOR = new TextFieldConfigOption("direction_value_color", config -> config.directionValueColor, (config, value) -> config.directionValueColor = value);
    public static final TextFieldConfigOption BIOME_VALUE_COLOR = new TextFieldConfigOption("biome_value_color", config -> config.biomeValueColor, (config, value) -> config.biomeValueColor = value);
    public static final TextFieldConfigOption PING_VALUE_COLOR = new TextFieldConfigOption("ping_value_color", config -> config.pingValueColor, (config, value) -> config.pingValueColor = value);
    public static final TextFieldConfigOption PING_200_AND_300_COLOR = new TextFieldConfigOption("ping_200_and_300_color", config -> config.ping200And300Color, (config, value) -> config.ping200And300Color = value);
    public static final TextFieldConfigOption PING_300_AND_500_COLOR = new TextFieldConfigOption("ping_300_and_500_color", config -> config.ping300And500Color, (config, value) -> config.ping300And500Color = value);
    public static final TextFieldConfigOption PING_MAX_500_COLOR = new TextFieldConfigOption("ping_max_500_color", config -> config.pingMax500Color, (config, value) -> config.pingMax500Color = value);
    public static final TextFieldConfigOption SERVER_IP_VALUE_COLOR = new TextFieldConfigOption("server_ip_value_color", config -> config.serverIPValueColor, (config, value) -> config.serverIPValueColor = value);
    public static final TextFieldConfigOption SLIME_CHUNK_VALUE_COLOR = new TextFieldConfigOption("slime_chunk_value_color", config -> config.slimeChunkValueColor, (config, value) -> config.slimeChunkValueColor = value);
    public static final TextFieldConfigOption REAL_TIME_HHMMSS_VALUE_COLOR = new TextFieldConfigOption("real_time_hhmmss_value_color", config -> config.realTimeHHMMSSValueColor, (config, value) -> config.realTimeHHMMSSValueColor = value);
    public static final TextFieldConfigOption REAL_TIME_DDMMYY_VALUE_COLOR = new TextFieldConfigOption("real_time_ddmmyy_value_color", config -> config.realTimeDDMMYYValueColor, (config, value) -> config.realTimeDDMMYYValueColor = value);
    public static final TextFieldConfigOption GAME_TIME_VALUE_COLOR = new TextFieldConfigOption("game_time_value_color", config -> config.gameTimeValueColor, (config, value) -> config.gameTimeValueColor = value);
    public static final TextFieldConfigOption GAME_WEATHER_VALUE_COLOR = new TextFieldConfigOption("game_weather_value_color", config -> config.gameWeatherValueColor, (config, value) -> config.gameWeatherValueColor = value);
    public static final TextFieldConfigOption MOON_PHASE_VALUE_COLOR = new TextFieldConfigOption("moon_phase_value_color", config -> config.moonPhaseValueColor, (config, value) -> config.moonPhaseValueColor = value);


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
            this.slimeChunkFinder = this.getBoolean(nbt, "SlimeChunkFinder", this.slimeChunkFinder);
            this.realTime = this.getBoolean(nbt, "RealTime", this.realTime);
            this.gameTime = this.getBoolean(nbt, "GameTime", this.gameTime);
            this.gameWeather = this.getBoolean(nbt, "GameWeather", this.gameWeather);
            this.moonPhase = this.getBoolean(nbt, "MoonPhase", this.moonPhase);
            this.potionHUDIcon = this.getBoolean(nbt, "PotionHUDIcon", this.potionHUDIcon);
            this.alternatePotionHUDTextColor = this.getBoolean(nbt, "AlternatePotionHUDTextColor", this.alternatePotionHUDTextColor);

            // Main
            this.swapRenderInfo = this.getBoolean(nbt, "SwapRenderInfo", this.swapRenderInfo);
            this.showCustomCape = this.getBoolean(nbt, "ShowCustomCape", this.showCustomCape);
            this.equipmentOrdering = Equipments.Ordering.byId(this.getInteger(nbt, "EquipmentOrdering", this.equipmentOrdering.getId()));
            this.equipmentDirection = Equipments.Direction.byId(this.getInteger(nbt, "EquipmentDirection", this.equipmentDirection.getId()));
            this.equipmentStatus = Equipments.Status.byId(this.getInteger(nbt, "EquipmentStatus", this.equipmentStatus.getId()));
            this.equipmentPosition = Equipments.Position.byId(this.getInteger(nbt, "EquipmentPosition", this.equipmentPosition.getId()));
            this.potionHUDStyle = StatusEffects.Style.byId(this.getInteger(nbt, "PotionHUDStyle", this.potionHUDStyle.getId()));
            this.potionHUDPosition = StatusEffects.Position.byId(this.getInteger(nbt, "PotionHUDPosition", this.potionHUDPosition.getId()));

            // Movement
            this.toggleSprint = this.getBoolean(nbt, "ToggleSprint", this.toggleSprint);
            this.toggleSneak = this.getBoolean(nbt, "ToggleSneak", this.toggleSneak);

            // Offset
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
            this.slimeChunkColor = this.getString(nbt, "SlimeChunkColor", this.slimeChunkColor);
            this.realTimeColor = this.getString(nbt, "RealTimeColor", this.realTimeColor);
            this.gameTimeColor = this.getString(nbt, "GameTimeColor", this.gameTimeColor);
            this.gameWeatherColor = this.getString(nbt, "GameWeatherColor", this.gameWeatherColor);
            this.moonPhaseColor = this.getString(nbt, "MoonPhaseColor", this.moonPhaseColor);

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
            this.slimeChunkValueColor = this.getString(nbt, "SlimeChunkValueColor", this.slimeChunkValueColor);
            this.realTimeHHMMSSValueColor = this.getString(nbt, "RealTimeHHMMSSValueColor", this.realTimeHHMMSSValueColor);
            this.realTimeDDMMYYValueColor = this.getString(nbt, "RealTimeDDMMYYValueColor", this.realTimeDDMMYYValueColor);
            this.gameTimeValueColor = this.getString(nbt, "GameTimeValueColor", this.gameTimeValueColor);
            this.gameWeatherValueColor = this.getString(nbt, "GameWeatherValueColor", this.gameWeatherValueColor);
            this.moonPhaseValueColor = this.getString(nbt, "MoonPhaseValueColor", this.moonPhaseValueColor);

            // Misc
            this.toggleSprintUseMode = this.getString(nbt, "ToggleSprintUseMode", this.toggleSprintUseMode);
            this.toggleSneakUseMode = this.getString(nbt, "ToggleSneakUseMode", this.toggleSneakUseMode);
            this.slimeChunkSeed = this.getLong(nbt, "SlimeChunkSeed", this.slimeChunkSeed);

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
            nbt.putBoolean("SlimeChunkFinder", this.slimeChunkFinder);
            nbt.putBoolean("RealTime", this.realTime);
            nbt.putBoolean("GameTime", this.gameTime);
            nbt.putBoolean("GameWeather", this.gameWeather);
            nbt.putBoolean("MoonPhase", this.moonPhase);
            nbt.putBoolean("PotionHUDIcon", this.potionHUDIcon);
            nbt.putBoolean("AlternatePotionHUDTextColor", this.alternatePotionHUDTextColor);

            // Main
            nbt.putBoolean("ShowCustomCape", this.showCustomCape);
            nbt.putBoolean("SwapRenderInfo", this.swapRenderInfo);
            nbt.putInt("EquipmentOrdering", this.equipmentOrdering.getId());
            nbt.putInt("EquipmentDirection", this.equipmentDirection.getId());
            nbt.putInt("EquipmentStatus", this.equipmentStatus.getId());
            nbt.putInt("EquipmentPosition", this.equipmentPosition.getId());
            nbt.putInt("PotionHUDStyle", this.potionHUDStyle.getId());
            nbt.putInt("PotionHUDPosition", this.potionHUDPosition.getId());

            // Movement
            nbt.putBoolean("ToggleSprint", this.toggleSprint);
            nbt.putBoolean("ToggleSneak", this.toggleSneak);

            // Offset
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
            nbt.putString("SlimeChunkColor", this.slimeChunkColor);
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
            nbt.putString("SlimeChunkValueColor", this.slimeChunkValueColor);
            nbt.putString("RealTimeHHMMSSValueColor", this.realTimeHHMMSSValueColor);
            nbt.putString("RealTimeDDMMYYValueColor", this.realTimeDDMMYYValueColor);
            nbt.putString("GameTimeValueColor", this.gameTimeValueColor);
            nbt.putString("GameWeatherValueColor", this.gameWeatherValueColor);
            nbt.putString("MoonPhaseValueColor", this.moonPhaseValueColor);

            // Misc
            nbt.putString("ToggleSprintUseMode", this.toggleSprintUseMode);
            nbt.putString("ToggleSneakUseMode", this.toggleSneakUseMode);
            nbt.putLong("SlimeChunkSeed", this.slimeChunkSeed);

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

    public static void resetConfig()
    {
        ExtendedConfig.instance = new ExtendedConfig();
        ExtendedConfig.instance.save(ExtendedConfig.currentProfile);
        ClientUtils.printClientMessage(LangUtils.translate("misc.extended_config.reset_config", ExtendedConfig.currentProfile));
    }

    private boolean getBoolean(CompoundTag nbt, String key, boolean defaultValue)
    {
        if (nbt.contains(key, 99))
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
        if (nbt.contains(key, 99))
        {
            return nbt.getInt(key);
        }
        else
        {
            return defaultValue;
        }
    }

    private String getString(CompoundTag nbt, String key, String defaultValue)
    {
        if (nbt.contains(key, 8))
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
        if (nbt.contains(key, 99))
        {
            return nbt.getLong(key);
        }
        else
        {
            return defaultValue;
        }
    }
}