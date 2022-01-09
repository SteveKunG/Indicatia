package com.stevekung.indicatia.forge.mixin.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.forge.config.IndicatiaConfig;
import com.stevekung.indicatia.utils.EnchantedSkullTileEntityRenderer;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class MixinBlockEntityWithoutLevelRenderer
{
    @Inject(method = "renderByItem", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.renderSkull(Lnet/minecraft/core/Direction;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void indicatia$renderEnchantedSkull(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, CallbackInfo info, Item item, Block block, GameProfile gameProfile, SkullBlock.Type type, SkullModelBase skullModelBase, RenderType renderType)
    {
        if (IndicatiaConfig.CONFIG.enableEnchantedRenderingOnSkull.get() && EnchantedSkullTileEntityRenderer.isVanillaHead(type))
        {
            EnchantedSkullTileEntityRenderer.render(gameProfile, 180.0F, 0.0F, poseStack, multiBufferSource, packedLight, skullModelBase, renderType, itemStack.hasFoil());
        }
    }
}