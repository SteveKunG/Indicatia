package com.stevekung.indicatia.gui.widget;

import java.util.Collections;

import com.mojang.blaze3d.systems.RenderSystem;
import com.stevekung.stevekungslib.utils.client.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class MinigameButton extends Button
{
    private static final ResourceLocation MAIN = new ResourceLocation("indicatia:textures/gui/main_lobby.png");
    private static final ResourceLocation PLAY = new ResourceLocation("indicatia:textures/gui/play_icon.png");
    private final Minecraft mc;
    private final boolean isPlay;
    private final String tooltips;

    public MinigameButton(int parentWidth, String tooltips, boolean isPlay, Button.IPressable pressable)
    {
        super(parentWidth - 130, 20, 20, 20, "Minigame Button", pressable);
        this.isPlay = isPlay;
        this.tooltips = tooltips;
        this.mc = Minecraft.getInstance();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if (!this.visible)
        {
            return;
        }
        RenderUtils.bindTexture(this.isPlay ? MinigameButton.PLAY : MinigameButton.MAIN);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        AbstractGui.blit(this.x, this.y, flag ? 20 : 0, 0, this.width, this.height, 40, 20);
    }

    public void render(int mouseX, int mouseY)
    {
        if (this.visible && this.isMouseOver(mouseX, mouseY))
        {
            GuiUtils.drawHoveringText(Collections.singletonList(this.tooltips), mouseX, mouseY, this.mc.currentScreen.width, this.mc.currentScreen.height, 128, this.mc.fontRenderer);
            RenderSystem.disableLighting();
        }
    }
}