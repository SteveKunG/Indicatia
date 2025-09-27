package com.stevekung.indicatia.mixin.renderer.special;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.Indicatia;

import net.minecraft.client.model.ChestModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.ChestSpecialRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.world.item.ItemDisplayContext;

@Mixin(ChestSpecialRenderer.class)
public class MixinChestSpecialRenderer
{
    @Shadow
    @Final
    MaterialSet materials;

    @Shadow
    @Final
    ChestModel model;

    @Shadow
    @Final
    Material material;

    @Shadow
    @Final
    float openness;

    @Inject(method = "submit", at = @At("TAIL"))
    private void indicatia$addEnchantedGlint(ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor, CallbackInfo info)
    {
        if (Indicatia.CONFIG.enableEnchantedRenderingOnAllBlockEntities && hasFoil)
        {
            submitNodeCollector.submitModel(this.model, this.openness, poseStack, RenderType.entityGlint(), packedLight, packedOverlay, -1, this.materials.get(this.material), 0, null);
        }
    }
}