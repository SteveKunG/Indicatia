package com.stevekung.indicatia.mixin.renderer.special;

import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.Indicatia;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.ChestSpecialRenderer;

@Mixin(ChestSpecialRenderer.class)
public class MixinChestSpecialRenderer
{
    @WrapOperation(method = "submit", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/SubmitNodeCollector.submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;IIILnet/minecraft/client/resources/model/sprite/SpriteId;Lnet/minecraft/client/resources/model/sprite/SpriteGetter;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
    private <S> void indicatia$addEnchantedGlint(SubmitNodeCollector submitNodeCollector, Model<? super S> model, S object, PoseStack poseStack, int lightCoords, int overlayCoords, int tintedColor, SpriteId sprite, SpriteGetter sprites, int outlineColor, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, Operation<Void> operation, @Local(argsOnly = true) boolean hasFoil)
    {
        operation.call(submitNodeCollector, model, object, poseStack, lightCoords, overlayCoords, tintedColor, sprite, sprites, outlineColor, crumblingOverlay);

        if (Indicatia.CONFIG.enableEnchantedRenderingOnAllBlockEntities && hasFoil)
        {
            submitNodeCollector.order(1).submitModel(model, object, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, tintedColor, null, outlineColor, crumblingOverlay);
        }
    }
}