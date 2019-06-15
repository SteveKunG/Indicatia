package stevekung.mods.indicatia.gui.config;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekungslib.utils.LangUtils;

@OnlyIn(Dist.CLIENT)
public abstract class ConfigSliderWidget extends Widget
{
    protected final ExtendedConfig options;
    protected double value;

    protected ConfigSliderWidget(int x, int y, int width, int height, double value)
    {
        this(ExtendedConfig.instance, x, y, width, height, value);
    }

    protected ConfigSliderWidget(ExtendedConfig config, int x, int y, int width, int height, double value)
    {
        super(x, y, width, height, "");
        this.options = config;
        this.value = value;
    }

    @Override
    protected int getYImage(boolean hover)
    {
        return 0;
    }

    @Override
    protected String getNarrationMessage()
    {
        return LangUtils.translate("gui.narrate.slider", this.getMessage());
    }

    @Override
    protected void renderBg(Minecraft mc, int x, int y)
    {
        mc.getTextureManager().bindTexture(WIDGETS_LOCATION);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int index = (this.isHovered() ? 2 : 1) * 20;
        this.blit(this.x + (int)(this.value * (this.width - 8)), this.y, 0, 46 + index, 4, 20);
        this.blit(this.x + (int)(this.value * (this.width - 8)) + 4, this.y, 196, 46 + index, 4, 20);
    }

    @Override
    public void onClick(double mouseX, double mouseY)
    {
        this.setValueFromMouse(mouseX);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        boolean boolean_1 = keyCode == 263;

        if (boolean_1 || keyCode == 262)
        {
            float float_1 = boolean_1 ? -1.0F : 1.0F;
            this.setValue(this.value + float_1 / (this.width - 8));
        }
        return false;
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double double_3, double double_4)
    {
        this.setValueFromMouse(mouseX);
        super.onDrag(mouseX, mouseY, double_3, double_4);
    }

    @Override
    public void playDownSound(SoundHandler manager) {}

    @Override
    public void onRelease(double mouseX, double mouseY)
    {
        super.playDownSound(Minecraft.getInstance().getSoundHandler());
    }

    private void setValueFromMouse(double mouseX)
    {
        this.setValue((mouseX - (this.x + 4)) / (this.width - 8));
    }

    private void setValue(double value)
    {
        double currentValue = this.value;
        this.value = MathHelper.clamp(value, 0.0D, 1.0D);

        if (currentValue != this.value)
        {
            this.applyValue();
        }
        this.updateMessage();
    }

    protected abstract void updateMessage();

    protected abstract void applyValue();
}
