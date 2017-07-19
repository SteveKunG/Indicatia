package stevekung.mods.indicatia.gui;

import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;

public class NumberSliderEntry extends ButtonEntry
{
    protected final double beforeValue;

    public NumberSliderEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
    {
        super(owningScreen, owningEntryList, configElement, new GuiSlider(0, owningEntryList.controlX, 0, owningEntryList.controlWidth, 18, "", "", Double.valueOf(configElement.getMinValue().toString()), Double.valueOf(configElement.getMaxValue().toString()), Double.valueOf(configElement.get().toString()), configElement.getType() == ConfigGuiType.DOUBLE, true));

        if (configElement.getType() == ConfigGuiType.INTEGER)
        {
            this.beforeValue = Integer.valueOf(configElement.get().toString());
        }
        else
        {
            this.beforeValue = Double.valueOf(configElement.get().toString());
        }
    }

    @Override
    public void updateValueButtonText()
    {
        ((GuiSlider) this.btnValue).updateSlider();
    }

    @Override
    public void valueButtonPressed(int slotIndex) {}

    @Override
    public boolean isDefault()
    {
        if (this.configElement.getType() == ConfigGuiType.INTEGER)
        {
            return ((GuiSlider) this.btnValue).getValueInt() == Integer.valueOf(this.configElement.getDefault().toString());
        }
        else
        {
            return ((GuiSlider) this.btnValue).getValue() == Double.valueOf(this.configElement.getDefault().toString());
        }
    }

    @Override
    public void setToDefault()
    {
        if (this.enabled())
        {
            ((GuiSlider) this.btnValue).setValue(Double.valueOf(this.configElement.getDefault().toString()));
            ((GuiSlider) this.btnValue).updateSlider();
        }
    }

    @Override
    public boolean isChanged()
    {
        if (this.configElement.getType() == ConfigGuiType.INTEGER)
        {
            return ((GuiSlider) this.btnValue).getValueInt() != (int) Math.round(this.beforeValue);
        }
        else
        {
            return ((GuiSlider) this.btnValue).getValue() != this.beforeValue;
        }
    }

    @Override
    public void undoChanges()
    {
        if (this.enabled())
        {
            ((GuiSlider) this.btnValue).setValue(this.beforeValue);
            ((GuiSlider) this.btnValue).updateSlider();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean saveConfigElement()
    {
        if (this.enabled() && this.isChanged())
        {
            if (this.configElement.getType() == ConfigGuiType.INTEGER)
            {
                this.configElement.set(((GuiSlider) this.btnValue).getValueInt());
            }
            else
            {
                this.configElement.set(((GuiSlider) this.btnValue).getValue());
            }
            return this.configElement.requiresMcRestart();
        }
        return false;
    }

    @Override
    public Object getCurrentValue()
    {
        if (this.configElement.getType() == ConfigGuiType.INTEGER)
        {
            return ((GuiSlider) this.btnValue).getValueInt();
        }
        else
        {
            return ((GuiSlider) this.btnValue).getValue();
        }
    }

    @Override
    public Object[] getCurrentValues()
    {
        return new Object[] { this.getCurrentValue() };
    }
}