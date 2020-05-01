package com.stevekung.indicatia.gui.exconfig.screen;

import java.util.ArrayList;
import java.util.List;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.gui.exconfig.ExtendedConfigOption;
import com.stevekung.indicatia.gui.exconfig.screen.widget.ConfigTextFieldWidgetList;
import com.stevekung.indicatia.gui.exconfig.screen.widget.ExtendedTextFieldWidget;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.ColorUtils.RGB;
import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

@Environment(EnvType.CLIENT)
public class CustomRenderInfoColorSettingsScreen extends Screen
{
    private final Screen parent;
    private ConfigTextFieldWidgetList optionsRowList;
    private static final List<ExtendedConfigOption> OPTIONS = new ArrayList<>();

    static
    {
        OPTIONS.add(ExtendedConfig.FPS_COLOR);
        OPTIONS.add(ExtendedConfig.XYZ_COLOR);
        OPTIONS.add(ExtendedConfig.BIOME_COLOR);
        OPTIONS.add(ExtendedConfig.DIRECTION_COLOR);
        OPTIONS.add(ExtendedConfig.PING_COLOR);
        OPTIONS.add(ExtendedConfig.PING_TO_SECOND_COLOR);
        OPTIONS.add(ExtendedConfig.SERVER_IP_COLOR);
        OPTIONS.add(ExtendedConfig.EQUIPMENT_STATUS_COLOR);
        OPTIONS.add(ExtendedConfig.ARROW_COUNT_COLOR);
        OPTIONS.add(ExtendedConfig.SLIME_CHUNK_COLOR);
        OPTIONS.add(ExtendedConfig.REAL_TIME_COLOR);
        OPTIONS.add(ExtendedConfig.GAME_TIME_COLOR);
        OPTIONS.add(ExtendedConfig.GAME_WEATHER_COLOR);
        OPTIONS.add(ExtendedConfig.MOON_PHASE_COLOR);

        OPTIONS.add(ExtendedConfig.FPS_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.FPS_26_AND_40_COLOR);
        OPTIONS.add(ExtendedConfig.FPS_LOW_25_COLOR);
        OPTIONS.add(ExtendedConfig.XYZ_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.DIRECTION_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.BIOME_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.PING_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.PING_200_AND_300_COLOR);
        OPTIONS.add(ExtendedConfig.PING_300_AND_500_COLOR);
        OPTIONS.add(ExtendedConfig.PING_MAX_500_COLOR);
        OPTIONS.add(ExtendedConfig.SERVER_IP_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.SLIME_CHUNK_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.REAL_TIME_HHMMSS_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.REAL_TIME_DDMMYY_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.GAME_TIME_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.GAME_WEATHER_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.MOON_PHASE_VALUE_COLOR);
    }

    CustomRenderInfoColorSettingsScreen(Screen parent)
    {
        super(JsonUtils.create("Render Info Custom Color Settings"));
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.minecraft.keyboard.enableRepeatEvents(true);
        this.addButton(new ButtonWidget(this.width / 2 - 105, this.height - 27, 100, 20, LangUtils.translate("gui.done"), button ->
        {
            this.optionsRowList.saveCurrentValue();
            ExtendedConfig.instance.save();
            this.minecraft.openScreen(this.parent);
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 5, this.height - 27, 100, 20, LangUtils.translate("menu.preview"), button ->
        {
            this.optionsRowList.saveCurrentValue();
            ExtendedConfig.instance.save();
            this.minecraft.openScreen(new RenderPreviewScreen(this, "render_info"));
        }));

        this.optionsRowList = new ConfigTextFieldWidgetList(this.width, this.height, 32, this.height - 32, 25);

        for (ExtendedConfigOption option : OPTIONS)
        {
            this.optionsRowList.addButton(option);
        }
        this.children.add(this.optionsRowList);
    }

    @Override
    public void onClose()
    {
        this.minecraft.keyboard.enableRepeatEvents(false);
    }

    @Override
    public void tick()
    {
        this.optionsRowList.tick();
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height)
    {
        this.optionsRowList.resize();
        super.resize(mc, width, height);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        ExtendedConfig.instance.save();
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