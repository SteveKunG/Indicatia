package com.stevekung.indicatia.gui.components;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.stevekunglib.utils.ColorUtils;
import com.stevekung.stevekunglib.utils.TextComponentUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class DropdownMinigamesButton extends Button
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("indicatia:textures/gui/dropdown.png");
    public boolean dropdownClicked;
    private int selectedMinigame = -1;
    private final List<String> minigameLists;
    private final IDropboxCallback parentClass;
    private final int displayLength;

    public DropdownMinigamesButton(IDropboxCallback parentClass, int x, int y, List<String> minigameLists)
    {
        super(x, y, 15, 15, TextComponentUtils.component("Minigame Dropdown Button"), null);
        this.parentClass = parentClass;
        this.minigameLists = minigameLists;
        this.displayLength = Math.min(this.minigameLists.size(), 6);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        var mc = Minecraft.getInstance();
        var hoverColor = 150;
        var hoverPos = (mouseY - this.y) / this.height;
        this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        if (!this.dropdownClicked && this.isHovered)
        {
            hoverColor = 180;
        }

        if (this.selectedMinigame == -1)
        {
            var initSelect = this.parentClass.getInitialSelection(this);
            var size = this.minigameLists.size() + IndicatiaSettings.INSTANCE.hypixelMinigameScrollPos;

            if (initSelect > size || IndicatiaSettings.INSTANCE.selectedHypixelMinigame > size || size == 1)
            {
                initSelect = 0;
                IndicatiaSettings.INSTANCE.hypixelMinigameScrollPos = 0;
                IndicatiaSettings.INSTANCE.selectedHypixelMinigame = 0;
            }
            this.selectedMinigame = initSelect;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        GuiComponent.fill(poseStack, this.x, this.y, this.x + this.width - 15, this.y + (this.dropdownClicked ? this.height * this.displayLength + 15 : this.height), ColorUtils.to32Bit(0, 0, 0, 255));
        GuiComponent.fill(poseStack, this.x + 1, this.y + 1, this.x + this.width - 16, this.y + (this.dropdownClicked ? this.height * this.displayLength + 15 : this.height) - 1, ColorUtils.to32Bit(hoverColor, hoverColor, hoverColor, 255));

        if (this.dropdownClicked)
        {
            GuiComponent.fill(poseStack, this.x + 1, this.y + 1, this.x + this.width - 16, this.y - 1 + this.height, ColorUtils.to32Bit(120, 120, 120, 255));
        }

        GuiComponent.fill(poseStack, this.x + this.width - 15, this.y, this.x + this.width - 1, this.y + this.height, ColorUtils.to32Bit(0, 0, 0, 255));
        GuiComponent.fill(poseStack, this.x + this.width - 15, this.y + 1, this.x + this.width - 2, this.y + this.height - 1, ColorUtils.to32Bit(150, 150, 150, 255));

        if (this.displayLength > 1 && this.dropdownClicked)
        {
            if (this.isHoverDropdown(mouseX, mouseY))
            {
                GuiComponent.fill(poseStack, this.x + 1, this.y + 2 + this.height * hoverPos - 1, this.x + this.width - 16, this.y + this.height * (hoverPos + 1) - 1, ColorUtils.to32Bit(180, 180, 180, 255));
            }
            if (mouseX >= this.x && mouseY >= this.y + 16 && mouseX < this.x + this.width - 16 && mouseY < this.y + this.height * this.displayLength + 15)
            {
                GuiComponent.fill(poseStack, this.x + 1, this.y + this.height * hoverPos - 1, this.x + this.width - 16, this.y + this.height * (hoverPos + 1) - 2, ColorUtils.to32Bit(180, 180, 180, 255));
            }
        }

        for (var i = 0; i + IndicatiaSettings.INSTANCE.hypixelMinigameScrollPos < this.minigameLists.size() && i < this.displayLength; ++i)
        {
            var minigames = this.minigameLists.get(i + IndicatiaSettings.INSTANCE.hypixelMinigameScrollPos);

            if (minigames != null)
            {
                if (this.dropdownClicked)
                {
                    mc.font.drawShadow(poseStack, minigames, this.x + this.width / 2F - 7 - mc.font.width(minigames) / 2F, this.y + (this.height + 22) / 2F + this.height * i, ColorUtils.to32Bit(255, 255, 255, 255));
                }
                mc.font.drawShadow(poseStack, this.minigameLists.get(this.selectedMinigame), this.x + this.width / 2F - 7 - mc.font.width(this.minigameLists.get(this.selectedMinigame)) / 2F, this.y + (this.height - 6) / 2F, ColorUtils.to32Bit(255, 255, 255, 255));
            }
        }
        RenderSystem.setShaderTexture(0, DropdownMinigamesButton.TEXTURE);
        GuiComponent.blit(poseStack, this.x + this.width - 12, this.y + 5, 0, 0, 7, 4, 7, 4);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseEvent)
    {
        if (this.displayLength == 1)
        {
            return false;
        }
        if (!this.dropdownClicked)
        {
            if (this.isHovered)
            {
                this.dropdownClicked = true;
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                return true;
            }
        }
        else
        {
            if (mouseX >= this.x && mouseY >= this.y + 15 && mouseX < this.x + this.width - 16 && mouseY < this.y + 15 + this.height * this.displayLength)
            {
                var optionClicked = (mouseY - this.y - 16) / this.height + IndicatiaSettings.INSTANCE.hypixelMinigameScrollPos;
                this.selectedMinigame = (int) optionClicked % this.minigameLists.size();
                this.dropdownClicked = false;
                this.parentClass.onSelectionChanged(this, this.selectedMinigame);
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                return true;
            }
            else
            {
                this.dropdownClicked = false;
                return false;
            }
        }
        return false;
    }

    public void scroll(double amount)
    {
        IndicatiaSettings.INSTANCE.hypixelMinigameScrollPos -= amount;
        var i = this.minigameLists.size();

        if (IndicatiaSettings.INSTANCE.hypixelMinigameScrollPos > i - this.displayLength)
        {
            IndicatiaSettings.INSTANCE.hypixelMinigameScrollPos = i - this.displayLength;
        }
        if (IndicatiaSettings.INSTANCE.hypixelMinigameScrollPos <= 0)
        {
            IndicatiaSettings.INSTANCE.hypixelMinigameScrollPos = 0;
        }
    }

    public boolean isHoverDropdown(double mouseX, double mouseY)
    {
        return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width - 16 && mouseY < this.y + this.height * this.displayLength + 15;
    }

    public interface IDropboxCallback
    {
        void onSelectionChanged(DropdownMinigamesButton dropdown, int selection);
        int getInitialSelection(DropdownMinigamesButton dropdown);
    }
}