package stevekung.mods.stevekunglib.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModLogger
{
    private static final Logger LOG = LogManager.getLogger("SteveKunG's Lib");
    private static final Logger LOG_DEBUG = LogManager.getLogger("SteveKunG's Lib Debug");

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

    public static void debug(String message)
    {
        //        if (ConfigManagerMP.enableDebug || MorePlanetsCore.isObfuscatedEnvironment())
        {
            ModLogger.LOG_DEBUG.info(message);
        }
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

    public static void debug(String message, Object... obj)
    {
        //        if (ConfigManagerMP.enableDebug || MorePlanetsCore.isObfuscatedEnvironment())
        {
            ModLogger.LOG_DEBUG.info(message, obj);
        }
    }
}