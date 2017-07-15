package stevekung.mods.indicatia.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.Chunk;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.renderer.SmallFontRenderer;

public class HUDInfo
{
    public static String getFPS()
    {
        return "FPS: " + InfoUtil.INSTANCE.getTextColor(ConfigManager.customColorFPS) + Minecraft.getDebugFPS();
    }

    public static String getXYZ(Minecraft mc)
    {
        BlockPos pos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY, mc.getRenderViewEntity().posZ);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        String nether = mc.thePlayer.dimension == -1 ? "Nether " : "";
        return nether + "XYZ: " + InfoUtil.INSTANCE.getTextColor(ConfigManager.customColorXYZ) + x + " " + y + " " + z;
    }

    public static String getOverworldXYZFromNether(Minecraft mc)
    {
        BlockPos pos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY, mc.getRenderViewEntity().posZ);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return "Overworld XYZ: " + InfoUtil.INSTANCE.getTextColor(ConfigManager.customColorXYZ) + x * 8 + " " + y + " " + z * 8;
    }

    public static String getBiome(Minecraft mc)
    {
        BlockPos pos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY, mc.getRenderViewEntity().posZ);
        Chunk chunk = mc.theWorld.getChunkFromBlockCoords(pos);

        if (mc.theWorld.isBlockLoaded(pos) && pos.getY() >= 0 && pos.getY() < 256)
        {
            if (!chunk.isEmpty())
            {
                String biomeName = chunk.getBiome(pos, mc.theWorld.getBiomeProvider()).getBiomeName().replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
                return "Biome: " + InfoUtil.INSTANCE.getTextColor(ConfigManager.customColorBiome) + biomeName;
            }
            else
            {
                return "Waiting for chunk...";
            }
        }
        else
        {
            return "Outside of world...";
        }
    }

    public static String getPing()
    {
        int responseTime = InfoUtil.INSTANCE.getPing();
        return "Ping: " + HUDInfo.getResponseTimeColor(responseTime) + responseTime + "ms";
    }

    public static String getServerIP(Minecraft mc)
    {
        String ip = "IP: " + InfoUtil.INSTANCE.getTextColor(ConfigManager.customColorServerIP) + mc.getCurrentServerData().serverIP;

        if (ConfigManager.enableServerIPMCVersion)
        {
            ip = ip + "/" + IndicatiaMod.MC_VERSION;
        }
        return ip;
    }

    public static String getCPS(Minecraft mc)
    {
        return "CPS: " + InfoUtil.INSTANCE.getTextColor(ConfigManager.customColorCPS) + InfoUtil.INSTANCE.getCPS();
    }

    public static String getRCPS(Minecraft mc)
    {
        return "RCPS: " + InfoUtil.INSTANCE.getTextColor(ConfigManager.customColorRCPS) + InfoUtil.INSTANCE.getRCPS();
    }

    public static void renderEquippedItems(Minecraft mc)
    {
        String ordering = ConfigManager.equipmentOrdering;
        String direction = ConfigManager.equipmentDirection;
        String status = ConfigManager.equipmentStatus;
        List<ItemStack> itemStackList = new ArrayList<>();
        List<String> itemStatusList = new ArrayList<>();
        List<String> arrowCountList = new ArrayList<>();
        int baseXOffset = 2;
        int baseYOffset = ExtendedConfig.ARMOR_STATUS_OFFSET;
        ItemStack mainHandItem = mc.thePlayer.getHeldItemMainhand();
        ItemStack offHandItem = mc.thePlayer.getHeldItemOffhand();
        int arrowCount = HUDInfo.getInventoryArrowCount(mc.thePlayer.inventory);
        SmallFontRenderer smallFontRenderer = new SmallFontRenderer();

        // held item stuff
        if (ordering.equals("reverse"))
        {
            if (mainHandItem != null)
            {
                itemStackList.add(mainHandItem);
                String itemCount = HUDInfo.getInventoryItemCount(mc.thePlayer.inventory, mainHandItem);
                itemStatusList.add(mainHandItem.isItemStackDamageable() ? HUDInfo.getArmorDurabilityStatus(mainHandItem) : status.equals("none") ? "" : HUDInfo.getItemStackCount(mainHandItem, Integer.parseInt(itemCount)));

                if (mainHandItem.getItem() == Items.BOW)
                {
                    arrowCountList.add(HUDInfo.getArrowStackCount(arrowCount));
                }
                else
                {
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
            if (offHandItem != null)
            {
                itemStackList.add(offHandItem);
                String itemCount = HUDInfo.getInventoryItemCount(mc.thePlayer.inventory, offHandItem);
                itemStatusList.add(offHandItem.isItemStackDamageable() ? HUDInfo.getArmorDurabilityStatus(offHandItem) : status.equals("none") ? "" : HUDInfo.getItemStackCount(offHandItem, Integer.parseInt(itemCount)));

                if (offHandItem.getItem() == Items.BOW)
                {
                    arrowCountList.add(HUDInfo.getArrowStackCount(arrowCount));
                }
                else
                {
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
        }

        // armor stuff
        switch (ordering)
        {
        case "default":
            for (int i = 3; i >= 0; i--)
            {
                if (mc.thePlayer.inventory.armorInventory[i] != null)
                {
                    itemStackList.add(mc.thePlayer.inventory.armorInventory[i]);
                    itemStatusList.add(HUDInfo.getArmorDurabilityStatus(mc.thePlayer.inventory.armorInventory[i]));
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
            break;
        case "reverse":
            for (int i = 0; i <= 3; i++)
            {
                if (mc.thePlayer.inventory.armorInventory[i] != null)
                {
                    itemStackList.add(mc.thePlayer.inventory.armorInventory[i]);
                    itemStatusList.add(HUDInfo.getArmorDurabilityStatus(mc.thePlayer.inventory.armorInventory[i]));
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
            break;
        }

        // held item stuff
        if (ordering.equals("default"))
        {
            if (mainHandItem != null)
            {
                itemStackList.add(mainHandItem);
                String itemCount = HUDInfo.getInventoryItemCount(mc.thePlayer.inventory, mainHandItem);
                itemStatusList.add(mainHandItem.isItemStackDamageable() ? HUDInfo.getArmorDurabilityStatus(mainHandItem) : status.equals("none") ? "" : HUDInfo.getItemStackCount(mainHandItem, Integer.parseInt(itemCount)));

                if (mainHandItem.getItem() == Items.BOW)
                {
                    arrowCountList.add(HUDInfo.getArrowStackCount(arrowCount));
                }
                else
                {
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
            if (offHandItem != null)
            {
                itemStackList.add(offHandItem);
                String itemCount = HUDInfo.getInventoryItemCount(mc.thePlayer.inventory, offHandItem);
                itemStatusList.add(offHandItem.isItemStackDamageable() ? HUDInfo.getArmorDurabilityStatus(offHandItem) : status.equals("none") ? "" : HUDInfo.getItemStackCount(offHandItem, Integer.parseInt(itemCount)));

                if (offHandItem.getItem() == Items.BOW)
                {
                    arrowCountList.add(HUDInfo.getArrowStackCount(arrowCount));
                }
                else
                {
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
        }

        // item render stuff
        for (int i = 0; i < itemStackList.size(); ++i)
        {
            ItemStack itemStack = itemStackList.get(i);
            mc.mcProfiler.startSection("item_stack_render");

            if (!itemStackList.isEmpty())
            {
                switch (direction)
                {
                case "vertical":
                    int yOffset = baseYOffset + 16 * i;
                    HUDInfo.renderItem(itemStack, baseXOffset, yOffset);
                    yOffset += 16;
                    break;
                case "horizontal":
                    int xLength = status.equals("percent") ? 42 : status.equals("damage") ? 38 : status.equals("none") ? 16 : 61;
                    int xOffset = baseXOffset + xLength * i;
                    HUDInfo.renderItem(itemStack, xOffset, baseYOffset);
                    xOffset += 16;
                    break;
                }
            }
            mc.mcProfiler.endSection();
        }

        float yOffset = 0;
        float fontHeight = 0;

        // durability/item count stuff
        for (int i = 0; i < itemStatusList.size(); ++i)
        {
            String string = itemStatusList.get(i);
            fontHeight = mc.fontRendererObj.FONT_HEIGHT + 7.0625F;

            switch (direction)
            {
            case "vertical":
                if (!string.isEmpty())
                {
                    yOffset = baseYOffset + 4 + fontHeight * i;
                    mc.mcProfiler.startSection("armor_durability_info");
                    mc.fontRendererObj.drawString(string, baseXOffset + 18.0625F, yOffset, 16777215, true);
                    mc.mcProfiler.endSection();
                }
                break;
            case "horizontal":
                if (!string.isEmpty())
                {
                    fontHeight = status.equals("percent") ? 43 : status.equals("damage") ? 38 : status.equals("none") ? 16 : 61;
                    float xOffset = baseXOffset + 16 + fontHeight * i;
                    mc.mcProfiler.startSection("armor_durability_info");
                    mc.fontRendererObj.drawString(string, xOffset, baseYOffset + 4, 16777215, true);
                    mc.mcProfiler.endSection();
                }
                break;
            }
        }

        // arrow count stuff
        for (int i = 0; i < arrowCountList.size(); ++i)
        {
            String string = arrowCountList.get(i);
            yOffset = baseYOffset + 8 + fontHeight * i;

            if (!string.isEmpty() && direction.equals("vertical"))
            {
                mc.mcProfiler.startSection("arrow_count");
                GlStateManager.disableDepth();
                smallFontRenderer.drawString(string, baseXOffset + 8.0625F, yOffset, 16777215, true);
                GlStateManager.enableDepth();
                mc.mcProfiler.endSection();
            }
        }
    }

    private static String getArmorDurabilityStatus(ItemStack itemStack)
    {
        String status = ConfigManager.equipmentStatus;

        switch (status)
        {
        case "damage/max_damage":
        default:
            return itemStack.getMaxDamage() - itemStack.getItemDamage() + "/" + itemStack.getMaxDamage();
        case "percent":
            return HUDInfo.calculateItemDurabilityPercent(itemStack) + "%";
        case "damage":
            return String.valueOf(itemStack.getMaxDamage() - itemStack.getItemDamage());
        case "none":
            return "";
        }
    }

    private static int calculateItemDurabilityPercent(ItemStack itemStack)
    {
        return itemStack.getMaxDamage() <= 0 ? 0 : 100 - itemStack.getItemDamage() * 100 / itemStack.getMaxDamage();
    }

    private static TextFormatting getResponseTimeColor(int responseTime)
    {
        if (responseTime >= 200 && responseTime < 300)
        {
            return TextFormatting.YELLOW;
        }
        else if (responseTime >= 300 && responseTime < 500)
        {
            return TextFormatting.RED;
        }
        else if (responseTime >= 500)
        {
            return TextFormatting.DARK_RED;
        }
        else
        {
            return TextFormatting.GREEN;
        }
    }

    private static void renderItem(ItemStack itemStack, int x, int y)
    {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        IndicatiaMod.MC.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        RenderHelper.disableStandardItemLighting();

        if (itemStack.isItemStackDamageable())
        {
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.disableLighting();
            GlStateManager.enableCull();
            IndicatiaMod.MC.getRenderItem().renderItemOverlays(IndicatiaMod.MC.fontRendererObj, itemStack, x, y);
            GlStateManager.blendFunc(770, 771);
            GlStateManager.disableLighting();
        }
    }

    private static String getInventoryItemCount(InventoryPlayer inventory, ItemStack other)
    {
        int count = 0;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack playerItems = inventory.getStackInSlot(i);

            if (playerItems != null && playerItems.getItem() == other.getItem() && playerItems.getItemDamage() == other.getItemDamage() && ItemStack.areItemStackTagsEqual(playerItems, other))
            {
                count += playerItems.stackSize;
            }
        }
        return String.valueOf(count);
    }

    private static int getInventoryArrowCount(InventoryPlayer inventory)
    {
        int arrowCount = 0;

        for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (itemStack != null && itemStack.getItem() instanceof ItemArrow)
            {
                arrowCount += itemStack.stackSize;
            }
        }
        return arrowCount;
    }

    private static String getItemStackCount(ItemStack itemStack, int count)
    {
        return count == 0 || count == 1 || count == 1 && itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("Unbreakable") ? "" : String.valueOf(count);
    }

    private static String getArrowStackCount(int count)
    {
        return count == 0 ? "" : String.valueOf(count);
    }
}