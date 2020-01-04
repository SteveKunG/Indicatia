package com.stevekung.indicatia.utils;

import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.util.text.TextFormatting;

public enum MojangServerStatus
{
    ONLINE(LangUtils.translate("status.mojang.online"), TextFormatting.GREEN),
    OFFLINE(LangUtils.translate("status.mojang.offline"), TextFormatting.DARK_RED),
    UNSTABLE(LangUtils.translate("status.mojang.unstable"), TextFormatting.YELLOW),
    UNKNOWN(LangUtils.translate("status.mojang.unknown"), TextFormatting.RED);

    private final String status;
    private final TextFormatting color;

    private MojangServerStatus(String status, TextFormatting color)
    {
        this.status = status;
        this.color = color;
    }

    public String getStatus()
    {
        return this.status;
    }

    public TextFormatting getColor()
    {
        return this.color;
    }

    public static MojangServerStatus get(String status)
    {
        switch (status)
        {
        case "green":
            return MojangServerStatus.ONLINE;
        case "yellow":
            return MojangServerStatus.UNSTABLE;
        case "red":
            return MojangServerStatus.OFFLINE;
        default:
            return MojangServerStatus.UNKNOWN;
        }
    }
}