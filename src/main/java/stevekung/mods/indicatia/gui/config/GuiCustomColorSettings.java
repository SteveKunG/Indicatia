package stevekung.mods.indicatia.gui.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

@OnlyIn(Dist.CLIENT)
public class GuiCustomColorSettings extends Screen
{
    private final Screen parent;

    GuiCustomColorSettings(Screen parent)
    {
        super(JsonUtils.create("Custom Color Settings"));
        this.parent = parent;
    }

    @Override
    public void init()
    {
        this.addButton(new Button(this.width / 2 - 155, this.height / 6 - 12, 150, 20, LangUtils.translate("extended_config.render_info_custom_color.title"), button -> this.minecraft.displayGuiScreen(new GuiRenderInfoCustomColorSettings(this))));
        this.addButton(new Button(this.width / 2 + 10, this.height / 6 - 12, 150, 20, LangUtils.translate("extended_config.keystroke_custom_color.title"), button -> this.minecraft.displayGuiScreen(new GuiKeystrokeCustomColorSettings(this))));
        this.addButton(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, LangUtils.translate("gui.done"), button -> this.minecraft.displayGuiScreen(this.parent)));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        this.drawCenteredString(this.font, LangUtils.translate("extended_config.custom_color.title"), this.width / 2, 15, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }
}