package com.stevekung.indicatia.hud;

import java.util.Random;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.event.IndicatiaEventHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;

public class InfoUtils
{
    public static final InfoUtils INSTANCE = new InfoUtils();
    private final Minecraft mc;
    public Entity extendedPointedEntity;

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
        return server != null && server.serverIP.equals("mc.hypixel.net");
    }

    public int getCPS()
    {
        IndicatiaEventHandler.LEFT_CLICK.removeIf(cps -> cps < Util.milliTime() - 1000L);
        return IndicatiaEventHandler.LEFT_CLICK.size();
    }

    public int getRCPS()
    {
        IndicatiaEventHandler.RIGHT_CLICK.removeIf(rcps -> rcps < Util.milliTime() - 1000L);
        return IndicatiaEventHandler.RIGHT_CLICK.size();
    }

    public String getMoonPhase(Minecraft mc)
    {
        int[] moonPhaseFactors = { 4, 3, 2, 1, 0, -1, -2, -3 };
        String status;

        switch (moonPhaseFactors[mc.world.dimension.getMoonPhase(mc.world.getDayTime())])
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
        return status;
    }

    public boolean isSlimeChunk(BlockPos pos)
    {
        int x = MathHelper.intFloorDiv(pos.getX(), 16);
        int z = MathHelper.intFloorDiv(pos.getZ(), 16);
        Random rand = new Random(ExtendedConfig.INSTANCE.slimeChunkSeed + x * x * 4987142 + x * 5947611 + z * z * 4392871L + z * 389711 ^ 987234911L);
        return rand.nextInt(10) == 0;
    }

    public void getMouseOverEntityExtended(Minecraft mc)
    {
        Entity entity = mc.getRenderViewEntity();
        double distance = 12.0D;

        if (entity != null)
        {
            this.extendedPointedEntity = null;
            mc.objectMouseOver = entity.pick(distance, mc.getRenderPartialTicks(), false);
            Vec3d eyePos = entity.getEyePosition(mc.getRenderPartialTicks());
            distance *= distance;

            if (mc.objectMouseOver != null)
            {
                distance = mc.objectMouseOver.getHitVec().squareDistanceTo(eyePos);
            }

            Vec3d vecLook = entity.getLook(1.0F);
            Vec3d vec3d2 = eyePos.add(vecLook.x * distance, vecLook.y * distance, vecLook.z * distance);
            AxisAlignedBB axisalignedbb = entity.getBoundingBox().expand(vecLook.scale(distance)).expand(1.0D, 1.0D, 1.0D);
            EntityRayTraceResult result = ProjectileHelper.rayTraceEntities(entity, eyePos, vec3d2, axisalignedbb, entityFilter -> !entityFilter.isSpectator() && entityFilter.canBeCollidedWith(), distance);

            if (result != null)
            {
                Entity pointedEntity = result.getEntity();
                Vec3d hitVec = result.getHitVec();
                double pointedDist = eyePos.squareDistanceTo(hitVec);

                if (pointedDist < distance || mc.objectMouseOver == null)
                {
                    mc.objectMouseOver = result;

                    if (pointedEntity instanceof LivingEntity)
                    {
                        this.extendedPointedEntity = pointedEntity;
                    }
                }
            }
        }
    }
}