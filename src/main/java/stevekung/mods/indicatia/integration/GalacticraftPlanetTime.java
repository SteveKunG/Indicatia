package stevekung.mods.indicatia.integration;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import net.minecraft.client.Minecraft;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.ColorUtils;

public class GalacticraftPlanetTime
{
    public static String getTime(Minecraft mc)
    {
        if (!(mc.world.provider instanceof WorldProviderSpace))
        {
            return null;
        }

        WorldProviderSpace space = (WorldProviderSpace) mc.world.provider;
        long dayLength = space.getDayLength();

        if (dayLength >= 1L && dayLength <= 24L)
        {
            return ColorUtils.stringToRGB(ExtendedConfig.gameTimeColor).toColoredFont() + "Game: " + ColorUtils.stringToRGB(ExtendedConfig.gameTimeValueColor).toColoredFont() + "Fastest Day-Night Cycle";
        }
        else if (dayLength == 0L)
        {
            return ColorUtils.stringToRGB(ExtendedConfig.gameTimeColor).toColoredFont() + "Game: " + ColorUtils.stringToRGB(ExtendedConfig.gameTimeValueColor).toColoredFont() + "No Day-Night Cycle";
        }

        long spaceWorldTime = space.getWorldTime() % dayLength;
        long worldTimeDivide = dayLength / 24;
        int hours = (int)((spaceWorldTime / worldTimeDivide + 6) % 24);
        int minutes = (int)(60 * (spaceWorldTime % worldTimeDivide) / worldTimeDivide);
        String sminutes = "" + minutes;
        String shours = "" + hours;
        String ampm = hours >= 12 ? "PM" : "AM";

        if (hours <= 9)
        {
            shours = 0 + "" + hours;
        }
        if (minutes <= 9)
        {
            sminutes = 0 + "" + minutes;
        }
        return ColorUtils.stringToRGB(ExtendedConfig.gameTimeColor).toColoredFont() + "Game: " + ColorUtils.stringToRGB(ExtendedConfig.gameTimeValueColor).toColoredFont() + shours + ":" + sminutes + " " + ampm;
    }
}
