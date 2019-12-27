package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArmorLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;

@Mixin(ArmorLayer.class)
public abstract class MixinArmorLayer<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> extends LayerRenderer<T, M>
{
    public MixinArmorLayer(IEntityRenderer<T, M> renderer)
    {
        super(renderer);
    }

    //    @Override
    //    protected boolean func_225622_a_(T p_225622_1_, boolean p_225622_2_)
    //    {
    //       return IndicatiaConfig.GENERAL.enableOldArmorRender.get();
    //    }
}