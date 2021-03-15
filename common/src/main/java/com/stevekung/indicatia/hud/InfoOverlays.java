package com.stevekung.indicatia.hud;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.common.collect.Lists;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.utils.MinecraftServerTick;
import com.stevekung.indicatia.utils.hud.InfoOverlay;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.ModDecimalFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class InfoOverlays
{
    public static InfoOverlay OVERALL_TPS = InfoOverlay.empty();
    public static InfoOverlay OVERWORLD_TPS = InfoOverlay.empty();
    public static InfoOverlay TPS = InfoOverlay.empty();
    public static final List<InfoOverlay> ALL_TPS = Lists.newArrayList();
    private static final ModDecimalFormat TPS_FORMAT = new ModDecimalFormat("########0.00");

    public static InfoOverlay getDirection(Minecraft mc)
    {
        Entity entity = mc.getCameraEntity();
        Direction coordDirection = entity.getDirection();
        int yaw = (int)entity.yRot + 22;
        String direction;
        String coord;

        yaw %= 360;

        if (yaw < 0)
        {
            yaw += 360;
        }

        int facing = yaw / 45;

        switch (coordDirection)
        {
            default:
            case NORTH:
                coord = "-Z";
                break;
            case SOUTH:
                coord = "+Z";
                break;
            case WEST:
                coord = "-X";
                break;
            case EAST:
                coord = "+X";
                break;
        }

        switch (facing)
        {
            case 0:
                direction = "hud.direction.south";
                break;
            case 1:
                direction = "hud.direction.south_west";
                break;
            case 2:
                direction = "hud.direction.west";
                break;
            case 3:
                direction = "hud.direction.north_west";
                break;
            default:
            case 4:
                direction = "hud.direction.north";
                break;
            case 5:
                direction = "hud.direction.north_east";
                break;
            case 6:
                direction = "hud.direction.east";
                break;
            case 7:
                direction = "hud.direction.south_east";
                break;
        }
        direction = LangUtils.translateString(direction);
        direction += " (" + coord + ")";
        return new InfoOverlay("hud.direction", direction, IndicatiaSettings.INSTANCE.directionColor, IndicatiaSettings.INSTANCE.directionValueColor, InfoOverlay.Position.LEFT);
    }

    public static void getTPS(MinecraftServer server)
    {
        double overallTPS = InfoOverlays.mean(server.tickTimes) * 1.0E-6D;
        double overworldTPS = InfoOverlays.mean(MinecraftServerTick.getTickTime(server, Level.OVERWORLD)) * 1.0E-6D;
        double tps = Math.min(1000.0D / overallTPS, 20);

        InfoOverlays.ALL_TPS.clear();
        InfoOverlays.OVERALL_TPS = new InfoOverlay("Overall TPS", InfoOverlays.TPS_FORMAT.format(overallTPS), IndicatiaSettings.INSTANCE.tpsColor, IndicatiaSettings.INSTANCE.tpsValueColor, InfoOverlay.Position.LEFT);

        if (IndicatiaSettings.INSTANCE.tpsAllDims)
        {
            InfoOverlays.OVERWORLD_TPS = InfoOverlay.empty();

            for (ServerLevel world : server.getAllLevels())
            {
                long[] values = MinecraftServerTick.getTickTime(server, world.dimension());
                String dimensionName = world.dimension().location().toString();

                if (values == null)
                {
                    continue;
                }
                double dimensionTPS = InfoOverlays.mean(values) * 1.0E-6D;
                InfoOverlays.ALL_TPS.add(new InfoOverlay("Dimension " + dimensionName, InfoOverlays.TPS_FORMAT.format(dimensionTPS), IndicatiaSettings.INSTANCE.tpsColor, IndicatiaSettings.INSTANCE.tpsValueColor, InfoOverlay.Position.LEFT));
            }
        }
        else
        {
            InfoOverlays.OVERWORLD_TPS = new InfoOverlay("Overworld TPS", InfoOverlays.TPS_FORMAT.format(overworldTPS), IndicatiaSettings.INSTANCE.tpsColor, IndicatiaSettings.INSTANCE.tpsValueColor, InfoOverlay.Position.LEFT);
        }
        InfoOverlays.TPS = new InfoOverlay("TPS", InfoOverlays.TPS_FORMAT.format(tps), IndicatiaSettings.INSTANCE.tpsColor, IndicatiaSettings.INSTANCE.tpsValueColor, InfoOverlay.Position.LEFT);
    }

    public static InfoOverlay getRealWorldTime()
    {
        Date date = new Date();
        String dateIns = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(date);
        String timeIns = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.getDefault()).format(date);
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
        StringBuilder builder = new StringBuilder();
        int hours = (int)((worldTicks / 1000 + 6) % 24);
        int minutes = (int)(60 * (worldTicks % 1000) / 1000);

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

    private static long mean(long[] values)
    {
        long sum = 0L;

        for (long value : values)
        {
            sum += value;
        }
        return sum / values.length;
    }
}