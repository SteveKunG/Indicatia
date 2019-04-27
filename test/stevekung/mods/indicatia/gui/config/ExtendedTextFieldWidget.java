package stevekung.mods.indicatia.gui.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import stevekung.mods.indicatia.config.ExtendedConfig;

@Environment(EnvType.CLIENT)
public class ExtendedTextFieldWidget extends TextFieldWidget
{
    private final ExtendedConfig.Options options;
    private int id;

    ExtendedTextFieldWidget(int id, int x, int y, int width, ExtendedConfig.Options options)
    {
        super(id, MinecraftClient.getInstance().fontRenderer, x, y, width, 20);
        this.id = id;
        this.options = options;
        this.setMaxLength(13);
    }

    ExtendedConfig.Options getOption()
    {
        return this.options;
    }

    public int getId()
    {
        return this.id;
    }
}