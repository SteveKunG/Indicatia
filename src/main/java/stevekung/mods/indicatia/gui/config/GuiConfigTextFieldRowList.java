package stevekung.mods.indicatia.gui.config;

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
import stevekung.mods.stevekungslib.utils.ColorUtils;

@OnlyIn(Dist.CLIENT)
public class GuiConfigTextFieldRowList extends AbstractOptionList<GuiConfigTextFieldRowList.Row>
{
    private final List<IGuiEventListener> textFields = new ArrayList<>();
    boolean selected = false;

    GuiConfigTextFieldRowList(int width, int height, int top, int bottom, int slotHeight)
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
        this.addEntry(GuiConfigTextFieldRowList.Row.createItems(ExtendedConfig.instance, this.width, config));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        this.children().stream().filter(row -> row.getTextField() != null).forEach(row ->
        {
            this.selected = mouseX >= row.getTextField().x && mouseX < row.getTextField().x + row.getTextField().getWidth() && mouseY >= row.getTextField().y && mouseY < row.getTextField().y + row.getTextField().getHeight();

            if (!row.getTextField().isFocused())
            {
                return;
            }
            row.getTextField().setFocused2(false);
        });
        return super.mouseClicked(mouseX, mouseY, button);
    }

    List<IGuiEventListener> getTextField()
    {
        return this.textFields;
    }

    void saveCurrentValue()
    {
        this.children().forEach(Row::saveCurrentValue);
    }

    void tick()
    {
        this.children().forEach(Row::tick);
    }

    void resize()
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

                if (button instanceof GuiTextFieldExtended)
                {
                    GuiTextFieldExtended textField = (GuiTextFieldExtended)button;
                    this.mc.fontRenderer.drawString(textField.getDisplayName(), rowLeft + 64, rowTop + 5, ColorUtils.rgbToDecimal(255, 255, 255));
                }
            }
        }

        @Override
        public List<? extends IGuiEventListener> children()
        {
            return this.buttons;
        }

        public static GuiConfigTextFieldRowList.Row createItems(ExtendedConfig config, int x, ExtendedConfigOption configOpt)
        {
            boolean isBoolean = configOpt instanceof BooleanConfigOption;
            int buttonX = isBoolean ? x / 2 - 80 : x / 2 + 40;
            return isBoolean ? new GuiConfigTextFieldRowList.Row(ImmutableList.of(configOpt.createOptionButton(config, buttonX, 0, 150))) : new GuiConfigTextFieldRowList.Row(ImmutableList.of(configOpt.createOptionButton(config, buttonX, 0, 80)));
        }

        GuiTextFieldExtended getTextField()
        {
            for (Widget widget : this.buttons)
            {
                if (widget instanceof GuiTextFieldExtended)
                {
                    return (GuiTextFieldExtended)widget;
                }
            }
            return null;
        }

        void saveCurrentValue()
        {
            if (this.getTextField() != null)
            {
                GuiTextFieldExtended text = this.getTextField();
                text.setValue(text.getText());
                ColorUtils.stringToRGB(text.getText(), false, text.getDisplayName());
            }
        }

        void tick()
        {
            if (this.getTextField() != null)
            {
                GuiTextFieldExtended text = this.getTextField();
                text.tick();
            }
        }

        void resize()
        {
            if (this.getTextField() != null)
            {
                GuiTextFieldExtended text = this.getTextField();
                String textTemp = text.getText();
                text.setText(textTemp);
            }
        }
    }
}