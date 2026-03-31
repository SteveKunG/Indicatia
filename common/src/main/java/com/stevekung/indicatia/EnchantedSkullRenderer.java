package com.stevekung.indicatia;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jspecify.annotations.Nullable;

public class EnchantedSkullRenderer
{
    public static void submitSkull(float animationValue, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SkullModelBase model, RenderType renderType, int outlineColor, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, boolean hasFoil)
    {
	    var modelState = new SkullModelBase.State();
        modelState.animationPos = animationValue;
        submitNodeCollector.submitModel(model, modelState, poseStack, renderType, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor, breakProgress);

        if (hasFoil)
        {
            submitNodeCollector.submitModel(model, modelState, poseStack, RenderTypes.entityGlint(), lightCoords, OverlayTexture.NO_OVERLAY, outlineColor, breakProgress);
        }
    }
}