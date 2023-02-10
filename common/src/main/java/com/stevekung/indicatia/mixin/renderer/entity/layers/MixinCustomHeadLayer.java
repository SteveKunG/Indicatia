package com.stevekung.indicatia.mixin.renderer.entity.layers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.utils.EnchantedSkullTileEntityRenderer;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@Mixin(CustomHeadLayer.class)
public class MixinCustomHeadLayer<T extends LivingEntity>
{
    @Unique
    private GameProfile gameProfile;

    @Unique
    private SkullModelBase skullModelBase;

    @Unique
    private RenderType renderType;

    @Unique
    private ItemStack itemStack;

    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.renderSkull(Lnet/minecraft/core/Direction;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;)V"), index = 16)
    private GameProfile indicatia$getGameProfile(GameProfile gameProfile)
    {
        return this.gameProfile = gameProfile;
    }

    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.renderSkull(Lnet/minecraft/core/Direction;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;)V"), index = 18)
    private SkullModelBase indicatia$getSkullModelBase(SkullModelBase skullModelBase)
    {
        return this.skullModelBase = skullModelBase;
    }

    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.renderSkull(Lnet/minecraft/core/Direction;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;)V"), index = 19)
    private RenderType indicatia$getRenderType(RenderType renderType)
    {
        return this.renderType = renderType;
    }

    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.renderSkull(Lnet/minecraft/core/Direction;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;)V"), index = 11)
    private ItemStack indicatia$getItemStack(ItemStack itemStack)
    {
        return this.itemStack = itemStack;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.renderSkull(Lnet/minecraft/core/Direction;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;)V"))
    private void indicatia$renderEnchantedSkull(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo info)
    {
        EnchantedSkullTileEntityRenderer.render(this.gameProfile, 180.0F, limbSwing, poseStack, multiBufferSource, packedLight, this.skullModelBase, this.renderType, this.itemStack.hasFoil());
    }
}