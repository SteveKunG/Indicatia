package stevekung.mods.indicatia.gui.config;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.stevekunglib.utils.LangUtils;

@OnlyIn(Dist.CLIENT)
public class GuiCustomColorSettings extends GuiScreen
{
    private final GuiScreen parent;

    GuiCustomColorSettings(GuiScreen parent)
    {
        this.parent = parent;
    }

    @Override
    public void initGui()
    {
        this.addButton(new GuiButton(100, this.width / 2 - 155, this.height / 6 - 12, 150, 20, LangUtils.translate("extended_config.render_info_custom_color.title"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                GuiCustomColorSettings.this.mc.displayGuiScreen(new GuiRenderInfoCustomColorSettings(GuiCustomColorSettings.this));
            }
        });
        this.addButton(new GuiButton(101, this.width / 2 + 10, this.height / 6 - 12, 150, 20, LangUtils.translate("extended_config.keystroke_custom_color.title"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                GuiCustomColorSettings.this.mc.displayGuiScreen(new GuiKeystrokeCustomColorSettings(GuiCustomColorSettings.this));
            }
        });
        this.addButton(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, LangUtils.translate("gui.done"))
        {
            @Override
            public void onClick(double mouseX, double mouseZ)
            {
                GuiCustomColorSettings.this.mc.displayGuiScreen(GuiCustomColorSettings.this.parent);
            }
        });
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, LangUtils.translate("extended_config.custom_color.title"), this.width / 2, 15, 16777215);
        super.render(mouseX, mouseY, partialTicks);
    }
}