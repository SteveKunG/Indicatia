package stevekung.mods.indicatia.gui.config;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import stevekung.mods.indicatia.config.ExtendedConfig;

@Environment(EnvType.CLIENT)
public class GuiConfigButtonRowList extends ElementListWidget<GuiConfigButtonRowList.Row>
{
    static String comment = null;

    GuiConfigButtonRowList(int width, int height, int top, int bottom, int slotHeight, ExtendedConfig.Options[] options)
    {
        super(MinecraftClient.getInstance(), width, height, top, bottom, slotHeight);
        this.centerListVertically = false;

        for (int i = 0; i < options.length; i += 2)
        {
            ExtendedConfig.Options exoptions = options[i];
            ExtendedConfig.Options exoptions1 = i < options.length - 1 ? options[i + 1] : null;
            ButtonWidget button = GuiConfigButtonRowList.createButton(width / 2 - 165, exoptions);
            ButtonWidget button1 = GuiConfigButtonRowList.createButton(width / 2 - 160 + 160, exoptions1);
            this.addEntry(new GuiConfigButtonRowList.Row(ImmutableList.of(button, button1)));
        }
    }

    private static ButtonWidget createButton(int x, ExtendedConfig.Options options)
    {
        if (options == null)
        {
            return null;
        }
        else
        {
            int i = options.getOrdinal();

            GuiConfigButton button1 = new GuiConfigButton(x, 0, 160, ExtendedConfig.instance.getKeyBinding(options), options.getComment())
            {
                @Override
                public void onClick(double mouseX, double mouseY)
                {
                    ExtendedConfig.instance.setOptionValue(options, 1);
                    this.setMessage(ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(i)));
                    ExtendedConfig.save();
                }

                @Override
                public boolean mouseClicked(double mouseX, double mouseY, int mouseEvent)
                {
                    String comment = this.getComment();

                    if (mouseEvent == 1 && !StringUtils.isEmpty(comment))
                    {
                        GuiConfigButtonRowList.comment = comment;
                        return true;
                    }
                    return super.mouseClicked(mouseX, mouseY, mouseEvent);
                }
            };
            GuiConfigButton button2 = new GuiConfigButton(x, 0, 160, ExtendedConfig.instance.getKeyBinding(options))
            {
                @Override
                public void onClick(double mouseX, double mouseY)
                {
                    ExtendedConfig.instance.setOptionValue(options, 1);
                    this.setMessage(ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(i)));
                    ExtendedConfig.save();
                }

                @Override
                public boolean mouseClicked(double mouseX, double mouseY, int mouseEvent)
                {
                    String comment = this.getComment();

                    if (mouseEvent == 1 && !StringUtils.isEmpty(comment))
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
    public int getRowWidth()
    {
        return 400;
    }

    @Override
    protected int getScrollbarPosition()
    {
        return super.getScrollbarPosition() + 40;
    }

    @Environment(EnvType.CLIENT)
    public static class Row extends ElementListWidget.Entry<Row>
    {
        private final List<AbstractButtonWidget> buttons;

        Row(List<AbstractButtonWidget> buttons)
        {
            this.buttons = buttons;
        }

        @Override
        public void render(int entryWidth, int entryHeight, int mouseX, int mouseY, int var5, int var6, int var7, boolean isSelected, float partialTicks)
        {
            this.buttons.forEach(button ->
            {
                button.y = entryHeight;
                button.render(var6, var7, partialTicks);
            });
        }

        @Override
        public List<? extends Element> children()
        {
            return this.buttons;
        }
    }
}