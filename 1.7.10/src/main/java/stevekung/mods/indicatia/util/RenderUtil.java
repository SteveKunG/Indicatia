package stevekung.mods.indicatia.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import stevekung.mods.indicatia.core.IndicatiaMod;

public class RenderUtil
{
    public static void renderEntityHealth(EntityLivingBase entityLivingBase, String text, double x, double y, double z)
    {
        Minecraft mc = IndicatiaMod.MC;
        double distance = entityLivingBase.getDistanceSqToEntity(mc.renderViewEntity);
        int maxDistance = 64;
        FontRenderer fontrenderer = IndicatiaMod.MC.fontRenderer;

        if (distance <= maxDistance * maxDistance)
        {
            if (entityLivingBase.isSneaking())
            {
                float f = 1.6F;
                float f1 = 0.016666668F * f;
                GL11.glPushMatrix();
                GL11.glTranslatef((float)x + 0.0F, (float)y + entityLivingBase.height + 1.0F, (float)z);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef((RenderManager.instance.options.thirdPersonView == 2 ? -1 : 1) * RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
                GL11.glScalef(-f1, -f1, f1);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
                GL11.glDepthMask(false);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                Tessellator tessellator = Tessellator.instance;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                tessellator.startDrawingQuads();
                int i = fontrenderer.getStringWidth(text) / 2;
                tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                tessellator.addVertex(-i - 1, -1.0D, 0.0D);
                tessellator.addVertex(-i - 1, 8.0D, 0.0D);
                tessellator.addVertex(i + 1, 8.0D, 0.0D);
                tessellator.addVertex(i + 1, -1.0D, 0.0D);
                tessellator.draw();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(true);
                fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2, 0, 553648127);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
            else
            {
                float f = 1.6F;
                float f1 = 0.016666668F * f;
                GL11.glPushMatrix();
                GL11.glTranslatef((float)x, (float) (y + entityLivingBase.height + 1.0F), (float)z);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef((RenderManager.instance.options.thirdPersonView == 2 ? -1 : 1) * RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
                GL11.glScalef(-f1, -f1, f1);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                Tessellator tessellator = Tessellator.instance;
                byte b0 = 0;

                if (text.equals("deadmau5"))
                {
                    b0 = -10;
                }

                GL11.glDisable(GL11.GL_TEXTURE_2D);
                tessellator.startDrawingQuads();
                int j = fontrenderer.getStringWidth(text) / 2;
                tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                tessellator.addVertex(-j - 1, -1 + b0, 0.0D);
                tessellator.addVertex(-j - 1, 8 + b0, 0.0D);
                tessellator.addVertex(j + 1, 8 + b0, 0.0D);
                tessellator.addVertex(j + 1, -1 + b0, 0.0D);
                tessellator.draw();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2, b0, 553648127);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2, b0, -1);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
        }
    }

    public static void bindKeystrokeTexture(String texture)
    {
        IndicatiaMod.MC.getTextureManager().bindTexture(new ResourceLocation("indicatia:textures/gui/" + texture + ".png"));
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
    }

    public static int to32BitColor(int a, int r, int g, int b)
    {
        a = a << 24;
        r = r << 16;
        g = g << 8;
        return a | r | g | b;
    }

    public static void drawRect(int left, int top, int right, int bottom, int color, float alpha)
    {
        if (alpha > 0.1F)
        {
            int j1;

            if (left < right)
            {
                j1 = left;
                left = right;
                right = j1;
            }
            if (top < bottom)
            {
                j1 = top;
                top = bottom;
                bottom = j1;
            }
            float f = (color >> 16 & 255) / 255.0F;
            float f1 = (color >> 8 & 255) / 255.0F;
            float f2 = (color & 255) / 255.0F;
            Tessellator tessellator = Tessellator.instance;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glColor4f(f, f1, f2, alpha);
            tessellator.startDrawingQuads();
            tessellator.addVertex(left, bottom, 0.0D);
            tessellator.addVertex(right, bottom, 0.0D);
            tessellator.addVertex(right, top, 0.0D);
            tessellator.addVertex(left, top, 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    public static int rgbToDecimal(int r, int g, int b)
    {
        return b + 256 * g + 65536 * r;
    }
}