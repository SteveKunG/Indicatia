package com.stevekung.indicatia.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MojangStatusButton extends Button
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("indicatia:textures/gui/mojang.png");
    private final Minecraft mc;

    public MojangStatusButton(int xPos, int yPos, Button.OnPress button)
    {
        super(xPos, yPos, 20, 20, TextComponentUtils.component("Mojang Status Button"), button);
        this.mc = Minecraft.getInstance();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        if (this.mc.screen instanceof TitleScreen main)
        {
            var f = main.fading ? (Util.getMillis() - main.fadeInStart) / 1000.0F : 1.0F;
            var f1 = main.fading ? Mth.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, MojangStatusButton.TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
            this.setAlpha(f1);

            var flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GuiComponent.blit(poseStack, this.x, this.y, flag ? 20 : 0, 0, this.width, this.height, 40, 20);
        }
    }
}