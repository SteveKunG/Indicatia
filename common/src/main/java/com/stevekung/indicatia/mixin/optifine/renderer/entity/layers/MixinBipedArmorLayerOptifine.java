package com.stevekung.indicatia.mixin.optifine.renderer.entity.layers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.stevekung.indicatia.utils.PlatformConfig;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

@Mixin(HumanoidArmorLayer.class)
public class MixinBipedArmorLayerOptifine<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>>
{
    @Redirect(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/entity/layers/BipedArmorLayer.renderModel(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IZLnet/minecraft/client/renderer/entity/model/BipedModel;FFFLnet/minecraft/util/ResourceLocation;)V", remap = false), require = 3, allow = 3)
    private void renderArmorOptifine(HumanoidArmorLayer<T, M, A> armorLayer, PoseStack _matrixStackIn, MultiBufferSource _bufferIn, int _packedLightIn, boolean glintIn, A modelIn, float red, float green, float blue, ResourceLocation armorResource, PoseStack matrixStackIn, MultiBufferSource bufferIn, T entityLivingBaseIn, EquipmentSlot slotIn, int packedLightIn, A model)
    {
        this.renderArmorModified(_matrixStackIn, _bufferIn, entityLivingBaseIn, _packedLightIn, glintIn, modelIn, red, green, blue, armorResource);
    }

    private void renderArmorModified(PoseStack matrixStack, MultiBufferSource buffer, T entity, int packedLight, boolean glint, A model, float red, float green, float blue, ResourceLocation armorResource)
    {
        VertexConsumer ivertexbuilder = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(armorResource), false, glint);
        model.renderToBuffer(matrixStack, ivertexbuilder, packedLight, PlatformConfig.getOldArmorRender() ? LivingEntityRenderer.getOverlayCoords(entity, 0.0F) : OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }
}