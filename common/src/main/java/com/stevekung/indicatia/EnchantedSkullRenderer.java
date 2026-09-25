package com.stevekung.indicatia;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.mixin.accessor.SkullBlockRendererAccessor;

import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.level.block.SkullBlock;

import org.jspecify.annotations.Nullable;

public class EnchantedSkullRenderer
{
    public static void submitPlayerHead(PlayerSkinRenderCache.@Nullable RenderInfo argument, float animationValue, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SkullModelBase model, RenderType renderType, int outlineColor, boolean hasFoil)
    {
        var modelState = new SkullModelBase.State();
        modelState.animationPos = animationValue;
        submitNodeCollector.submitModel(model, modelState, poseStack, renderType, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor);

        if (hasFoil)
        {
            var texture = argument != null ? argument.playerSkin().body().texturePath() : DefaultPlayerSkin.getDefaultTexture();
            submitNodeCollector.order(1).submitModel(model, modelState, poseStack, ModRenderTypes.entityTranslucentGlint(texture), lightCoords, OverlayTexture.NO_OVERLAY, outlineColor);
        }
    }

    public static void submitSkull(float animationValue, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SkullModelBase model, RenderType renderType, int outlineColor, boolean hasFoil, SkullBlock.Type type)
    {
        var modelState = new SkullModelBase.State();
        modelState.animationPos = animationValue;
        submitNodeCollector.submitModel(model, modelState, poseStack, renderType, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor);

        if (hasFoil)
        {
            submitNodeCollector.order(1).submitModel(model, modelState, poseStack, ModRenderTypes.entityTranslucentGlint(SkullBlockRendererAccessor.getSkinByType().get(type)), lightCoords, OverlayTexture.NO_OVERLAY, outlineColor);
        }
    }
}