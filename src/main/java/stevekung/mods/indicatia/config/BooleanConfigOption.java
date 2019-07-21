package stevekung.mods.indicatia.config;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class BooleanConfigOption extends ExtendedConfigOption
{
    private final Predicate<ExtendedConfig> getter;
    private final BiConsumer<ExtendedConfig, Boolean> setter;

    public BooleanConfigOption(String key, Predicate<ExtendedConfig> getter, BiConsumer<ExtendedConfig, Boolean> setter)
    {
        super(key);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public AbstractButtonWidget createOptionButton(ExtendedConfig config, int x, int y, int width)
    {
        return new ConfigOptionButtonWidget(x, y, width, 20, this.getDisplayString(config), button ->
        {
            this.set(config);
            button.setMessage(this.getDisplayString(config));
        });
    }

    public void set(ExtendedConfig config, String value)
    {
        this.set(config, "true".equals(value));
    }

    public void set(ExtendedConfig config)
    {
        this.set(config, !this.get(config));
        config.save();
    }

    private void set(ExtendedConfig config, boolean value)
    {
        this.setter.accept(config, value);
    }

    public boolean get(ExtendedConfig config)
    {
        return this.getter.test(config);
    }

    public String getDisplayString(ExtendedConfig config)
    {
        return this.getDisplayPrefix() + (this.get(config) ? Formatting.GREEN + "true" : Formatting.RED + "false");
    }
}