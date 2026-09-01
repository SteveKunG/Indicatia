package com.stevekung.indicatia;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class EnchantedSkullRenderer
{
    public static void submitSkull(float animationValue, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SkullModelBase model, RenderType renderType, int outlineColor, boolean hasFoil)
    {
	    var modelState = new SkullModelBase.State();
        modelState.animationPos = animationValue;
        submitNodeCollector.submitModel(model, modelState, poseStack, renderType, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor);

        //TODO Test
        if (hasFoil)
        {
            submitNodeCollector.order(1).submitModel(model, modelState, poseStack, RenderTypes.patternedShieldGlint(), lightCoords, OverlayTexture.NO_OVERLAY, outlineColor);
        }
    }
}