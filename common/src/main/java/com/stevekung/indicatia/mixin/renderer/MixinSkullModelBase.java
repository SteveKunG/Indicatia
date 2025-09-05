package com.stevekung.indicatia.mixin.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.stevekung.indicatia.SkullModelBaseExtender;

import net.minecraft.client.model.SkullModelBase;

@Mixin(SkullModelBase.class)
public class MixinSkullModelBase implements SkullModelBaseExtender
{
    @Unique
    private boolean hasFoil;

    @Override
    public boolean indicatia$hasFoil()
    {
        return this.hasFoil;
    }

    @Override
    public void indicatia$setFoil(boolean hasFoil)
    {
        this.hasFoil = hasFoil;
    }
}