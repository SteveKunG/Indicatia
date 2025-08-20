package com.stevekung.indicatia.mixin.renderer;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.stevekung.indicatia.Indicatia;
import com.stevekung.indicatia.utils.EnchantedSkullItemCache;

import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;

@Mixin(ModelFeatureRenderer.class)
public class MixinModelFeatureRenderer
{
    @ModifyArg(method = "renderTranslucents", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/feature/ModelFeatureRenderer.renderModel(Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/renderer/OutlineBufferSource;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;)V"), index = 2)
    private VertexConsumer indicatia$useEnchantedVertexForTranslucent(VertexConsumer original, @Local(ordinal = 0, argsOnly = true) MultiBufferSource.BufferSource bufferSource, @Local SubmitNodeStorage.TranslucentModelSubmit<?> translucentModelSubmit)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnSkulls)
        {
            var modelSubmit = translucentModelSubmit.modelSubmit();

            if (modelSubmit.model() instanceof SkullModelBase && modelSubmit.state() instanceof SkullModelBase.State)
            {
                return !EnchantedSkullItemCache.playerHead ? ItemRenderer.getFoilBuffer(bufferSource, RenderType.armorEntityGlint(), false, EnchantedSkullItemCache.glintNext) : ItemRenderer.getFoilBuffer(bufferSource, translucentModelSubmit.renderType(), false, EnchantedSkullItemCache.glintNext);
            }
        }
        return original;
    }

    @ModifyArg(method = "renderBatch", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/feature/ModelFeatureRenderer.renderModel(Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/renderer/OutlineBufferSource;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;)V"), index = 2)
    private VertexConsumer indicatia$useEnchantedVertexForBatch(VertexConsumer original, @Local(ordinal = 0, argsOnly = true) MultiBufferSource.BufferSource bufferSource, @Local Map.Entry<RenderType, List<SubmitNodeStorage.ModelSubmit<?>>> entry, @Local SubmitNodeStorage.ModelSubmit<?> modelSubmit)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnSkulls)
        {
            if (modelSubmit.model() instanceof SkullModelBase && modelSubmit.state() instanceof SkullModelBase.State)
            {
                return !EnchantedSkullItemCache.playerHead ? ItemRenderer.getFoilBuffer(bufferSource, RenderType.armorEntityGlint(), false, EnchantedSkullItemCache.glintNext) : ItemRenderer.getFoilBuffer(bufferSource, entry.getKey(), false, EnchantedSkullItemCache.glintNext);
            }
        }
        return original;
    }
}