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

public class OffsetSettingsScreen extends Screen
{
    private final Screen parent;
    private ConfigButtonListWidget optionsRowList;
    private static final ImmutableList<AbstractSettings<IndicatiaSettings>> OPTIONS = ImmutableList.of(IndicatiaSettings.ARMOR_HUD_Y, IndicatiaSettings.POTION_HUD_Y, IndicatiaSettings.MAXIMUM_POTION_DISPLAY, IndicatiaSettings.POTION_LENGTH_Y_OFFSET, IndicatiaSettings.POTION_LENGTH_Y_OFFSET_OVERLAP);

    public OffsetSettingsScreen(Screen parent)
    {
        super(TextComponent.EMPTY);
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new Button(this.width / 2 + 5, this.height - 25, 100, 20, LangUtils.translate("gui.done"), button ->
        {
            IndicatiaSettings.INSTANCE.save();
            this.minecraft.setScreen(this.parent);
        }));
        this.addButton(new Button(this.width / 2 - 105, this.height - 25, 100, 20, LangUtils.translate("menu.preview"), button ->
        {
            IndicatiaSettings.INSTANCE.save();
            this.minecraft.setScreen(new OffsetRenderPreviewScreen(this));
        }));

        this.optionsRowList = new ConfigButtonListWidget(this.width, this.height, 16, this.height - 30, 25);
        this.optionsRowList.addAll(OPTIONS);
        this.children.add(this.optionsRowList);
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
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);
        GuiComponent.drawCenteredString(matrixStack, this.font, LangUtils.translate("menu.offset.title"), this.width / 2, 5, 16777215);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}