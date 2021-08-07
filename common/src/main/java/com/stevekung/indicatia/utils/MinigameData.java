package com.stevekung.indicatia.utils;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;

public record MinigameData(String name, boolean sort, List<Command> commands)
{
    public static List<MinigameData> DATA = Lists.newArrayList();

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

    public record Command(String name, String command, @SerializedName("minigame") boolean isMinigame, String uuid, String texture)
    {
        public String getName()
        {
            return this.name;
        }

        public String getCommand()
        {
            return this.command;
        }

        @Override
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