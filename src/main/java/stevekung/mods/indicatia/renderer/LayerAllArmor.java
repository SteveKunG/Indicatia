package stevekung.mods.indicatia.renderer;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerAllArmor<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> extends BipedArmorLayer<T, M, A>
{
    public LayerAllArmor(IEntityRenderer<T, M> renderer, A bipedModel1, A bipedModel2)
    {
        super(renderer, bipedModel1, bipedModel2);
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return true;
    }
}