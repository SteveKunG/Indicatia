package stevekung.mods.indicatia.util;

import java.util.UUID;

import stevekung.mods.indicatia.core.IndicatiaMod;

public class GameProfileUtil
{
    public static String getUsername()
    {
        return IndicatiaMod.MC.getSession().func_148256_e().getName();
    }

    public static UUID getUUID()
    {
        return IndicatiaMod.MC.getSession().func_148256_e().getId();
    }
}