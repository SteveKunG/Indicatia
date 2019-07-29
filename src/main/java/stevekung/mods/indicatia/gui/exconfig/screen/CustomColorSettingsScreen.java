package stevekung.mods.indicatia.gui.exconfig.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

@Environment(EnvType.CLIENT)
public class CustomColorSettingsScreen extends Screen
{
    private final Screen parent;

    CustomColorSettingsScreen(Screen parent)
    {
        super(JsonUtils.create("Custom Color Settings"));
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 - 12, 150, 20, LangUtils.translate("extended_config.render_info_custom_color.title"), button ->
        {
            this.minecraft.openScreen(new CustomRenderInfoColorSettingsScreen(this));
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 168, 200, 20, LangUtils.translate("gui.done"), button ->
        {
            this.minecraft.openScreen(this.parent);
        }));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.drawCenteredString(this.minecraft.textRenderer, LangUtils.translate("extended_config.custom_color.title"), this.width / 2, 15, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }
}