package stevekung.mods.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;

@Mixin(CapeFeatureRenderer.class)
public abstract class CapeFeatureRendererMixin
{
    @Overwrite
    public boolean hasHurtOverlay()
    {
        return true;
    }
}