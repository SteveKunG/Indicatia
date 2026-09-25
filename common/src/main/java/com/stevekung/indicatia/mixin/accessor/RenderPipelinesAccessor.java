package com.stevekung.indicatia.mixin.accessor;

import com.mojang.renderpearl.api.pipeline.RenderPipeline;

import net.minecraft.client.renderer.RenderPipelines;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

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