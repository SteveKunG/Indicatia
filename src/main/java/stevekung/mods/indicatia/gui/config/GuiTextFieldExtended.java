package stevekung.mods.indicatia.gui.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;

@OnlyIn(Dist.CLIENT)
class GuiTextFieldExtended extends TextFieldWidget
{
    private final ExtendedConfig.Options options;

    GuiTextFieldExtended(int x, int y, int width, ExtendedConfig.Options options)
    {
        super(Minecraft.getInstance().fontRenderer, x, y, width, 20, "");
        this.options = options;
        this.setVisible(true);
        this.setMaxStringLength(13);
    }

    ExtendedConfig.Options getOption()
    {
        return this.options;
    }
}