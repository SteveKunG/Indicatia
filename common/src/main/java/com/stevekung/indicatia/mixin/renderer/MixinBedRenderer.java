package com.stevekung.indicatia.mixin.renderer;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.EnchantedFoilExtender;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

@Mixin(BedRenderer.class)
public class MixinBedRenderer implements EnchantedFoilExtender
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

    @WrapOperation(method = "submitPiece", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/SubmitNodeCollector.submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
    private <S> void indicatia$addEnchantedGlint(SubmitNodeCollector submitNodeCollector, Model<? super S> model, S object, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int tintedColor, @Nullable TextureAtlasSprite textureAtlasSprite, int outlineColor, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, Operation<Void> operation)
    {
        operation.call(submitNodeCollector, model, object, poseStack, renderType, lightCoords, overlayCoords, tintedColor, textureAtlasSprite, outlineColor, crumblingOverlay);

        if (this.hasFoil)
        {
            operation.call(submitNodeCollector, model, object, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, tintedColor, textureAtlasSprite, outlineColor, crumblingOverlay);
        }
    }
}