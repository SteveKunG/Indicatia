package stevekung.mods.indicatia.gui.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import stevekung.mods.indicatia.config.ExtendedConfig;

@Environment(EnvType.CLIENT)
class GuiTextFieldExtended extends TextFieldWidget
{
    private final ExtendedConfig.Options options;

    GuiTextFieldExtended(int x, int y, int width, ExtendedConfig.Options options)
    {
        super(MinecraftClient.getInstance().textRenderer, x, y, width, 20, "");
        this.options = options;
        this.setVisible(true);
        this.setMaxLength(13);
    }

    ExtendedConfig.Options getOption()
    {
        return this.options;
    }
}