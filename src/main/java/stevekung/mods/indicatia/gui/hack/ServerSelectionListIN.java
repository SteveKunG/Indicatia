package stevekung.mods.indicatia.gui.hack;

import java.net.UnknownHostException;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.ClientHooks;
import stevekung.mods.stevekungslib.utils.client.ClientUtils;

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

    class ServerListEntryNormalIN extends ServerSelectionList.NormalEntry
    {
        public ServerListEntryNormalIN(MultiplayerScreen gui, ServerData data)
        {
            super(gui, data);
        }

        @Override
        public void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
        {
            if (!this.server.pinged)
            {
                this.server.pinged = true;
                this.server.pingToServer = -2L;
                this.server.serverMOTD = "";
                this.server.populationInfo = "";

                ServerSelectionList.field_214358_b.submit(() ->
                {
                    try
                    {
                        ServerSelectionListIN.this.owner.getOldServerPinger().ping(ServerListEntryNormalIN.this.server);
                    }
                    catch (UnknownHostException e)
                    {
                        ServerListEntryNormalIN.this.server.pingToServer = -1L;
                        ServerListEntryNormalIN.this.server.serverMOTD = TextFormatting.DARK_RED + I18n.format("multiplayer.status.cannot_resolve");
                    }
                    catch (Exception e)
                    {
                        ServerListEntryNormalIN.this.server.pingToServer = -1L;
                        ServerListEntryNormalIN.this.server.serverMOTD = TextFormatting.DARK_RED + I18n.format("multiplayer.status.cannot_connect");
                    }
                });
            }

            if (this.server.serverMOTD.contains("Pinging..."))
            {
                this.server.serverMOTD = "";
            }

            boolean flag = this.server.version > SharedConstants.getVersion().getProtocolVersion();
            boolean flag1 = this.server.version < SharedConstants.getVersion().getProtocolVersion();
            boolean flag2 = flag || flag1;
            this.mc.fontRenderer.drawString(this.server.serverName, x + 32 + 3, y + 1, 16777215);
            List<String> list = this.mc.fontRenderer.listFormattedStringToWidth(ClientHooks.fixDescription(this.server.serverMOTD), entryWidth - 48 - 2);

            for (int i = 0; i < Math.min(list.size(), 2); ++i)
            {
                this.mc.fontRenderer.drawString(list.get(i), x + 32 + 3, y + 12 + this.mc.fontRenderer.FONT_HEIGHT * i, 8421504);
            }

            String ping;
            long responseTime = this.server.pingToServer;
            String responseTimeText = String.valueOf(responseTime);

            if (this.server.serverMOTD.contains(I18n.format("multiplayer.status.cannot_connect")))
            {
                ping = TextFormatting.DARK_RED + "Failed to ping...";
            }
            else if (responseTime < 0L)
            {
                ping = TextFormatting.GRAY + "Pinging...";
            }
            else if (responseTime >= 200 && responseTime < 300)
            {
                ping = TextFormatting.YELLOW + responseTimeText + "ms";
            }
            else if (responseTime >= 300 && responseTime < 500)
            {
                ping = TextFormatting.RED + responseTimeText + "ms";
            }
            else if (responseTime >= 500)
            {
                ping = TextFormatting.DARK_RED + responseTimeText + "ms";
            }
            else
            {
                ping = TextFormatting.GREEN + responseTimeText + "ms";
            }

            String info = ClientUtils.isShiftKeyDown() ? this.server.gameVersion : "Not supported this version!";
            String s2 = flag2 ? TextFormatting.DARK_RED + info : this.server.populationInfo + " " + ping;
            int j = this.mc.fontRenderer.getStringWidth(s2);
            this.mc.fontRenderer.drawString(s2, x + entryWidth - j - 6, y + 1, 8421504);
            String s = null;
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            if (flag2)
            {
                s = this.server.playerList;
            }
            else if (this.server.pinged && this.server.pingToServer != -2L)
            {
                if (this.server.pingToServer > 0L)
                {
                    s = this.server.playerList;
                }
            }

            if (this.server.getBase64EncodedIconData() != null && !this.server.getBase64EncodedIconData().equals(this.lastIconB64))
            {
                this.lastIconB64 = this.server.getBase64EncodedIconData();
                this.prepareServerIcon();
                ServerSelectionListIN.this.owner.getServerList().saveServerList();
            }

            if (this.icon != null)
            {
                this.drawTextureAt(x, y, this.serverIcon);
            }
            else
            {
                this.drawTextureAt(x, y, ServerSelectionList.field_214359_c);
            }

            int i1 = mouseX - x;
            int j1 = mouseY - y;

            /*String tooltip = ClientHooks.enhanceServerListEntry(this, this.server, x + 3, entryWidth - 5, y, i1, j1);

            if (tooltip != null)
            {
                ServerSelectionListIN.this.owner.setHoveringText(tooltip);
            }
            else*/
            {
                if (i1 >= entryWidth - j - 15 - 2 && i1 <= entryWidth - 15 - 2 && j1 >= 0 && j1 <= 8)
                {
                    ServerSelectionListIN.this.owner.setHoveringText(s);
                }
            }
            ClientHooks.drawForgePingInfo(ServerSelectionListIN.this.owner, this.server, x, y, entryWidth, i1, j1);

            if (this.mc.gameSettings.touchscreen || isSelected)
            {
                this.mc.getTextureManager().bindTexture(ServerSelectionList.field_214360_d);
                AbstractGui.fill(x, y, x + 32, y + 32, -1601138544);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                int k1 = mouseX - x;
                int l1 = mouseY - y;

                if (this.canJoin())
                {
                    if (k1 < 32 && k1 > 16)
                    {
                        AbstractGui.blit(x, y, 0.0F, 32.0F, 32, 32, 256, 256);
                    }
                    else
                    {
                        AbstractGui.blit(x, y, 0.0F, 0.0F, 32, 32, 256, 256);
                    }
                }
                if (index > 0)
                {
                    if (k1 < 16 && l1 < 16)
                    {
                        AbstractGui.blit(x, y, 96.0F, 32.0F, 32, 32, 256, 256);
                    }
                    else
                    {
                        AbstractGui.blit(x, y, 96.0F, 0.0F, 32, 32, 256, 256);
                    }
                }
                if (index < ServerSelectionListIN.this.owner.getServerList().countServers() - 1)
                {
                    if (k1 < 16 && l1 > 16)
                    {
                        AbstractGui.blit(x, y, 64.0F, 32.0F, 32, 32, 256, 256);
                    }
                    else
                    {
                        AbstractGui.blit(x, y, 64.0F, 0.0F, 32, 32, 256, 256);
                    }
                }
            }
        }
    }
}