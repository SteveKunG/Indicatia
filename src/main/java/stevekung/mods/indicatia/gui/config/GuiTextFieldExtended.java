package stevekung.mods.indicatia.gui.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;

@OnlyIn(Dist.CLIENT)
class GuiTextFieldExtended extends TextFieldWidget
{
    private final ExtendedConfig options;
    private final TextFieldConfigOption textFieldOption;
    private String displayName;
    private String displayPrefix;

    public GuiTextFieldExtended(int x, int y, int width, ExtendedConfig config, TextFieldConfigOption textFieldOption)
    {
        super(Minecraft.getInstance().fontRenderer, x, y, width, 20, "");
        this.options = config;
        this.textFieldOption = textFieldOption;
        this.setText(textFieldOption.get(config));
        this.setVisible(true);
        this.setMaxStringLength(13);
    }

    public void setValue(String value)
    {
        this.textFieldOption.set(this.options, value);
        this.options.save();
    }

    public void setDisplayName(String name)
    {
        this.displayName = name;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public void setDisplayPrefix(String name)
    {
        this.displayPrefix = name;
    }

    public String getDisplayPrefix()
    {
        return this.displayPrefix;
    }
}