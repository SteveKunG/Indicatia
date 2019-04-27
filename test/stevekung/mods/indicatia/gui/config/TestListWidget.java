package stevekung.mods.indicatia.gui.config;

import com.google.common.base.Strings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import stevekung.mods.indicatia.config.ExtendedConfig;

import javax.annotation.Nullable;

@Environment(EnvType.CLIENT)
public class TestListWidget extends EntryListWidget<TestListWidget.Row>
{
    public TestListWidget(int width, int height, int top, int bottom, int slotHeight, ExtendedConfig.Options[] options)
    {
        super(MinecraftClient.getInstance(), width, height, top, bottom, slotHeight);
        this.field_2173 = false;

        for (int i = 0; i < options.length; i += 2)
        {
            ExtendedConfig.Options option1 = options[i];
            ExtendedConfig.Options option2 = i < options.length - 1 ? options[i + 1] : null;
            this.addEntry(new Row(width, option1, option2));
        }
    }

    @Nullable
    private static ButtonWidget createButton(int x, int y, @Nullable ExtendedConfig.Options options)
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
    public class Row extends EntryListWidget.Entry<Row>
    {
        @Nullable
        private final ButtonWidget buttonA;

        @Nullable
        private final ButtonWidget buttonB;

        public Row(@Nullable ButtonWidget buttonA, @Nullable ButtonWidget buttonB)
        {
            this.buttonA = buttonA;
            this.buttonB = buttonB;
        }

        public Row(int x, ExtendedConfig.Options option1, ExtendedConfig.Options option2)
        {
            this(TestListWidget.createButton(x / 2 - 155, 0, option1), TestListWidget.createButton(x / 2 - 155 + 160, 0, option2));
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

//        @Override
//        public boolean mouseClicked(double mouseX, double mouseY, int mouseEvent)
//        {
//            return false;
//            if (this.buttonA != null && this.buttonA.mouseClicked(mouseX, mouseY, mouseEvent))
//            {
//                if (this.buttonA instanceof ConfigButtonWidget)
//                {
//                    String comment = ((ConfigButtonWidget)this.buttonA).getComment();
//
//                    if (mouseEvent == 1 && !Strings.isNullOrEmpty(comment))
//                    {
//                        ConfigListWidget.comment = comment;
//                    }
//
//                    if (mouseEvent == 0)
//                    {
//                        ExtendedConfig.instance.setOptionValue(((ConfigButtonWidget)this.buttonA).getOption(), 1);
//                        this.buttonA.text = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(this.buttonA.id));
//                        this.buttonA.playPressedSound(TestListWidget.this.client.getSoundLoader());
//                    }
//                }
//                if (this.buttonA instanceof ConfigSliderWidget)
//                {
//                    this.buttonA.playPressedSound(TestListWidget.this.client.getSoundLoader());
//                }
//                return true;
//            }
//            else
//            {
//                if (this.buttonB instanceof ConfigButtonWidget)
//                {
//                    String comment = ((ConfigButtonWidget)this.buttonB).getComment();
//
//                    if (mouseEvent == 1 && !Strings.isNullOrEmpty(comment))
//                    {
//                        ConfigListWidget.comment = comment;
//                    }
//
//                    if (mouseEvent == 0)
//                    {
//                        ExtendedConfig.instance.setOptionValue(((ConfigButtonWidget)this.buttonB).getOption(), 1);
//                        this.buttonB.text = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(this.buttonB.id));
//                        this.buttonB.playPressedSound(TestListWidget.this.client.getSoundLoader());
//                    }
//                }
//                if (this.buttonB instanceof ConfigSliderWidget)
//                {
//                    this.buttonB.playPressedSound(TestListWidget.this.client.getSoundLoader());
//                }
//                return this.buttonB != null && this.buttonB.mouseClicked(mouseX, mouseY, mouseEvent);
//            }
//        }

//        @Override
//        public boolean mouseReleased(double x, double y, int mouseEvent)
//        {
//            ConfigListWidget.comment = null;
//            boolean boolean_1 = this.buttonA != null && this.buttonA.mouseReleased(x, y, mouseEvent);
//            boolean boolean_2 = this.buttonB != null && this.buttonB.mouseReleased(x, y, mouseEvent);
//            return boolean_1 || boolean_2;
//        }

        @Override
        public void method_1904(float partialTicks) {}//updatePosition
    }
}
