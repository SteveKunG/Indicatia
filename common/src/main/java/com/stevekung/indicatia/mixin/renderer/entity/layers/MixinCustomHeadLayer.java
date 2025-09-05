package com.stevekung.indicatia.mixin.renderer.entity.layers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

@Mixin(CustomHeadLayer.class)
public class MixinCustomHeadLayer
{
    @ModifyArg(method = "submit", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.submitSkull(Lnet/minecraft/core/Direction;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/model/SkullModelBase;Lnet/minecraft/client/renderer/RenderType;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"), index = 6)
    private SkullModelBase indicatia$storeEnchantedCacheIntoModel(SkullModelBase model, @Local(argsOnly = true) LivingEntityRenderState livingEntityRenderState)
    {
        model.indicatia$setFoil(livingEntityRenderState.indicatia$hasFoil());
        return model;
    }
}