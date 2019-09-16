package com.stevekung.indicatia.hud;

import java.util.Collection;

import com.google.common.collect.Ordering;
import com.mojang.blaze3d.platform.GlStateManager;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.config.StatusEffects;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.math.MathHelper;

public class HUDInfo
{
    public static void renderPotionHUD(Minecraft mc)
    {
        boolean iconAndTime = ExtendedConfig.INSTANCE.potionHUDStyle == StatusEffects.Style.ICON_AND_TIME;
        boolean right = ExtendedConfig.INSTANCE.potionHUDPosition == StatusEffects.Position.RIGHT;
        boolean showIcon = ExtendedConfig.INSTANCE.potionHUDIcon;
        int size = ExtendedConfig.INSTANCE.maximumPotionDisplay;
        int length = ExtendedConfig.INSTANCE.potionLengthYOffset;
        int lengthOverlap = ExtendedConfig.INSTANCE.potionLengthYOffsetOverlap;
        Collection<EffectInstance> collection = mc.player.getActivePotionEffects();
        int xPotion;
        int yPotion;

        if (ExtendedConfig.INSTANCE.potionHUDPosition == StatusEffects.Position.HOTBAR_LEFT)
        {
            xPotion = mc.mainWindow.getScaledWidth() / 2 - 91 - 35;
            yPotion = mc.mainWindow.getScaledHeight() - 46;
        }
        else if (ExtendedConfig.INSTANCE.potionHUDPosition == StatusEffects.Position.HOTBAR_RIGHT)
        {
            xPotion = mc.mainWindow.getScaledWidth() / 2 + 91 - 20;
            yPotion = mc.mainWindow.getScaledHeight() - 42;
        }
        else
        {
            xPotion = right ? mc.mainWindow.getScaledWidth() - 32 : -24;
            yPotion = mc.mainWindow.getScaledHeight() - 220 + ExtendedConfig.INSTANCE.potionHUDYOffset + 90;
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

                if (!effectIns.isAmbient() && duration <= 200)
                {
                    int j1 = 10 - duration / 20;
                    alpha = MathHelper.clamp(duration / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos(duration * (float)Math.PI / 5.0F) * MathHelper.clamp(j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                }

                GlStateManager.color4f(1.0F, 1.0F, 1.0F, alpha);
                GlStateManager.disableLighting();
                mc.getTextureManager().bindTexture(AtlasTexture.LOCATION_EFFECTS_TEXTURE);

                Effect effect = effectIns.getPotion();
                int amplifier = effectIns.getAmplifier();
                String name = LangUtils.translate(effect.getName());
                String durationTxt = EffectUtils.getPotionDurationString(effectIns, 1.0F);
                int stringwidth1 = mc.fontRenderer.getStringWidth(name);
                int stringwidth2 = mc.fontRenderer.getStringWidth(durationTxt);
                int yOffset = iconAndTime ? 11 : 16;
                alpha = alpha * 255.0F;
                int alphaRGB = (int)alpha << 24 & -16777216;
                int textColor = ExtendedConfig.INSTANCE.alternatePotionHUDTextColor ? effect.getLiquidColor() | alphaRGB : 16777215 | alphaRGB;

                if (amplifier >= 1 && amplifier <= 9)
                {
                    name = name + ' ' + LangUtils.translate("enchantment.level." + (amplifier + 1));
                }

                if (duration > 16)
                {
                    effect.renderInventoryEffect(effectIns, null, xPotion, yPotion, mc.ingameGUI.blitOffset);

                    if (showIcon)
                    {
                        AbstractGui.blit(right ? xPotion + 12 : xPotion + 28, yPotion + 6, mc.ingameGUI.blitOffset, 18, 18, mc.getPotionSpriteUploader().getSprite(effect));
                    }
                    if (ExtendedConfig.INSTANCE.potionHUDPosition == StatusEffects.Position.HOTBAR_LEFT)
                    {
                        int xOffset = showIcon ? 8 : 28;

                        if (!iconAndTime)
                        {
                            mc.fontRenderer.drawStringWithShadow(name, xPotion + xOffset - stringwidth2, yPotion + 6, textColor);
                        }
                        mc.fontRenderer.drawStringWithShadow(durationTxt, xPotion + xOffset - stringwidth1, yPotion + yOffset, textColor);
                    }
                    else if (ExtendedConfig.INSTANCE.potionHUDPosition == StatusEffects.Position.HOTBAR_RIGHT)
                    {
                        int xOffset = showIcon ? 46 : 28;

                        if (!iconAndTime)
                        {
                            mc.fontRenderer.drawStringWithShadow(name, xPotion + xOffset, yPotion + 6, textColor);
                        }
                        mc.fontRenderer.drawStringWithShadow(durationTxt, xPotion + xOffset, yPotion + yOffset, textColor);
                    }
                    else
                    {
                        int leftXOffset = showIcon ? 50 : 28;
                        int rightXOffset = showIcon ? 8 : 28;

                        if (!iconAndTime)
                        {
                            mc.fontRenderer.drawStringWithShadow(name, right ? xPotion + rightXOffset - stringwidth2 : xPotion + leftXOffset, yPotion + 6, textColor);
                        }
                        mc.fontRenderer.drawStringWithShadow(durationTxt, right ? xPotion + rightXOffset - stringwidth1 : xPotion + leftXOffset, yPotion + yOffset, textColor);
                    }
                    yPotion -= length;
                }
            }
        }
    }
}