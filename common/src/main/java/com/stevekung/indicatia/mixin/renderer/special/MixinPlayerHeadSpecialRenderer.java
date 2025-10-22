package com.stevekung.indicatia.mixin.renderer.special;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.EnchantedSkullRenderer;
import com.stevekung.indicatia.Indicatia;

import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.special.PlayerHeadSpecialRenderer;
import net.minecraft.core.Direction;

@Mixin(PlayerHeadSpecialRenderer.class)
public class MixinPlayerHeadSpecialRenderer
{
    @Redirect(method = "submit", at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.submitSkull(Lnet/minecraft/core/Direction;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/rendertype/RenderType;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
    private void indicatia$useCustomEnchantedSkullRenderer(@Nullable Direction direction, float yRot, float animationPos, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, SkullModelBase skullModelBase, RenderType renderType, int outlineColor, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, @Local(argsOnly = true) boolean hasFoil)
    {
        EnchantedSkullRenderer.submitSkull(direction, yRot, animationPos, poseStack, submitNodeCollector, packedLight, skullModelBase, renderType, outlineColor, crumblingOverlay, Indicatia.CONFIG.enableEnchantedRenderingOnSkulls && hasFoil);
    }
}