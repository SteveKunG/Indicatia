package com.stevekung.indicatia;

import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.stevekung.indicatia.mixin.accessor.EntityRenderPipelineVariantAccessor;

import net.minecraft.client.renderer.pipeline.variant.EntityRenderPipelineVariant;

public class ModEntityRenderPipelineVariant
{
    public static final EntityRenderPipelineVariant MOB_SKULL_GLINT = EntityRenderPipelineVariantAccessor.indicatia$getValues()
            .add(new EntityRenderPipelineVariant("mob_skull_glint",
                    new ColorTargetState(BlendFunction.TRANSLUCENT),
                    DepthStencilState.DEFAULT,
                    EntityRenderPipelineVariant.Lighting.DEFAULT,
                    true,
                    true,
                    true,
                    false,
                    true,
                    false,
                    false,
                    false,
                    false));

    public static final EntityRenderPipelineVariant PLAYER_AND_DRAGON_HEAD_GLINT = EntityRenderPipelineVariantAccessor.indicatia$getValues()
            .add(new EntityRenderPipelineVariant("player_and_dragon_head_glint",
                    new ColorTargetState(BlendFunction.GLINT),
                    DepthStencilState.DEFAULT,
                    EntityRenderPipelineVariant.Lighting.DEFAULT,
                    true,
                    true,
                    true,
                    false,
                    true,
                    false,
                    false,
                    false,
                    false));
}