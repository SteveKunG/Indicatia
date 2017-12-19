package stevekung.mods.indicatia.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ServerSelectionListCustom extends ServerSelectionList
{
    private final GuiMultiplayerCustom owner;
    private final List<ServerListEntryNormal> serverListInternet = new ArrayList<>();

    public ServerSelectionListCustom(GuiMultiplayerCustom gui, Minecraft mc, int width, int height, int top, int bottom, int slotHeight)
    {
        super(gui, mc, width, height, top, bottom, slotHeight);
        this.owner = gui;
    }

    @Override
    public GuiListExtended.IGuiListEntry getListEntry(int index)
    {
        if (index < this.serverListInternet.size())
        {
            return this.serverListInternet.get(index);
        }
        else
        {
            index = index - this.serverListInternet.size();

            if (index == 0)
            {
                return this.lanScanEntry;
            }
            else
            {
                --index;
                return this.serverListLan.get(index);
            }
        }
    }

    @Override
    public int getSize()
    {
        return this.serverListInternet.size() + 1 + this.serverListLan.size();
    }

    @Override
    public void updateOnlineServers(ServerList list)
    {
        this.serverListInternet.clear();

        for (int i = 0; i < list.countServers(); ++i)
        {
            this.serverListInternet.add(new ServerListEntryNormalCustom(this.owner, list.getServerData(i)));
        }
    }
}