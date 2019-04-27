package stevekung.mods.indicatia.gui.config;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionSliderWidget;
import net.minecraft.util.math.MathHelper;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.LangUtils;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ConfigSliderWidget extends ButtonWidget
{
    private static final List<String> SMALL_TEXT = new ArrayList<>();
    private double sliderValue;
    public boolean dragging;
    private final ExtendedConfig.Options options;

    static
    {
        SMALL_TEXT.add("potion_length_y_offset_overlap.extended_config");
    }

    public ConfigSliderWidget(int buttonId, int x, int y, int width, ExtendedConfig.Options option)
    {
        super(buttonId, x, y, width, 20, "");
        this.sliderValue = 1.0D;
        this.options = option;
        this.sliderValue = option.normalizeValue(ExtendedConfig.instance.getOptionFloatValue(option));
        this.text = ExtendedConfig.instance.getKeyBinding(option);
    }

    @Override
    protected int getTextureId(boolean mouseOver)
    {
        return 0;
    }

    @Override
    public void onPressed(double mouseX, double mouseY)
    {
        this.sliderValue = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
        this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0F, 1.0F);
        ExtendedConfig.instance.setOptionFloatValue(this.options, this.options.denormalizeValue(this.sliderValue));
        this.text = ExtendedConfig.instance.getKeyBinding(this.options);
        this.dragging = true;
    }

    @Override
    public void onReleased(double mouseX, double mouseY)
    {
        this.dragging = false;
    }

    @Override
    protected void drawBackground(MinecraftClient mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            if (this.dragging)
            {
                this.sliderValue = (double)((float)(mouseX - (this.x + 4)) / (float)(this.width - 8));
                this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0D, 1.0D);
            }
            mc.getTextureManager().bindTexture(WIDGET_TEX);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedRect(this.x + (int)(this.sliderValue * (double)(this.width - 8)), this.y, 0, 66, 4, 20);
            this.drawTexturedRect(this.x + (int)(this.sliderValue * (double)(this.width - 8)) + 4, this.y, 196, 66, 4, 20);
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            MinecraftClient mc = MinecraftClient.getInstance();
            mc.getTextureManager().bindTexture(WIDGET_TEX);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int state = this.getTextureId(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SrcBlendFactor.SRC_ALPHA, GlStateManager.DstBlendFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcBlendFactor.ONE, GlStateManager.DstBlendFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SrcBlendFactor.SRC_ALPHA, GlStateManager.DstBlendFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedRect(this.x, this.y, 0, 46 + state * 20, this.width / 2, this.height);
            this.drawTexturedRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + state * 20, this.width / 2, this.height);
            int color = 14737632;

            if (!this.enabled)
            {
                color = 10526880;
            }
            else if (this.hovered)
            {
                color = 16777120;
            }

            boolean smallText = SMALL_TEXT.stream().anyMatch(text -> this.text.trim().contains(LangUtils.translate(text)));

            if (smallText)
            {
                //.method_1568().setForceUnicodeFont(true);TODO
            }

            this.drawStringCentered(mc.fontRenderer, this.text, this.x + this.width / 2, this.y + (this.height - 8) / 2, color);

            if (smallText)
            {
                //mc.method_1568().setForceUnicodeFont(mc.options.forceUnicodeFont);
            }
        }
    }
}