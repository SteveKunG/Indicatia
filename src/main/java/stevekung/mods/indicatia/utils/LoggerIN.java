package stevekung.mods.indicatia.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class LoggerIN
{
    private static final Logger LOG = LogManager.getLogger("Indicatia");
    static final Marker INFO = MarkerManager.getMarker("INFO");
    static final Marker ERROR = MarkerManager.getMarker("ERROR");
    static final Marker WARNING = MarkerManager.getMarker("WARNING");

    public static void info(String message)
    {
        LoggerIN.LOG.info(INFO, message);
    }

    public static void error(String message)
    {
        LoggerIN.LOG.error(ERROR, message);
    }

    public static void warning(String message)
    {
        LoggerIN.LOG.warn(WARNING, message);
    }

    public static void info(String message, Object... obj)
    {
        LoggerIN.LOG.info(INFO, message, obj);
    }

    public static void error(String message, Object... obj)
    {
        LoggerIN.LOG.error(ERROR, message, obj);
    }

    public static void warning(String message, Object... obj)
    {
        LoggerIN.LOG.warn(WARNING, message, obj);
    }
}