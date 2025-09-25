package com.stevekung.indicatia.mixin.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.stevekung.indicatia.SkullModelBase_StateExtender;

import net.minecraft.client.model.SkullModelBase;

@Mixin(SkullModelBase.State.class)
public class MixinSkullModelBase_State implements SkullModelBase_StateExtender
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