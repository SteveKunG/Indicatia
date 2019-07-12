package stevekung.mods.indicatia.gui.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.ColorUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;
import stevekung.mods.stevekunglib.utils.client.RenderUtils;

@SideOnly(Side.CLIENT)
public class GuiConfigSlider extends GuiButton
{
    private static final List<String> SMALL_TEXT = new ArrayList<>();
    private float sliderValue;
    public boolean dragging;
    private final ExtendedConfig.Options options;

    static
    {
        SMALL_TEXT.add("potion_length_y_offset_overlap.extended_config");
    }

    public GuiConfigSlider(int buttonId, int x, int y, int width, ExtendedConfig.Options option)
    {
        super(buttonId, x, y, width, 20, "");
        this.sliderValue = 1.0F;
        this.options = option;
        this.sliderValue = option.normalizeValue(ExtendedConfig.instance.getOptionFloatValue(option));
        this.displayString = ExtendedConfig.instance.getKeyBinding(option);
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
                ExtendedConfig.instance.setOptionFloatValue(this.options, f);
                this.sliderValue = this.options.normalizeValue(f);
                this.displayString = ExtendedConfig.instance.getKeyBinding(this.options);
            }
            RenderUtils.bindTexture(BUTTON_TEXTURES);
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
            ExtendedConfig.instance.setOptionFloatValue(this.options, this.options.denormalizeValue(this.sliderValue));
            this.displayString = ExtendedConfig.instance.getKeyBinding(this.options);
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

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int state = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + state * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + state * 20, this.width / 2, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int color = 14737632;

            if (this.packedFGColour != 0)
            {
                color = this.packedFGColour;
            }
            else
            {
                if (!this.enabled)
                {
                    color = 10526880;
                }
                else if (this.hovered)
                {
                    color = 16777120;
                }
            }
            boolean smallText = SMALL_TEXT.stream().anyMatch(text -> this.displayString.trim().contains(LangUtils.translate(text)));
            this.drawCenteredString(smallText ? ColorUtils.unicodeFontRenderer : mc.fontRenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, color);
        }
    }
}