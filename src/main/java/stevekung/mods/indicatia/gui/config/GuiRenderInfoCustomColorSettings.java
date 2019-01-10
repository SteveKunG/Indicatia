package stevekung.mods.indicatia.gui.config;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.stevekunglib.utils.ColorUtils;
import stevekung.mods.stevekunglib.utils.ColorUtils.RGB;
import stevekung.mods.stevekunglib.utils.LangUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiRenderInfoCustomColorSettings extends GuiScreen
{
    private final GuiScreen parent;
    private GuiConfigTextFieldRowList optionsRowList;
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

        if (IndicatiaMod.isYoutubeChatLoaded)
        {
            OPTIONS.add(ExtendedConfig.Options.YTCHAT_VIEW_COUNT_COLOR);
        }

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

        if (IndicatiaMod.isYoutubeChatLoaded)
        {
            OPTIONS.add(ExtendedConfig.Options.YTCHAT_VIEW_COUNT_VALUE_COLOR);
        }
    }

    GuiRenderInfoCustomColorSettings(GuiScreen parent)
    {
        this.parent = parent;
    }

    @Override
    public void initGui()
    {
        this.mc.keyboardListener.enableRepeatEvents(true);
        this.addButton(new GuiButton(200, this.width / 2 - 105, this.height - 27, 100, 20, LangUtils.translate("gui.done"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                GuiRenderInfoCustomColorSettings.this.optionsRowList.saveCurrentValue();
                ExtendedConfig.save();
                GuiRenderInfoCustomColorSettings.this.mc.displayGuiScreen(GuiRenderInfoCustomColorSettings.this.parent);
            }
        });
        this.addButton(new GuiButton(201, this.width / 2 + 5, this.height - 27, 100, 20, LangUtils.translate("message.preview"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                GuiRenderInfoCustomColorSettings.this.optionsRowList.saveCurrentValue();
                ExtendedConfig.save();
                GuiRenderInfoCustomColorSettings.this.mc.displayGuiScreen(new GuiRenderPreview(GuiRenderInfoCustomColorSettings.this, "render_info"));
            }
        });

        ExtendedConfig.Options[] options = new ExtendedConfig.Options[OPTIONS.size()];
        options = OPTIONS.toArray(options);
        this.optionsRowList = new GuiConfigTextFieldRowList(this.parent, this.width, this.height, 32, this.height - 32, 25, options);
        this.children.add(this.optionsRowList);
    }

    @Nullable
    @Override
    public IGuiEventListener getFocused()
    {
        return this.optionsRowList;
    }

    @Override
    public void onGuiClosed()
    {
        this.mc.keyboardListener.enableRepeatEvents(false);
    }

    @Override
    public void tick()
    {
        this.optionsRowList.updateCursorCounter();
    }

    @Override
    public boolean keyPressed(int keyCode, int p_keyPressed_2_, int p_keyPressed_3_)
    {
        ExtendedConfig.save();
        return super.keyPressed(keyCode, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.optionsRowList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, LangUtils.translate("extended_config.render_info_custom_color.title"), this.width / 2, 5, 16777215);

        for (int i = 0; i < this.optionsRowList.getChildren().size(); ++i)
        {
            if (this.optionsRowList.selected == i)
            {
                ExtendedConfig.Options options = this.optionsRowList.getChildren().get(i).getTextField().getOption();
                RGB rgb = ColorUtils.stringToRGB(this.optionsRowList.getChildren().get(i).getTextField().getText());
                this.drawCenteredString(ColorUtils.coloredFontRenderer, LangUtils.translate("message.example") + ": " + rgb.toColoredFont() + options.getTranslation(), this.width / 2, 15, 16777215);
            }
            if (this.optionsRowList.selected == -1)
            {
                this.drawCenteredString(this.fontRenderer, "Color Format is '255,255,255'", this.width / 2, 15, 16777215);
            }
        }
        super.render(mouseX, mouseY, partialTicks);
    }
}