package com.stevekung.indicatia.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;

public class RenderUtils
{
    public static void renderPotionDurationOnTopRight(Font font, GuiGraphics guiGraphics, MobEffectInstance mobEffectInstance, int x, int y, float alpha)
    {
        var isInfinite = mobEffectInstance.isInfiniteDuration();
        var ticks = Mth.floor((float) mobEffectInstance.getDuration());
        var component = isInfinite ? Component.translatable("effect.duration.infinite") : Component.literal(StringUtil.formatTickDuration(ticks));
        var text = component.withStyle(Style.EMPTY.withFont(isInfinite ? null : Minecraft.UNIFORM_FONT));
        var color = 0xFFFFFF | Mth.floor(alpha * 255.0F) << 24 & 0xFF000000;
        guiGraphics.drawCenteredString(font, text, x + 12, y + 15, color);
    }
}