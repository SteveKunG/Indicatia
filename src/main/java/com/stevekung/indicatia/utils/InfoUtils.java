package com.stevekung.indicatia.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.stevekungslib.utils.ColorUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class InfoUtils
{
    public static final InfoUtils INSTANCE = new InfoUtils();

    private InfoUtils() {}

    public int getPing()
    {
        PlayerListEntry info = MinecraftClient.getInstance().getNetworkHandler().getPlayerListEntry(MinecraftClient.getInstance().player.getUuid());

        if (info != null)
        {
            return info.getLatency();
        }
        return 0;
    }

    public boolean isHypixel()
    {
        IntegratedServer server = MinecraftClient.getInstance().getServer();

        if (server != null && !Strings.isNullOrEmpty(server.getServerIp()))
        {
            Pattern pattern = Pattern.compile("^(?:(?:(?:.*\\.)?hypixel\\.net)|(?:209\\.222\\.115\\.\\d{1,3}))(?::\\d{1,5})?$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(server.getServerIp());
            return matcher.find();
        }
        return false;
    }

    public String getCurrentGameTime(long worldTicks)
    {
        int hours = (int)((worldTicks / 1000 + 6) % 24);
        int minutes = (int)(60 * (worldTicks % 1000) / 1000);
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
        return ColorUtils.stringToRGB(ExtendedConfig.instance.gameTimeColor).toColoredFont() + "Game: " + ColorUtils.stringToRGB(ExtendedConfig.instance.gameTimeValueColor).toColoredFont() + shours + ":" + sminutes + " " + ampm;
    }

    public String getMoonPhase(MinecraftClient mc)
    {
        int[] moonPhaseFactors = { 4, 3, 2, 1, 0, -1, -2, -3 };
        int phase = moonPhaseFactors[mc.world.dimension.getMoonPhase(mc.world.getTimeOfDay())];
        String status;

        switch (phase)
        {
        case 4:
        default:
            status = "Full Moon";
            break;
        case 3:
            status = "Waning Gibbous";
            break;
        case 2:
            status = "Last Quarter";
            break;
        case 1:
            status = "Waning Crescent";
            break;
        case 0:
            status = "New Moon";
            break;
        case -1:
            status = "Waxing Crescent";
            break;
        case -2:
            status = "First Quarter";
            break;
        case -3:
            status = "Waxing Gibbous";
            break;
        }
        return ColorUtils.stringToRGB(ExtendedConfig.instance.moonPhaseColor).toColoredFont() + "Moon Phase: " + ColorUtils.stringToRGB(ExtendedConfig.instance.moonPhaseValueColor).toColoredFont() + status;
    }

    public boolean isSlimeChunk(BlockPos pos)
    {
        int x = MathHelper.floorDiv(pos.getX(), 16);
        int z = MathHelper.floorDiv(pos.getZ(), 16);
        Random rnd = new Random(ExtendedConfig.instance.slimeChunkSeed + x * x * 4987142 + x * 5947611 + z * z * 4392871L + z * 389711 ^ 987234911L);
        return rnd.nextInt(10) == 0;
    }
}