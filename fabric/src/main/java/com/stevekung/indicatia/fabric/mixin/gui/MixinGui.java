package com.stevekung.indicatia.fabric.mixin.gui;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.llamalad7.mixinextras.sugar.Local;
import com.stevekung.indicatia.Indicatia;
import com.stevekung.indicatia.utils.FlashbackHelper;
import com.stevekung.indicatia.utils.RenderUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.GameType;

@Mixin(Gui.class)
public class MixinGui
{
    @Shadow
    @Final
    Minecraft minecraft;

    @Inject(method = "renderEffects", at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/gui/GuiGraphics.blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIII)V",
            shift = At.Shift.AFTER))
    private void indicatia$addPotionTime(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info, @Local MobEffectInstance mobEffectInstance, @Local(index = 9, ordinal = 2) int x, @Local(index = 10, ordinal = 3) int y, @Local(index = 11) float alpha)
    {
        if (Indicatia.CONFIG.displayPotionDurationOnTopRightPotionHUD)
        {
            RenderUtils.renderPotionDurationOnTopRight(this.minecraft.font, guiGraphics, mobEffectInstance, x, y, alpha, this.minecraft.level.tickRateManager().tickrate());
        }
    }

    @Inject(method = "renderHotbarAndDecorations", at = @At("TAIL"))
    private void renderPhantomIndicator(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info)
    {
        Indicatia.renderPhantomIndicator(guiGraphics, this.minecraft);
    }
}