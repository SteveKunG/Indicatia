package stevekung.mods.indicatia.gui;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMultiplayerCustom extends GuiMultiplayer
{
    public GuiMultiplayerCustom()
    {
        super(new GuiMainMenu());
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.initialized = false;

        if (!this.initialized)
        {
            this.serverListSelector = new ServerSelectionListCustom(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
            this.serverListSelector.updateOnlineServers(this.savedServerList);
        }
        else
        {
            this.serverListSelector.setDimensions(this.width, this.height, 32, this.height - 64);
        }
    }
}