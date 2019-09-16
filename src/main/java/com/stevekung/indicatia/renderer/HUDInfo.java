package com.stevekung.indicatia.renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Ordering;
import com.google.common.math.DoubleMath;
import com.mojang.blaze3d.platform.GlStateManager;
import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.config.StatusEffects;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.math.MathHelper;

public class HUDInfo
{
    public static void renderHorizontalEquippedItems(Minecraft mc)
    {
        boolean isRightSide = ExtendedConfig.INSTANCE.equipmentPosition == Equipments.Position.RIGHT;
        int baseXOffset = 2;
        int baseYOffset = ExtendedConfig.INSTANCE.armorHUDYOffset;
        ItemStack mainHandItem = mc.player.getHeldItemMainhand();
        ItemStack offHandItem = mc.player.getHeldItemOffhand();
        List<HorizontalEquipment> element = new ArrayList<>();
        int prevX = 0;
        int rightWidth = 0;
        element.clear();

        // armor/held item stuff
        switch (ExtendedConfig.INSTANCE.equipmentOrdering)
        {
        case DEFAULT:
            if (!mainHandItem.isEmpty())
            {
                element.add(new HorizontalEquipment(mainHandItem, false));
            }
            if (!offHandItem.isEmpty())
            {
                element.add(new HorizontalEquipment(offHandItem, false));
            }
            for (int i = 3; i >= 0; i--)
            {
                if (!mc.player.inventory.armorInventory.get(i).isEmpty())
                {
                    element.add(new HorizontalEquipment(mc.player.inventory.armorInventory.get(i), mc.player.inventory.armorInventory.get(i).isDamageable()));
                }
            }
            break;
        case REVERSE:
            for (int i = 0; i <= 3; i++)
            {
                if (!mc.player.inventory.armorInventory.get(i).isEmpty())
                {
                    element.add(new HorizontalEquipment(mc.player.inventory.armorInventory.get(i), mc.player.inventory.armorInventory.get(i).isDamageable()));
                }
            }
            if (!mainHandItem.isEmpty())
            {
                element.add(new HorizontalEquipment(mainHandItem, false));
            }
            if (!offHandItem.isEmpty())
            {
                element.add(new HorizontalEquipment(offHandItem, false));
            }
            break;
        }

        for (HorizontalEquipment equipment : element)
        {
            rightWidth += equipment.getWidth();
        }
        for (HorizontalEquipment equipment : element)
        {
            int xBaseRight = mc.mainWindow.getScaledWidth() - rightWidth - baseXOffset;
            equipment.render(isRightSide ? xBaseRight + prevX + equipment.getWidth() : baseXOffset + prevX, baseYOffset);
            prevX += equipment.getWidth();
        }
    }

    public static void renderVerticalEquippedItems(Minecraft mc)
    {
        boolean isRightSide = ExtendedConfig.INSTANCE.equipmentPosition == Equipments.Position.RIGHT;
        List<ItemStack> itemStackList = new ArrayList<>();
        List<String> itemStatusList = new ArrayList<>();
        List<String> arrowCountList = new ArrayList<>();
        int baseXOffset = isRightSide ? mc.mainWindow.getScaledWidth() - 18 : 2;
        int baseYOffset = ExtendedConfig.INSTANCE.armorHUDYOffset;
        ItemStack mainHandItem = mc.player.getHeldItemMainhand();
        ItemStack offHandItem = mc.player.getHeldItemOffhand();
        int arrowCount = HUDInfo.getInventoryArrowCount(mc.player.inventory);

        // held item stuff
        if (ExtendedConfig.INSTANCE.equipmentOrdering == Equipments.Ordering.REVERSE)
        {
            if (!mainHandItem.isEmpty())
            {
                itemStackList.add(mainHandItem);
                String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mainHandItem);
                itemStatusList.add(mainHandItem.isDamageable() ? HUDInfo.getArmorDurabilityStatus(mainHandItem) : ExtendedConfig.INSTANCE.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(mainHandItem, Integer.parseInt(itemCount)));

                if (mainHandItem.getItem() == Items.BOW)
                {
                    arrowCountList.add(HUDInfo.getArrowStackCount(arrowCount));
                }
                else
                {
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
            if (!offHandItem.isEmpty())
            {
                itemStackList.add(offHandItem);
                String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, offHandItem);
                itemStatusList.add(offHandItem.isDamageable() ? HUDInfo.getArmorDurabilityStatus(offHandItem) : ExtendedConfig.INSTANCE.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(offHandItem, Integer.parseInt(itemCount)));

                if (offHandItem.getItem() == Items.BOW)
                {
                    arrowCountList.add(HUDInfo.getArrowStackCount(arrowCount));
                }
                else
                {
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
        }

        // armor stuff
        switch (ExtendedConfig.INSTANCE.equipmentOrdering)
        {
        case DEFAULT:
            for (int i = 3; i >= 0; i--)
            {
                if (!mc.player.inventory.armorInventory.get(i).isEmpty())
                {
                    String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mc.player.inventory.armorInventory.get(i));
                    itemStackList.add(mc.player.inventory.armorInventory.get(i));
                    itemStatusList.add(mc.player.inventory.armorInventory.get(i).isDamageable() ? HUDInfo.getArmorDurabilityStatus(mc.player.inventory.armorInventory.get(i)) : HUDInfo.getItemStackCount(mc.player.inventory.armorInventory.get(i), Integer.parseInt(itemCount)));
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
            break;
        case REVERSE:
            for (int i = 0; i <= 3; i++)
            {
                if (!mc.player.inventory.armorInventory.get(i).isEmpty())
                {
                    String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mc.player.inventory.armorInventory.get(i));
                    itemStackList.add(mc.player.inventory.armorInventory.get(i));
                    itemStatusList.add(mc.player.inventory.armorInventory.get(i).isDamageable() ? HUDInfo.getArmorDurabilityStatus(mc.player.inventory.armorInventory.get(i)) : HUDInfo.getItemStackCount(mc.player.inventory.armorInventory.get(i), Integer.parseInt(itemCount)));
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
            break;
        }

        // held item stuff
        if (ExtendedConfig.INSTANCE.equipmentOrdering == Equipments.Ordering.DEFAULT)
        {
            if (!mainHandItem.isEmpty())
            {
                itemStackList.add(mainHandItem);
                String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mainHandItem);
                itemStatusList.add(mainHandItem.isDamageable() ? HUDInfo.getArmorDurabilityStatus(mainHandItem) : ExtendedConfig.INSTANCE.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(mainHandItem, Integer.parseInt(itemCount)));

                if (mainHandItem.getItem() == Items.BOW)
                {
                    arrowCountList.add(HUDInfo.getArrowStackCount(arrowCount));
                }
                else
                {
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
            if (!offHandItem.isEmpty())
            {
                itemStackList.add(offHandItem);
                String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, offHandItem);
                itemStatusList.add(offHandItem.isDamageable() ? HUDInfo.getArmorDurabilityStatus(offHandItem) : ExtendedConfig.INSTANCE.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(offHandItem, Integer.parseInt(itemCount)));

                if (offHandItem.getItem() == Items.BOW)
                {
                    arrowCountList.add(HUDInfo.getArrowStackCount(arrowCount));
                }
                else
                {
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
        }

        // item render stuff
        for (int i = 0; i < itemStackList.size(); ++i)
        {
            ItemStack itemStack = itemStackList.get(i);

            if (!itemStackList.isEmpty())
            {
                int yOffset = baseYOffset + 16 * i;
                HUDInfo.renderItem(itemStack, baseXOffset, yOffset);
                yOffset += 16;
            }
        }

        float yOffset = 0;
        float fontHeight = 0;

        // durability/item count stuff
        for (int i = 0; i < itemStatusList.size(); ++i)
        {
            String string = itemStatusList.get(i);
            fontHeight = mc.fontRenderer.FONT_HEIGHT + 7.0625F;

            if (!string.isEmpty())
            {
                yOffset = baseYOffset + 4 + fontHeight * i;
                float xOffset = isRightSide ? mc.mainWindow.getScaledWidth() - mc.fontRenderer.getStringWidth(string) - 20.0625F : baseXOffset + 18.0625F;
                mc.fontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.equipmentStatusColor).toColoredFont() + string, xOffset, yOffset, 16777215);
            }
        }

        // arrow count stuff
        for (int i = 0; i < arrowCountList.size(); ++i)
        {
            String string = arrowCountList.get(i);
            yOffset = baseYOffset + 8 + fontHeight * i;

            if (!string.isEmpty())
            {
                GlStateManager.disableDepthTest();
                mc.fontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.arrowCountColor).toColoredFont() + string, isRightSide ? mc.mainWindow.getScaledWidth() - mc.fontRenderer.getStringWidth(string) - 2.0625F : baseXOffset + 8.0625F, yOffset, 16777215);
                GlStateManager.enableDepthTest();
            }
        }
    }

    public static void renderHotbarEquippedItems(Minecraft mc)
    {
        List<ItemStack> leftItemStackList = new ArrayList<>();
        List<String> leftItemStatusList = new ArrayList<>();
        List<String> leftArrowCountList = new ArrayList<>();
        List<ItemStack> rightItemStackList = new ArrayList<>();
        List<String> rightItemStatusList = new ArrayList<>();
        List<String> rightArrowCountList = new ArrayList<>();
        ItemStack mainHandItem = mc.player.getHeldItemMainhand();
        ItemStack offHandItem = mc.player.getHeldItemOffhand();
        int arrowCount = HUDInfo.getInventoryArrowCount(mc.player.inventory);

        for (int i = 2; i <= 3; i++)
        {
            if (!mc.player.inventory.armorInventory.get(i).isEmpty())
            {
                String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mc.player.inventory.armorInventory.get(i));
                leftItemStackList.add(mc.player.inventory.armorInventory.get(i));
                leftItemStatusList.add(mc.player.inventory.armorInventory.get(i).isDamageable() ? HUDInfo.getArmorDurabilityStatus(mc.player.inventory.armorInventory.get(i)) : HUDInfo.getItemStackCount(mc.player.inventory.armorInventory.get(i), Integer.parseInt(itemCount)));
                leftArrowCountList.add(""); // dummy bow arrow count list size
            }
        }

        for (int i = 0; i <= 1; i++)
        {
            if (!mc.player.inventory.armorInventory.get(i).isEmpty())
            {
                String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mc.player.inventory.armorInventory.get(i));
                rightItemStackList.add(mc.player.inventory.armorInventory.get(i));
                rightItemStatusList.add(mc.player.inventory.armorInventory.get(i).isDamageable() ? HUDInfo.getArmorDurabilityStatus(mc.player.inventory.armorInventory.get(i)) : HUDInfo.getItemStackCount(mc.player.inventory.armorInventory.get(i), Integer.parseInt(itemCount)));
                rightArrowCountList.add(""); // dummy bow arrow count list size
            }
        }

        if (!mainHandItem.isEmpty())
        {
            leftItemStackList.add(mainHandItem);
            String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mainHandItem);
            leftItemStatusList.add(!(ExtendedConfig.INSTANCE.equipmentStatus == Equipments.Status.COUNT || ExtendedConfig.INSTANCE.equipmentStatus == Equipments.Status.COUNT_AND_STACK) && mainHandItem.isDamageable() ? HUDInfo.getArmorDurabilityStatus(mainHandItem) : ExtendedConfig.INSTANCE.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(mainHandItem, Integer.parseInt(itemCount)));

            if (mainHandItem.getItem() == Items.BOW)
            {
                leftArrowCountList.add(HUDInfo.getArrowStackCount(arrowCount));
            }
            else
            {
                leftArrowCountList.add(""); // dummy bow arrow count list size
            }
        }
        if (!offHandItem.isEmpty())
        {
            rightItemStackList.add(offHandItem);
            String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, offHandItem);
            rightItemStatusList.add(offHandItem.isDamageable() ? HUDInfo.getArmorDurabilityStatus(offHandItem) : ExtendedConfig.INSTANCE.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(offHandItem, Integer.parseInt(itemCount)));

            if (offHandItem.getItem() == Items.BOW)
            {
                rightArrowCountList.add(HUDInfo.getArrowStackCount(arrowCount));
            }
            else
            {
                rightArrowCountList.add(""); // dummy bow arrow count list size
            }
        }

        // left item render stuff
        for (int i = 0; i < leftItemStackList.size(); ++i)
        {
            ItemStack itemStack = leftItemStackList.get(i);

            if (!leftItemStackList.isEmpty())
            {
                int baseXOffset = mc.mainWindow.getScaledWidth() / 2 - 91 - 20;
                int yOffset = mc.mainWindow.getScaledHeight() - 16 * i - 40;
                HUDInfo.renderItem(itemStack, baseXOffset, yOffset);
            }
        }

        // right item render stuff
        for (int i = 0; i < rightItemStackList.size(); ++i)
        {
            ItemStack itemStack = rightItemStackList.get(i);

            if (!rightItemStackList.isEmpty())
            {
                int baseXOffset = mc.mainWindow.getScaledWidth() / 2 + 95;
                int yOffset = mc.mainWindow.getScaledHeight() - 16 * i - 40;
                HUDInfo.renderItem(itemStack, baseXOffset, yOffset);
            }
        }

        // left durability/item count stuff
        for (int i = 0; i < leftItemStatusList.size(); ++i)
        {
            String string = leftItemStatusList.get(i);
            int stringWidth = mc.fontRenderer.getStringWidth(string);
            float xOffset = mc.mainWindow.getScaledWidth() / 2 - 114 - stringWidth;
            int yOffset = mc.mainWindow.getScaledHeight() - 16 * i - 36;
            mc.fontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.equipmentStatusColor).toColoredFont() + string, xOffset, yOffset, 16777215);
        }

        // right durability/item count stuff
        for (int i = 0; i < rightItemStatusList.size(); ++i)
        {
            String string = rightItemStatusList.get(i);
            float xOffset = mc.mainWindow.getScaledWidth() / 2 + 114;
            int yOffset = mc.mainWindow.getScaledHeight() - 16 * i - 36;
            mc.fontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.equipmentStatusColor).toColoredFont() + string, xOffset, yOffset, 16777215);
        }

        // left arrow count stuff
        for (int i = 0; i < leftArrowCountList.size(); ++i)
        {
            String string = leftArrowCountList.get(i);
            int stringWidth = mc.fontRenderer.getStringWidth(string);
            float xOffset = mc.mainWindow.getScaledWidth() / 2 - 90 - stringWidth;
            int yOffset = mc.mainWindow.getScaledHeight() - 16 * i - 32;

            if (!string.isEmpty())
            {
                GlStateManager.disableDepthTest();
                mc.fontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.arrowCountColor).toColoredFont() + string, xOffset, yOffset, 16777215);
                GlStateManager.enableDepthTest();
            }
        }

        // right arrow count stuff
        for (int i = 0; i < rightArrowCountList.size(); ++i)
        {
            String string = rightArrowCountList.get(i);
            float xOffset = mc.mainWindow.getScaledWidth() / 2 + 104;
            int yOffset = mc.mainWindow.getScaledHeight() - 16 * i - 32;

            if (!string.isEmpty())
            {
                GlStateManager.disableDepthTest();
                mc.fontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.arrowCountColor).toColoredFont() + string, xOffset, yOffset, 16777215);
                GlStateManager.enableDepthTest();
            }
        }
    }

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

    static String getArmorDurabilityStatus(ItemStack itemStack)
    {
        switch (ExtendedConfig.INSTANCE.equipmentStatus)
        {
        case DAMAGE_AND_MAX_DAMAGE:
        default:
            return itemStack.getMaxDamage() - itemStack.getDamage() + "/" + itemStack.getMaxDamage();
        case PERCENT:
            return HUDInfo.calculateItemDurabilityPercent(itemStack) + "%";
        case ONLY_DAMAGE:
            return String.valueOf(itemStack.getMaxDamage() - itemStack.getDamage());
        case NONE:
        case COUNT:
        case COUNT_AND_STACK:
            return "";
        }
    }

    private static int calculateItemDurabilityPercent(ItemStack itemStack)
    {
        return itemStack.getMaxDamage() <= 0 ? 0 : 100 - itemStack.getDamage() * 100 / itemStack.getMaxDamage();
    }

    public static String getResponseTimeColor(int responseTime)
    {
        if (responseTime >= 200 && responseTime < 300)
        {
            return ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.ping200And300Color).toColoredFont();
        }
        else if (responseTime >= 300 && responseTime < 500)
        {
            return ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.ping300And500Color).toColoredFont();
        }
        else if (responseTime >= 500)
        {
            return ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.pingMax500Color).toColoredFont();
        }
        else
        {
            return ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.pingValueColor).toColoredFont();
        }
    }

    static void renderItem(ItemStack itemStack, int x, int y)
    {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(itemStack, x, y);
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        RenderHelper.disableStandardItemLighting();

        if (itemStack.isDamageable())
        {
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.disableLighting();
            GlStateManager.enableCull();
            Minecraft.getInstance().getItemRenderer().renderItemOverlays(Minecraft.getInstance().fontRenderer, itemStack, x, y);
            GlStateManager.blendFunc(770, 771);
            GlStateManager.disableLighting();
        }
    }

    static String getInventoryItemCount(PlayerInventory inventory, ItemStack other)
    {
        Equipments.Status status = ExtendedConfig.INSTANCE.equipmentStatus;
        int count = 0;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack playerItems = inventory.getStackInSlot(i);

            if (playerItems.isEmpty())
            {
                continue;
            }
            if (other.isDamageable() && (status == Equipments.Status.COUNT || status == Equipments.Status.COUNT_AND_STACK))
            {
                break;
            }
            if (playerItems.getItem() == other.getItem() && playerItems.getDamage() == other.getDamage() && ItemStack.areItemStackTagsEqual(playerItems, other))
            {
                count += playerItems.getCount();
            }
        }
        return String.valueOf(count);
    }

    static int getInventoryArrowCount(PlayerInventory inventory)
    {
        int arrowCount = 0;

        for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (!itemStack.isEmpty() && itemStack.getItem() instanceof ArrowItem)
            {
                arrowCount += itemStack.getCount();
            }
        }
        return arrowCount;
    }

    static String getItemStackCount(ItemStack itemStack, int count)
    {
        Equipments.Status status = ExtendedConfig.INSTANCE.equipmentStatus;
        double stack = count / 64.0D;
        int stackInt = count / 64;
        String stackText = String.format("%.2f", stack);

        if (DoubleMath.isMathematicalInteger(stack))
        {
            stackText = String.valueOf(stackInt);
        }
        return count == 0 || count == 1 || count == 1 && itemStack.hasTag() && itemStack.getTag().getBoolean("Unbreakable") ? "" : String.valueOf(status == Equipments.Status.COUNT_AND_STACK ? count + "/" + stackText : count);
    }

    static String getArrowStackCount(int count)
    {
        return count == 0 ? "" : String.valueOf(count);
    }
}