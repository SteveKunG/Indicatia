package com.stevekung.indicatia.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.event.IndicatiaEventHandler;
import com.stevekung.indicatia.extra.ExtraEventHandler;
import com.stevekung.indicatia.extra.ThreadDownloadWeatherData;
import com.stevekung.indicatia.handler.KeyBindingHandler;
import com.stevekung.indicatia.utils.LoggerIN;
import com.stevekung.stevekungslib.utils.client.ClientRegistryUtils;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.nbt.CompoundTag;

public class IndicatiaMod implements ClientModInitializer
{
    public static final String MOD_ID = "indicatia";
    private static final File profile = new File(ExtendedConfig.userDir, "profile.txt");
    public static final LoggerIN LOGGER = new LoggerIN();
    private static final Splitter COLON_SPLITTER = Splitter.on(':').limit(2);

    static
    {
        IndicatiaMod.initProfileFile();
    }

    @Override
    public void onInitializeClient()
    {
        IndicatiaMod.loadProfileOption();
        KeyBindingHandler.init();

        ClientRegistryUtils.registerClientTick(mc -> new IndicatiaEventHandler().onClientTick());
        ClientRegistryUtils.registerClientTick(mc -> new ExtraEventHandler().onClientTick());

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new ThreadDownloadWeatherData(), 0, 5, TimeUnit.MINUTES);
    }

    private static void loadProfileOption()
    {
        if (!profile.exists())
        {
            return;
        }
        if (!ExtendedConfig.defaultConfig.exists())
        {
            IndicatiaMod.LOGGER.info("Initializing created default Indicatia profile...");
            ExtendedConfig.setCurrentProfile("default");
            ExtendedConfig.instance.save();
        }

        CompoundTag nbt = new CompoundTag();

        try (BufferedReader bufferedReader = Files.newReader(profile, Charsets.UTF_8);)
        {
            bufferedReader.lines().forEach(option ->
            {
                try
                {
                    Iterator<String> iterator = COLON_SPLITTER.split(option).iterator();
                    nbt.putString(iterator.next(), iterator.next());
                }
                catch (Exception e) {}
            });
        }
        catch (Exception e) {}

        for (String property : nbt.getKeys())
        {
            String key = nbt.getString(property);

            if ("profile".equals(property))
            {
                IndicatiaMod.LOGGER.info("Loaded current profile by name '{}'", key);
                ExtendedConfig.setCurrentProfile(key);
                ExtendedConfig.instance.load();
            }
        }
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
                IndicatiaMod.LOGGER.info("Creating profile option at {}", profile.getPath());
            }
            catch (IOException e)
            {
                IndicatiaMod.LOGGER.error("Failed to save profile");
                e.printStackTrace();
            }
        }
    }
}