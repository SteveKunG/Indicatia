package com.stevekung.indicatia.utils;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.font.FontOption;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;

public class RenderUtils
{
    private static final FontDescription UNIFORM = new FontDescription.Resource(Identifier.withDefaultNamespace(FontOption.UNIFORM.getSerializedName()));

    public static void renderPotionDurationOnTopRight(Font font, GuiGraphicsExtractor graphics, MobEffectInstance mobEffectInstance, int x, int y, float alpha, float ticksPerSecond)
    {
        var isInfinite = mobEffectInstance.isInfiniteDuration();
        var ticks = Mth.floor((float) mobEffectInstance.getDuration());
        var component = isInfinite ? Component.translatable("effect.duration.infinite") : Component.literal(StringUtil.formatTickDuration(ticks, ticksPerSecond));
        var text = component.withStyle(Style.EMPTY.withFont(isInfinite ? null : UNIFORM));
        var color = 0xFFFFFF | Mth.floor(alpha * 255.0F) << 24 & 0xFF000000;
        graphics.centeredText(font, text, x + 12, y + 15, color);
    }
}