package com.stevekung.indicatia.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;

public class RenderUtils
{
    public static void renderPotionDurationOnTopRight(Minecraft minecraft, PoseStack poseStack, MobEffectInstance mobEffectInstance, int x, int y, float alpha)
    {
        var ticks = Mth.floor((float) mobEffectInstance.getDuration());
        var text = Component.literal(StringUtil.formatTickDuration(ticks)).withStyle(Style.EMPTY.withFont(Minecraft.UNIFORM_FONT));
        var color = 0xFFFFFF | Mth.floor(alpha * 255.0F) << 24 & 0xFF000000;
        GuiComponent.drawCenteredString(poseStack, minecraft.font, text, x + 12, y + 15, color);
        RenderSystem.enableBlend();
    }
}