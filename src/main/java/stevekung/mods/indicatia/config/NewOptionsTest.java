package stevekung.mods.indicatia.config;

import stevekung.mods.indicatia.gui.config.GuiExtendedConfig;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class NewOptionsTest
{
    public static final DoubleConfigOption CPS_OPACITY = new DoubleConfigOption("cps_opacity", 0.0D, 100.0D, 1.0F, config -> config.cpsOpacity, (config, value) -> config.cpsOpacity = value, (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + doubleOpt.normalizeValue(doubleOpt.get(config)));

    public static final DoubleConfigOption ARMOR_HUD_Y = new DoubleConfigOption("armor_hud_y", -512.0D, 512.0D, 1.0F, config -> (double)config.armorHUDYOffset, (config, value) -> config.armorHUDYOffset = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + doubleOpt.normalizeValue(doubleOpt.get(config)));
    public static final DoubleConfigOption POTION_HUD_Y = new DoubleConfigOption("potion_hud_y", -512.0D, 512.0D, 1.0F, config -> (double)config.potionHUDYOffset, (config, value) -> config.potionHUDYOffset = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + doubleOpt.normalizeValue(doubleOpt.get(config)));
    public static final DoubleConfigOption KEYSTROKE_Y = new DoubleConfigOption("keystroke_y", -512.0D, 512.0D, 1.0F, config -> (double)config.keystrokeYOffset, (config, value) -> config.keystrokeYOffset = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + doubleOpt.normalizeValue(doubleOpt.get(config)));
    public static final DoubleConfigOption MAXIMUM_POTION_DISPLAY = new DoubleConfigOption("maximum_potion_display", 2.0D, 8.0D, 1.0F, config -> (double)config.maximumPotionDisplay, (config, value) -> config.maximumPotionDisplay = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + doubleOpt.normalizeValue(doubleOpt.get(config)));
    public static final DoubleConfigOption POTION_LENGTH_Y_OFFSET = new DoubleConfigOption("potion_length_y_offset", 1.0D, 256.0D, 1.0F, config -> (double)config.potionLengthYOffset, (config, value) -> config.potionLengthYOffset = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + doubleOpt.normalizeValue(doubleOpt.get(config)));
    public static final DoubleConfigOption POTION_LENGTH_Y_OFFSET_OVERLAP = new DoubleConfigOption("potion_length_y_offset_overlap", 1.0D, 256.0D, 1.0F, config -> (double)config.potionLengthYOffsetOverlap, (config, value) -> config.potionLengthYOffsetOverlap = value.intValue(), (config, doubleOpt) -> doubleOpt.getDisplayPrefix() + doubleOpt.normalizeValue(doubleOpt.get(config)));


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
    public static final BooleanConfigOption TPS = new BooleanConfigOption("extended_config.render_info.tps.info", true, config -> config.tps, (config, value) -> config.tps = value);
    public static final BooleanConfigOption TPS_ALL_DIMS = new BooleanConfigOption("extended_config.render_info.tps_all_dims.info", true, config -> config.tpsAllDims, (config, value) -> config.tpsAllDims = value);
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
    public static final StringConfigOption EQUIPMENT_DIRECTION = new StringConfigOption("equipment_direction", (config, value) -> config.equipmentDirection = Equipments.Direction.byId(config.equipmentDirection.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.equipmentOrdering.getTranslationKey()));
    public static final StringConfigOption EQUIPMENT_STATUS = new StringConfigOption("equipment_status", (config, value) -> config.equipmentStatus = Equipments.Status.byId(config.equipmentStatus.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.equipmentOrdering.getTranslationKey()));
    public static final StringConfigOption EQUIPMENT_POSITION = new StringConfigOption("equipment_position", (config, value) -> config.equipmentPosition = Equipments.Position.byId(config.equipmentPosition.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.equipmentOrdering.getTranslationKey()));
    public static final StringConfigOption POTION_HUD_STYLE = new StringConfigOption("potion_hud_style", (config, value) -> config.potionHUDStyle = StatusEffects.Style.byId(config.potionHUDStyle.getId() + value), (config, stringOpt) -> stringOpt.getDisplayPrefix() + LangUtils.translate(config.equipmentOrdering.getTranslationKey()));



















}