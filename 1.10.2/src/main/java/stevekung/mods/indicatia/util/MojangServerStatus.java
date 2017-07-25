package stevekung.mods.indicatia.util;

import net.minecraft.util.text.TextFormatting;

public enum MojangServerStatus
{
    ONLINE("Online", TextFormatting.GREEN.getFriendlyName()),
    UNSTABLE("Unstable", TextFormatting.YELLOW.getFriendlyName()),
    OFFLINE("Offline", TextFormatting.DARK_RED.getFriendlyName()),
    UNKNOWN("Unknown", TextFormatting.RED.getFriendlyName());

    private String status;
    private String color;

    private MojangServerStatus(String status, String color)
    {
        this.status = status;
        this.color = color;
    }

    public String getStatus()
    {
        return this.status;
    }

    public String getColor()
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