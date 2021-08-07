package com.stevekung.indicatia.utils;

import java.util.Arrays;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.level.block.SkullBlock;

public class EnchantedSkullTileEntityRenderer
{
    public static void render(GameProfile gameProfile, SkullBlock.Type type, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedHurt, SkullModelBase skullModelBase, RenderType renderType, boolean glint)
    {
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.0D, 0.5D);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        skullModelBase.setupAnim(0.0F, 180.0F, 0.0F);
        VertexConsumer vertexConsumer;

        if (gameProfile == null)
        {
            vertexConsumer = ItemRenderer.getArmorFoilBuffer(multiBufferSource, RenderType.entityCutoutNoCullZOffset(SkullBlockRenderer.SKIN_BY_TYPE.get(type)), false, glint);
        }
        else
        {
            vertexConsumer = ItemRenderer.getFoilBufferDirect(multiBufferSource, renderType, false, glint);
        }

        skullModelBase.renderToBuffer(poseStack, vertexConsumer, combinedLight, combinedHurt, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    public static boolean isVanillaHead(SkullBlock.Type skullType)
    {
        return Arrays.stream(SkullBlock.Types.values()).anyMatch(type -> skullType == type);
    }
}