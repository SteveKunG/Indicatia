package stevekung.mods.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import stevekung.mods.indicatia.config.ConfigManagerIN;

@Mixin(LayerElytra.class)
public abstract class LayerElytraMixin implements LayerRenderer<EntityLivingBase>
{
    @Override
    @Overwrite
    public boolean shouldCombineTextures()
    {
        return ConfigManagerIN.indicatia_general.enableOldArmorRender;
    }
}