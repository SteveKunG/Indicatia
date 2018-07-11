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
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.projectile.EntityFishHook;
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
import stevekung.mods.indicatia.event.*;
import stevekung.mods.indicatia.handler.KeyBindingHandler;
import stevekung.mods.indicatia.renderer.RenderFishNew;
import stevekung.mods.indicatia.utils.CapeUtils;
import stevekung.mods.indicatia.utils.LoggerIN;
import stevekung.mods.stevekunglib.utils.CommonUtils;
import stevekung.mods.stevekunglib.utils.GameProfileUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;
import stevekung.mods.stevekunglib.utils.VersionChecker;
import stevekung.mods.stevekunglib.utils.client.ClientUtils;

@Mod(modid = IndicatiaMod.MOD_ID, name = IndicatiaMod.NAME, version = IndicatiaMod.VERSION, dependencies = IndicatiaMod.DEPENDENCIES, updateJSON = IndicatiaMod.JSON_URL, clientSideOnly = true, certificateFingerprint = IndicatiaMod.CERTIFICATE)
public class IndicatiaMod
{
    protected static final String NAME = "Indicatia";
    public static final String MOD_ID = "indicatia";
    private static final int MAJOR_VERSION = 1;
    private static final int MINOR_VERSION = 2;
    private static final int BUILD_VERSION = 2;
    public static final String VERSION = IndicatiaMod.MAJOR_VERSION + "." + IndicatiaMod.MINOR_VERSION + "." + IndicatiaMod.BUILD_VERSION;
    private static final String FORGE_VERSION = "after:forge@[14.23.4.2705,);";
    protected static final String DEPENDENCIES = "required-after:stevekung's_lib@[1.0.2,); " + IndicatiaMod.FORGE_VERSION;
    private static final String URL = "https://minecraft.curseforge.com/projects/indicatia";
    protected static final String JSON_URL = "https://raw.githubusercontent.com/SteveKunG/VersionCheckLibrary/master/indicatia_version.json";
    protected static final String CERTIFICATE = "@FINGERPRINT@";

    @Instance(IndicatiaMod.MOD_ID)
    public static IndicatiaMod INSTANCE;

    public static boolean isDevelopment;
    public static final File profile = new File(ExtendedConfig.userDir, "profile.txt");
    public static final File resetFlag = new File(ExtendedConfig.userDir, "reset");
    public static VersionChecker CHECKER;
    public static final boolean isGalacticraftLoaded = Loader.isModLoaded("galacticraftcore");
    private static final List<String> allowedUUID = new ArrayList<>();

    static
    {
        try
        {
            IndicatiaMod.isDevelopment = Launch.classLoader.getClassBytes("net.minecraft.world.World") != null;
        }
        catch (Exception e) {}

        if (IndicatiaMod.resetFlag.exists())
        {
            ExtendedConfig.defaultConfig.delete();
            IndicatiaMod.resetFlag.delete();
            LoggerIN.info("Reset default config");
        }
        IndicatiaMod.initProfileFile();
        IndicatiaMod.allowedUUID.add("84b5eb0f-11d8-464b-881d-4bba203cc77b");
        IndicatiaMod.allowedUUID.add("f1dfdd47-6e03-4c2d-b766-e414c7b77f10");
        IndicatiaMod.allowedUUID.add("7d06c93d-736c-4d63-a683-c7583f6763e7");
        IndicatiaMod.allowedUUID.add("4675476a-46e5-45ee-89a5-010dc02996d9");
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
        CommonUtils.registerEventHandler(new BlockhitAnimationEventHandler());

        if (ConfigManagerIN.indicatia_general.enableFishingRodOldRender)
        {
            Minecraft.getMinecraft().getRenderManager().entityRenderMap.keySet().removeIf(key -> key.equals(EntityFishHook.class));
            Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(EntityFishHook.class, new RenderFishNew(Minecraft.getMinecraft().getRenderManager()));
            LoggerIN.info("Successfully replacing {}", EntityFishHook.class.getName());
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        CapeUtils.loadCapeTextureAtStartup();
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
            ExtendedConfig.setCurrentProfile("default");
            ExtendedConfig.save();
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
                ExtendedConfig.setCurrentProfile(key);
                ExtendedConfig.load();
            }
        });
    }

    private static void initProfileFile()
    {
        if (!ExtendedConfig.indicatiaDir.exists())
        {
            ExtendedConfig.indicatiaDir.mkdirs();
        }
        if (!ExtendedConfig.userDir.exists())
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

    public static void saveResetFlag()
    {
        ClientUtils.printClientMessage(LangUtils.translate("message.reset_config_flag"));

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(resetFlag), StandardCharsets.UTF_8)))
        {
            writer.println("reset");
        }
        catch (IOException e)
        {
            LoggerIN.error("Failed to save reset flag");
            e.printStackTrace();
        }
    }
}