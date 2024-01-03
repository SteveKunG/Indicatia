package com.stevekung.indicatia.mixin.renderer;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.Indicatia;
import com.stevekung.indicatia.utils.EnchantedSkullBlockEntityRenderer;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class MixinBlockEntityWithoutLevelRenderer
{
    @WrapWithCondition(method = "renderByItem", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.renderSkull(Lnet/minecraft/core/Direction;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;)V"))
    private boolean indicatia$renderEnchantedSkull(@Nullable Direction direction, float yRot, float mouthAnimation, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, SkullModelBase model, RenderType renderType, @Local(argsOnly = true) ItemStack itemStack, @Local(argsOnly = true) MultiBufferSource multiBufferSource, @Local GameProfile gameProfile, @Local SkullModelBase skullModelBase)
    {
        EnchantedSkullBlockEntityRenderer.render(gameProfile, 180.0F, 0.0F, poseStack, multiBufferSource, packedLight, skullModelBase, renderType, itemStack.hasFoil());
        return !Indicatia.CONFIG.enableEnchantedRenderingOnSkulls;
    }
}