package com.stevekung.indicatia.hud;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.utils.hud.EquipmentOverlay;
import com.stevekung.indicatia.utils.hud.HorizontalEquipmentOverlay;
import com.stevekung.indicatia.utils.hud.HotbarEquipmentOverlay;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;

public class EquipmentOverlays
{
    public static void renderHorizontalEquippedItems(Minecraft mc, PoseStack matrixStack)
    {
        boolean right = IndicatiaSettings.INSTANCE.equipmentPosition == Equipments.Position.RIGHT;
        int baseYOffset = IndicatiaSettings.INSTANCE.armorHUDYOffset;
        ItemStack mainhandStack = mc.player.getMainHandItem();
        ItemStack offhandStack = mc.player.getOffhandItem();
        List<HorizontalEquipmentOverlay> equippedLists = Lists.newArrayList();
        int prevX = 0;

        if (IndicatiaSettings.INSTANCE.equipmentArmorItems)
        {
            for (int i = 3; i >= 0; i--)
            {
                equippedLists.add(new HorizontalEquipmentOverlay(mc.player.inventory.armor.get(i)));
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
            int xBaseRight = mc.getWindow().getGuiScaledWidth() - totalWidth - 2;
            equipment.render(matrixStack, right ? xBaseRight + prevX + equipment.getWidth() : 2 + prevX, baseYOffset);
            prevX += equipment.getWidth();
        }
    }

    public static void renderVerticalEquippedItems(Minecraft mc, PoseStack matrixStack)
    {
        int i = 0;
        List<EquipmentOverlay> equippedLists = Lists.newArrayList();
        ItemStack mainhandStack = mc.player.getMainHandItem();
        ItemStack offhandStack = mc.player.getOffhandItem();
        boolean right = IndicatiaSettings.INSTANCE.equipmentPosition == Equipments.Position.RIGHT;
        int baseXOffset = right ? mc.getWindow().getGuiScaledWidth() - 18 : 2;
        int baseYOffset = IndicatiaSettings.INSTANCE.armorHUDYOffset;

        if (IndicatiaSettings.INSTANCE.equipmentArmorItems)
        {
            for (int armorSlot = 3; armorSlot >= 0; armorSlot--)
            {
                equippedLists.add(new EquipmentOverlay(mc.player.inventory.armor.get(armorSlot)));
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
            MutableComponent arrowInfo = TextComponentUtils.component(equipment.renderArrowInfo()).copy();
            arrowInfo.setStyle(arrowInfo.getStyle().withFont(ClientUtils.UNICODE));
            float fontHeight = (mc.font.lineHeight + 7) * i;
            float infoXOffset = right ? mc.getWindow().getGuiScaledWidth() - mc.font.width(info) - 20.0625F : baseXOffset + 18.0625F;
            float infoYOffset = baseYOffset + 4 + fontHeight;
            float arrowXOffset = right ? mc.getWindow().getGuiScaledWidth() - mc.font.width(arrowInfo) - 2.0625F : baseXOffset + 8.0625F;
            float arrowYOffset = baseYOffset + 8 + fontHeight;

            EquipmentOverlay.renderItem(itemStack, baseXOffset, equipmentYOffset);

            if (!StringUtil.isNullOrEmpty(info))
            {
                mc.font.drawShadow(matrixStack, info, infoXOffset, infoYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.equipmentStatusColor));
            }
            if (!StringUtil.isNullOrEmpty(arrowInfo.getString()))
            {
                RenderSystem.disableDepthTest();
                mc.font.drawShadow(matrixStack, arrowInfo, arrowXOffset, arrowYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.arrowCountColor));
                RenderSystem.enableDepthTest();
            }
            ++i;
        }
    }

    public static void renderHotbarEquippedItems(Minecraft mc, PoseStack matrixStack)
    {
        List<HotbarEquipmentOverlay> equippedLists = Lists.newArrayList();
        ItemStack mainhandStack = mc.player.getMainHandItem();
        ItemStack offhandStack = mc.player.getOffhandItem();
        int iLeft = 0;
        int iRight = 0;

        if (IndicatiaSettings.INSTANCE.equipmentArmorItems)
        {
            for (int i = 2; i <= 3; i++)
            {
                equippedLists.add(new HotbarEquipmentOverlay(mc.player.inventory.armor.get(i), HotbarEquipmentOverlay.Side.LEFT));
            }
            for (int i = 0; i <= 1; i++)
            {
                equippedLists.add(new HotbarEquipmentOverlay(mc.player.inventory.armor.get(i), HotbarEquipmentOverlay.Side.RIGHT));
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
            MutableComponent arrowInfo = TextComponentUtils.component(equipment.renderArrowInfo()).copy();
            arrowInfo.setStyle(arrowInfo.getStyle().withFont(ClientUtils.UNICODE));

            if (itemStack.isEmpty())
            {
                continue;
            }

            if (equipment.getSide() == HotbarEquipmentOverlay.Side.LEFT)
            {
                int baseXOffset = mc.getWindow().getGuiScaledWidth() / 2 - 111;
                int armorYOffset = mc.getWindow().getGuiScaledHeight() - 16 * iLeft - 40;
                float infoXOffset = mc.getWindow().getGuiScaledWidth() / 2F - 114 - mc.font.width(info);
                int infoYOffset = mc.getWindow().getGuiScaledHeight() - 16 * iLeft - 36;

                EquipmentOverlay.renderItem(itemStack, baseXOffset, armorYOffset);

                if (!StringUtil.isNullOrEmpty(info))
                {
                    mc.font.drawShadow(matrixStack, info, infoXOffset, infoYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.equipmentStatusColor));
                }
                if (!StringUtil.isNullOrEmpty(arrowInfo.getString()))
                {
                    float arrowXOffset = mc.getWindow().getGuiScaledWidth() / 2F - 104;
                    int arrowYOffset = mc.getWindow().getGuiScaledHeight() - 16 * iLeft - 32;

                    RenderSystem.disableDepthTest();
                    mc.font.drawShadow(matrixStack, arrowInfo, arrowXOffset, arrowYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.arrowCountColor));
                    RenderSystem.enableDepthTest();
                }
                ++iLeft;
            }
            else
            {
                int baseXOffset = mc.getWindow().getGuiScaledWidth() / 2 + 95;
                int armorYOffset = mc.getWindow().getGuiScaledHeight() - 16 * iRight - 40;
                float infoXOffset = mc.getWindow().getGuiScaledWidth() / 2F + 114;
                int infoYOffset = mc.getWindow().getGuiScaledHeight() - 16 * iRight - 36;

                EquipmentOverlay.renderItem(itemStack, baseXOffset, armorYOffset);

                if (!StringUtil.isNullOrEmpty(info))
                {
                    mc.font.drawShadow(matrixStack, info, infoXOffset, infoYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.equipmentStatusColor));
                }
                if (!StringUtil.isNullOrEmpty(arrowInfo.getString()))
                {
                    float arrowXOffset = mc.getWindow().getGuiScaledWidth() / 2F + 112 - mc.font.width(arrowInfo);
                    int arrowYOffset = mc.getWindow().getGuiScaledHeight() - 16 * iRight - 32;

                    RenderSystem.disableDepthTest();
                    mc.font.drawShadow(matrixStack, arrowInfo, arrowXOffset, arrowYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.arrowCountColor));
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