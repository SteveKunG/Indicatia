package com.stevekung.indicatia.hud;

import java.util.Random;

import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.utils.hud.HUDHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.ServerData;
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
        PlayerInfo info = this.mc.getConnection().getPlayerInfo(this.mc.player.getUUID());

        if (info != null)
        {
            if (info.getLatency() > 0)
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
        ServerData server = this.mc.getCurrentServer();
        return server != null && server.ip.contains("hypixel");
    }

    public String getMoonPhase(Minecraft mc)
    {
        int[] moonPhaseFactors = {4, 3, 2, 1, 0, -1, -2, -3};
        String status;

        switch (moonPhaseFactors[mc.level.dimensionType().moonPhase(mc.level.getDayTime())])
        {
            case 4:
            default:
                status = "hud.moon_phase.full_moon";
                break;
            case 3:
                status = "hud.moon_phase.waning_gibbous";
                break;
            case 2:
                status = "hud.moon_phase.last_quarter";
                break;
            case 1:
                status = "hud.moon_phase.waning_crescent";
                break;
            case 0:
                status = "hud.moon_phase.new_moon";
                break;
            case -1:
                status = "hud.moon_phase.waxing_crescent";
                break;
            case -2:
                status = "hud.moon_phase.first_quarter";
                break;
            case -3:
                status = "hud.moon_phase.waxing_gibbous";
                break;
        }
        return status;
    }

    public boolean isSlimeChunk(BlockPos pos)
    {
        int x = Mth.intFloorDiv(pos.getX(), 16);
        int z = Mth.intFloorDiv(pos.getZ(), 16);
        Random rand = new Random(IndicatiaSettings.INSTANCE.slimeChunkSeed + x * x * 4987142L + x * 5947611L + z * z * 4392871L + z * 389711L ^ 987234911L);
        return rand.nextInt(10) == 0;
    }
}