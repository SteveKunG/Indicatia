package com.stevekung.indicatia.gui.exconfig.components;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.config.AbstractSettings;
import com.stevekung.stevekungslib.utils.config.TextFieldSettingsWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;

public class ConfigTextFieldWidgetList extends ContainerObjectSelectionList<ConfigTextFieldWidgetList.Row>
{
    public boolean selected = false;

    public ConfigTextFieldWidgetList(int width, int height, int top, int bottom, int slotHeight)
    {
        super(Minecraft.getInstance(), width, height, top, bottom, slotHeight);
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
        return super.getScrollbarPosition() + 40;
    }

    public void addButton(AbstractSettings<IndicatiaSettings> config)
    {
        this.addEntry(ConfigTextFieldWidgetList.Row.createItems(this.width, config));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (this.getSelected() != null && this.getSelected().getTextField() != null)
        {
            TextFieldSettingsWidget<IndicatiaSettings> text = this.getSelected().getTextField();
            this.selected = mouseX >= text.x && mouseX < text.x + text.getWidth() && mouseY >= text.y && mouseY < text.y + text.getHeight();
            text.setFocus(false);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void saveCurrentValue()
    {
        this.children().forEach(Row::saveCurrentValue);
    }

    public void tick()
    {
        this.children().forEach(Row::tick);
    }

    public void resize()
    {
        this.children().forEach(Row::resize);
    }

    public static class Row extends ContainerObjectSelectionList.Entry<Row>
    {
        private final List<TextFieldSettingsWidget<IndicatiaSettings>> textFields;

        private Row(List<TextFieldSettingsWidget<IndicatiaSettings>> list)
        {
            this.textFields = list;
        }

        @Override
        public void render(PoseStack matrixStack, int index, int rowTop, int rowLeft, int rowWidth, int itemHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            for (TextFieldSettingsWidget<IndicatiaSettings> textField : this.textFields)
            {
                textField.y = rowTop;
                textField.render(matrixStack, mouseX, mouseY, partialTicks);
                Minecraft.getInstance().font.draw(matrixStack, textField.getDisplayName(), rowLeft + 64, rowTop + 5, ColorUtils.toDecimal(255, 255, 255));
            }
        }

        @Override
        public List<? extends GuiEventListener> children()
        {
            return this.textFields;
        }

        @SuppressWarnings("unchecked")
        public static ConfigTextFieldWidgetList.Row createItems(int x, AbstractSettings<IndicatiaSettings> configOpt)
        {
            return new ConfigTextFieldWidgetList.Row(ImmutableList.of((TextFieldSettingsWidget<IndicatiaSettings>)configOpt.createWidget(IndicatiaSettings.INSTANCE, x / 2 + 40, 0, 80)));
        }

        @SuppressWarnings("unchecked")
        public TextFieldSettingsWidget<IndicatiaSettings> getTextField()
        {
            if (this.getFocused() != null)
            {
                return (TextFieldSettingsWidget<IndicatiaSettings>)this.getFocused();
            }
            return null;
        }

        void saveCurrentValue()
        {
            if (this.getTextField() != null)
            {
                TextFieldSettingsWidget<IndicatiaSettings> text = this.getTextField();
                text.setValue(IndicatiaSettings.INSTANCE, text.getValue());
            }
        }

        void tick()
        {
            if (this.getTextField() != null)
            {
                TextFieldSettingsWidget<IndicatiaSettings> text = this.getTextField();
                text.tick();
            }
        }

        void resize()
        {
            if (this.getTextField() != null)
            {
                TextFieldSettingsWidget<IndicatiaSettings> text = this.getTextField();
                String textTemp = text.getValue();
                text.setValue(textTemp);
            }
        }
    }
}