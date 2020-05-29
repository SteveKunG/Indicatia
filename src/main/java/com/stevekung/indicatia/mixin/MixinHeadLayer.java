package com.stevekung.indicatia.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.stevekung.indicatia.utils.EnchantedSkullTileEntityRenderer;

import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

@Mixin(HeadLayer.class)
public abstract class MixinHeadLayer<T extends LivingEntity, M extends EntityModel<T> & IHasHead> extends LayerRenderer<T, M>
{
    public MixinHeadLayer(IEntityRenderer<T, M> renderer)
    {
        super(renderer);
    }

    @Redirect(method = "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/tileentity/SkullTileEntityRenderer.render(Lnet/minecraft/util/Direction;FLnet/minecraft/block/SkullBlock$ISkullType;Lcom/mojang/authlib/GameProfile;FLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V"))
    private void renderEnchantedSkull(@Nullable Direction directionIn, float p_228879_1_, SkullBlock.ISkullType skullType, @Nullable GameProfile gameProfileIn, float animationProgress, MatrixStack matrixStackIn, IRenderTypeBuffer buffer, int combinedLight, MatrixStack matrixStackIn_, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EquipmentSlotType.HEAD);
        EnchantedSkullTileEntityRenderer.render(((AbstractSkullBlock)((BlockItem)itemstack.getItem()).getBlock()).getSkullType(), gameProfileIn, matrixStackIn, bufferIn, packedLightIn, itemstack.hasEffect());
    }
}