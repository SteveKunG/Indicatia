//package stevekung.mods.indicatia.mixin;
//
//import java.net.UnknownHostException;
//import java.util.List;
//import java.util.concurrent.ThreadPoolExecutor;
//
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//import org.spongepowered.asm.mixin.Shadow;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.Gui;
//import net.minecraft.client.gui.GuiListExtended;
//import net.minecraft.client.gui.GuiMultiplayer;
//import net.minecraft.client.gui.ServerListEntryNormal;
//import net.minecraft.client.multiplayer.ServerData;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.texture.DynamicTexture;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.text.TextFormatting;
//import net.minecraftforge.fml.client.FMLClientHandler;
//import stevekung.mods.indicatia.config.ConfigManagerIN;
//import stevekung.mods.stevekunglib.utils.LangUtils;
//import stevekung.mods.stevekunglib.utils.client.ClientUtils;
//
//@Mixin(ServerListEntryNormal.class)
//public abstract class ServerListEntryNormalMixin implements GuiListExtended.IGuiListEntry
//{
//    private final ServerListEntryNormal that = (ServerListEntryNormal) (Object) this;
//
//    @Shadow
//    @Final
//    private ServerData server;
//
//    @Shadow
//    @Final
//    private Minecraft mc;
//
//    @Shadow
//    @Final
//    private GuiMultiplayer owner;
//
//    @Shadow
//    @Final
//    private static ThreadPoolExecutor EXECUTOR;
//
//    @Shadow
//    @Final
//    private static ResourceLocation UNKNOWN_SERVER;
//
//    @Shadow
//    @Final
//    private static ResourceLocation SERVER_SELECTION_BUTTONS;
//
//    @Shadow
//    private String lastIconB64;
//
//    @Shadow
//    private DynamicTexture icon;
//
//    @Shadow
//    private ResourceLocation serverIcon;
//
//    @Shadow
//    protected abstract void prepareServerIcon();
//
//    @Shadow
//    protected abstract void drawTextureAt(int x, int y, ResourceLocation resource);
//
//    @Shadow
//    protected abstract boolean canJoin();
//
//    @Override
//    @Overwrite
//    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
//    {
//        if (ConfigManagerIN.indicatia_general.enableCustomServerSelectionGui)
//        {
//            if (!this.server.pinged)
//            {
//                this.server.pinged = true;
//                this.server.pingToServer = -2L;
//                this.server.serverMOTD = "";
//                this.server.populationInfo = "";
//
//                EXECUTOR.submit(() ->
//                {
//                    try
//                    {
//                        this.owner.getOldServerPinger().ping(this.server);
//                    }
//                    catch (UnknownHostException e)
//                    {
//                        this.server.pingToServer = -1L;
//                        this.server.serverMOTD = TextFormatting.DARK_RED + LangUtils.translate("multiplayer.status.cannot_resolve");
//                    }
//                    catch (Exception e)
//                    {
//                        this.server.pingToServer = -1L;
//                        this.server.serverMOTD = TextFormatting.DARK_RED + LangUtils.translate("multiplayer.status.cannot_connect");
//                    }
//                });
//            }
//
//            if (this.server.serverMOTD.contains(LangUtils.translate("multiplayer.status.pinging")))
//            {
//                this.server.serverMOTD = "";
//            }
//
//            boolean flag = this.server.version > 340;
//            boolean flag1 = this.server.version < 340;
//            boolean flag2 = flag || flag1;
//            this.mc.fontRenderer.drawString(this.server.serverName, x + 32 + 3, y + 1, 16777215);
//            List<String> list = this.mc.fontRenderer.listFormattedStringToWidth(FMLClientHandler.instance().fixDescription(this.server.serverMOTD), listWidth - 48 - 2);
//
//            for (int i = 0; i < Math.min(list.size(), 2); ++i)
//            {
//                this.mc.fontRenderer.drawString(list.get(i), x + 32 + 3, y + 12 + this.mc.fontRenderer.FONT_HEIGHT * i, 8421504);
//            }
//
//            String ping = "";
//            long responseTime = this.server.pingToServer;
//            String responseTimeText = String.valueOf(responseTime);
//
//            if (this.server.serverMOTD.contains(LangUtils.translate("multiplayer.status.cannot_connect")))
//            {
//                ping = TextFormatting.DARK_RED + "Failed to ping...";
//            }
//            else if (responseTime < 0L)
//            {
//                ping = TextFormatting.GRAY + LangUtils.translate("multiplayer.status.pinging");
//            }
//            else if (responseTime >= 200 && responseTime < 300)
//            {
//                ping = TextFormatting.YELLOW + responseTimeText + "ms";
//            }
//            else if (responseTime >= 300 && responseTime < 500)
//            {
//                ping = TextFormatting.RED + responseTimeText + "ms";
//            }
//            else if (responseTime >= 500)
//            {
//                ping = TextFormatting.DARK_RED + responseTimeText + "ms";
//            }
//            else
//            {
//                ping = TextFormatting.GREEN + responseTimeText + "ms";
//            }
//
//            String info = ClientUtils.isShiftKeyDown() ? this.server.gameVersion : "Not supported this version!";
//            String s2 = flag2 ? TextFormatting.DARK_RED + info : this.server.populationInfo + " " + ping;
//            int j = this.mc.fontRenderer.getStringWidth(s2);
//            this.mc.fontRenderer.drawString(s2, x + listWidth - j - 6, y + 1, 8421504);
//            String s = null;
//            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//
//            if (flag2)
//            {
//                s = this.server.playerList;
//            }
//            else if (this.server.pinged && this.server.pingToServer != -2L)
//            {
//                if (this.server.pingToServer > 0L)
//                {
//                    s = this.server.playerList;
//                }
//            }
//
//            if (this.server.getBase64EncodedIconData() != null && !this.server.getBase64EncodedIconData().equals(this.lastIconB64))
//            {
//                this.lastIconB64 = this.server.getBase64EncodedIconData();
//                this.prepareServerIcon();
//                this.owner.getServerList().saveServerList();
//            }
//
//            if (this.icon != null)
//            {
//                this.drawTextureAt(x, y, this.serverIcon);
//            }
//            else
//            {
//                this.drawTextureAt(x, y, UNKNOWN_SERVER);
//            }
//
//            int i1 = mouseX - x;
//            int j1 = mouseY - y;
//
//            String tooltip = FMLClientHandler.instance().enhanceServerListEntry(this.that, this.server, x + 3, listWidth - 5, y, i1, j1);
//
//            if (tooltip != null)
//            {
//                this.owner.setHoveringText(tooltip);
//            }
//            else
//            {
//                if (i1 >= listWidth - j - 15 - 2 && i1 <= listWidth - 15 - 2 && j1 >= 0 && j1 <= 8)
//                {
//                    this.owner.setHoveringText(s);
//                }
//            }
//
//            if (this.mc.gameSettings.touchscreen || isSelected)
//            {
//                this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
//                Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
//                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//                int k1 = mouseX - x;
//                int l1 = mouseY - y;
//
//                if (this.canJoin())
//                {
//                    if (k1 < 32 && k1 > 16)
//                    {
//                        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
//                    }
//                    else
//                    {
//                        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
//                    }
//                }
//                if (this.owner.canMoveUp(this.that, slotIndex))
//                {
//                    if (k1 < 16 && l1 < 16)
//                    {
//                        Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
//                    }
//                    else
//                    {
//                        Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
//                    }
//                }
//                if (this.owner.canMoveDown(this.that, slotIndex))
//                {
//                    if (k1 < 16 && l1 > 16)
//                    {
//                        Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
//                    }
//                    else
//                    {
//                        Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
//                    }
//                }
//            }
//        }
//        else
//        {
//            if (!this.server.pinged)
//            {
//                this.server.pinged = true;
//                this.server.pingToServer = -2L;
//                this.server.serverMOTD = "";
//                this.server.populationInfo = "";
//
//                EXECUTOR.submit(() ->
//                {
//                    try
//                    {
//                        this.owner.getOldServerPinger().ping(this.server);
//                    }
//                    catch (UnknownHostException var2)
//                    {
//                        this.server.pingToServer = -1L;
//                        this.server.serverMOTD = TextFormatting.DARK_RED + LangUtils.translate("multiplayer.status.cannot_resolve");
//                    }
//                    catch (Exception var3)
//                    {
//                        this.server.pingToServer = -1L;
//                        this.server.serverMOTD = TextFormatting.DARK_RED + LangUtils.translate("multiplayer.status.cannot_connect");
//                    }
//                });
//            }
//
//            boolean flag = this.server.version > 340;
//            boolean flag1 = this.server.version < 340;
//            boolean flag2 = flag || flag1;
//            this.mc.fontRenderer.drawString(this.server.serverName, x + 32 + 3, y + 1, 16777215);
//            List<String> list = this.mc.fontRenderer.listFormattedStringToWidth(FMLClientHandler.instance().fixDescription(this.server.serverMOTD), listWidth - 32 - 2);
//
//            for (int i = 0; i < Math.min(list.size(), 2); ++i)
//            {
//                this.mc.fontRenderer.drawString(list.get(i), x + 32 + 3, y + 12 + this.mc.fontRenderer.FONT_HEIGHT * i, 8421504);
//            }
//
//            String s2 = flag2 ? TextFormatting.DARK_RED + this.server.gameVersion : this.server.populationInfo;
//            int j = this.mc.fontRenderer.getStringWidth(s2);
//            this.mc.fontRenderer.drawString(s2, x + listWidth - j - 15 - 2, y + 1, 8421504);
//            int k = 0;
//            String s = null;
//            int l;
//            String s1;
//
//            if (flag2)
//            {
//                l = 5;
//                s1 = LangUtils.translate(flag ? "multiplayer.status.client_out_of_date" : "multiplayer.status.server_out_of_date");
//                s = this.server.playerList;
//            }
//            else if (this.server.pinged && this.server.pingToServer != -2L)
//            {
//                if (this.server.pingToServer < 0L)
//                {
//                    l = 5;
//                }
//                else if (this.server.pingToServer < 150L)
//                {
//                    l = 0;
//                }
//                else if (this.server.pingToServer < 300L)
//                {
//                    l = 1;
//                }
//                else if (this.server.pingToServer < 600L)
//                {
//                    l = 2;
//                }
//                else if (this.server.pingToServer < 1000L)
//                {
//                    l = 3;
//                }
//                else
//                {
//                    l = 4;
//                }
//
//                if (this.server.pingToServer < 0L)
//                {
//                    s1 = LangUtils.translate("multiplayer.status.no_connection");
//                }
//                else
//                {
//                    s1 = this.server.pingToServer + "ms";
//                    s = this.server.playerList;
//                }
//            }
//            else
//            {
//                k = 1;
//                l = (int)(Minecraft.getSystemTime() / 100L + slotIndex * 2 & 7L);
//
//                if (l > 4)
//                {
//                    l = 8 - l;
//                }
//                s1 = LangUtils.translate("multiplayer.status.pinging");
//            }
//
//            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//            this.mc.getTextureManager().bindTexture(Gui.ICONS);
//            Gui.drawModalRectWithCustomSizedTexture(x + listWidth - 15, y, k * 10, 176 + l * 8, 10, 8, 256.0F, 256.0F);
//
//            if (this.server.getBase64EncodedIconData() != null && !this.server.getBase64EncodedIconData().equals(this.lastIconB64))
//            {
//                this.lastIconB64 = this.server.getBase64EncodedIconData();
//                this.prepareServerIcon();
//                this.owner.getServerList().saveServerList();
//            }
//
//            if (this.icon != null)
//            {
//                this.drawTextureAt(x, y, this.serverIcon);
//            }
//            else
//            {
//                this.drawTextureAt(x, y, UNKNOWN_SERVER);
//            }
//
//            int i1 = mouseX - x;
//            int j1 = mouseY - y;
//
//            String tooltip = FMLClientHandler.instance().enhanceServerListEntry(this.that, this.server, x, listWidth, y, i1, j1);
//
//            if (tooltip != null)
//            {
//                this.owner.setHoveringText(tooltip);
//            }
//            else
//            {
//                if (i1 >= listWidth - 15 && i1 <= listWidth - 5 && j1 >= 0 && j1 <= 8)
//                {
//                    this.owner.setHoveringText(s1);
//                }
//                else if (i1 >= listWidth - j - 15 - 2 && i1 <= listWidth - 15 - 2 && j1 >= 0 && j1 <= 8)
//                {
//                    this.owner.setHoveringText(s);
//                }
//            }
//
//            if (this.mc.gameSettings.touchscreen || isSelected)
//            {
//                this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
//                Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
//                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//                int k1 = mouseX - x;
//                int l1 = mouseY - y;
//
//                if (this.canJoin())
//                {
//                    if (k1 < 32 && k1 > 16)
//                    {
//                        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
//                    }
//                    else
//                    {
//                        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
//                    }
//                }
//                if (this.owner.canMoveUp(this.that, slotIndex))
//                {
//                    if (k1 < 16 && l1 < 16)
//                    {
//                        Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
//                    }
//                    else
//                    {
//                        Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
//                    }
//                }
//                if (this.owner.canMoveDown(this.that, slotIndex))
//                {
//                    if (k1 < 16 && l1 > 16)
//                    {
//                        Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
//                    }
//                    else
//                    {
//                        Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
//                    }
//                }
//            }
//        }
//    }
//}