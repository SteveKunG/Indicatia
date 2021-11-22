package com.stevekung.indicatia.utils.hud;

import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.stevekunglib.utils.ModDecimalFormat;
import net.minecraft.client.Minecraft;
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
        var status = IndicatiaSettings.INSTANCE.equipmentStatus;

        if (status == Equipments.Status.NONE || this.itemStack.isDamageableItem() && (status == Equipments.Status.AMOUNT || status == Equipments.Status.AMOUNT_AND_STACK))
        {
            return "";
        }

        var itemCount = EquipmentOverlay.getInventoryItemCount(this.mc.player.getInventory(), this.itemStack);

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
        var arrowCount = EquipmentOverlay.getInventoryArrowCount(this.mc.player.getInventory());

        if (this.itemStack.getItem() instanceof BowItem && arrowCount > 0)
        {
            return String.valueOf(arrowCount);
        }
        return "";
    }

    @SuppressWarnings("deprecation")
    public static void renderItem(ItemStack itemStack, int x, int y)
    {
        var itemRender = Minecraft.getInstance().getItemRenderer();
        itemRender.blitOffset = -200.0F;
        itemRender.renderAndDecorateItem(itemStack, x, y);
        itemRender.blitOffset = 0.0F;
    }

    private static String getArmorDurabilityStatus(ItemStack itemStack)
    {
        return switch (IndicatiaSettings.INSTANCE.equipmentStatus)
                {
                    default -> itemStack.getMaxDamage() - itemStack.getDamageValue() + "/" + itemStack.getMaxDamage();
                    case PERCENT -> EquipmentOverlay.calculateItemDurabilityPercent(itemStack) + "%";
                    case DAMAGE -> String.valueOf(itemStack.getMaxDamage() - itemStack.getDamageValue());
                    case NONE, AMOUNT, AMOUNT_AND_STACK -> "";
                };
    }

    private static int calculateItemDurabilityPercent(ItemStack itemStack)
    {
        return itemStack.getMaxDamage() <= 0 ? 0 : 100 - itemStack.getDamageValue() * 100 / itemStack.getMaxDamage();
    }

    private static String getItemStackCount(ItemStack itemStack, int count)
    {
        var status = IndicatiaSettings.INSTANCE.equipmentStatus;
        double stack = count / (double) itemStack.getMaxStackSize();
        return count == 1 || itemStack.hasTag() && itemStack.getTag().getBoolean("Unbreakable") ? "" : String.valueOf(status == Equipments.Status.AMOUNT_AND_STACK ? count + "/" + EquipmentOverlay.STACK.format(stack) : count);
    }

    private static int getInventoryItemCount(Inventory inventory, ItemStack other)
    {
        var count = 0;

        for (var i = 0; i < inventory.getContainerSize(); i++)
        {
            var playerItems = inventory.getItem(i);

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
        var arrowCount = 0;

        for (var i = 0; i < inventory.getContainerSize(); ++i)
        {
            var itemStack = inventory.getItem(i);

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