package stevekung.mods.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin
{
    @Overwrite
    public boolean hasHurtOverlay()
    {
        return true;
    }
}