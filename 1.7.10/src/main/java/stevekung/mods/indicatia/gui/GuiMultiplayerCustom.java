package stevekung.mods.indicatia.gui;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;

@SideOnly(Side.CLIENT)
public class GuiMultiplayerCustom extends GuiMultiplayer
{
    public GuiMultiplayerCustom(GuiScreen parentScreen)
    {
        super(parentScreen);
    }

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();

        if (!this.field_146801_C)
        {
            this.field_146801_C = true;
            this.field_146804_i = new ServerList(this.mc);
            this.field_146804_i.loadServerList();
            this.field_146799_A = new LanServerDetector.LanServerList();

            try
            {
                this.field_146800_B = new LanServerDetector.ThreadLanServerFind(this.field_146799_A);
                this.field_146800_B.start();
            }
            catch (Exception e) {}

            this.field_146803_h = new ServerSelectionListCustom(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
            this.field_146803_h.func_148195_a(this.field_146804_i);
        }
        else
        {
            this.field_146803_h.func_148122_a(this.width, this.height, 32, this.height - 64);
        }
        this.func_146794_g();
    }

    @Override
    protected void func_146792_q()
    {
        this.mc.displayGuiScreen(new GuiMultiplayerCustom(this.field_146798_g));
    }
}