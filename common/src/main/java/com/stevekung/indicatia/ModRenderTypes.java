package com.stevekung.indicatia;

import java.util.function.Function;

import com.stevekung.indicatia.mixin.accessor.RenderTypeAccessor;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

public class ModRenderTypes
{
//    private static final Function<Identifier, RenderType> ENTITY_SOLID_GLINT = Util.memoize(texture ->
//                                                                                            {
//                                                                                                var state = RenderSetup.builder(RenderPipelines.ENTITY_SOLID_GLINT).setOitPipelines(RenderPipelines.OIT_ENTITY).withTexture("Sampler0", texture).withTexture("GlintSampler", ItemFeatureRenderer.ENCHANTED_GLINT_ITEM).setTextureTransform(TextureTransform.ENTITY_GLINT_TEXTURING).useLightmap().useOverlay().affectsCrumbling().setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE).createRenderSetup();
//                                                                                                return RenderType.create("entity_solid_glint", state);
//                                                                                            });

    private static final Function<Identifier, RenderType> ENTITY_TRANSLUCENT_GLINT = Util.memoize(texture ->
    {
        var state = RenderSetup.builder(ModRenderPipelines.ENTITY_TRANSLUCENT_GLINT)
                            .setOitPipelines(RenderPipelines.OIT_ENTITY)
                            .withTexture("Sampler0", texture)
                            .withTexture("GlintSampler", ItemFeatureRenderer.ENCHANTED_GLINT_ITEM)
                            .useLightmap()
                            .useOverlay()
                            .affectsCrumbling()
                            .sortOnUpload()
                            .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
                            .createRenderSetup();
        return RenderTypeAccessor.callCreate("entity_translucent_glint", state);
    });

    public static RenderType entityTranslucentGlint(Identifier texture)
    {
        return ENTITY_TRANSLUCENT_GLINT.apply(texture);
    }
}