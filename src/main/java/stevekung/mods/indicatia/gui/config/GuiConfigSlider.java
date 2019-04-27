package stevekung.mods.indicatia.gui.config;

import com.mojang.blaze3d.platform.GlStateManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.math.MathHelper;
import stevekung.mods.indicatia.config.ExtendedConfig;

@Environment(EnvType.CLIENT)
public class GuiConfigSlider extends ButtonWidget
{
    private double sliderValue;
    private boolean dragging;
    private final ExtendedConfig.Options options;

    GuiConfigSlider(int id, int x, int y, int width, ExtendedConfig.Options option)
    {
        super(x, y, width, 20, "", null);
        this.sliderValue = 1.0D;
        this.options = option;
        this.sliderValue = option.normalizeValue(ExtendedConfig.instance.getOptionDoubleValue(option));
        this.setMessage(ExtendedConfig.instance.getKeyBinding(option));
    }

    @Override
    protected int getYImage(boolean mouseOver)
    {
        return 0;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            MinecraftClient mc = MinecraftClient.getInstance();
            mc.getTextureManager().bindTexture(WIDGETS_LOCATION);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getYImage(this.isHovered);
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.blit(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
            this.blit(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            this.renderBg(mc, mouseX, mouseY);
            int color = 14737632;

            if (!this.active)
            {
                color = 10526880;
            }
            else if (this.isHovered)
            {
                color = 16777120;
            }

            if (this.getMessage().length() > 24)
            {
                //                mc.fontRenderer.setUnicodeFlag(true);TODO
            }

            this.drawCenteredString(mc.textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, color);

            if (this.getMessage().length() > 24)
            {
                //                mc.fontRenderer.setUnicodeFlag(mc.getLanguageManager().isCurrentLocaleUnicode() || mc.gameSettings.forceUnicodeFont);
            }
        }
    }

    @Override
    protected void renderBg(MinecraftClient mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            if (this.dragging)
            {
                this.sliderValue = (float)(mouseX - (this.x + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0D, 1.0D);
                double value = this.options.denormalizeValue(this.sliderValue);
                ExtendedConfig.instance.setOptionDoubleValue(this.options, value);
                this.sliderValue = this.options.normalizeValue(value);
                this.setMessage(ExtendedConfig.instance.getKeyBinding(this.options));
            }
            mc.getTextureManager().bindTexture(WIDGETS_LOCATION);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.blit(this.x + (int)(this.sliderValue * (this.width - 8)), this.y, 0, 66, 4, 20);
            this.blit(this.x + (int)(this.sliderValue * (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY)
    {
        this.sliderValue = (mouseX - (this.x + 4)) / (this.width - 8);
        this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0D, 1.0D);
        ExtendedConfig.instance.setOptionDoubleValue(this.options, this.options.denormalizeValue(this.sliderValue));
        this.setMessage(ExtendedConfig.instance.getKeyBinding(this.options));
        this.dragging = true;
    }

    @Override
    public void onRelease(double mouseX, double mouseY)
    {
        this.dragging = false;
    }
}
