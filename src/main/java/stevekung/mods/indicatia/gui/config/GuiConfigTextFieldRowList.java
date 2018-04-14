package stevekung.mods.indicatia.gui.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.config.ExtendedConfig.Options;
import stevekung.mods.stevekunglib.util.ColorUtils;

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

        for (Options exoptions : options)
        {
            GuiTextFieldExtended button = this.createButton(this.width / 2 + 40, 0, exoptions);
            this.options.add(new GuiConfigTextFieldRowList.Row(exoptions.getTranslation(), button));
        }
    }

    private GuiTextFieldExtended createButton(int x, int y, ExtendedConfig.Options options)
    {
        if (options == null)
        {
            return null;
        }
        else
        {
            return new GuiTextFieldExtended(options.getOrdinal(), x, y, 80, options);
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
        boolean flag = mouseX >= this.options.get(slotIndex).getTextField().x && mouseX < this.options.get(slotIndex).getTextField().x + this.options.get(slotIndex).getTextField().width && mouseY >= this.options.get(slotIndex).getTextField().y && mouseY < this.options.get(slotIndex).getTextField().y + this.options.get(slotIndex).getTextField().height;

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
        for (Row row : this.options)
        {
            row.saveCurrentValue();
        }
    }

    public void mouseClickedText(int mouseX, int mouseY, int mouseEvent)
    {
        for (Row row : this.options)
        {
            row.mouseClicked(mouseX, mouseY, mouseEvent);
        }
    }

    public void updateCursorCounter()
    {
        for (Row row : this.options)
        {
            row.updateCursorCounter();
        }
    }

    public void textboxKeyTyped(char typedChar, int keyCode)
    {
        if (keyCode == 28)
        {
            this.saveCurrentValue();
            ExtendedConfig.save();
            this.mc.displayGuiScreen(this.parent);
        }
        for (Row row : this.options)
        {
            row.textboxKeyTyped(typedChar, keyCode);
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Row implements GuiListExtended.IGuiListEntry
    {
        private final Minecraft mc = Minecraft.getMinecraft();
        private final GuiTextFieldExtended textField;
        private final String name;

        public Row(String name, GuiTextFieldExtended textField)
        {
            this.textField = textField;
            this.name = name;
            this.textField.setText(ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(this.textField.getId())));
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            if (this.textField != null)
            {
                this.textField.y = y;
                this.textField.drawTextBox();
                this.mc.fontRenderer.drawString(this.name, x + 64, y + 5, ColorUtils.rgbToDecimal(255, 255, 255));
            }
        }

        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
        {
            return false;
        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}

        @Override
        public void updatePosition(int slotIndex, int x, int y, float partialTicks) {}

        public void saveCurrentValue()
        {
            if (this.textField != null)
            {
                ExtendedConfig.instance.setOptionStringValue(this.textField.getOption(), this.textField.getText());
                ColorUtils.stringToRGB(this.textField.getText(), true, this.textField.getOption().getTranslation());
            }
        }

        public void mouseClicked(int mouseX, int mouseY, int mouseEvent)
        {
            if (this.textField != null)
            {
                this.textField.mouseClicked(mouseX, mouseY, mouseEvent);
            }
        }

        public void updateCursorCounter()
        {
            if (this.textField != null)
            {
                this.textField.updateCursorCounter();
            }
        }

        public void textboxKeyTyped(char typedChar, int keyCode)
        {
            if (this.textField != null)
            {
                this.textField.textboxKeyTyped(typedChar, keyCode);
            }
        }

        public GuiTextFieldExtended getTextField()
        {
            return this.textField;
        }
    }
}