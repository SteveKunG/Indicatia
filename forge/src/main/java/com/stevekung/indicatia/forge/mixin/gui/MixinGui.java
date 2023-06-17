package com.stevekung.indicatia.forge.mixin.gui;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import com.stevekung.indicatia.Indicatia;
import com.stevekung.indicatia.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

@Mixin(Gui.class)
public class MixinGui
{
    @Shadow
    @Final
    Minecraft minecraft;

    @Inject(method = "renderEffects", at = @At(value = "INVOKE", target = "java/util/List.add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER, remap = false), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void indicatia$addPotionTime(GuiGraphics guiGraphics, CallbackInfo info, Collection<MobEffectInstance> collection, Screen screen, int j1, int k1, MobEffectTextureManager mobEffectTextureManager, List<Runnable> list, Iterator<MobEffectInstance> iterator, MobEffectInstance mobEffectInstance, MobEffect mobEffect, IClientMobEffectExtensions extensions, int x, int y, float alpha, TextureAtlasSprite textureAtlasSprite, int i1, float f1, int i_f)
    {
        if (Indicatia.CONFIG.timeOnVanillaPotionHUD)
        {
            list.add(() -> RenderUtils.renderPotionDurationOnTopRight(this.minecraft, guiGraphics, mobEffectInstance, x, y, alpha));
        }
    }
}