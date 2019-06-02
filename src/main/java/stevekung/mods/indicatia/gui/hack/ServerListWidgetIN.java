//package stevekung.mods.indicatia.gui.hack;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.google.common.collect.Lists;
//
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.menu.ListEntryRemoteServer;
//import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
//import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
//import net.minecraft.client.options.ServerList;
//
//@Environment(EnvType.CLIENT)
//public class ServerListWidgetIN extends AlwaysSelectedEntryListWidget<MultiplayerServerListWidget.Entry>
//{
//    private final MultiplayerGuiIN owner;
//    private final List<MultiplayerServerListWidget.ServerItem> field_19109 = new ArrayList<>();
//
//    public ServerListWidgetIN(MultiplayerGuiIN gui, int width, int height, int top, int bottom, int slotHeight)
//    {
//        super(gui, MinecraftClient.getInstance(), width, height, top, bottom, slotHeight);
//        this.owner = gui;
//    }
//
//    @Override
//    public void method_20125(ServerList list)
//    {
//        this.field_19109.clear();
//
//        for (int i = 0; i < list.size(); ++i)
//        {
//            this.field_19109.add(new ListEntryRemoteServerIN(this.owner, list.get(i)));
//        }
//    }
//}