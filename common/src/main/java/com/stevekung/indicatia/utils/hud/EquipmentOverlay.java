package com.stevekung.indicatia.utils.hud;

import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.stevekungslib.utils.ModDecimalFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;

public class EquipmentOverlay
{
    private static final ModDecimalFormat STACK = new ModDecimalFormat("#.##");
    protected final ItemStack itemStack;
    protected final Minecraft mc;

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
        Equipments.Status status = IndicatiaSettings.INSTANCE.equipmentStatus;

        if (status == Equipments.Status.NONE || this.itemStack.isDamageableItem() && (status == Equipments.Status.AMOUNT || status == Equipments.Status.AMOUNT_AND_STACK))
        {
            return "";
        }

        int itemCount = EquipmentOverlay.getInventoryItemCount(this.mc.player.inventory, this.itemStack);

        if (this.itemStack.isDamageableItem())
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
        ItemRenderer itemRender = Minecraft.getInstance().getItemRenderer();
        itemRender.blitOffset = -200.0F;
        itemRender.renderAndDecorateItem(itemStack, x, y);
        itemRender.blitOffset = 0.0F;
    }

    private static String getArmorDurabilityStatus(ItemStack itemStack)
    {
        switch (IndicatiaSettings.INSTANCE.equipmentStatus)
        {
            case DAMAGE_AND_MAX_DAMAGE:
            default:
                return itemStack.getMaxDamage() - itemStack.getDamageValue() + "/" + itemStack.getMaxDamage();
            case PERCENT:
                return EquipmentOverlay.calculateItemDurabilityPercent(itemStack) + "%";
            case DAMAGE:
                return String.valueOf(itemStack.getMaxDamage() - itemStack.getDamageValue());
            case NONE:
            case AMOUNT:
            case AMOUNT_AND_STACK:
                return "";
        }
    }

    private static int calculateItemDurabilityPercent(ItemStack itemStack)
    {
        return itemStack.getMaxDamage() <= 0 ? 0 : 100 - itemStack.getDamageValue() * 100 / itemStack.getMaxDamage();
    }

    private static String getItemStackCount(ItemStack itemStack, int count)
    {
        Equipments.Status status = IndicatiaSettings.INSTANCE.equipmentStatus;
        double stack = count / (double)itemStack.getMaxStackSize();
        return count == 1 || itemStack.hasTag() && itemStack.getTag().getBoolean("Unbreakable") ? "" : String.valueOf(status == Equipments.Status.AMOUNT_AND_STACK ? count + "/" + EquipmentOverlay.STACK.format(stack) : count);
    }

    private static int getInventoryItemCount(Inventory inventory, ItemStack other)
    {
        int count = 0;

        for (int i = 0; i < inventory.getContainerSize(); i++)
        {
            ItemStack playerItems = inventory.getItem(i);

            if (playerItems.isEmpty())
            {
                continue;
            }
            if (playerItems.getItem() == other.getItem() && ItemStack.tagMatches(playerItems, other))
            {
                count += playerItems.getCount();
            }
        }
        return count;
    }

    private static int getInventoryArrowCount(Inventory inventory)
    {
        int arrowCount = 0;

        for (int i = 0; i < inventory.getContainerSize(); ++i)
        {
            ItemStack itemStack = inventory.getItem(i);

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