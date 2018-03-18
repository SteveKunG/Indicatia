package stevekung.mods.indicatia.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import stevekung.mods.indicatia.core.IndicatiaMod;

public class NewExtendedConfig
{
    private static final File OLD_FILE = new File(IndicatiaMod.MC.mcDataDir, "indicatia_data.dat");
    private static final File DEST = new File(IndicatiaMod.MC.mcDataDir, "indicatia_profile");

    public static void moveOldConfig()
    {
        if (!DEST.exists())
        {
            if (!DEST.mkdirs())
            {
                return;
            }
        }

        if (OLD_FILE.exists())
        {
            File destPath = new File(DEST, OLD_FILE.getName());

            if (destPath.exists())
            {
                OLD_FILE.delete();
                return;
            }
            try
            {
                Files.move(OLD_FILE.toPath(), destPath.toPath());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}