package com.stevekung.indicatia.hud;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.common.collect.Lists;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.utils.hud.InfoOverlay;
import com.stevekung.stevekunglib.utils.LangUtils;
import com.stevekung.stevekunglib.utils.ModDecimalFormat;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class InfoOverlays
{
    public static InfoOverlay OVERALL_TPS = InfoOverlay.empty();
    public static InfoOverlay OVERWORLD_TPS = InfoOverlay.empty();
    public static InfoOverlay TPS = InfoOverlay.empty();
    public static final List<InfoOverlay> ALL_TPS = Lists.newArrayList();
    public static final ModDecimalFormat TPS_FORMAT = new ModDecimalFormat("########0.00");

    public static InfoOverlay getDirection(Minecraft mc)
    {
        var entity = mc.getCameraEntity();
        var coordDirection = entity.getDirection();
        var yaw = (int) entity.getYRot() + 22;
        String direction;
        String coord;

        yaw %= 360;

        if (yaw < 0)
        {
            yaw += 360;
        }

        var facing = yaw / 45;

        coord = switch (coordDirection)
                {
                    case NORTH -> "-Z";
                    case SOUTH -> "+Z";
                    case WEST -> "-X";
                    case EAST -> "+X";
                    default -> throw new IllegalStateException("Invalid direction");
                };

        direction = switch (facing)
                {
                    case 0 -> "hud.direction.south";
                    case 1 -> "hud.direction.south_west";
                    case 2 -> "hud.direction.west";
                    case 3 -> "hud.direction.north_west";
                    case 4 -> "hud.direction.north";
                    case 5 -> "hud.direction.north_east";
                    case 6 -> "hud.direction.east";
                    case 7 -> "hud.direction.south_east";
                    default -> throw new IllegalStateException("Invalid direction");
                };
        direction = LangUtils.translateString(direction);
        direction += " (" + coord + ")";
        return new InfoOverlay("hud.direction", direction, IndicatiaSettings.INSTANCE.directionColor, IndicatiaSettings.INSTANCE.directionValueColor, InfoOverlay.Position.LEFT);
    }

    @ExpectPlatform
    public static void getTPS(MinecraftServer server)
    {
    }

    public static InfoOverlay getRealWorldTime()
    {
        var date = new Date();
        var dateIns = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(date);
        var timeIns = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.getDefault()).format(date);
        return new InfoOverlay("hud.real_time", dateIns + " " + timeIns, IndicatiaSettings.INSTANCE.realTimeColor, IndicatiaSettings.INSTANCE.realTimeValueColor, InfoOverlay.Position.RIGHT);
    }

    public static InfoOverlay getGameTime(Minecraft mc)
    {
        /*boolean isSpace = false; //TODO Rewrite

        if (IndicatiaMod.isGalacticraftLoaded)
        {
            try
            {
                Class<?> spaceWorld = Class.forName("micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace");
                isSpace = spaceWorld.isAssignableFrom(mc.player.world.dimension.getClass());
            }
            catch (Exception e) {}

            if (isSpace)
            {
                return GalacticraftPlanetsTime.getSpaceTime(mc);
            }
        }*/
        return InfoOverlays.getVanillaGameTime(mc.level.getDayTime() % 24000);
    }

    private static InfoOverlay getVanillaGameTime(long worldTicks)
    {
        var builder = new StringBuilder();
        var hours = (int) ((worldTicks / 1000 + 6) % 24);
        var minutes = (int) (60 * (worldTicks % 1000) / 1000);

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
        builder.append(" ").append(hours >= 12 ? "PM" : "AM");
        return new InfoOverlay("hud.time", builder.toString(), IndicatiaSettings.INSTANCE.gameTimeColor, IndicatiaSettings.INSTANCE.gameTimeValueColor, InfoOverlay.Position.RIGHT);
    }

    public static long mean(long[] values)
    {
        var sum = 0L;

        for (var value : values)
        {
            sum += value;
        }
        return sum / values.length;
    }
}