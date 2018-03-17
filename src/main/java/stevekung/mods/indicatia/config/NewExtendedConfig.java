package stevekung.mods.indicatia.config;

import java.io.*;

import stevekung.mods.indicatia.core.IndicatiaMod;

public class NewExtendedConfig
{
    private static final File OLD_FILE = new File(IndicatiaMod.MC.mcDataDir, "indicatia_data.dat");
    private static final File FILE = new File(IndicatiaMod.MC.mcDataDir, "indicatia_profile/default_profile.dat");
    
    public static void moveOldConfig() throws IOException
    {
       if (OLD_FILE.exists())
       {
           InputStream input = null;
           OutputStream output = null;
           
           try
           {
               input = new FileInputStream(OLD_FILE);
               output = new FileOutputStream(FILE);
               byte[] buffer = new byte[1024];
               int length;
               
               while ((length = input.read(buffer)) > 0)
               {
                  output.write(buffer, 0, length); 
               }
           }
           finally
           {
               input.close();
               output.close();
           }
       }
    }
}
