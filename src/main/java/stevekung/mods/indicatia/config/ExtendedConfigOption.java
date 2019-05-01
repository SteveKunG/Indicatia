package stevekung.mods.indicatia.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import stevekung.mods.stevekungslib.utils.LangUtils;

@Environment(EnvType.CLIENT)
public abstract class ExtendedConfigOption
{
    private final String key;

    public ExtendedConfigOption(String key)
    {
        this.key = "extended_config." + key;
    }

    public abstract AbstractButtonWidget createOptionButton(ExtendedConfig options, int x, int y, int width);

    public String getDisplayPrefix()
    {
        return LangUtils.translate(this.key) + ": ";
    }
}
