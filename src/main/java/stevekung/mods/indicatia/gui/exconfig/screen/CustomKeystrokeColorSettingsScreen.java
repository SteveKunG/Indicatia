package stevekung.mods.indicatia.gui.exconfig.screen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.gui.exconfig.ExtendedConfigOption;
import stevekung.mods.indicatia.gui.exconfig.screen.widget.ConfigTextFieldWidgetList;
import stevekung.mods.indicatia.gui.exconfig.screen.widget.ExtendedTextFieldWidget;
import stevekung.mods.stevekungslib.utils.ColorUtils;
import stevekung.mods.stevekungslib.utils.ColorUtils.RGB;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

@OnlyIn(Dist.CLIENT)
public class CustomKeystrokeColorSettingsScreen extends Screen
{
    private final Screen parent;
    private ConfigTextFieldWidgetList optionsRowList;
    private static final List<ExtendedConfigOption> OPTIONS = new ArrayList<>();

    static
    {
        OPTIONS.add(ExtendedConfig.KEYSTROKE_WASD_COLOR);
        OPTIONS.add(ExtendedConfig.KEYSTROKE_MOUSE_BUTTON_COLOR);
        OPTIONS.add(ExtendedConfig.KEYSTROKE_SPRINT_COLOR);
        OPTIONS.add(ExtendedConfig.KEYSTROKE_SNEAK_COLOR);
        OPTIONS.add(ExtendedConfig.KEYSTROKE_BLOCKING_COLOR);
        OPTIONS.add(ExtendedConfig.KEYSTROKE_CPS_COLOR);
        OPTIONS.add(ExtendedConfig.KEYSTROKE_RCPS_COLOR);

        OPTIONS.add(ExtendedConfig.KEYSTROKE_WASD_RAINBOW);
        OPTIONS.add(ExtendedConfig.KEYSTROKE_MOUSE_BUTTON_RAINBOW);
        OPTIONS.add(ExtendedConfig.KEYSTROKE_SPRINT_RAINBOW);
        OPTIONS.add(ExtendedConfig.KEYSTROKE_SNEAK_RAINBOW);
        OPTIONS.add(ExtendedConfig.KEYSTROKE_BLOCKING_RAINBOW);
        OPTIONS.add(ExtendedConfig.KEYSTROKE_CPS_RAINBOW);
        OPTIONS.add(ExtendedConfig.KEYSTROKE_RCPS_RAINBOW);
    }

    CustomKeystrokeColorSettingsScreen(Screen parent)
    {
        super(JsonUtils.create("Keystroke Custom Color Settings"));
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.minecraft.keyboardListener.enableRepeatEvents(true);
        this.addButton(new Button(this.width / 2 - 105, this.height - 27, 100, 20, LangUtils.translate("gui.done"), button ->
        {
            this.optionsRowList.saveCurrentValue();
            ExtendedConfig.instance.save();
            this.minecraft.displayGuiScreen(this.parent);
        }));
        this.addButton(new Button(this.width / 2 + 5, this.height - 27, 100, 20, LangUtils.translate("menu.preview"), button ->
        {
            this.optionsRowList.saveCurrentValue();
            ExtendedConfig.instance.save();
            this.minecraft.displayGuiScreen(new RenderPreviewScreen(this, "keystroke"));
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