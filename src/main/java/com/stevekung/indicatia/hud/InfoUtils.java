package com.stevekung.indicatia.hud;

import java.util.Random;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.event.IndicatiaEventHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

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
        NetworkPlayerInfo info = this.mc.getConnection().getPlayerInfo(this.mc.player.getUniqueID());

        if (info != null)
        {
            if (info.getResponseTime() > 0)
            {
                return info.getResponseTime();
            }
            else
            {
                return IndicatiaEventHandler.currentServerPing;
            }
        }
        return 0;
    }

    public String getResponseTimeColor(int responseTime)
    {
        if (responseTime >= 200 && responseTime < 300)
        {
            return ExtendedConfig.INSTANCE.ping200And300Color;
        }
        else if (responseTime >= 300 && responseTime < 500)
        {
            return ExtendedConfig.INSTANCE.ping300And500Color;
        }
        else if (responseTime >= 500)
        {
            return ExtendedConfig.INSTANCE.pingMax500Color;
        }
        else
        {
            return ExtendedConfig.INSTANCE.pingValueColor;
        }
    }

    public boolean isHypixel()
    {
        ServerData server = this.mc.getCurrentServerData();
        return server != null && server.serverIP.contains("hypixel");
    }

    public String getMoonPhase(Minecraft mc)
    {
        int[] moonPhaseFactors = { 4, 3, 2, 1, 0, -1, -2, -3 };
        String status;

        switch (moonPhaseFactors[mc.world.dimension.getMoonPhase(mc.world.getDayTime())])
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
        int x = MathHelper.intFloorDiv(pos.getX(), 16);
        int z = MathHelper.intFloorDiv(pos.getZ(), 16);
        Random rand = new Random(ExtendedConfig.INSTANCE.slimeChunkSeed + x * x * 4987142 + x * 5947611 + z * z * 4392871L + z * 389711 ^ 987234911L);
        return rand.nextInt(10) == 0;
    }
}