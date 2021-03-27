package com.stevekung.indicatia.utils.hud;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;

import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;

public class HorizontalEquipmentOverlay extends EquipmentOverlay
{
    private int width;
    private int itemDamageWidth;

    public HorizontalEquipmentOverlay(ItemStack itemStack)
    {
        super(itemStack);
        this.initSize();
    }

    public int getWidth()
    {
        return this.width;
    }

    public void render(MatrixStack matrixStack, int x, int y)
    {
        boolean right = IndicatiaSettings.INSTANCE.equipmentPosition == Equipments.Position.RIGHT;
        IFormattableTextComponent arrowInfo = TextComponentUtils.component(this.renderArrowInfo()).deepCopy();
        arrowInfo.setStyle(arrowInfo.getStyle().setFontId(ClientUtils.UNICODE));
        EquipmentOverlay.renderItem(this.itemStack, right ? x - 18 : x, y);
        this.mc.fontRenderer.drawStringWithShadow(matrixStack, this.renderInfo(), right ? x - 20 - this.itemDamageWidth : x + 18, y + 4, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.equipmentStatusColor));

        if (this.itemStack.getItem() instanceof BowItem)
        {
            RenderSystem.disableDepthTest();
            this.mc.fontRenderer.drawTextWithShadow(matrixStack, arrowInfo, right ? x - this.mc.fontRenderer.getStringPropertyWidth(arrowInfo) : x + 6, y + 8, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.arrowCountColor));
            RenderSystem.enableDepthTest();
        }
    }

    private void initSize()
    {
        this.itemDamageWidth = this.mc.fontRenderer.getStringWidth(this.renderInfo());
        this.width = 20 + this.itemDamageWidth;
    }
}