package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

@Mixin(ElytraLayer.class)
public abstract class MixinElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M>
{
    public MixinElytraLayer(IEntityRenderer<T, M> renderer)
    {
        super(renderer);
    }

    //    @Override
    //    public boolean shouldCombineTextures()
    //    {
    //        return IndicatiaConfig.GENERAL.enableOldArmorRender.get();
    //    }
}