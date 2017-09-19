package stevekung.mods.indicatia.util;

import net.minecraft.util.text.TextFormatting;

public enum MojangServerStatus
{
    ONLINE("Online", TextFormatting.GREEN),
    UNSTABLE("Unstable", TextFormatting.YELLOW),
    OFFLINE("Offline", TextFormatting.DARK_RED),
    UNKNOWN("Unknown", TextFormatting.RED);

    private String status;
    private TextFormatting color;

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