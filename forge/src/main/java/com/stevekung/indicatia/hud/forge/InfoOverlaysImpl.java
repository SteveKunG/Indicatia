package com.stevekung.indicatia.hud.forge;

import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.hud.InfoOverlays;
import com.stevekung.indicatia.utils.hud.InfoOverlay;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class InfoOverlaysImpl
{
    public static void getTPS(MinecraftServer server)
    {
        double overallTPS = InfoOverlays.mean(server.tickTimes) * 1.0E-6D;
        double overworldTPS = InfoOverlays.mean(server.getTickTime(Level.OVERWORLD)) * 1.0E-6D;
        double tps = Math.min(1000.0D / overallTPS, 20);

        InfoOverlays.ALL_TPS.clear();
        InfoOverlays.OVERALL_TPS = new InfoOverlay("Overall TPS", InfoOverlays.TPS_FORMAT.format(overallTPS), IndicatiaSettings.INSTANCE.tpsColor, IndicatiaSettings.INSTANCE.tpsValueColor, InfoOverlay.Position.LEFT);

        if (IndicatiaSettings.INSTANCE.tpsAllDims)
        {
            InfoOverlays.OVERWORLD_TPS = InfoOverlay.empty();

            for (ServerLevel world : server.getAllLevels())
            {
                long[] values = server.getTickTime(world.dimension());
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
}