package com.stevekung.indicatia.utils;

import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public enum MojangServerStatus
{
    ONLINE(LangUtils.formatted("status.mojang.online", TextFormatting.GREEN)),
    OFFLINE(LangUtils.formatted("status.mojang.offline", TextFormatting.DARK_RED)),
    UNSTABLE(LangUtils.formatted("status.mojang.unstable", TextFormatting.YELLOW)),
    UNKNOWN(LangUtils.formatted("status.mojang.unknown", TextFormatting.RED));

    private final ITextComponent status;

    private MojangServerStatus(ITextComponent status)
    {
        this.status = status;
    }

    public ITextComponent getStatus()
    {
        return this.status;
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