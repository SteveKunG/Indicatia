package stevekung.mods.indicatia.gui.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.ColorUtils;

@SideOnly(Side.CLIENT)
public class GuiConfigTextFieldRowList extends GuiListExtended
{
    private final List<GuiConfigTextFieldRowList.Row> options = new ArrayList<>();
    private final GuiScreen parent;
    public int selected = -1;

    public GuiConfigTextFieldRowList(GuiScreen parent, int width, int height, int top, int bottom, int slotHeight, ExtendedConfig.Options[] options)
    {
        super(parent.mc, width, height, top, bottom, slotHeight);
        this.parent = parent;
        this.centerListVertically = false;

        Arrays.stream(options).forEach(option ->
        {
            int buttonWidth = option.isBoolean() ? width / 2 - 80 : this.width / 2 + 40;
            Gui gui = this.createButton(buttonWidth, 0, option);
            this.options.add(new GuiConfigTextFieldRowList.Row(option.getTranslation(), gui));
        });
    }

    private Gui createButton(int x, int y, ExtendedConfig.Options options)
    {
        if (options == null)
        {
            return null;
        }
        else
        {
            int i = options.getOrdinal();
            return options.isBoolean() ? new GuiConfigButton(i, x, y, 160, options, ExtendedConfig.instance.getKeyBinding(options)) : new GuiTextFieldExtended(i, x, y, 80, options);
        }
    }

    @Override
    public GuiConfigTextFieldRowList.Row getListEntry(int index)
    {
        return this.options.get(index);
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
    {
        boolean flag = this.options.get(slotIndex).getTextField() != null && mouseX >= this.options.get(slotIndex).getTextField().x && mouseX < this.options.get(slotIndex).getTextField().x + this.options.get(slotIndex).getTextField().width && mouseY >= this.options.get(slotIndex).getTextField().y && mouseY < this.options.get(slotIndex).getTextField().y + this.options.get(slotIndex).getTextField().height;

        if (flag)
        {
            this.selected = slotIndex;
        }
    }

    @Override
    protected int getSize()
    {
        return this.options.size();
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

    public void saveCurrentValue()
    {
        this.options.forEach(row -> row.saveCurrentValue());
    }

    public void mouseClickedText(int mouseX, int mouseY, int mouseEvent)
    {
        this.options.forEach(row -> row.mouseClicked(mouseX, mouseY, mouseEvent));
    }

    public void updateCursorCounter()
    {
        this.options.forEach(row -> row.updateCursorCounter());
    }

    public void textboxKeyTyped(char typedChar, int keyCode)
    {
        if (keyCode == 28)
        {
            this.saveCurrentValue();
            ExtendedConfig.instance.save();
            this.mc.displayGuiScreen(this.parent);
        }
        this.options.forEach(row -> row.textboxKeyTyped(typedChar, keyCode));
    }

    @SideOnly(Side.CLIENT)
    public static class Row implements GuiListExtended.IGuiListEntry
    {
        private final Minecraft mc = Minecraft.getMinecraft();
        private final Gui gui;
        private final String name;

        public Row(String name, Gui gui)
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
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            if (this.gui != null)
            {
                if (this.gui instanceof GuiTextFieldExtended)
                {
                    GuiTextFieldExtended text = (GuiTextFieldExtended)this.gui;
                    text.y = y;
                    text.drawTextBox();
                    this.mc.fontRenderer.drawString(this.name, x + 64, y + 5, ColorUtils.rgbToDecimal(255, 255, 255));
                }
                if (this.gui instanceof GuiConfigButton)
                {
                    GuiConfigButton button = (GuiConfigButton)this.gui;
                    button.y = y;
                    button.drawButton(this.mc, mouseX, mouseY, partialTicks);
                }
            }
        }

        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.gui != null && this.gui instanceof GuiConfigButton)
            {
                GuiConfigButton button = (GuiConfigButton)this.gui;

                if (button.mousePressed(this.mc, mouseX, mouseY))
                {
                    if (mouseEvent == 0)
                    {
                        ExtendedConfig.instance.setOptionValue(button.getOption(), 1);
                        button.displayString = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(button.id));
                        button.playPressSound(this.mc.getSoundHandler());
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.gui != null && this.gui instanceof GuiConfigButton)
            {
                GuiConfigButton button = (GuiConfigButton)this.gui;
                button.mouseReleased(x, y);
            }
        }

        @Override
        public void updatePosition(int slotIndex, int x, int y, float partialTicks) {}

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
                text.updateCursorCounter();
            }
        }

        public void textboxKeyTyped(char typedChar, int keyCode)
        {
            if (this.gui != null && this.gui instanceof GuiTextFieldExtended)
            {
                GuiTextFieldExtended text = (GuiTextFieldExtended)this.gui;
                text.textboxKeyTyped(typedChar, keyCode);
            }
        }

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