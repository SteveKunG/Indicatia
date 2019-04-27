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
public class KeystrokeCustomColorSettingsGui extends Gui
{
    private final Gui parent;
    private ConfigTextFieldWidgetRowList optionsRowList;
    private static final List<ExtendedConfig.Options> OPTIONS = new ArrayList<>();

    static
    {
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_WASD_COLOR);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_MOUSE_BUTTON_COLOR);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_SPRINT_COLOR);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_SNEAK_COLOR);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_BLOCKING_COLOR);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_CPS_COLOR);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_RCPS_COLOR);

        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_WASD_RAINBOW);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_MOUSE_BUTTON_RAINBOW);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_SPRINT_RAINBOW);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_SNEAK_RAINBOW);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_BLOCKING_RAINBOW);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_CPS_RAINBOW);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_RCPS_RAINBOW);
    }

    KeystrokeCustomColorSettingsGui(Gui parent)
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
                KeystrokeCustomColorSettingsGui.this.client.openGui(new CustomColorSettingsGui(KeystrokeCustomColorSettingsGui.this.parent));
            }
        });
        this.addButton(new ButtonWidget(201, this.width / 2 + 5, this.height - 27, 100, 20, LangUtils.translate("message.preview"))
        {
            @Override
            public void onPressed(double mouseX, double mouseY)
            {
                KeystrokeCustomColorSettingsGui.this.client.openGui(new RenderPreviewGui(KeystrokeCustomColorSettingsGui.this, "keystroke"));
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
        this.optionsRowList.mouseClicked(mouseX, mouseY, mouseButton);
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
        this.drawStringCentered(this.fontRenderer, LangUtils.translate("extended_config.keystroke_custom_color.title"), this.width / 2, 5, 16777215);

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