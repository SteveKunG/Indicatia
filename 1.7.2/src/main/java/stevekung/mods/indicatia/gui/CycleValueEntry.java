package stevekung.mods.indicatia.gui;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import stevekung.mods.indicatia.utils.LangUtil;

public class CycleValueEntry extends GuiConfigEntries.ButtonEntry
{
    private int beforeIndex;
    private int defaultIndex;
    protected int currentIndex;

    public CycleValueEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
    {
        super(owningScreen, owningEntryList, configElement);
        this.beforeIndex = this.getIndex(configElement.get().toString());
        this.defaultIndex = this.getIndex(configElement.getDefault().toString());
        this.currentIndex = this.beforeIndex;
        this.btnValue.enabled = this.enabled();
        this.updateValueButtonText();
    }

    @Override
    public void updateValueButtonText()
    {
        this.btnValue.displayString = LangUtil.translate(this.configElement.getValidValues()[this.currentIndex]);
    }

    @Override
    public void valueButtonPressed(int slotIndex)
    {
        if (this.enabled())
        {
            if (++this.currentIndex >= this.configElement.getValidValues().length)
            {
                this.currentIndex = 0;
            }
            this.updateValueButtonText();
        }
    }

    @Override
    public boolean isDefault()
    {
        return this.currentIndex == this.defaultIndex;
    }

    @Override
    public void setToDefault()
    {
        if (this.enabled())
        {
            this.currentIndex = this.defaultIndex;
            this.updateValueButtonText();
        }
    }

    @Override
    public boolean isChanged()
    {
        return this.currentIndex != this.beforeIndex;
    }

    @Override
    public void undoChanges()
    {
        if (this.enabled())
        {
            this.currentIndex = this.beforeIndex;
            this.updateValueButtonText();
        }
    }

    @Override
    public boolean saveConfigElement()
    {
        if (this.enabled() && this.isChanged())
        {
            this.configElement.set(this.configElement.getValidValues()[this.currentIndex]);
            return this.configElement.requiresMcRestart();
        }
        return false;
    }

    @Override
    public String getCurrentValue()
    {
        return this.configElement.getValidValues()[this.currentIndex];
    }

    @Override
    public String[] getCurrentValues()
    {
        return new String[] { this.getCurrentValue() };
    }

    private int getIndex(String string)
    {
        for (int i = 0; i < this.configElement.getValidValues().length; i++)
        {
            if (this.configElement.getValidValues()[i].equalsIgnoreCase(string))
            {
                return i;
            }
        }
        return 0;
    }
}