package com.stevekung.indicatia.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.event.HypixelEventHandler;
import com.stevekung.indicatia.gui.screens.IndicatiaChatScreen;
import com.stevekung.indicatia.handler.KeyBindingHandler;
import com.stevekung.indicatia.utils.ThreadMinigameData;
import com.stevekung.stevekungslib.utils.CommonUtils;
import com.stevekung.stevekungslib.utils.LoggerBase;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;
import me.shedaniel.architectury.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.nbt.CompoundTag;

public class Indicatia
{
    public static final String MOD_ID = "indicatia";
    private static final File PROFILE = new File(IndicatiaSettings.USER_DIR, "profile.txt");
    public static boolean isGalacticraftLoaded;
    public static int index;
    public static final LoggerBase LOGGER = new LoggerBase("Indicatia");
    private static final Splitter COLON_SPLITTER = Splitter.on(':').limit(2);
    public static KeyMapping keyBindAltChat;

    static
    {
        Indicatia.initProfileFile();
    }

    public static void init()
    {
        isGalacticraftLoaded = Platform.isModLoaded("galacticraft");
        Indicatia.loadProfileOption();
        new HypixelEventHandler();
        new IndicatiaChatScreen();
        KeyBindingHandler.init();
        CommonUtils.runAsync(ThreadMinigameData::new);
    }

    private static void loadProfileOption()
    {
        if (!PROFILE.exists())
        {
            return;
        }
        if (!IndicatiaSettings.DEFAULT_CONFIG_FILE.exists())
        {
            Indicatia.LOGGER.info("Creating default profile...");
            IndicatiaSettings.setCurrentProfile("default");
            IndicatiaSettings.INSTANCE.save();
        }

        CompoundTag nbt = new CompoundTag();

        try (BufferedReader reader = Files.newReader(PROFILE, Charsets.UTF_8))
        {
            reader.lines().forEach(option ->
            {
                try
                {
                    Iterator<String> iterator = Indicatia.COLON_SPLITTER.split(option).iterator();
                    nbt.putString(iterator.next(), iterator.next());
                }
                catch (Exception ignored) {}
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for (String property : nbt.getAllKeys())
        {
            String key = nbt.getString(property);

            if ("profile".equals(property))
            {
                Indicatia.LOGGER.info("Load current profile '{}'", key);
                IndicatiaSettings.setCurrentProfile(key);
                setIndexByName(key);
                IndicatiaSettings.INSTANCE.load();
            }
        }
    }

    public static List<File> getProfileList()
    {
        return Arrays.stream(IndicatiaSettings.USER_DIR.listFiles()).filter(file -> file.getName().endsWith(".dat")).collect(Collectors.toList());
    }

    public static void selectProfile(String fileName)
    {
        IndicatiaSettings.setCurrentProfile(fileName);
        IndicatiaSettings.saveProfileFile(fileName);
        IndicatiaSettings.INSTANCE.load();
        ClientUtils.setOverlayMessage(TextComponentUtils.component("Profile '").append(TextComponentUtils.formatted(fileName, ChatFormatting.YELLOW)).append("' selected"));
    }

    private static void setIndexByName(String name)
    {
        List<File> listFiles = getProfileList();

        for (int i = 0; i < listFiles.size(); i++)
        {
            File file = listFiles.get(i);

            if (file.getName().equals(name + ".dat"))
            {
                index = i;
                Indicatia.LOGGER.debug("Setting profile index {} from {}", i, name);
            }
        }
    }

    private static void initProfileFile()
    {
        if (!IndicatiaSettings.INDICATIA_DIR.exists())
        {
            IndicatiaSettings.INDICATIA_DIR.mkdirs();
        }
        if (!IndicatiaSettings.USER_DIR.exists())
        {
            IndicatiaSettings.USER_DIR.mkdirs();
        }

        if (!Indicatia.PROFILE.exists())
        {
            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(Indicatia.PROFILE), StandardCharsets.UTF_8)))
            {
                writer.println("profile:default");
                Indicatia.LOGGER.info("Creating profile option at {}", Indicatia.PROFILE.getPath());
            }
            catch (IOException e)
            {
                Indicatia.LOGGER.error("Failed to save profile");
                e.printStackTrace();
            }
        }
    }
}