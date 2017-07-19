package stevekung.mods.indicatia.gui;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiUtils;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.renderer.Tessellator;
import stevekung.mods.indicatia.utils.InfoUtil;

public class ConfigColorEntry extends CycleValueEntry
{
    public ConfigColorEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
    {
        super(owningScreen, owningEntryList, configElement);
        this.btnValue.enabled = this.enabled();
        this.updateValueButtonText();
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
    {
        int color = GuiUtils.getColorCode(InfoUtil.INSTANCE.getColorCode()[this.currentIndex].charAt(0), true);

        if (color == 0)
        {
            color = 3618615;
        }
        this.btnValue.packedFGColour = color;
        super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
    }

    @Override
    public void updateValueButtonText()
    {
        this.btnValue.displayString = InfoUtil.INSTANCE.getJsonColor()[this.currentIndex];
    }
}