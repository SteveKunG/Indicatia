package com.stevekung.indicatia;

import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.stevekung.indicatia.mixin.accessor.RenderPipelinesAccessor;

import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;

public class ModRenderPipelines
{
    public static final RenderPipeline ENTITY_TRANSLUCENT_GLINT = RenderPipelinesAccessor.callRegister(
            RenderPipeline.builder(RenderPipelinesAccessor.getEntitySnippet(), RenderPipelines.GLINT_SNIPPET)
        .withLocation("pipeline/entity_translucent_glint")
        .withShaderDefine("ALPHA_CUTOUT", 0.1F)
        .withShaderDefine("PER_FACE_LIGHTING")
        .withShaderDefine("GLINT")
        .withBindGroupLayout(BindGroupLayouts.GLINT_SAMPLER)
        .withBindGroupLayout(BindGroupLayouts.SAMPLER0_SAMPLER1)
        .withColorTargetState(new ColorTargetState(BlendFunction.GLINT))
        .withCull(false)
//        .withVertexShader("core/entity").withFragmentShader("core/entity")
//        .withVertexBinding(0, DefaultVertexFormat.ENTITY_GLINT_SPECIAL)
//        .withPrimitiveTopology(PrimitiveTopology.QUADS).withDepthStencilState(new DepthStencilState(CompareOp.EQUAL, false))
        .build());

//    register(RenderPipeline.builder(GLOBALS_SNIPPET).withBindGroupLayout(BindGroupLayouts.PROJECTION).withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS).withBindGroupLayout(BindGroupLayouts.FOG).withLocation("pipeline/glint").withVertexShader("core/glint").withFragmentShader("core/glint").withBindGroupLayout(BindGroupLayouts.SAMPLER0).withCull(false).withColorTargetState(new ColorTargetState(BlendFunction.GLINT)).withVertexBinding(0,DefaultVertexFormat.POSITION_TEX).withPrimitiveTopology(
//        PrimitiveTopology.QUADS).withDepthStencilState(new DepthStencilState(CompareOp.EQUAL, false)).build());

//    public static final RenderPipeline ENTITY_SOLID_GLINT = RenderPipelinesAccessor.callRegister(RenderPipeline.builder(RenderPipelinesAccessor.getEntitySnippet(), RenderPipelines.GLINT_SNIPPET).withLocation("pipeline/entity_solid_glint").withBindGroupLayout(BindGroupLayouts.SAMPLER1).withColorTargetState(ColorTargetState.DEFAULT).build());
}