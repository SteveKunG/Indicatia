package com.stevekung.indicatia.utils;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;

public class RenderUtilsIN
{
    public static void drawRect(int left, int top, int right, int bottom, int color, float alpha)
    {
        if (alpha > 0.1F)
        {
            if (left < right)
            {
                int i = left;
                left = right;
                right = i;
            }
            if (top < bottom)
            {
                int j = top;
                top = bottom;
                bottom = j;
            }
            float r = (color >> 16 & 255) / 255.0F;
            float g = (color >> 8 & 255) / 255.0F;
            float b = (color & 255) / 255.0F;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder vertexbuffer = tessellator.getBuffer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture();
            GlStateManager.blendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color4f(r, g, b, alpha);
            vertexbuffer.begin(7, VertexFormats.POSITION);
            vertexbuffer.vertex(left, bottom, 0.0D).next();
            vertexbuffer.vertex(right, bottom, 0.0D).next();
            vertexbuffer.vertex(right, top, 0.0D).next();
            vertexbuffer.vertex(left, top, 0.0D).next();
            tessellator.draw();
            GlStateManager.enableTexture();
            GlStateManager.disableBlend();
        }
    }
}