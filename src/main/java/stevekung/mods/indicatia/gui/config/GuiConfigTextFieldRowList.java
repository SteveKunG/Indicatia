package stevekung.mods.indicatia.gui.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ElementListWidget;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekungslib.utils.ColorUtils;

@Environment(EnvType.CLIENT)
public class GuiConfigTextFieldRowList extends ElementListWidget<GuiConfigTextFieldRowList.Row>
{
    private final List<Element> textFields = new ArrayList<>();
    int selected = -1;

    GuiConfigTextFieldRowList(int width, int height, int top, int bottom, int slotHeight, ExtendedConfig.Options[] options)
    {
        super(MinecraftClient.getInstance(), width, height, top, bottom, slotHeight);
        this.centerListVertically = false;

        Arrays.stream(options).forEach(option ->
        {
            int buttonWidth = option.isBoolean() ? width / 2 - 80 : this.width / 2 + 40;
            Element gui = this.createButton(buttonWidth, option);
            this.addEntry(new GuiConfigTextFieldRowList.Row(option.getTranslation(), gui));
        });
    }

    private Element createButton(int x, ExtendedConfig.Options options)
    {
        if (options == null)
        {
            return null;
        }
        else
        {
            int i = options.getOrdinal();
            GuiTextFieldExtended textField = new GuiTextFieldExtended(i, x, 80, options);

            GuiConfigButton button = new GuiConfigButton(x, 0, 160, ExtendedConfig.instance.getKeyBinding(options))
            {
                @Override
                public void onClick(double mouseX, double mouseY)
                {
                    ExtendedConfig.instance.setOptionValue(options, 1);
                    this.setMessage(ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(i)));
                    ExtendedConfig.save();
                }
            };

            if (!options.isBoolean())
            {
                this.textFields.add(textField);
            }
            return options.isBoolean() ? button : textField;
        }
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        //TODO
        /*boolean flag = this.children().get(this.getSelected()).getTextField() != null && mouseX >= this.children().get(index).getTextField().x && mouseX < this.children().get(index).getTextField().x + this.children().get(index).getTextField().width && mouseY >= this.children().get(index).getTextField().y && mouseY < this.children().get(index).getTextField().y + this.children().get(index).getTextField().height;
        this.selected = flag ? index : -1;

        this.children().stream().filter(row -> row.getTextField() != null).forEach(row ->
        {
            if (!row.getTextField().isFocused())
            {
                return;
            }
            row.getTextField().setFocused(false);
        });*/
        return super.mouseClicked(mouseX, mouseY, button);
    }

    List<Element> getTextField()
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

    void onResize()
    {
        this.children().forEach(Row::onResize);
    }

    void keyPressedText(int keyCode, int scanCode, int modifiers)
    {
        this.children().forEach(row -> row.keyPressedText(keyCode, scanCode, modifiers));
    }

    void charTypedText(char codePoint, int modifiers)
    {
        this.children().forEach(row -> row.charTypedText(codePoint, modifiers));
    }

    @Environment(EnvType.CLIENT)
    public class Row extends ElementListWidget.Entry<Row>
    {
        private final MinecraftClient mc = MinecraftClient.getInstance();
        private final Element gui;
        private final String name;

        Row(String name, Element gui)
        {
            this.gui = gui;
            this.name = name;

            if (this.gui instanceof GuiTextFieldExtended)
            {
                GuiTextFieldExtended text = (GuiTextFieldExtended)this.gui;
                text.setText(ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(text.getOption().getOrdinal())));
            }
        }

        @Override
        public void render(int mouseX, int mouseY, int x, int y, int var5, int var6, int var7, boolean isSelected, float partialTicks)
        {
            if (this.gui != null)
            {
                if (this.gui instanceof GuiTextFieldExtended)
                {
                    GuiTextFieldExtended text = (GuiTextFieldExtended)this.gui;
                    text.y = y;
                    text.render(mouseX, mouseY, partialTicks);
                    this.mc.textRenderer.draw(this.name, x + 64, y + 5, ColorUtils.rgbToDecimal(255, 255, 255));
                }
                if (this.gui instanceof GuiConfigButton)
                {
                    GuiConfigButton button = (GuiConfigButton)this.gui;
                    button.y = y;
                    button.render(mouseX, mouseY, partialTicks);
                }
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int mouseEvent)
        {
            return this.gui != null && this.gui instanceof GuiConfigButton && ((GuiConfigButton)this.gui).mouseClicked(mouseX, mouseY, mouseEvent) || this.getTextField() != null && this.getTextField().mouseClicked(mouseX, mouseY, mouseEvent);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int mouseEvent)
        {
            return this.gui != null && this.gui instanceof GuiConfigButton && ((GuiConfigButton)this.gui).mouseReleased(mouseX, mouseY, mouseEvent);
        }

        GuiTextFieldExtended getTextField()
        {
            if (this.gui instanceof GuiTextFieldExtended)
            {
                return (GuiTextFieldExtended) this.gui;
            }
            return null;
        }

        void saveCurrentValue()
        {
            if (this.getTextField() != null)
            {
                GuiTextFieldExtended text = this.getTextField();
                ExtendedConfig.instance.setOptionStringValue(text.getOption(), text.getText());
                ColorUtils.stringToRGB(text.getText(), false, text.getOption().getTranslation());
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

        void onResize()
        {
            if (this.getTextField() != null)
            {
                GuiTextFieldExtended text = this.getTextField();
                String textTemp = text.getText();
                text.setText(textTemp);
            }
        }

        void keyPressedText(int keyCode, int scanCode, int modifiers)
        {
            if (this.getTextField() != null)
            {
                GuiTextFieldExtended text = this.getTextField();
                text.keyPressed(keyCode, scanCode, modifiers);
            }
        }

        void charTypedText(char codePoint, int modifiers)
        {
            if (this.getTextField() != null)
            {
                GuiTextFieldExtended text = this.getTextField();
                text.charTyped(codePoint, modifiers);
            }
        }

        @Override
        public List<? extends Element> children()
        {
            // TODO Auto-generated method stub
            return null;
        }
    }
}