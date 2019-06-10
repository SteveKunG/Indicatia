package stevekung.mods.indicatia.gui.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekungslib.utils.ColorUtils;
import stevekung.mods.stevekungslib.utils.ColorUtils.RGB;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

@OnlyIn(Dist.CLIENT)
public class GuiKeystrokeCustomColorSettings extends Screen
{
    private final Screen parent;
    private GuiConfigTextFieldRowList optionsRowList;
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

    GuiKeystrokeCustomColorSettings(Screen parent)
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
            GuiKeystrokeCustomColorSettings.this.optionsRowList.saveCurrentValue();
            ExtendedConfig.instance.save();
            GuiKeystrokeCustomColorSettings.this.minecraft.displayGuiScreen(GuiKeystrokeCustomColorSettings.this.parent);
        }));
        this.addButton(new Button(this.width / 2 + 5, this.height - 27, 100, 20, LangUtils.translate("menu.preview"), button ->
        {
            GuiKeystrokeCustomColorSettings.this.optionsRowList.saveCurrentValue();
            ExtendedConfig.instance.save();
            GuiKeystrokeCustomColorSettings.this.minecraft.displayGuiScreen(new GuiRenderPreview(GuiKeystrokeCustomColorSettings.this, "keystroke"));
        }));

        ExtendedConfig.Options[] options = new ExtendedConfig.Options[OPTIONS.size()];
        options = OPTIONS.toArray(options);
        this.optionsRowList = new GuiConfigTextFieldRowList(this.width, this.height, 32, this.height - 32, 25, options);
        this.children.add(this.optionsRowList);
        this.children.addAll(this.optionsRowList.getTextField());
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
        this.optionsRowList.onResize();
        super.resize(mc, width, height);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        ExtendedConfig.instance.save();
        this.optionsRowList.saveCurrentValue();
        this.optionsRowList.keyPressedText(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        this.optionsRowList.charTypedText(codePoint, modifiers);
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.optionsRowList.render(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.font, LangUtils.translate("extended_config.keystroke_custom_color.title"), this.width / 2, 5, 16777215);
        int index = this.optionsRowList.selected;

        if (index != -1)
        {
            ExtendedConfig.Options options = this.optionsRowList.getChildren().get(index).getTextField().getOption();
            RGB rgb = ColorUtils.stringToRGB(this.optionsRowList.getChildren().get(index).getTextField().getText());
            this.drawCenteredString(ColorUtils.coloredFontRenderer, LangUtils.translate("menu.example") + ": " + rgb.toColoredFont() + options.getTranslation(), this.width / 2, 15, 16777215);
        }
        else
        {
            this.drawCenteredString(this.font, "Color Format is '255,255,255'", this.width / 2, 15, 16777215);
        }
        super.render(mouseX, mouseY, partialTicks);
    }
}