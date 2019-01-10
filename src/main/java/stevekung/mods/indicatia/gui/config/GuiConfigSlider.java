package stevekung.mods.indicatia.gui.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.LangUtils;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiConfigSlider extends GuiButton
{
    private double sliderValue;
    private boolean dragging;
    private final ExtendedConfig.Options options;
    private static final List<String> SMALL_TEXT = new ArrayList<>();

    static
    {
        SMALL_TEXT.add("potion_length_y_offset_overlap.extended_config");
    }

    GuiConfigSlider(int id, int x, int y, int width, ExtendedConfig.Options option)
    {
        super(id, x, y, width, 20, "");
        this.sliderValue = 1.0D;
        this.options = option;
        this.sliderValue = option.normalizeValue(ExtendedConfig.instance.getOptionDoubleValue(option));
        this.displayString = ExtendedConfig.instance.getKeyBinding(option);
    }

    @Override
    protected int getHoverState(boolean mouseOver)
    {
        return 0;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            Minecraft mc = Minecraft.getInstance();
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            this.renderBg(mc, mouseX, mouseY);
            int color = 14737632;

            if (this.packedFGColor != 0)
            {
                color = this.packedFGColor;
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

            if (smallText)
            {
                //                mc.fontRenderer.setUnicodeFlag(true);TODO
            }

            this.drawCenteredString(mc.fontRenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, color);

            if (smallText)
            {
                //                mc.fontRenderer.setUnicodeFlag(mc.getLanguageManager().isCurrentLocaleUnicode() || mc.gameSettings.forceUnicodeFont);
            }
        }
    }

    @Override
    protected void renderBg(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            if (this.dragging)
            {
                this.sliderValue = (double)((float)(mouseX - (this.x + 4)) / (float)(this.width - 8));
                this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0D, 1.0D);
                double value = this.options.denormalizeValue(this.sliderValue);
                ExtendedConfig.instance.setOptionDoubleValue(this.options, value);
                this.sliderValue = this.options.normalizeValue(value);
                this.displayString = ExtendedConfig.instance.getKeyBinding(this.options);
            }
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (double)(this.width - 8)), this.y, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (double)(this.width - 8)) + 4, this.y, 196, 66, 4, 20);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY)
    {
        this.sliderValue = (mouseX - (double)(this.x + 4)) / (double)(this.width - 8);
        this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0D, 1.0D);
        ExtendedConfig.instance.setOptionDoubleValue(this.options, this.options.denormalizeValue(this.sliderValue));
        this.displayString = ExtendedConfig.instance.getKeyBinding(this.options);
        this.dragging = true;
    }

    @Override
    public void onRelease(double mouseX, double mouseY)
    {
        this.dragging = false;
    }
}
