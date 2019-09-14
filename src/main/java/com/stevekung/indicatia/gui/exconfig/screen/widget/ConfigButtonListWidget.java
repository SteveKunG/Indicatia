package com.stevekung.indicatia.gui.exconfig.screen.widget;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.stevekung.indicatia.gui.exconfig.ExtendedConfigOption;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConfigButtonListWidget extends AbstractOptionList<ConfigButtonListWidget.ButtonItem>
{
    public ConfigButtonListWidget(int x, int y, int top, int bottom, int itemHeight)
    {
        super(Minecraft.getInstance(), x, y, top, bottom, itemHeight);
        this.centerListVertically = false;
    }

    @Override
    public int getRowWidth()
    {
        return 400;
    }

    @Override
    protected int getScrollbarPosition()
    {
        return super.getScrollbarPosition() + 32;
    }

    public void addButton(ExtendedConfigOption config1, ExtendedConfigOption config2)
    {
        this.addEntry(ConfigButtonListWidget.ButtonItem.createItems(this.width, config1, config2));
    }

    public void addAll(ExtendedConfigOption[] config)
    {
        for (int i = 0; i < config.length; i += 2)
        {
            this.addButton(config[i], i < config.length - 1 ? config[i + 1] : null);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ButtonItem extends AbstractOptionList.Entry<ButtonItem>
    {
        private final List<Widget> buttons;

        private ButtonItem(List<Widget> list)
        {
            this.buttons = list;
        }

        @Override
        public void render(int index, int rowTop, int rowLeft, int rowWidth, int itemHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            for (Widget button : this.buttons)
            {
                button.y = rowTop;
                button.render(mouseX, mouseY, partialTicks);
            }
        }

        @Override
        public List<? extends IGuiEventListener> children()
        {
            return this.buttons;
        }

        public static ConfigButtonListWidget.ButtonItem createItems(int x, ExtendedConfigOption configOpt1, ExtendedConfigOption configOpt2)
        {
            Widget button = configOpt1.createOptionButton(x / 2 - 155, 0, 150);
            return configOpt2 == null ? new ConfigButtonListWidget.ButtonItem(ImmutableList.of(button)) : new ConfigButtonListWidget.ButtonItem(ImmutableList.of(button, configOpt2.createOptionButton(x / 2 - 155 + 160, 0, 150)));
        }
    }
}
