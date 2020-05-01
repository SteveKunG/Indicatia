package com.stevekung.indicatia.utils;

import net.minecraft.util.Formatting;

public enum MojangServerStatus
{
    ONLINE("Online", Formatting.GREEN),
    UNSTABLE("Unstable", Formatting.YELLOW),
    OFFLINE("Offline", Formatting.DARK_RED),
    UNKNOWN("Unknown", Formatting.RED);

    private String status;
    private Formatting color;

    MojangServerStatus(String status, Formatting color)
    {
        this.status = status;
        this.color = color;
    }

    public String getStatus()
    {
        return this.status;
    }

    public Formatting getColor()
    {
        return this.color;
    }

    public static MojangServerStatus get(String status)
    {
        if (status.equalsIgnoreCase("green"))
        {
            return MojangServerStatus.ONLINE;
        }
        else if (status.equalsIgnoreCase("yellow"))
        {
            return MojangServerStatus.UNSTABLE;
        }
        else if (status.equalsIgnoreCase("red"))
        {
            return MojangServerStatus.OFFLINE;
        }
        else
        {
            return MojangServerStatus.UNKNOWN;
        }
    }
}