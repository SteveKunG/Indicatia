package stevekung.mods.indicatia.gui.config;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;

@OnlyIn(Dist.CLIENT)
public class ConfigButtonListWidget extends ExtendedList<ConfigButtonListWidget.ButtonItem>
{
    public ConfigButtonListWidget(int x, int y, int top, int bottom, int itemHeight)
    {
        super(Minecraft.getInstance(), x, y, top, bottom, itemHeight);
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

    @OnlyIn(Dist.CLIENT)
    public static class ButtonItem extends ExtendedList.AbstractListEntry<ButtonItem>
    {
        private final List<AbstractButton> buttons;

        private ButtonItem(List<AbstractButton> list)
        {
            this.buttons = list;
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

        public static ConfigButtonListWidget.ButtonItem createItem(ExtendedConfig config, int int_1, ExtendedConfigOption configOpt)
        {
            return new ConfigButtonListWidget.ButtonItem(ImmutableList.of(configOpt.createOptionButton(config, int_1 / 2 - 155, 0, 310)));
        }

        public static ConfigButtonListWidget.ButtonItem createItems(ExtendedConfig config, int x, ExtendedConfigOption configOpt1, ExtendedConfigOption configOpt2)
        {
            AbstractButton button = configOpt1.createOptionButton(config, x / 2 - 155, 0, 150);
            return configOpt2 == null ? new ConfigButtonListWidget.ButtonItem(ImmutableList.of(button)) : new ConfigButtonListWidget.ButtonItem(ImmutableList.of(button, configOpt2.createOptionButton(config, x / 2 - 155 + 160, 0, 150)));
        }
    }
}
