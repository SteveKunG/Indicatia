package stevekung.mods.indicatia.renderer;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import stevekung.mods.indicatia.config.Equipments;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.stevekungslib.utils.ColorUtils;

public class HorizontalEquipment
{
    private final ItemStack itemStack;
    private final boolean isArmor;
    private int width;
    private int itemDamageWidth;
    private String itemDamage = "";

    HorizontalEquipment(ItemStack itemStack, boolean isArmor)
    {
        this.itemStack = itemStack;
        this.isArmor = isArmor;
        this.initSize();
    }

    public int getWidth()
    {
        return this.width;
    }

    public void render(int x, int y)
    {
        boolean isRightSide = Equipments.Position.getById(ExtendedConfig.equipmentPosition).equalsIgnoreCase("right");
        HUDInfo.renderItem(this.itemStack, isRightSide ? x - 18 : x, y);
        ColorUtils.coloredFontRenderer.drawWithShadow(ColorUtils.stringToRGB(ExtendedConfig.equipmentStatusColor).toColoredFont() + this.itemDamage, isRightSide ? x - 20 - this.itemDamageWidth : x + 18, y + 4, 16777215);

        if (this.itemStack.getItem() instanceof BowItem)
        {
            int arrowCount = HUDInfo.getInventoryArrowCount(MinecraftClient.getInstance().player.inventory);
            GlStateManager.disableDepthTest();
            ColorUtils.coloredFontRendererUnicode.drawWithShadow(ColorUtils.stringToRGB(ExtendedConfig.arrowCountColor).toColoredFont() + HUDInfo.getArrowStackCount(arrowCount), isRightSide ? x - 10 : x + 8, y + 8, 16777215);
            GlStateManager.enableDepthTest();
        }
    }

    private void initSize()
    {
        String itemCount = HUDInfo.getInventoryItemCount(MinecraftClient.getInstance().player.inventory, this.itemStack);

        if (this.isArmor)
        {
            this.itemDamage = this.itemStack.hasDurability() ? HUDInfo.getArmorDurabilityStatus(this.itemStack) : HUDInfo.getItemStackCount(this.itemStack, Integer.parseInt(itemCount));
        }
        else
        {
            String status = Equipments.Status.getById(ExtendedConfig.equipmentStatus);
            this.itemDamage = this.itemStack.hasDurability() ? HUDInfo.getArmorDurabilityStatus(this.itemStack) : status.equalsIgnoreCase("none") ? "" : HUDInfo.getItemStackCount(this.itemStack, Integer.parseInt(itemCount));
        }
        this.itemDamageWidth = MinecraftClient.getInstance().textRenderer.getStringWidth(this.itemDamage);
        this.width = 20 + this.itemDamageWidth;
    }
}