package stevekung.mods.indicatia.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stevekung.mods.indicatia.command.*;
import stevekung.mods.indicatia.config.ConfigManagerIN;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.event.BlockhitAnimationEventHandler;
import stevekung.mods.indicatia.event.ChatMessageEventHandler;
import stevekung.mods.indicatia.event.HUDRenderEventHandler;
import stevekung.mods.indicatia.event.IndicatiaEventHandler;
import stevekung.mods.indicatia.handler.KeyBindingHandler;
import stevekung.mods.indicatia.renderer.RenderFishNew;
import stevekung.mods.indicatia.utils.CapeUtils;
import stevekung.mods.indicatia.utils.ModLogger;
import stevekung.mods.stevekunglib.util.ClientUtils;
import stevekung.mods.stevekunglib.util.CommonUtils;
import stevekung.mods.stevekunglib.util.GameProfileUtils;
import stevekung.mods.stevekunglib.util.VersionChecker;
import stevekung.mods.stevekunglib.util.client.ColoredFontRenderer;

@Mod(modid = IndicatiaMod.MOD_ID, name = IndicatiaMod.NAME, version = IndicatiaMod.VERSION, dependencies = IndicatiaMod.DEPENDENCIES, clientSideOnly = true, certificateFingerprint = IndicatiaMod.CERTIFICATE)
public class IndicatiaMod
{
    public static final String NAME = "Indicatia";
    public static final String MOD_ID = "indicatia";
    public static final int MAJOR_VERSION = 2;
    public static final int MINOR_VERSION = 0;
    public static final int BUILD_VERSION = 0;
    public static final String VERSION = IndicatiaMod.MAJOR_VERSION + "." + IndicatiaMod.MINOR_VERSION + "." + IndicatiaMod.BUILD_VERSION;
    public static final String FORGE_VERSION = "after:forge@[14.23.3.2661,);";
    protected static final String DEPENDENCIES = "required-after:stevekung's_lib@[1.0.0,); " + IndicatiaMod.FORGE_VERSION;
    public static final String URL = "https://minecraft.curseforge.com/projects/indicatia";
    protected static final String CERTIFICATE = "@FINGERPRINT@";

    public static boolean isDevelopment;
    public static boolean noConnection;
    public static boolean foundLatest;
    public static boolean showAnnounceMessage;
    public static ColoredFontRenderer coloredFontRenderer;
    public static final File profile = new File(ExtendedConfig.indicatiaDir, "profile.txt");
    public static final VersionChecker checker = new VersionChecker(MOD_ID, VERSION, MAJOR_VERSION, MINOR_VERSION, BUILD_VERSION);
    public static final boolean isGalacticraftLoaded = Loader.isModLoaded("galacticraftcore");
    private static final List<String> allowedUUID = new ArrayList<>();

    static
    {
        try
        {
            IndicatiaMod.isDevelopment = Launch.classLoader.getClassBytes("net.minecraft.world.World") != null;
        }
        catch (Exception e) {}

        IndicatiaMod.initProfileFile();
        IndicatiaMod.allowedUUID.add("7d06c93d-736c-4d63-a683-c7583f6763e7");
        IndicatiaMod.allowedUUID.add("dbd9f8ed-0101-4cd3-8300-782a775c0225");
        IndicatiaMod.allowedUUID.add("2cd88ad0-89b1-4ca7-907e-78066fe36b08");
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        IndicatiaMod.init(event.getModMetadata());
        KeyBindingHandler.init();
        CommonUtils.registerEventHandler(this);
        CommonUtils.registerEventHandler(new HUDRenderEventHandler());
        CommonUtils.registerEventHandler(new IndicatiaEventHandler());
        CommonUtils.registerEventHandler(new BlockhitAnimationEventHandler());
        CommonUtils.registerEventHandler(new ChatMessageEventHandler());

        if (GameProfileUtils.isSteveKunG() || IndicatiaMod.allowedUUID.stream().anyMatch(uuid -> GameProfileUtils.getUUID().toString().trim().contains(uuid)))
        {
            try
            {
                Class<?> clazz = Class.forName("stevekung.mods.indicatia.extra.IndicatiaExtra");
                clazz.getMethod("init").invoke(null);
            }
            catch (Exception e) {}
        }
        if (ConfigManagerIN.indicatia_general.enableFishingRodOldRender)
        {
            ModelLoader.setCustomModelResourceLocation(Items.FISHING_ROD, 0, new ModelResourceLocation("indicatia:fishing_rod", "inventory"));
            ModLogger.info("Successfully replacing vanilla Fishing Rod item model");
        }

        ClientUtils.registerCommand(new CommandMojangStatusCheck());
        ClientUtils.registerCommand(new CommandChangeLog());
        ClientUtils.registerCommand(new CommandAutoLogin());
        ClientUtils.registerCommand(new CommandSlimeChunkSeed());
        ClientUtils.registerCommand(new CommandAFK());
        ClientUtils.registerCommand(new CommandIndicatia());
        ClientUtils.registerCommand(new CommandProfile());
        ClientUtils.registerCommand(new CommandRealmsMessage());
        ClientUtils.registerCommand(new CommandHideName());
        ClientUtils.registerCommand(new CommandPingAll());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        IndicatiaMod.loadProfileOption();

        if (ConfigManagerIN.indicatia_general.enableFishingRodOldRender)
        {
            Minecraft.getMinecraft().getRenderManager().entityRenderMap.entrySet().removeIf(entry -> entry.getKey().equals(EntityFishHook.class));
            Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(EntityFishHook.class, new RenderFishNew(Minecraft.getMinecraft().getRenderManager()));
            ModLogger.info("Successfully replacing {}", EntityFishHook.class.getName());
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        if (ConfigManagerIN.indicatia_general.enableVersionChecker)
        {
            IndicatiaMod.checker.startCheck();
        }
        CapeUtils.loadCapeTextureAtStartup();
        IndicatiaMod.coloredFontRenderer = new ColoredFontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, false);
        ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(IndicatiaMod.coloredFontRenderer);
    }

    @EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event)
    {
        if (IndicatiaMod.isDevelopment)
        {
            ModLogger.info("Development environment detected! Ignore certificate check.");
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
            ModLogger.info("Initializing created default Indicatia profile...");
            ExtendedConfig.setCurrentProfile("default");
            ExtendedConfig.save();
        }

        NBTTagCompound nbt = new NBTTagCompound();

        try
        {
            List<String> list = IOUtils.readLines(new FileInputStream(profile), StandardCharsets.UTF_8);

            for (String option : list)
            {
                Iterator<String> iterator = GameSettings.COLON_SPLITTER.omitEmptyStrings().limit(2).split(option).iterator();
                nbt.setString(iterator.next(), iterator.next());
            }
        }
        catch (Exception e) {}

        for (String property : nbt.getKeySet())
        {
            String key = nbt.getString(property);

            if ("profile".equals(property))
            {
                ModLogger.info("Loaded current profile by name '" + key + "'");
                ExtendedConfig.setCurrentProfile(key);
                ExtendedConfig.load();
            }
        }
    }

    private static void initProfileFile()
    {
        if (!ExtendedConfig.indicatiaDir.exists())
        {
            ExtendedConfig.indicatiaDir.mkdirs();
        }

        File profile = new File(ExtendedConfig.indicatiaDir, "profile.txt");

        if (!profile.exists())
        {
            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(profile), StandardCharsets.UTF_8)))
            {
                writer.println("profile:default");
                ModLogger.info("Creating profile option at {}", profile.getPath());
            }
            catch (IOException e)
            {
                ModLogger.error("Failed to save profile");
                e.printStackTrace();
            }
        }
    }
}