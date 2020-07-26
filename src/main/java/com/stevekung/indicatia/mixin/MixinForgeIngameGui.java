package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraftforge.client.gui.ForgeIngameGui;

@Mixin(ForgeIngameGui.class)
public class MixinForgeIngameGui extends IngameGui
{
    public MixinForgeIngameGui(Minecraft mc)
    {
        super(mc);
    }

    @Inject(method = "renderChat(IILcom/mojang/blaze3d/matrix/MatrixStack;)V", at = @At(value = "INVOKE", target = "com/mojang/blaze3d/systems/RenderSystem.translatef(FFF)V", shift = At.Shift.AFTER))
    private void renderChatBefore(int width, int height, MatrixStack matrixStack, CallbackInfo info)
    {
        RenderSystem.disableDepthTest();
    }

    @Inject(method = "renderChat(IILcom/mojang/blaze3d/matrix/MatrixStack;)V", at = @At(value = "INVOKE", target = "com/mojang/blaze3d/systems/RenderSystem.popMatrix()V", shift = At.Shift.BEFORE))
    private void renderChatAfter(int width, int height, MatrixStack matrixStack, CallbackInfo info)
    {
        RenderSystem.enableDepthTest();
    }
}