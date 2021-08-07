//package com.stevekung.indicatia.mixin.forge.renderer.entity.layers;
//
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import com.stevekung.indicatia.utils.PlatformConfig;
//import net.minecraft.client.model.HumanoidModel;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.entity.ItemRenderer;
//import net.minecraft.client.renderer.entity.LivingEntityRenderer;
//import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.LivingEntity;
//
//@Mixin(HumanoidArmorLayer.class)
//public class MixinHumanoidArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>>
//{
//    @Redirect(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/entity/layers/BipedArmorLayer.renderModel(Lcom/mojang/blaze3d/matrix/poseStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IZLnet/minecraft/client/renderer/entity/model/BipedModel;FFFLnet/minecraft/util/ResourceLocation;)V", remap = false), require = 3, allow = 3)
//    private void renderArmorProd(HumanoidArmorLayer<T, M, A> armorLayer, PoseStack _poseStack, MultiBufferSource _bufferIn, int _packedLightIn, boolean glintIn, A modelIn, float red, float green, float blue, ResourceLocation armorResource, PoseStack poseStack, MultiBufferSource buffer, T entityLivingBaseIn, EquipmentSlot slot, int packedLight, A model)
//    {
//        this.renderArmorModified(_poseStack, _bufferIn, entityLivingBaseIn, _packedLightIn, glintIn, modelIn, red, green, blue, armorResource);
//    }
//
//    private void renderArmorModified(PoseStack poseStack, MultiBufferSource buffer, T entity, int packedLight, boolean glint, A model, float red, float green, float blue, ResourceLocation armorResource)
//    {
//        VertexConsumer ivertexbuilder = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(armorResource), false, glint);
//        model.renderToBuffer(poseStack, ivertexbuilder, packedLight, PlatformConfig.getOldArmorRender() ? LivingEntityRenderer.getOverlayCoords(entity, 0.0F) : OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
//    }
//}