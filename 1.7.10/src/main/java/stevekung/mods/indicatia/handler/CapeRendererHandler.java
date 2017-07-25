package stevekung.mods.indicatia.handler;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.utils.CapeUtils;
import stevekung.mods.indicatia.utils.GameProfileUtil;

public class CapeRendererHandler
{
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPostRender(RenderPlayerEvent.Specials.Post event)
    {
        EntityPlayer player = event.entityPlayer;
        float yaw;

        if (ConfigManager.enableCustomCape && player.getCommandSenderName().equals(GameProfileUtil.getUsername()) && !player.isInvisible() && ExtendedConfig.SHOW_CAPE && !CapeUtils.CAPE_TEXTURE.isEmpty())
        {
            CapeUtils.bindCapeTexture();
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d3 = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * event.partialRenderTick - (player.prevPosX + (player.posX - player.prevPosX) * event.partialRenderTick);
            double d4 = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * event.partialRenderTick - (player.prevPosY + (player.posY - player.prevPosY) * event.partialRenderTick);
            double d0 = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * event.partialRenderTick - (player.prevPosZ + (player.posZ - player.prevPosZ) * event.partialRenderTick);
            yaw = (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * event.partialRenderTick) / 57.29578F;
            double d1 = MathHelper.sin(yaw);
            double d2 = -MathHelper.cos(yaw);
            float f5 = (float) d4 * 10.0F;

            if (f5 < -6.0F)
            {
                f5 = -6.0F;
            }
            if (f5 > 32.0F)
            {
                f5 = 32.0F;
            }

            float f6 = (float) (d3 * d1 + d0 * d2) * 100.0F;
            float f7 = (float) (d3 * d2 - d0 * d1) * 100.0F;

            if (f6 < 0.0F)
            {
                f6 = 0.0F;
            }

            float f8 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * event.partialRenderTick;
            f5 += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * event.partialRenderTick) * 6.0F) * 32.0F * f8;

            if (player.isSneaking())
            {
                f5 += 25.0F;
                GL11.glTranslatef(0.0F, 0.145F, -0.015F);
            }

            GL11.glRotatef(6.0F + f6 / 2.0F + f5, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f7 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f7 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            event.renderer.modelBipedMain.renderCloak(0.0625F);
            GL11.glPopMatrix();
        }
    }
}