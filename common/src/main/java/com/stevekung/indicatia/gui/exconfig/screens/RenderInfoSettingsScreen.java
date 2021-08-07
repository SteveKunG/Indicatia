package com.stevekung.indicatia.gui.exconfig.screens;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.gui.exconfig.components.ConfigButtonListWidget;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.config.AbstractSettings;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class RenderInfoSettingsScreen extends Screen
{
    private final Screen parent;
    private ConfigButtonListWidget optionsRowList;
    private static final ImmutableList<AbstractSettings<IndicatiaSettings>> OPTIONS = ImmutableList.of(IndicatiaSettings.FPS, IndicatiaSettings.XYZ, IndicatiaSettings.DIRECTION, IndicatiaSettings.BIOME, IndicatiaSettings.PING, IndicatiaSettings.PING_TO_SECOND, IndicatiaSettings.SERVER_IP, IndicatiaSettings.SERVER_IP_MC, IndicatiaSettings.EQUIPMENT_HUD, IndicatiaSettings.EQUIPMENT_ARMOR_ITEMS, IndicatiaSettings.EQUIPMENT_HAND_ITEMS, IndicatiaSettings.POTION_HUD, IndicatiaSettings.SLIME_CHUNK, IndicatiaSettings.REAL_TIME, IndicatiaSettings.GAME_TIME, IndicatiaSettings.GAME_WEATHER, IndicatiaSettings.MOON_PHASE, IndicatiaSettings.POTION_ICON, IndicatiaSettings.TPS, IndicatiaSettings.TPS_ALL_DIMS, IndicatiaSettings.ALTERNATE_POTION_COLOR);

    public RenderInfoSettingsScreen(Screen parent)
    {
        super(TextComponent.EMPTY);
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addRenderableWidget(new Button(this.width / 2 - 100, this.height - 25, 200, 20, LangUtils.translate("gui.done"), button ->
        {
            IndicatiaSettings.INSTANCE.save();
            this.minecraft.setScreen(this.parent);
        }));

        this.optionsRowList = new ConfigButtonListWidget(this.width, this.height, 16, this.height - 30, 25);
        this.optionsRowList.addAll(OPTIONS);
        this.addWidget(this.optionsRowList);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        IndicatiaSettings.INSTANCE.save();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose()
    {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(poseStack);
        this.optionsRowList.render(poseStack, mouseX, mouseY, partialTicks);
        GuiComponent.drawCenteredString(poseStack, this.font, LangUtils.translate("menu.render_info.title"), this.width / 2, 5, 16777215);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }
}