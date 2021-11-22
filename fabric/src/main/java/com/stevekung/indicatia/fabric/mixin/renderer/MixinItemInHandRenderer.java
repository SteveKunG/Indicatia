package com.stevekung.indicatia.fabric.mixin.renderer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.gui.exconfig.screens.OffsetRenderPreviewScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer
{
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "renderArmWithItem", cancellable = true, at = @At("HEAD"))
    private void indicatia$cancelRenderHand(AbstractClientPlayer abstractClientPlayer, float f, float g, InteractionHand interactionHand, float h, ItemStack itemStack, float i, PoseStack poseStack, MultiBufferSource multiBufferSource, int j, CallbackInfo info)
    {
        if (this.minecraft.screen instanceof OffsetRenderPreviewScreen)
        {
            info.cancel();
        }
    }
}