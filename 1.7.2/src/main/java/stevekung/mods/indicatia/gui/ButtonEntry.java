package stevekung.mods.indicatia.gui;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.ListEntryBase;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;

public abstract class ButtonEntry extends ListEntryBase
{
    protected final GuiButtonExt btnValue;

    public ButtonEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
    {
        this(owningScreen, owningEntryList, configElement, new GuiButtonExt(0, owningEntryList.controlX, 0, owningEntryList.controlWidth, 18, configElement.get() != null ? I18n.format(String.valueOf(configElement.get())) : ""));
    }

    public ButtonEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement, GuiButtonExt button)
    {
        super(owningScreen, owningEntryList, configElement);
        this.btnValue = button;
    }

    public abstract void updateValueButtonText();
    public abstract void valueButtonPressed(int slotIndex);

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
    {
        super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
        this.btnValue.width = this.owningEntryList.controlWidth;
        this.btnValue.xPosition = this.owningScreen.entryList.controlX;
        this.btnValue.yPosition = y;
        this.btnValue.enabled = this.enabled();
        this.btnValue.drawButton(this.mc, mouseX, mouseY);
    }

    @Override
    public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
        if (this.btnValue.mousePressed(this.mc, x, y))
        {
            this.btnValue.func_146113_a(this.mc.getSoundHandler());
            this.valueButtonPressed(index);
            this.updateValueButtonText();
            return true;
        }
        else
        {
            return super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
        }
    }

    @Override
    public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY)
    {
        super.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
        this.btnValue.mouseReleased(x, y);
    }

    @Override
    public void keyTyped(char eventChar, int eventKey) {}

    @Override
    public void updateCursorCounter() {}

    @Override
    public void mouseClicked(int x, int y, int mouseEvent) {}
}