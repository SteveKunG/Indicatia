package com.stevekung.indicatia.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MojangStatusButton extends Button
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("indicatia:textures/gui/mojang.png");

    public MojangStatusButton(int xPos, int yPos, Button.IPressable button)
    {
        super(xPos, yPos, 20, 20, "", button);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        Minecraft mc = Minecraft.getInstance();

        if (mc.currentScreen instanceof MainMenuScreen)
        {
            MainMenuScreen main = (MainMenuScreen)mc.currentScreen;
            float f = main.showFadeInAnimation ? (Util.milliTime() - main.firstRenderTime) / 1000.0F : 1.0F;
            float f1 = main.showFadeInAnimation ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, f1);
            this.setAlpha(f1);

            mc.getTextureManager().bindTexture(MojangStatusButton.TEXTURE);
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            AbstractGui.blit(this.x, this.y, flag ? 20 : 0, 0, this.width, this.height, 40, 20);
        }
    }
}