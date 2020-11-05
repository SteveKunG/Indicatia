package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.stevekung.indicatia.config.IndicatiaConfig;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

@Mixin(ElytraLayer.class)
public class MixinElytraLayer<T extends LivingEntity, M extends EntityModel<T>>
{
    @Redirect(method = "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/entity/model/ElytraModel.render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;IIFFFF)V"))
    private void renderElytra(ElytraModel<T> modelElytra, MatrixStack _matrixStackIn, IVertexBuilder _bufferIn, int _packedLightIn, int _packedOverlayIn, float red, float green, float blue, float alpha, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        modelElytra.render(_matrixStackIn, _bufferIn, _packedLightIn, IndicatiaConfig.GENERAL.enableOldArmorRender.get() ? LivingRenderer.getPackedOverlay(entitylivingbaseIn, 0.0F) : _packedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}