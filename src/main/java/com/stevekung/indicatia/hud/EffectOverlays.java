package com.stevekung.indicatia.hud;

import java.util.Collection;

import com.google.common.collect.Ordering;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.config.StatusEffects;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.texture.PotionSpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.math.MathHelper;

public class EffectOverlays
{
    public static void renderPotionHUD(Minecraft mc, MatrixStack matrixStack)
    {
        boolean iconAndTime = ExtendedConfig.INSTANCE.potionHUDStyle == StatusEffects.Style.ICON_AND_TIME;
        boolean right = ExtendedConfig.INSTANCE.potionHUDPosition == StatusEffects.Position.RIGHT;
        boolean showIcon = ExtendedConfig.INSTANCE.potionHUDIcon;
        int size = ExtendedConfig.INSTANCE.maximumPotionDisplay;
        int length = ExtendedConfig.INSTANCE.potionLengthYOffset;
        int lengthOverlap = ExtendedConfig.INSTANCE.potionLengthYOffsetOverlap;
        Collection<EffectInstance> collection = mc.player.getActivePotionEffects();
        PotionSpriteUploader uploader = mc.getPotionSpriteUploader();
        int xPotion;
        int yPotion;

        if (ExtendedConfig.INSTANCE.potionHUDPosition == StatusEffects.Position.HOTBAR_LEFT)
        {
            xPotion = mc.getMainWindow().getScaledWidth() / 2 - 91 - 35;
            yPotion = mc.getMainWindow().getScaledHeight() - 46;
        }
        else if (ExtendedConfig.INSTANCE.potionHUDPosition == StatusEffects.Position.HOTBAR_RIGHT)
        {
            xPotion = mc.getMainWindow().getScaledWidth() / 2 + 91 - 20;
            yPotion = mc.getMainWindow().getScaledHeight() - 42;
        }
        else
        {
            xPotion = right ? mc.getMainWindow().getScaledWidth() - 32 : -24;
            yPotion = mc.getMainWindow().getScaledHeight() - 220 + ExtendedConfig.INSTANCE.potionHUDYOffset + 90;
        }

        if (!collection.isEmpty())
        {
            if (collection.size() > size)
            {
                length = lengthOverlap / (collection.size() - 1);
            }

            for (EffectInstance effectIns : Ordering.natural().sortedCopy(collection))
            {
                float alpha = 1.0F;
                int duration = effectIns.getDuration();
                TextureAtlasSprite sprite = uploader.getSprite(effectIns.getPotion());

                if (!effectIns.isAmbient() && duration <= 200)
                {
                    int j1 = 10 - duration / 20;
                    alpha = MathHelper.clamp(duration / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos(duration * (float)Math.PI / 5.0F) * MathHelper.clamp(j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                }

                RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
                RenderSystem.disableLighting();
                mc.getTextureManager().bindTexture(sprite.getAtlasTexture().getTextureLocation());

                Effect effect = effectIns.getPotion();
                int amplifier = effectIns.getAmplifier();
                String name = LangUtils.translateComponent(effect.getName()).getString();
                String durationTxt = EffectUtils.getPotionDurationString(effectIns, 1.0F);
                int stringwidth1 = mc.fontRenderer.getStringWidth(name);
                int stringwidth2 = mc.fontRenderer.getStringWidth(durationTxt);
                int yOffset = iconAndTime ? 11 : 16;
                alpha = alpha * 255.0F;
                int alphaRGB = (int)alpha << 24 & -16777216;
                int textColor = ExtendedConfig.INSTANCE.alternatePotionHUDTextColor ? effect.getLiquidColor() | alphaRGB : 16777215 | alphaRGB;

                if (amplifier >= 1 && amplifier <= 9)
                {
                    name = name + ' ' + LangUtils.translateComponent("enchantment.level." + (amplifier + 1));
                }

                if (duration > 16)
                {
                    if (showIcon)
                    {
                        AbstractGui.blit(matrixStack, right ? xPotion + 12 : xPotion + 28, yPotion + 6, mc.ingameGUI.getBlitOffset(), 18, 18, mc.getPotionSpriteUploader().getSprite(effect));
                    }
                    if (ExtendedConfig.INSTANCE.potionHUDPosition == StatusEffects.Position.HOTBAR_LEFT)
                    {
                        int xOffset = showIcon ? 8 : 28;

                        if (!iconAndTime)
                        {
                            mc.fontRenderer.drawStringWithShadow(matrixStack, name, xPotion + xOffset - stringwidth2, yPotion + 6, textColor);
                        }
                        mc.fontRenderer.drawStringWithShadow(matrixStack, durationTxt, xPotion + xOffset - stringwidth1, yPotion + yOffset, textColor);
                    }
                    else if (ExtendedConfig.INSTANCE.potionHUDPosition == StatusEffects.Position.HOTBAR_RIGHT)
                    {
                        int xOffset = showIcon ? 46 : 28;

                        if (!iconAndTime)
                        {
                            mc.fontRenderer.drawStringWithShadow(matrixStack, name, xPotion + xOffset, yPotion + 6, textColor);
                        }
                        mc.fontRenderer.drawStringWithShadow(matrixStack, durationTxt, xPotion + xOffset, yPotion + yOffset, textColor);
                    }
                    else
                    {
                        int leftXOffset = showIcon ? 50 : 28;
                        int rightXOffset = showIcon ? 8 : 28;

                        if (!iconAndTime)
                        {
                            mc.fontRenderer.drawStringWithShadow(matrixStack, name, right ? xPotion + rightXOffset - stringwidth2 : xPotion + leftXOffset, yPotion + 6, textColor);
                        }
                        mc.fontRenderer.drawStringWithShadow(matrixStack, durationTxt, right ? xPotion + rightXOffset - stringwidth1 : xPotion + leftXOffset, yPotion + yOffset, textColor);
                    }
                    yPotion -= length;
                }
            }
        }
    }
}