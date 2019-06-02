//package stevekung.mods.indicatia.gui.hack;
//
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
//import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
//import net.minecraft.client.network.LanServerQueryManager;
//import net.minecraft.client.options.ServerList;
//import stevekung.mods.stevekungslib.utils.ColorUtils;
//import stevekung.mods.stevekungslib.utils.client.RenderUtils;
//
//@Environment(EnvType.CLIENT)
//@Mixin(MultiplayerScreen.class)
//public class MultiplayerGuiIN extends MultiplayerScreen
//{
//    @Shadow
//    private boolean initialized;
//
//    @Shadow
//    private MultiplayerServerListWidget serverListWidget;
//
//    @Shadow
//    private ServerList serverList;
//
//    @Shadow
//    private LanServerQueryManager.LanServerEntryList lanServers;
//
//    @Shadow
//    private final Screen parent;
//
//    @Shadow
//    private LanServerQueryManager.LanServerDetector lanServerDetector;
//
//    public MultiplayerGuiIN(Screen parent)
//    {
//        super(parent);
//        this.parent = parent;
//    }
//
//    @Override
//    public void onInitialized()
//    {
//        this.minecraft.keyboard.enableRepeatEvents(true);
//        this.buttons.clear();
//
//        if (this.initialized)
//        {
//            this.serverListWidget.updateSize(this.width, this.height, 32, this.height - 64);//serverListSelector.setDimensions
//        }
//        else
//        {
//            this.initialized = true;
//            this.serverList = new ServerList(this.minecraft);//savedServerList
//            this.serverList.loadFile();
//            this.lanServers = new LanServerQueryManager.LanServerEntryList();//lanServerList
//
//            try
//            {
//                this.lanServerDetector = new LanServerQueryManager.LanServerDetector(this.lanServers);//lanServerDetector
//                this.lanServerDetector.start();
//            }
//            catch (Exception e) {}
//
//            this.serverListWidget = new ServerListWidgetIN(this, this.width, this.height, 32, this.height - 64, 36);
//            this.serverListWidget.setUserServers(this.serverList);
//        }
//        this.method_2540();//createButtons
//    }
//
//    @Override
//    protected void method_2534()//refreshServerList
//    {
//        this.minecraft.openScreen(new MultiplayerGuiIN(this.parent));
//    }
//
//    @Override
//    public void render(int mouseX, int mouseY, float partialTicks)
//    {
//        super.render(mouseX, mouseY, partialTicks);
//        RenderUtils.disableLighting();
//        String info1 = "Press <SHIFT> for";
//        String info2 = "server version info";
//        this.minecraft.textRenderer.drawWithShadow(info1, 4, 3, ColorUtils.hexToRgb("#17F9DB"));
//        this.minecraft.textRenderer.drawWithShadow(info2, 4, 3 + this.minecraft.textRenderer.fontHeight + 1, ColorUtils.hexToRgb("#17F9DB"));
//        RenderUtils.enableLighting();
//    }
//}