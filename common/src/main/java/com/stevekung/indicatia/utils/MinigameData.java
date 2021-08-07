package com.stevekung.indicatia.utils;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;

public record MinigameData(String name, boolean sort, List<Command> commands)
{
    public static List<MinigameData> DATA = Lists.newArrayList();

    public static void addMinigame(MinigameData data)
    {
        MinigameData.DATA.add(data);
    }

    public record Command(String name, String command, @SerializedName("minigame") boolean isMinigame, String uuid, String texture) {}
}