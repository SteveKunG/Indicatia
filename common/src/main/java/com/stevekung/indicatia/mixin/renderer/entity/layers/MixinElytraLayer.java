package com.stevekung.indicatia.mixin.renderer.entity.layers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.stevekung.indicatia.utils.PlatformConfig;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.world.entity.LivingEntity;

@Mixin(ElytraLayer.class)
public class MixinElytraLayer<T extends LivingEntity>
{
    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "net/minecraft/client/model/ElytraModel.renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"))
    private void renderElytra(ElytraModel<T> modelElytra, PoseStack _poseStackIn, VertexConsumer _bufferIn, int _packedLightIn, int _packedOverlayIn, float red, float green, float blue, float alpha, PoseStack poseStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        modelElytra.renderToBuffer(_poseStackIn, _bufferIn, _packedLightIn, PlatformConfig.getOldArmorRender() ? LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F) : _packedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}