package stevekung.mods.indicatia.gui.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.config.ExtendedConfig;

@SideOnly(Side.CLIENT)
public class GuiConfigButtonRowList extends GuiListExtended
{
    private final List<GuiConfigButtonRowList.Row> options = new ArrayList<>();
    public static String comment = null;

    public GuiConfigButtonRowList(int width, int height, int top, int bottom, int slotHeight, ExtendedConfig.Options[] options)
    {
        super(Minecraft.getMinecraft(), width, height, top, bottom, slotHeight);
        this.centerListVertically = false;

        for (int i = 0; i < options.length; i += 2)
        {
            ExtendedConfig.Options exoptions = options[i];
            ExtendedConfig.Options exoptions1 = i < options.length - 1 ? options[i + 1] : null;
            GuiButton button = this.createButton(width / 2 - 165, 0, exoptions);
            GuiButton button1 = this.createButton(width / 2 - 160 + 160, 0, exoptions1);
            this.options.add(new GuiConfigButtonRowList.Row(button, button1));
        }
    }

    private GuiButton createButton(int x, int y, ExtendedConfig.Options options)
    {
        if (options == null)
        {
            return null;
        }
        else
        {
            int i = options.getOrdinal();
            return options.isFloat() ? new GuiConfigSlider(i, x, y, 160, options) : options.getComment() != null ? new GuiConfigButton(i, x, y, 160, options, ExtendedConfig.instance.getKeyBinding(options), options.getComment()) : new GuiConfigButton(i, x, y, 160, options, ExtendedConfig.instance.getKeyBinding(options));
        }
    }

    @Override
    public GuiConfigButtonRowList.Row getListEntry(int index)
    {
        return this.options.get(index);
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

    @SideOnly(Side.CLIENT)
    public static class Row implements GuiListExtended.IGuiListEntry
    {
        private final Minecraft mc = Minecraft.getMinecraft();
        private final GuiButton buttonA;
        private final GuiButton buttonB;
        public Row(GuiButton buttonA, GuiButton buttonB)
        {
            this.buttonA = buttonA;
            this.buttonB = buttonB;
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            if (this.buttonA != null)
            {
                this.buttonA.y = y;
                this.buttonA.drawButton(this.mc, mouseX, mouseY, partialTicks);
            }
            if (this.buttonB != null)
            {
                this.buttonB.y = y;
                this.buttonB.drawButton(this.mc, mouseX, mouseY, partialTicks);
            }
        }

        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.buttonA.mousePressed(this.mc, mouseX, mouseY))
            {
                if (this.buttonA instanceof GuiConfigButton)
                {
                    String comment = ((GuiConfigButton)this.buttonA).getComment();

                    if (mouseEvent == 1 && !StringUtils.isNullOrEmpty(comment))
                    {
                        GuiConfigButtonRowList.comment = comment;
                    }

                    if (mouseEvent == 0)
                    {
                        ExtendedConfig.instance.setOptionValue(((GuiConfigButton)this.buttonA).getOption(), 1);
                        this.buttonA.displayString = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(this.buttonA.id));
                        this.buttonA.playPressSound(this.mc.getSoundHandler());
                    }
                }
                if (this.buttonA instanceof GuiConfigSlider)
                {
                    this.buttonA.playPressSound(this.mc.getSoundHandler());
                }
                return true;
            }
            else if (this.buttonB != null && this.buttonB.mousePressed(this.mc, mouseX, mouseY))
            {
                if (this.buttonB instanceof GuiConfigButton)
                {
                    String comment = ((GuiConfigButton)this.buttonB).getComment();

                    if (mouseEvent == 1 && !StringUtils.isNullOrEmpty(comment))
                    {
                        GuiConfigButtonRowList.comment = comment;
                    }

                    if (mouseEvent == 0)
                    {
                        ExtendedConfig.instance.setOptionValue(((GuiConfigButton)this.buttonB).getOption(), 1);
                        this.buttonB.displayString = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(this.buttonB.id));
                        this.buttonB.playPressSound(this.mc.getSoundHandler());
                    }
                }
                if (this.buttonB instanceof GuiConfigSlider)
                {
                    this.buttonB.playPressSound(this.mc.getSoundHandler());
                }
                return true;
            }
            else
            {
                return false;
            }
        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
        {
            if (this.buttonA != null)
            {
                GuiConfigButtonRowList.comment = null;
                this.buttonA.mouseReleased(x, y);
            }
            if (this.buttonB != null)
            {
                GuiConfigButtonRowList.comment = null;
                this.buttonB.mouseReleased(x, y);
            }
        }

        @Override
        public void updatePosition(int slotIndex, int x, int y, float partialTicks) {}
    }
}