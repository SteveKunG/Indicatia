package com.stevekung.indicatia.fabric.mixin.renderer.entity.layers;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.world.item.ArmorItem;

@Mixin(HumanoidArmorLayer.class)
public abstract class MixinHumanoidArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>>
{
    @Shadow
    abstract ResourceLocation getArmorLocation(ArmorItem armorItem, boolean legs, @Nullable String armorResource);

    @Redirect(method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/entity/layers/HumanoidArmorLayer.renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/ArmorItem;ZLnet/minecraft/client/model/HumanoidModel;ZFFFLjava/lang/String;)V"), require = 3, allow = 3)
    private void renderArmor(HumanoidArmorLayer<T, M, A> armorLayer, PoseStack _poseStackIn, MultiBufferSource _bufferIn, int _packedLightIn, ArmorItem armorItem, boolean glintIn, A modelIn, boolean legs, float red, float green, float blue, String armorResource, PoseStack poseStackIn, MultiBufferSource bufferIn, T entityLivingBaseIn, EquipmentSlot slotIn, int packedLightIn, A model)
    {
        this.renderArmorModified(_poseStackIn, _bufferIn, entityLivingBaseIn, _packedLightIn, armorItem, glintIn, modelIn, legs, red, green, blue, armorResource);
    }

    private void renderArmorModified(PoseStack poseStack, MultiBufferSource buffer, T entity, int packedLight, ArmorItem armorItem, boolean glint, A model, boolean legs, float red, float green, float blue, String armorResource)
    {
        var ivertexbuilder = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(this.getArmorLocation(armorItem, legs, armorResource)), false, glint);
        model.renderToBuffer(poseStack, ivertexbuilder, packedLight, PlatformConfig.getOldArmorRender() ? LivingEntityRenderer.getOverlayCoords(entity, 0.0F) : OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }
}