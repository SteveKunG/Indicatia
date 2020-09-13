package com.stevekung.indicatia.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MinigameData
{
    public static List<MinigameData> DATA = new ArrayList<>();
    private final String name;
    private final boolean sort;
    private final List<MinigameData.Command> commands;

    public MinigameData(String name, boolean sort, List<MinigameData.Command> commands)
    {
        this.name = name;
        this.sort = sort;
        this.commands = commands;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean isSorted()
    {
        return this.sort;
    }

    public List<MinigameData.Command> getCommands()
    {
        return this.commands;
    }

    public static void addMinigame(MinigameData data)
    {
        MinigameData.DATA.add(data);
    }

    public static class Command
    {
        private final String name;
        private final String command;
        @SerializedName("minigame")
        private final boolean isMinigame;
        private final String uuid;
        private final String texture;

        public Command(String name, String command, boolean isMinigame, String uuid, String texture)
        {
            this.name = name;
            this.command = command;
            this.isMinigame = isMinigame;
            this.uuid = uuid;
            this.texture = texture;
        }

        public String getName()
        {
            return this.name;
        }

        public String getCommand()
        {
            return this.command;
        }

        public boolean isMinigame()
        {
            return this.isMinigame;
        }

        public String getUUID()
        {
            return this.uuid;
        }

        public String getTexture()
        {
            return this.texture;
        }
    }
}