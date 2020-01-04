package com.stevekung.indicatia.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.stevekung.indicatia.command.*;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.event.HUDRenderEventHandler;
import com.stevekung.indicatia.event.HypixelEventHandler;
import com.stevekung.indicatia.event.IndicatiaEventHandler;
import com.stevekung.indicatia.gui.screen.IndicatiaChatScreen;
import com.stevekung.indicatia.handler.KeyBindingHandler;
import com.stevekung.indicatia.utils.ThreadMinigameData;
import com.stevekung.stevekungslib.client.gui.ChatScreenRegistry;
import com.stevekung.stevekungslib.utils.CommonUtils;
import com.stevekung.stevekungslib.utils.LoggerBase;
import com.stevekung.stevekungslib.utils.VersionChecker;
import com.stevekung.stevekungslib.utils.client.command.ClientCommands;

import net.minecraft.client.GameSettings;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod(IndicatiaMod.MOD_ID)
public class IndicatiaMod
{
    private static final String NAME = "Indicatia";
    public static final String MOD_ID = "indicatia";
    private static final String URL = "https://minecraft.curseforge.com/projects/indicatia";
    private static final File PROFILE = new File(ExtendedConfig.USER_DIR, "profile.txt");
    public static VersionChecker CHECKER;
    public static boolean isGalacticraftLoaded;
    public static final LoggerBase LOGGER = new LoggerBase("Indicatia");

    static
    {
        IndicatiaMod.initProfileFile();
    }

    public IndicatiaMod()
    {
        CommonUtils.addModListener(this::phaseOne);
        CommonUtils.addModListener(this::loadComplete);

        CommonUtils.registerConfig(ModConfig.Type.CLIENT, IndicatiaConfig.GENERAL_BUILDER);
        CommonUtils.registerModEventBus(IndicatiaConfig.class);

        IndicatiaMod.isGalacticraftLoaded = ModList.get().isLoaded("galacticraftcore");
    }

    private void phaseOne(FMLCommonSetupEvent event)
    {
        this.registerClientCommands();
        KeyBindingHandler.init();
        CommonUtils.registerEventHandler(new HUDRenderEventHandler());
        CommonUtils.registerEventHandler(new IndicatiaEventHandler());
        CommonUtils.registerEventHandler(new HypixelEventHandler());
        CommonUtils.registerEventHandler(this);

        IndicatiaMod.CHECKER = new VersionChecker(this, IndicatiaMod.NAME, IndicatiaMod.URL);

        if (IndicatiaConfig.GENERAL.enableVersionChecker.get())
        {
            IndicatiaMod.CHECKER.startCheck();
        }
        IndicatiaMod.loadProfileOption();
    }

    private void loadComplete(FMLLoadCompleteEvent event)
    {
        ChatScreenRegistry.register(new IndicatiaChatScreen());
        CommonUtils.execute(new ThreadMinigameData());
    }

    private void registerClientCommands()
    {
        ClientCommands.register(new AFKCommand());
        ClientCommands.register(new AutoFishCommand());
        ClientCommands.register(new MojangStatusCheckCommand());
        ClientCommands.register(new PingAllCommand());
        ClientCommands.register(new ProfileCommand());
        ClientCommands.register(new SlimeSeedCommand());
        IndicatiaMod.LOGGER.info("Registering client side commands");
    }

    private static void loadProfileOption()
    {
        if (!PROFILE.exists())
        {
            return;
        }
        if (!ExtendedConfig.DEFAULT_CONFIG_FILE.exists())
        {
            IndicatiaMod.LOGGER.info("Initializing created default Indicatia profile...");
            ExtendedConfig.setCurrentProfile("default");
            ExtendedConfig.INSTANCE.save();
        }

        CompoundNBT nbt = new CompoundNBT();

        try (BufferedReader reader = Files.newReader(PROFILE, Charsets.UTF_8))
        {
            reader.lines().forEach(option ->
            {
                try
                {
                    Iterator<String> iterator = GameSettings.COLON_SPLITTER.omitEmptyStrings().limit(2).split(option).iterator();
                    nbt.putString(iterator.next(), iterator.next());
                }
                catch (Exception e) {}
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for (String property : nbt.keySet())
        {
            String key = nbt.getString(property);

            if ("profile".equals(property))
            {
                IndicatiaMod.LOGGER.info("Loaded current profile by name '{}'", key);
                ExtendedConfig.setCurrentProfile(key);
                ExtendedConfig.INSTANCE.load();
            }
        }
    }

    private static void initProfileFile()
    {
        if (!ExtendedConfig.INDICATIA_DIR.exists())
        {
            ExtendedConfig.INDICATIA_DIR.mkdirs();
        }
        if (!ExtendedConfig.USER_DIR.exists())
        {
            ExtendedConfig.USER_DIR.mkdirs();
        }

        if (!IndicatiaMod.PROFILE.exists())
        {
            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(IndicatiaMod.PROFILE), StandardCharsets.UTF_8)))
            {
                writer.println("profile:default");
                IndicatiaMod.LOGGER.info("Creating profile option at {}", IndicatiaMod.PROFILE.getPath());
            }
            catch (IOException e)
            {
                IndicatiaMod.LOGGER.error("Failed to save profile");
                e.printStackTrace();
            }
        }
    }
}