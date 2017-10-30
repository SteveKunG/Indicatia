package stevekung.mods.indicatia.gui;

import java.net.UnknownHostException;
import java.util.List;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
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
        if (!this.field_148301_e.field_78841_f)
        {
            this.field_148301_e.field_78841_f = true;
            this.field_148301_e.pingToServer = -2L;
            this.field_148301_e.serverMOTD = "";
            this.field_148301_e.populationInfo = "";

            field_148302_b.submit(() ->
            {
                try
                {
                    ServerListEntryNormalCustom.this.field_148303_c.getOldServerPinger().ping(ServerListEntryNormalCustom.this.field_148301_e);
                }
                catch (UnknownHostException e)
                {
                    ServerListEntryNormalCustom.this.field_148301_e.pingToServer = -1L;
                    ServerListEntryNormalCustom.this.field_148301_e.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t resolve hostname";
                }
                catch (Exception e)
                {
                    ServerListEntryNormalCustom.this.field_148301_e.pingToServer = -1L;
                    ServerListEntryNormalCustom.this.field_148301_e.serverMOTD = EnumChatFormatting.DARK_RED + "Can\'t connect to server.";
                }
            });
        }

        if (this.field_148301_e.serverMOTD.contains("Pinging..."))
        {
            this.field_148301_e.serverMOTD = "";
        }

        boolean flag = this.field_148301_e.version > 47;
        boolean flag1 = this.field_148301_e.version < 47;
        boolean flag2 = flag || flag1;
        this.mc.fontRendererObj.drawString(this.field_148301_e.serverName, x + 32 + 3, y + 1, 16777215);
        List<String> list = this.mc.fontRendererObj.listFormattedStringToWidth(FMLClientHandler.instance().fixDescription(this.field_148301_e.serverMOTD), listWidth - 48 - 2);

        for (int i = 0; i < Math.min(list.size(), 2); ++i)
        {
            this.mc.fontRendererObj.drawString(list.get(i), x + 32 + 3, y + 12 + this.mc.fontRendererObj.FONT_HEIGHT * i, 8421504);
        }

        String ping = "";
        long responseTime = this.field_148301_e.pingToServer;
        String responseTimeText = String.valueOf(responseTime);

        if (this.field_148301_e.serverMOTD.contains("Can\'t connect to server."))
        {
            ping = EnumChatFormatting.DARK_RED + "Failed to ping...";
        }
        else if (responseTime < 0L)
        {
            ping = EnumChatFormatting.GRAY + "Pinging...";
        }
        else if (responseTime >= 200 && responseTime < 300)
        {
            ping = EnumChatFormatting.YELLOW + responseTimeText + "ms";
        }
        else if (responseTime >= 300 && responseTime < 500)
        {
            ping = EnumChatFormatting.RED + responseTimeText + "ms";
        }
        else if (responseTime >= 500)
        {
            ping = EnumChatFormatting.DARK_RED + responseTimeText + "ms";
        }
        else
        {
            ping = EnumChatFormatting.GREEN + responseTimeText + "ms";
        }

        String s2 = flag2 ? EnumChatFormatting.DARK_RED + this.field_148301_e.gameVersion : this.field_148301_e.populationInfo + " " + ping;
        int j = this.mc.fontRendererObj.getStringWidth(s2);
        this.mc.fontRendererObj.drawString(s2, x + listWidth - j - 6, y + 1, 8421504);
        String s = null;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (flag2)
        {
            s = this.field_148301_e.playerList;
        }
        else if (this.field_148301_e.field_78841_f && this.field_148301_e.pingToServer != -2L)
        {
            if (this.field_148301_e.pingToServer > 0L)
            {
                s = this.field_148301_e.playerList;
            }
        }

        if (this.field_148301_e.getBase64EncodedIconData() != null && !this.field_148301_e.getBase64EncodedIconData().equals(this.field_148299_g))
        {
            this.field_148299_g = this.field_148301_e.getBase64EncodedIconData();
            this.prepareServerIcon();
            this.field_148303_c.getServerList().saveServerList();
        }

        if (this.field_148305_h != null)
        {
            this.func_178012_a(x, y, this.field_148306_i);
        }
        else
        {
            this.func_178012_a(x, y, UNKNOWN_SERVER);
        }

        int i1 = mouseX - x;
        int j1 = mouseY - y;

        String tooltip = FMLClientHandler.instance().enhanceServerListEntry(this, this.field_148301_e, x, listWidth, y, i1, j1);

        if (tooltip != null)
        {
            this.field_148303_c.setHoveringText(tooltip);
        }
        else
        {
            if (i1 >= listWidth - j - 15 - 2 && i1 <= listWidth - 15 - 2 && j1 >= 0 && j1 <= 8)
            {
                this.field_148303_c.setHoveringText(s);
            }
        }

        if (this.mc.gameSettings.touchscreen || isSelected)
        {
            this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int k1 = mouseX - x;
            int l1 = mouseY - y;

            if (this.func_178013_b())
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
            if (this.field_148303_c.func_175392_a(this, slotIndex))
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
            if (this.field_148303_c.func_175394_b(this, slotIndex))
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