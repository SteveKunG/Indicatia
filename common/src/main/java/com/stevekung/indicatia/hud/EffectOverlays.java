package com.stevekung.indicatia.hud;

import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.CompareToBuilder;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.config.StatusEffects;
import com.stevekung.stevekungslib.utils.LangUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectUtil;

public class EffectOverlays
{
    @SuppressWarnings("deprecation")
    public static void renderPotionHUD(Minecraft mc, PoseStack poseStack)
    {
        var iconAndTime = IndicatiaSettings.INSTANCE.potionHUDStyle == StatusEffects.Style.ICON_AND_TIME;
        var right = IndicatiaSettings.INSTANCE.potionHUDPosition == StatusEffects.Position.RIGHT;
        var showIcon = IndicatiaSettings.INSTANCE.potionHUDIcon;
        var size = IndicatiaSettings.INSTANCE.maximumPotionDisplay;
        var length = IndicatiaSettings.INSTANCE.potionLengthYOffset;
        var lengthOverlap = IndicatiaSettings.INSTANCE.potionLengthYOffsetOverlap;
        var collection = mc.player.getActiveEffects().stream().sorted((mob1, mob2) -> new CompareToBuilder().append(mob1.getDuration(), mob2.getDuration()).build()).collect(Collectors.toCollection(TreeSet::new));
        var mobEffectTextures = mc.getMobEffectTextures();
        int xPotion;
        int yPotion;

        if (IndicatiaSettings.INSTANCE.potionHUDPosition == StatusEffects.Position.HOTBAR_LEFT)
        {
            xPotion = mc.getWindow().getGuiScaledWidth() / 2 - 91 - 35;
            yPotion = mc.getWindow().getGuiScaledHeight() - 46;
        }
        else if (IndicatiaSettings.INSTANCE.potionHUDPosition == StatusEffects.Position.HOTBAR_RIGHT)
        {
            xPotion = mc.getWindow().getGuiScaledWidth() / 2 + 91 - 20;
            yPotion = mc.getWindow().getGuiScaledHeight() - 42;
        }
        else
        {
            xPotion = right ? mc.getWindow().getGuiScaledWidth() - 32 : -24;
            yPotion = mc.getWindow().getGuiScaledHeight() - 220 + IndicatiaSettings.INSTANCE.potionHUDYOffset + 90;
        }

        if (!collection.isEmpty())
        {
            if (collection.size() > size)
            {
                length = lengthOverlap / (collection.size() - 1);
            }

            for (var effectIns : Ordering.natural().sortedCopy(collection))
            {
                var alpha = 1.0F;
                var duration = effectIns.getDuration();

                if (!effectIns.isAmbient() && duration <= 200)
                {
                    var j1 = 10 - duration / 20;
                    alpha = Mth.clamp(duration / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + Mth.cos(duration * (float) Math.PI / 5.0F) * Mth.clamp(j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                }

                var effect = effectIns.getEffect();
                var amplifier = effectIns.getAmplifier();
                var name = LangUtils.translateString(effect.getDescriptionId());
                var durationTxt = MobEffectUtil.formatDuration(effectIns, 1.0F);
                var nameWidth = mc.font.width(name);
                var durationWidth = mc.font.width(durationTxt);
                int xPotionAdd;
                var yOffset = iconAndTime ? 11 : 16;
                alpha = alpha * 255.0F;
                var alphaRGB = (int) alpha << 24 & -16777216;
                var textColor = IndicatiaSettings.INSTANCE.alternatePotionHUDTextColor ? effect.getColor() | alphaRGB : 16777215 | alphaRGB;

                if (amplifier >= 1 && amplifier <= 9)
                {
                    name = name + ' ' + LangUtils.translateString("enchantment.level." + (amplifier + 1));
                }

                if (duration > 16)
                {
                    if (IndicatiaSettings.INSTANCE.potionHUDPosition == StatusEffects.Position.HOTBAR_LEFT)
                    {
                        xPotionAdd = 12;
                        var xOffset = showIcon ? 8 : 28;

                        if (!iconAndTime)
                        {
                            mc.font.drawShadow(poseStack, name, xPotion + xOffset - nameWidth, yPotion + 6, textColor);
                        }
                        mc.font.drawShadow(poseStack, durationTxt, xPotion + xOffset - durationWidth, yPotion + yOffset, textColor);
                    }
                    else if (IndicatiaSettings.INSTANCE.potionHUDPosition == StatusEffects.Position.HOTBAR_RIGHT)
                    {
                        xPotionAdd = 24;
                        var xOffset = showIcon ? 48 : 28;

                        if (!iconAndTime)
                        {
                            mc.font.drawShadow(poseStack, name, xPotion + xOffset, yPotion + 6, textColor);
                        }
                        mc.font.drawShadow(poseStack, durationTxt, xPotion + xOffset, yPotion + yOffset, textColor);
                    }
                    else
                    {
                        xPotionAdd = 28;
                        var leftXOffset = showIcon ? 50 : 28;
                        var rightXOffset = showIcon ? 8 : 28;

                        if (!iconAndTime)
                        {
                            mc.font.drawShadow(poseStack, name, right ? xPotion + rightXOffset - nameWidth : xPotion + leftXOffset, yPotion + 6, textColor);
                        }
                        mc.font.drawShadow(poseStack, durationTxt, right ? xPotion + rightXOffset - durationWidth : xPotion + leftXOffset, yPotion + yOffset, textColor);
                    }
                    if (showIcon)
                    {
                        var sprite = mobEffectTextures.get(effectIns.getEffect());
                        RenderSystem.setShaderTexture(0, sprite.atlas().location());
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
                        GuiComponent.blit(poseStack, right ? xPotion + 12 : xPotion + xPotionAdd, yPotion + 6, mc.gui.getBlitOffset(), 18, 18, mc.getMobEffectTextures().get(effect));
                    }
                    yPotion -= length;
                }
            }
        }
    }
}