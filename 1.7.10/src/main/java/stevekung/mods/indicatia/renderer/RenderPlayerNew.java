package stevekung.mods.indicatia.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderPlayer;

@SideOnly(Side.CLIENT)
public class RenderPlayerNew extends RenderPlayer
{
    public RenderPlayerNew()
    {
        super();
        this.mainModel = new ModelBipedNew(0.0F);
        this.modelBipedMain = (ModelBipedNew)this.mainModel;
        this.modelArmorChestplate = new ModelBipedNew(1.0F);
        this.modelArmor = new ModelBipedNew(0.5F);
    }
}