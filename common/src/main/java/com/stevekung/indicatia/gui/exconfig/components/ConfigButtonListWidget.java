package com.stevekung.indicatia.gui.exconfig.components;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.stevekunglib.utils.config.AbstractSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

public class ConfigButtonListWidget extends ContainerObjectSelectionList<ConfigButtonListWidget.ButtonItem>
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

    public void addButton(AbstractSettings<IndicatiaSettings> config1, AbstractSettings<IndicatiaSettings> config2)
    {
        this.addEntry(ConfigButtonListWidget.ButtonItem.createItems(this.width, config1, config2));
    }

    public void addAll(List<AbstractSettings<IndicatiaSettings>> config)
    {
        for (var i = 0; i < config.size(); i += 2)
        {
            this.addButton(config.get(i), i < config.size() - 1 ? config.get(i + 1) : null);
        }
    }

    public static class ButtonItem extends ContainerObjectSelectionList.Entry<ButtonItem>
    {
        private final List<AbstractWidget> buttons;

        private ButtonItem(List<AbstractWidget> list)
        {
            this.buttons = list;
        }

        @Override
        public void render(PoseStack poseStack, int index, int rowTop, int rowLeft, int rowWidth, int itemHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            for (var button : this.buttons)
            {
                button.y = rowTop;
                button.render(poseStack, mouseX, mouseY, partialTicks);
            }
        }

        @Override
        public List<? extends GuiEventListener> children()
        {
            return this.buttons;
        }

        @Override
        public List<? extends NarratableEntry> narratables()
        {
            return this.buttons;
        }

        public static ConfigButtonListWidget.ButtonItem createItems(int x, AbstractSettings<IndicatiaSettings> configOpt1, AbstractSettings<IndicatiaSettings> configOpt2)
        {
            var button = configOpt1.createWidget(IndicatiaSettings.INSTANCE, x / 2 - 155, 0, 150);
            return configOpt2 == null ? new ConfigButtonListWidget.ButtonItem(ImmutableList.of(button)) : new ConfigButtonListWidget.ButtonItem(ImmutableList.of(button, configOpt2.createWidget(IndicatiaSettings.INSTANCE, x / 2 - 155 + 160, 0, 150)));
        }
    }
}