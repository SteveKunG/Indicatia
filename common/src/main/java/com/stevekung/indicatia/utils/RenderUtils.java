package com.stevekung.indicatia.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;

public class RenderUtils
{
    public static void renderPotionDurationOnTopRight(Minecraft minecraft, PoseStack poseStack, MobEffectInstance mobEffectInstance, int x, int y, float alpha)
    {
        var isInfinite = mobEffectInstance.isInfiniteDuration();
        var ticks = Mth.floor((float) mobEffectInstance.getDuration());
        var component = isInfinite ? Component.translatable("effect.duration.infinite") : Component.literal(StringUtil.formatTickDuration(ticks));
        var text = component.withStyle(Style.EMPTY.withFont(isInfinite ? null : Minecraft.UNIFORM_FONT));
        var color = 0xFFFFFF | Mth.floor(alpha * 255.0F) << 24 & 0xFF000000;
        var formattedCharSequence = text.getVisualOrderText();
        var xf = (float)(x - minecraft.font.width(formattedCharSequence) / 2);
        minecraft.font.drawShadow(poseStack, formattedCharSequence, xf + 12f + (isInfinite ? 0.5f : 0f), y + 15, color);
        RenderSystem.enableBlend();
    }
}