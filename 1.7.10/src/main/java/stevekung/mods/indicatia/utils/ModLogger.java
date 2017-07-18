package stevekung.mods.indicatia.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModLogger
{
    private static final Logger LOG;

    static
    {
        LOG = LogManager.getLogger("Indicatia");
    }

    public static void info(String message)
    {
        ModLogger.LOG.info(message);
    }

    public static void error(String message)
    {
        ModLogger.LOG.error(message);
    }

    public static void warning(String message)
    {
        ModLogger.LOG.warn(message);
    }

    public static void info(String message, Object... obj)
    {
        ModLogger.LOG.info(message, obj);
    }

    public static void error(String message, Object... obj)
    {
        ModLogger.LOG.error(message, obj);
    }

    public static void warning(String message, Object... obj)
    {
        ModLogger.LOG.warn(message, obj);
    }
}