package com.stevekung.indicatia.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.CompareToBuilder;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.stevekung.indicatia.core.Indicatia;
import com.stevekung.stevekungslib.utils.RecordTypeAdapterFactory;

public class ThreadMinigameData implements Runnable
{
    public ThreadMinigameData()
    {
        this.run();
    }

    @Override
    public void run()
    {
        try
        {
            var gson = new GsonBuilder().registerTypeAdapterFactory(new RecordTypeAdapterFactory()).create();
            var url = new URL("https://raw.githubusercontent.com/SteveKunG/Indicatia/minigame_data/minigames.json");
            var in = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), StandardCharsets.UTF_8));
            MinigameData.DATA = Arrays.stream(gson.fromJson(in, MinigameData[].class)).collect(Collectors.toList());
            MinigameData.DATA.forEach(command -> command.commands().sort((minigame1, minigame2) -> !command.sort() ? 1 : new CompareToBuilder().append(minigame1.isMinigame(), minigame2.isMinigame()).append(minigame1.name(), minigame2.name()).build()));
            MinigameData.DATA.sort((minigame1, minigame2) -> minigame2.name().equals("Main") ? 1 : new CompareToBuilder().append(minigame1.name(), minigame2.name()).build());
            Indicatia.LOGGER.info("Successfully getting Minigames data from GitHub!");
        }
        catch (IOException | JsonIOException | JsonSyntaxException e)
        {
            e.printStackTrace();
            Indicatia.LOGGER.error("Couldn't get Minigames data from GitHub!");
            MinigameData.addMinigame(new MinigameData("Couldn't get Minigames data from GitHub!", false, Collections.emptyList()));
        }
    }
}