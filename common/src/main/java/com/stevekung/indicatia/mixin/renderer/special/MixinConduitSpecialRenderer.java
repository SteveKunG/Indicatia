package com.stevekung.indicatia.mixin.renderer.special;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.sugar.Local;
import com.stevekung.indicatia.Indicatia;

import net.minecraft.client.renderer.special.ConduitSpecialRenderer;

@Mixin(ConduitSpecialRenderer.class)
public class MixinConduitSpecialRenderer
{
    @ModifyArg(method = "submit", at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/renderer/SubmitNodeCollector.submitModelPart(Lnet/minecraft/client/model/geom/ModelPart;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ZZILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V"),
            index = 7)
    private boolean indicatia$addEnchantedGlint(boolean original, @Local(argsOnly = true) boolean hasFoil)
    {
        return Indicatia.CONFIG.enableEnchantedRenderingOnAllBlockEntities && hasFoil;
    }
}