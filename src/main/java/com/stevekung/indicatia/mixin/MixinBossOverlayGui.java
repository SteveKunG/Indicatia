package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.stevekung.indicatia.config.IndicatiaConfig;

import net.minecraft.client.gui.overlay.BossOverlayGui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

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

    @Redirect(method = "func_238484_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;)V", at = @At(value = "INVOKE", target = "net/minecraftforge/client/event/RenderGameOverlayEvent$BossInfo.getIncrement()I", remap = false))
    private int modifyIncrement(RenderGameOverlayEvent.BossInfo event)
    {
        return IndicatiaConfig.GENERAL.enableBossHealthBarRender.get() ? event.getIncrement() : 12;
    }

    @ModifyConstant(method = "func_238484_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;)V", constant = @Constant(intValue = 3))
    private int modifyHeight(int defaultValue)
    {
        return IndicatiaConfig.GENERAL.enableBossHealthBarRender.get() ? defaultValue : 4;
    }
}