package stevekung.mods.indicatia.utils;

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

import stevekung.mods.indicatia.minigames.MinigameCommand;
import stevekung.mods.indicatia.minigames.MinigameData;

public class ThreadMinigameData extends Thread
{
    public ThreadMinigameData()
    {
        super("Minigame Data Thread");
    }

    @Override
    public void run()
    {
        try
        {
            URL url = new URL("https://raw.githubusercontent.com/SteveKunG/Indicatia/1.13-pre/minigames.json");
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(in);
            JsonArray minigames = element.getAsJsonArray();

            for (Object obj : minigames)
            {
                JsonObject minigame = (JsonObject)obj;
                JsonArray commands = minigame.getAsJsonArray("commands");
                String name = minigame.get("name").getAsString();
                List<MinigameCommand> minigameCommandList = new ArrayList<>();

                for (Object obj2 : commands)
                {
                    JsonObject command = (JsonObject)obj2;
                    String displayName = command.get("name").getAsString();
                    String minigameCommand = command.get("command").getAsString();
                    boolean isMinigame = command.get("minigame").getAsBoolean();
                    minigameCommandList.add(new MinigameCommand(displayName, minigameCommand, isMinigame));

                    minigameCommandList.sort((minigame1, minigame2) ->
                    {
                        return new CompareToBuilder().append(minigame1.isMinigame(), minigame2.isMinigame()).append(minigame1.getName(), minigame2.getName()).build();
                    });
                }

                MinigameData.addMinigameData(new MinigameData(name, minigameCommandList));
                MinigameData.getMinigameData().sort((minigame1, minigame2) ->
                {
                    if (minigame1.getName().equals("Main"))
                    {
                        return -1;
                    }
                    else
                    {
                        return new CompareToBuilder().append(minigame1.getName(), minigame2.getName()).build();
                    }
                });
            }
            LoggerIN.info("Successfully getting minigames data from GitHub!");
        }
        catch (IOException | JsonSyntaxException e)
        {
            e.printStackTrace();
            LoggerIN.error("Could not get minigames data from GitHub!");
            MinigameData.addMinigameData(new MinigameData("Could not get minigames data from Database!", new ArrayList<>()));
        }
    }
}