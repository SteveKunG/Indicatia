package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.stevekung.stevekungslib.utils.client.RenderUtils;

import net.minecraft.client.renderer.entity.layers.ArrowLayer;

@Mixin(ArrowLayer.class)
public abstract class MixinArrowLayer
{
    @Redirect(method = "render(Lnet/minecraft/entity/LivingEntity;FFFFFFF)V", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/RenderHelper.enableStandardItemLighting()V"), expect = 0)
    private void enableStandardItemLighting()
    {
        RenderUtils.enableLighting();
    }
}