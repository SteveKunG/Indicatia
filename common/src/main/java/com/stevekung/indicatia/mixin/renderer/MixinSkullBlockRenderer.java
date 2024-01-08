package com.stevekung.indicatia.mixin.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.stevekung.indicatia.Indicatia;
import com.stevekung.indicatia.utils.EnchantedSkullItemCache;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;

@Mixin(SkullBlockRenderer.class)
public class MixinSkullBlockRenderer
{
    @ModifyVariable(method = "renderSkull", at = @At(value = "STORE"))
    private static VertexConsumer indicatia$useEnchantedVertex(VertexConsumer original, @Local(argsOnly = true) MultiBufferSource bufferSource, @Local(argsOnly = true) RenderType renderType)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnSkulls)
        {
            return EnchantedSkullItemCache.lastGameProfile == null ? ItemRenderer.getArmorFoilBuffer(bufferSource, renderType, false, EnchantedSkullItemCache.glintNext) : ItemRenderer.getFoilBufferDirect(bufferSource, renderType, false, EnchantedSkullItemCache.glintNext);
        }
        return original;
    }
}