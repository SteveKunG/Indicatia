package stevekung.mods.indicatia.gui.exconfig.screen.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.exconfig.BooleanConfigOption;
import stevekung.mods.indicatia.gui.exconfig.ExtendedConfigOption;
import stevekung.mods.stevekungslib.utils.ColorUtils;

@OnlyIn(Dist.CLIENT)
public class ConfigTextFieldWidgetList extends AbstractOptionList<ConfigTextFieldWidgetList.Row>
{
    private final List<IGuiEventListener> textFields = new ArrayList<>();
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
        this.addEntry(ConfigTextFieldWidgetList.Row.createItems(ExtendedConfig.instance, this.width, config));
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

    List<IGuiEventListener> getTextField()
    {
        return this.textFields;
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

    @OnlyIn(Dist.CLIENT)
    public static class Row extends AbstractOptionList.Entry<Row>
    {
        private final Minecraft mc = Minecraft.getInstance();
        private final List<Widget> buttons;

        private Row(List<Widget> list)
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

                if (button instanceof ExtendedTextFieldWidget)
                {
                    ExtendedTextFieldWidget textField = (ExtendedTextFieldWidget)button;
                    this.mc.fontRenderer.drawString(textField.getDisplayName(), rowLeft + 64, rowTop + 5, ColorUtils.rgbToDecimal(255, 255, 255));
                }
            }
        }

        @Override
        public List<? extends IGuiEventListener> children()
        {
            return this.buttons;
        }

        public static ConfigTextFieldWidgetList.Row createItems(ExtendedConfig config, int x, ExtendedConfigOption configOpt)
        {
            boolean isBoolean = configOpt instanceof BooleanConfigOption;
            int buttonX = isBoolean ? x / 2 - 80 : x / 2 + 40;
            return isBoolean ? new ConfigTextFieldWidgetList.Row(ImmutableList.of(configOpt.createOptionButton(config, buttonX, 0, 150))) : new ConfigTextFieldWidgetList.Row(ImmutableList.of(configOpt.createOptionButton(config, buttonX, 0, 80)));
        }

        public ExtendedTextFieldWidget getTextField()
        {
            for (Widget widget : this.buttons)
            {
                if (widget instanceof ExtendedTextFieldWidget)
                {
                    return (ExtendedTextFieldWidget)widget;
                }
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