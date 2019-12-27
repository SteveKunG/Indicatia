package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;

@Mixin(CapeLayer.class)
public abstract class MixinCapeLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>
{
    public MixinCapeLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer)
    {
        super(renderer);
    }

    //    @Override
    //    public boolean shouldCombineTextures()
    //    {
    //        return IndicatiaConfig.GENERAL.enableOldArmorRender.get();
    //    }
}