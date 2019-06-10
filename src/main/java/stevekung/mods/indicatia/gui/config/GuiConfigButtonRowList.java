package stevekung.mods.indicatia.gui.config;

import com.google.common.base.Strings;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;

@OnlyIn(Dist.CLIENT)
public class GuiConfigButtonRowList extends ElementListWidget<GuiConfigButtonRowList.Row>
{
    static String comment = null;

    GuiConfigButtonRowList(int width, int height, int top, int bottom, int slotHeight, ExtendedConfig.Options[] options)
    {
        super(Minecraft.getInstance(), width, height, top, bottom, slotHeight);
        this.centerListVertically = false;

        for (int i = 0; i < options.length; i += 2)
        {
            ExtendedConfig.Options exoptions = options[i];
            ExtendedConfig.Options exoptions1 = i < options.length - 1 ? options[i + 1] : null;
            GuiButton button = GuiConfigButtonRowList.createButton(width / 2 - 165, exoptions);
            GuiButton button1 = GuiConfigButtonRowList.createButton(width / 2 - 160 + 160, exoptions1);
            this.addEntry(new GuiConfigButtonRowList.Row(button, button1));
        }
    }

    private static GuiButton createButton(int x, ExtendedConfig.Options options)
    {
        if (options == null)
        {
            return null;
        }
        else
        {
            int i = options.getOrdinal();

            GuiConfigButton button1 = new GuiConfigButton(i, x, 0, 160, ExtendedConfig.instance.getKeyBinding(options), options.getComment())
            {
                @Override
                public void onClick(double mouseX, double mouseY)
                {
                    ExtendedConfig.instance.setOptionValue(options, 1);
                    this.displayString = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(this.id));
                    ExtendedConfig.save();
                }

                @Override
                public boolean mouseClicked(double mouseX, double mouseY, int mouseEvent)
                {
                    String comment = this.getComment();

                    if (mouseEvent == 1 && !StringUtils.isNullOrEmpty(comment))
                    {
                        GuiConfigButtonRowList.comment = comment;
                        return true;
                    }
                    return super.mouseClicked(mouseX, mouseY, mouseEvent);
                }
            };
            GuiConfigButton button2 = new GuiConfigButton(i, x, 0, 160, ExtendedConfig.instance.getKeyBinding(options))
            {
                @Override
                public void onClick(double mouseX, double mouseY)
                {
                    ExtendedConfig.instance.setOptionValue(options, 1);
                    this.displayString = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(this.id));
                    ExtendedConfig.save();
                }

                @Override
                public boolean mouseClicked(double mouseX, double mouseY, int mouseEvent)
                {
                    String comment = this.getComment();

                    if (mouseEvent == 1 && !StringUtils.isNullOrEmpty(comment))
                    {
                        GuiConfigButtonRowList.comment = comment;
                        return true;
                    }
                    return super.mouseClicked(mouseX, mouseY, mouseEvent);
                }
            };
            return options.isDouble() ? new GuiConfigSlider(i, x, 0, 160, options) : Strings.isNullOrEmpty(options.getComment()) ? button1 : button2;
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

    @OnlyIn(Dist.CLIENT)
    public static class Row extends ElementListWidget.Entry<Row>
    {
        private final GuiButton buttonA;
        private final GuiButton buttonB;

        Row(GuiButton buttonA, GuiButton buttonB)
        {
            this.buttonA = buttonA;
            this.buttonB = buttonB;
        }

        @Override
        public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            if (this.buttonA != null)
            {
                this.buttonA.y = this.getY();
                this.buttonA.render(mouseX, mouseY, partialTicks);
            }
            if (this.buttonB != null)
            {
                this.buttonB.y = this.getY();
                this.buttonB.render(mouseX, mouseY, partialTicks);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int mouseEvent)
        {
            if (this.buttonA.mouseClicked(mouseX, mouseY, mouseEvent))
            {
                return true;
            }
            return this.buttonB != null && this.buttonB.mouseClicked(mouseX, mouseY, mouseEvent);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int mouseEvent)
        {
            GuiConfigButtonRowList.comment = null;
            boolean flag = this.buttonA != null && this.buttonA.mouseReleased(mouseX, mouseY, mouseEvent);
            boolean flag1 = this.buttonB != null && this.buttonB.mouseReleased(mouseX, mouseY, mouseEvent);
            return flag || flag1;
        }

        @Override
        public void func_195000_a(float partialTicks) {} //TODO updatePosition
    }
}