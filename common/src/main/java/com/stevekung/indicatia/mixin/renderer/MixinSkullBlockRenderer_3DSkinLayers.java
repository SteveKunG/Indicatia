package com.stevekung.indicatia.mixin.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.stevekung.indicatia.Indicatia;
import com.stevekung.indicatia.utils.EnchantedSkullItemCache;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;

@Mixin(value = SkullBlockRenderer.class, priority = 1100)
public class MixinSkullBlockRenderer_3DSkinLayers
{
    @TargetHandler(mixin = "dev.tr7zw.skinlayers.mixin.SkullBlockEntityRendererMixin", name = "renderSkull", prefix = "handler")
    @ModifyArg(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "dev/tr7zw/skinlayers/api/Mesh.render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"), index = 1)
    private static VertexConsumer indicatia$useEnchantedVertexFor3DSkinLayers(VertexConsumer original, @Local(argsOnly = true) MultiBufferSource bufferSource, @Local(argsOnly = true) RenderType renderType)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnSkulls)
        {
            return EnchantedSkullItemCache.lastGameProfile == null ? ItemRenderer.getArmorFoilBuffer(bufferSource, renderType, false, EnchantedSkullItemCache.glintNext) : ItemRenderer.getFoilBufferDirect(bufferSource, renderType, false, EnchantedSkullItemCache.glintNext);
        }
        return original;
    }
}