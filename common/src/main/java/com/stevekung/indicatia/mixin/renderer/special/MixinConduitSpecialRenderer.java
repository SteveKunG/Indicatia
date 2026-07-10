package com.stevekung.indicatia.mixin.renderer.special;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.sugar.Local;
import com.stevekung.indicatia.Indicatia;

import net.minecraft.client.renderer.special.ConduitSpecialRenderer;

@Mixin(ConduitSpecialRenderer.class)
public class MixinConduitSpecialRenderer
{
    @WrapOperation(method = "submit", at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/renderer/SubmitNodeCollector.submitModelPart(Lnet/minecraft/client/model/geom/ModelPart;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V"))
    private void indicatia$addEnchantedGlint(SubmitNodeCollector submitNodeCollector, ModelPart modelPart, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, @Nullable TextureAtlasSprite sprite, int tintedColor, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay, int outlineColor, Operation<Void> operation, @Local(argsOnly = true) boolean hasFoil)
    {
        operation.call(submitNodeCollector, modelPart, poseStack, renderType, lightCoords, overlayCoords, sprite, tintedColor, crumblingOverlay, outlineColor);

        if (Indicatia.CONFIG.enableEnchantedRenderingOnAllBlockEntities && hasFoil)
        {
            submitNodeCollector.order(1).submitModelPart(modelPart, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, sprite, tintedColor, crumblingOverlay, outlineColor);
        }
    }
}