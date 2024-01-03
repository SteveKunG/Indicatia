package com.stevekung.indicatia.neoforge.mixin.gui;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import com.llamalad7.mixinextras.sugar.Local;
import com.stevekung.indicatia.Indicatia;
import com.stevekung.indicatia.utils.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.effect.MobEffectInstance;

@Mixin(Gui.class)
public class MixinGui
{
    @Inject(method = "renderEffects", at = @At(value = "INVOKE", target = "java/util/List.add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, remap = false), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void indicatia$addPotionTime(GuiGraphics guiGraphics, CallbackInfo info, @Local List<Runnable> list, @Local MobEffectInstance mobEffectInstance, @Local(index = 12, ordinal = 2) int x, @Local(index = 13, ordinal = 3) int y, @Local(index = 17, ordinal = 1) float alpha)
    {
        if (Indicatia.CONFIG.displayPotionDurationOnTopRightPotionHUD)
        {
            list.add(() -> RenderUtils.renderPotionDurationOnTopRight(Gui.class.cast(this).getFont(), guiGraphics, mobEffectInstance, x, y, alpha));
        }
    }
}