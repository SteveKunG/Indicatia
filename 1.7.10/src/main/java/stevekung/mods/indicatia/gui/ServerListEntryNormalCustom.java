package stevekung.mods.indicatia.gui;

import java.net.UnknownHostException;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ServerListEntryNormalCustom extends ServerListEntryNormal
{
    private static final ResourceLocation UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_pack.png");

    protected ServerListEntryNormalCustom(GuiMultiplayerCustom gui, ServerData data)
    {
        super(gui, data);
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected)
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
                    ServerListEntryNormalCustom.this.field_148303_c.func_146789_i().func_147224_a(ServerListEntryNormalCustom.this.field_148301_e);
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

        boolean flag = this.field_148301_e.field_82821_f > 5;
        boolean flag1 = this.field_148301_e.field_82821_f < 5;
        boolean flag2 = flag || flag1;
        this.field_148300_d.fontRenderer.drawString(this.field_148301_e.serverName, x + 32 + 3, y + 1, 16777215);
        List<String> list = this.field_148300_d.fontRenderer.listFormattedStringToWidth(FMLClientHandler.instance().fixDescription(this.field_148301_e.serverMOTD), listWidth - 48 - 2);

        for (int i = 0; i < Math.min(list.size(), 2); ++i)
        {
            this.field_148300_d.fontRenderer.drawString(list.get(i), x + 32 + 3, y + 12 + this.field_148300_d.fontRenderer.FONT_HEIGHT * i, 8421504);
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

        String info = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? this.field_148301_e.gameVersion : "Not supported this version!";
        String s2 = flag2 ? EnumChatFormatting.DARK_RED + info : this.field_148301_e.populationInfo + " " + ping;
        int j = this.field_148300_d.fontRenderer.getStringWidth(s2);
        this.field_148300_d.fontRenderer.drawString(s2, x + listWidth - j - 6, y + 1, 8421504);
        String s = null;

        if (flag2)
        {
            s = this.field_148301_e.field_147412_i;
        }
        else if (this.field_148301_e.field_78841_f && this.field_148301_e.pingToServer != -2L)
        {
            if (this.field_148301_e.pingToServer > 0L)
            {
                s = this.field_148301_e.field_147412_i;
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.field_148301_e.getBase64EncodedIconData() != null && !this.field_148301_e.getBase64EncodedIconData().equals(this.field_148299_g))
        {
            this.field_148299_g = this.field_148301_e.getBase64EncodedIconData();
            this.func_148297_b();
            this.field_148303_c.func_146795_p().saveServerList();
        }

        if (this.field_148305_h != null)
        {
            this.field_148300_d.getTextureManager().bindTexture(this.field_148306_i);
            Gui.func_146110_a(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        }
        else
        {
            this.field_148300_d.getTextureManager().bindTexture(ServerListEntryNormalCustom.UNKNOWN_SERVER);
            Gui.func_146110_a(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
        }

        int i1 = mouseX - x;
        int j1 = mouseY - y;

        String tooltip = FMLClientHandler.instance().enhanceServerListEntry(this, this.field_148301_e, x, listWidth, y, i1, j1);

        if (tooltip != null)
        {
            this.field_148303_c.func_146793_a(tooltip);
        }
        else
        {
            if (i1 >= listWidth - j - 15 - 2 && i1 <= listWidth - 15 - 2 && j1 >= 0 && j1 <= 8)
            {
                this.field_148303_c.func_146793_a(s);
            }
        }
    }
}