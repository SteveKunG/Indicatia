//package stevekung.mods.indicatia.gui.hack;
//
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.menu.ListEntryRemoteServer;
//import net.minecraft.client.gui.widget.ServerListWidget;
//import net.minecraft.client.settings.ServerList;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Environment(EnvType.CLIENT)
//public class ServerListWidgetIN extends ServerListWidget
//{
//    private final MultiplayerGuiIN owner;
//    private final List<ListEntryRemoteServer> serverListInternet = new ArrayList<>();
//
//    public ServerListWidgetIN(MultiplayerGuiIN gui, int width, int height, int top, int bottom, int slotHeight)
//    {
//        super(gui, MinecraftClient.getInstance(), width, height, top, bottom, slotHeight);
//        this.owner = gui;
//    }
//
//    @Override
//    public void setUserServers(ServerList list)
//    {
//        this.serverListInternet.clear();
//
//        for (int i = 0; i < list.size(); ++i)
//        {
//            this.serverListInternet.add(new ListEntryRemoteServerIN(this.owner, list.get(i)));
//        }
//    }
//}