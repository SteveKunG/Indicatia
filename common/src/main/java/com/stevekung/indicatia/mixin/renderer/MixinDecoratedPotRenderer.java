package com.stevekung.indicatia.mixin.renderer;

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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.stevekung.indicatia.EnchantedFoilExtender;

import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;

@Mixin(DecoratedPotRenderer.class)
public class MixinDecoratedPotRenderer implements EnchantedFoilExtender
{
    @Unique
    private boolean hasFoil;

    @Override
    public void indicatia$setFoil(boolean hasFoil)
    {
        this.hasFoil = hasFoil;
    }

    @Override
    public boolean indicatia$hasFoil()
    {
        return this.hasFoil;
    }

    @WrapOperation(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IILnet/minecraft/world/level/block/entity/PotDecorations;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/renderer/SubmitNodeCollector.submitModelPart(Lnet/minecraft/client/model/geom/ModelPart;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V"))
    private void indicatia$addEnchantedGlint(SubmitNodeCollector submitNodeCollector, ModelPart modelPart, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, @Nullable TextureAtlasSprite sprite, int tintedColor, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay, int outlineColor, Operation<Void> operation) {
        operation.call(submitNodeCollector, modelPart, poseStack, renderType, lightCoords, overlayCoords, sprite, tintedColor, crumblingOverlay, outlineColor);

        //TODO Test
        if (this.hasFoil)
        {
            submitNodeCollector.order(1).submitModelPart(modelPart, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, sprite, tintedColor, crumblingOverlay, outlineColor);
        }
    }
}