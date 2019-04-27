//package stevekung.mods.indicatia.gui.hack;
//
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.minecraft.client.gui.Gui;
//import net.minecraft.client.gui.menu.MultiplayerGui;
//import net.minecraft.client.gui.widget.ServerListWidget;
//import net.minecraft.client.network.LanServerQueryManager;
//import net.minecraft.client.settings.ServerList;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import stevekung.mods.stevekunglib.utils.ColorUtils;
//import stevekung.mods.stevekunglib.utils.client.RenderUtils;
//
//@Environment(EnvType.CLIENT)
//@Mixin(MultiplayerGui.class)
//public class MultiplayerGuiIN extends MultiplayerGui
//{
//    @Shadow
//    private boolean field_3048;
//
//    @Shadow
//    private ServerListWidget field_3043;
//
//    @Shadow
//    private ServerList field_3040;
//
//    @Shadow
//    private LanServerQueryManager.LanServerEntryList field_3046;
//
//    @Shadow
//    private final Gui parent;
//
//    @Shadow
//    private LanServerQueryManager.LanServerDetector field_3045;
//
//    public MultiplayerGuiIN(Gui parent)
//    {
//        super(parent);
//        this.parent = parent;
//    }
//
//    @Override
//    public void onInitialized()
//    {
//        this.client.keyboard.enableRepeatEvents(true);
//        this.buttons.clear();
//
//        if (this.field_3048)//initialized
//        {
//            this.field_3043.method_1953(this.width, this.height, 32, this.height - 64);//serverListSelector.setDimensions
//        }
//        else
//        {
//            this.field_3048 = true;
//            this.field_3040 = new ServerList(this.client);//savedServerList
//            this.field_3040.loadFile();
//            this.field_3046 = new LanServerQueryManager.LanServerEntryList();//lanServerList
//
//            try
//            {
//                this.field_3045 = new LanServerQueryManager.LanServerDetector(this.field_3046);//lanServerDetector
//                this.field_3045.start();
//            }
//            catch (Exception e) {}
//
//            this.field_3043 = new ServerListWidgetIN(this, this.width, this.height, 32, this.height - 64, 36);
//            this.field_3043.setUserServers(this.field_3040);
//        }
//        this.method_2540();//createButtons
//    }
//
//    @Override
//    protected void method_2534()//refreshServerList
//    {
//        this.client.openGui(new MultiplayerGuiIN(this.parent));
//    }
//
//    @Override
//    public void draw(int mouseX, int mouseY, float partialTicks)
//    {
//        super.draw(mouseX, mouseY, partialTicks);
//        RenderUtils.disableLighting();
//        String info1 = "Press <SHIFT> for";
//        String info2 = "server version info";
//        this.client.fontRenderer.drawWithShadow(info1, 4, 3, ColorUtils.hexToRgb("#17F9DB"));
//        this.client.fontRenderer.drawWithShadow(info2, 4, 3 + this.client.fontRenderer.fontHeight + 1, ColorUtils.hexToRgb("#17F9DB"));
//        RenderUtils.enableLighting();
//    }
//}