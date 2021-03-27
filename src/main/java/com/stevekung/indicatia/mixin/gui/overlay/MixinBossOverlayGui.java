package com.stevekung.indicatia.mixin.gui.overlay;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.stevekung.indicatia.config.IndicatiaConfig;

import net.minecraft.client.gui.overlay.BossOverlayGui;

@Mixin(BossOverlayGui.class)
public class MixinBossOverlayGui
{
    @Inject(method = "func_238484_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;)V", cancellable = true, at = @At("HEAD"))
    private void render(MatrixStack matrixStack, CallbackInfo info)
    {
        if (!IndicatiaConfig.GENERAL.enableBossHealthBarRender.get())
        {
            info.cancel();
        }
    }

    @ModifyVariable(method = "func_238484_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;)V", at = @At(value = "INVOKE", target = "net/minecraft/client/MainWindow.getScaledHeight()I", shift = Shift.BEFORE), index = 3, ordinal = 1)
    private int modifyIncrement(int defaultValue)
    {
        return IndicatiaConfig.GENERAL.enableBossHealthBarRender.get() ? defaultValue : 12;
    }

    @ModifyConstant(method = "func_238484_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;)V", constant = @Constant(intValue = 3))
    private int modifyHeight(int defaultValue)
    {
        return IndicatiaConfig.GENERAL.enableBossHealthBarRender.get() ? defaultValue : 4;
    }
}