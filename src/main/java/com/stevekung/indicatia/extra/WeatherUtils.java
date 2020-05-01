package com.stevekung.indicatia.extra;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import com.mojang.blaze3d.platform.GlStateManager;
import com.stevekung.stevekungslib.utils.JsonUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;

public class WeatherUtils
{
    public static NativeImageBackedTexture WEATHER_TEXTURE;
    public static final File texture = new File(MinecraftClient.getInstance().runDirectory, "weather.png");

    public static void bindWeatherTexture()
    {
        GlStateManager.bindTexture(Optional.ofNullable(WeatherUtils.WEATHER_TEXTURE.getGlId()).orElse(null));
    }

    public static void loadTexture()
    {
        if (WeatherUtils.texture.exists())
        {
            try (NativeImage image = NativeImage.read(new FileInputStream(WeatherUtils.texture)))
            {
                WeatherUtils.WEATHER_TEXTURE = new NativeImageBackedTexture(image);
                MinecraftClient.getInstance().player.sendMessage(JsonUtils.create("Current weather data in your region successfully downloaded").setStyle(JsonUtils.GREEN));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}