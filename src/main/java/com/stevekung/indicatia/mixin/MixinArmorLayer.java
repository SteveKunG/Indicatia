package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.stevekung.indicatia.config.IndicatiaConfig;

import net.minecraft.client.renderer.entity.layers.ArmorLayer;

@Mixin(ArmorLayer.class)
public class MixinArmorLayer
{
    @Overwrite
    public boolean shouldCombineTextures()
    {
        return IndicatiaConfig.GENERAL.enableOldArmorRender.get();
    }
}