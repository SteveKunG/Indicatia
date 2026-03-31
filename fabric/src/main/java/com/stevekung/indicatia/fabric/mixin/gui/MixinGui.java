package com.stevekung.indicatia.fabric.mixin.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.llamalad7.mixinextras.sugar.Local;
import com.stevekung.indicatia.Indicatia;
import com.stevekung.indicatia.utils.RenderUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.effect.MobEffectInstance;

@Mixin(Gui.class)
public class MixinGui
{
    @Shadow
    @Final
    Minecraft minecraft;

    @Inject(method = "extractEffects", at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/gui/GuiGraphicsExtractor.blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIII)V",
            shift = At.Shift.AFTER))
    private void indicatia$addPotionTime(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo info, @Local MobEffectInstance mobEffectInstance, @Local(index = 9, ordinal = 2) int x, @Local(index = 10, ordinal = 3) int y, @Local(index = 11) float alpha)
    {
        if (Indicatia.CONFIG.displayPotionDurationOnTopRightPotionHUD)
        {
            RenderUtils.renderPotionDurationOnTopRight(this.minecraft.font, graphics, mobEffectInstance, x, y, alpha, this.minecraft.level.tickRateManager().tickrate());
        }
    }
}