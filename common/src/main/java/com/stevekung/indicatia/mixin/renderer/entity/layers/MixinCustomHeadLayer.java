package com.stevekung.indicatia.mixin.renderer.entity.layers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.utils.EnchantedSkullItemCache;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

@Mixin(CustomHeadLayer.class)
public class MixinCustomHeadLayer
{
    //TODO Fix player head does not render glint when wearing

    @Inject(method = "submit", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.submitSkull(FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;I)V"))
    private void indicatia$storeEnchantedCachePre(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, LivingEntityRenderState livingEntityRenderState, float yRot, float xRot, CallbackInfo info)
    {
        EnchantedSkullItemCache.preCache(livingEntityRenderState.indicatia$isPlayerHead(), livingEntityRenderState.indicatia$hasHeadGlint());
    }

    @Inject(method = "submit", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.submitSkull(FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;I)V", shift = At.Shift.AFTER))
    private void indicatia$storeEnchantedCachePost(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, LivingEntityRenderState livingEntityRenderState, float yRot, float xRot, CallbackInfo info)
    {
        EnchantedSkullItemCache.postCache();
    }
}