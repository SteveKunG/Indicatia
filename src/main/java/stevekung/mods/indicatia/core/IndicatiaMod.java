package stevekung.mods.indicatia.core;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.commons.io.IOUtils;
import stevekung.mods.indicatia.command.*;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.config.IndicatiaConfig;
import stevekung.mods.indicatia.event.*;
import stevekung.mods.indicatia.gui.hack.GuiIndicatiaChat;
import stevekung.mods.indicatia.handler.KeyBindingHandler;
import stevekung.mods.indicatia.renderer.RenderFishNew;
import stevekung.mods.indicatia.utils.CapeUtils;
import stevekung.mods.indicatia.utils.LoggerIN;
import stevekung.mods.indicatia.utils.ThreadMinigameData;
import stevekung.mods.stevekunglib.client.gui.GuiChatRegistry;
import stevekung.mods.stevekunglib.config.ConfigManagerBase;
import stevekung.mods.stevekunglib.utils.CommonUtils;
import stevekung.mods.stevekunglib.utils.GameProfileUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;
import stevekung.mods.stevekunglib.utils.VersionChecker;
import stevekung.mods.stevekunglib.utils.client.ClientUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod(IndicatiaMod.MOD_ID)
public class IndicatiaMod
{
    private static final String NAME = "Indicatia";
    static final String MOD_ID = "indicatia";
    private static final String URL = "https://minecraft.curseforge.com/projects/indicatia";
    private static final File profile = new File(ExtendedConfig.userDir, "profile.txt");
    private static final File resetFlag = new File(ExtendedConfig.userDir, "reset");
    public static VersionChecker CHECKER;
    public static boolean isGalacticraftLoaded;
    public static boolean isYoutubeChatLoaded;
    public static boolean isOptiFineLoaded;
    private static final List<String> allowedUUID = new ArrayList<>();
    public static ConfigManagerBase INSTANCE;

    static
    {
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

    public IndicatiaMod()
    {
        CommonUtils.addModListener(this::preInit);
        CommonUtils.addModListener(this::init);
        CommonUtils.addModListener(this::postInit);
        CommonUtils.addListener(this::serverStarting);

        IndicatiaMod.isGalacticraftLoaded = ModList.get().isLoaded("galacticraftcore");
        IndicatiaMod.isYoutubeChatLoaded = ModList.get().isLoaded("youtube_chat");
        IndicatiaMod.isOptiFineLoaded = ModList.get().isLoaded("optifine");
        IndicatiaMod.INSTANCE =  new ConfigManagerBase(IndicatiaMod.MOD_ID, IndicatiaConfig.BUILDER);
        IndicatiaMod.INSTANCE.load();
    }

    private void preInit(FMLPreInitializationEvent event)
    {
        KeyBindingHandler.init();
        CommonUtils.registerEventHandler(new HUDRenderEventHandler());
        CommonUtils.registerEventHandler(new IndicatiaEventHandler());
        CommonUtils.registerEventHandler(new ChatMessageEventHandler());
        CommonUtils.registerEventHandler(new HypixelEventHandler());
        CommonUtils.registerEventHandler(this);

        if (GameProfileUtils.isSteveKunG() || IndicatiaMod.allowedUUID.stream().anyMatch(uuid -> GameProfileUtils.getUUID().toString().trim().contains(uuid)))
        {
            try
            {
                Class<?> clazz = Class.forName("stevekung.mods.indicatia.extra.IndicatiaExtra");
                clazz.getMethod("init").invoke(null);
            }
            catch (Exception e) {}
        }
        if (IndicatiaMod.INSTANCE.getConfig().getOrElse("enableOldFishingRodRender", false))
        {
            //ModelLoader.setCustomModelResourceLocation(Items.FISHING_ROD, 0, new ModelResourceLocation("indicatia:fishing_rod", "inventory"));TODO
            LoggerIN.info("Successfully replacing vanilla Fishing Rod item model");
        }

        IndicatiaMod.CHECKER = new VersionChecker(this, IndicatiaMod.NAME, IndicatiaMod.URL);

        if (IndicatiaMod.INSTANCE.getConfig().getOrElse("enableVersionChecker", true))
        {
            IndicatiaMod.CHECKER.startCheck();
        }
    }

    private void init(FMLInitializationEvent event)
    {
        IndicatiaMod.loadProfileOption();
        CommonUtils.registerEventHandler(new BlockhitAnimationEventHandler());

        if (IndicatiaMod.INSTANCE.getConfig().getOrElse("enableOldFishingRodRender", false))
        {
            Minecraft.getInstance().getRenderManager().entityRenderMap.keySet().removeIf(key -> key.equals(EntityFishHook.class));
            Minecraft.getInstance().getRenderManager().entityRenderMap.put(EntityFishHook.class, new RenderFishNew(Minecraft.getInstance().getRenderManager()));
            LoggerIN.info("Successfully replacing {}", EntityFishHook.class.getName());
        }
    }

    private void postInit(FMLPostInitializationEvent event)
    {
        CapeUtils.loadCapeTextureAtStartup();
        GuiChatRegistry.register(new GuiIndicatiaChat());
        new ThreadMinigameData().run();
    }

    private void serverStarting(FMLServerStartingEvent event)
    {
        CommandDispatcher<CommandSource> dispatcher = event.getCommandDispatcher();
        MojangStatusCheckCommand.register(dispatcher);
        SetSlimeChunkSeedCommand.register(dispatcher);
        AFKCommand.register(dispatcher);
        IndicatiaCommand.register(dispatcher);
        ProfileCommand.register(dispatcher);
        PingAllCommand.register(dispatcher);
        AutoFishCommand.register(dispatcher);
        SwedenTimeCommand.register(dispatcher);
        HideNameCommand.register(dispatcher);
        AutoLoginCommand.register(dispatcher);
        LoggerIN.info("Registering client side commands");
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(IndicatiaMod.MOD_ID))
        {
            //ConfigManager.sync(IndicatiaMod.MOD_ID, Config.Type.INSTANCE);TODO
        }
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

        nbt.keySet().forEach(property ->
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