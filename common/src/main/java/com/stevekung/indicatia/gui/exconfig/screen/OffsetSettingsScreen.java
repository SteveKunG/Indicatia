package com.stevekung.indicatia.gui.exconfig.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.gui.exconfig.screen.widget.ConfigButtonListWidget;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.config.AbstractSettings;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class OffsetSettingsScreen extends Screen
{
    private final Screen parent;
    private ConfigButtonListWidget optionsRowList;
    private static final ImmutableList<AbstractSettings<IndicatiaSettings>> OPTIONS = ImmutableList.of(IndicatiaSettings.ARMOR_HUD_Y, IndicatiaSettings.POTION_HUD_Y, IndicatiaSettings.MAXIMUM_POTION_DISPLAY, IndicatiaSettings.POTION_LENGTH_Y_OFFSET, IndicatiaSettings.POTION_LENGTH_Y_OFFSET_OVERLAP);

    public OffsetSettingsScreen(Screen parent)
    {
        super(StringTextComponent.EMPTY);
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new Button(this.width / 2 + 5, this.height - 25, 100, 20, LangUtils.translate("gui.done"), button ->
        {
            IndicatiaSettings.INSTANCE.save();
            this.minecraft.displayGuiScreen(this.parent);
        }));
        this.addButton(new Button(this.width / 2 - 105, this.height - 25, 100, 20, LangUtils.translate("menu.preview"), button ->
        {
            IndicatiaSettings.INSTANCE.save();
            this.minecraft.displayGuiScreen(new OffsetRenderPreviewScreen(this));
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
    public void closeScreen()
    {
        this.minecraft.displayGuiScreen(this.parent);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);
        AbstractGui.drawCenteredString(matrixStack, this.font, LangUtils.translate("menu.offset.title"), this.width / 2, 5, 16777215);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}