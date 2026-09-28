package com.stevekung.indicatia;

import java.util.function.Function;

import com.stevekung.indicatia.mixin.accessor.RenderTypeAccessor;

import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.TextureTransform;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

public class ModRenderTypes
{
    private static final Function<Identifier, RenderType> MOB_SKULL_GLINT = Util.memoize(texture ->
    {
        var state = RenderSetup.builder(ModRenderPipelines.MOB_SKULL_GLINT)
                            .withTexture("Sampler0", texture)
                            .withTexture("Sampler1", texture)
                            .withTexture("GlintSampler", ItemFeatureRenderer.ENCHANTED_GLINT_ITEM)
                            .setTextureTransform(TextureTransform.ENTITY_GLINT_TEXTURING)
                            .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
                            .createRenderSetup();
        return RenderTypeAccessor.callCreate("mob_skull_glint", state);
    });

    private static final Function<Identifier, RenderType> PLAYER_AND_DRAGON_HEAD_GLINT = Util.memoize(texture ->
    {
        var state = RenderSetup.builder(ModRenderPipelines.PLAYER_AND_DRAGON_HEAD_GLINT)
                          .withTexture("Sampler0", texture)
                          .withTexture("GlintSampler", ItemFeatureRenderer.ENCHANTED_GLINT_ITEM)
                          .setTextureTransform(TextureTransform.ENTITY_GLINT_TEXTURING)
                          .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
                          .createRenderSetup();
        return RenderTypeAccessor.callCreate("player_and_dragon_head_glint", state);
    });

    public static RenderType mobSkullGlint(Identifier texture)
    {
        return MOB_SKULL_GLINT.apply(texture);
    }

    public static RenderType playerAndDragonHeadGlint(Identifier texture)
    {
        return PLAYER_AND_DRAGON_HEAD_GLINT.apply(texture);
    }
}