package com.stevekung.indicatia.mixin.renderer;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.utils.EnchantedSkullTileEntityRenderer;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class MixinBlockEntityWithoutLevelRenderer
{
    @Redirect(method = "renderByItem", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.renderSkull(Lnet/minecraft/core/Direction;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;)V"))
    private void renderEnchantedSkull(Direction direction, float rotationYaw, float mouthAnimation, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, SkullModelBase skullModelBase, RenderType renderType, ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack _poseStack, MultiBufferSource _multiBufferSource, int _combinedLight, int _combinedOverlay)
    {
        GameProfile gameProfile = null;
        Block block = ((BlockItem) itemStack.getItem()).getBlock();
        SkullBlock.Type type = ((AbstractSkullBlock) block).getType();

        if (itemStack.hasTag())
        {
            CompoundTag compoundTag = itemStack.getTag();

            if (compoundTag.contains("SkullOwner", 10))
            {
                gameProfile = NbtUtils.readGameProfile(compoundTag.getCompound("SkullOwner"));
            }
            else if (compoundTag.contains("SkullOwner", 8) && !StringUtils.isBlank(compoundTag.getString("SkullOwner")))
            {
                gameProfile = new GameProfile(null, compoundTag.getString("SkullOwner"));
                compoundTag.remove("SkullOwner");
                SkullBlockEntity.updateGameprofile(gameProfile, gameProfilex -> compoundTag.put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), gameProfilex)));
            }
        }

        if (EnchantedSkullTileEntityRenderer.isVanillaHead(type))
        {
            EnchantedSkullTileEntityRenderer.render(gameProfile, type, poseStack, multiBufferSource, combinedLight, OverlayTexture.NO_OVERLAY, skullModelBase, renderType, itemStack.hasFoil());
        }
        else
        {
            SkullBlockRenderer.renderSkull(direction, rotationYaw, mouthAnimation, poseStack, multiBufferSource, combinedLight, skullModelBase, renderType);
        }
    }
}