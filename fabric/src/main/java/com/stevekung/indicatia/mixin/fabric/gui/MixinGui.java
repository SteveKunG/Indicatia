package com.stevekung.indicatia.mixin.fabric.gui;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.core.IndicatiaFabricMod;
import com.stevekung.indicatia.event.HUDRenderEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.BossHealthOverlay;

@Mixin(Gui.class)
public class MixinGui
{
    @Shadow
    @Final
    private Minecraft minecraft;

    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;F)V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/components/BossHealthOverlay.render(Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
    private void redirectBossOverlay(BossHealthOverlay overlay, PoseStack poseStack)
    {
        if (IndicatiaFabricMod.CONFIG.getConfig().enableRenderBossHealthStatus)
        {
            overlay.render(poseStack);
        }
        else
        {
            RenderSystem.enableDepthTest();
            RenderSystem.defaultBlendFunc();
        }
    }

    @Inject(method = "renderEffects(Lcom/mojang/blaze3d/vertex/PoseStack;)V", cancellable = true, at = @At("HEAD"))
    private void disableVanillaHUD(PoseStack poseStack, CallbackInfo info)
    {
        if (!IndicatiaFabricMod.CONFIG.getConfig().enableVanillaPotionHUD)
        {
            info.cancel();
        }
    }

    @SuppressWarnings("deprecation")
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;F)V", at = @At(value = "FIELD", target = "net/minecraft/client/Options.hideGui:Z", shift = At.Shift.BEFORE))
    private void renderHUD(PoseStack poseStack, float partialTicks, CallbackInfo info)
    {
        HUDRenderEventHandler.INSTANCE.onPreInfoRender(this.minecraft, poseStack);
        RenderSystem.enableBlend();
        RenderSystem.enableAlphaTest();
    }
}