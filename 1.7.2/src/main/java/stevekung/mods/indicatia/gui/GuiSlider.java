package stevekung.mods.indicatia.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.config.GuiButtonExt;
import net.minecraft.client.Minecraft;

public class GuiSlider extends GuiButtonExt
{
    public double sliderValue = 1.0F;
    public String dispString = "";
    public boolean dragging = false;
    public boolean showDecimal = true;
    public double minValue = 0.0D;
    public double maxValue = 5.0D;
    public int precision = 1;
    public ISlider parent = null;
    public String suffix = "";
    public boolean drawString = true;

    public GuiSlider(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr)
    {
        this(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, null);
    }

    public GuiSlider(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, ISlider par)
    {
        super(id, xPos, yPos, width, height, prefix);
        this.minValue = minVal;
        this.maxValue = maxVal;
        this.sliderValue = (currentVal - this.minValue) / (this.maxValue - this.minValue);
        this.dispString = prefix;
        this.parent = par;
        this.suffix = suf;
        this.showDecimal = showDec;
        String val;

        if (this.showDecimal)
        {
            val = Double.toString(this.sliderValue * (this.maxValue - this.minValue) + this.minValue);
            this.precision = Math.min(val.substring(val.indexOf(".") + 1).length(), 4);
        }
        else
        {
            val = Integer.toString((int)Math.round(this.sliderValue * (this.maxValue - this.minValue) + this.minValue));
            this.precision = 0;
        }

        this.displayString = this.dispString + val + this.suffix;
        this.drawString = drawStr;

        if (!this.drawString)
        {
            this.displayString = "";
        }
    }

    public GuiSlider(int id, int xPos, int yPos, String displayStr, double minVal, double maxVal, double currentVal, ISlider par)
    {
        this(id, xPos, yPos, 150, 20, displayStr, "", minVal, maxVal, currentVal, true, true, par);
    }

    @Override
    public int getHoverState(boolean hover)
    {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseZ)
    {
        if (this.visible)
        {
            if (this.dragging)
            {
                this.sliderValue = (mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                this.updateSlider();
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseZ)
    {
        if (super.mousePressed(mc, mouseX, mouseZ))
        {
            this.sliderValue = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
            this.updateSlider();
            this.dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void updateSlider()
    {
        if (this.sliderValue < 0.0F)
        {
            this.sliderValue = 0.0F;
        }
        if (this.sliderValue > 1.0F)
        {
            this.sliderValue = 1.0F;
        }

        String val;

        if (this.showDecimal)
        {
            val = Double.toString(this.sliderValue * (this.maxValue - this.minValue) + this.minValue);

            if (val.substring(val.indexOf(".") + 1).length() > this.precision)
            {
                val = val.substring(0, val.indexOf(".") + this.precision + 1);

                if (val.endsWith("."))
                {
                    val = val.substring(0, val.indexOf(".") + this.precision);
                }
            }
            else
            {
                while (val.substring(val.indexOf(".") + 1).length() < this.precision)
                {
                    val = val + "0";
                }
            }
        }
        else
        {
            val = Integer.toString((int)Math.round(this.sliderValue * (this.maxValue - this.minValue) + this.minValue));
        }

        if (this.drawString)
        {
            this.displayString = this.dispString + val + this.suffix;
        }
        if (this.parent != null)
        {
            this.parent.onChangeSliderValue(this);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseZ)
    {
        this.dragging = false;
    }

    public int getValueInt()
    {
        return (int)Math.round(this.sliderValue * (this.maxValue - this.minValue) + this.minValue);
    }

    public double getValue()
    {
        return this.sliderValue * (this.maxValue - this.minValue) + this.minValue;
    }

    public void setValue(double d)
    {
        this.sliderValue = (d - this.minValue) / (this.maxValue - this.minValue);
    }

    public static interface ISlider
    {
        void onChangeSliderValue(GuiSlider slider);
    }
}