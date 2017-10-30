package stevekung.mods.indicatia.gui;

import java.net.UnknownHostException;
import java.util.List;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ServerListEntryNormalCustom extends ServerListEntryNormal
{
    protected ServerListEntryNormalCustom(GuiMultiplayerCustom gui, ServerData data)
    {
        super(gui, data);
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
    {
        if (!this.server.pinged)
        {
            this.server.pinged = true;
            this.server.pingToServer = -2L;
            this.server.serverMOTD = "";
            this.server.populationInfo = "";

            EXECUTOR.submit(() ->
            {
                try
                {
                    ServerListEntryNormalCustom.this.owner.getOldServerPinger().ping(ServerListEntryNormalCustom.this.server);
                }
                catch (UnknownHostException e)
                {
                    ServerListEntryNormalCustom.this.server.pingToServer = -1L;
                    ServerListEntryNormalCustom.this.server.serverMOTD = TextFormatting.DARK_RED + "Can\'t resolve hostname";
                }
                catch (Exception e)
                {
                    ServerListEntryNormalCustom.this.server.pingToServer = -1L;
                    ServerListEntryNormalCustom.this.server.serverMOTD = TextFormatting.DARK_RED + "Can\'t connect to server.";
                }
            });
        }

        if (this.server.serverMOTD.contains("Pinging..."))
        {
            this.server.serverMOTD = "";
        }

        boolean flag = this.server.version > 316;
        boolean flag1 = this.server.version < 316;
        boolean flag2 = flag || flag1;
        this.mc.fontRendererObj.drawString(this.server.serverName, x + 32 + 3, y + 1, 16777215);
        List<String> list = this.mc.fontRendererObj.listFormattedStringToWidth(FMLClientHandler.instance().fixDescription(this.server.serverMOTD), listWidth - 48 - 2);

        for (int i = 0; i < Math.min(list.size(), 2); ++i)
        {
            this.mc.fontRendererObj.drawString(list.get(i), x + 32 + 3, y + 12 + this.mc.fontRendererObj.FONT_HEIGHT * i, 8421504);
        }

        String ping = "";
        long responseTime = this.server.pingToServer;
        String responseTimeText = String.valueOf(responseTime);

        if (this.server.serverMOTD.contains("Can\'t connect to server."))
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

        String s2 = flag2 ? TextFormatting.DARK_RED + this.server.gameVersion : this.server.populationInfo + " " + ping;
        int j = this.mc.fontRendererObj.getStringWidth(s2);
        this.mc.fontRendererObj.drawString(s2, x + listWidth - j - 6, y + 1, 8421504);
        String s = null;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

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
            this.owner.getServerList().saveServerList();
        }

        if (this.icon != null)
        {
            this.drawTextureAt(x, y, this.serverIcon);
        }
        else
        {
            this.drawTextureAt(x, y, UNKNOWN_SERVER);
        }

        int i1 = mouseX - x;
        int j1 = mouseY - y;

        String tooltip = FMLClientHandler.instance().enhanceServerListEntry(this, this.server, x, listWidth, y, i1, j1);

        if (tooltip != null)
        {
            this.owner.setHoveringText(tooltip);
        }
        else
        {
            if (i1 >= listWidth - j - 15 - 2 && i1 <= listWidth - 15 - 2 && j1 >= 0 && j1 <= 8)
            {
                this.owner.setHoveringText(s);
            }
        }

        if (this.mc.gameSettings.touchscreen || isSelected)
        {
            this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int k1 = mouseX - x;
            int l1 = mouseY - y;

            if (this.canJoin())
            {
                if (k1 < 32 && k1 > 16)
                {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                }
                else
                {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }
            if (this.owner.canMoveUp(this, slotIndex))
            {
                if (k1 < 16 && l1 < 16)
                {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                }
                else
                {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }
            if (this.owner.canMoveDown(this, slotIndex))
            {
                if (k1 < 16 && l1 > 16)
                {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
                }
                else
                {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
                }
            }
        }
    }
}