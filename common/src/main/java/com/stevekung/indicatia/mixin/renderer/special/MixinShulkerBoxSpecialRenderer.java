package com.stevekung.indicatia.mixin.renderer.special;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.Indicatia;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
import net.minecraft.client.renderer.special.ShulkerBoxSpecialRenderer;

@Mixin(ShulkerBoxSpecialRenderer.class)
public class MixinShulkerBoxSpecialRenderer
{
    @Shadow
    ShulkerBoxRenderer shulkerBoxRenderer;

    @Inject(method = "submit", at = @At("HEAD"))
    private void indicatia$preHasFoil(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor, CallbackInfo info)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnAllBlockEntities)
        {
            this.shulkerBoxRenderer.indicatia$setFoil(hasFoil);
        }
    }

    @Inject(method = "submit", at = @At("TAIL"))
    private void indicatia$postHasFoil(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor, CallbackInfo info)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnAllBlockEntities)
        {
            this.shulkerBoxRenderer.indicatia$setFoil(false);
        }
    }
}