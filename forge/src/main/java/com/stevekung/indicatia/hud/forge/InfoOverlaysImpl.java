package com.stevekung.indicatia.hud.forge;

import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.hud.InfoOverlays;
import com.stevekung.indicatia.utils.hud.InfoOverlay;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

public class InfoOverlaysImpl
{
    public static void getTPS(MinecraftServer server)
    {
        var overallTPS = InfoOverlays.mean(server.tickTimes) * 1.0E-6D;
        var overworldTPS = InfoOverlays.mean(server.getTickTime(Level.OVERWORLD)) * 1.0E-6D;
        var tps = Math.min(1000.0D / overallTPS, 20);

        InfoOverlays.ALL_TPS.clear();
        InfoOverlays.OVERALL_TPS = new InfoOverlay("Overall TPS", InfoOverlays.TPS_FORMAT.format(overallTPS), IndicatiaSettings.INSTANCE.tpsColor, IndicatiaSettings.INSTANCE.tpsValueColor, InfoOverlay.Position.LEFT);

        if (IndicatiaSettings.INSTANCE.tpsAllDims)
        {
            InfoOverlays.OVERWORLD_TPS = InfoOverlay.empty();

            for (var world : server.getAllLevels())
            {
                var values = server.getTickTime(world.dimension());
                var dimensionName = world.dimension().location().toString();

                if (values == null)
                {
                    continue;
                }
                var dimensionTPS = InfoOverlays.mean(values) * 1.0E-6D;
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