package stevekung.mods.indicatia.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import stevekung.mods.indicatia.core.IndicatiaMod;

public class ThreadDownloadedCustomCape extends Thread
{
    private final String url;

    public ThreadDownloadedCustomCape(String url)
    {
        this.url = url;
        this.setDaemon(true);
    }

    @Override
    public void run()
    {
        URL capeUrl = null;
        JsonUtil json = IndicatiaMod.json;

        try
        {
            capeUrl = new URL(this.url);
            ImageIO.write(ImageIO.read(capeUrl), "png", CapeUtil.pngFile);
            CapeUtil.textureDownloaded = false;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();

            if (IndicatiaMod.MC.player != null)
            {
                IndicatiaMod.MC.player.sendMessage(json.text("Missing protocol or wrong Image URL format, must be .png!").setStyle(json.red()));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();

            if (IndicatiaMod.MC.player != null)
            {
                IndicatiaMod.MC.player.sendMessage(json.text("Cannot read image from URL/No internet connection!").setStyle(json.red()));
            }
        }
    }
}