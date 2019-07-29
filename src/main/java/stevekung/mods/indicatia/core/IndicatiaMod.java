package stevekung.mods.indicatia.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.options.GameOptions;
import net.minecraft.nbt.CompoundTag;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.event.IndicatiaEventHandler;
import stevekung.mods.indicatia.extra.ExtraEventHandler;
import stevekung.mods.indicatia.extra.ThreadDownloadWeatherData;
import stevekung.mods.indicatia.handler.KeyBindingHandler;
import stevekung.mods.indicatia.utils.LoggerIN;
import stevekung.mods.stevekungslib.utils.client.ClientRegistryUtils;

public class IndicatiaMod implements ClientModInitializer
{
    public static final String MOD_ID = "indicatia";
    private static final File profile = new File(ExtendedConfig.userDir, "profile.txt");
    private static final List<String> allowedUUID = new ArrayList<>();
    public static LoggerIN LOGGER = new LoggerIN();

    static
    {
        IndicatiaMod.initProfileFile();
        IndicatiaMod.allowedUUID.add("84b5eb0f-11d8-464b-881d-4bba203cc77b");
        IndicatiaMod.allowedUUID.add("f1dfdd47-6e03-4c2d-b766-e414c7b77f10");
        IndicatiaMod.allowedUUID.add("7d06c93d-736c-4d63-a683-c7583f6763e7");
        IndicatiaMod.allowedUUID.add("4675476a-46e5-45ee-89a5-010dc02996d9");
        IndicatiaMod.allowedUUID.add("e2d72023-34b9-45c2-825b-63ae2d1b2f36");

        IndicatiaMod.allowedUUID.add("b996cae9-43ad-48ad-ba89-4f1c50f14943");
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

        try
        {
            List<String> list = IOUtils.readLines(new FileInputStream(profile), StandardCharsets.UTF_8);

            list.forEach(option ->
            {
                Iterator<String> iterator = GameOptions.COLON_SPLITTER.omitEmptyStrings().limit(2).split(option).iterator();
                nbt.putString(iterator.next(), iterator.next());
            });
        }
        catch (Exception e) {}

        nbt.getKeys().forEach(property ->
        {
            String key = nbt.getString(property);

            if ("profile".equals(property))
            {
                IndicatiaMod.LOGGER.info("Loaded current profile by name '{}'", key);
                ExtendedConfig.setCurrentProfile(key);
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