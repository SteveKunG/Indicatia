package stevekung.mods.indicatia.utils;

import net.minecraft.util.EnumChatFormatting;

public enum MojangServerStatus
{
    ONLINE("Online", EnumChatFormatting.GREEN.getFriendlyName()),
    UNSTABLE("Unstable", EnumChatFormatting.YELLOW.getFriendlyName()),
    OFFLINE("Offline", EnumChatFormatting.DARK_RED.getFriendlyName()),
    UNKNOWN("Unknown", EnumChatFormatting.RED.getFriendlyName());

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