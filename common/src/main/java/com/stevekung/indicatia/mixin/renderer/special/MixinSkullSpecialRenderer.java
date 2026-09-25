package com.stevekung.indicatia.mixin.renderer.special;

import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.EnchantedSkullRenderer;
import com.stevekung.indicatia.Indicatia;
import com.stevekung.indicatia.SkullSpecialRendererExtender;

import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.special.SkullSpecialRenderer;
import net.minecraft.world.level.block.SkullBlock;

@Mixin(SkullSpecialRenderer.class)
public class MixinSkullSpecialRenderer implements SkullSpecialRendererExtender
{
    @Unique
    private SkullBlock.Type kind;

    @Redirect(method = "submit", at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/renderer/blockentity/SkullBlockRenderer.submitSkull(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/model/object/skull/SkullModelBase;Lnet/minecraft/client/renderer/rendertype/RenderType;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
    private void indicatia$useCustomEnchantedSkullRenderer(float animationValue, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SkullModelBase model, RenderType renderType, int outlineColor, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, @Local(argsOnly = true) boolean hasFoil)
    {
        EnchantedSkullRenderer.submitSkull(animationValue, poseStack, submitNodeCollector, lightCoords, model, renderType, outlineColor, Indicatia.CONFIG.enableEnchantedRenderingOnSkulls && hasFoil, this.kind);
    }

    @Override
    public void setKind(SkullBlock.Type type)
    {
        this.kind = type;
    }

    @Mixin(SkullSpecialRenderer.Unbaked.class)
    public static class MixinSkullSpecialRenderer_Unbaked
    {
        @ModifyReturnValue(method = "bake", at = @At(value = "RETURN", ordinal = 1))
        private SkullSpecialRenderer indicatia$storeKind(SkullSpecialRenderer renderer)
        {
            var thisRender = SkullSpecialRenderer.Unbaked.class.cast(this);
            ((SkullSpecialRendererExtender) renderer).setKind(thisRender.kind());
            return renderer;
        }
    }
}