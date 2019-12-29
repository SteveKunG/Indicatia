package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.stevekung.indicatia.config.IndicatiaConfig;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

@Mixin(FirstPersonRenderer.class)
public abstract class MixinFirstPersonRenderer
{
    @Inject(method = "func_228405_a_(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V", at =
        {
                @At(value = "INVOKE", target = "net/minecraft/client/renderer/FirstPersonRenderer.func_228406_b_(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/util/HandSide;F)V", shift = At.Shift.AFTER, ordinal = 2),
                @At(value = "INVOKE", target = "net/minecraft/client/renderer/FirstPersonRenderer.func_228406_b_(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/util/HandSide;F)V", shift = At.Shift.AFTER, ordinal = 3),
                @At(value = "INVOKE", target = "net/minecraft/client/renderer/FirstPersonRenderer.func_228406_b_(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/util/HandSide;F)V", shift = At.Shift.AFTER, ordinal = 4),
                @At(value = "INVOKE", target = "net/minecraft/client/renderer/FirstPersonRenderer.func_228406_b_(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/util/HandSide;F)V", shift = At.Shift.AFTER, ordinal = 5),
                @At(value = "INVOKE", target = "net/minecraft/client/renderer/FirstPersonRenderer.func_228406_b_(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/util/HandSide;F)V", shift = At.Shift.AFTER, ordinal = 6)
        })
    private void renderItemInFirstPerson(AbstractClientPlayerEntity player, float partialTicks, float rotationPitch, Hand hand, float swingProgress, ItemStack itemStack, float equipProgress, MatrixStack stack, IRenderTypeBuffer buffer, int color, CallbackInfo info)
    {
        if (IndicatiaConfig.GENERAL.enableBlockhitAnimation.get())
        {
            float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
            float f1 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float)Math.PI);
            RenderSystem.translatef(0.0F, equipProgress * 0.6F, 0.0F);
            RenderSystem.rotatef(0.0F, 0.0F, 1.0F, 0.0F);
            RenderSystem.rotatef(f * 20.0F, 0.0F, 1.0F, 0.0F);
            RenderSystem.rotatef(f1 * 20.0F, 0.0F, 0.0F, 1.0F);
            RenderSystem.rotatef(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        }
    }
}