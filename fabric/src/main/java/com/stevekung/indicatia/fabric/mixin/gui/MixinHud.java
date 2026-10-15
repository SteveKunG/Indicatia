package com.stevekung.indicatia.fabric.mixin.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
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
import net.minecraft.world.effect.MobEffectInstance;

@Mixin(Hud.class)
public class MixinHud
{
    @Shadow
    @Final
    Minecraft minecraft;

    @Inject(method = "extractEffects", at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/gui/GuiGraphicsExtractor.blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V",
            shift = At.Shift.AFTER))
    private void indicatia$addPotionTime(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo info, @Local(name = "instance") MobEffectInstance instance, @Local(name = "x") int x, @Local(name = "y") int y, @Local(name = "alpha") float alpha)
    {
        if (Indicatia.CONFIG.displayPotionDurationOnTopRightPotionHUD)
        {
            RenderUtils.renderPotionDurationOnTopRight(this.minecraft.font, graphics, instance, x, y, alpha, this.minecraft.level.tickRateManager().tickrate());
        }
    }

    @Inject(method = "extractHotbarAndDecorations", at = @At("TAIL"))
    private void indicatia$renderPhantomIndicator(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo info)
    {
        Indicatia.renderPhantomIndicator(graphics, this.minecraft);
    }
}