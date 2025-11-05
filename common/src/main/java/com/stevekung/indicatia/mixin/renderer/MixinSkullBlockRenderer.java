package com.stevekung.indicatia.mixin.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.stevekung.indicatia.Indicatia;

import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

@Mixin(SkullBlockRenderer.class)
public class MixinSkullBlockRenderer
{
    @WrapOperation(method = "getSkullRenderType", at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/renderer/rendertype/RenderTypes.entityCutoutNoCullZOffset(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;"))
    private static RenderType indicatia$changeSkullRenderType(Identifier resourceLocation, Operation<RenderType> original)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnSkulls)
        {
            return RenderTypes.entityTranslucent(resourceLocation);
        }
        return original.call(resourceLocation);
    }
}