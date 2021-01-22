package com.stevekung.indicatia.mixin.renderer.entity.layers;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.utils.EnchantedSkullTileEntityRenderer;

import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.SkullTileEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

@Mixin(HeadLayer.class)
public class MixinHeadLayer<T extends LivingEntity, M extends EntityModel<T> & IHasHead>
{
    @Redirect(method = "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/tileentity/SkullTileEntityRenderer.render(Lnet/minecraft/util/Direction;FLnet/minecraft/block/SkullBlock$ISkullType;Lcom/mojang/authlib/GameProfile;FLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V"))
    private void renderEnchantedSkull(@Nullable Direction direction, float rotationY, SkullBlock.ISkullType skullType, @Nullable GameProfile gameProfile, float animationProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, MatrixStack _matrixStack, IRenderTypeBuffer _buffer, int _packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        ItemStack itemStack = entity.getItemStackFromSlot(EquipmentSlotType.HEAD);

        if (EnchantedSkullTileEntityRenderer.isVanillaHead(skullType))
        {
            EnchantedSkullTileEntityRenderer.render(((AbstractSkullBlock)((BlockItem)itemStack.getItem()).getBlock()).getSkullType(), gameProfile, _matrixStack, _buffer, _packedLight, IndicatiaConfig.GENERAL.enableOldArmorRender.get() ? LivingRenderer.getPackedOverlay(entity, 0.0F) : OverlayTexture.NO_OVERLAY, itemStack.hasEffect());
        }
        else
        {
            SkullTileEntityRenderer.render(null, 180.0F, ((AbstractSkullBlock)((BlockItem)itemStack.getItem()).getBlock()).getSkullType(), gameProfile, limbSwing, _matrixStack, _buffer, _packedLight);
        }
    }
}