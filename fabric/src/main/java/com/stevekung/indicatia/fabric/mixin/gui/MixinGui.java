package com.stevekung.indicatia.fabric.mixin.gui;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.event.HUDRenderEventHandler;
import com.stevekung.indicatia.fabric.core.IndicatiaFabric;
import com.stevekung.stevekunglib.utils.ColorUtils;
import com.stevekung.stevekunglib.utils.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

@Mixin(Gui.class)
public class MixinGui
{
    @Shadow
    @Final
    Minecraft minecraft;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/components/BossHealthOverlay.render(Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
    private void indicatia$renderBossOverlay(BossHealthOverlay overlay, PoseStack poseStack)
    {
        if (IndicatiaFabric.CONFIG.general.enableRenderBossHealthStatus)
        {
            overlay.render(poseStack);
        }
        else
        {
            RenderSystem.enableDepthTest();
            RenderSystem.defaultBlendFunc();
        }
    }

    @Inject(method = "renderEffects", cancellable = true, at = @At("HEAD"))
    private void indicatia$disableVanillaHUD(PoseStack poseStack, CallbackInfo info)
    {
        if (!IndicatiaFabric.CONFIG.general.enableVanillaPotionHUD)
        {
            info.cancel();
        }
    }

    @Inject(method = "render", at = @At(value = "FIELD", target = "net/minecraft/client/Options.hideGui:Z", shift = At.Shift.BEFORE, ordinal = 0))
    private void indicatia$renderHUD(PoseStack poseStack, float partialTicks, CallbackInfo info)
    {
        HUDRenderEventHandler.INSTANCE.onPreInfoRender(this.minecraft, poseStack);
        RenderSystem.enableBlend();
    }

    @Inject(method = "renderEffects", at = @At(value = "INVOKE", target = "java/util/List.add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, remap = false), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void indicatia$addPotionTime(PoseStack poseStack, CallbackInfo info, Collection<MobEffectInstance> collection, int i, int j, MobEffectTextureManager mobEffectTextureManager, List<Runnable> list, Iterator<MobEffectInstance> iterator, MobEffectInstance mobEffectInstance, MobEffect mobEffect, int x, int y, float alpha, TextureAtlasSprite textureAtlasSprite, int n, int o, float g)
    {
        if (IndicatiaSettings.INSTANCE.timeOnVanillaPotionHUD)
        {
            list.add(() ->
            {
                var ticks = Mth.floor((float) mobEffectInstance.getDuration());
                var text = new TextComponent(StringUtil.formatTickDuration(ticks)).withStyle(Style.EMPTY.withFont(ClientUtils.UNICODE));
                GuiComponent.drawCenteredString(poseStack, this.minecraft.font, text, x + 12, y + 15, ColorUtils.to32Bit(255, 255, 255, (int) (alpha * 255)));
                RenderSystem.enableBlend();
            });
        }
    }
}