package stevekung.mods.indicatia.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.config.ExtendedConfig;

@SideOnly(Side.CLIENT)
public class GuiCPSSlider extends GuiButton
{
    private float sliderValue;
    public boolean dragging;
    private Options options;

    public GuiCPSSlider(int id, int x, int y, Options option)
    {
        super(id, x, y, 100, 20, "");
        this.options = option;
        this.sliderValue = option.normalizeValue(ExtendedConfig.CPS_OPACITY);

        if (this.sliderValue == 0.45000002F)
        {
            this.sliderValue = 0.45F;
        }
        if (this.sliderValue == 0.65000004F)
        {
            this.sliderValue = 0.65F;
        }
        if (this.sliderValue == 0.90000004F)
        {
            this.sliderValue = 0.9F;
        }
        this.displayString = option.getEnumString() + " " + this.sliderValue;
    }

    @Override
    protected int getHoverState(boolean mouseOver)
    {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            if (this.dragging)
            {
                this.sliderValue = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
                float f = this.options.denormalizeValue(this.sliderValue);
                this.sliderValue = this.options.normalizeValue(f);

                if (this.sliderValue == 0.45000002F)
                {
                    this.sliderValue = 0.45F;
                }
                if (this.sliderValue == 0.65000004F)
                {
                    this.sliderValue = 0.65F;
                }
                if (this.sliderValue == 0.90000004F)
                {
                    this.sliderValue = 0.9F;
                }
                ExtendedConfig.CPS_OPACITY = this.sliderValue;
                this.displayString = this.options.getEnumString() + " " + this.sliderValue;
            }
            mc.getTextureManager().bindTexture(GuiButton.BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (this.width - 8)), this.y, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (super.mousePressed(mc, mouseX, mouseY))
        {
            this.sliderValue = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
            this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
            ExtendedConfig.CPS_OPACITY = this.sliderValue;
            this.displayString = this.options.getEnumString();
            this.dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY)
    {
        this.dragging = false;
        ExtendedConfig.save();
    }

    public static enum Options
    {
        CPS_OPACITY("CPS Opacity", 0.0F, 1.0F, 0.05F);

        private String enumString;
        private float valueStep;
        private float valueMin;
        private float valueMax;

        private Options(String str, float valMin, float valMax, float valStep)
        {
            this.enumString = str;
            this.valueMin = valMin;
            this.valueMax = valMax;
            this.valueStep = valStep;
        }

        public String getEnumString()
        {
            return this.enumString;
        }

        public float normalizeValue(float value)
        {
            return MathHelper.clamp((this.snapToStepClamp(value) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
        }

        public float denormalizeValue(float value)
        {
            return this.snapToStepClamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp(value, 0.0F, 1.0F));
        }

        public float snapToStepClamp(float value)
        {
            value = this.snapToStep(value);
            return MathHelper.clamp(value, this.valueMin, this.valueMax);
        }

        private float snapToStep(float value)
        {
            if (this.valueStep > 0.0F)
            {
                value = this.valueStep * Math.round(value / this.valueStep);
            }
            return value;
        }
    }
}