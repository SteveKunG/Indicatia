package stevekung.mods.indicatia.gui.exconfig;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;

@OnlyIn(Dist.CLIENT)
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
    public Widget createOptionButton(ExtendedConfig config, int x, int y, int width)
    {
        return new Button(x, y, width, 20, this.getDisplayString(config), button ->
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
        return this.getDisplayPrefix() + (this.get(config) ? TextFormatting.GREEN + "true" : TextFormatting.RED + "false");
    }
}
