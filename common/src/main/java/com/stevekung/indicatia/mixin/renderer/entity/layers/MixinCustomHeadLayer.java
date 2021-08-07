package com.stevekung.indicatia.mixin.renderer.entity.layers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.utils.EnchantedSkullTileEntityRenderer;
import com.stevekung.indicatia.utils.PlatformConfig;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.AbstractSkullBlock;

@Mixin(CustomHeadLayer.class)
public class MixinCustomHeadLayer<T extends LivingEntity>
{
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.renderSkull(Lnet/minecraft/core/Direction;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;)V"))
    private void renderEnchantedSkull(Direction direction, float rotationYaw, float mouthAnimation, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, SkullModelBase skullModelBase, RenderType renderType, PoseStack _poseStack, MultiBufferSource _multiBufferSource, int _combinedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        var itemStack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        GameProfile gameProfile = null;

        if (itemStack.hasTag())
        {
            var compoundTag = itemStack.getTag();

            if (compoundTag.contains("SkullOwner", 10))
            {
                gameProfile = NbtUtils.readGameProfile(compoundTag.getCompound("SkullOwner"));
            }
        }

        var type = ((AbstractSkullBlock) ((BlockItem) itemStack.getItem()).getBlock()).getType();

        if (EnchantedSkullTileEntityRenderer.isVanillaHead(type))
        {
            EnchantedSkullTileEntityRenderer.render(gameProfile, rotationYaw, mouthAnimation, type, poseStack, multiBufferSource, combinedLight, PlatformConfig.getOldArmorRender() ? LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0F) : OverlayTexture.NO_OVERLAY, skullModelBase, renderType, itemStack.hasFoil());
        }
        else
        {
            SkullBlockRenderer.renderSkull(direction, rotationYaw, mouthAnimation, poseStack, multiBufferSource, combinedLight, skullModelBase, renderType);
        }
    }
}