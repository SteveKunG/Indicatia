package stevekung.mods.indicatia.util;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.handler.CommonHandler;

public class InfoUtil
{
    public static final InfoUtil INSTANCE = new InfoUtil();
    public Entity extendedPointedEntity;
    private Entity pointedEntity;

    public int getPing()
    {
        NetworkPlayerInfo info = IndicatiaMod.MC.getNetHandler().getPlayerInfo(GameProfileUtil.getUUID());

        if (info != null)
        {
            if (info.getResponseTime() > 0)
            {
                return info.getResponseTime();
            }
            else
            {
                return CommonHandler.currentServerPing;
            }
        }
        return 0;
    }

    public boolean isHypixel()
    {
        return IndicatiaMod.MC.getCurrentServerData() != null && (IndicatiaMod.MC.getCurrentServerData().serverIP.toLowerCase().contains("hypixel") || IndicatiaMod.MC.getCurrentServerData().serverIP.contains("209.222.115.42"));
    }

    public int getCPS()
    {
        Iterator<Long> iterator = CommonHandler.LEFT_CLICK.iterator();

        while (iterator.hasNext())
        {
            if (iterator.next().longValue() < System.currentTimeMillis() - 1000L)
            {
                iterator.remove();
            }
        }
        return CommonHandler.LEFT_CLICK.size();
    }

    public int getRCPS()
    {
        Iterator<Long> iterator = CommonHandler.RIGHT_CLICK.iterator();

        while (iterator.hasNext())
        {
            if (iterator.next().longValue() < System.currentTimeMillis() - 1000L)
            {
                iterator.remove();
            }
        }
        return CommonHandler.RIGHT_CLICK.size();
    }

    public void setOverlayMessage(String message, boolean isPlaying)
    {
        IndicatiaMod.MC.ingameGUI.setRecordPlaying(message, isPlaying);
    }

    public void setOverlayMessage(IChatComponent component, boolean isPlaying)
    {
        IndicatiaMod.MC.ingameGUI.setRecordPlaying(component, isPlaying);
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
        return "Game: " + shours + ":" + sminutes + " " + ampm;
    }

    public String getMoonPhase(Minecraft mc)
    {
        int[] moonPhaseFactors = { 4, 3, 2, 1, 0, -1, -2, -3 };
        int phase = moonPhaseFactors[mc.theWorld.provider.getMoonPhase(mc.theWorld.getWorldTime())];
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
        return "Moon Phase: " + status;
    }

    public boolean isSlimeChunk(BlockPos pos)
    {
        int x = MathHelper.bucketInt(pos.getX(), 16);
        int z = MathHelper.bucketInt(pos.getZ(), 16);
        Random rnd = new Random(ExtendedConfig.SLIME_CHUNK_SEED + x * x * 4987142 + x * 5947611 + z * z * 4392871L + z * 389711 ^ 987234911L);
        return rnd.nextInt(10) == 0;
    }

    public int getAlternatePotionHUDTextColor(Potion potion)
    {
        int color = 0;

        if (potion == Potion.absorption)
        {
            color = RenderUtil.rgbToDecimal(247, 219, 21);
        }
        else if (potion == Potion.regeneration)
        {
            color = RenderUtil.rgbToDecimal(244, 120, 226);
        }
        else if (potion == Potion.damageBoost)
        {
            color = RenderUtil.rgbToDecimal(179, 55, 55);
        }
        else if (potion == Potion.moveSpeed)
        {
            color = RenderUtil.rgbToDecimal(120, 201, 224);
        }
        else if (potion == Potion.fireResistance)
        {
            color = RenderUtil.rgbToDecimal(233, 157, 73);
        }
        else if (potion == Potion.resistance)
        {
            color = RenderUtil.rgbToDecimal(137, 140, 154);
        }
        else if (potion == Potion.jump)
        {
            color = RenderUtil.rgbToDecimal(33, 251, 75);
        }
        else if (potion == Potion.nightVision)
        {
            color = RenderUtil.rgbToDecimal(97, 97, 224);
        }
        else if (potion == Potion.waterBreathing)
        {
            color = RenderUtil.rgbToDecimal(79, 122, 202);
        }
        else if (potion == Potion.moveSlowdown)
        {
            color = RenderUtil.rgbToDecimal(103, 123, 146);
        }
        else if (potion == Potion.digSpeed)
        {
            color = RenderUtil.rgbToDecimal(182, 169, 80);
        }
        else if (potion == Potion.digSlowdown)
        {
            color = RenderUtil.rgbToDecimal(90, 81, 29);
        }
        else if (potion == Potion.confusion)
        {
            color = RenderUtil.rgbToDecimal(125, 43, 108);
        }
        else if (potion == Potion.invisibility)
        {
            color = RenderUtil.rgbToDecimal(139, 142, 156);
        }
        else if (potion == Potion.blindness)
        {
            color = RenderUtil.rgbToDecimal(90, 90, 90);
        }
        else if (potion == Potion.hunger)
        {
            color = RenderUtil.rgbToDecimal(99, 133, 92);
        }
        else if (potion == Potion.weakness)
        {
            color = RenderUtil.rgbToDecimal(102, 108, 102);
        }
        else if (potion == Potion.poison)
        {
            color = RenderUtil.rgbToDecimal(81, 152, 50);
        }
        else if (potion == Potion.wither)
        {
            color = RenderUtil.rgbToDecimal(105, 84, 80);
        }
        else if (potion == Potion.healthBoost)
        {
            color = RenderUtil.rgbToDecimal(245, 124, 35);
        }
        return color;
    }

    public int parseInt(String input, String type)
    {
        JsonUtil json = new JsonUtil();

        try
        {
            return Integer.parseInt(input);
        }
        catch (NumberFormatException e)
        {
            IndicatiaMod.MC.thePlayer.addChatMessage(json.text(LangUtil.translate("commands.generic.num.invalid", input) + " in " + type + " setting").setChatStyle(json.red()));
            return 0;
        }
    }

    public void processMouseOverEntity(Minecraft mc, float partialTicks)
    {
        Entity entity = mc.getRenderViewEntity();
        double distance = 12.0D;

        if (entity != null)
        {
            if (mc.theWorld != null)
            {
                mc.mcProfiler.startSection("pick");
                this.extendedPointedEntity = null;
                mc.objectMouseOver = entity.rayTrace(distance, partialTicks);
                Vec3 vec3d = entity.getPositionEyes(partialTicks);
                boolean flag = false;
                double d1 = distance;

                if (mc.playerController.extendedReach())
                {
                    d1 = distance;
                    distance = d1;
                }
                else
                {
                    if (distance > distance)
                    {
                        flag = true;
                    }
                }

                if (mc.objectMouseOver != null)
                {
                    d1 = mc.objectMouseOver.hitVec.distanceTo(vec3d);
                }

                Vec3 vec3d1 = entity.getLook(1.0F);
                Vec3 vec3d2 = vec3d.addVector(vec3d1.xCoord * distance, vec3d1.yCoord * distance, vec3d1.zCoord * distance);
                this.pointedEntity = null;
                Vec3 vec3d3 = null;
                List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(vec3d1.xCoord * distance, vec3d1.yCoord * distance, vec3d1.zCoord * distance).expand(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, (Predicate<Entity>) entry -> entry != null && entry.canBeCollidedWith()));
                double d2 = d1;

                for (int j = 0; j < list.size(); ++j)
                {
                    Entity entity1 = list.get(j);
                    float size = entity1.getCollisionBorderSize();
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(size, size, size);
                    MovingObjectPosition raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

                    if (axisalignedbb.isVecInside(vec3d))
                    {
                        if (d2 >= 0.0D)
                        {
                            this.pointedEntity = entity1;
                            vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
                            d2 = 0.0D;
                        }
                    }
                    else if (raytraceresult != null)
                    {
                        double d3 = vec3d.distanceTo(raytraceresult.hitVec);

                        if (d3 < d2 || d2 == 0.0D)
                        {
                            if (entity1 == entity.ridingEntity && !entity.canRiderInteract())
                            {
                                if (d2 == 0.0D)
                                {
                                    this.pointedEntity = entity1;
                                    vec3d3 = raytraceresult.hitVec;
                                }
                            }
                            else
                            {
                                this.pointedEntity = entity1;
                                vec3d3 = raytraceresult.hitVec;
                                d2 = d3;
                            }
                        }
                    }
                }
                if (this.pointedEntity != null && flag && vec3d.distanceTo(vec3d3) > distance)
                {
                    this.pointedEntity = null;
                    mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec3d3, (EnumFacing)null, new BlockPos(vec3d3));
                }
                if (this.pointedEntity != null && (d2 < d1 || mc.objectMouseOver == null))
                {
                    mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec3d3);

                    if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame)
                    {
                        this.extendedPointedEntity = this.pointedEntity;
                    }
                }
                mc.mcProfiler.endSection();
            }
        }
    }
}