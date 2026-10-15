package com.stevekung.indicatia.mixin.accessor;

import net.minecraft.client.renderer.RenderPipelines;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.blaze3d.pipeline.RenderPipeline;

@Mixin(RenderPipelines.class)
public interface RenderPipelinesAccessor
{
    @Invoker
    static RenderPipeline callRegister(RenderPipeline pipeline)
    {
        throw new AssertionError("Implemented via mixin");
    }

    @Accessor("ENTITY_SNIPPET")
    static RenderPipeline.Snippet getEntitySnippet()
    {
        throw new AssertionError("Implemented via mixin");
    }
}