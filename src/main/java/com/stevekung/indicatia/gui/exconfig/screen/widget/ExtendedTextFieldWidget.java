package com.stevekung.indicatia.gui.exconfig.screen.widget;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.gui.exconfig.TextFieldConfigOption;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;

@Environment(EnvType.CLIENT)
public class ExtendedTextFieldWidget extends TextFieldWidget
{
    private final ExtendedConfig options;
    private final TextFieldConfigOption textFieldOption;
    private String displayName;
    private String displayPrefix;

    public ExtendedTextFieldWidget(int x, int y, int width, ExtendedConfig config, TextFieldConfigOption textFieldOption)
    {
        super(MinecraftClient.getInstance().textRenderer, x, y, width, 20, "");
        this.options = config;
        this.textFieldOption = textFieldOption;
        this.setText(textFieldOption.get(config));
        this.setVisible(true);
        this.setMaxLength(13);
    }

    @Override
    public void setFocused(boolean focused)
    {
        super.setFocused(focused);
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

    public int getHeight()
    {
        return this.height;
    }
}