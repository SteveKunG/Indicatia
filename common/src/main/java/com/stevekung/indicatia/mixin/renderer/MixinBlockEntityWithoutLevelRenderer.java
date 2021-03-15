package com.stevekung.indicatia.mixin.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.utils.EnchantedSkullTileEntityRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SkullBlock;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class MixinBlockEntityWithoutLevelRenderer
{
    @Redirect(method = "renderByItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.renderSkull(Lnet/minecraft/core/Direction;FLnet/minecraft/world/level/block/SkullBlock$Type;Lcom/mojang/authlib/GameProfile;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    private void renderEnchantedSkull(Direction direction, float rotationYaw, SkullBlock.Type skullType, GameProfile gameProfile, float animationProgress, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack _matrixStack, MultiBufferSource _buffer, int _combinedLight, int _combinedOverlay)
    {
        if (EnchantedSkullTileEntityRenderer.isVanillaHead(skullType))
        {
            EnchantedSkullTileEntityRenderer.render(skullType, gameProfile, matrixStack, buffer, combinedLight, OverlayTexture.NO_OVERLAY, itemStack.hasFoil());
        }
        else
        {
            SkullBlockRenderer.renderSkull(direction, rotationYaw, skullType, gameProfile, animationProgress, matrixStack, buffer, combinedLight);
        }
    }
}