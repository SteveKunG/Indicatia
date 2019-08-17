package stevekung.mods.indicatia.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.Items;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stevekung.mods.indicatia.command.*;
import stevekung.mods.indicatia.config.ConfigManagerIN;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.event.ChatMessageEventHandler;
import stevekung.mods.indicatia.event.HUDRenderEventHandler;
import stevekung.mods.indicatia.event.HypixelEventHandler;
import stevekung.mods.indicatia.event.IndicatiaEventHandler;
import stevekung.mods.indicatia.gui.GuiIndicatiaChat;
import stevekung.mods.indicatia.handler.KeyBindingHandler;
import stevekung.mods.indicatia.utils.CapeUtils;
import stevekung.mods.indicatia.utils.LoggerIN;
import stevekung.mods.indicatia.utils.ThreadMinigameData;
import stevekung.mods.stevekunglib.client.gui.GuiChatRegistry;
import stevekung.mods.stevekunglib.utils.CommonUtils;
import stevekung.mods.stevekunglib.utils.VersionChecker;
import stevekung.mods.stevekunglib.utils.client.ClientUtils;

@Mod(modid = IndicatiaMod.MOD_ID, name = IndicatiaMod.NAME, version = IndicatiaMod.VERSION, dependencies = IndicatiaMod.DEPENDENCIES, updateJSON = IndicatiaMod.JSON_URL, clientSideOnly = true, certificateFingerprint = IndicatiaMod.CERTIFICATE)
public class IndicatiaMod
{
    protected static final String NAME = "Indicatia";
    public static final String MOD_ID = "indicatia";
    private static final int MAJOR_VERSION = 1;
    private static final int MINOR_VERSION = 2;
    private static final int BUILD_VERSION = 6;
    public static final String VERSION = IndicatiaMod.MAJOR_VERSION + "." + IndicatiaMod.MINOR_VERSION + "." + IndicatiaMod.BUILD_VERSION;
    private static final String FORGE_VERSION = "after:forge@[14.23.5.2838,);";
    protected static final String DEPENDENCIES = "required-after:stevekung's_lib@[1.1.0,); " + IndicatiaMod.FORGE_VERSION;
    private static final String URL = "https://minecraft.curseforge.com/projects/indicatia";
    protected static final String JSON_URL = "https://raw.githubusercontent.com/SteveKunG/VersionCheckLibrary/master/indicatia_version.json";
    protected static final String CERTIFICATE = "@FINGERPRINT@";

    @Instance(IndicatiaMod.MOD_ID)
    public static IndicatiaMod INSTANCE;

    public static boolean isDevelopment;
    public static final File profile = new File(ExtendedConfig.userDir, "profile.txt");
    public static VersionChecker CHECKER;
    public static final boolean isGalacticraftLoaded = Loader.isModLoaded("galacticraftcore");
    public static final boolean isYoutubeChatLoaded = Loader.isModLoaded("youtube_chat");

    static
    {
        try
        {
            IndicatiaMod.isDevelopment = Launch.classLoader.getClassBytes("net.minecraft.world.World") != null;
        }
        catch (Exception e) {}

        IndicatiaMod.initProfileFile();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        IndicatiaMod.init(event.getModMetadata());
        KeyBindingHandler.init();
        CommonUtils.registerEventHandler(this);
        CommonUtils.registerEventHandler(new HUDRenderEventHandler());
        CommonUtils.registerEventHandler(new IndicatiaEventHandler());
        CommonUtils.registerEventHandler(new ChatMessageEventHandler());
        CommonUtils.registerEventHandler(new HypixelEventHandler());

        if (ConfigManagerIN.indicatia_general.enableFishingRodOldRender)
        {
            ModelLoader.setCustomModelResourceLocation(Items.FISHING_ROD, 0, new ModelResourceLocation("indicatia:fishing_rod", "inventory"));
            LoggerIN.info("Successfully replacing vanilla Fishing Rod item model");
        }

        ClientUtils.registerCommand(new CommandMojangStatusCheck());
        ClientUtils.registerCommand(new CommandAutoLogin());
        ClientUtils.registerCommand(new CommandSlimeChunkSeed());
        ClientUtils.registerCommand(new CommandAFK());
        ClientUtils.registerCommand(new CommandIndicatia());
        ClientUtils.registerCommand(new CommandProfile());
        ClientUtils.registerCommand(new CommandRealmsMessage());
        ClientUtils.registerCommand(new CommandHideName());
        ClientUtils.registerCommand(new CommandPingAll());
        ClientUtils.registerCommand(new CommandAutoFish());
        ClientUtils.registerCommand(new CommandSwedenTime());

        IndicatiaMod.CHECKER = new VersionChecker(IndicatiaMod.INSTANCE, IndicatiaMod.NAME, IndicatiaMod.URL);

        if (ConfigManagerIN.indicatia_general.enableVersionChecker)
        {
            IndicatiaMod.CHECKER.startCheck();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        IndicatiaMod.loadProfileOption();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        CapeUtils.loadCapeTextureAtStartup();
        GuiChatRegistry.register(new GuiIndicatiaChat());
        new ThreadMinigameData().run();
    }

    @EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event)
    {
        if (IndicatiaMod.isDevelopment)
        {
            LoggerIN.info("Development environment detected! Ignore certificate check.");
        }
        else
        {
            throw new RuntimeException("Invalid fingerprint detected! This version will NOT be supported by the author!");
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(IndicatiaMod.MOD_ID))
        {
            ConfigManager.sync(IndicatiaMod.MOD_ID, Config.Type.INSTANCE);
        }
    }

    private static void init(ModMetadata info)
    {
        info.autogenerated = false;
        info.modId = IndicatiaMod.MOD_ID;
        info.name = IndicatiaMod.NAME;
        info.version = IndicatiaMod.VERSION;
        info.description = "Simple in-game info and utilities!";
        info.url = IndicatiaMod.URL;
        info.authorList = Arrays.asList("SteveKunG");
    }

    private static void loadProfileOption()
    {
        if (!profile.exists())
        {
            return;
        }
        if (!ExtendedConfig.defaultConfig.exists())
        {
            LoggerIN.info("Initializing created default Indicatia profile...");
            ExtendedConfig.instance.setCurrentProfile("default");
            ExtendedConfig.instance.save();
        }

        NBTTagCompound nbt = new NBTTagCompound();

        try
        {
            List<String> list = IOUtils.readLines(new FileInputStream(profile), StandardCharsets.UTF_8);

            list.forEach(option ->
            {
                Iterator<String> iterator = GameSettings.COLON_SPLITTER.omitEmptyStrings().limit(2).split(option).iterator();
                nbt.setString(iterator.next(), iterator.next());
            });
        }
        catch (Exception e) {}

        nbt.getKeySet().forEach(property ->
        {
            String key = nbt.getString(property);

            if ("profile".equals(property))
            {
                LoggerIN.info("Loaded current profile by name '{}'", key);
                ExtendedConfig.instance.setCurrentProfile(key);
                ExtendedConfig.instance.load();
            }
        });
    }

    private static void initProfileFile()
    {
        if (!ExtendedConfig.indicatiaDir.exists())
        {
            ExtendedConfig.indicatiaDir.mkdirs();
        }
        else if (!ExtendedConfig.userDir.exists())
        {
            ExtendedConfig.userDir.mkdirs();
        }

        File profile = new File(ExtendedConfig.userDir, "profile.txt");

        if (!profile.exists())
        {
            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(profile), StandardCharsets.UTF_8)))
            {
                writer.println("profile:default");
                LoggerIN.info("Creating profile option at {}", profile.getPath());
            }
            catch (IOException e)
            {
                LoggerIN.error("Failed to save profile");
                e.printStackTrace();
            }
        }
    }
}