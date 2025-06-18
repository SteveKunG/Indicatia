package com.stevekung.indicatia.mixin.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Mixin(LivingEntityRenderer.class)
public class MixinLivingEntityRenderer
{
    @Inject(method = "extractRenderState", at = @At(
            value = "FIELD",
            target = "net/minecraft/client/renderer/entity/state/LivingEntityRenderState.wornHeadProfile:Lnet/minecraft/world/item/component/ResolvableProfile;", ordinal = 0))
    private void indicatia$extractHeadGlintState(LivingEntity livingEntity, LivingEntityRenderState livingEntityRenderState, float partialTicks, CallbackInfo info, @Local ItemStack itemStack)
    {
        livingEntityRenderState.indicatia$setPlayerHead(itemStack.is(Items.PLAYER_HEAD));
        livingEntityRenderState.indicatia$setHeadGlint(itemStack.hasFoil());
    }

    @Inject(method = "extractRenderState", at = @At(
            value = "FIELD",
            target = "net/minecraft/client/renderer/entity/state/LivingEntityRenderState.wornHeadProfile:Lnet/minecraft/world/item/component/ResolvableProfile;", ordinal = 1))
    private void indicatia$clearHeadGlintState(LivingEntity livingEntity, LivingEntityRenderState livingEntityRenderState, float partialTicks, CallbackInfo info)
    {
        livingEntityRenderState.indicatia$setPlayerHead(false);
        livingEntityRenderState.indicatia$setHeadGlint(false);
    }
}