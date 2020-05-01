package com.stevekung.indicatia.extra;

import com.mojang.blaze3d.platform.GlStateManager;
import com.stevekung.stevekungslib.utils.JsonUtils;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;

public class WeatherRegionScreen extends Screen
{
    public WeatherRegionScreen()
    {
        super(JsonUtils.create("Weather"));
        WeatherUtils.loadTexture();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        int scale = 250;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepthTest();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        WeatherUtils.bindWeatherTexture();
        DrawableHelper.blit(this.minecraft.getWindow().getScaledWidth() / 2 - 124, this.minecraft.getWindow().getScaledHeight() / 2 - 124, 0, 0, scale, scale, scale, scale);
        GlStateManager.disableBlend();
        GlStateManager.enableDepthTest();
        GlStateManager.popMatrix();
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}