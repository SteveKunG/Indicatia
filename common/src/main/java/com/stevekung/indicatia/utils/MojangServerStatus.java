package com.stevekung.indicatia.utils;

import com.stevekung.stevekungslib.utils.LangUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public enum MojangServerStatus
{
    ONLINE(LangUtils.formatted("status.mojang.online", ChatFormatting.GREEN)),
    OFFLINE(LangUtils.formatted("status.mojang.offline", ChatFormatting.DARK_RED)),
    UNSTABLE(LangUtils.formatted("status.mojang.unstable", ChatFormatting.YELLOW)),
    UNKNOWN(LangUtils.formatted("status.mojang.unknown", ChatFormatting.RED));

    private final Component status;

    MojangServerStatus(Component status)
    {
        this.status = status;
    }

    public Component getStatus()
    {
        return this.status;
    }

    public static MojangServerStatus get(String status)
    {
        return switch (status)
                {
                    case "green" -> MojangServerStatus.ONLINE;
                    case "yellow" -> MojangServerStatus.UNSTABLE;
                    case "red" -> MojangServerStatus.OFFLINE;
                    default -> MojangServerStatus.UNKNOWN;
                };
    }
}