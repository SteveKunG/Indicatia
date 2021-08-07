package com.stevekung.indicatia.mixin.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.stevekung.indicatia.utils.PlatformConfig;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer
{
    @Inject(method = "renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", slice = @Slice(from = @At(value = "INVOKE", target = "net/minecraft/client/renderer/ItemInHandRenderer.applyEatTransform(Lcom/mojang/blaze3d/vertex/PoseStack;FLnet/minecraft/world/entity/HumanoidArm;Lnet/minecraft/world/item/ItemStack;)V"), to = @At(value = "INVOKE", target = "net/minecraft/client/player/AbstractClientPlayer.isAutoSpinAttack()Z")), at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/ItemInHandRenderer.applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V", shift = At.Shift.AFTER))
    private void renderArmWithItem(AbstractClientPlayer player, float partialTicks, float rotationPitch, InteractionHand hand, float swingProgress, ItemStack itemStack, float equipProgress, PoseStack poseStack, MultiBufferSource buffer, int color, CallbackInfo info)
    {
        if (PlatformConfig.getBlockHitAnimation())
        {
            var f = Mth.sin(swingProgress * swingProgress * (float) Math.PI);
            var f1 = Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI);
            poseStack.translate(0.0F, equipProgress * 0.6F, 0.0F);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(f * 20.0F));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(f1 * 20.0F));
            poseStack.mulPose(Vector3f.XP.rotationDegrees(f1 * -80.0F));
        }
    }
}