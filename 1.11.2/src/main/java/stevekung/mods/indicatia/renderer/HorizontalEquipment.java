package stevekung.mods.indicatia.renderer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;

public class HorizontalEquipment
{
    private final ItemStack itemStack;
    private int width;
    private String itemDamage = "";
    private int itemDamageWidth;
    private final boolean isArmor;

    public HorizontalEquipment(ItemStack itemStack, boolean isArmor)
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
        boolean isRightSide = ConfigManager.equipmentPosition.equals("right");
        HUDInfo.renderItem(this.itemStack, isRightSide ? x - 18 : x, y);
        IndicatiaMod.coloredFontRenderer.drawString(ColoredFontRenderer.color(ExtendedConfig.EQUIPMENT_COLOR_R, ExtendedConfig.EQUIPMENT_COLOR_G, ExtendedConfig.EQUIPMENT_COLOR_B) + this.itemDamage, isRightSide ? x - 20 - this.itemDamageWidth : x + 18, y + 4, 16777215, true);

        if (this.itemStack.getItem() instanceof ItemBow)
        {
            int arrowCount = HUDInfo.getInventoryArrowCount(IndicatiaMod.MC.player.inventory);
            GlStateManager.disableDepth();
            IndicatiaMod.coloredFontRenderer.setUnicodeFlag(true);
            IndicatiaMod.coloredFontRenderer.drawString(ColoredFontRenderer.color(ExtendedConfig.ARROW_COUNT_COLOR_R, ExtendedConfig.ARROW_COUNT_COLOR_G, ExtendedConfig.ARROW_COUNT_COLOR_B) + HUDInfo.getArrowStackCount(arrowCount), isRightSide ? x - 10 : x + 8, y + 8, 16777215, true);
            IndicatiaMod.coloredFontRenderer.setUnicodeFlag(false);
            GlStateManager.enableDepth();
        }
    }

    private void initSize()
    {
        String itemCount = HUDInfo.getInventoryItemCount(IndicatiaMod.MC.player.inventory, this.itemStack);

        if (this.isArmor)
        {
            this.itemDamage = this.itemStack.isItemStackDamageable() ? HUDInfo.getArmorDurabilityStatus(this.itemStack) : HUDInfo.getItemStackCount(this.itemStack, Integer.parseInt(itemCount));
        }
        else
        {
            String status = ConfigManager.equipmentStatus;
            this.itemDamage = this.itemStack.isItemStackDamageable() ? HUDInfo.getArmorDurabilityStatus(this.itemStack) : status.equals("none") ? "" : HUDInfo.getItemStackCount(this.itemStack, Integer.parseInt(itemCount));
        }
        this.itemDamageWidth = IndicatiaMod.MC.fontRendererObj.getStringWidth(this.itemDamage);
        this.width = 20 + this.itemDamageWidth;
    }
}