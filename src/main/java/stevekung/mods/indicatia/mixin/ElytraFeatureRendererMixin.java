package stevekung.mods.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;

@Mixin(ElytraFeatureRenderer.class)
public abstract class ElytraFeatureRendererMixin
{
    @Overwrite
    public boolean hasHurtOverlay()
    {
        return true;
    }
}