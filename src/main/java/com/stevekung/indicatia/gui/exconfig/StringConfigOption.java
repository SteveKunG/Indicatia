package com.stevekung.indicatia.gui.exconfig;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.gui.exconfig.screen.widget.ConfigOptionButtonWidget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.AbstractButtonWidget;

@Environment(EnvType.CLIENT)
public class StringConfigOption extends ExtendedConfigOption
{
    private final BiConsumer<ExtendedConfig, Integer> getter;
    private final BiFunction<ExtendedConfig, StringConfigOption, String> setter;

    public StringConfigOption(String key, BiConsumer<ExtendedConfig, Integer> getter, BiFunction<ExtendedConfig, StringConfigOption, String> setter)
    {
        super(key);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public AbstractButtonWidget createOptionButton(ExtendedConfig config, int x, int y, int width)
    {
        return new ConfigOptionButtonWidget(x, y, width, 20, this.get(config), button ->
        {
            this.set(config, 1);
            button.setMessage(this.get(config));
        });
    }

    public void set(ExtendedConfig config, int value)
    {
        this.getter.accept(config, value);
        config.save();
    }

    public String get(ExtendedConfig config)
    {
        return this.setter.apply(config, this);
    }
}