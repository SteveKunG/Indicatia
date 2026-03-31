package com.stevekung.indicatia;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.object.banner.BannerFlagModel;
import net.minecraft.client.model.object.banner.BannerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class EnchantedBannerRenderer
{
    public static void submitEnchantedBanner(SpriteGetter sprites, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, BannerModel bannerModel, BannerFlagModel flagModel, float phase, DyeColor baseColor, BannerPatternLayers patterns, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress, int outlineColor, boolean hasFoil)
    {
        var sprite = Sheets.BANNER_BASE;
        submitNodeCollector.submitModel(bannerModel, Unit.INSTANCE, poseStack, lightCoords, overlayCoords, -1, sprite, sprites, outlineColor, breakProgress);
        submitNodeCollector.submitModel(flagModel, phase, poseStack, lightCoords, overlayCoords, -1, sprite, sprites, outlineColor, breakProgress);

        if (hasFoil)
        {
            submitNodeCollector.submitModel(bannerModel, Unit.INSTANCE, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, -1, null, outlineColor, breakProgress);
            submitNodeCollector.submitModel(flagModel, phase, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, -1, null, outlineColor, breakProgress);
        }

        BannerRenderer.submitPatterns(sprites, poseStack, submitNodeCollector, lightCoords, overlayCoords, flagModel, phase, true, baseColor, patterns, breakProgress);
    }
}