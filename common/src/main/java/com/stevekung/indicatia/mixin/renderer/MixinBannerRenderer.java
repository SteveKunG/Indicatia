package com.stevekung.indicatia.mixin.renderer;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.EnchantedBannerRenderer;
import com.stevekung.indicatia.EnchantedFoilExtender;

import net.minecraft.client.model.BannerFlagModel;
import net.minecraft.client.model.BannerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

@Mixin(BannerRenderer.class)
public class MixinBannerRenderer implements EnchantedFoilExtender
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

    @WrapOperation(method = "submitSpecial", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/BannerRenderer.submitBanner (Lnet/minecraft/client/resources/model/MaterialSet;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IIFLnet/minecraft/client/model/BannerModel;Lnet/minecraft/client/model/BannerFlagModel;FLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V"))
    private void indicatia$addEnchantedGlint(MaterialSet materialSet, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, float rotationDegrees, BannerModel bannerModel, BannerFlagModel bannerFlagModel, float phase, DyeColor dyeColor, BannerPatternLayers bannerPatternLayers, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, int outlineColor, Operation<Void> operation)
    {
        EnchantedBannerRenderer.submitEnchantedBanner(materialSet, poseStack, submitNodeCollector, lightCoords, overlayCoords, rotationDegrees, bannerModel, bannerFlagModel, phase, dyeColor, bannerPatternLayers, crumblingOverlay, outlineColor, this.hasFoil);
    }
}