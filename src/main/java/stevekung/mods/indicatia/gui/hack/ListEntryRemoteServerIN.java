//package stevekung.mods.indicatia.gui.hack;
//
//import com.mojang.blaze3d.platform.GlStateManager;
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.minecraft.SharedConstants;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.Drawable;
//import net.minecraft.client.gui.Gui;
//import net.minecraft.client.gui.menu.ListEntryRemoteServer;
//import net.minecraft.client.gui.menu.MultiplayerGui;
//import net.minecraft.client.settings.ServerEntry;
//import net.minecraft.client.texture.NativeImageBackedTexture;
//import net.minecraft.text.TextFormat;
//import net.minecraft.util.Identifier;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import stevekung.mods.stevekunglib.utils.client.ClientUtils;
//
//import java.net.UnknownHostException;
//import java.util.List;
//import java.util.concurrent.ThreadPoolExecutor;
//
//@Environment(EnvType.CLIENT)
//@Mixin(ListEntryRemoteServer.class)
//public class ListEntryRemoteServerIN extends ListEntryRemoteServer
//{
//    @Shadow
//    private ServerEntry serverEntry;
//
//    @Shadow
//    private MinecraftClient client;
//
//    @Shadow
//    private MultiplayerGui guiMultiplayer;
//
//    @Shadow
//    private static ThreadPoolExecutor PING_THREAD_POOL;
//
//    @Shadow
//    private String field_3062;
//
//    @Shadow
//    private NativeImageBackedTexture field_3063;
//
//    @Shadow
//    private Identifier field_3065;
//
//    private static final Identifier UNKNOWN_SERVER = new Identifier("textures/misc/unknown_server.png");
//    private static final Identifier SERVER_SELECTION_BUTTONS = new Identifier("textures/gui/server_selection.png");
//
//    protected ListEntryRemoteServerIN(MultiplayerGuiIN gui, ServerEntry data)
//    {
//        super(gui, data);
//    }
//
//    @Override
//    //int listWidth, int var2, int mouseX, int mouseY, boolean var5, float var6
//    public void draw(int listWidth, int var2, int mouseX, int mouseY, boolean isSelected, float partialTicks)
//    {
//        int x = this.getX();
//        int y = this.getY();
//
//        if (!this.serverEntry.field_3754)//pinged
//        {
//            this.serverEntry.field_3754 = true;
//            this.serverEntry.ping = -2L;
//            this.serverEntry.label = "";
//            this.serverEntry.playerCountLabel = "";
//
//            PING_THREAD_POOL.submit(() ->
//            {
//                try
//                {
//                    ListEntryRemoteServerIN.this.guiMultiplayer.method_2538().method_3003(ListEntryRemoteServerIN.this.serverEntry);//getOldServerPinger ping
//                }
//                catch (UnknownHostException e)
//                {
//                    ListEntryRemoteServerIN.this.serverEntry.ping = -1L;
//                    ListEntryRemoteServerIN.this.serverEntry.label = TextFormat.DARK_RED + "Can\'t resolve hostname";
//                }
//                catch (Exception e)
//                {
//                    ListEntryRemoteServerIN.this.serverEntry.ping = -1L;
//                    ListEntryRemoteServerIN.this.serverEntry.label = TextFormat.DARK_RED + "Can\'t connect to server.";
//                }
//            });
//        }
//
//        if (this.serverEntry.label.contains("Pinging..."))
//        {
//            this.serverEntry.label = "";
//        }
//
//        boolean flag = this.serverEntry.protocolVersion > SharedConstants.getGameVersion().getProtocolVersion();
//        boolean flag1 = this.serverEntry.protocolVersion < SharedConstants.getGameVersion().getProtocolVersion();
//        boolean flag2 = flag || flag1;
//        this.client.fontRenderer.draw(this.serverEntry.name, x + 32 + 3, y + 1, 16777215);
//        List<String> list = this.client.fontRenderer.wrapStringToWidthAsList(this.serverEntry.label, listWidth - 48 - 2);
//
//        for (int i = 0; i < Math.min(list.size(), 2); ++i)
//        {
//            this.client.fontRenderer.draw(list.get(i), x + 32 + 3, y + 12 + this.client.fontRenderer.fontHeight * i, 8421504);
//        }
//
//        String ping = "";
//        long responseTime = this.serverEntry.ping;
//        String responseTimeText = String.valueOf(responseTime);
//
//        if (this.serverEntry.label.contains("Can\'t connect to server."))
//        {
//            ping = TextFormat.DARK_RED + "Failed to ping...";
//        }
//        else if (responseTime < 0L)
//        {
//            ping = TextFormat.GRAY + "Pinging...";
//        }
//        else if (responseTime >= 200 && responseTime < 300)
//        {
//            ping = TextFormat.YELLOW + responseTimeText + "ms";
//        }
//        else if (responseTime >= 300 && responseTime < 500)
//        {
//            ping = TextFormat.RED + responseTimeText + "ms";
//        }
//        else if (responseTime >= 500)
//        {
//            ping = TextFormat.DARK_RED + responseTimeText + "ms";
//        }
//        else
//        {
//            ping = TextFormat.GREEN + responseTimeText + "ms";
//        }
//
//        String info = ClientUtils.isShiftKeyDown() ? this.serverEntry.version : "Not supported this version!";
//        String s2 = flag2 ? TextFormat.DARK_RED + info : this.serverEntry.playerCountLabel + " " + ping;
//        int j = this.client.fontRenderer.getStringWidth(s2);
//        this.client.fontRenderer.draw(s2, x + listWidth - j - 6, y + 1, 8421504);
//        String s = null;
//        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//        if (flag2)
//        {
//            s = this.serverEntry.playerListSummary;
//        }
//        else if (this.serverEntry.field_3754 && this.serverEntry.ping != -2L)
//        {
//            if (this.serverEntry.ping > 0L)
//            {
//                s = this.serverEntry.playerListSummary;
//            }
//        }
//
//        if (this.serverEntry.getIcon() != null && !this.serverEntry.getIcon().equals(this.field_3062))//lastIconB64
//        {
//            this.field_3062 = this.serverEntry.getIcon();
//            this.method_2554();//prepareServerIcon
//            this.guiMultiplayer.method_2529().saveFile();//getServerList
//        }
//
//        if (this.field_3063 != null)
//        {
//            this.drawIcon(x, y, this.field_3065);
//        }
//        else
//        {
//            this.drawIcon(x, y, UNKNOWN_SERVER);
//        }
//
//        int i1 = mouseX - x;
//        int j1 = mouseY - y;
//
//        String tooltip = "STATUS TODO";//FMLClientHandler.instance().enhanceServerListEntry(this, this.serverEntry, x + 3, listWidth - 5, y, i1, j1);TODO
//
//        if (tooltip != null)
//        {
//            this.guiMultiplayer.method_2528(tooltip);//setHoveringText
//        }
//        else
//        {
//            if (i1 >= listWidth - j - 15 - 2 && i1 <= listWidth - 15 - 2 && j1 >= 0 && j1 <= 8)
//            {
//                this.guiMultiplayer.method_2528(s);//setHoveringText
//            }
//        }
//
//        if (this.client.options.touchscreen || isSelected)
//        {
//            this.client.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
//            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
//            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//            int k1 = mouseX - x;
//            int l1 = mouseY - y;
//
//            if (this.method_2558())//canJoin
//            {
//                if (k1 < 32 && k1 > 16)
//                {
//                    Drawable.drawTexturedRect(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
//                }
//                else
//                {
//                    Drawable.drawTexturedRect(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
//                }
//            }
//            if (this.guiMultiplayer.method_2533(this, this.method_1908()))//canMoveUp  slotIndex
//            {
//                if (k1 < 16 && l1 < 16)
//                {
//                    Drawable.drawTexturedRect(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
//                }
//                else
//                {
//                    Drawable.drawTexturedRect(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
//                }
//            }
//            if (this.guiMultiplayer.method_2547(this, this.method_1908()))//canMoveDown  slotIndex
//            {
//                if (k1 < 16 && l1 > 16)
//                {
//                    Drawable.drawTexturedRect(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
//                }
//                else
//                {
//                    Drawable.drawTexturedRect(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
//                }
//            }
//        }
//    }
//}