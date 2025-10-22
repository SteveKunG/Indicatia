package com.stevekung.indicatia;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.model.BannerFlagModel;
import net.minecraft.client.model.BannerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class EnchantedBannerRenderer
{
    public static void submitEnchantedBanner(MaterialSet materialSet, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, float rotationDegrees, BannerModel bannerModel, BannerFlagModel bannerFlagModel, float phase, DyeColor dyeColor, BannerPatternLayers bannerPatternLayers, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, int outlineColor, boolean hasFoil)
    {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.0F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationDegrees));
        poseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
        var material = ModelBakery.BANNER_BASE;
        submitNodeCollector.submitModel(bannerModel, Unit.INSTANCE, poseStack, material.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, -1, materialSet.get(material), outlineColor, crumblingOverlay);

        if (hasFoil)
        {
            submitNodeCollector.submitModel(bannerModel, Unit.INSTANCE, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, -1, materialSet.get(material), outlineColor, crumblingOverlay);
        }

        BannerRenderer.submitPatterns(materialSet, poseStack, submitNodeCollector, lightCoords, overlayCoords, bannerFlagModel, phase, material, true, dyeColor, bannerPatternLayers, hasFoil, crumblingOverlay, outlineColor);
        poseStack.popPose();
    }
}