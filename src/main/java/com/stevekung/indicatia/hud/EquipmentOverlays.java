package com.stevekung.indicatia.hud;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.utils.hud.EquipmentOverlay;
import com.stevekung.indicatia.utils.hud.HorizontalEquipmentOverlay;
import com.stevekung.indicatia.utils.hud.HotbarEquipmentOverlay;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.IFormattableTextComponent;

public class EquipmentOverlays
{
    public static void renderHorizontalEquippedItems(Minecraft mc, MatrixStack matrixStack)
    {
        boolean right = IndicatiaSettings.INSTANCE.equipmentPosition == Equipments.Position.RIGHT;
        int baseYOffset = IndicatiaSettings.INSTANCE.armorHUDYOffset;
        ItemStack mainhandStack = mc.player.getHeldItemMainhand();
        ItemStack offhandStack = mc.player.getHeldItemOffhand();
        List<HorizontalEquipmentOverlay> equippedLists = Lists.newArrayList();
        int prevX = 0;

        if (IndicatiaSettings.INSTANCE.equipmentArmorItems)
        {
            for (int i = 3; i >= 0; i--)
            {
                equippedLists.add(new HorizontalEquipmentOverlay(mc.player.inventory.armorInventory.get(i)));
            }
        }

        if (IndicatiaSettings.INSTANCE.equipmentHandItems)
        {
            equippedLists.add(new HorizontalEquipmentOverlay(mainhandStack));
            equippedLists.add(new HorizontalEquipmentOverlay(offhandStack));
        }

        for (HorizontalEquipmentOverlay equipment : equippedLists)
        {
            ItemStack itemStack = equipment.getItemStack();
            int totalWidth = EquipmentOverlays.getTotalWidth(equippedLists);

            if (itemStack.isEmpty())
            {
                continue;
            }
            int xBaseRight = mc.getMainWindow().getScaledWidth() - totalWidth - 2;
            equipment.render(matrixStack, right ? xBaseRight + prevX + equipment.getWidth() : 2 + prevX, baseYOffset);
            prevX += equipment.getWidth();
        }
    }

    public static void renderVerticalEquippedItems(Minecraft mc, MatrixStack matrixStack)
    {
        int i = 0;
        List<EquipmentOverlay> equippedLists = Lists.newArrayList();
        ItemStack mainhandStack = mc.player.getHeldItemMainhand();
        ItemStack offhandStack = mc.player.getHeldItemOffhand();
        boolean right = IndicatiaSettings.INSTANCE.equipmentPosition == Equipments.Position.RIGHT;
        int baseXOffset = right ? mc.getMainWindow().getScaledWidth() - 18 : 2;
        int baseYOffset = IndicatiaSettings.INSTANCE.armorHUDYOffset;

        if (IndicatiaSettings.INSTANCE.equipmentArmorItems)
        {
            for (int armorSlot = 3; armorSlot >= 0; armorSlot--)
            {
                equippedLists.add(new EquipmentOverlay(mc.player.inventory.armorInventory.get(armorSlot)));
            }
        }

        if (IndicatiaSettings.INSTANCE.equipmentHandItems)
        {
            equippedLists.add(new EquipmentOverlay(mainhandStack));
            equippedLists.add(new EquipmentOverlay(offhandStack));
        }

        for (EquipmentOverlay equipment : equippedLists)
        {
            ItemStack itemStack = equipment.getItemStack();

            if (itemStack.isEmpty())
            {
                continue;
            }
            int equipmentYOffset = baseYOffset + 16 * i;
            String info = equipment.renderInfo();
            IFormattableTextComponent arrowInfo = TextComponentUtils.component(equipment.renderArrowInfo()).deepCopy();
            arrowInfo.setStyle(arrowInfo.getStyle().setFontId(ClientUtils.UNICODE));
            float fontHeight = (mc.fontRenderer.FONT_HEIGHT + 7) * i;
            float infoXOffset = right ? mc.getMainWindow().getScaledWidth() - mc.fontRenderer.getStringWidth(info) - 20.0625F : baseXOffset + 18.0625F;
            float infoYOffset = baseYOffset + 4 + fontHeight;
            float arrowXOffset = right ? mc.getMainWindow().getScaledWidth() - mc.fontRenderer.getStringPropertyWidth(arrowInfo) - 2.0625F : baseXOffset + 8.0625F;
            float arrowYOffset = baseYOffset + 8 + fontHeight;

            EquipmentOverlay.renderItem(itemStack, baseXOffset, equipmentYOffset);

            if (!StringUtils.isNullOrEmpty(info))
            {
                mc.fontRenderer.drawStringWithShadow(matrixStack, info, infoXOffset, infoYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.equipmentStatusColor));
            }
            if (!StringUtils.isNullOrEmpty(arrowInfo.getString()))
            {
                RenderSystem.disableDepthTest();
                mc.fontRenderer.func_243246_a(matrixStack, arrowInfo, arrowXOffset, arrowYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.arrowCountColor));
                RenderSystem.enableDepthTest();
            }
            ++i;
        }
    }

    public static void renderHotbarEquippedItems(Minecraft mc, MatrixStack matrixStack)
    {
        List<HotbarEquipmentOverlay> equippedLists = Lists.newArrayList();
        ItemStack mainhandStack = mc.player.getHeldItemMainhand();
        ItemStack offhandStack = mc.player.getHeldItemOffhand();
        int iLeft = 0;
        int iRight = 0;

        if (IndicatiaSettings.INSTANCE.equipmentArmorItems)
        {
            for (int i = 2; i <= 3; i++)
            {
                equippedLists.add(new HotbarEquipmentOverlay(mc.player.inventory.armorInventory.get(i), HotbarEquipmentOverlay.Side.LEFT));
            }
            for (int i = 0; i <= 1; i++)
            {
                equippedLists.add(new HotbarEquipmentOverlay(mc.player.inventory.armorInventory.get(i), HotbarEquipmentOverlay.Side.RIGHT));
            }
        }

        if (IndicatiaSettings.INSTANCE.equipmentHandItems)
        {
            equippedLists.add(new HotbarEquipmentOverlay(mainhandStack, HotbarEquipmentOverlay.Side.LEFT));
            equippedLists.add(new HotbarEquipmentOverlay(offhandStack, HotbarEquipmentOverlay.Side.RIGHT));
        }

        for (HotbarEquipmentOverlay equipment : equippedLists)
        {
            ItemStack itemStack = equipment.getItemStack();
            String info = equipment.renderInfo();
            IFormattableTextComponent arrowInfo = TextComponentUtils.component(equipment.renderArrowInfo()).deepCopy();
            arrowInfo.setStyle(arrowInfo.getStyle().setFontId(ClientUtils.UNICODE));

            if (itemStack.isEmpty())
            {
                continue;
            }

            if (equipment.getSide() == HotbarEquipmentOverlay.Side.LEFT)
            {
                int baseXOffset = mc.getMainWindow().getScaledWidth() / 2 - 111;
                int armorYOffset = mc.getMainWindow().getScaledHeight() - 16 * iLeft - 40;
                float infoXOffset = mc.getMainWindow().getScaledWidth() / 2 - 114 - mc.fontRenderer.getStringWidth(info);
                int infoYOffset = mc.getMainWindow().getScaledHeight() - 16 * iLeft - 36;

                EquipmentOverlay.renderItem(itemStack, baseXOffset, armorYOffset);

                if (!StringUtils.isNullOrEmpty(info))
                {
                    mc.fontRenderer.drawStringWithShadow(matrixStack, info, infoXOffset, infoYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.equipmentStatusColor));
                }
                if (!StringUtils.isNullOrEmpty(arrowInfo.getString()))
                {
                    float arrowXOffset = mc.getMainWindow().getScaledWidth() / 2 - 104;
                    int arrowYOffset = mc.getMainWindow().getScaledHeight() - 16 * iLeft - 32;

                    RenderSystem.disableDepthTest();
                    mc.fontRenderer.func_243246_a(matrixStack, arrowInfo, arrowXOffset, arrowYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.arrowCountColor));
                    RenderSystem.enableDepthTest();
                }
                ++iLeft;
            }
            else
            {
                int baseXOffset = mc.getMainWindow().getScaledWidth() / 2 + 95;
                int armorYOffset = mc.getMainWindow().getScaledHeight() - 16 * iRight - 40;
                float infoXOffset = mc.getMainWindow().getScaledWidth() / 2 + 114;
                int infoYOffset = mc.getMainWindow().getScaledHeight() - 16 * iRight - 36;

                EquipmentOverlay.renderItem(itemStack, baseXOffset, armorYOffset);

                if (!StringUtils.isNullOrEmpty(info))
                {
                    mc.fontRenderer.drawStringWithShadow(matrixStack, info, infoXOffset, infoYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.equipmentStatusColor));
                }
                if (!StringUtils.isNullOrEmpty(arrowInfo.getString()))
                {
                    float arrowXOffset = mc.getMainWindow().getScaledWidth() / 2 + 112 - mc.fontRenderer.getStringPropertyWidth(arrowInfo);
                    int arrowYOffset = mc.getMainWindow().getScaledHeight() - 16 * iRight - 32;

                    RenderSystem.disableDepthTest();
                    mc.fontRenderer.func_243246_a(matrixStack, arrowInfo, arrowXOffset, arrowYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.arrowCountColor));
                    RenderSystem.enableDepthTest();
                }
                ++iRight;
            }
        }
    }

    private static int getTotalWidth(List<HorizontalEquipmentOverlay> equippedLists)
    {
        int width = 0;

        for (HorizontalEquipmentOverlay equipment : equippedLists)
        {
            ItemStack itemStack = equipment.getItemStack();

            if (itemStack.isEmpty())
            {
                continue;
            }
            width += equipment.getWidth();
        }
        return width;
    }
}