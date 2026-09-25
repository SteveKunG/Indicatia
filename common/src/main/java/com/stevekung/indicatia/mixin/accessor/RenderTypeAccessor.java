package com.stevekung.indicatia.mixin.accessor;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.class)
public interface RenderTypeAccessor
{
    @Invoker
    static RenderType callCreate(String name, RenderSetup state)
    {
        throw new AssertionError("Implemented via mixin");
    }
}