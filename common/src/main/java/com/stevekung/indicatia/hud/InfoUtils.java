package com.stevekung.indicatia.hud;

import java.util.Random;

import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.utils.hud.HUDHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

public class InfoUtils
{
    public static final InfoUtils INSTANCE = new InfoUtils();
    private final Minecraft mc;

    private InfoUtils()
    {
        this.mc = Minecraft.getInstance();
    }

    public int getPing()
    {
        var info = this.mc.getConnection().getPlayerInfo(this.mc.player.getUUID());

        if (info != null)
        {
            if (info.getLatency() > 1)
            {
                return info.getLatency();
            }
            else
            {
                return HUDHelper.currentServerPing;
            }
        }
        return HUDHelper.currentServerPing;
    }

    public String getResponseTimeColor(int responseTime)
    {
        if (responseTime >= 200 && responseTime < 300)
        {
            return IndicatiaSettings.INSTANCE.ping200And300Color;
        }
        else if (responseTime >= 300 && responseTime < 500)
        {
            return IndicatiaSettings.INSTANCE.ping300And500Color;
        }
        else if (responseTime >= 500)
        {
            return IndicatiaSettings.INSTANCE.pingMax500Color;
        }
        else
        {
            return IndicatiaSettings.INSTANCE.pingValueColor;
        }
    }

    public boolean isHypixel()
    {
        var server = this.mc.getCurrentServer();
        return server != null && server.ip.contains("hypixel");
    }

    public String getMoonPhase(Minecraft mc)
    {
        var moonPhaseFactors = new int[] {4, 3, 2, 1, 0, -1, -2, -3};
        return switch (moonPhaseFactors[mc.level.dimensionType().moonPhase(mc.level.getDayTime())])
                {
                    default -> "hud.moon_phase.full_moon";
                    case 3 -> "hud.moon_phase.waning_gibbous";
                    case 2 -> "hud.moon_phase.last_quarter";
                    case 1 -> "hud.moon_phase.waning_crescent";
                    case 0 -> "hud.moon_phase.new_moon";
                    case -1 -> "hud.moon_phase.waxing_crescent";
                    case -2 -> "hud.moon_phase.first_quarter";
                    case -3 -> "hud.moon_phase.waxing_gibbous";
                };
    }

    public boolean isSlimeChunk(BlockPos pos)
    {
        var x = Mth.intFloorDiv(pos.getX(), 16);
        var z = Mth.intFloorDiv(pos.getZ(), 16);
        var rand = new Random(IndicatiaSettings.INSTANCE.slimeChunkSeed + x * x * 4987142L + x * 5947611L + z * z * 4392871L + z * 389711L ^ 987234911L);
        return rand.nextInt(10) == 0;
    }
}