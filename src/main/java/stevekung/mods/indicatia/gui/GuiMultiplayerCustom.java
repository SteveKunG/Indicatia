package stevekung.mods.indicatia.gui;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.util.RenderUtil;

@SideOnly(Side.CLIENT)
public class GuiMultiplayerCustom extends GuiMultiplayer
{
    public GuiMultiplayerCustom(GuiScreen parentScreen)
    {
        super(parentScreen);
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

            this.serverListSelector = new ServerSelectionListCustom(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
            this.serverListSelector.updateOnlineServers(this.savedServerList);
        }
        this.createButtons();
    }

    @Override
    protected void refreshServerList()
    {
        this.mc.displayGuiScreen(new GuiMultiplayerCustom(this.parentScreen));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderUtil.renderLight(false);
        ScaledResolution res = new ScaledResolution(this.mc);
        this.mc.fontRenderer.drawString("Press <SHIFT> for", res.getScaledWidth() - this.mc.fontRenderer.getStringWidth("Press <SHIFT> for") - 2, res.getScaledHeight() - 20, RenderUtil.hexToRgb("#17F9DB"), true);
        this.mc.fontRenderer.drawString("server version info", res.getScaledWidth() - this.mc.fontRenderer.getStringWidth("server version info") - 2, res.getScaledHeight() - 10, RenderUtil.hexToRgb("#17F9DB"), true);
        RenderUtil.renderLight(true);
    }
}