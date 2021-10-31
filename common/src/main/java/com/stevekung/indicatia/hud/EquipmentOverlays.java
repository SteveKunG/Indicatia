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
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.HumanoidArm;

public class EquipmentOverlays
{
    public static void renderHorizontalEquippedItems(Minecraft mc, PoseStack poseStack)
    {
        var right = IndicatiaSettings.INSTANCE.equipmentPosition == Equipments.Position.RIGHT;
        var baseYOffset = IndicatiaSettings.INSTANCE.armorHUDYOffset;
        var mainhandStack = mc.player.getMainHandItem();
        var offhandStack = mc.player.getOffhandItem();
        var equippedLists = Lists.<HorizontalEquipmentOverlay>newArrayList();
        var prevX = 0;

        if (IndicatiaSettings.INSTANCE.equipmentArmorItems)
        {
            for (var i = 3; i >= 0; i--)
            {
                equippedLists.add(new HorizontalEquipmentOverlay(mc.player.getInventory().armor.get(i)));
            }
        }

        if (IndicatiaSettings.INSTANCE.equipmentHandItems)
        {
            equippedLists.add(new HorizontalEquipmentOverlay(mainhandStack));
            equippedLists.add(new HorizontalEquipmentOverlay(offhandStack));
        }

        for (var equipment : equippedLists)
        {
            var itemStack = equipment.getItemStack();
            var totalWidth = EquipmentOverlays.getTotalWidth(equippedLists);

            if (itemStack.isEmpty())
            {
                continue;
            }
            var xBaseRight = mc.getWindow().getGuiScaledWidth() - totalWidth - 2;
            equipment.render(poseStack, right ? xBaseRight + prevX + equipment.getWidth() : 2 + prevX, baseYOffset);
            prevX += equipment.getWidth();
        }
    }

    public static void renderVerticalEquippedItems(Minecraft mc, PoseStack poseStack)
    {
        var i = 0;
        var equippedLists = Lists.<EquipmentOverlay>newArrayList();
        var mainhandStack = mc.player.getMainHandItem();
        var offhandStack = mc.player.getOffhandItem();
        var right = IndicatiaSettings.INSTANCE.equipmentPosition == Equipments.Position.RIGHT;
        var baseXOffset = right ? mc.getWindow().getGuiScaledWidth() - 18 : 2;
        var baseYOffset = IndicatiaSettings.INSTANCE.armorHUDYOffset;

        if (IndicatiaSettings.INSTANCE.equipmentArmorItems)
        {
            for (var armorSlot = 3; armorSlot >= 0; armorSlot--)
            {
                equippedLists.add(new EquipmentOverlay(mc.player.getInventory().armor.get(armorSlot)));
            }
        }

        if (IndicatiaSettings.INSTANCE.equipmentHandItems)
        {
            equippedLists.add(new EquipmentOverlay(mainhandStack));
            equippedLists.add(new EquipmentOverlay(offhandStack));
        }

        for (var equipment : equippedLists)
        {
            var itemStack = equipment.getItemStack();

            if (itemStack.isEmpty())
            {
                continue;
            }
            var equipmentYOffset = baseYOffset + 16 * i;
            var info = equipment.renderInfo();
            var arrowInfo = TextComponentUtils.component(equipment.renderArrowInfo()).copy();
            arrowInfo.setStyle(arrowInfo.getStyle().withFont(ClientUtils.UNICODE));
            var fontHeight = (mc.font.lineHeight + 7) * i;
            var infoXOffset = right ? mc.getWindow().getGuiScaledWidth() - mc.font.width(info) - 20.0625F : baseXOffset + 18.0625F;
            var infoYOffset = baseYOffset + 4 + fontHeight;
            var arrowXOffset = right ? mc.getWindow().getGuiScaledWidth() - mc.font.width(arrowInfo) - 2.0625F : baseXOffset + 8.0625F;
            var arrowYOffset = baseYOffset + 8 + fontHeight;

            EquipmentOverlay.renderItem(itemStack, baseXOffset, equipmentYOffset);

            if (!StringUtil.isNullOrEmpty(info))
            {
                mc.font.drawShadow(poseStack, info, infoXOffset, infoYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.equipmentStatusColor));
            }
            if (!StringUtil.isNullOrEmpty(arrowInfo.getString()))
            {
                RenderSystem.disableDepthTest();
                mc.font.drawShadow(poseStack, arrowInfo, arrowXOffset, arrowYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.arrowCountColor));
                RenderSystem.enableDepthTest();
            }
            ++i;
        }
    }

    public static void renderHotbarEquippedItems(Minecraft mc, PoseStack poseStack)
    {
        var equippedLists = Lists.<HotbarEquipmentOverlay>newArrayList();
        var mainhandStack = mc.player.getMainHandItem();
        var offhandStack = mc.player.getOffhandItem();
        var iLeft = 0;
        var iRight = 0;

        if (IndicatiaSettings.INSTANCE.equipmentArmorItems)
        {
            for (var i = 2; i <= 3; i++)
            {
                equippedLists.add(new HotbarEquipmentOverlay(mc.player.getInventory().armor.get(i), HotbarEquipmentOverlay.Side.LEFT));
            }
            for (var i = 0; i <= 1; i++)
            {
                equippedLists.add(new HotbarEquipmentOverlay(mc.player.getInventory().armor.get(i), HotbarEquipmentOverlay.Side.RIGHT));
            }
        }

        if (IndicatiaSettings.INSTANCE.equipmentHandItems)
        {
            equippedLists.add(new HotbarEquipmentOverlay(mainhandStack, HotbarEquipmentOverlay.Side.LEFT));
            equippedLists.add(new HotbarEquipmentOverlay(offhandStack, mc.options.mainHand == HumanoidArm.RIGHT ? HotbarEquipmentOverlay.Side.LEFT : HotbarEquipmentOverlay.Side.RIGHT));
        }

        for (var equipment : equippedLists)
        {
            var itemStack = equipment.getItemStack();
            var info = equipment.renderInfo();
            var arrowInfo = TextComponentUtils.component(equipment.renderArrowInfo()).copy();
            arrowInfo.setStyle(arrowInfo.getStyle().withFont(ClientUtils.UNICODE));

            if (itemStack.isEmpty())
            {
                continue;
            }

            if (equipment.getSide() == HotbarEquipmentOverlay.Side.LEFT)
            {
                var baseXOffset = mc.getWindow().getGuiScaledWidth() / 2 - 111;
                var armorYOffset = mc.getWindow().getGuiScaledHeight() - 16 * iLeft - 40;
                var infoXOffset = mc.getWindow().getGuiScaledWidth() / 2F - 114 - mc.font.width(info);
                var infoYOffset = mc.getWindow().getGuiScaledHeight() - 16 * iLeft - 36;

                EquipmentOverlay.renderItem(itemStack, baseXOffset, armorYOffset);

                if (!StringUtil.isNullOrEmpty(info))
                {
                    mc.font.drawShadow(poseStack, info, infoXOffset, infoYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.equipmentStatusColor));
                }
                if (!StringUtil.isNullOrEmpty(arrowInfo.getString()))
                {
                    var arrowXOffset = mc.getWindow().getGuiScaledWidth() / 2F - 104;
                    var arrowYOffset = mc.getWindow().getGuiScaledHeight() - 16 * iLeft - 32;

                    RenderSystem.disableDepthTest();
                    mc.font.drawShadow(poseStack, arrowInfo, arrowXOffset, arrowYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.arrowCountColor));
                    RenderSystem.enableDepthTest();
                }
                ++iLeft;
            }
            else
            {
                var baseXOffset = mc.getWindow().getGuiScaledWidth() / 2 + 95;
                var armorYOffset = mc.getWindow().getGuiScaledHeight() - 16 * iRight - 40;
                var infoXOffset = mc.getWindow().getGuiScaledWidth() / 2F + 114;
                var infoYOffset = mc.getWindow().getGuiScaledHeight() - 16 * iRight - 36;

                EquipmentOverlay.renderItem(itemStack, baseXOffset, armorYOffset);

                if (!StringUtil.isNullOrEmpty(info))
                {
                    mc.font.drawShadow(poseStack, info, infoXOffset, infoYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.equipmentStatusColor));
                }
                if (!StringUtil.isNullOrEmpty(arrowInfo.getString()))
                {
                    var arrowXOffset = mc.getWindow().getGuiScaledWidth() / 2F + 112 - mc.font.width(arrowInfo);
                    var arrowYOffset = mc.getWindow().getGuiScaledHeight() - 16 * iRight - 32;

                    RenderSystem.disableDepthTest();
                    mc.font.drawShadow(poseStack, arrowInfo, arrowXOffset, arrowYOffset, ColorUtils.rgbToDecimal(IndicatiaSettings.INSTANCE.arrowCountColor));
                    RenderSystem.enableDepthTest();
                }
                ++iRight;
            }
        }
    }

    private static int getTotalWidth(List<HorizontalEquipmentOverlay> equippedLists)
    {
        var width = 0;

        for (var equipment : equippedLists)
        {
            var itemStack = equipment.getItemStack();

            if (itemStack.isEmpty())
            {
                continue;
            }
            width += equipment.getWidth();
        }
        return width;
    }
}