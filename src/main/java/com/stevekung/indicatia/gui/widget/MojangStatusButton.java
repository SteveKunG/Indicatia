package com.stevekung.indicatia.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.stevekung.stevekungslib.utils.TextComponentUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public class MojangStatusButton extends Button
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("indicatia:textures/gui/mojang.png");
    private final Minecraft mc;

    public MojangStatusButton(int xPos, int yPos, Button.IPressable button)
    {
        super(xPos, yPos, 20, 20, TextComponentUtils.component("Mojang Status Button"), button);
        this.mc = Minecraft.getInstance();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if (this.mc.currentScreen instanceof MainMenuScreen)
        {
            MainMenuScreen main = (MainMenuScreen)this.mc.currentScreen;
            float f = main.showFadeInAnimation ? (Util.milliTime() - main.firstRenderTime) / 1000.0F : 1.0F;
            float f1 = main.showFadeInAnimation ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, f1);
            this.setAlpha(f1);

            this.mc.getTextureManager().bindTexture(MojangStatusButton.TEXTURE);
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            AbstractGui.blit(matrixStack, this.x, this.y, flag ? 20 : 0, 0, this.width, this.height, 40, 20);
        }
    }
}