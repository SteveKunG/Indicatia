package com.stevekung.indicatia.mixin.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.stevekung.indicatia.EnchantedFoilExtender;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

@Mixin(LivingEntityRenderState.class)
public class MixinLivingEntityRenderState implements EnchantedFoilExtender
{
    @Unique
    private boolean hasFoil;

    @Override
    public void indicatia$setFoil(boolean hasFoil)
    {
        this.hasFoil = hasFoil;
    }

    @Override
    public boolean indicatia$hasFoil()
    {
        return this.hasFoil;
    }
}