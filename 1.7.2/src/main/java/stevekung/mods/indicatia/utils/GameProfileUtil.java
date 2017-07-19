package stevekung.mods.indicatia.utils;

import stevekung.mods.indicatia.core.IndicatiaMod;

public class GameProfileUtil
{
    public static String getUsername()
    {
        return IndicatiaMod.MC.getSession().func_148256_e().getName();
    }

    public static String getUUID()
    {
        return IndicatiaMod.MC.getSession().func_148256_e().getId();
    }
}