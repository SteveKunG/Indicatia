package stevekung.mods.indicatia.renderer;

import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderArmorStandNew extends ArmorStandRenderer
{
    public RenderArmorStandNew(RenderManager manager)
    {
        super(manager);
        this.layerRenderers.clear();
        LayerBipedArmor armor = new LayerBipedArmor(this)
        {
            @Override
            protected void initArmor()
            {
                this.field_177189_c = new ModelArmorStandArmor(0.5F);
                this.field_177186_d = new ModelArmorStandArmor(1.0F);
            }
        };
        this.addLayer(armor);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerCustomHeadNew(this.getMainModel().bipedHead));
    }
}