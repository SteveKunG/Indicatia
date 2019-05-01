package stevekung.mods.indicatia.gui.config;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import stevekung.mods.indicatia.config.ExtendedConfig;

@Environment(EnvType.CLIENT)
public class ConfigButtonListWidget extends ElementListWidget<ConfigButtonListWidget.ButtonItem>
{
    public ConfigButtonListWidget(MinecraftClient mc, int int_1, int int_2, int int_3, int int_4, int int_5)
    {
        super(mc, int_1, int_2, int_3, int_4, int_5);
        this.centerListVertically = false;
    }

    public void addButton(ExtendedConfig.Options gameOption_1, ExtendedConfig.Options gameOption_2)
    {
        this.addEntry(ConfigButtonListWidget.ButtonItem.method_20410(this.minecraft.options, this.width, gameOption_1, gameOption_2));
    }

    public void addAll(ExtendedConfig.Options[] gameOptions_1)
    {
        VideoOptionsScreen test;
        for (int int_1 = 0; int_1 < gameOptions_1.length; int_1 += 2)
        {
            this.addButton(gameOptions_1[int_1], int_1 < gameOptions_1.length - 1 ? gameOptions_1[int_1 + 1] : null);
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
        return super.getScrollbarPosition() + 32;
    }

    @Environment(EnvType.CLIENT)
    public static class ButtonItem extends ElementListWidget.Entry<ButtonItem>
    {
        private final List<AbstractButtonWidget> buttons;

        private ButtonItem(List<AbstractButtonWidget> list)
        {
            this.buttons = list;
        }

        public static ConfigButtonListWidget.ButtonItem method_20409(ExtendedConfig.Options gameOptions_1, int int_1, ExtendedConfig.Options gameOption_1)
        {
            return new ConfigButtonListWidget.ButtonItem(ImmutableList.of(gameOption_1.createOptionButton(gameOptions_1, int_1 / 2 - 155, 0, 310)));
        }

        public static ConfigButtonListWidget.ButtonItem method_20410(ExtendedConfig.Options gameOptions_1, int int_1, ExtendedConfig.Options gameOption_1, ExtendedConfig.Options gameOption_2)
        {
            AbstractButtonWidget abstractButtonWidget_1 = gameOption_1.createOptionButton(gameOptions_1, int_1 / 2 - 155, 0, 150);
            return gameOption_2 == null ? new ConfigButtonListWidget.ButtonItem(ImmutableList.of(abstractButtonWidget_1)) : new ConfigButtonListWidget.ButtonItem(ImmutableList.of(abstractButtonWidget_1, gameOption_2.createOptionButton(gameOptions_1, int_1 / 2 - 155 + 160, 0, 150)));
        }

        @Override
        public void render(int x, int y, int int_3, int int_4, int int_5, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            this.buttons.forEach(button ->
            {
                button.y = y;
                button.render(mouseX, mouseY, partialTicks);
            });
        }

        @Override
        public List<AbstractButtonWidget> children()
        {
            return this.buttons;
        }
    }
}
