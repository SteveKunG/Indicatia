package stevekung.mods.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import stevekung.mods.indicatia.config.ConfigManagerIN;

@Mixin(LayerArmorBase.class)
public abstract class LayerArmorBaseMixin implements LayerRenderer<EntityLivingBase>
{
    @Override
    @Overwrite
    public boolean shouldCombineTextures()
    {
        return ConfigManagerIN.indicatia_general.enableOldArmorRender;
    }
}