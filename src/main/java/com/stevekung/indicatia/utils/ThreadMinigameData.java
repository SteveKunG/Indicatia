package com.stevekung.indicatia.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.google.gson.*;
import com.stevekung.indicatia.core.IndicatiaMod;

public class ThreadMinigameData implements Runnable
{
    @Override
    public void run()
    {
        try
        {
            URL url = new URL("https://raw.githubusercontent.com/SteveKunG/Indicatia/minigame_data/minigames.json");
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            JsonElement element = new JsonParser().parse(in);

            for (JsonElement minigameEle : element.getAsJsonArray())
            {
                JsonObject minigame = minigameEle.getAsJsonObject();
                String name = minigame.get("name").getAsString();
                boolean sort = !minigame.has("sort") ? true : minigame.get("sort").getAsBoolean();
                List<MinigameData.Command> minigameCmds = new ArrayList<>();

                for (JsonElement commandEle : minigame.getAsJsonArray("commands"))
                {
                    JsonObject command = commandEle.getAsJsonObject();
                    String uuid = "";
                    String texture = "";
                    String displayName = command.get("name").getAsString();
                    String minigameCommand = command.get("command").getAsString();
                    boolean isMinigame = command.get("minigame").getAsBoolean();

                    if (command.has("uuid"))
                    {
                        uuid = command.get("uuid").getAsString();
                        texture = command.get("texture").getAsString();
                    }

                    minigameCmds.add(new MinigameData.Command(displayName, minigameCommand, isMinigame, uuid, texture));
                    minigameCmds.sort((minigame1, minigame2) -> !sort ? 1 : new CompareToBuilder().append(minigame1.isMinigame(), minigame2.isMinigame()).append(minigame1.getName(), minigame2.getName()).build());
                }
                MinigameData.addMinigame(new MinigameData(name, minigameCmds));
                MinigameData.getMinigames().sort((minigame1, minigame2) -> minigame1.getName().equals("Main") ? -1 : new CompareToBuilder().append(minigame1.getName(), minigame2.getName()).build());
            }
            IndicatiaMod.LOGGER.info("Successfully getting Minigames data from GitHub!");
        }
        catch (IOException | JsonIOException | JsonSyntaxException e)
        {
            e.printStackTrace();
            IndicatiaMod.LOGGER.error("Couldn't get Minigames data from GitHub!");
            MinigameData.addMinigame(new MinigameData("Couldn't get Minigames data from GitHub!", new ArrayList<>()));
        }
    }
}