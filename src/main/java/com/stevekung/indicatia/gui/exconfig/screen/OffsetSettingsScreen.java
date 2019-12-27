package com.stevekung.indicatia.gui.exconfig.screen;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.gui.exconfig.ExtendedConfigOption;
import com.stevekung.indicatia.gui.exconfig.screen.widget.ConfigButtonListWidget;
import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;

public class OffsetSettingsScreen extends Screen
{
    private final Screen parent;
    private ConfigButtonListWidget optionsRowList;
    private static final ExtendedConfigOption[] OPTIONS = new ExtendedConfigOption[] { ExtendedConfig.ARMOR_HUD_Y, ExtendedConfig.POTION_HUD_Y, ExtendedConfig.MAXIMUM_POTION_DISPLAY, ExtendedConfig.POTION_LENGTH_Y_OFFSET, ExtendedConfig.POTION_LENGTH_Y_OFFSET_OVERLAP };

    public OffsetSettingsScreen(Screen parent)
    {
        super(JsonUtils.create("Offset Settings"));
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new Button(this.width / 2 - 105, this.height - 25, 100, 20, LangUtils.translate("gui.done"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(this.parent);
        }));
        this.addButton(new Button(this.width / 2 + 5, this.height - 25, 100, 20, LangUtils.translate("menu.preview"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new OffsetRenderPreviewScreen(this));
        }));

        this.optionsRowList = new ConfigButtonListWidget(this.width, this.height, 16, this.height - 30, 25);
        this.optionsRowList.addAll(OPTIONS);
        this.children.add(this.optionsRowList);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        ExtendedConfig.INSTANCE.save();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.optionsRowList.render(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.font, LangUtils.translate("extended_config.offset.title"), this.width / 2, 5, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }
}