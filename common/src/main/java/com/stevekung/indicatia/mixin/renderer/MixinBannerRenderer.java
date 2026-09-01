package com.stevekung.indicatia.mixin.renderer;

import net.minecraft.client.resources.model.sprite.SpriteGetter;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.EnchantedBannerRenderer;
import com.stevekung.indicatia.EnchantedFoilExtender;

import net.minecraft.client.model.object.banner.BannerFlagModel;
import net.minecraft.client.model.object.banner.BannerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
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

    @WrapOperation(method = "submitSpecial", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/BannerRenderer.submitBanner(Lnet/minecraft/client/resources/model/sprite/SpriteGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IILnet/minecraft/client/model/object/banner/BannerModel;Lnet/minecraft/client/model/object/banner/BannerFlagModel;FLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V"))
    private void indicatia$addEnchantedGlint(SpriteGetter sprites, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, BannerModel bannerModel, BannerFlagModel bannerFlagModel, float phase, DyeColor baseColor, BannerPatternLayers patterns, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress, int outlineColor, Operation<Void> operation)
    {
        EnchantedBannerRenderer.submitEnchantedBanner(sprites, poseStack, submitNodeCollector, lightCoords, overlayCoords, bannerModel, bannerFlagModel, phase, baseColor, patterns, outlineColor, this.hasFoil);
    }
}