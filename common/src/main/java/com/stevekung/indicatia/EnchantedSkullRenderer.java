package com.stevekung.indicatia;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;

public class EnchantedSkullRenderer
{
    public static void submitSkull(@Nullable Direction direction, float yRot, float animationPos, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, SkullModelBase skullModelBase, RenderType renderType, int outlineColor, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, boolean hasFoil)
    {
        poseStack.pushPose();

        if (direction == null)
        {
            poseStack.translate(0.5F, 0.0F, 0.5F);
        }
        else
        {
            poseStack.translate(0.5F - direction.getStepX() * 0.25F, 0.25F, 0.5F - direction.getStepZ() * 0.25F);
        }

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        SkullModelBase.State state = new SkullModelBase.State();
        state.animationPos = animationPos;
        state.yRot = yRot;
        state.indicatia$setFoil(hasFoil);
        submitNodeCollector.submitModel(skullModelBase, state, poseStack, renderType, packedLight, OverlayTexture.NO_OVERLAY, outlineColor, crumblingOverlay);
        poseStack.popPose();
    }
}