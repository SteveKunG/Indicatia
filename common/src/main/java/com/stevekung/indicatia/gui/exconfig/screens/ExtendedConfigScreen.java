package com.stevekung.indicatia.gui.exconfig.screens;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.config.BooleanSettings;
import com.stevekung.stevekungslib.utils.config.IteratableSettings;
import com.stevekung.stevekungslib.utils.config.SliderPercentageSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TextComponent;

public class ExtendedConfigScreen extends Screen
{
    private static final ImmutableList<?> OPTIONS = ImmutableList.of(IndicatiaSettings.SWAP_INFO_POS, IndicatiaSettings.EQUIPMENT_DIRECTION, IndicatiaSettings.EQUIPMENT_STATUS, IndicatiaSettings.EQUIPMENT_POSITION, IndicatiaSettings.POTION_HUD_STYLE, IndicatiaSettings.POTION_HUD_POSITION, IndicatiaSettings.PING_MODE, IndicatiaSettings.TIME_ON_VANILLA_POTION_HUD);

    public ExtendedConfigScreen()
    {
        super(TextComponent.EMPTY);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init()
    {
        int i = 0;

        for (Object options : OPTIONS)
        {
            if (options instanceof SliderPercentageSettings)
            {
                this.addButton(((SliderPercentageSettings<IndicatiaSettings>) options).createWidget(IndicatiaSettings.INSTANCE, this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 17 + 24 * (i >> 1), 160));
            }
            else if (options instanceof BooleanSettings)
            {
                this.addButton(((BooleanSettings<IndicatiaSettings>) options).createWidget(IndicatiaSettings.INSTANCE, this.width / 2 - 160 + i % 2 * 165, this.height / 6 - 17 + 24 * (i >> 1), 160));
            }
            else
            {
                this.addButton(((IteratableSettings<IndicatiaSettings>) options).createWidget(IndicatiaSettings.INSTANCE, this.width / 2 - 160 + i % 2 * 165, this.height / 6 - 17 + 24 * (i >> 1), 160));
            }
            ++i;
        }

        this.addButton(new Button(this.width / 2 - 155, this.height / 6 + 127, 150, 20, LangUtils.translate("menu.render_info.title"), button ->
        {
            IndicatiaSettings.INSTANCE.save();
            this.minecraft.setScreen(new RenderInfoSettingsScreen(this));
        }));
        this.addButton(new Button(this.width / 2 + 10, this.height / 6 + 127, 150, 20, LangUtils.translate("menu.custom_color.title"), button ->
        {
            IndicatiaSettings.INSTANCE.save();
            this.minecraft.setScreen(new CustomColorSettingsScreen(this));
        }));
        this.addButton(new Button(this.width / 2 - 155, this.height / 6 + 151, 150, 20, LangUtils.translate("menu.offset.title"), button ->
        {
            IndicatiaSettings.INSTANCE.save();
            this.minecraft.setScreen(new OffsetSettingsScreen(this));
        }));
        this.addButton(new Button(this.width / 2 + 10, this.height / 6 + 151, 150, 20, LangUtils.translate("menu.hypixel.title"), button ->
        {
            IndicatiaSettings.INSTANCE.save();
            this.minecraft.setScreen(new HypixelSettingsScreen(this));
        }));
        this.addButton(new Button(this.width / 2 + 5, this.height / 6 + 175, 160, 20, CommonComponents.GUI_DONE, button ->
        {
            IndicatiaSettings.INSTANCE.save();
            this.minecraft.setScreen(null);
        }));
        this.addButton(new Button(this.width / 2 - 160, this.height / 6 + 175, 160, 20, LangUtils.translate("menu.reset_config"), button ->
        {
            IndicatiaSettings.INSTANCE.save();
            this.minecraft.setScreen(new ConfirmScreen(this::resetConfig, LangUtils.translate("menu.reset_config_confirm"), TextComponent.EMPTY));
        }));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        IndicatiaSettings.INSTANCE.save();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        GuiComponent.drawCenteredString(matrixStack, this.font, LangUtils.translate("menu.main.title").copy().append(" : ").append(LangUtils.formatted("menu.current_selected_profile", ChatFormatting.YELLOW, IndicatiaSettings.CURRENT_PROFILE)), this.width / 2, 10, 16777215);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void resetConfig(boolean condition)
    {
        if (condition)
        {
            IndicatiaSettings.resetConfig();
        }
        this.minecraft.setScreen(this);
    }
}