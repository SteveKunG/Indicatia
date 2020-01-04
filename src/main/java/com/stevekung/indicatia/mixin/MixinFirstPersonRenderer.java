package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.stevekung.indicatia.config.IndicatiaConfig;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

@Mixin(FirstPersonRenderer.class)
public abstract class MixinFirstPersonRenderer
{
    @Inject(method = "func_228405_a_(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V",
            slice = @Slice(from = @At(value = "INVOKE", target = "func_228406_b_", shift = At.Shift.AFTER, remap = false)),
            at = {
                    @At(value = "INVOKE", target = "func_228406_b_", shift = At.Shift.AFTER, ordinal = 2, remap = false),
                    @At(value = "INVOKE", target = "func_228406_b_", shift = At.Shift.AFTER, ordinal = 3, remap = false),
                    @At(value = "INVOKE", target = "func_228406_b_", shift = At.Shift.AFTER, ordinal = 4, remap = false),
                    @At(value = "INVOKE", target = "func_228406_b_", shift = At.Shift.AFTER, ordinal = 5, remap = false)
    })
    private void renderItemInFirstPerson(AbstractClientPlayerEntity player, float partialTicks, float rotationPitch, Hand hand, float swingProgress, ItemStack itemStack, float equipProgress, MatrixStack stack, IRenderTypeBuffer buffer, int color, CallbackInfo info)
    {
        if (IndicatiaConfig.GENERAL.enableBlockhitAnimation.get())
        {
            float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
            float f1 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float)Math.PI);
            stack.func_227861_a_(0.0F, equipProgress * 0.6F, 0.0F); // translate
            stack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(f * 20.0F)); // rotate
            stack.func_227863_a_(Vector3f.field_229183_f_.func_229187_a_(f1 * 20.0F));
            stack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(f1 * -80.0F));
        }
    }
}