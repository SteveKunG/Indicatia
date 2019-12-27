package com.stevekung.indicatia.gui.exconfig.screen.widget;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.stevekung.indicatia.gui.exconfig.BooleanConfigOption;
import com.stevekung.indicatia.gui.exconfig.ExtendedConfigOption;
import com.stevekung.stevekungslib.utils.ColorUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.list.AbstractOptionList;

public class ConfigTextFieldWidgetList extends AbstractOptionList<ConfigTextFieldWidgetList.Row>
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

    public void addButton(ExtendedConfigOption config)
    {
        this.addEntry(ConfigTextFieldWidgetList.Row.createItems(this.width, config));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (this.getFocused() != null && this.getFocused().getTextField() != null)
        {
            ExtendedTextFieldWidget text = this.getFocused().getTextField();
            this.selected = mouseX >= text.x && mouseX < text.x + text.getWidth() && mouseY >= text.y && mouseY < text.y + text.getHeight();
            text.setFocused2(false);
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

    public static class Row extends AbstractOptionList.Entry<Row>
    {
        private final List<ExtendedTextFieldWidget> textFields;

        private Row(List<ExtendedTextFieldWidget> list)
        {
            this.textFields = list;
        }

        @Override
        public void render(int index, int rowTop, int rowLeft, int rowWidth, int itemHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            for (ExtendedTextFieldWidget textField : this.textFields)
            {
                textField.y = rowTop;
                textField.render(mouseX, mouseY, partialTicks);
                Minecraft.getInstance().fontRenderer.drawString(textField.getDisplayName(), rowLeft + 64, rowTop + 5, ColorUtils.rgbToDecimal(255, 255, 255));
            }
        }

        @Override
        public List<? extends IGuiEventListener> children()
        {
            return this.textFields;
        }

        public static ConfigTextFieldWidgetList.Row createItems(int x, ExtendedConfigOption configOpt)
        {
            boolean isBoolean = configOpt instanceof BooleanConfigOption;
            int buttonX = isBoolean ? x / 2 - 80 : x / 2 + 40;
            return isBoolean ? new ConfigTextFieldWidgetList.Row(ImmutableList.of((ExtendedTextFieldWidget)configOpt.createOptionButton(buttonX, 0, 150))) : new ConfigTextFieldWidgetList.Row(ImmutableList.of((ExtendedTextFieldWidget)configOpt.createOptionButton(buttonX, 0, 80)));
        }

        public ExtendedTextFieldWidget getTextField()
        {
            if ((ExtendedTextFieldWidget)this.getFocused() != null)
            {
                return (ExtendedTextFieldWidget)this.getFocused();
            }
            return null;
        }

        void saveCurrentValue()
        {
            if (this.getTextField() != null)
            {
                ExtendedTextFieldWidget text = this.getTextField();
                text.setValue(text.getText());
                ColorUtils.stringToRGB(text.getText(), false, text.getDisplayName());
            }
        }

        void tick()
        {
            if (this.getTextField() != null)
            {
                ExtendedTextFieldWidget text = this.getTextField();
                text.tick();
            }
        }

        void resize()
        {
            if (this.getTextField() != null)
            {
                ExtendedTextFieldWidget text = this.getTextField();
                String textTemp = text.getText();
                text.setText(textTemp);
            }
        }
    }
}