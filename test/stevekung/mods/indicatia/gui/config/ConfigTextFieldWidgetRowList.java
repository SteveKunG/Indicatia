package stevekung.mods.indicatia.gui.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.widget.EntryListWidget;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.ColorUtils;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class ConfigTextFieldWidgetRowList extends EntryListWidget<ConfigTextFieldWidgetRowList.Row>
{
    private final Gui parent;
    public int selected = -1;

    public ConfigTextFieldWidgetRowList(Gui parent, int width, int height, int top, int bottom, int slotHeight, ExtendedConfig.Options[] options)
    {
        super(MinecraftClient.getInstance(), width, height, top, bottom, slotHeight);
        this.parent = parent;
        this.field_2173 = false;//centerListVertically

        Arrays.stream(options).forEach(option ->
        {
            int buttonWidth = option.isBoolean() ? width / 2 - 80 : this.width / 2 + 40;
            Drawable gui = this.createButton(buttonWidth, 0, option);
            this.addEntry(new ConfigTextFieldWidgetRowList.Row(option.getTranslation(), gui));
        });
    }

    private Drawable createButton(int x, int y, ExtendedConfig.Options options)
    {
        if (options == null)
        {
            return null;
        }
        else
        {
            int i = options.getOrdinal();
            return options.isBoolean() ? new ConfigButtonWidget(i, x, y, 160, options, ExtendedConfig.instance.getKeyBinding(options)) : new ExtendedTextFieldWidget(i, x, y, 80, options);
        }
    }

    //    @Override TODO
    //    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
    //    {
    //        boolean flag = this.options.get(slotIndex).getTextField() != null && mouseX >= this.options.get(slotIndex).getTextField().x && mouseX < this.options.get(slotIndex).getTextField().x + this.options.get(slotIndex).getTextField().width && mouseY >= this.options.get(slotIndex).getTextField().y && mouseY < this.options.get(slotIndex).getTextField().y + this.options.get(slotIndex).getTextField().height;
    //
    //        if (flag)
    //        {
    //            this.selected = slotIndex;
    //        }
    //    }

    @Override
    public int getEntryWidth()
    {
        return 400;
    }

    @Override
    protected int getScrollbarPosition()
    {
        return super.getScrollbarPosition() + 40;
    }

    public void saveCurrentValue()
    {
        this.getEntries().forEach(row -> row.saveCurrentValue());
    }

    public void mouseClickedText(double mouseX, double mouseY, int mouseEvent)
    {
        this.getEntries().forEach(row -> row.mouseClicked(mouseX, mouseY, mouseEvent));
    }

    public void updateCursorCounter()
    {
        this.getEntries().forEach(row -> row.updateCursorCounter());
    }

    public void textboxKeyTyped(char typedChar, int keyCode)
    {
        if (keyCode == 28)
        {
            this.saveCurrentValue();
            ExtendedConfig.save();
            this.client.openGui(this.parent);
        }
        this.getEntries().forEach(row -> row.textboxKeyTyped(typedChar, keyCode));
    }

    @Environment(EnvType.CLIENT)
    public static class Row extends EntryListWidget.Entry<Row>
    {
        private final MinecraftClient mc = MinecraftClient.getInstance();
        private final Drawable drawable;
        private final String name;

        public Row(String name, Drawable drawable)
        {
            this.drawable = drawable;
            this.name = name;

            if (this.drawable instanceof ExtendedTextFieldWidget)
            {
                ExtendedTextFieldWidget text = (ExtendedTextFieldWidget)this.drawable;
                text.setText(ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(text.getId())));
            }
        }

        @Override
        public void draw(int slotIndex, int listWidth, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            if (this.drawable != null)
            {
                if (this.drawable instanceof ExtendedTextFieldWidget)
                {
                    ExtendedTextFieldWidget text = (ExtendedTextFieldWidget)this.drawable;
                    //text.y = this.getY();TODO
                    //text.drawTextBox();
                    this.mc.fontRenderer.draw(this.name, this.getX() + 64, this.getY() + 5, ColorUtils.rgbToDecimal(255, 255, 255));
                }
                if (this.drawable instanceof ConfigButtonWidget)
                {
                    ConfigButtonWidget button = (ConfigButtonWidget)this.drawable;
                    button.y = this.getY();
                    button.draw(mouseX, mouseY, partialTicks);
                }
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int mouseEvent)
        {
            if (this.drawable != null && this.drawable instanceof ConfigButtonWidget)
            {
                ConfigButtonWidget button = (ConfigButtonWidget)this.drawable;

                if (button.mouseClicked(mouseX, mouseY, mouseEvent))
                {
                    if (mouseEvent == 0)
                    {
                        ExtendedConfig.instance.setOptionValue(button.getOption(), 1);
                        button.text = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(button.id));
                        button.playPressedSound(this.mc.getSoundLoader());
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean mouseReleased(double x, double y, int mouseEvent)
        {
            if (this.drawable != null && this.drawable instanceof ConfigButtonWidget)
            {
                ConfigButtonWidget button = (ConfigButtonWidget)this.drawable;
                return button.mouseReleased(x, y, mouseEvent);
            }
            return false;
        }

        @Override
        public void method_1904(float partialTicks) {}//updatePosition

        public void saveCurrentValue()
        {
            if (this.drawable != null && this.drawable instanceof ExtendedTextFieldWidget)
            {
                ExtendedTextFieldWidget text = (ExtendedTextFieldWidget)this.drawable;
                ExtendedConfig.instance.setOptionStringValue(text.getOption(), text.getText());
                ColorUtils.stringToRGB(text.getText(), true, text.getOption().getTranslation());
            }
        }

        public void mouseClicked(int mouseX, int mouseY, int mouseEvent)
        {
            if (this.drawable != null && this.drawable instanceof ExtendedTextFieldWidget)
            {
                ExtendedTextFieldWidget text = (ExtendedTextFieldWidget)this.drawable;
                text.mouseClicked(mouseX, mouseY, mouseEvent);
            }
        }

        public void updateCursorCounter()
        {
            if (this.drawable != null && this.drawable instanceof ExtendedTextFieldWidget)
            {
                ExtendedTextFieldWidget text = (ExtendedTextFieldWidget)this.drawable;
                text.tick();
            }
        }

        public void textboxKeyTyped(char typedChar, int keyCode)
        {
            if (this.drawable != null && this.drawable instanceof ExtendedTextFieldWidget)
            {
                ExtendedTextFieldWidget text = (ExtendedTextFieldWidget)this.drawable;
                text.charTyped(typedChar, keyCode);
            }
        }

        public ExtendedTextFieldWidget getTextField()
        {
            if (this.drawable instanceof ExtendedTextFieldWidget)
            {
                return (ExtendedTextFieldWidget) this.drawable;
            }
            return null;
        }
    }
}