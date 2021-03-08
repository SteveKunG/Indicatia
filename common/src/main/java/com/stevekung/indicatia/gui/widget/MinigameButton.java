package com.stevekung.indicatia.gui.widget;

import java.util.Collections;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.stevekung.stevekungslib.utils.TextComponentUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class MinigameButton extends Button
{
    private static final ResourceLocation BLANK = new ResourceLocation("indicatia:textures/gui/blank.png");
    private static final ResourceLocation MAIN = new ResourceLocation("indicatia:textures/gui/main_lobby.png");
    private static final ResourceLocation PLAY = new ResourceLocation("indicatia:textures/gui/play_icon.png");
    private final Minecraft mc;
    private final boolean isPlay;
    private final ITextComponent tooltips;
    private final ItemStack head;

    public MinigameButton(int parentWidth, ITextComponent tooltips, boolean isPlay, Button.IPressable pressable, ItemStack head)
    {
        super(parentWidth - 130, 20, 20, 20, TextComponentUtils.component("Minigame Button"), pressable);
        this.isPlay = isPlay;
        this.tooltips = tooltips;
        this.mc = Minecraft.getInstance();
        this.head = head;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            this.mc.getTextureManager().bindTexture(this.head.isEmpty() ? this.isPlay ? PLAY : MAIN : BLANK);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            AbstractGui.blit(matrixStack, this.x, this.y, flag ? 20 : 0, 0, this.width, this.height, 40, 20);

            if (!this.head.isEmpty())
            {
                RenderSystem.enableDepthTest();
                RenderSystem.enableRescaleNormal();
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(770, 771, 1, 0);
                RenderHelper.enableStandardItemLighting();
                RenderSystem.enableLighting();
                this.mc.getItemRenderer().renderItemAndEffectIntoGUI(this.head, this.x + 2, this.y + 2);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void render(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        if (this.visible && this.isMouseOver(mouseX, mouseY))
        {
            GuiUtils.drawHoveringText(matrixStack, Collections.singletonList(this.tooltips), mouseX, mouseY, this.mc.currentScreen.width, this.mc.currentScreen.height, 128, this.mc.fontRenderer);
            RenderSystem.disableLighting();
        }
    }
}