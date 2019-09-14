package stevekung.mods.indicatia.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.stevekung.stevekungslib.utils.ColorUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import stevekung.mods.indicatia.config.Equipments;
import stevekung.mods.indicatia.config.ExtendedConfig;

public class HorizontalEquipment
{
    private final Minecraft mc;
    private final ItemStack itemStack;
    private final boolean isArmor;
    private int width;
    private int itemDamageWidth;
    private String itemDamage = "";

    HorizontalEquipment(ItemStack itemStack, boolean isArmor)
    {
        this.itemStack = itemStack;
        this.isArmor = isArmor;
        this.mc = Minecraft.getInstance();
        this.initSize();
    }

    public int getWidth()
    {
        return this.width;
    }

    public void render(int x, int y)
    {
        boolean isRightSide = ExtendedConfig.INSTANCE.equipmentPosition == Equipments.Position.RIGHT;
        HUDInfo.renderItem(this.itemStack, isRightSide ? x - 18 : x, y);
        this.mc.fontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.equipmentStatusColor).toColoredFont() + this.itemDamage, isRightSide ? x - 20 - this.itemDamageWidth : x + 18, y + 4, 16777215);

        if (this.itemStack.getItem() instanceof BowItem)
        {
            int arrowCount = HUDInfo.getInventoryArrowCount(Minecraft.getInstance().player.inventory);
            GlStateManager.disableDepthTest();
            this.mc.fontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.arrowCountColor).toColoredFont() + HUDInfo.getArrowStackCount(arrowCount), isRightSide ? x - 10 : x + 8, y + 8, 16777215);
            GlStateManager.enableDepthTest();
        }
    }

    private void initSize()
    {
        String itemCount = HUDInfo.getInventoryItemCount(Minecraft.getInstance().player.inventory, this.itemStack);

        if (this.isArmor)
        {
            this.itemDamage = this.itemStack.isDamageable() ? HUDInfo.getArmorDurabilityStatus(this.itemStack) : HUDInfo.getItemStackCount(this.itemStack, Integer.parseInt(itemCount));
        }
        else
        {
            this.itemDamage = this.itemStack.isDamageable() ? HUDInfo.getArmorDurabilityStatus(this.itemStack) : ExtendedConfig.INSTANCE.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(this.itemStack, Integer.parseInt(itemCount));
        }
        this.itemDamageWidth = Minecraft.getInstance().fontRenderer.getStringWidth(this.itemDamage);
        this.width = 20 + this.itemDamageWidth;
    }
}