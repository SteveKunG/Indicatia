package com.stevekung.indicatia.utils;

import com.google.common.math.DoubleMath;
import com.mojang.blaze3d.platform.GlStateManager;
import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.ExtendedConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;

public class EquipmentOverlay
{
    protected ItemStack itemStack;
    protected Minecraft mc;

    public EquipmentOverlay(ItemStack itemStack)
    {
        this.itemStack = itemStack;
        this.mc = Minecraft.getInstance();
    }

    public ItemStack getItemStack()
    {
        return this.itemStack;
    }

    public String renderInfo()
    {
        Equipments.Status status = ExtendedConfig.INSTANCE.equipmentStatus;

        if (status == Equipments.Status.NONE || this.itemStack.isDamageable() && (status == Equipments.Status.COUNT || status == Equipments.Status.COUNT_AND_STACK))
        {
            return "";
        }

        int itemCount = EquipmentOverlay.getInventoryItemCount(this.mc.player.inventory, this.itemStack);

        if (this.itemStack.isDamageable())
        {
            return EquipmentOverlay.getArmorDurabilityStatus(this.itemStack);
        }
        else
        {
            return EquipmentOverlay.getItemStackCount(this.itemStack, itemCount);
        }
    }

    public String renderArrowInfo()
    {
        int arrowCount = EquipmentOverlay.getInventoryArrowCount(this.mc.player.inventory);

        if (this.itemStack.getItem() instanceof BowItem && arrowCount > 0)
        {
            return String.valueOf(arrowCount);
        }
        return "";
    }

    public static void renderItem(ItemStack itemStack, int x, int y)
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

    private static String getArmorDurabilityStatus(ItemStack itemStack)
    {
        switch (ExtendedConfig.INSTANCE.equipmentStatus)
        {
        case DAMAGE_AND_MAX_DAMAGE:
        default:
            return itemStack.getMaxDamage() - itemStack.getDamage() + "/" + itemStack.getMaxDamage();
        case PERCENT:
            return EquipmentOverlay.calculateItemDurabilityPercent(itemStack) + "%";
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

    private static String getItemStackCount(ItemStack itemStack, int count)
    {
        Equipments.Status status = ExtendedConfig.INSTANCE.equipmentStatus;
        double stack = count / 64.0D;
        int stackInt = count / 64;
        String stackText = String.format("%.2f", stack);

        if (DoubleMath.isMathematicalInteger(stack))
        {
            stackText = String.valueOf(stackInt);
        }
        return count == 1 && itemStack.hasTag() && itemStack.getTag().getBoolean("Unbreakable") ? "" : String.valueOf(status == Equipments.Status.COUNT_AND_STACK ? count + "/" + stackText : count);
    }

    private static int getInventoryItemCount(PlayerInventory inventory, ItemStack other)
    {
        int count = 0;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack playerItems = inventory.getStackInSlot(i);

            if (playerItems.isEmpty())
            {
                continue;
            }
            if (playerItems.getItem() == other.getItem() && ItemStack.areItemStackTagsEqual(playerItems, other))
            {
                count += playerItems.getCount();
            }
        }
        return count;
    }

    private static int getInventoryArrowCount(PlayerInventory inventory)
    {
        int arrowCount = 0;

        for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (itemStack.isEmpty())
            {
                continue;
            }
            if (itemStack.getItem() instanceof ArrowItem)
            {
                arrowCount += itemStack.getCount();
            }
        }
        return arrowCount;
    }
}