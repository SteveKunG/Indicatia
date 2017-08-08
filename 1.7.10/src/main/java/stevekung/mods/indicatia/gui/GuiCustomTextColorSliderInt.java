package stevekung.mods.indicatia.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import stevekung.mods.indicatia.config.ExtendedConfig;

@SideOnly(Side.CLIENT)
public class GuiCustomTextColorSliderInt extends GuiButton
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("indicatia:textures/gui/trans_gui.png");
    private float sliderValue;
    public boolean dragging;
    private final Options options;

    public GuiCustomTextColorSliderInt(int x, int y, Options option)
    {
        super(0, x, y, 100, 16, "");
        this.sliderValue = 1.0F;
        this.options = option;
        this.sliderValue = option.normalizeValue(this.getOptionValue(option));
        this.displayString = option.getEnumString() + ": " + this.getOptionValue(option);
    }

    @Override
    public int getHoverState(boolean mouseOver)
    {
        return 0;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(GuiCustomTextColorSliderInt.TEXTURE);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, i * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, i * 20, this.width / 2, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }
            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
        }
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            if (this.dragging)
            {
                this.sliderValue = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
                float f = this.options.denormalizeValue(this.sliderValue);
                this.setOptionValue(this.options, f);
                this.sliderValue = this.options.normalizeValue(f);
                this.displayString = this.options.getEnumString() + ": " + this.getOptionValue(this.options);
            }
            mc.getTextureManager().bindTexture(GuiCustomTextColorSliderInt.TEXTURE);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)), this.yPosition, 0, 20, 4, this.height);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 4, this.yPosition, 196, 20, 4, this.height);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (super.mousePressed(mc, mouseX, mouseY))
        {
            this.sliderValue = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
            this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
            this.setOptionValue(this.options, this.options.denormalizeValue(this.sliderValue));
            this.displayString = this.options.getEnumString() + ": " + this.getOptionValue(this.options);
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
    }

    public void setOptionValue(Options option, float value)
    {
        int ivalue = (int) value;

        switch (option)
        {
        case FPS_R:
            ExtendedConfig.FPS_COLOR_R = ivalue;
            break;
        case FPS_G:
            ExtendedConfig.FPS_COLOR_G = ivalue;
            break;
        case FPS_B:
            ExtendedConfig.FPS_COLOR_B = ivalue;
            break;
        case FPS_M40_R:
            ExtendedConfig.FPS_M40_COLOR_R = ivalue;
            break;
        case FPS_M40_G:
            ExtendedConfig.FPS_M40_COLOR_G = ivalue;
            break;
        case FPS_M40_B:
            ExtendedConfig.FPS_M40_COLOR_B = ivalue;
            break;
        case FPS_26_40_R:
            ExtendedConfig.FPS_26_40_COLOR_R = ivalue;
            break;
        case FPS_26_40_G:
            ExtendedConfig.FPS_26_40_COLOR_G = ivalue;
            break;
        case FPS_26_40_B:
            ExtendedConfig.FPS_26_40_COLOR_B = ivalue;
            break;
        case FPS_L25_R:
            ExtendedConfig.FPS_L25_COLOR_R = ivalue;
            break;
        case FPS_L25_G:
            ExtendedConfig.FPS_L25_COLOR_G = ivalue;
            break;
        case FPS_L25_B:
            ExtendedConfig.FPS_L25_COLOR_B = ivalue;
            break;
        case XYZ_R:
            ExtendedConfig.XYZ_COLOR_R = ivalue;
            break;
        case XYZ_G:
            ExtendedConfig.XYZ_COLOR_G = ivalue;
            break;
        case XYZ_B:
            ExtendedConfig.XYZ_COLOR_B = ivalue;
            break;
        case XYZ_VALUE_R:
            ExtendedConfig.XYZ_VALUE_COLOR_R = ivalue;
            break;
        case XYZ_VALUE_G:
            ExtendedConfig.XYZ_VALUE_COLOR_G = ivalue;
            break;
        case XYZ_VALUE_B:
            ExtendedConfig.XYZ_VALUE_COLOR_B = ivalue;
            break;
        case BIOME_R:
            ExtendedConfig.BIOME_COLOR_R = ivalue;
            break;
        case BIOME_G:
            ExtendedConfig.BIOME_COLOR_G = ivalue;
            break;
        case BIOME_B:
            ExtendedConfig.BIOME_COLOR_B = ivalue;
            break;
        case BIOME_VALUE_R:
            ExtendedConfig.BIOME_VALUE_COLOR_R = ivalue;
            break;
        case BIOME_VALUE_G:
            ExtendedConfig.BIOME_VALUE_COLOR_G = ivalue;
            break;
        case BIOME_VALUE_B:
            ExtendedConfig.BIOME_VALUE_COLOR_B = ivalue;
            break;
        case PING_R:
            ExtendedConfig.PING_COLOR_R = ivalue;
            break;
        case PING_G:
            ExtendedConfig.PING_COLOR_G = ivalue;
            break;
        case PING_B:
            ExtendedConfig.PING_COLOR_B = ivalue;
            break;
        case PING_L200_R:
            ExtendedConfig.PING_L200_COLOR_R = ivalue;
            break;
        case PING_L200_G:
            ExtendedConfig.PING_L200_COLOR_G = ivalue;
            break;
        case PING_L200_B:
            ExtendedConfig.PING_L200_COLOR_B = ivalue;
            break;
        case PING_200_300_R:
            ExtendedConfig.PING_200_300_COLOR_R = ivalue;
            break;
        case PING_200_300_G:
            ExtendedConfig.PING_200_300_COLOR_G = ivalue;
            break;
        case PING_200_300_B:
            ExtendedConfig.PING_200_300_COLOR_B = ivalue;
            break;
        case PING_300_500_R:
            ExtendedConfig.PING_300_500_COLOR_R = ivalue;
            break;
        case PING_300_500_G:
            ExtendedConfig.PING_300_500_COLOR_G = ivalue;
            break;
        case PING_300_500_B:
            ExtendedConfig.PING_300_500_COLOR_B = ivalue;
            break;
        case PING_M500_R:
            ExtendedConfig.PING_M500_COLOR_R = ivalue;
            break;
        case PING_M500_G:
            ExtendedConfig.PING_M500_COLOR_G = ivalue;
            break;
        case PING_M500_B:
            ExtendedConfig.PING_M500_COLOR_B = ivalue;
            break;
        case IP_R:
            ExtendedConfig.IP_COLOR_R = ivalue;
            break;
        case IP_G:
            ExtendedConfig.IP_COLOR_G = ivalue;
            break;
        case IP_B:
            ExtendedConfig.IP_COLOR_B = ivalue;
            break;
        case IP_VALUE_R:
            ExtendedConfig.IP_VALUE_COLOR_R = ivalue;
            break;
        case IP_VALUE_G:
            ExtendedConfig.IP_VALUE_COLOR_G = ivalue;
            break;
        case IP_VALUE_B:
            ExtendedConfig.IP_VALUE_COLOR_B = ivalue;
            break;
        case CPS_R:
            ExtendedConfig.CPS_COLOR_R = ivalue;
            break;
        case CPS_G:
            ExtendedConfig.CPS_COLOR_G = ivalue;
            break;
        case CPS_B:
            ExtendedConfig.CPS_COLOR_B = ivalue;
            break;
        case CPS_VALUE_R:
            ExtendedConfig.CPS_VALUE_COLOR_R = ivalue;
            break;
        case CPS_VALUE_G:
            ExtendedConfig.CPS_VALUE_COLOR_G = ivalue;
            break;
        case CPS_VALUE_B:
            ExtendedConfig.CPS_VALUE_COLOR_B = ivalue;
            break;
        case RCPS_R:
            ExtendedConfig.RCPS_COLOR_R = ivalue;
            break;
        case RCPS_G:
            ExtendedConfig.RCPS_COLOR_G = ivalue;
            break;
        case RCPS_B:
            ExtendedConfig.RCPS_COLOR_B = ivalue;
            break;
        case RCPS_VALUE_R:
            ExtendedConfig.RCPS_VALUE_COLOR_R = ivalue;
            break;
        case RCPS_VALUE_G:
            ExtendedConfig.RCPS_VALUE_COLOR_G = ivalue;
            break;
        case RCPS_VALUE_B:
            ExtendedConfig.RCPS_VALUE_COLOR_B = ivalue;
            break;
        case TOP_DONATE_NAME_R:
            ExtendedConfig.TOP_DONATE_NAME_COLOR_R = ivalue;
            break;
        case TOP_DONATE_NAME_G:
            ExtendedConfig.TOP_DONATE_NAME_COLOR_G = ivalue;
            break;
        case TOP_DONATE_NAME_B:
            ExtendedConfig.TOP_DONATE_NAME_COLOR_B = ivalue;
            break;
        case RECENT_DONATE_NAME_R:
            ExtendedConfig.RECENT_DONATE_NAME_COLOR_R = ivalue;
            break;
        case RECENT_DONATE_NAME_G:
            ExtendedConfig.RECENT_DONATE_NAME_COLOR_G = ivalue;
            break;
        case RECENT_DONATE_NAME_B:
            ExtendedConfig.RECENT_DONATE_NAME_COLOR_B = ivalue;
            break;
        case TOP_DONATE_COUNT_R:
            ExtendedConfig.TOP_DONATE_COUNT_COLOR_R = ivalue;
            break;
        case TOP_DONATE_COUNT_G:
            ExtendedConfig.TOP_DONATE_COUNT_COLOR_G = ivalue;
            break;
        case TOP_DONATE_COUNT_B:
            ExtendedConfig.TOP_DONATE_COUNT_COLOR_B = ivalue;
            break;
        case RECENT_DONATE_COUNT_R:
            ExtendedConfig.RECENT_DONATE_COUNT_COLOR_R = ivalue;
            break;
        case RECENT_DONATE_COUNT_G:
            ExtendedConfig.RECENT_DONATE_COUNT_COLOR_G = ivalue;
            break;
        case RECENT_DONATE_COUNT_B:
            ExtendedConfig.RECENT_DONATE_COUNT_COLOR_B = ivalue;
            break;
        case SLIME_R:
            ExtendedConfig.SLIME_COLOR_R = ivalue;
            break;
        case SLIME_G:
            ExtendedConfig.SLIME_COLOR_G = ivalue;
            break;
        case SLIME_B:
            ExtendedConfig.SLIME_COLOR_B = ivalue;
            break;
        case SLIME_VALUE_R:
            ExtendedConfig.SLIME_VALUE_COLOR_R = ivalue;
            break;
        case SLIME_VALUE_G:
            ExtendedConfig.SLIME_VALUE_COLOR_G = ivalue;
            break;
        case SLIME_VALUE_B:
            ExtendedConfig.SLIME_VALUE_COLOR_B = ivalue;
            break;
        }
    }

    public int getOptionValue(Options option)
    {
        switch (option)
        {
        case FPS_R:
            return ExtendedConfig.FPS_COLOR_R;
        case FPS_G:
            return ExtendedConfig.FPS_COLOR_G;
        case FPS_B:
            return ExtendedConfig.FPS_COLOR_B;
        case FPS_M40_R:
            return ExtendedConfig.FPS_M40_COLOR_R;
        case FPS_M40_G:
            return ExtendedConfig.FPS_M40_COLOR_G;
        case FPS_M40_B:
            return ExtendedConfig.FPS_M40_COLOR_B;
        case FPS_26_40_R:
            return ExtendedConfig.FPS_26_40_COLOR_R;
        case FPS_26_40_G:
            return ExtendedConfig.FPS_26_40_COLOR_G;
        case FPS_26_40_B:
            return ExtendedConfig.FPS_26_40_COLOR_B;
        case FPS_L25_R:
            return ExtendedConfig.FPS_L25_COLOR_R;
        case FPS_L25_G:
            return ExtendedConfig.FPS_L25_COLOR_G;
        case FPS_L25_B:
            return ExtendedConfig.FPS_L25_COLOR_B;
        case XYZ_R:
            return ExtendedConfig.XYZ_COLOR_R;
        case XYZ_G:
            return ExtendedConfig.XYZ_COLOR_G;
        case XYZ_B:
            return ExtendedConfig.XYZ_COLOR_B;
        case XYZ_VALUE_R:
            return ExtendedConfig.XYZ_VALUE_COLOR_R;
        case XYZ_VALUE_G:
            return ExtendedConfig.XYZ_VALUE_COLOR_G;
        case XYZ_VALUE_B:
            return ExtendedConfig.XYZ_VALUE_COLOR_B;
        case BIOME_R:
            return ExtendedConfig.BIOME_COLOR_R;
        case BIOME_G:
            return ExtendedConfig.BIOME_COLOR_G;
        case BIOME_B:
            return ExtendedConfig.BIOME_COLOR_B;
        case BIOME_VALUE_R:
            return ExtendedConfig.BIOME_VALUE_COLOR_R;
        case BIOME_VALUE_G:
            return ExtendedConfig.BIOME_VALUE_COLOR_G;
        case BIOME_VALUE_B:
            return ExtendedConfig.BIOME_VALUE_COLOR_B;
        case PING_R:
            return ExtendedConfig.PING_COLOR_R;
        case PING_G:
            return ExtendedConfig.PING_COLOR_G;
        case PING_B:
            return ExtendedConfig.PING_COLOR_B;
        case PING_L200_R:
            return ExtendedConfig.PING_L200_COLOR_R;
        case PING_L200_G:
            return ExtendedConfig.PING_L200_COLOR_G;
        case PING_L200_B:
            return ExtendedConfig.PING_L200_COLOR_B;
        case PING_200_300_R:
            return ExtendedConfig.PING_200_300_COLOR_R;
        case PING_200_300_G:
            return ExtendedConfig.PING_200_300_COLOR_G;
        case PING_200_300_B:
            return ExtendedConfig.PING_200_300_COLOR_B;
        case PING_300_500_R:
            return ExtendedConfig.PING_300_500_COLOR_R;
        case PING_300_500_G:
            return ExtendedConfig.PING_300_500_COLOR_G;
        case PING_300_500_B:
            return ExtendedConfig.PING_300_500_COLOR_B;
        case PING_M500_R:
            return ExtendedConfig.PING_M500_COLOR_R;
        case PING_M500_G:
            return ExtendedConfig.PING_M500_COLOR_G;
        case PING_M500_B:
            return ExtendedConfig.PING_M500_COLOR_B;
        case IP_R:
            return ExtendedConfig.IP_COLOR_R;
        case IP_G:
            return ExtendedConfig.IP_COLOR_G;
        case IP_B:
            return ExtendedConfig.IP_COLOR_B;
        case IP_VALUE_R:
            return ExtendedConfig.IP_VALUE_COLOR_R;
        case IP_VALUE_G:
            return ExtendedConfig.IP_VALUE_COLOR_G;
        case IP_VALUE_B:
            return ExtendedConfig.IP_VALUE_COLOR_B;
        case CPS_R:
            return ExtendedConfig.CPS_COLOR_R;
        case CPS_G:
            return ExtendedConfig.CPS_COLOR_G;
        case CPS_B:
            return ExtendedConfig.CPS_COLOR_B;
        case CPS_VALUE_R:
            return ExtendedConfig.CPS_VALUE_COLOR_R;
        case CPS_VALUE_G:
            return ExtendedConfig.CPS_VALUE_COLOR_G;
        case CPS_VALUE_B:
            return ExtendedConfig.CPS_VALUE_COLOR_B;
        case RCPS_R:
            return ExtendedConfig.RCPS_COLOR_R;
        case RCPS_G:
            return ExtendedConfig.RCPS_COLOR_G;
        case RCPS_B:
            return ExtendedConfig.RCPS_COLOR_B;
        case RCPS_VALUE_R:
            return ExtendedConfig.RCPS_VALUE_COLOR_R;
        case RCPS_VALUE_G:
            return ExtendedConfig.RCPS_VALUE_COLOR_G;
        case RCPS_VALUE_B:
            return ExtendedConfig.RCPS_VALUE_COLOR_B;
        case TOP_DONATE_NAME_R:
            return ExtendedConfig.TOP_DONATE_NAME_COLOR_R;
        case TOP_DONATE_NAME_G:
            return ExtendedConfig.TOP_DONATE_NAME_COLOR_G;
        case TOP_DONATE_NAME_B:
            return ExtendedConfig.TOP_DONATE_NAME_COLOR_B;
        case RECENT_DONATE_NAME_R:
            return ExtendedConfig.RECENT_DONATE_NAME_COLOR_R;
        case RECENT_DONATE_NAME_G:
            return ExtendedConfig.RECENT_DONATE_NAME_COLOR_G;
        case RECENT_DONATE_NAME_B:
            return ExtendedConfig.RECENT_DONATE_NAME_COLOR_B;
        case TOP_DONATE_COUNT_R:
            return ExtendedConfig.TOP_DONATE_COUNT_COLOR_R;
        case TOP_DONATE_COUNT_G:
            return ExtendedConfig.TOP_DONATE_COUNT_COLOR_G;
        case TOP_DONATE_COUNT_B:
            return ExtendedConfig.TOP_DONATE_COUNT_COLOR_B;
        case RECENT_DONATE_COUNT_R:
            return ExtendedConfig.RECENT_DONATE_COUNT_COLOR_R;
        case RECENT_DONATE_COUNT_G:
            return ExtendedConfig.RECENT_DONATE_COUNT_COLOR_G;
        case RECENT_DONATE_COUNT_B:
            return ExtendedConfig.RECENT_DONATE_COUNT_COLOR_B;
        case SLIME_R:
            return ExtendedConfig.SLIME_COLOR_R;
        case SLIME_G:
            return ExtendedConfig.SLIME_COLOR_G;
        case SLIME_B:
            return ExtendedConfig.SLIME_COLOR_B;
        case SLIME_VALUE_R:
            return ExtendedConfig.SLIME_VALUE_COLOR_R;
        case SLIME_VALUE_G:
            return ExtendedConfig.SLIME_VALUE_COLOR_G;
        case SLIME_VALUE_B:
            return ExtendedConfig.SLIME_VALUE_COLOR_B;
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public static enum Options
    {
        FPS_R("FPS R"),
        FPS_G("FPS G"),
        FPS_B("FPS B"),
        FPS_M40_R("FPS > 40 R"),
        FPS_M40_G("FPS > 40 G"),
        FPS_M40_B("FPS > 40 B"),
        FPS_26_40_R("FPS 25&40 R"),
        FPS_26_40_G("FPS 25&40 G"),
        FPS_26_40_B("FPS 25&40 B"),
        FPS_L25_R("FPS < 25 R"),
        FPS_L25_G("FPS < 25 G"),
        FPS_L25_B("FPS < 25 B"),
        XYZ_R("XYZ R"),
        XYZ_G("XYZ G"),
        XYZ_B("XYZ B"),
        XYZ_VALUE_R("XYZ Value R"),
        XYZ_VALUE_G("XYZ Value G"),
        XYZ_VALUE_B("XYZ Value B"),
        BIOME_R("Biome R"),
        BIOME_G("Biome G"),
        BIOME_B("Biome B"),
        BIOME_VALUE_R("Biome Value R"),
        BIOME_VALUE_G("Biome Value G"),
        BIOME_VALUE_B("Biome Value B"),
        PING_R("Ping R"),
        PING_G("Ping G"),
        PING_B("Ping B"),
        PING_L200_R("Ping < 200 R"),
        PING_L200_G("Ping < 200 G"),
        PING_L200_B("Ping < 200 B"),
        PING_200_300_R("Ping 200&300 R"),
        PING_200_300_G("Ping 200&300 G"),
        PING_200_300_B("Ping 200&300 B"),
        PING_300_500_R("Ping 300&500 R"),
        PING_300_500_G("Ping 300&500 G"),
        PING_300_500_B("Ping 300&500 B"),
        PING_M500_R("Ping > 500 R"),
        PING_M500_G("Ping > 500 G"),
        PING_M500_B("Ping > 500 B"),
        IP_R("IP R"),
        IP_G("IP G"),
        IP_B("IP B"),
        IP_VALUE_R("IP Value R"),
        IP_VALUE_G("IP Value G"),
        IP_VALUE_B("IP Value B"),
        CPS_R("CPS R"),
        CPS_G("CPS G"),
        CPS_B("CPS B"),
        CPS_VALUE_R("CPS Value R"),
        CPS_VALUE_G("CPS Value G"),
        CPS_VALUE_B("CPS Value B"),
        RCPS_R("RCPS R"),
        RCPS_G("RCPS G"),
        RCPS_B("RCPS B"),
        RCPS_VALUE_R("RCPS Value R"),
        RCPS_VALUE_G("RCPS Value G"),
        RCPS_VALUE_B("RCPS Value B"),
        TOP_DONATE_NAME_R("Top Donate R"),
        TOP_DONATE_NAME_G("Top Donate G"),
        TOP_DONATE_NAME_B("Top Donate B"),
        RECENT_DONATE_NAME_R("Recent Donate R"),
        RECENT_DONATE_NAME_G("Recent Donate G"),
        RECENT_DONATE_NAME_B("Recent Donate B"),
        TOP_DONATE_COUNT_R("TDonate Count R"),
        TOP_DONATE_COUNT_G("TDonate Count G"),
        TOP_DONATE_COUNT_B("TDonate Count B"),
        RECENT_DONATE_COUNT_R("RDonate Count R"),
        RECENT_DONATE_COUNT_G("RDonate Count G"),
        RECENT_DONATE_COUNT_B("RDonate Count B"),
        SLIME_R("Slime R"),
        SLIME_G("Slime G"),
        SLIME_B("Slime B"),
        SLIME_VALUE_R("Slime Value R"),
        SLIME_VALUE_G("Slime Value G"),
        SLIME_VALUE_B("Slime Value B");

        private String enumString;
        private float valueStep;
        private float valueMin;
        private float valueMax;

        private Options(String str)
        {
            this.enumString = str;
            this.valueMin = 0;
            this.valueMax = 255;
            this.valueStep = 1;
        }

        public String getEnumString()
        {
            return this.enumString;
        }

        public float normalizeValue(float value)
        {
            return MathHelper.clamp_float((this.snapToStepclamp(value) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
        }

        public float denormalizeValue(float value)
        {
            return this.snapToStepclamp(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float(value, 0.0F, 1.0F));
        }

        public float snapToStepclamp(float value)
        {
            value = this.snapToStep(value);
            return MathHelper.clamp_float(value, this.valueMin, this.valueMax);
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