package com.stevekung.indicatia.gui.exconfig.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.gui.exconfig.screen.widget.ConfigTextFieldWidgetList;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.config.AbstractSettings;
import com.stevekung.stevekungslib.utils.config.TextFieldSettingsWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TextComponent;

public class CustomRenderInfoColorSettingsScreen extends Screen
{
    private final Screen parent;
    private ConfigTextFieldWidgetList optionsRowList;
    private static final ImmutableList<AbstractSettings<IndicatiaSettings>> OPTIONS = ImmutableList.of(IndicatiaSettings.FPS_COLOR, IndicatiaSettings.XYZ_COLOR, IndicatiaSettings.BIOME_COLOR, IndicatiaSettings.DIRECTION_COLOR, IndicatiaSettings.PING_COLOR, IndicatiaSettings.PING_TO_SECOND_COLOR, IndicatiaSettings.SERVER_IP_COLOR, IndicatiaSettings.EQUIPMENT_STATUS_COLOR, IndicatiaSettings.ARROW_COUNT_COLOR, IndicatiaSettings.SLIME_CHUNK_COLOR, IndicatiaSettings.TPS_COLOR, IndicatiaSettings.REAL_TIME_COLOR, IndicatiaSettings.GAME_TIME_COLOR, IndicatiaSettings.GAME_WEATHER_COLOR, IndicatiaSettings.MOON_PHASE_COLOR, IndicatiaSettings.FPS_VALUE_COLOR, IndicatiaSettings.FPS_26_AND_40_COLOR, IndicatiaSettings.FPS_LOW_25_COLOR, IndicatiaSettings.XYZ_VALUE_COLOR, IndicatiaSettings.DIRECTION_VALUE_COLOR, IndicatiaSettings.BIOME_VALUE_COLOR, IndicatiaSettings.PING_VALUE_COLOR, IndicatiaSettings.PING_200_AND_300_COLOR, IndicatiaSettings.PING_300_AND_500_COLOR, IndicatiaSettings.PING_MAX_500_COLOR, IndicatiaSettings.SERVER_IP_VALUE_COLOR, IndicatiaSettings.SLIME_CHUNK_VALUE_COLOR, IndicatiaSettings.TPS_VALUE_COLOR, IndicatiaSettings.REAL_TIME_VALUE_COLOR, IndicatiaSettings.GAME_TIME_VALUE_COLOR, IndicatiaSettings.GAME_WEATHER_VALUE_COLOR, IndicatiaSettings.MOON_PHASE_VALUE_COLOR);

    public CustomRenderInfoColorSettingsScreen(Screen parent)
    {
        super(TextComponent.EMPTY);
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.addButton(new Button(this.width / 2 - 100, this.height - 25, 200, 20, CommonComponents.GUI_DONE, button ->
        {
            this.optionsRowList.saveCurrentValue();
            IndicatiaSettings.INSTANCE.save();
            this.minecraft.setScreen(this.parent);
        }));

        this.optionsRowList = new ConfigTextFieldWidgetList(this.width, this.height, 28, this.height - 30, 25);

        for (AbstractSettings<IndicatiaSettings> option : OPTIONS)
        {
            this.optionsRowList.addButton(option);
        }
        this.children.add(this.optionsRowList);
    }

    @Override
    public void onClose()
    {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
        this.minecraft.setScreen(this.parent);
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
        IndicatiaSettings.INSTANCE.save();
        this.optionsRowList.saveCurrentValue();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);
        GuiComponent.drawCenteredString(matrixStack, this.font, LangUtils.translate("menu.render_info_custom_color.title"), this.width / 2, 5, 16777215);

        if (this.optionsRowList.selected && this.optionsRowList.getSelected() != null && this.optionsRowList.getSelected().getTextField() != null)
        {
            TextFieldSettingsWidget<IndicatiaSettings> textField = this.optionsRowList.getSelected().getTextField();
            GuiComponent.drawCenteredString(matrixStack, this.font, LangUtils.translate("menu.example") + ": " + textField.getDisplayName(), this.width / 2, 15, ColorUtils.rgbToDecimal(textField.getValue()));
        }
        else
        {
            GuiComponent.drawCenteredString(matrixStack, this.font, LangUtils.translate("menu.color_format_info"), this.width / 2, 15, 16777215);
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}