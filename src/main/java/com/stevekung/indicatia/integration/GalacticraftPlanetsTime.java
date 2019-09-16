package com.stevekung.indicatia.integration;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.utils.InfoOverlay;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import net.minecraft.client.Minecraft;

public class GalacticraftPlanetsTime
{
    public static InfoOverlay getSpaceTime(Minecraft mc)
    {
        if (!(mc.world.dimension instanceof WorldProviderSpace))
        {
            return null;
        }

        WorldProviderSpace space = (WorldProviderSpace) mc.world.dimension;
        long dayLength = space.getDayLength();
        StringBuilder builder = new StringBuilder();
        long spaceWorldTime = space.getWorldTime() % dayLength;
        long worldTimeDivide = dayLength / 24;
        int hours = (int)((spaceWorldTime / worldTimeDivide + 6) % 24);
        int minutes = (int)(60 * (spaceWorldTime % worldTimeDivide) / worldTimeDivide);

        if (dayLength >= 1L && dayLength <= 24L)
        {
            return new InfoOverlay("Game", "Fastest Day-Night Cycle", ExtendedConfig.INSTANCE.gameTimeColor, ExtendedConfig.INSTANCE.gameTimeValueColor, InfoOverlay.Position.RIGHT);
        }
        else if (dayLength == 0L)
        {
            return new InfoOverlay("Game", "No Day-Night Cycle", ExtendedConfig.INSTANCE.gameTimeColor, ExtendedConfig.INSTANCE.gameTimeValueColor, InfoOverlay.Position.RIGHT);
        }

        if (hours <= 9)
        {
            builder.append(0);
        }

        builder.append(hours);
        builder.append(":");

        if (minutes <= 9)
        {
            builder.append(0);
        }
        builder.append(minutes);
        builder.append(" " + (hours >= 12 ? "PM" : "AM"));
        return new InfoOverlay("Game", builder.toString(), ExtendedConfig.INSTANCE.gameTimeColor, ExtendedConfig.INSTANCE.gameTimeValueColor, InfoOverlay.Position.RIGHT);
    }
}