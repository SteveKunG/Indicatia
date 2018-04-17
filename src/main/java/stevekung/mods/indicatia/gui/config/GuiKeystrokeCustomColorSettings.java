package stevekung.mods.indicatia.gui.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.stevekunglib.utils.ColorUtils;
import stevekung.mods.stevekunglib.utils.ColorUtils.RGB;
import stevekung.mods.stevekunglib.utils.LangUtils;

@SideOnly(Side.CLIENT)
public class GuiKeystrokeCustomColorSettings extends GuiScreen
{
    private final GuiScreen parent;
    private GuiConfigTextFieldRowList optionsRowList;
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

    public GuiKeystrokeCustomColorSettings(GuiScreen parent)
    {
        this.parent = parent;
    }

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(200, this.width / 2 - 105, this.height - 27, 100, 20, LangUtils.translate("gui.done")));
        this.buttonList.add(new GuiButton(201, this.width / 2 + 5, this.height - 27, 100, 20, LangUtils.translate("message.preview")));

        ExtendedConfig.Options[] options = new ExtendedConfig.Options[OPTIONS.size()];
        options = OPTIONS.toArray(options);
        this.optionsRowList = new GuiConfigTextFieldRowList(this.parent, this.width, this.height, 32, this.height - 32, 25, options);
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen()
    {
        this.optionsRowList.updateCursorCounter();
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.optionsRowList.handleMouseInput();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            ExtendedConfig.save();
        }
        this.optionsRowList.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.optionsRowList.mouseClicked(mouseX, mouseY, mouseButton);
        this.optionsRowList.mouseClickedText(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
        this.optionsRowList.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            this.optionsRowList.saveCurrentValue();
            ExtendedConfig.save();

            if (button.id == 200)
            {
                this.mc.displayGuiScreen(this.parent);
            }
            if (button.id == 201)
            {
                this.mc.displayGuiScreen(new GuiRenderPreview(this, "keystroke"));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.optionsRowList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, LangUtils.translate("extended_config.keystroke_custom_color.title"), this.width / 2, 5, 16777215);

        for (int i = 0; i < this.optionsRowList.getSize(); ++i)
        {
            if (this.optionsRowList.selected == i)
            {
                ExtendedConfig.Options options = this.optionsRowList.getListEntry(i).getTextField().getOption();
                RGB rgb = ColorUtils.stringToRGB(this.optionsRowList.getListEntry(i).getTextField().getText());
                this.drawCenteredString(IndicatiaMod.coloredFontRenderer, LangUtils.translate("message.example") + ": " + rgb.toColoredFont() + options.getTranslation(), this.width / 2, 15, 16777215);
            }
            if (this.optionsRowList.selected == -1)
            {
                this.drawCenteredString(this.fontRenderer, "Color Format is '255,255,255'", this.width / 2, 15, 16777215);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}