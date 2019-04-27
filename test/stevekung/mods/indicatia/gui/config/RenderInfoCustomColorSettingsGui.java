package stevekung.mods.indicatia.gui.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.widget.ButtonWidget;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.utils.ColorUtils;
import stevekung.mods.stevekunglib.utils.ColorUtils.RGB;
import stevekung.mods.stevekunglib.utils.LangUtils;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class RenderInfoCustomColorSettingsGui extends Gui
{
    private final Gui parent;
    private ConfigTextFieldWidgetRowList optionsRowList;
    private static final List<ExtendedConfig.Options> OPTIONS = new ArrayList<>();

    static
    {
        OPTIONS.add(ExtendedConfig.Options.FPS_COLOR);
        OPTIONS.add(ExtendedConfig.Options.XYZ_COLOR);
        OPTIONS.add(ExtendedConfig.Options.BIOME_COLOR);
        OPTIONS.add(ExtendedConfig.Options.DIRECTION_COLOR);
        OPTIONS.add(ExtendedConfig.Options.PING_COLOR);
        OPTIONS.add(ExtendedConfig.Options.PING_TO_SECOND_COLOR);
        OPTIONS.add(ExtendedConfig.Options.SERVER_IP_COLOR);
        OPTIONS.add(ExtendedConfig.Options.EQUIPMENT_STATUS_COLOR);
        OPTIONS.add(ExtendedConfig.Options.ARROW_COUNT_COLOR);
        OPTIONS.add(ExtendedConfig.Options.CPS_COLOR);
        OPTIONS.add(ExtendedConfig.Options.RCPS_COLOR);
        OPTIONS.add(ExtendedConfig.Options.SLIME_CHUNK_COLOR);
        OPTIONS.add(ExtendedConfig.Options.TOP_DONATOR_NAME_COLOR);
        OPTIONS.add(ExtendedConfig.Options.RECENT_DONATOR_NAME_COLOR);
        OPTIONS.add(ExtendedConfig.Options.TPS_COLOR);
        OPTIONS.add(ExtendedConfig.Options.REAL_TIME_COLOR);
        OPTIONS.add(ExtendedConfig.Options.GAME_TIME_COLOR);
        OPTIONS.add(ExtendedConfig.Options.GAME_WEATHER_COLOR);
        OPTIONS.add(ExtendedConfig.Options.MOON_PHASE_COLOR);

        //        if (IndicatiaMod.isYoutubeChatLoaded)TODO
        //        {
        //            OPTIONS.add(ExtendedConfig.Options.YTCHAT_VIEW_COUNT_COLOR);
        //        }

        OPTIONS.add(ExtendedConfig.Options.FPS_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.FPS_26_AND_40_COLOR);
        OPTIONS.add(ExtendedConfig.Options.FPS_LOW_25_COLOR);
        OPTIONS.add(ExtendedConfig.Options.XYZ_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.DIRECTION_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.BIOME_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.PING_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.PING_200_AND_300_COLOR);
        OPTIONS.add(ExtendedConfig.Options.PING_300_AND_500_COLOR);
        OPTIONS.add(ExtendedConfig.Options.PING_MAX_500_COLOR);
        OPTIONS.add(ExtendedConfig.Options.SERVER_IP_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.CPS_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.RCPS_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.SLIME_CHUNK_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.TOP_DONATOR_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.RECENT_DONATOR_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.TPS_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.REAL_TIME_HHMMSS_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.REAL_TIME_DDMMYY_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.GAME_TIME_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.GAME_WEATHER_VALUE_COLOR);
        OPTIONS.add(ExtendedConfig.Options.MOON_PHASE_VALUE_COLOR);

        //        if (IndicatiaMod.isYoutubeChatLoaded)TODO
        //        {
        //            OPTIONS.add(ExtendedConfig.Options.YTCHAT_VIEW_COUNT_VALUE_COLOR);
        //        }
    }

    public RenderInfoCustomColorSettingsGui(Gui parent)
    {
        this.parent = parent;
    }

    @Override
    public void onInitialized()
    {
        this.client.keyboard.enableRepeatEvents(true);
        this.addButton(new ButtonWidget(200, this.width / 2 - 105, this.height - 27, 100, 20, LangUtils.translate("gui.done"))
        {
            @Override
            public void onPressed(double mouseX, double mouseY)
            {
                RenderInfoCustomColorSettingsGui.this.optionsRowList.saveCurrentValue();
                ExtendedConfig.save();
                RenderInfoCustomColorSettingsGui.this.client.openGui(RenderInfoCustomColorSettingsGui.this.parent);
            }
        });
        this.addButton(new ButtonWidget(201, this.width / 2 + 5, this.height - 27, 100, 20, LangUtils.translate("message.preview"))
        {
            @Override
            public void onPressed(double mouseX, double mouseY)
            {
                RenderInfoCustomColorSettingsGui.this.optionsRowList.saveCurrentValue();
                ExtendedConfig.save();
                RenderInfoCustomColorSettingsGui.this.client.openGui(new RenderPreviewGui(RenderInfoCustomColorSettingsGui.this, "render_info"));
            }
        });

        ExtendedConfig.Options[] options = new ExtendedConfig.Options[OPTIONS.size()];
        options = OPTIONS.toArray(options);
        this.optionsRowList = new ConfigTextFieldWidgetRowList(this.parent, this.width, this.height, 32, this.height - 32, 25, options);
    }

    @Override
    public void close()
    {
        this.client.keyboard.enableRepeatEvents(false);
    }

    @Override
    public void update()
    {
        this.optionsRowList.updateCursorCounter();
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode)
    {
        if (keyCode == 1)
        {
            ExtendedConfig.save();
        }
        this.optionsRowList.textboxKeyTyped(typedChar, keyCode);
        return super.charTyped(typedChar, keyCode);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        this.optionsRowList.mouseClickedText(mouseX, mouseY, mouseButton);
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state)
    {
        this.optionsRowList.mouseReleased(mouseX, mouseY, state);
        return super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks)
    {
        this.drawBackground();
        this.optionsRowList.draw(mouseX, mouseY, partialTicks);
        this.drawStringCentered(this.fontRenderer, LangUtils.translate("extended_config.render_info_custom_color.title"), this.width / 2, 5, 16777215);

        for (int i = 0; i < this.optionsRowList.getEntries().size(); ++i)
        {
            if (this.optionsRowList.selected == i)
            {
                ExtendedConfig.Options options = this.optionsRowList.getEntries().get(i).getTextField().getOption();
                RGB rgb = ColorUtils.stringToRGB(this.optionsRowList.getEntries().get(i).getTextField().getText());
                this.drawStringCentered(ColorUtils.coloredFontRenderer, LangUtils.translate("message.example") + ": " + rgb.toColoredFont() + options.getTranslation(), this.width / 2, 15, 16777215);
            }
            if (this.optionsRowList.selected == -1)
            {
                this.drawStringCentered(this.fontRenderer, "Color Format is '255,255,255'", this.width / 2, 15, 16777215);
            }
        }
        super.draw(mouseX, mouseY, partialTicks);
    }
}