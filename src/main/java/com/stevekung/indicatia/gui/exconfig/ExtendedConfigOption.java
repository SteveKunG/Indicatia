package com.stevekung.indicatia.gui.exconfig;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.AbstractButtonWidget;

@Environment(EnvType.CLIENT)
public abstract class ExtendedConfigOption
{
    private final String key;

    public ExtendedConfigOption(String key)
    {
        this.key = "extended_config." + key;
    }

    public abstract AbstractButtonWidget createOptionButton(ExtendedConfig options, int x, int y, int width);

    public String getDisplayName()
    {
        return LangUtils.translate(this.key);
    }

    public String getDisplayPrefix()
    {
        return LangUtils.translate(this.key) + ": ";
    }
}