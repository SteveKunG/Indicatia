package stevekung.mods.indicatia.gui.exconfig.screen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.gui.exconfig.ExtendedConfigOption;
import stevekung.mods.indicatia.gui.exconfig.screen.widget.ConfigTextFieldWidgetList;
import stevekung.mods.indicatia.gui.exconfig.screen.widget.ExtendedTextFieldWidget;
import stevekung.mods.stevekungslib.utils.ColorUtils;
import stevekung.mods.stevekungslib.utils.ColorUtils.RGB;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

@OnlyIn(Dist.CLIENT)
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
        OPTIONS.add(ExtendedConfig.CPS_COLOR);
        OPTIONS.add(ExtendedConfig.RCPS_COLOR);
        OPTIONS.add(ExtendedConfig.SLIME_CHUNK_COLOR);
        OPTIONS.add(ExtendedConfig.TOP_DONATOR_NAME_COLOR);
        OPTIONS.add(ExtendedConfig.RECENT_DONATOR_NAME_COLOR);
        OPTIONS.add(ExtendedConfig.TPS_COLOR);
        OPTIONS.add(ExtendedConfig.REAL_TIME_COLOR);
        OPTIONS.add(ExtendedConfig.GAME_TIME_COLOR);
        OPTIONS.add(ExtendedConfig.GAME_WEATHER_COLOR);
        OPTIONS.add(ExtendedConfig.MOON_PHASE_COLOR);

        if (IndicatiaMod.isYoutubeChatLoaded)
        {
            OPTIONS.add(ExtendedConfig.YTCHAT_VIEW_COUNT_COLOR);
        }

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
        OPTIONS.add(ExtendedConfig.CPS_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.RCPS_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.SLIME_CHUNK_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.TOP_DONATOR_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.RECENT_DONATOR_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.TPS_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.REAL_TIME_HHMMSS_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.REAL_TIME_DDMMYY_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.GAME_TIME_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.GAME_WEATHER_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.MOON_PHASE_VALUE_COLOR);

        if (IndicatiaMod.isYoutubeChatLoaded)
        {
            OPTIONS.add(ExtendedConfig.YTCHAT_VIEW_COUNT_VALUE_COLOR);
        }
    }

    CustomRenderInfoColorSettingsScreen(Screen parent)
    {
        super(JsonUtils.create("Render Info Custom Color Settings"));
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.minecraft.keyboardListener.enableRepeatEvents(true);
        this.addButton(new Button(this.width / 2 - 105, this.height - 27, 100, 20, LangUtils.translate("gui.done"), button ->
        {
            CustomRenderInfoColorSettingsScreen.this.optionsRowList.saveCurrentValue();
            ExtendedConfig.instance.save();
            CustomRenderInfoColorSettingsScreen.this.minecraft.displayGuiScreen(CustomRenderInfoColorSettingsScreen.this.parent);
        }));
        this.addButton(new Button(this.width / 2 + 5, this.height - 27, 100, 20, LangUtils.translate("menu.preview"), button ->
        {
            CustomRenderInfoColorSettingsScreen.this.optionsRowList.saveCurrentValue();
            ExtendedConfig.instance.save();
            CustomRenderInfoColorSettingsScreen.this.minecraft.displayGuiScreen(new RenderPreviewScreen(CustomRenderInfoColorSettingsScreen.this, "render_info"));
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
        this.minecraft.keyboardListener.enableRepeatEvents(false);
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
        ExtendedConfig.instance.save();
        this.optionsRowList.saveCurrentValue();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.optionsRowList.render(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.font, LangUtils.translate("extended_config.keystroke_custom_color.title"), this.width / 2, 5, 16777215);
        boolean selected = this.optionsRowList.selected;

        if (selected && this.optionsRowList.getFocused().getTextField() != null)
        {
            ExtendedTextFieldWidget textField = this.optionsRowList.getFocused().getTextField();
            RGB rgb = ColorUtils.stringToRGB(textField.getText());
            this.drawCenteredString(ColorUtils.coloredFontRenderer, LangUtils.translate("menu.example") + ": " + rgb.toColoredFont() + textField.getDisplayName(), this.width / 2, 15, 16777215);
        }
        else
        {
            this.drawCenteredString(this.font, "Color Format is '255,255,255'", this.width / 2, 15, 16777215);
        }
        super.render(mouseX, mouseY, partialTicks);
    }
}