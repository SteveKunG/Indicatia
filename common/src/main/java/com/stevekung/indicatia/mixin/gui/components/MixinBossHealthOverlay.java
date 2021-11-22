package com.stevekung.indicatia.mixin.gui.components;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.utils.PlatformConfig;
import net.minecraft.client.gui.components.BossHealthOverlay;

@Mixin(BossHealthOverlay.class)
public class MixinBossHealthOverlay
{
    @Inject(method = "render", cancellable = true, at = @At("HEAD"))
    private void indicatia$cancelBossBarRender(PoseStack poseStack, CallbackInfo info)
    {
        if (!PlatformConfig.getRenderBossHealthBar())
        {
            info.cancel();
        }
    }

    @ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "com/mojang/blaze3d/platform/Window.getGuiScaledHeight()I", shift = At.Shift.BEFORE), index = 3, ordinal = 1)
    private int indicatia$modifyBossBarIncrement(int defaultValue)
    {
        return PlatformConfig.getRenderBossHealthBar() ? defaultValue : 12;
    }

    @ModifyConstant(method = "render", constant = @Constant(intValue = 3))
    private int indicatia$modifyBossBarHeight(int defaultValue)
    {
        return PlatformConfig.getRenderBossHealthBar() ? defaultValue : 4;
    }
}