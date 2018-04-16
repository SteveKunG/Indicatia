package stevekung.mods.indicatia.gui.config;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.stevekunglib.util.LangUtils;

@SideOnly(Side.CLIENT)
public class GuiCustomColorSettings extends GuiScreen
{
    private final GuiScreen parent;

    public GuiCustomColorSettings(GuiScreen parent)
    {
        this.parent = parent;
    }

    @Override
    public void initGui()
    {
        this.buttonList.add(new GuiButton(100, this.width / 2 - 155, this.height / 6 - 12, 150, 20, LangUtils.translate("extended_config.render_info_custom_color.title")));
        this.buttonList.add(new GuiButton(101, this.width / 2 + 10, this.height / 6 - 12, 150, 20, LangUtils.translate("extended_config.keystroke_custom_color.title")));
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, LangUtils.translate("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
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