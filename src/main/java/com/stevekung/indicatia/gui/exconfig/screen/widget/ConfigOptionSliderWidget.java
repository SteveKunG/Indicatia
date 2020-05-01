package com.stevekung.indicatia.gui.exconfig.screen.widget;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.gui.exconfig.DoubleConfigOption;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ConfigOptionSliderWidget extends ConfigSliderWidget
{
    private final DoubleConfigOption option;

    public ConfigOptionSliderWidget(ExtendedConfig config, int x, int y, int width, int height, DoubleConfigOption doubleOpt)
    {
        super(config, x, y, width, height, (float)doubleOpt.normalizeValue(doubleOpt.get(config)));
        this.option = doubleOpt;
        this.updateMessage();
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