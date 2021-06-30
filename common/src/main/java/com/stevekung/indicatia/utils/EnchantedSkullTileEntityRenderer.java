package com.stevekung.indicatia.utils;

import java.util.Arrays;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.level.block.SkullBlock;

public class EnchantedSkullTileEntityRenderer
{
    public static void render(SkullBlock.Type skullType, GameProfile gameProfile, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedHurt, boolean glint)
    {
        SkullModel genericheadmodel = SkullBlockRenderer.MODEL_BY_TYPE.get(skullType);
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0.0D, 0.5D);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        VertexConsumer ivertexbuilder;

        if (gameProfile == null)
        {
            ivertexbuilder = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.entityCutoutNoCullZOffset(SkullBlockRenderer.SKIN_BY_TYPE.get(skullType)), false, glint);
        }
        else
        {
            ivertexbuilder = ItemRenderer.getFoilBufferDirect(buffer, SkullBlockRenderer.getRenderType(skullType, gameProfile), false, glint);
        }

        genericheadmodel.setupAnim(0.0F, 180.0F, 0.0F);
        genericheadmodel.renderToBuffer(matrixStack, ivertexbuilder, combinedLight, combinedHurt, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }

    public static boolean isVanillaHead(SkullBlock.Type skullType)
    {
        return Arrays.stream(SkullBlock.Types.values()).anyMatch(type -> skullType == type);
    }
}