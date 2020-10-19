package com.stevekung.indicatia.gui.exconfig;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.gui.exconfig.screen.widget.ExtendedButton;
import com.stevekung.stevekungslib.utils.TextComponentUtils;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;

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
    public Widget createOptionButton(int x, int y, int width)
    {
        return new ExtendedButton(x, y, width, 20, this.get(), button ->
        {
            this.set(1);
            button.setMessage(this.get());
        });
    }

    public void set(int value)
    {
        this.getter.accept(ExtendedConfig.INSTANCE, value);
        ExtendedConfig.INSTANCE.save();
    }

    public ITextComponent get()
    {
        return TextComponentUtils.component(this.setter.apply(ExtendedConfig.INSTANCE, this));
    }
}