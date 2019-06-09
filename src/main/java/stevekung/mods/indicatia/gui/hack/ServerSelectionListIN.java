package stevekung.mods.indicatia.gui.hack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ServerSelectionListIN extends ServerSelectionList
{
    private final GuiMultiplayerIN owner;

    ServerSelectionListIN(GuiMultiplayerIN gui, int width, int height, int top, int bottom, int slotHeight)
    {
        super(gui, Minecraft.getInstance(), width, height, top, bottom, slotHeight);
        this.owner = gui;
    }

    @Override
    public void updateOnlineServers(ServerList list)
    {
        this.serverListInternet.clear();

        for (int i = 0; i < list.countServers(); ++i)
        {
            this.serverListInternet.add(new ServerListEntryNormalIN(this.owner, list.getServerData(i)));
        }
        this.func_195094_h();
    }
}