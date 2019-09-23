package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.stevekung.indicatia.config.IndicatiaConfig;

import net.minecraft.client.renderer.entity.layers.CapeLayer;

@Mixin(CapeLayer.class)
public abstract class MixinCapeLayer
{
    @Overwrite
    public boolean shouldCombineTextures()
    {
        return IndicatiaConfig.GENERAL.enableOldArmorRender.get();
    }
}