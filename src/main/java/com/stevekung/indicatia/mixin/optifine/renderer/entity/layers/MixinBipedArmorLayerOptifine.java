package com.stevekung.indicatia.mixin.optifine.renderer.entity.layers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.stevekung.indicatia.config.IndicatiaConfig;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;

@Mixin(BipedArmorLayer.class)
public class MixinBipedArmorLayerOptifine<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>>
{
    @Redirect(method = "func_241739_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/inventory/EquipmentSlotType;ILnet/minecraft/client/renderer/entity/model/BipedModel;)V", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/entity/layers/BipedArmorLayer.renderModel(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IZLnet/minecraft/client/renderer/entity/model/BipedModel;FFFLnet/minecraft/util/ResourceLocation;)V", remap = false),
            require = 3, allow = 3)
    private void renderArmorOptifine(BipedArmorLayer<T, M, A> armorLayer, MatrixStack _matrixStackIn, IRenderTypeBuffer _bufferIn, int _packedLightIn, boolean glintIn, A modelIn, float red, float green, float blue, ResourceLocation armorResource, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, T entityLivingBaseIn, EquipmentSlotType slotIn, int packedLightIn, A model)
    {
        this.renderArmorModified(_matrixStackIn, _bufferIn, entityLivingBaseIn, _packedLightIn, glintIn, modelIn, red, green, blue, armorResource);
    }

    private void renderArmorModified(MatrixStack matrixStack, IRenderTypeBuffer buffer, T entity, int packedLight, boolean glint, A model, float red, float green, float blue, ResourceLocation armorResource)
    {
        IVertexBuilder ivertexbuilder = ItemRenderer.getArmorVertexBuilder(buffer, RenderType.getArmorCutoutNoCull(armorResource), false, glint);
        model.render(matrixStack, ivertexbuilder, packedLight, IndicatiaConfig.GENERAL.enableOldArmorRender.get() ? LivingRenderer.getPackedOverlay(entity, 0.0F) : OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }
}