package com.stevekung.indicatia.utils;

import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public enum MojangServerStatus
{
    ONLINE(LangUtils.translateComponent("status.mojang.online"), TextFormatting.GREEN),
    OFFLINE(LangUtils.translateComponent("status.mojang.offline"), TextFormatting.DARK_RED),
    UNSTABLE(LangUtils.translateComponent("status.mojang.unstable"), TextFormatting.YELLOW),
    UNKNOWN(LangUtils.translateComponent("status.mojang.unknown"), TextFormatting.RED);

    private final ITextComponent status;
    private final TextFormatting color;

    private MojangServerStatus(ITextComponent status, TextFormatting color)
    {
        this.status = status;
        this.color = color;
    }

    public ITextComponent getStatus()
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