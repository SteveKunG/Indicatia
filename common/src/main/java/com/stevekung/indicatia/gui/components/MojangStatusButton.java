package com.stevekung.indicatia.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.mixin.InvokerTitleScreen;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
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
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if (this.mc.screen instanceof TitleScreen)
        {
            TitleScreen main = (TitleScreen) this.mc.screen;
            float f = ((InvokerTitleScreen) main).getFading() ? (Util.getMillis() - ((InvokerTitleScreen) main).getFadeInStart()) / 1000.0F : 1.0F;
            float f1 = ((InvokerTitleScreen) main).getFading() ? Mth.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, f1);
            this.setAlpha(f1);

            this.mc.getTextureManager().bind(MojangStatusButton.TEXTURE);
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GuiComponent.blit(matrixStack, this.x, this.y, flag ? 20 : 0, 0, this.width, this.height, 40, 20);
        }
    }
}