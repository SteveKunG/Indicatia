package com.stevekung.indicatia.gui.exconfig.screen;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.gui.exconfig.ExtendedConfigOption;
import com.stevekung.indicatia.gui.exconfig.screen.widget.ConfigTextFieldWidgetList;
import com.stevekung.indicatia.gui.exconfig.screen.widget.ExtendedTextFieldWidget;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.ColorUtils.RGB;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;

public class CustomRenderInfoColorSettingsScreen extends Screen
{
    private final Screen parent;
    private ConfigTextFieldWidgetList optionsRowList;
    private static ExtendedConfigOption[] OPTIONS = new ExtendedConfigOption[] { ExtendedConfig.FPS_COLOR, ExtendedConfig.XYZ_COLOR, ExtendedConfig.BIOME_COLOR, ExtendedConfig.DIRECTION_COLOR, ExtendedConfig.PING_COLOR, ExtendedConfig.PING_TO_SECOND_COLOR,
            ExtendedConfig.SERVER_IP_COLOR, ExtendedConfig.EQUIPMENT_STATUS_COLOR, ExtendedConfig.ARROW_COUNT_COLOR, ExtendedConfig.SLIME_CHUNK_COLOR, ExtendedConfig.TPS_COLOR, ExtendedConfig.REAL_TIME_COLOR,
            ExtendedConfig.GAME_TIME_COLOR, ExtendedConfig.GAME_WEATHER_COLOR, ExtendedConfig.MOON_PHASE_COLOR, ExtendedConfig.FPS_VALUE_COLOR, ExtendedConfig.FPS_26_AND_40_COLOR, ExtendedConfig.FPS_LOW_25_COLOR, ExtendedConfig.XYZ_VALUE_COLOR, ExtendedConfig.DIRECTION_VALUE_COLOR,
            ExtendedConfig.BIOME_VALUE_COLOR, ExtendedConfig.PING_VALUE_COLOR, ExtendedConfig.PING_200_AND_300_COLOR, ExtendedConfig.PING_300_AND_500_COLOR, ExtendedConfig.PING_MAX_500_COLOR, ExtendedConfig.SERVER_IP_VALUE_COLOR,
            ExtendedConfig.SLIME_CHUNK_VALUE_COLOR, ExtendedConfig.TPS_VALUE_COLOR, ExtendedConfig.REAL_TIME_VALUE_COLOR, ExtendedConfig.GAME_TIME_VALUE_COLOR, ExtendedConfig.GAME_WEATHER_VALUE_COLOR, ExtendedConfig.MOON_PHASE_VALUE_COLOR };

    public CustomRenderInfoColorSettingsScreen(Screen parent)
    {
        super(NarratorChatListener.EMPTY);
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.minecraft.keyboardListener.enableRepeatEvents(true);
        this.addButton(new Button(this.width / 2 - 100, this.height - 25, 200, 20, LangUtils.translate("gui.done"), button ->
        {
            this.optionsRowList.saveCurrentValue();
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(this.parent);
        }));

        this.optionsRowList = new ConfigTextFieldWidgetList(this.width, this.height, 28, this.height - 30, 25);

        for (ExtendedConfigOption option : OPTIONS)
        {
            this.optionsRowList.addButton(option);
        }
        this.children.add(this.optionsRowList);
    }

    @Override
    public void onClose()
    {
        this.minecraft.keyboardListener.enableRepeatEvents(false);
        this.minecraft.displayGuiScreen(this.parent);
    }

    @Override
    public void tick()
    {
        this.optionsRowList.tick();
    }

    @Override
    public void resize(Minecraft mc, int width, int height)
    {
        this.optionsRowList.resize();
        super.resize(mc, width, height);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        ExtendedConfig.INSTANCE.save();
        this.optionsRowList.saveCurrentValue();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.optionsRowList.render(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.font, LangUtils.translate("extended_config.render_info_custom_color.title"), this.width / 2, 5, 16777215);

        if (this.optionsRowList.selected && this.optionsRowList.getFocused() != null && this.optionsRowList.getFocused().getTextField() != null)
        {
            ExtendedTextFieldWidget textField = this.optionsRowList.getFocused().getTextField();
            RGB rgb = ColorUtils.stringToRGB(textField.getText());
            this.drawCenteredString(this.font, LangUtils.translate("menu.example") + ": " + rgb.toColoredFont() + textField.getDisplayName(), this.width / 2, 15, 16777215);
        }
        else
        {
            this.drawCenteredString(this.font, "Color Format is '255,255,255'", this.width / 2, 15, 16777215);
        }
        super.render(mouseX, mouseY, partialTicks);
    }
}