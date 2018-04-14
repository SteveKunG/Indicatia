package stevekung.mods.indicatia.gui.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekunglib.util.LangUtils;

@SideOnly(Side.CLIENT)
public class GuiCustomColorSettings extends GuiScreen
{
    private static final List<ExtendedConfig.Options> OPTIONS = new ArrayList<>();
    private final GuiScreen parent;

    static
    {
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_WASD_RAINBOW);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_MOUSE_BUTTON_RAINBOW);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_SPRINT_RAINBOW);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_SNEAK_RAINBOW);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_BLOCKING_RAINBOW);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_CPS_RAINBOW);
        OPTIONS.add(ExtendedConfig.Options.KEYSTROKE_RCPS_RAINBOW);
    }

    public GuiCustomColorSettings(GuiScreen parent)
    {
        this.parent = parent;
    }

    @Override
    public void initGui()
    {
        int i = 0;

        for (ExtendedConfig.Options options : OPTIONS)
        {
            if (options.isFloat())
            {
                this.buttonList.add(new GuiConfigSlider(options.getOrdinal(), this.width / 2 - 155 + i % 2 * 160, this.height / 6 + 12 + 24 * (i >> 1), 160, options));
            }
            else
            {
                GuiConfigButton button = new GuiConfigButton(options.getOrdinal(), this.width / 2 - 160 + i % 2 * 165, this.height / 6 + 12 + 24 * (i >> 1), 160, options, ExtendedConfig.instance.getKeyBinding(options));
                this.buttonList.add(button);
            }
            ++i;
        }
        this.buttonList.add(new GuiButton(100, this.width / 2 - 155, this.height / 6 - 12, 150, 20, LangUtils.translate("extended_config.render_info_custom_color.title")));
        this.buttonList.add(new GuiButton(101, this.width / 2 + 10, this.height / 6 - 12, 150, 20, LangUtils.translate("extended_config.keystroke_custom_color.title")));
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, LangUtils.translate("gui.done")));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            ExtendedConfig.save();
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            ExtendedConfig.save();

            if (button.id < 100 && button instanceof GuiConfigButton)
            {
                ExtendedConfig.Options options = ((GuiConfigButton)button).getOption();
                ExtendedConfig.instance.setOptionValue(options, 1);
                button.displayString = ExtendedConfig.instance.getKeyBinding(ExtendedConfig.Options.byOrdinal(button.id));
            }
            if (button.id == 100)
            {
                this.mc.displayGuiScreen(new GuiRenderInfoCustomColorSettings(this));
            }
            if (button.id == 101)
            {
                this.mc.displayGuiScreen(new GuiKeystrokeCustomColorSettings(this));
            }
            if (button.id == 200)
            {
                this.mc.displayGuiScreen(this.parent);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, LangUtils.translate("extended_config.custom_color.title"), this.width / 2, 15, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}