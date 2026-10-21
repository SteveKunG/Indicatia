package com.stevekung.indicatia.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import net.minecraft.client.renderer.pipeline.RenderPipelines;

@Mixin(RenderPipelines.class)
public interface RenderPipelinesAccessor
{
    @Invoker
    static RenderPipeline callPrecompiled(RenderPipeline pipeline)
    {
        throw new AssertionError("Implemented via mixin");
    }

    //TODO Rewrite
    @Accessor("ENTITY_SNIPPET")
    static RenderPipeline.Snippet getEntitySnippet()
    {
        throw new AssertionError("Implemented via mixin");
    }
}