package stevekung.mods.indicatia.core;

import com.mojang.brigadier.CommandDispatcher;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
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
import stevekung.mods.stevekungslib.client.gui.GuiChatRegistry;
import stevekung.mods.stevekungslib.utils.CommonUtils;
import stevekung.mods.stevekungslib.utils.GameProfileUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;
import stevekung.mods.stevekungslib.utils.VersionChecker;
import stevekung.mods.stevekungslib.utils.client.ClientUtils;

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
        CommonUtils.addModListener(this::setup);
        CommonUtils.addModListener(this::loadComplete);
        CommonUtils.addListener(this::serverStarting);

        CommonUtils.registerConfig(ModConfig.Type.CLIENT, IndicatiaConfig.GENERAL_BUILDER);
        CommonUtils.registerModEventBus(IndicatiaConfig.class);

        IndicatiaMod.isGalacticraftLoaded = ModList.get().isLoaded("galacticraftcore");
        IndicatiaMod.isYoutubeChatLoaded = ModList.get().isLoaded("youtube_chat");
        IndicatiaMod.isOptiFineLoaded = ModList.get().isLoaded("optifine");
    }

    private void setup(FMLClientSetupEvent event)
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
        if (IndicatiaConfig.GENERAL.enableOldFishingRodRender.get())
        {
            //ModelLoader.setCustomModelResourceLocation(Items.FISHING_ROD, 0, new ModelResourceLocation("indicatia:fishing_rod", "inventory"));TODO
            LoggerIN.info("Successfully replacing vanilla Fishing Rod item model");
        }

        IndicatiaMod.CHECKER = new VersionChecker(this, IndicatiaMod.NAME, IndicatiaMod.URL);

        if (IndicatiaConfig.GENERAL.enableVersionChecker.get())
        {
            IndicatiaMod.CHECKER.startCheck();
        }

        IndicatiaMod.loadProfileOption();
        CommonUtils.registerEventHandler(new BlockhitAnimationEventHandler());

        if (IndicatiaConfig.GENERAL.enableOldFishingRodRender.get())
        {
            Minecraft.getInstance().getRenderManager().entityRenderMap.keySet().removeIf(key -> key.equals(EntityFishHook.class));
            Minecraft.getInstance().getRenderManager().entityRenderMap.put(EntityFishHook.class, new RenderFishNew(Minecraft.getInstance().getRenderManager()));
            LoggerIN.info("Successfully replacing {}", EntityFishHook.class.getName());
        }
    }

    private void loadComplete(FMLLoadCompleteEvent event)
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

        if (GameProfileUtils.isSteveKunG() || IndicatiaMod.allowedUUID.stream().anyMatch(uuid -> GameProfileUtils.getUUID().toString().trim().contains(uuid)))
        {
            try
            {
                Class<?> clazz = Class.forName("stevekung.mods.indicatia.extra.IndicatiaExtra");
                clazz.getMethod("registerCommand", FMLServerStartingEvent.class).invoke(null, event);
            }
            catch (Exception e) {}
        }

        LoggerIN.info("Registering client side commands");
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
        ClientUtils.printClientMessage(LangUtils.translate("misc.extended_config.set_reset_flag"));

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