package stevekung.mods.indicatia.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;

import stevekung.mods.indicatia.core.IndicatiaMod;

public class VersionChecker implements Runnable
{
    private static boolean LATEST;
    private static boolean NO_CONNECTION;
    private static String LATEST_VERSION;
    private static String EXCEPTION_MESSAGE;
    private static List<String> ANNOUNCE_MESSAGE = Lists.newArrayList();
    public static final VersionChecker INSTANCE = new VersionChecker();

    public static void startCheck()
    {
        Thread thread = new Thread(VersionChecker.INSTANCE);
        thread.start();
    }

    @Override
    public void run()
    {
        InputStream version = null;
        InputStream desc = null;

        try
        {
            version = new URL("https://raw.githubusercontent.com/SteveKunG/VersionCheckLibrary/master/indicatia/indicatia_version.txt").openStream();
            desc = new URL("https://raw.githubusercontent.com/SteveKunG/VersionCheckLibrary/master/indicatia/indicatia_desc.txt").openStream();
        }
        catch (MalformedURLException e)
        {
            VersionChecker.EXCEPTION_MESSAGE = e.getClass().getName() + " " + e.getMessage();
            e.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            VersionChecker.EXCEPTION_MESSAGE = e.getClass().getName() + " " + e.getMessage();
            e.printStackTrace();
        }
        catch (Exception e)
        {
            VersionChecker.EXCEPTION_MESSAGE = e.getClass().getName() + " " + e.getMessage();
            e.printStackTrace();
        }

        if (version == null && desc == null)
        {
            VersionChecker.NO_CONNECTION = true;
            return;
        }

        try
        {
            for (EnumMCVersion mcVersion : EnumMCVersion.valuesCached())
            {
                if (IndicatiaMod.MC_VERSION.equals(mcVersion.getVersion()))
                {
                    VersionChecker.LATEST_VERSION = IOUtils.readLines(version).get(mcVersion.ordinal());
                }
            }
            VersionChecker.ANNOUNCE_MESSAGE = IOUtils.readLines(desc);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(version);
            IOUtils.closeQuietly(desc);
        }

        int major = 0;
        int minor = 0;
        int build = 0;
        String latest = VersionChecker.LATEST_VERSION;

        if (latest.contains("[" + IndicatiaMod.MC_VERSION + "]="))
        {
            latest = latest.replace("[" + IndicatiaMod.MC_VERSION + "]=", "").replace(".", "");

            try
            {
                major = Integer.parseInt(String.valueOf(latest.charAt(0)));
                minor = Integer.parseInt(String.valueOf(latest.charAt(1)));
                build = Integer.parseInt(String.valueOf(latest.charAt(2)));
            }
            catch (Exception e) {}
        }
        String latestVersion = major + "." + minor + "." + build;
        VersionChecker.LATEST = !IndicatiaMod.VERSION.equals(latestVersion) && (major > IndicatiaMod.MAJOR_VERSION || minor > IndicatiaMod.MINOR_VERSION || build > IndicatiaMod.BUILD_VERSION);
    }

    public boolean isLatestVersion()
    {
        return VersionChecker.LATEST;
    }

    public boolean noConnection()
    {
        return VersionChecker.NO_CONNECTION;
    }

    public String getLatestVersion()
    {
        return VersionChecker.LATEST_VERSION;
    }

    public String getLatestVersionReplaceMC()
    {
        return VersionChecker.LATEST_VERSION.replace("[" + IndicatiaMod.MC_VERSION + "]=", "");
    }

    public String getExceptionMessage()
    {
        return VersionChecker.EXCEPTION_MESSAGE;
    }

    public List<String> getAnnounceMessage()
    {
        return VersionChecker.ANNOUNCE_MESSAGE;
    }
}