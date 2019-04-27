package stevekung.mods.indicatia.utils;

import net.minecraft.text.TextFormat;

public enum MojangServerStatus
{
    ONLINE("Online", TextFormat.GREEN),
    UNSTABLE("Unstable", TextFormat.YELLOW),
    OFFLINE("Offline", TextFormat.DARK_RED),
    UNKNOWN("Unknown", TextFormat.RED);

    private String status;
    private TextFormat color;

    MojangServerStatus(String status, TextFormat color)
    {
        this.status = status;
        this.color = color;
    }

    public String getStatus()
    {
        return this.status;
    }

    public TextFormat getColor()
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