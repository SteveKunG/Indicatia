package com.stevekung.indicatia.gui.exconfig.screen;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.gui.exconfig.DoubleConfigOption;
import com.stevekung.indicatia.gui.exconfig.ExtendedConfigOption;
import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;

import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TextFormatting;

public class ExtendedConfigScreen extends Screen
{
    private static final ExtendedConfigOption[] OPTIONS = new ExtendedConfigOption[] { ExtendedConfig.SWAP_INFO_POS, ExtendedConfig.HEALTH_STATUS, ExtendedConfig.EQUIPMENT_DIRECTION, ExtendedConfig.EQUIPMENT_STATUS,
            ExtendedConfig.EQUIPMENT_POSITION, ExtendedConfig.POTION_HUD_STYLE, ExtendedConfig.POTION_HUD_POSITION, ExtendedConfig.PING_MODE };
    public static boolean PREVIEW;
    private Button resetButton;
    private Button doneButton;

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

        this.addButton(new Button(this.width / 2 - 155, this.height / 6 + 127, 150, 20, LangUtils.translate("menu.render_info.title"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new RenderInfoSettingsScreen(this));
        }));
        this.addButton(new Button(this.width / 2 + 10, this.height / 6 + 127, 150, 20, LangUtils.translate("menu.custom_color.title"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new CustomColorSettingsScreen(this));
        }));
        this.addButton(new Button(this.width / 2 - 155, this.height / 6 + 151, 150, 20, LangUtils.translate("menu.offset.title"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new OffsetSettingsScreen(this));
        }));
        this.addButton(new Button(this.width / 2 + 10, this.height / 6 + 151, 150, 20, LangUtils.translate("menu.hypixel.title"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new HypixelSettingsScreen(this));
        }));
        this.addButton(ExtendedConfig.PREVIEW.createOptionButton(this.width / 2 - 160, this.height / 6 + 175, 160));
        this.addButton(this.doneButton = new Button(this.width / 2 + 5, this.height / 6 + 175, 160, 20, LangUtils.translate("gui.done"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(null);
        }));
        this.addButton(this.resetButton = new Button(this.width / 2 + 87, this.height / 6 + 175, 78, 20, LangUtils.translate("menu.reset_config"), button ->
        {
            ExtendedConfig.INSTANCE.save();
            this.minecraft.displayGuiScreen(new ConfirmScreen(this::resetConfig, LangUtils.translateComponent("menu.reset_config_confirm"), JsonUtils.create("")));
        }));
        this.resetButton.visible = false;
    }

    @Override
    public void tick()
    {
        if (ClientUtils.isShiftKeyDown())
        {
            this.doneButton.setWidth(78);
            this.doneButton.x = this.width / 2 + 5;
            this.resetButton.visible = true;
        }
        else
        {
            this.doneButton.setWidth(160);
            this.doneButton.x = this.width / 2 + 5;
            this.resetButton.visible = false;
        }
    }

    @Override
    public void onClose()
    {
        ExtendedConfigScreen.PREVIEW = false;
        super.onClose();
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
        if (!ExtendedConfigScreen.PREVIEW)
        {
            this.renderBackground();
        }
        this.drawCenteredString(this.font, LangUtils.translate("menu.main.title") + " : " + LangUtils.translate("menu.current_selected_profile", TextFormatting.YELLOW + ExtendedConfig.CURRENT_PROFILE + TextFormatting.RESET), this.width / 2, 10, 16777215);
        super.render(mouseX, mouseY, partialTicks);
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