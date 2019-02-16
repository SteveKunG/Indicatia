package stevekung.mods.indicatia.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.*;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.event.IndicatiaEventHandler;
import stevekung.mods.stevekungslib.utils.ColorUtils;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoUtils
{
    public static final InfoUtils INSTANCE = new InfoUtils();
    public Entity extendedPointedEntity;

    private InfoUtils() {}

    public int getPing()
    {
        NetworkPlayerInfo info = Minecraft.getInstance().getConnection().getPlayerInfo(Minecraft.getInstance().player.getUniqueID());

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

    public boolean isHypixel()
    {
        ServerData server = Minecraft.getInstance().getCurrentServerData();

        if (server != null)
        {
            Pattern pattern = Pattern.compile("^(?:(?:(?:.*\\.)?hypixel\\.net)|(?:209\\.222\\.115\\.\\d{1,3}))(?::\\d{1,5})?$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(server.serverIP);
            return matcher.find();
        }
        return false;
    }

    public int getCPS()
    {
        IndicatiaEventHandler.LEFT_CLICK.removeIf(cps -> cps < System.currentTimeMillis() - 1000L);
        return IndicatiaEventHandler.LEFT_CLICK.size();
    }

    public int getRCPS()
    {
        IndicatiaEventHandler.RIGHT_CLICK.removeIf(rcps -> rcps < System.currentTimeMillis() - 1000L);
        return IndicatiaEventHandler.RIGHT_CLICK.size();
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
        return ColorUtils.stringToRGB(ExtendedConfig.gameTimeColor).toColoredFont() + "Game: " + ColorUtils.stringToRGB(ExtendedConfig.gameTimeValueColor).toColoredFont() + shours + ":" + sminutes + " " + ampm;
    }

    public String getMoonPhase(Minecraft mc)
    {
        int[] moonPhaseFactors = { 4, 3, 2, 1, 0, -1, -2, -3 };
        int phase = moonPhaseFactors[mc.world.dimension.getMoonPhase(mc.world.getDayTime())];
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
        return ColorUtils.stringToRGB(ExtendedConfig.moonPhaseColor).toColoredFont() + "Moon Phase: " + ColorUtils.stringToRGB(ExtendedConfig.moonPhaseValueColor).toColoredFont() + status;
    }

    public boolean isSlimeChunk(BlockPos pos)
    {
        int x = MathHelper.intFloorDiv(pos.getX(), 16);
        int z = MathHelper.intFloorDiv(pos.getZ(), 16);
        Random rnd = new Random(ExtendedConfig.slimeChunkSeed + x * x * 4987142 + x * 5947611 + z * z * 4392871L + z * 389711 ^ 987234911L);
        return rnd.nextInt(10) == 0;
    }

    public void processMouseOverEntity(Minecraft mc)
    {
        Entity entity = mc.getRenderViewEntity();
        Entity pointedEntity;
        double distance = 12.0D;

        if (entity != null && mc.world != null)
        {
            this.extendedPointedEntity = null;
            mc.objectMouseOver = entity.rayTrace(distance, mc.getRenderPartialTicks(), RayTraceFluidMode.NEVER);
            Vec3d vec3d = entity.getEyePosition(mc.getRenderPartialTicks());
            boolean flag = false;
            double d1 = distance;

            if (!mc.playerController.extendedReach())
            {
                flag = true;
            }

            if (mc.objectMouseOver != null)
            {
                d1 = mc.objectMouseOver.hitVec.distanceTo(vec3d);
            }

            Vec3d vec3d1 = entity.getLook(1.0F);
            Vec3d vec3d2 = vec3d.add(vec3d1.x * distance, vec3d1.y * distance, vec3d1.z * distance);
            pointedEntity = null;
            Vec3d vec3d3 = null;
            List<Entity> list = mc.world.getEntitiesInAABBexcluding(entity, entity.getBoundingBox().expand(vec3d1.x * distance, vec3d1.y * distance, vec3d1.z * distance).grow(1.0D, 1.0D, 1.0D), EntitySelectors.NOT_SPECTATING.and(Entity::canBeCollidedWith));
            double d2 = d1;

            for (Entity entity1 : list)
            {
                AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow(entity1.getCollisionBorderSize());
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

                if (axisalignedbb.contains(vec3d))
                {
                    if (d2 >= 0.0D)
                    {
                        pointedEntity = entity1;
                        vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
                        d2 = 0.0D;
                    }
                }
                else if (raytraceresult != null)
                {
                    double d3 = vec3d.distanceTo(raytraceresult.hitVec);

                    if (d3 < d2 || d2 == 0.0D)
                    {
                        if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity() && !entity1.canRiderInteract())
                        {
                            if (d2 == 0.0D)
                            {
                                pointedEntity = entity1;
                                vec3d3 = raytraceresult.hitVec;
                            }
                        }
                        else
                        {
                            pointedEntity = entity1;
                            vec3d3 = raytraceresult.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }
            if (pointedEntity != null && flag && vec3d.distanceTo(vec3d3) > distance)
            {
                pointedEntity = null;
                mc.objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, null, new BlockPos(vec3d3));
            }
            if (pointedEntity != null && (d2 < d1 || mc.objectMouseOver == null))
            {
                mc.objectMouseOver = new RayTraceResult(pointedEntity, vec3d3);

                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame)
                {
                    this.extendedPointedEntity = pointedEntity;
                }
            }
        }
    }
}