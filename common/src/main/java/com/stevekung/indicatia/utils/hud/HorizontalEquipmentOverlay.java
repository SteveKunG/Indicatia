package com.stevekung.indicatia.utils.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;

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

    public void render(PoseStack matrixStack, int x, int y)
    {
        boolean right = IndicatiaSettings.INSTANCE.equipmentPosition == Equipments.Position.RIGHT;
        MutableComponent arrowInfo = TextComponentUtils.component(this.renderArrowInfo()).copy();
        arrowInfo.setStyle(arrowInfo.getStyle().withFont(ClientUtils.UNICODE));
        EquipmentOverlay.renderItem(this.itemStack, right ? x - 18 : x, y);
        this.mc.font.drawShadow(matrixStack, this.renderInfo(), right ? x - 20 - this.itemDamageWidth : x + 18, y + 4, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.equipmentStatusColor));

        if (this.itemStack.getItem() instanceof BowItem)
        {
            RenderSystem.disableDepthTest();
            this.mc.font.drawShadow(matrixStack, arrowInfo, right ? x - this.mc.font.width(arrowInfo) : x + 6, y + 8, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.arrowCountColor));
            RenderSystem.enableDepthTest();
        }
    }

    private void initSize()
    {
        this.itemDamageWidth = this.mc.font.width(this.renderInfo());
        this.width = 20 + this.itemDamageWidth;
    }
}