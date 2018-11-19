package stevekung.mods.indicatia.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
            JsonObject jsonObj = element.getAsJsonObject();
            JsonArray minigames = jsonObj.getAsJsonArray("minigames");

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
                }
                MinigameData.addMinigameData(new MinigameData(name, minigameCommandList));
            }
        }
        catch (IOException | JsonSyntaxException e)
        {
            e.printStackTrace();
            LoggerIN.error("Could not getting minigames data from GitHub!");
            MinigameData.addMinigameData(new MinigameData("null", new ArrayList<>()));
        }
    }
}