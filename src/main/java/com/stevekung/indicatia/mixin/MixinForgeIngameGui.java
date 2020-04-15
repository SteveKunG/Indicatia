package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraftforge.client.ForgeIngameGui;

@Mixin(value = ForgeIngameGui.class, remap = false)
public class MixinForgeIngameGui
{
    @Inject(method = "renderChat(II)V", at = @At(value = "INVOKE", target = "com/mojang/blaze3d/platform/GlStateManager.translatef(FFF)V", shift = At.Shift.AFTER))
    private void renderChatBefore(int width, int height, CallbackInfo info)
    {
        GlStateManager.disableDepthTest();
    }

    @Inject(method = "renderChat(II)V", at = @At(value = "INVOKE", target = "com/mojang/blaze3d/platform/GlStateManager.popMatrix()V", shift = At.Shift.BEFORE))
    private void renderChatAfter(int width, int height, CallbackInfo info)
    {
        GlStateManager.enableDepthTest();
    }
}