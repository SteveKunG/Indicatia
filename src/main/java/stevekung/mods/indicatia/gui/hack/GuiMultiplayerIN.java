package stevekung.mods.indicatia.gui.hack;

import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.stevekungslib.utils.ColorUtils;
import stevekung.mods.stevekungslib.utils.client.RenderUtils;

@OnlyIn(Dist.CLIENT)
public class GuiMultiplayerIN extends MultiplayerScreen
{
    public GuiMultiplayerIN(Screen parent)
    {
        super(parent);
    }

    @Override
    public void init()
    {
        this.minecraft.keyboardListener.enableRepeatEvents(true);

        if (this.initialized)
        {
            this.serverListSelector.setDimensions(this.width, this.height, 32, this.height - 64);
        }
        else
        {
            this.initialized = true;
            this.savedServerList = new ServerList(this.minecraft);
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
    public void refreshServerList()
    {
        this.minecraft.displayGuiScreen(new GuiMultiplayerIN(this.parentScreen));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        super.render(mouseX, mouseY, partialTicks);
        RenderUtils.disableLighting();
        String info1 = "Press <SHIFT> for";
        String info2 = "server version info";
        this.minecraft.fontRenderer.drawStringWithShadow(info1, 4, 3, ColorUtils.hexToRgb("#17F9DB"));
        this.minecraft.fontRenderer.drawStringWithShadow(info2, 4, 3 + this.minecraft.fontRenderer.FONT_HEIGHT + 1, ColorUtils.hexToRgb("#17F9DB"));
        RenderUtils.enableLighting();
    }
}