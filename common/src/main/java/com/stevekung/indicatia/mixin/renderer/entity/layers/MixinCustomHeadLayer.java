package com.stevekung.indicatia.mixin.renderer.entity.layers;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.EnchantedSkullRenderer;

import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;

@Mixin(CustomHeadLayer.class)
public class MixinCustomHeadLayer
{
    @Redirect(method = "submit", at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.submitSkull(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/model/object/skull/SkullModelBase;Lnet/minecraft/client/renderer/rendertype/RenderType;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
    private void indicatia$useCustomEnchantedSkullRenderer(float animationValue, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SkullModelBase model, RenderType renderType, int outlineColor, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, @Local(argsOnly = true) LivingEntityRenderState livingEntityRenderState)
    {
        EnchantedSkullRenderer.submitSkull(animationValue, poseStack, submitNodeCollector, lightCoords, model, renderType, outlineColor, livingEntityRenderState.indicatia$hasFoil());
    }
}