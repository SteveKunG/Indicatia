package stevekung.mods.indicatia.utils;

import net.minecraft.ChatFormat;

public enum MojangServerStatus
{
    ONLINE("Online", ChatFormat.GREEN),
    UNSTABLE("Unstable", ChatFormat.YELLOW),
    OFFLINE("Offline", ChatFormat.DARK_RED),
    UNKNOWN("Unknown", ChatFormat.RED);

    private String status;
    private ChatFormat color;

    MojangServerStatus(String status, ChatFormat color)
    {
        this.status = status;
        this.color = color;
    }

    public String getStatus()
    {
        return this.status;
    }

    public ChatFormat getColor()
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