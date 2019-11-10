package com.stevekung.indicatia.gui.widget;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MinigameButton extends Button
{
    private final Minecraft mc;
    private static final ResourceLocation MAIN = new ResourceLocation("indicatia:textures/gui/main_lobby.png");
    private static final ResourceLocation PLAY = new ResourceLocation("indicatia:textures/gui/play_icon.png");
    private final boolean isPlay;
    private final int parentWidth;
    private final String tooltips;
    public final String command;

    public MinigameButton(int parentWidth, String tooltips, String command, boolean isPlay)
    {
        super(parentWidth - 130, 20, 20, 20, "", null);
        this.isPlay = isPlay;
        this.parentWidth = parentWidth;
        this.tooltips = tooltips;
        this.command = command.startsWith("/") ? command : isPlay ? "/play " + command : "/lobby " + command;
        this.mc = Minecraft.getInstance();
    }

    @Override
    public void onClick(double mouseX, double mouseZ)
    {
        this.mc.player.sendChatMessage(this.command);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            this.mc.getTextureManager().bindTexture(this.isPlay ? MinigameButton.PLAY : MinigameButton.MAIN);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            AbstractGui.blit(this.x, this.y, flag ? 20 : 0, 0, this.width, this.height, 40, 20);
        }
    }

    public void render(int mouseX, int mouseY)
    {
        if (this.visible)
        {
            boolean isHover = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GlStateManager.disableDepthTest();

            if (isHover)
            {
                int k = this.mc.fontRenderer.getStringWidth(this.tooltips);
                int i1 = mouseX + 12;
                int j1 = mouseY - 12;
                int k1 = 8;
                int l1 = -267386864;
                int i2 = 1347420415;
                int i3 = i2 & 16711422;
                int i4 = i2 & -16777216;
                int j2 = i3 >> 1 | i4;

            if (i1 + k > this.parentWidth)
            {
                i1 -= 28 + k;
            }

            this.blitOffset = 300;
            this.fillGradient(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
            this.fillGradient(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
            this.fillGradient(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
            this.fillGradient(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
            this.fillGradient(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
            this.fillGradient(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
            this.fillGradient(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
            this.fillGradient(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
            this.fillGradient(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);
            this.mc.fontRenderer.drawStringWithShadow(this.tooltips, i1, j1, -1);
            this.blitOffset = 0;
            GlStateManager.enableDepthTest();
            }
        }
    }
}