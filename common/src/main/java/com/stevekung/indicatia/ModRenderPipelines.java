package com.stevekung.indicatia;

import com.mojang.renderpearl.api.pipeline.*;
import com.stevekung.indicatia.mixin.accessor.RenderPipelinesAccessor;

import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;

public class ModRenderPipelines
{
    public static final RenderPipeline MOB_SKULL_GLINT = RenderPipelinesAccessor.callRegister(
            RenderPipeline.builder(RenderPipelinesAccessor.getEntitySnippet(), RenderPipelines.GLINT_SNIPPET)
                    .withLocation("pipeline/mob_skull_glint")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withBindGroupLayout(BindGroupLayouts.SAMPLER0_SAMPLER1)
                    .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                    .build());

    public static final RenderPipeline PLAYER_AND_DRAGON_HEAD_GLINT = RenderPipelinesAccessor.callRegister(
            RenderPipeline.builder(RenderPipelinesAccessor.getEntitySnippet(), RenderPipelines.GLINT_SNIPPET)
                    .withLocation("pipeline/player_and_dragon_head_glint")
                    .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                    .withBindGroupLayout(BindGroupLayouts.SAMPLER0_SAMPLER1)
                    .withColorTargetState(new ColorTargetState(BlendFunction.GLINT))
                    .build());
}