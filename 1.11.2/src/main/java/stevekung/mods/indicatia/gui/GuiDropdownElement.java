package stevekung.mods.indicatia.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.util.RenderUtil;

public class GuiDropdownElement extends GuiButton
{
    private static final ResourceLocation texture = new ResourceLocation("indicatia:textures/gui/dropdown.png");
    public boolean dropdownClicked;
    public int selectedOption = -1;
    private final String[] optionStrings;
    private final IDropboxCallback parentClass;

    public GuiDropdownElement(IDropboxCallback parentClass, int x, int y, String... optionList)
    {
        super(0, x, y, 15, 15, "");
        this.parentClass = parentClass;
        this.optionStrings = optionList;
        int largestString = Integer.MIN_VALUE;

        for (String text : optionList)
        {
            largestString = Math.max(largestString, IndicatiaMod.MC.fontRendererObj.getStringWidth(text));
        }
        this.width = largestString + 8 + 15;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        int hoverColor = 150;

        if (!this.dropdownClicked && this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height)
        {
            hoverColor = 180;
        }
        if (this.selectedOption == -1)
        {
            this.selectedOption = this.parentClass.getInitialSelection(this);
        }
        if (this.visible)
        {
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width - 15, this.yPosition + (this.dropdownClicked ? this.height * this.optionStrings.length : this.height), RenderUtil.to32BitColor(255, 0, 0, 0));
            Gui.drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + this.width - 16, this.yPosition + (this.dropdownClicked ? this.height * this.optionStrings.length : this.height) - 1, RenderUtil.to32BitColor(255, hoverColor, hoverColor, hoverColor));
            Gui.drawRect(this.xPosition + this.width - 15, this.yPosition, this.xPosition + this.width - 1, this.yPosition + this.height, RenderUtil.to32BitColor(255, 0, 0, 0));
            Gui.drawRect(this.xPosition + this.width - 15, this.yPosition + 1, this.xPosition + this.width - 2, this.yPosition + this.height - 1, RenderUtil.to32BitColor(255, 150, 150, 150));

            if (this.dropdownClicked && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width - 16 && mouseY < this.yPosition + this.height * this.optionStrings.length)
            {
                int hoverPos = (mouseY - this.yPosition) / this.height;
                Gui.drawRect(this.xPosition + 1, this.yPosition + this.height * hoverPos + 1, this.xPosition + this.width - 16, this.yPosition + this.height * (hoverPos + 1) - 1, RenderUtil.to32BitColor(255, 180, 180, 180));
            }

            if (this.dropdownClicked)
            {
                for (int i = 0; i < this.optionStrings.length; i++)
                {
                    mc.fontRendererObj.drawStringWithShadow(this.optionStrings[i], this.xPosition + this.width / 2 - 7 - mc.fontRendererObj.getStringWidth(this.optionStrings[i]) / 2, this.yPosition + (this.height - 6) / 2 + this.height * i, RenderUtil.to32BitColor(255, 255, 255, 255));
                }
            }
            else
            {
                mc.fontRendererObj.drawStringWithShadow(this.optionStrings[this.selectedOption], this.xPosition + this.width / 2 - 7 - mc.fontRendererObj.getStringWidth(this.optionStrings[this.selectedOption]) / 2, this.yPosition + (this.height - 6) / 2, RenderUtil.to32BitColor(255, 255, 255, 255));
            }
            mc.renderEngine.bindTexture(GuiDropdownElement.texture);
            Gui.drawModalRectWithCustomSizedTexture(this.xPosition + this.width - 12, this.yPosition + 5, 0, 0, 7, 4, 7, 4);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (!this.dropdownClicked)
        {
            if (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height)
            {
                this.dropdownClicked = true;
                return true;
            }
        }
        else
        {
            if (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width - 16 && mouseY < this.yPosition + this.height * this.optionStrings.length)
            {
                int optionClicked = (mouseY - this.yPosition) / this.height;
                this.selectedOption = optionClicked % this.optionStrings.length;
                this.dropdownClicked = false;
                this.parentClass.onSelectionChanged(this, this.selectedOption);
                return true;
            }
            else
            {
                this.dropdownClicked = false;
                return true;
            }
        }
        return false;
    }

    public interface IDropboxCallback
    {
        void onSelectionChanged(GuiDropdownElement dropdown, int selection);

        int getInitialSelection(GuiDropdownElement dropdown);
    }
}