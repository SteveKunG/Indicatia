package com.stevekung.indicatia.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.renderer.pipeline.variant.EntityRenderPipelineVariant;
import net.minecraft.client.renderer.pipeline.variant.VariantValuesList;

@Mixin(EntityRenderPipelineVariant.class)
public interface EntityRenderPipelineVariantAccessor
{
    @Accessor("VALUES")
    static VariantValuesList<EntityRenderPipelineVariant> indicatia$getValues()
    {
        throw new AssertionError("Implemented via mixin");
    }
}