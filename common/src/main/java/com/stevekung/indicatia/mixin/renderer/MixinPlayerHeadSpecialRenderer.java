package com.stevekung.indicatia.mixin.renderer;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.utils.EnchantedSkullItemCache;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.special.PlayerHeadSpecialRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.component.ResolvableProfile;

@Mixin(PlayerHeadSpecialRenderer.class)
public class MixinPlayerHeadSpecialRenderer
{
    @ModifyReturnValue(method = "extractArgument", at = @At(value = "RETURN", ordinal = 1))
    private PlayerHeadSpecialRenderer.PlayerHeadRenderInfo indicatia$setResolvableProfileInExtractArgument(PlayerHeadSpecialRenderer.PlayerHeadRenderInfo playerHeadRenderInfo, @Local ResolvableProfile resolvableProfile)
    {
        playerHeadRenderInfo.indicatia$setResolvableProfile(resolvableProfile);
        return playerHeadRenderInfo;
    }

    @ModifyVariable(
            method = "createAndCacheIfTextureIsUnpacked",
            at = @At(
                    value = "INVOKE",
                    target = "java/util/Map.put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                    remap = false))
    private PlayerHeadSpecialRenderer.PlayerHeadRenderInfo indicatia$setResolvableProfileInCreateAndCacheIfTextureIsUnpacked(PlayerHeadSpecialRenderer.PlayerHeadRenderInfo playerHeadRenderInfo, @Local(argsOnly = true) ResolvableProfile resolvableProfile)
    {
        playerHeadRenderInfo.indicatia$setResolvableProfile(resolvableProfile);
        return playerHeadRenderInfo;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.renderSkull(Lnet/minecraft/core/Direction;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;)V"))
    private void indicatia$storeEnchantedCachePre(@Nullable PlayerHeadSpecialRenderer.PlayerHeadRenderInfo playerHeadRenderInfo, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, boolean glint, CallbackInfo info)
    {
        EnchantedSkullItemCache.preCache(playerHeadRenderInfo.indicatia$resolvableProfile(), glint);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.renderSkull(Lnet/minecraft/core/Direction;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;)V", shift = At.Shift.AFTER))
    private void indicatia$storeEnchantedCachePost(@Nullable PlayerHeadSpecialRenderer.PlayerHeadRenderInfo playerHeadRenderInfo, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, boolean glint, CallbackInfo info)
    {
        EnchantedSkullItemCache.postCache();
    }
}