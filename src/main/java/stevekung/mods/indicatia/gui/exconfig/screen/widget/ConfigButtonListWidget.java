package stevekung.mods.indicatia.gui.exconfig.screen.widget;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.exconfig.ExtendedConfigOption;

@Environment(EnvType.CLIENT)
public class ConfigButtonListWidget extends ElementListWidget<ConfigButtonListWidget.ButtonItem>
{
    public ConfigButtonListWidget(int x, int y, int top, int bottom, int itemHeight)
    {
        super(MinecraftClient.getInstance(), x, y, top, bottom, itemHeight);
        this.centerListVertically = false;
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

    public void addButton(ExtendedConfigOption config1, ExtendedConfigOption config2)
    {
        this.addEntry(ConfigButtonListWidget.ButtonItem.createItems(ExtendedConfig.instance, this.width, config1, config2));
    }

    public void addAll(ExtendedConfigOption[] config)
    {
        for (int i = 0; i < config.length; i += 2)
        {
            this.addButton(config[i], i < config.length - 1 ? config[i + 1] : null);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class ButtonItem extends ElementListWidget.Entry<ButtonItem>
    {
        private final List<AbstractButtonWidget> buttons;

        private ButtonItem(List<AbstractButtonWidget> list)
        {
            this.buttons = list;
        }

        @Override
        public void render(int x, int y, int int_3, int int_4, int int_5, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            for (AbstractButtonWidget button : this.buttons)
            {
                button.y = y;
                button.render(mouseX, mouseY, partialTicks);
            }
        }

        @Override
        public List<AbstractButtonWidget> children()
        {
            return this.buttons;
        }

        public static ConfigButtonListWidget.ButtonItem createItem(ExtendedConfig config, int int_1, ExtendedConfigOption configOpt)
        {
            return new ConfigButtonListWidget.ButtonItem(ImmutableList.of(configOpt.createOptionButton(config, int_1 / 2 - 155, 0, 310)));
        }

        public static ConfigButtonListWidget.ButtonItem createItems(ExtendedConfig config, int x, ExtendedConfigOption configOpt1, ExtendedConfigOption configOpt2)
        {
            AbstractButtonWidget button = configOpt1.createOptionButton(config, x / 2 - 155, 0, 150);
            return configOpt2 == null ? new ConfigButtonListWidget.ButtonItem(ImmutableList.of(button)) : new ConfigButtonListWidget.ButtonItem(ImmutableList.of(button, configOpt2.createOptionButton(config, x / 2 - 155 + 160, 0, 150)));
        }
    }
}
