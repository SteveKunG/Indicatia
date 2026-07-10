package com.stevekung.indicatia.mixin.renderer.special;

import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.Indicatia;

import net.minecraft.client.model.object.statue.CopperGolemStatueModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.CopperGolemStatueSpecialRenderer;

@Mixin(CopperGolemStatueSpecialRenderer.class)
public class MixinCopperGolemStatueSpecialRenderer
{
    @Shadow
    @Final
    CopperGolemStatueModel model;

    @Inject(method = "submit", at = @At("TAIL"))
    private void indicatia$addEnchantedGlint(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor, CallbackInfo info)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnAllBlockEntities && hasFoil)
        {
            submitNodeCollector.order(1).submitModel(this.model, Unit.INSTANCE, poseStack, RenderTypes.entityGlint(), packedLight, packedOverlay, -1, null, outlineColor, null);
        }
    }
}