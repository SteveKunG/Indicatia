package com.stevekung.indicatia.mixin.renderer.entity.layers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.utils.EnchantedSkullTileEntityRenderer;
import com.stevekung.indicatia.utils.PlatformConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;

@Mixin(CustomHeadLayer.class)
public class MixinCustomHeadLayer<T extends LivingEntity>
{
    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.renderSkull(Lnet/minecraft/core/Direction;FLnet/minecraft/world/level/block/SkullBlock$Type;Lcom/mojang/authlib/GameProfile;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    private void renderEnchantedSkull(Direction direction, float rotationY, SkullBlock.Type skullType, GameProfile gameProfile, float animationProgress, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, PoseStack _matrixStack, MultiBufferSource _buffer, int _packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        ItemStack itemStack = entity.getItemBySlot(EquipmentSlot.HEAD);

        if (EnchantedSkullTileEntityRenderer.isVanillaHead(skullType))
        {
            EnchantedSkullTileEntityRenderer.render(((AbstractSkullBlock)((BlockItem)itemStack.getItem()).getBlock()).getType(), gameProfile, _matrixStack, _buffer, _packedLight, PlatformConfig.getOldArmorRender() ? LivingEntityRenderer.getOverlayCoords(entity, 0.0F) : OverlayTexture.NO_OVERLAY, itemStack.hasFoil());
        }
        else
        {
            SkullBlockRenderer.renderSkull(null, 180.0F, ((AbstractSkullBlock)((BlockItem)itemStack.getItem()).getBlock()).getType(), gameProfile, limbSwing, _matrixStack, _buffer, _packedLight);
        }
    }
}