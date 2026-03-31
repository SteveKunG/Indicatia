package com.stevekung.indicatia.mixin.renderer.special;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.Indicatia;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.special.BannerSpecialRenderer;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

@Mixin(BannerSpecialRenderer.class)
public class MixinBannerSpecialRenderer
{
    @Shadow
    BannerRenderer bannerRenderer;

    @Inject(method = "submit", at = @At("HEAD"))
    private void indicatia$preHasFoil(@Nullable BannerPatternLayers patterns, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor, CallbackInfo info)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnAllBlockEntities)
        {
            this.bannerRenderer.indicatia$setFoil(hasFoil);
        }
    }

    @Inject(method = "submit", at = @At("TAIL"))
    private void indicatia$postHasFoil(@Nullable BannerPatternLayers patterns, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor, CallbackInfo info)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnAllBlockEntities)
        {
            this.bannerRenderer.indicatia$setFoil(false);
        }
    }
}