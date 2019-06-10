package stevekung.mods.indicatia.gui.config;

import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekungslib.utils.LangUtils;

@OnlyIn(Dist.CLIENT)
public abstract class ExtendedConfigOption
{
    private final String key;

    public ExtendedConfigOption(String key)
    {
        this.key = "extended_config." + key;
    }

    public abstract AbstractButton createOptionButton(ExtendedConfig options, int x, int y, int width);

    public String getDisplayPrefix()
    {
        return LangUtils.translate(this.key) + ": ";
    }
}
