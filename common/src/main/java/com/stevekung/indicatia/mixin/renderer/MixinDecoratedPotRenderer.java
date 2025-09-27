package com.stevekung.indicatia.mixin.renderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.stevekung.indicatia.EnchantedFoilExtender;

import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;

@Mixin(DecoratedPotRenderer.class)
public class MixinDecoratedPotRenderer implements EnchantedFoilExtender
{
    @Unique
    private boolean hasFoil;

    @Override
    public void indicatia$setFoil(boolean hasFoil)
    {
        this.hasFoil = hasFoil;
    }

    @Override
    public boolean indicatia$hasFoil()
    {
        return this.hasFoil;
    }

    @ModifyArg(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IILnet/minecraft/world/level/block/entity/PotDecorations;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/renderer/SubmitNodeCollector.submitModelPart(Lnet/minecraft/client/model/geom/ModelPart;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/RenderType;IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ZZILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V"),
            index = 7)
    private boolean indicatia$addEnchantedGlint(boolean original)
    {
        return this.hasFoil;
    }
}