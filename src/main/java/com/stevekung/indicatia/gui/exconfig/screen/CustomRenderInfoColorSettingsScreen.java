package com.stevekung.indicatia.gui.exconfig.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.gui.exconfig.ExtendedConfigOption;
import com.stevekung.indicatia.gui.exconfig.screen.widget.ConfigTextFieldWidgetList;
import com.stevekung.indicatia.gui.exconfig.screen.widget.ExtendedTextFieldWidget;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class CustomRenderInfoColorSettingsScreen extends Screen
{
    private final Screen parent;
    private ConfigTextFieldWidgetList optionsRowList;
    private static final ExtendedConfigOption[] OPTIONS = new ExtendedConfigOption[] { ExtendedConfig.FPS_COLOR, ExtendedConfig.XYZ_COLOR, ExtendedConfig.BIOME_COLOR, ExtendedConfig.DIRECTION_COLOR, ExtendedConfig.PING_COLOR, ExtendedConfig.PING_TO_SECOND_COLOR,
            ExtendedConfig.SERVER_IP_COLOR, ExtendedConfig.EQUIPMENT_STATUS_COLOR, ExtendedConfig.ARROW_COUNT_COLOR, ExtendedConfig.SLIME_CHUNK_COLOR, ExtendedConfig.TPS_COLOR, ExtendedConfig.REAL_TIME_COLOR,
            ExtendedConfig.GAME_TIME_COLOR, ExtendedConfig.GAME_WEATHER_COLOR, ExtendedConfig.MOON_PHASE_COLOR, ExtendedConfig.FPS_VALUE_COLOR, ExtendedConfig.FPS_26_AND_40_COLOR, ExtendedConfig.FPS_LOW_25_COLOR, ExtendedConfig.XYZ_VALUE_COLOR, ExtendedConfig.DIRECTION_VALUE_COLOR,
            ExtendedConfig.BIOME_VALUE_COLOR, ExtendedConfig.PING_VALUE_COLOR, ExtendedConfig.PING_200_AND_300_COLOR, ExtendedConfig.PING_300_AND_500_COLOR, ExtendedConfig.PING_MAX_500_COLOR, ExtendedConfig.SERVER_IP_VALUE_COLOR,
            ExtendedConfig.SLIME_CHUNK_VALUE_COLOR, ExtendedConfig.TPS_VALUE_COLOR, ExtendedConfig.REAL_TIME_VALUE_COLOR, ExtendedConfig.GAME_TIME_VALUE_COLOR, ExtendedConfig.GAME_WEATHER_VALUE_COLOR, ExtendedConfig.MOON_PHASE_VALUE_COLOR };

    public CustomRenderInfoColorSettingsScreen(Screen parent)
    {
        super(StringTextComponent.EMPTY);
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.minecraft.keyboardListener.enableRepeatEvents(true);
        this.addButton(new Button(this.width / 2 - 100, this.height - 25, 200, 20, DialogTexts.GUI_DONE, button ->
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
    public void closeScreen()
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
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);
        AbstractGui.drawCenteredString(matrixStack, this.font, LangUtils.translate("menu.render_info_custom_color.title"), this.width / 2, 5, 16777215);

        if (this.optionsRowList.selected && this.optionsRowList.getSelected() != null && this.optionsRowList.getSelected().getTextField() != null)
        {
            ExtendedTextFieldWidget textField = this.optionsRowList.getSelected().getTextField();
            AbstractGui.drawCenteredString(matrixStack, this.font, LangUtils.translate("menu.example") + ": " + textField.getDisplayName(), this.width / 2, 15, ColorUtils.rgbToDecimal(textField.getText()));
        }
        else
        {
            AbstractGui.drawCenteredString(matrixStack, this.font, LangUtils.translate("menu.color_format_info"), this.width / 2, 15, 16777215);
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}