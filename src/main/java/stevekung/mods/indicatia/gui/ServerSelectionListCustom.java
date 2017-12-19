package stevekung.mods.indicatia.gui;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerList;

@SideOnly(Side.CLIENT)
public class ServerSelectionListCustom extends ServerSelectionList
{
    private final GuiMultiplayerCustom owner;
    private final List serverListInternet = new ArrayList<>();

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
            return (GuiListExtended.IGuiListEntry)this.serverListInternet.get(index);
        }
        else
        {
            index -= this.serverListInternet.size();

            if (index == 0)
            {
                return this.field_148196_n;
            }
            else
            {
                --index;
                return (GuiListExtended.IGuiListEntry)this.field_148199_m.get(index);
            }
        }
    }

    @Override
    public int getSize()
    {
        return this.serverListInternet.size() + 1 + this.field_148199_m.size();
    }

    @Override
    public void func_148195_a(ServerList list)
    {
        this.serverListInternet.clear();

        for (int i = 0; i < list.countServers(); ++i)
        {
            this.serverListInternet.add(new ServerListEntryNormalCustom(this.owner, list.getServerData(i)));
        }
    }
}