package com.stevekung.indicatia.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class MinigameButton extends Button
{
    private static final ResourceLocation BLANK = new ResourceLocation("indicatia:textures/gui/blank.png");
    private static final ResourceLocation MAIN = new ResourceLocation("indicatia:textures/gui/main_lobby.png");
    private static final ResourceLocation PLAY = new ResourceLocation("indicatia:textures/gui/play_icon.png");
    private final Minecraft mc;
    private final boolean isPlay;
    private final Component tooltips;
    private final ItemStack head;

    public MinigameButton(int parentWidth, Component tooltips, boolean isPlay, Button.OnPress pressable, ItemStack head)
    {
        super(parentWidth - 130, 20, 20, 20, TextComponentUtils.component("Minigame Button"), pressable);
        this.isPlay = isPlay;
        this.tooltips = tooltips;
        this.mc = Minecraft.getInstance();
        this.head = head;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, this.head.isEmpty() ? this.isPlay ? PLAY : MAIN : BLANK);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            var flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GuiComponent.blit(poseStack, this.x, this.y, flag ? 20 : 0, 0, this.width, this.height, 40, 20);

            if (!this.head.isEmpty())
            {
                this.mc.getItemRenderer().renderAndDecorateItem(this.head, this.x + 2, this.y + 2);
            }
        }
    }

    public void render(PoseStack poseStack, int mouseX, int mouseY)
    {
        if (this.visible && this.isMouseOver(mouseX, mouseY))
        {
            this.mc.screen.renderTooltip(poseStack, this.tooltips, mouseX, mouseY);
        }
    }
}