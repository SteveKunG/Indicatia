package stevekung.mods.indicatia.gui.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.widget.ButtonWidget;
import stevekung.mods.stevekunglib.utils.LangUtils;

@Environment(EnvType.CLIENT)
public class CustomColorSettingsGui extends Gui
{
    private final Gui parent;

    public CustomColorSettingsGui(Gui parent)
    {
        this.parent = parent;
    }

    @Override
    public void onInitialized()
    {
        this.addButton(new ButtonWidget(100, this.width / 2 - 155, this.height / 6 - 12, 150, 20, LangUtils.translate("extended_config.render_info_custom_color.title"))
        {
            @Override
            public void onPressed(double mouseX, double mouseY)
            {
                CustomColorSettingsGui.this.client.openGui(new RenderInfoCustomColorSettingsGui(CustomColorSettingsGui.this));
            }
        });
        this.addButton(new ButtonWidget(101, this.width / 2 + 10, this.height / 6 - 12, 150, 20, LangUtils.translate("extended_config.keystroke_custom_color.title"))
        {
            @Override
            public void onPressed(double mouseX, double mouseY)
            {
                CustomColorSettingsGui.this.client.openGui(new KeystrokeCustomColorSettingsGui(CustomColorSettingsGui.this));
            }
        });
        this.addButton(new ButtonWidget(200, this.width / 2 - 100, this.height / 6 + 168, LangUtils.translate("gui.done"))
        {
            @Override
            public void onPressed(double mouseX, double mouseY)
            {
                CustomColorSettingsGui.this.client.openGui(CustomColorSettingsGui.this.parent);
            }
        });
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks)
    {
        this.drawBackground();
        this.drawStringCentered(this.fontRenderer, LangUtils.translate("extended_config.custom_color.title"), this.width / 2, 15, 16777215);
        super.draw(mouseX, mouseY, partialTicks);
    }
}