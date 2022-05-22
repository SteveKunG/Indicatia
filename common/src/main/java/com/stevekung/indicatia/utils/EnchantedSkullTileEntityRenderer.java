package com.stevekung.indicatia.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.core.Indicatia;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class EnchantedSkullTileEntityRenderer
{
    public static void render(GameProfile gameProfile, float rotationYaw, float mouthAnimation, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, SkullModelBase skullModelBase, RenderType renderType, boolean glint)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnSkull)
        {
            poseStack.pushPose();
            poseStack.translate(0.5D, 0.0D, 0.5D);
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            skullModelBase.setupAnim(mouthAnimation, rotationYaw, 0.0F);
            var vertexConsumer = gameProfile == null ? ItemRenderer.getArmorFoilBuffer(multiBufferSource, renderType, false, glint) : ItemRenderer.getFoilBufferDirect(multiBufferSource, renderType, false, glint);
            skullModelBase.renderToBuffer(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }
}