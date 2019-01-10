package stevekung.mods.indicatia.gui.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.ColorUtils;

import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class GuiConfigTextFieldRowList extends GuiListExtended<GuiConfigTextFieldRowList.Row>
{
    private final GuiScreen parent;
    int selected = -1;

    GuiConfigTextFieldRowList(GuiScreen parent, int width, int height, int top, int bottom, int slotHeight, ExtendedConfig.Options[] options)
    {
        super(parent.mc, width, height, top, bottom, slotHeight);
        this.parent = parent;
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

            GuiConfigButton button = new GuiConfigButton(i, x, 0, 160, options, ExtendedConfig.instance.getKeyBinding(options))
            {
                @Override
                public void onClick(double mouseX, double mouseY)
                {
                    ExtendedConfig.instance.setOptionValue(options, 1);
                    this.displayString = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(this.id));
                    ExtendedConfig.save();
                }
            };

            return options.isBoolean() ? button : new GuiTextFieldExtended(i, x, 0, 80, options);
        }
    }

    //    @Override TODO
    //    protected void elementClicked(int slotIndex, int mouseX, int mouseY)
    //    {
    //        boolean flag = this.options.get(slotIndex).getTextField() != null && mouseX >= this.options.get(slotIndex).getTextField().x && mouseX < this.options.get(slotIndex).getTextField().x + this.options.get(slotIndex).getTextField().width && mouseY >= this.options.get(slotIndex).getTextField().y && mouseY < this.options.get(slotIndex).getTextField().y + this.options.get(slotIndex).getTextField().height;
    //
    //        if (flag)
    //        {
    //            this.selected = slotIndex;
    //        }
    //    }

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

    public void saveCurrentValue()
    {
        this.getChildren().forEach(row -> row.saveCurrentValue());
    }

    public void mouseClickedText(int mouseX, int mouseY, int mouseEvent)
    {
        this.getChildren().forEach(row -> row.mouseClicked(mouseX, mouseY, mouseEvent));
    }

    public void updateCursorCounter()
    {
        this.getChildren().forEach(row -> row.updateCursorCounter());
    }

    //    public void textboxKeyTyped(char typedChar, int keyCode)TODO
    //    {
    //        if (keyCode == 28)
    //        {
    //            this.saveCurrentValue();
    //            ExtendedConfig.save();
    //            this.mc.displayGuiScreen(this.parent);
    //        }
    //        this.options.forEach(row -> row.textboxKeyTyped(typedChar, keyCode));
    //    }

    @OnlyIn(Dist.CLIENT)
    public static class Row extends GuiListExtended.IGuiListEntry<Row>
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
            return this.gui != null && this.gui instanceof GuiConfigButton && ((GuiConfigButton)this.gui).mouseClicked(mouseX, mouseY, mouseEvent);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int mouseEvent)
        {
            return this.gui != null && this.gui instanceof GuiConfigButton && ((GuiConfigButton)this.gui).mouseReleased(mouseX, mouseY, mouseEvent);
        }

        @Override
        public void func_195000_a(float partialTicks) {} //TODO updatePosition

        public void saveCurrentValue()
        {
            if (this.gui != null && this.gui instanceof GuiTextFieldExtended)
            {
                GuiTextFieldExtended text = (GuiTextFieldExtended)this.gui;
                ExtendedConfig.instance.setOptionStringValue(text.getOption(), text.getText());
                ColorUtils.stringToRGB(text.getText(), true, text.getOption().getTranslation());
            }
        }

        public void mouseClicked(int mouseX, int mouseY, int mouseEvent)
        {
            if (this.gui != null && this.gui instanceof GuiTextFieldExtended)
            {
                GuiTextFieldExtended text = (GuiTextFieldExtended)this.gui;
                text.mouseClicked(mouseX, mouseY, mouseEvent);
            }
        }

        public void updateCursorCounter()
        {
            if (this.gui != null && this.gui instanceof GuiTextFieldExtended)
            {
                GuiTextFieldExtended text = (GuiTextFieldExtended)this.gui;
                text.tick();
            }
        }

        //        public void textboxKeyTyped(char typedChar, int keyCode)TODO
        //        {
        //            if (this.gui != null && this.gui instanceof GuiTextFieldExtended)
        //            {
        //                GuiTextFieldExtended text = (GuiTextFieldExtended)this.gui;
        //                text.textboxKeyTyped(typedChar, keyCode);
        //            }
        //        }

        public GuiTextFieldExtended getTextField()
        {
            if (this.gui instanceof GuiTextFieldExtended)
            {
                return (GuiTextFieldExtended) this.gui;
            }
            return null;
        }
    }
}