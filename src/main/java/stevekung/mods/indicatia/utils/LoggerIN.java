package stevekung.mods.indicatia.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerIN
{
    private static final Logger LOG = LogManager.getLogger("Indicatia");

    public static void info(String message)
    {
        LoggerIN.LOG.info(message);
    }

    public static void error(String message)
    {
        LoggerIN.LOG.error(message);
    }

    public static void warning(String message)
    {
        LoggerIN.LOG.warn(message);
    }

    public static void info(String message, Object... obj)
    {
        LoggerIN.LOG.info(message, obj);
    }

    public static void error(String message, Object... obj)
    {
        LoggerIN.LOG.error(message, obj);
    }

    public static void warning(String message, Object... obj)
    {
        LoggerIN.LOG.warn(message, obj);
    }
}