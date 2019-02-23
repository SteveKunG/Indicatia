package stevekung.mods.indicatia.gui.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekungslib.utils.ColorUtils;

@OnlyIn(Dist.CLIENT)
public class GuiConfigTextFieldRowList extends GuiListExtended<GuiConfigTextFieldRowList.Row>
{
    private final List<IGuiEventListener> textFields = new ArrayList<>();
    int selected = -1;

    GuiConfigTextFieldRowList(int width, int height, int top, int bottom, int slotHeight, ExtendedConfig.Options[] options)
    {
        super(Minecraft.getInstance(), width, height, top, bottom, slotHeight);
        this.centerListVertically = false;

        Arrays.stream(options).forEach(option ->
        {
            int buttonWidth = option.isBoolean() ? width / 2 - 80 : this.width / 2 + 40;
            Gui gui = this.createButton(buttonWidth, option);
            this.addEntry(new GuiConfigTextFieldRowList.Row(option.getTranslation(), gui));
        });
    }

    private Gui createButton(int x, ExtendedConfig.Options options)
    {
        if (options == null)
        {
            return null;
        }
        else
        {
            int i = options.getOrdinal();
            GuiTextFieldExtended textField = new GuiTextFieldExtended(i, x, 0, 80, options);

            GuiConfigButton button = new GuiConfigButton(i, x, 0, 160, ExtendedConfig.instance.getKeyBinding(options))
            {
                @Override
                public void onClick(double mouseX, double mouseY)
                {
                    ExtendedConfig.instance.setOptionValue(options, 1);
                    this.displayString = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(this.id));
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
    public int getListWidth()
    {
        return 400;
    }

    @Override
    protected int getScrollBarX()
    {
        return super.getScrollBarX() + 40;
    }

    @Override
    protected boolean mouseClicked(int index, int button, double mouseX, double mouseY)
    {
        boolean flag = this.getChildren().get(index).getTextField() != null && mouseX >= this.getChildren().get(index).getTextField().x && mouseX < this.getChildren().get(index).getTextField().x + this.getChildren().get(index).getTextField().width && mouseY >= this.getChildren().get(index).getTextField().y && mouseY < this.getChildren().get(index).getTextField().y + this.getChildren().get(index).getTextField().height;
        this.selected = flag ? index : -1;

        this.getChildren().stream().filter(row -> row.getTextField() != null).forEach(row ->
        {
            if (!row.getTextField().isFocused())
            {
                return;
            }
            row.getTextField().setFocused(false);
        });
        return super.mouseClicked(index, button, mouseX, mouseY);
    }

    List<IGuiEventListener> getTextField()
    {
        return this.textFields;
    }

    void saveCurrentValue()
    {
        this.getChildren().forEach(Row::saveCurrentValue);
    }

    void tick()
    {
        this.getChildren().forEach(Row::tick);
    }

    void onResize()
    {
        this.getChildren().forEach(Row::onResize);
    }

    void keyPressedText(int keyCode, int scanCode, int modifiers)
    {
        this.getChildren().forEach(row -> row.keyPressedText(keyCode, scanCode, modifiers));
    }

    void charTypedText(char codePoint, int modifiers)
    {
        this.getChildren().forEach(row -> row.charTypedText(codePoint, modifiers));
    }

    @OnlyIn(Dist.CLIENT)
    public class Row extends GuiListExtended.IGuiListEntry<Row>
    {
        private final Minecraft mc = Minecraft.getInstance();
        private final Gui gui;
        private final String name;

        Row(String name, Gui gui)
        {
            this.gui = gui;
            this.name = name;

            if (this.gui instanceof GuiTextFieldExtended)
            {
                GuiTextFieldExtended text = (GuiTextFieldExtended)this.gui;
                text.setText(ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(text.getId())));
            }
        }

        @Override
        public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            if (this.gui != null)
            {
                if (this.gui instanceof GuiTextFieldExtended)
                {
                    GuiTextFieldExtended text = (GuiTextFieldExtended)this.gui;
                    text.y = this.getY();
                    text.drawTextField(mouseX, mouseY, partialTicks);
                    this.mc.fontRenderer.drawString(this.name, this.getX() + 64, this.getY() + 5, ColorUtils.rgbToDecimal(255, 255, 255));
                }
                if (this.gui instanceof GuiConfigButton)
                {
                    GuiConfigButton button = (GuiConfigButton)this.gui;
                    button.y = this.getY();
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

        @Override
        public void func_195000_a(float partialTicks) {} //TODO updatePosition

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
    }
}