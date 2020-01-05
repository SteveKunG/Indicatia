package com.stevekung.indicatia.hud;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;

public class EquipmentOverlays
{
    public static void renderHorizontalEquippedItems(Minecraft mc)
    {
        boolean right = ExtendedConfig.INSTANCE.equipmentPosition == Equipments.Position.RIGHT;
        int baseYOffset = ExtendedConfig.INSTANCE.armorHUDYOffset;
        ItemStack mainhandStack = mc.player.getHeldItemMainhand();
        ItemStack offhandStack = mc.player.getHeldItemOffhand();
        List<HorizontalEquipmentOverlay> equippedLists = new ArrayList<>();
        int prevX = 0;

        if (ExtendedConfig.INSTANCE.equipmentArmorItems)
        {
            for (int i = 3; i >= 0; i--)
            {
                equippedLists.add(new HorizontalEquipmentOverlay(mc.player.inventory.armorInventory.get(i)));
            }
        }

        if (ExtendedConfig.INSTANCE.equipmentHandItems)
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
            int xBaseRight = mc.func_228018_at_().getScaledWidth() - totalWidth - 2;
            equipment.render(right ? xBaseRight + prevX + equipment.getWidth() : 2 + prevX, baseYOffset);
            prevX += equipment.getWidth();
        }
    }

    public static void renderVerticalEquippedItems(Minecraft mc)
    {
        int i = 0;
        List<EquipmentOverlay> equippedLists = new ArrayList<>();
        ItemStack mainhandStack = mc.player.getHeldItemMainhand();
        ItemStack offhandStack = mc.player.getHeldItemOffhand();
        boolean right = ExtendedConfig.INSTANCE.equipmentPosition == Equipments.Position.RIGHT;
        int baseXOffset = right ? mc.func_228018_at_().getScaledWidth() - 18 : 2;
        int baseYOffset = ExtendedConfig.INSTANCE.armorHUDYOffset;

        if (ExtendedConfig.INSTANCE.equipmentArmorItems)
        {
            for (int armorSlot = 3; armorSlot >= 0; armorSlot--)
            {
                equippedLists.add(new EquipmentOverlay(mc.player.inventory.armorInventory.get(armorSlot)));
            }
        }

        if (ExtendedConfig.INSTANCE.equipmentHandItems)
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
            String arrowInfo = equipment.renderArrowInfo();
            float fontHeight = (mc.fontRenderer.FONT_HEIGHT + 7) * i;
            float infoXOffset = right ? mc.func_228018_at_().getScaledWidth() - mc.fontRenderer.getStringWidth(info) - 20.0625F : baseXOffset + 18.0625F;
            float infoYOffset = baseYOffset + 4 + fontHeight;
            float arrowXOffset = right ? mc.func_228018_at_().getScaledWidth() - ClientUtils.unicodeFontRenderer.getStringWidth(arrowInfo) - 2.0625F : baseXOffset + 8.0625F;
            float arrowYOffset = baseYOffset + 8 + fontHeight;

            EquipmentOverlay.renderItem(itemStack, baseXOffset, equipmentYOffset);

            if (!StringUtils.isNullOrEmpty(info))
            {
                mc.fontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.equipmentStatusColor).toColoredFont() + info, infoXOffset, infoYOffset, 16777215);
            }
            if (!StringUtils.isNullOrEmpty(arrowInfo))
            {
                RenderSystem.disableDepthTest();
                ClientUtils.unicodeFontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.arrowCountColor).toColoredFont() + arrowInfo, arrowXOffset, arrowYOffset, 16777215);
                RenderSystem.enableDepthTest();
            }
            ++i;
        }
    }

    public static void renderHotbarEquippedItems(Minecraft mc)
    {
        List<HotbarEquipmentOverlay> equippedLists = new ArrayList<>();
        ItemStack mainhandStack = mc.player.getHeldItemMainhand();
        ItemStack offhandStack = mc.player.getHeldItemOffhand();
        int iLeft = 0;
        int iRight = 0;

        if (ExtendedConfig.INSTANCE.equipmentArmorItems)
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

        if (ExtendedConfig.INSTANCE.equipmentHandItems)
        {
            equippedLists.add(new HotbarEquipmentOverlay(mainhandStack, HotbarEquipmentOverlay.Side.LEFT));
            equippedLists.add(new HotbarEquipmentOverlay(offhandStack, HotbarEquipmentOverlay.Side.RIGHT));
        }

        for (HotbarEquipmentOverlay equipment : equippedLists)
        {
            ItemStack itemStack = equipment.getItemStack();
            String info = equipment.renderInfo();
            String arrowInfo = equipment.renderArrowInfo();

            if (itemStack.isEmpty())
            {
                continue;
            }

            if (equipment.getSide() == HotbarEquipmentOverlay.Side.LEFT)
            {
                int baseXOffset = mc.func_228018_at_().getScaledWidth() / 2 - 111;
                int armorYOffset = mc.func_228018_at_().getScaledHeight() - 16 * iLeft - 40;
                float infoXOffset = mc.func_228018_at_().getScaledWidth() / 2 - 114 - mc.fontRenderer.getStringWidth(info);
                int infoYOffset = mc.func_228018_at_().getScaledHeight() - 16 * iLeft - 36;

                EquipmentOverlay.renderItem(itemStack, baseXOffset, armorYOffset);

                if (!StringUtils.isNullOrEmpty(info))
                {
                    mc.fontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.equipmentStatusColor).toColoredFont() + info, infoXOffset, infoYOffset, 16777215);
                }
                if (!StringUtils.isNullOrEmpty(arrowInfo))
                {
                    float arrowXOffset = mc.func_228018_at_().getScaledWidth() / 2 - 104;
                    int arrowYOffset = mc.func_228018_at_().getScaledHeight() - 16 * iLeft - 32;

                    RenderSystem.disableDepthTest();
                    ClientUtils.unicodeFontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.arrowCountColor).toColoredFont() + arrowInfo, arrowXOffset, arrowYOffset, 16777215);
                    RenderSystem.enableDepthTest();
                }
                ++iLeft;
            }
            else
            {
                int baseXOffset = mc.func_228018_at_().getScaledWidth() / 2 + 95;
                int armorYOffset = mc.func_228018_at_().getScaledHeight() - 16 * iRight - 40;
                float infoXOffset = mc.func_228018_at_().getScaledWidth() / 2 + 114;
                int infoYOffset = mc.func_228018_at_().getScaledHeight() - 16 * iRight - 36;

                EquipmentOverlay.renderItem(itemStack, baseXOffset, armorYOffset);

                if (!StringUtils.isNullOrEmpty(info))
                {
                    mc.fontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.equipmentStatusColor).toColoredFont() + info, infoXOffset, infoYOffset, 16777215);
                }
                if (!StringUtils.isNullOrEmpty(arrowInfo))
                {
                    float arrowXOffset = mc.func_228018_at_().getScaledWidth() / 2 + 112 - mc.fontRenderer.getStringWidth(arrowInfo);
                    int arrowYOffset = mc.func_228018_at_().getScaledHeight() - 16 * iRight - 32;

                    RenderSystem.disableDepthTest();
                    ClientUtils.unicodeFontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.INSTANCE.arrowCountColor).toColoredFont() + arrowInfo, arrowXOffset, arrowYOffset, 16777215);
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