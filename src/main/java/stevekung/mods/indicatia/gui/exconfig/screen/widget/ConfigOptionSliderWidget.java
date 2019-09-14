package stevekung.mods.indicatia.gui.exconfig.screen.widget;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.exconfig.DoubleConfigOption;

@OnlyIn(Dist.CLIENT)
public class ConfigOptionSliderWidget extends ConfigSliderWidget
{
    private final DoubleConfigOption option;

    public ConfigOptionSliderWidget(int x, int y, int width, int height, DoubleConfigOption doubleOpt)
    {
        super(x, y, width, height, (float)doubleOpt.normalizeValue(doubleOpt.get()));
        this.option = doubleOpt;
        this.updateMessage();
    }

    @Override
    protected void applyValue()
    {
        this.option.set(this.option.denormalizeValue(this.value));
        ExtendedConfig.INSTANCE.save();
    }

    @Override
    protected void updateMessage()
    {
        this.setMessage(this.option.getDisplayString());
    }
}
