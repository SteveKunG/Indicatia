package stevekung.mods.indicatia.renderer;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerAllArmor extends LayerBipedArmor
{
    public LayerAllArmor(RenderLivingBase<?> renderer)
    {
        super(renderer);
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return true;
    }
}