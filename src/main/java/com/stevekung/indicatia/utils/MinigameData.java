package com.stevekung.indicatia.utils;

import java.util.ArrayList;
import java.util.List;

public class MinigameData
{
    private static final List<MinigameData> DATA = new ArrayList<>();
    private final String name;
    private final List<MinigameData.Command> commands;

    public MinigameData(String name, List<MinigameData.Command> commands)
    {
        this.name = name;
        this.commands = commands;
    }

    public String getName()
    {
        return this.name;
    }

    public List<MinigameData.Command> getCommands()
    {
        return this.commands;
    }

    public static List<MinigameData> getMinigames()
    {
        return MinigameData.DATA;
    }

    public static void addMinigame(MinigameData data)
    {
        MinigameData.DATA.add(data);
    }

    public static class Command
    {
        private final String name;
        private final String command;
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