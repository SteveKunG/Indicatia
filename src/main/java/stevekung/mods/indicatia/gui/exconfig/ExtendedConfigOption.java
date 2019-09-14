package stevekung.mods.indicatia.gui.exconfig;

import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class ExtendedConfigOption
{
    private final String key;

    public ExtendedConfigOption(String key)
    {
        this.key = "extended_config." + key;
    }

    public String getDisplayPrefix()
    {
        return LangUtils.translate(this.key) + ": ";
    }

    public String getDisplayName()
    {
        return LangUtils.translate(this.key);
    }

    public abstract Widget createOptionButton(int x, int y, int width);
}