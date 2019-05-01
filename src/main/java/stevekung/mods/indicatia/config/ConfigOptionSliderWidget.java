package stevekung.mods.indicatia.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ConfigOptionSliderWidget extends ConfigSliderWidget
{
    private final DoubleConfigOption option;

    public ConfigOptionSliderWidget(ExtendedConfig config, int int_1, int int_2, int int_3, int int_4, DoubleConfigOption doubleOpt)
    {
        super(config, int_1, int_2, int_3, int_4, (float)doubleOpt.normalizeValue(doubleOpt.get(config)));
        this.option = doubleOpt;
        this.updateMessage();
    }

    @Override
    public void renderButton(int x, int y, float partialTicks)
    {
        /*if (this.option == GameOption.FULLSCREEN_RESOLUTION)
        {
            this.updateMessage();
        }*/
        super.renderButton(x, y, partialTicks);
    }

    @Override
    protected void applyValue()
    {
        this.option.set(this.options, this.option.denormalizeValue(this.value));
        this.options.save();
    }

    @Override
    protected void updateMessage()
    {
        this.setMessage(this.option.getDisplayString(this.options));
    }
}
