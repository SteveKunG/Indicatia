package stevekung.mods.indicatia.gui.hack;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.stevekunglib.utils.ColorUtils;
import stevekung.mods.stevekunglib.utils.client.RenderUtils;

@SideOnly(Side.CLIENT)
public class GuiMultiplayerIN extends GuiMultiplayer
{
    public GuiMultiplayerIN(GuiScreen parent)
    {
        super(parent);
    }

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();

        if (this.initialized)
        {
            this.serverListSelector.setDimensions(this.width, this.height, 32, this.height - 64);
        }
        else
        {
            this.initialized = true;
            this.savedServerList = new ServerList(this.mc);
            this.savedServerList.loadServerList();
            this.lanServerList = new LanServerDetector.LanServerList();

            try
            {
                this.lanServerDetector = new LanServerDetector.ThreadLanServerFind(this.lanServerList);
                this.lanServerDetector.start();
            }
            catch (Exception e) {}

            this.serverListSelector = new ServerSelectionListIN(this, this.width, this.height, 32, this.height - 64, 36);
            this.serverListSelector.updateOnlineServers(this.savedServerList);
        }
        this.createButtons();
    }

    @Override
    protected void refreshServerList()
    {
        this.mc.displayGuiScreen(new GuiMultiplayerIN(this.parentScreen));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderUtils.disableLighting();
        ScaledResolution res = new ScaledResolution(this.mc);
        String info1 = "Press <SHIFT> for";
        String info2 = "server version info";
        this.mc.fontRenderer.drawString(info1, res.getScaledWidth() - 2 - this.mc.fontRenderer.getStringWidth(info1), 3, ColorUtils.hexToRgb("#17F9DB"), true);
        this.mc.fontRenderer.drawString(info2, res.getScaledWidth() - 2 - this.mc.fontRenderer.getStringWidth(info2), 3 + this.mc.fontRenderer.FONT_HEIGHT + 1, ColorUtils.hexToRgb("#17F9DB"), true);
        RenderUtils.enableLighting();
    }
}