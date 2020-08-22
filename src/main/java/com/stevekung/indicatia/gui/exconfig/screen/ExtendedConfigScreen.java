package com.stevekung.indicatia.gui.exconfig.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.gui.exconfig.DoubleConfigOption;
import com.stevekung.indicatia.gui.exconfig.ExtendedConfigOption;
import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TextFormatting;

public class ExtendedConfigScreen extends Screen
{
    private static final ExtendedConfigOption[] OPTIONS = new ExtendedConfigOption[] { ExtendedConfig.SWAP_INFO_POS, ExtendedConfig.EQUIPMENT_DIRECTION, ExtendedConfig.EQUIPMENT_STATUS,
            ExtendedConfig.EQUIPMENT_POSITION, ExtendedConfig.POTION_HUD_STYLE, ExtendedConfig.POTION_HUD_POSITION, ExtendedConfig.PING_MODE };

    public ExtendedConfigScreen()
    {
        super(NarratorChatListener.EMPTY);
    }

    @Override
    public void init()
    {
        int i = 0;

        for (ExtendedConfigOption options : OPTIONS)
        {
            if (options instanceof DoubleConfigOption)
            {
                this.addButton(options.createOptionButton(this.width / 2 - 155 + i % 2 * 160, this.height / 6 - 17 + 24 * (i >> 1), 160));
            }
            else
            {
                this.addButton(options.createOptionButton(this.width / 2 - 160 + i % 2 * 165, this.height / 6 - 17 + 24 * (i >> 1), 160));
            }
            ++i;
        }

        this.addButton(new Button(this.width / 2 - 155, this.height / 6 + 127, 150, 20, LangUtils.translateComponent("menu.render_info.title"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new RenderInfoSettingsScreen(this));
        }));
        this.addButton(new Button(this.width / 2 + 10, this.height / 6 + 127, 150, 20, LangUtils.translateComponent("menu.custom_color.title"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new CustomColorSettingsScreen(this));
        }));
        this.addButton(new Button(this.width / 2 - 155, this.height / 6 + 151, 150, 20, LangUtils.translateComponent("menu.offset.title"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new OffsetSettingsScreen(this));
        }));
        this.addButton(new Button(this.width / 2 + 10, this.height / 6 + 151, 150, 20, LangUtils.translateComponent("menu.hypixel.title"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new HypixelSettingsScreen(this));
        }));
        this.addButton(new Button(this.width / 2 + 5, this.height / 6 + 175, 160, 20, LangUtils.translateComponent("gui.done"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(null);
        }));
        this.addButton(new Button(this.width / 2 - 160, this.height / 6 + 175, 160, 20, LangUtils.translateComponent("menu.reset_config"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new ConfirmScreen(this::resetConfig, LangUtils.translateComponent("menu.reset_config_confirm"), JsonUtils.create("")));
        }));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        ExtendedConfig.INSTANCE.save();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        AbstractGui.drawCenteredString(matrixStack, this.font, LangUtils.translateComponent("menu.main.title") + " : " + LangUtils.translateComponent("menu.current_selected_profile", TextFormatting.YELLOW + ExtendedConfig.CURRENT_PROFILE + TextFormatting.RESET), this.width / 2, 10, 16777215);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void resetConfig(boolean condition)
    {
        if (condition)
        {
            ExtendedConfig.resetConfig();
            this.minecraft.displayGuiScreen(this);
        }
        else
        {
            this.minecraft.displayGuiScreen(this);
        }
    }
}