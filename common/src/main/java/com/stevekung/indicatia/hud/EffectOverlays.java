package com.stevekung.indicatia.hud;

import java.util.Collection;

import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.config.StatusEffects;
import com.stevekung.stevekungslib.utils.LangUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

public class EffectOverlays
{
    @SuppressWarnings("deprecation")
    public static void renderPotionHUD(Minecraft mc, PoseStack matrixStack)
    {
        boolean iconAndTime = IndicatiaSettings.INSTANCE.potionHUDStyle == StatusEffects.Style.ICON_AND_TIME;
        boolean right = IndicatiaSettings.INSTANCE.potionHUDPosition == StatusEffects.Position.RIGHT;
        boolean showIcon = IndicatiaSettings.INSTANCE.potionHUDIcon;
        int size = IndicatiaSettings.INSTANCE.maximumPotionDisplay;
        int length = IndicatiaSettings.INSTANCE.potionLengthYOffset;
        int lengthOverlap = IndicatiaSettings.INSTANCE.potionLengthYOffsetOverlap;
        Collection<MobEffectInstance> collection = mc.player.getActiveEffects();
        MobEffectTextureManager uploader = mc.getMobEffectTextures();
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

            for (MobEffectInstance effectIns : Ordering.natural().sortedCopy(collection))
            {
                float alpha = 1.0F;
                int duration = effectIns.getDuration();
                TextureAtlasSprite sprite = uploader.get(effectIns.getEffect());

                if (!effectIns.isAmbient() && duration <= 200)
                {
                    int j1 = 10 - duration / 20;
                    alpha = Mth.clamp(duration / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + Mth.cos(duration * (float)Math.PI / 5.0F) * Mth.clamp(j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                }

                RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
                RenderSystem.disableLighting();
                mc.getTextureManager().bind(sprite.atlas().location());

                MobEffect effect = effectIns.getEffect();
                int amplifier = effectIns.getAmplifier();
                String name = LangUtils.translateString(effect.getDescriptionId());
                String durationTxt = MobEffectUtil.formatDuration(effectIns, 1.0F);
                int stringwidth1 = mc.font.width(name);
                int stringwidth2 = mc.font.width(durationTxt);
                int yOffset = iconAndTime ? 11 : 16;
                alpha = alpha * 255.0F;
                int alphaRGB = (int)alpha << 24 & -16777216;
                int textColor = IndicatiaSettings.INSTANCE.alternatePotionHUDTextColor ? effect.getColor() | alphaRGB : 16777215 | alphaRGB;

                if (amplifier >= 1 && amplifier <= 9)
                {
                    name = name + ' ' + LangUtils.translateString("enchantment.level." + (amplifier + 1));
                }

                if (duration > 16)
                {
                    if (showIcon)
                    {
                        GuiComponent.blit(matrixStack, right ? xPotion + 12 : xPotion + 28, yPotion + 6, mc.gui.getBlitOffset(), 18, 18, mc.getMobEffectTextures().get(effect));
                    }
                    if (IndicatiaSettings.INSTANCE.potionHUDPosition == StatusEffects.Position.HOTBAR_LEFT)
                    {
                        int xOffset = showIcon ? 8 : 28;

                        if (!iconAndTime)
                        {
                            mc.font.drawShadow(matrixStack, name, xPotion + xOffset - stringwidth2, yPotion + 6, textColor);
                        }
                        mc.font.drawShadow(matrixStack, durationTxt, xPotion + xOffset - stringwidth1, yPotion + yOffset, textColor);
                    }
                    else if (IndicatiaSettings.INSTANCE.potionHUDPosition == StatusEffects.Position.HOTBAR_RIGHT)
                    {
                        int xOffset = showIcon ? 46 : 28;

                        if (!iconAndTime)
                        {
                            mc.font.drawShadow(matrixStack, name, xPotion + xOffset, yPotion + 6, textColor);
                        }
                        mc.font.drawShadow(matrixStack, durationTxt, xPotion + xOffset, yPotion + yOffset, textColor);
                    }
                    else
                    {
                        int leftXOffset = showIcon ? 50 : 28;
                        int rightXOffset = showIcon ? 8 : 28;

                        if (!iconAndTime)
                        {
                            mc.font.drawShadow(matrixStack, name, right ? xPotion + rightXOffset - stringwidth2 : xPotion + leftXOffset, yPotion + 6, textColor);
                        }
                        mc.font.drawShadow(matrixStack, durationTxt, right ? xPotion + rightXOffset - stringwidth1 : xPotion + leftXOffset, yPotion + yOffset, textColor);
                    }
                    yPotion -= length;
                }
            }
        }
    }
}