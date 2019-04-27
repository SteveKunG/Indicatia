package stevekung.mods.indicatia.gui.config;

import com.google.common.base.Strings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import stevekung.mods.indicatia.config.ExtendedConfig;

@Environment(EnvType.CLIENT)
public class ConfigListWidget extends EntryListWidget<ConfigListWidget.Row>
{
    public static String comment = null;

    public ConfigListWidget(int width, int height, int top, int bottom, int slotHeight, ExtendedConfig.Options[] options)
    {
        super(MinecraftClient.getInstance(), width, height, top, bottom, slotHeight);
        this.field_2173 = false;//centerListVertically

        for (int i = 0; i < options.length; i += 2)
        {
            ExtendedConfig.Options exoptions = options[i];
            ExtendedConfig.Options exoptions1 = i < options.length - 1 ? options[i + 1] : null;
            ButtonWidget button = this.createButton(width / 2 - 165, 0, exoptions);
            ButtonWidget button1 = this.createButton(width / 2 - 160 + 160, 0, exoptions1);
            this.addEntry(new ConfigListWidget.Row(button, button1));
        }
    }

    private ButtonWidget createButton(int x, int y, ExtendedConfig.Options options)
    {
        if (options == null)
        {
            return null;
        }
        else
        {
            int i = options.getOrdinal();
            return options.isDouble() ? new ConfigSliderWidget(i, x, y, 160, options) : options.getComment() != null ? new ConfigButtonWidget(i, x, y, 160, options, ExtendedConfig.instance.getKeyBinding(options), options.getComment()) : new ConfigButtonWidget(i, x, y, 160, options, ExtendedConfig.instance.getKeyBinding(options));
        }
    }

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

    @Environment(EnvType.CLIENT)
    public static class Row extends EntryListWidget.Entry<Row>
    {
        private final MinecraftClient mc = MinecraftClient.getInstance();
        private final ButtonWidget buttonA;
        private final ButtonWidget buttonB;

        public Row(ButtonWidget buttonA, ButtonWidget buttonB)
        {
            this.buttonA = buttonA;
            this.buttonB = buttonB;
        }

        @Override
        public void draw(int slotIndex, int listWidth, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            if (this.buttonA != null)
            {
                this.buttonA.y = this.getY();
                this.buttonA.draw(mouseX, mouseY, partialTicks);
            }
            if (this.buttonB != null)
            {
                this.buttonB.y = this.getY();
                this.buttonB.draw(mouseX, mouseY, partialTicks);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int mouseEvent)
        {
            if (this.buttonA.mouseClicked(mouseX, mouseY, mouseEvent))
            {
                if (this.buttonA instanceof ConfigButtonWidget)
                {
                    String comment = ((ConfigButtonWidget)this.buttonA).getComment();

                    if (mouseEvent == 1 && !Strings.isNullOrEmpty(comment))
                    {
                        ConfigListWidget.comment = comment;
                    }

                    if (mouseEvent == 0)
                    {
                        ExtendedConfig.instance.setOptionValue(((ConfigButtonWidget)this.buttonA).getOption(), 1);
                        this.buttonA.text = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(this.buttonA.id));
                        this.buttonA.playPressedSound(this.mc.getSoundLoader());
                    }
                }
                if (this.buttonA instanceof ConfigSliderWidget)
                {
                    this.buttonA.playPressedSound(this.mc.getSoundLoader());
                }
                return true;
            }
            else if (this.buttonB != null && this.buttonB.mouseClicked(mouseX, mouseY, mouseEvent))
            {
                if (this.buttonB instanceof ConfigButtonWidget)
                {
                    String comment = ((ConfigButtonWidget)this.buttonB).getComment();

                    if (mouseEvent == 1 && !Strings.isNullOrEmpty(comment))
                    {
                        ConfigListWidget.comment = comment;
                    }

                    if (mouseEvent == 0)
                    {
                        ExtendedConfig.instance.setOptionValue(((ConfigButtonWidget)this.buttonB).getOption(), 1);
                        this.buttonB.text = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(this.buttonB.id));
                        this.buttonB.playPressedSound(this.mc.getSoundLoader());
                    }
                }
                if (this.buttonB instanceof ConfigSliderWidget)
                {
                    this.buttonB.playPressedSound(this.mc.getSoundLoader());
                }
                return true;
            }
            else
            {
                return false;
            }
        }

        @Override
        public boolean mouseReleased(double x, double y, int mouseEvent)
        {
            if (this.buttonA != null)
            {
                ConfigListWidget.comment = null;
                return this.buttonA.mouseReleased(x, y, mouseEvent);
            }
            if (this.buttonB != null)
            {
                ConfigListWidget.comment = null;
                return this.buttonB.mouseReleased(x, y, mouseEvent);
            }
            return false;
        }

        @Override
        public void method_1904(float partialTicks) {}//updatePosition
    }
}