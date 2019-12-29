package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;
import com.stevekung.indicatia.config.IndicatiaConfig;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

@Mixin(FirstPersonRenderer.class)
public abstract class MixinFirstPersonRenderer
{
    @Inject(method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;F)V", at =
        {
                @At(value = "INVOKE", target = "net/minecraft/client/renderer/FirstPersonRenderer.transformSideFirstPerson(Lnet/minecraft/util/HandSide;F)V", shift = At.Shift.AFTER, ordinal = 2),
                @At(value = "INVOKE", target = "net/minecraft/client/renderer/FirstPersonRenderer.transformSideFirstPerson(Lnet/minecraft/util/HandSide;F)V", shift = At.Shift.AFTER, ordinal = 3),
                @At(value = "INVOKE", target = "net/minecraft/client/renderer/FirstPersonRenderer.transformSideFirstPerson(Lnet/minecraft/util/HandSide;F)V", shift = At.Shift.AFTER, ordinal = 4),
                @At(value = "INVOKE", target = "net/minecraft/client/renderer/FirstPersonRenderer.transformSideFirstPerson(Lnet/minecraft/util/HandSide;F)V", shift = At.Shift.AFTER, ordinal = 5),
                @At(value = "INVOKE", target = "net/minecraft/client/renderer/FirstPersonRenderer.transformSideFirstPerson(Lnet/minecraft/util/HandSide;F)V", shift = At.Shift.AFTER, ordinal = 6)
        })
    private void renderItemInFirstPerson(AbstractClientPlayerEntity player, float partialTicks, float rotationPitch, Hand hand, float swingProgress, ItemStack itemStack, float equipProgress, CallbackInfo info)
    {
        if (IndicatiaConfig.GENERAL.enableBlockhitAnimation.get())
        {
            float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
            float f1 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float)Math.PI);
            GlStateManager.translatef(0.0F, equipProgress * 0.6F, 0.0F);
            GlStateManager.rotatef(0.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(f * 20.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef(f1 * 20.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotatef(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        }
    }
}