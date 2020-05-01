package com.stevekung.indicatia.extra;

import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import net.minecraft.client.MinecraftClient;

public class ThreadDownloadWeatherData implements Runnable
{
    @Override
    public void run()
    {
        URL url = null;

        try
        {
            url = new URL("https://weather.tmd.go.th/lmp/lmp240_latest.gif");
            ImageIO.write(ImageIO.read(url), "png", new File(MinecraftClient.getInstance().runDirectory, "weather.png"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}