package stevekung.mods.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.StickingOutThingsFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import stevekung.mods.stevekungslib.utils.client.RenderUtils;

@Mixin(StickingOutThingsFeatureRenderer.class)
public abstract class StickingOutThingsFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M>
{
    public StickingOutThingsFeatureRendererMixin(LivingEntityRenderer<T, M> renderer)
    {
        super(renderer);
    }

    @Overwrite
    public void afterRendering()
    {
        RenderUtils.enableLighting();
    }
}