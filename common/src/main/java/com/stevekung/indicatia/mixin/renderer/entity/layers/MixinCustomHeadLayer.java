package com.stevekung.indicatia.mixin.renderer.entity.layers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.LivingEntityRenderStateExtender;
import com.stevekung.indicatia.utils.EnchantedSkullItemCache;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

@Mixin(CustomHeadLayer.class)
public class MixinCustomHeadLayer
{
    @Inject(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.renderSkull(Lnet/minecraft/core/Direction;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;)V"))
    private void indicatia$storeEnchantedCachePre(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, LivingEntityRenderState livingEntityRenderState, float yRot, float xRot, CallbackInfo info)
    {
        EnchantedSkullItemCache.preCache(livingEntityRenderState.wornHeadProfile, ((LivingEntityRenderStateExtender)livingEntityRenderState).indicatia$hasHeadGlint());
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.renderSkull(Lnet/minecraft/core/Direction;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;)V", shift = At.Shift.AFTER))
    private void indicatia$storeEnchantedCachePost(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, LivingEntityRenderState livingEntityRenderState, float yRot, float xRot, CallbackInfo info)
    {
        EnchantedSkullItemCache.postCache();
    }
}