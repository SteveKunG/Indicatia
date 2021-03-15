package com.stevekung.indicatia.mixin.gui.components;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.utils.PlatformConfig;
import net.minecraft.client.gui.components.BossHealthOverlay;

@Mixin(BossHealthOverlay.class)
public class MixinBossHealthOverlay
{
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;)V", cancellable = true, at = @At("HEAD"))
    private void render(PoseStack poseStack, CallbackInfo info)
    {
        if (!PlatformConfig.getRenderBossHealthBar())
        {
            info.cancel();
        }
    }

    @ModifyConstant(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;)V", constant = @Constant(intValue = 3))
    private int modifyHeight(int defaultValue)
    {
        return PlatformConfig.getRenderBossHealthBar() ? defaultValue : 4;
    }
}