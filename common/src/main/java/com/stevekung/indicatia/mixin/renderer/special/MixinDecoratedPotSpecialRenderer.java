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
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.client.renderer.special.DecoratedPotSpecialRenderer;
import net.minecraft.world.level.block.entity.PotDecorations;

@Mixin(DecoratedPotSpecialRenderer.class)
public class MixinDecoratedPotSpecialRenderer
{
    @Shadow
    DecoratedPotRenderer decoratedPotRenderer;

    @Inject(method = "submit", at = @At("HEAD"))
    private void indicatia$preHasFoil(@Nullable PotDecorations potDecorations, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor, CallbackInfo info)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnAllBlockEntities)
        {
            this.decoratedPotRenderer.indicatia$setFoil(hasFoil);
        }
    }

    @Inject(method = "submit", at = @At("TAIL"))
    private void indicatia$postHasFoil(@Nullable PotDecorations potDecorations, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor, CallbackInfo info)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnAllBlockEntities)
        {
            this.decoratedPotRenderer.indicatia$setFoil(false);
        }
    }
}