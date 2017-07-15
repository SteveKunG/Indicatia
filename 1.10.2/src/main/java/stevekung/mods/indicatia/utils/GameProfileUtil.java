package stevekung.mods.indicatia.utils;

import java.util.UUID;

import stevekung.mods.indicatia.core.IndicatiaMod;

public class GameProfileUtil
{
    public static String getUsername()
    {
        return IndicatiaMod.MC.getSession().getProfile().getName();
    }

    public static UUID getUUID()
    {
        return IndicatiaMod.MC.getSession().getProfile().getId();
    }
}