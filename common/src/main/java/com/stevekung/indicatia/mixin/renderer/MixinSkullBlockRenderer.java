package com.stevekung.indicatia.mixin.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.stevekung.indicatia.Indicatia;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.resources.ResourceLocation;

@Mixin(SkullBlockRenderer.class)
public class MixinSkullBlockRenderer
{
    @WrapOperation(method = "getSkullRenderType", at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/renderer/RenderType.entityCutoutNoCullZOffset(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType indicatia$changeSkullRenderType(ResourceLocation resourceLocation, Operation<RenderType> original)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnSkulls)
        {
            return RenderType.entityTranslucent(resourceLocation);
        }
        return original.call(resourceLocation);
    }
}