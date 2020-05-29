package com.stevekung.indicatia.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.stevekung.indicatia.utils.EnchantedSkullTileEntityRenderer;

import net.minecraft.block.SkullBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

@Mixin(ItemStackTileEntityRenderer.class)
public abstract class MixinItemStackTileEntityRenderer
{
    @Redirect(method = "render(Lnet/minecraft/item/ItemStack;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;II)V", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/tileentity/SkullTileEntityRenderer.render(Lnet/minecraft/util/Direction;FLnet/minecraft/block/SkullBlock$ISkullType;Lcom/mojang/authlib/GameProfile;FLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V"))
    private void renderEnchantedSkull(@Nullable Direction directionIn, float p_228879_1_, SkullBlock.ISkullType skullType, @Nullable GameProfile gameProfileIn, float animationProgress, MatrixStack matrixStackIn_, IRenderTypeBuffer buffer, int combinedLight, ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        EnchantedSkullTileEntityRenderer.render(skullType, gameProfileIn, matrixStackIn, bufferIn, combinedLightIn, itemStackIn.hasEffect());
    }
}