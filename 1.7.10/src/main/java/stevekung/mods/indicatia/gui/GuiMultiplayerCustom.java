package stevekung.mods.indicatia.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;

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
        super.initGui();
        this.field_146801_C = false;

        if (this.field_146801_C)
        {
            this.field_146803_h.func_148122_a(this.width, this.height, 32, this.height - 64);
        }
        else
        {
            this.field_146801_C = true;
            this.field_146803_h = new ServerSelectionListCustom(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
            this.field_146803_h.func_148195_a(this.field_146804_i);
        }
    }

    @Override
    protected void func_146792_q()
    {
        this.mc.displayGuiScreen(new GuiMultiplayerCustom(this.field_146798_g));
    }
}