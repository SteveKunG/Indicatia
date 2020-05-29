package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stevekung.indicatia.config.IndicatiaConfig;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.overlay.BossOverlayGui;
import net.minecraft.world.BossInfo;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

@Mixin(BossOverlayGui.class)
public abstract class MixinBossOverlayGui extends AbstractGui
{
    @Inject(method = "render(IILnet/minecraft/world/BossInfo;)V", cancellable = true, at = @At("HEAD"))
    private void render(int x, int y, BossInfo info, CallbackInfo ci)
    {
        if (!IndicatiaConfig.GENERAL.enableBossHealthBarRender.get())
        {
            ci.cancel();
        }
    }

    @Redirect(method = "render()V", at = @At(value = "INVOKE", target = "net/minecraftforge/client/event/RenderGameOverlayEvent$BossInfo.getIncrement()I", remap = false))
    private int modifyIncrement(RenderGameOverlayEvent.BossInfo event)
    {
        return IndicatiaConfig.GENERAL.enableBossHealthBarRender.get() ? event.getIncrement() : 12;
    }

    @ModifyConstant(method = "render()V", constant = @Constant(intValue = 3))
    private int modifyHeight(int defaultValue)
    {
        return IndicatiaConfig.GENERAL.enableBossHealthBarRender.get() ? defaultValue : 4;
    }
}