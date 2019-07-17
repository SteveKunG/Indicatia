package stevekung.mods.indicatia.renderer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.*;

import com.google.common.collect.Ordering;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.realmsclient.dto.RealmsServer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.PotionSpriteUploader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsScreenProxy;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import stevekung.mods.indicatia.config.Equipments;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.config.StatusEffects;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.integration.GalacticraftPlanetTime;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.stevekungslib.utils.ColorUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class HUDInfo
{
    public static String getFPS()
    {
        int fps = Minecraft.getDebugFPS();
        String color = ColorUtils.stringToRGB(ExtendedConfig.instance.fpsValueColor).toColoredFont();

        if (fps >= 26 && fps <= 49)
        {
            color = ColorUtils.stringToRGB(ExtendedConfig.instance.fps26And49Color).toColoredFont();
        }
        else if (fps <= 25)
        {
            color = ColorUtils.stringToRGB(ExtendedConfig.instance.fpsLow25Color).toColoredFont();
        }
        return ColorUtils.stringToRGB(ExtendedConfig.instance.fpsColor).toColoredFont() + "FPS: " + color + fps;
    }

    public static String getXYZ(Minecraft mc)
    {
        BlockPos pos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getBoundingBox().minY, mc.getRenderViewEntity().posZ);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        String nether = mc.player.dimension == DimensionType.THE_NETHER ? "Nether " : "";
        return ColorUtils.stringToRGB(ExtendedConfig.instance.xyzColor).toColoredFont() + nether + "XYZ: " + ColorUtils.stringToRGB(ExtendedConfig.instance.xyzValueColor).toColoredFont() + x + " " + y + " " + z;
    }

    public static String getOverworldXYZFromNether(Minecraft mc)
    {
        BlockPos pos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getBoundingBox().minY, mc.getRenderViewEntity().posZ);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return ColorUtils.stringToRGB(ExtendedConfig.instance.xyzColor).toColoredFont() + "Overworld XYZ: " + ColorUtils.stringToRGB(ExtendedConfig.instance.xyzValueColor).toColoredFont() + x * 8 + " " + y + " " + z * 8;
    }

    public static String getBiome(Minecraft mc)
    {
        BlockPos blockPos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getBoundingBox().minY, mc.getRenderViewEntity().posZ);
        ChunkPos chunkPos = new ChunkPos(blockPos);
        Chunk worldChunk = mc.world.getChunk(chunkPos.x, chunkPos.z);

        if (worldChunk.isEmpty())
        {
            return "Waiting for chunk...";
        }
        else
        {
            String biomeName = worldChunk.getBiome(blockPos).getDisplayName().getFormattedText().replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
            return ColorUtils.stringToRGB(ExtendedConfig.instance.biomeColor).toColoredFont() + "Biome: " + ColorUtils.stringToRGB(ExtendedConfig.instance.biomeValueColor).toColoredFont() + biomeName;
        }
    }

    public static String getPing()
    {
        int responseTime = InfoUtils.INSTANCE.getPing();
        return ColorUtils.stringToRGB(ExtendedConfig.instance.pingColor).toColoredFont() + "Ping: " + HUDInfo.getResponseTimeColor(responseTime) + responseTime + "ms";
    }

    public static String getPingToSecond()
    {
        double responseTime = InfoUtils.INSTANCE.getPing() / 1000D;
        return ColorUtils.stringToRGB(ExtendedConfig.instance.pingToSecondColor).toColoredFont() + "Delay: " + HUDInfo.getResponseTimeColor((int) (responseTime * 1000D)) + responseTime + "s";
    }

    public static String getServerIP(Minecraft mc)
    {
        String ip = ColorUtils.stringToRGB(ExtendedConfig.instance.serverIPColor).toColoredFont() + "IP: " + "" + ColorUtils.stringToRGB(ExtendedConfig.instance.serverIPValueColor).toColoredFont() + mc.getCurrentServerData().serverIP;

        if (ExtendedConfig.instance.serverIPMCVersion)
        {
            ip = ip + "/" + mc.getVersion();
        }
        return ip;
    }

    public static String getRealmName(Minecraft mc)
    {
        String text = "Realms Server";
        Screen screen = mc.getConnection().guiScreenServer;
        RealmsScreenProxy screenProxy = (RealmsScreenProxy) screen;
        RealmsScreen realmsScreen = screenProxy.getScreen();

        if (!(realmsScreen instanceof RealmsScreen))
        {
            return text;
        }

        RealmsScreen realmsMainScreen = (RealmsScreen) realmsScreen;
        RealmsServer realmsServer;

        try
        {
            Field selectedServerId = realmsMainScreen.getClass().getDeclaredField("selectedServerId");
            selectedServerId.setAccessible(true);

            if (!selectedServerId.getType().equals(long.class))
            {
                return text;
            }

            long id = selectedServerId.getLong(realmsMainScreen);
            Method findServer = realmsMainScreen.getClass().getDeclaredMethod("findServer", long.class);
            findServer.setAccessible(true);
            Object obj = findServer.invoke(realmsMainScreen, id);

            if (!(obj instanceof RealmsServer))
            {
                return text;
            }
            realmsServer = (RealmsServer)obj;
        }
        catch (Exception e)
        {
            return text;
        }
        return ColorUtils.stringToRGB(ExtendedConfig.instance.serverIPColor).toColoredFont() + "Realms: " + "" + ColorUtils.stringToRGB(ExtendedConfig.instance.serverIPValueColor).toColoredFont() + realmsServer.getName();
    }

    public static String renderDirection(Minecraft mc)
    {
        Entity entity = mc.getRenderViewEntity();
        int yaw = (int)entity.rotationYaw + 22;
        String direction;

        yaw %= 360;

        if (yaw < 0)
        {
            yaw += 360;
        }

        int facing = yaw / 45;

        if (facing < 0)
        {
            facing = 7;
        }

        switch (facing)
        {
        case 0:
            direction = "South";
            break;
        case 1:
            direction = "South West";
            break;
        case 2:
            direction = "West";
            break;
        case 3:
            direction = "North West";
            break;
        case 4:
            direction = "North";
            break;
        case 5:
            direction = "North East";
            break;
        case 6:
            direction = "East";
            break;
        case 7:
            direction = "South East";
            break;
        default:
            direction = "Unknown";
            break;
        }
        return ColorUtils.stringToRGB(ExtendedConfig.instance.directionColor).toColoredFont() + "Direction: " + ColorUtils.stringToRGB(ExtendedConfig.instance.directionValueColor).toColoredFont() + direction;
    }

    public static String getCPS()
    {
        return ColorUtils.stringToRGB(ExtendedConfig.instance.cpsColor).toColoredFont() + "CPS: " + "" + ColorUtils.stringToRGB(ExtendedConfig.instance.cpsValueColor).toColoredFont() + InfoUtils.INSTANCE.getCPS();
    }

    public static String getRCPS()
    {
        return ColorUtils.stringToRGB(ExtendedConfig.instance.rcpsColor).toColoredFont() + "RCPS: " + "" + ColorUtils.stringToRGB(ExtendedConfig.instance.rcpsValueColor).toColoredFont() + InfoUtils.INSTANCE.getRCPS();
    }

    public static String getCurrentTime()
    {
        Date date = new Date();
        boolean isThai = Calendar.getInstance().getTimeZone().getID().equals("Asia/Bangkok");
        String dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, isThai ? new Locale("th", "TH") : Locale.getDefault()).format(date);
        String timeFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM, isThai ? new Locale("th", "TH") : Locale.getDefault()).format(date);
        String currentTime = ColorUtils.stringToRGB(ExtendedConfig.instance.realTimeDDMMYYValueColor).toColoredFont() + dateFormat + " " + ColorUtils.stringToRGB(ExtendedConfig.instance.realTimeHHMMSSValueColor).toColoredFont() + timeFormat;
        return ColorUtils.stringToRGB(ExtendedConfig.instance.realTimeColor).toColoredFont() + "Time: " + currentTime;
    }

    public static String getCurrentGameTime(Minecraft mc)
    {
        boolean isSpace = false;

        try
        {
            Class<?> worldProvider = mc.player.world.dimension.getClass();
            Class<?> spaceWorld = Class.forName("micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace");
            isSpace = IndicatiaMod.isGalacticraftLoaded && spaceWorld.isAssignableFrom(worldProvider);
        }
        catch (Exception e) {}
        return isSpace ? GalacticraftPlanetTime.getTime(mc) : InfoUtils.INSTANCE.getCurrentGameTime(mc.world.getDayTime() % 24000);
    }

    public static String getGameWeather(Minecraft mc)
    {
        String weather = mc.world.isRaining() && !mc.world.isThundering() ? "Raining" : mc.world.isRaining() && mc.world.isThundering() ? "Thunder" : "";
        return ColorUtils.stringToRGB(ExtendedConfig.instance.gameWeatherColor).toColoredFont() + "Weather: " + ColorUtils.stringToRGB(ExtendedConfig.instance.gameWeatherValueColor).toColoredFont() + weather;
    }

    public static void renderHorizontalEquippedItems(Minecraft mc)
    {
        boolean isRightSide = ExtendedConfig.instance.equipmentPosition == Equipments.Position.RIGHT;
        int baseXOffset = 2;
        int baseYOffset = ExtendedConfig.instance.armorHUDYOffset;
        ItemStack mainHandItem = mc.player.getHeldItemMainhand();
        ItemStack offHandItem = mc.player.getHeldItemOffhand();
        List<HorizontalEquipment> element = new LinkedList<>();
        int prevX = 0;
        int rightWidth = 0;
        element.clear();

        // armor/held item stuff
        switch (ExtendedConfig.instance.equipmentOrdering)
        {
        case DEFAULT:
            if (!mainHandItem.isEmpty())
            {
                element.add(new HorizontalEquipment(mainHandItem, false));
            }
            if (!offHandItem.isEmpty())
            {
                element.add(new HorizontalEquipment(offHandItem, false));
            }
            for (int i = 3; i >= 0; i--)
            {
                if (!mc.player.inventory.armorInventory.get(i).isEmpty())
                {
                    element.add(new HorizontalEquipment(mc.player.inventory.armorInventory.get(i), mc.player.inventory.armorInventory.get(i).isDamageable()));
                }
            }
            break;
        case REVERSE:
            for (int i = 0; i <= 3; i++)
            {
                if (!mc.player.inventory.armorInventory.get(i).isEmpty())
                {
                    element.add(new HorizontalEquipment(mc.player.inventory.armorInventory.get(i), mc.player.inventory.armorInventory.get(i).isDamageable()));
                }
            }
            if (!mainHandItem.isEmpty())
            {
                element.add(new HorizontalEquipment(mainHandItem, false));
            }
            if (!offHandItem.isEmpty())
            {
                element.add(new HorizontalEquipment(offHandItem, false));
            }
            break;
        }

        for (HorizontalEquipment equipment : element)
        {
            rightWidth += equipment.getWidth();
        }
        for (HorizontalEquipment equipment : element)
        {
            int xBaseRight = mc.mainWindow.getScaledWidth() - rightWidth - baseXOffset;
            equipment.render(isRightSide ? xBaseRight + prevX + equipment.getWidth() : baseXOffset + prevX, baseYOffset);
            prevX += equipment.getWidth();
        }
    }

    public static void renderVerticalEquippedItems(Minecraft mc)
    {
        boolean isRightSide = ExtendedConfig.instance.equipmentPosition == Equipments.Position.RIGHT;
        List<ItemStack> itemStackList = new LinkedList<>();
        List<String> itemStatusList = new LinkedList<>();
        List<String> arrowCountList = new LinkedList<>();
        int baseXOffset = isRightSide ? mc.mainWindow.getScaledWidth() - 18 : 2;
        int baseYOffset = ExtendedConfig.instance.armorHUDYOffset;
        ItemStack mainHandItem = mc.player.getHeldItemMainhand();
        ItemStack offHandItem = mc.player.getHeldItemOffhand();
        int arrowCount = HUDInfo.getInventoryArrowCount(mc.player.inventory);

        // held item stuff
        if (ExtendedConfig.instance.equipmentOrdering == Equipments.Ordering.REVERSE)
        {
            if (!mainHandItem.isEmpty())
            {
                itemStackList.add(mainHandItem);
                String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mainHandItem);
                itemStatusList.add(mainHandItem.isDamageable() ? HUDInfo.getArmorDurabilityStatus(mainHandItem) : ExtendedConfig.instance.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(mainHandItem, Integer.parseInt(itemCount)));

                if (mainHandItem.getItem() == Items.BOW)
                {
                    arrowCountList.add(HUDInfo.getArrowStackCount(arrowCount));
                }
                else
                {
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
            if (!offHandItem.isEmpty())
            {
                itemStackList.add(offHandItem);
                String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, offHandItem);
                itemStatusList.add(offHandItem.isDamageable() ? HUDInfo.getArmorDurabilityStatus(offHandItem) : ExtendedConfig.instance.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(offHandItem, Integer.parseInt(itemCount)));

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
        switch (ExtendedConfig.instance.equipmentOrdering)
        {
        case DEFAULT:
            for (int i = 3; i >= 0; i--)
            {
                if (!mc.player.inventory.armorInventory.get(i).isEmpty())
                {
                    String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mc.player.inventory.armorInventory.get(i));
                    itemStackList.add(mc.player.inventory.armorInventory.get(i));
                    itemStatusList.add(mc.player.inventory.armorInventory.get(i).isDamageable() ? HUDInfo.getArmorDurabilityStatus(mc.player.inventory.armorInventory.get(i)) : HUDInfo.getItemStackCount(mc.player.inventory.armorInventory.get(i), Integer.parseInt(itemCount)));
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
            break;
        case REVERSE:
            for (int i = 0; i <= 3; i++)
            {
                if (!mc.player.inventory.armorInventory.get(i).isEmpty())
                {
                    String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mc.player.inventory.armorInventory.get(i));
                    itemStackList.add(mc.player.inventory.armorInventory.get(i));
                    itemStatusList.add(mc.player.inventory.armorInventory.get(i).isDamageable() ? HUDInfo.getArmorDurabilityStatus(mc.player.inventory.armorInventory.get(i)) : HUDInfo.getItemStackCount(mc.player.inventory.armorInventory.get(i), Integer.parseInt(itemCount)));
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
            break;
        }

        // held item stuff
        if (ExtendedConfig.instance.equipmentOrdering == Equipments.Ordering.DEFAULT)
        {
            if (!mainHandItem.isEmpty())
            {
                itemStackList.add(mainHandItem);
                String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mainHandItem);
                itemStatusList.add(mainHandItem.isDamageable() ? HUDInfo.getArmorDurabilityStatus(mainHandItem) : ExtendedConfig.instance.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(mainHandItem, Integer.parseInt(itemCount)));

                if (mainHandItem.getItem() == Items.BOW)
                {
                    arrowCountList.add(HUDInfo.getArrowStackCount(arrowCount));
                }
                else
                {
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
            if (!offHandItem.isEmpty())
            {
                itemStackList.add(offHandItem);
                String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, offHandItem);
                itemStatusList.add(offHandItem.isDamageable() ? HUDInfo.getArmorDurabilityStatus(offHandItem) : ExtendedConfig.instance.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(offHandItem, Integer.parseInt(itemCount)));

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

            if (!itemStackList.isEmpty())
            {
                int yOffset = baseYOffset + 16 * i;
                HUDInfo.renderItem(itemStack, baseXOffset, yOffset);
                yOffset += 16;
            }
        }

        float yOffset = 0;
        float fontHeight = 0;

        // durability/item count stuff
        for (int i = 0; i < itemStatusList.size(); ++i)
        {
            String string = itemStatusList.get(i);
            fontHeight = ColorUtils.coloredFontRenderer.FONT_HEIGHT + 7.0625F;

            if (!string.isEmpty())
            {
                yOffset = baseYOffset + 4 + fontHeight * i;
                float xOffset = isRightSide ? mc.mainWindow.getScaledWidth() - ColorUtils.coloredFontRenderer.getStringWidth(string) - 20.0625F : baseXOffset + 18.0625F;
                ColorUtils.coloredFontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.instance.equipmentStatusColor).toColoredFont() + string, xOffset, yOffset, 16777215);
            }
        }

        // arrow count stuff
        for (int i = 0; i < arrowCountList.size(); ++i)
        {
            String string = arrowCountList.get(i);
            yOffset = baseYOffset + 8 + fontHeight * i;

            if (!string.isEmpty())
            {
                GlStateManager.disableDepthTest();
                ColorUtils.coloredFontRendererUnicode.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.instance.arrowCountColor).toColoredFont() + string, isRightSide ? mc.mainWindow.getScaledWidth() - ColorUtils.coloredFontRendererUnicode.getStringWidth(string) - 2.0625F : baseXOffset + 8.0625F, yOffset, 16777215);
                GlStateManager.enableDepthTest();
            }
        }
    }

    public static void renderHotbarEquippedItems(Minecraft mc)
    {
        List<ItemStack> leftItemStackList = new LinkedList<>();
        List<String> leftItemStatusList = new LinkedList<>();
        List<String> leftArrowCountList = new LinkedList<>();
        List<ItemStack> rightItemStackList = new LinkedList<>();
        List<String> rightItemStatusList = new LinkedList<>();
        List<String> rightArrowCountList = new LinkedList<>();
        ItemStack mainHandItem = mc.player.getHeldItemMainhand();
        ItemStack offHandItem = mc.player.getHeldItemOffhand();
        int arrowCount = HUDInfo.getInventoryArrowCount(mc.player.inventory);

        for (int i = 2; i <= 3; i++)
        {
            if (!mc.player.inventory.armorInventory.get(i).isEmpty())
            {
                String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mc.player.inventory.armorInventory.get(i));
                leftItemStackList.add(mc.player.inventory.armorInventory.get(i));
                leftItemStatusList.add(mc.player.inventory.armorInventory.get(i).isDamageable() ? HUDInfo.getArmorDurabilityStatus(mc.player.inventory.armorInventory.get(i)) : HUDInfo.getItemStackCount(mc.player.inventory.armorInventory.get(i), Integer.parseInt(itemCount)));
                leftArrowCountList.add(""); // dummy bow arrow count list size
            }
        }

        for (int i = 0; i <= 1; i++)
        {
            if (!mc.player.inventory.armorInventory.get(i).isEmpty())
            {
                String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mc.player.inventory.armorInventory.get(i));
                rightItemStackList.add(mc.player.inventory.armorInventory.get(i));
                rightItemStatusList.add(mc.player.inventory.armorInventory.get(i).isDamageable() ? HUDInfo.getArmorDurabilityStatus(mc.player.inventory.armorInventory.get(i)) : HUDInfo.getItemStackCount(mc.player.inventory.armorInventory.get(i), Integer.parseInt(itemCount)));
                rightArrowCountList.add(""); // dummy bow arrow count list size
            }
        }

        if (!mainHandItem.isEmpty())
        {
            leftItemStackList.add(mainHandItem);
            String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mainHandItem);
            leftItemStatusList.add(mainHandItem.isDamageable() ? HUDInfo.getArmorDurabilityStatus(mainHandItem) : ExtendedConfig.instance.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(mainHandItem, Integer.parseInt(itemCount)));

            if (mainHandItem.getItem() == Items.BOW)
            {
                leftArrowCountList.add(HUDInfo.getArrowStackCount(arrowCount));
            }
            else
            {
                leftArrowCountList.add(""); // dummy bow arrow count list size
            }
        }
        if (!offHandItem.isEmpty())
        {
            rightItemStackList.add(offHandItem);
            String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, offHandItem);
            rightItemStatusList.add(offHandItem.isDamageable() ? HUDInfo.getArmorDurabilityStatus(offHandItem) : ExtendedConfig.instance.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(offHandItem, Integer.parseInt(itemCount)));

            if (offHandItem.getItem() == Items.BOW)
            {
                rightArrowCountList.add(HUDInfo.getArrowStackCount(arrowCount));
            }
            else
            {
                rightArrowCountList.add(""); // dummy bow arrow count list size
            }
        }

        // left item render stuff
        for (int i = 0; i < leftItemStackList.size(); ++i)
        {
            ItemStack itemStack = leftItemStackList.get(i);

            if (!leftItemStackList.isEmpty())
            {
                int baseXOffset = mc.mainWindow.getScaledWidth() / 2 - 91 - 20;
                int yOffset = mc.mainWindow.getScaledHeight() - 16 * i - 40;
                HUDInfo.renderItem(itemStack, baseXOffset, yOffset);
            }
        }

        // right item render stuff
        for (int i = 0; i < rightItemStackList.size(); ++i)
        {
            ItemStack itemStack = rightItemStackList.get(i);

            if (!rightItemStackList.isEmpty())
            {
                int baseXOffset = mc.mainWindow.getScaledWidth() / 2 + 95;
                int yOffset = mc.mainWindow.getScaledHeight() - 16 * i - 40;
                HUDInfo.renderItem(itemStack, baseXOffset, yOffset);
            }
        }

        // left durability/item count stuff
        for (int i = 0; i < leftItemStatusList.size(); ++i)
        {
            String string = leftItemStatusList.get(i);
            int stringWidth = ColorUtils.coloredFontRenderer.getStringWidth(string);
            float xOffset = mc.mainWindow.getScaledWidth() / 2 - 114 - stringWidth;
            int yOffset = mc.mainWindow.getScaledHeight() - 16 * i - 36;
            ColorUtils.coloredFontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.instance.equipmentStatusColor).toColoredFont() + string, xOffset, yOffset, 16777215);
        }

        // right durability/item count stuff
        for (int i = 0; i < rightItemStatusList.size(); ++i)
        {
            String string = rightItemStatusList.get(i);
            float xOffset = mc.mainWindow.getScaledWidth() / 2 + 114;
            int yOffset = mc.mainWindow.getScaledHeight() - 16 * i - 36;
            ColorUtils.coloredFontRenderer.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.instance.equipmentStatusColor).toColoredFont() + string, xOffset, yOffset, 16777215);
        }

        // left arrow count stuff
        for (int i = 0; i < leftArrowCountList.size(); ++i)
        {
            String string = leftArrowCountList.get(i);
            int stringWidth = ColorUtils.coloredFontRendererUnicode.getStringWidth(string);
            float xOffset = mc.mainWindow.getScaledWidth() / 2 - 90 - stringWidth;
            int yOffset = mc.mainWindow.getScaledHeight() - 16 * i - 32;

            if (!string.isEmpty())
            {
                GlStateManager.disableDepthTest();
                ColorUtils.coloredFontRendererUnicode.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.instance.arrowCountColor).toColoredFont() + string, xOffset, yOffset, 16777215);
                GlStateManager.enableDepthTest();
            }
        }

        // right arrow count stuff
        for (int i = 0; i < rightArrowCountList.size(); ++i)
        {
            String string = rightArrowCountList.get(i);
            float xOffset = mc.mainWindow.getScaledWidth() / 2 + 104;
            int yOffset = mc.mainWindow.getScaledHeight() - 16 * i - 32;

            if (!string.isEmpty())
            {
                GlStateManager.disableDepthTest();
                ColorUtils.coloredFontRendererUnicode.drawStringWithShadow(ColorUtils.stringToRGB(ExtendedConfig.instance.arrowCountColor).toColoredFont() + string, xOffset, yOffset, 16777215);
                GlStateManager.enableDepthTest();
            }
        }
    }

    public static void renderPotionHUD(Minecraft mc)
    {
        boolean iconAndTime = ExtendedConfig.instance.potionHUDStyle == StatusEffects.Style.ICON_AND_TIME;
        boolean right = ExtendedConfig.instance.potionHUDPosition == StatusEffects.Position.RIGHT;
        boolean showIcon = ExtendedConfig.instance.potionHUDIcon;
        int size = ExtendedConfig.instance.maximumPotionDisplay;
        int length = ExtendedConfig.instance.potionLengthYOffset;
        int lengthOverlap = ExtendedConfig.instance.potionLengthYOffsetOverlap;
        Collection<EffectInstance> collection = mc.player.getActivePotionEffects();
        int xPotion;
        int yPotion;

        if (ExtendedConfig.instance.potionHUDPosition == StatusEffects.Position.HOTBAR_LEFT)
        {
            xPotion = mc.mainWindow.getScaledWidth() / 2 - 91 - 35;
            yPotion = mc.mainWindow.getScaledHeight() - 46;
        }
        else if (ExtendedConfig.instance.potionHUDPosition == StatusEffects.Position.HOTBAR_RIGHT)
        {
            xPotion = mc.mainWindow.getScaledWidth() / 2 + 91 - 20;
            yPotion = mc.mainWindow.getScaledHeight() - 42;
        }
        else
        {
            xPotion = right ? mc.mainWindow.getScaledWidth() - 32 : -24;
            yPotion = mc.mainWindow.getScaledHeight() - 220 + ExtendedConfig.instance.potionHUDYOffset + 90;
        }

        if (!collection.isEmpty())
        {
            if (collection.size() > size)
            {
                length = lengthOverlap / (collection.size() - 1);
            }

            for (EffectInstance potioneffect : Ordering.natural().sortedCopy(collection))
            {
                float alpha = 1.0F;
                Effect potion = potioneffect.getPotion();
                String s = EffectUtils.getPotionDurationString(potioneffect, 1.0F);
                String s1 = LangUtils.translate(potion.getName());

                if (!potioneffect.isAmbient() && potioneffect.getDuration() <= 200)
                {
                    int j1 = 10 - potioneffect.getDuration() / 20;
                    alpha = MathHelper.clamp(potioneffect.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos(potioneffect.getDuration() * (float)Math.PI / 5.0F) * MathHelper.clamp(j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                }

                GlStateManager.enableBlend();
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, alpha);

                if (showIcon)
                {
                    potion.renderInventoryEffect(potioneffect, (DisplayEffectsScreen<?>)mc.currentScreen, xPotion, yPotion, mc.currentScreen.blitOffset);
                    PotionSpriteUploader sprite = mc.getPotionSpriteUploader();

                    if (ExtendedConfig.instance.potionHUDPosition == StatusEffects.Position.HOTBAR_LEFT)
                    {
                        AbstractGui.blit(xPotion + 12, yPotion + 6, mc.currentScreen.blitOffset, 18, 18, sprite.getSprite(potion));
                    }
                    else if (ExtendedConfig.instance.potionHUDPosition == StatusEffects.Position.HOTBAR_RIGHT)
                    {
                        AbstractGui.blit(xPotion + 24, yPotion + 6, mc.currentScreen.blitOffset, 18, 18, sprite.getSprite(potion));
                    }
                    else
                    {
                        AbstractGui.blit(right ? xPotion + 12 : xPotion + 28, yPotion + 6, mc.currentScreen.blitOffset, 18, 18, sprite.getSprite(potion));
                    }
                }

                if (potioneffect.getAmplifier() == 1)
                {
                    s1 = s1 + " " + LangUtils.translate("enchantment.level.2");
                }
                else if (potioneffect.getAmplifier() == 2)
                {
                    s1 = s1 + " " + LangUtils.translate("enchantment.level.3");
                }
                else if (potioneffect.getAmplifier() == 3)
                {
                    s1 = s1 + " " + LangUtils.translate("enchantment.level.4");
                }

                int stringwidth1 = ColorUtils.coloredFontRenderer.getStringWidth(s);
                int stringwidth2 = ColorUtils.coloredFontRenderer.getStringWidth(s1);
                int yOffset = iconAndTime ? 11 : 16;

                if (ExtendedConfig.instance.potionHUDPosition == StatusEffects.Position.HOTBAR_LEFT)
                {
                    int xOffset = showIcon ? 8 : 28;

                    if (!iconAndTime)
                    {
                        ColorUtils.coloredFontRenderer.drawStringWithShadow(s1, xPotion + xOffset - stringwidth2, yPotion + 6, ExtendedConfig.instance.alternatePotionHUDTextColor ? potion.getLiquidColor() : 16777215);
                    }
                    ColorUtils.coloredFontRenderer.drawStringWithShadow(s, xPotion + xOffset - stringwidth1, yPotion + yOffset, ExtendedConfig.instance.alternatePotionHUDTextColor ? potion.getLiquidColor() : 16777215);
                }
                else if (ExtendedConfig.instance.potionHUDPosition == StatusEffects.Position.HOTBAR_RIGHT)
                {
                    int xOffset = showIcon ? 46 : 28;

                    if (!iconAndTime)
                    {
                        ColorUtils.coloredFontRenderer.drawStringWithShadow(s1, xPotion + xOffset, yPotion + 6, ExtendedConfig.instance.alternatePotionHUDTextColor ? potion.getLiquidColor() : 16777215);
                    }
                    ColorUtils.coloredFontRenderer.drawStringWithShadow(s, xPotion + xOffset, yPotion + yOffset, ExtendedConfig.instance.alternatePotionHUDTextColor ? potion.getLiquidColor() : 16777215);
                }
                else
                {
                    int leftXOffset = showIcon ? 50 : 28;
                    int rightXOffset = showIcon ? 8 : 28;

                    if (!iconAndTime)
                    {
                        ColorUtils.coloredFontRenderer.drawStringWithShadow(s1, right ? xPotion + rightXOffset - stringwidth2 : xPotion + leftXOffset, yPotion + 6, ExtendedConfig.instance.alternatePotionHUDTextColor ? potion.getLiquidColor() : 16777215);
                    }
                    ColorUtils.coloredFontRenderer.drawStringWithShadow(s, right ? xPotion + rightXOffset - stringwidth1 : xPotion + leftXOffset, yPotion + yOffset, ExtendedConfig.instance.alternatePotionHUDTextColor ? potion.getLiquidColor() : 16777215);
                }
                yPotion -= length;
            }
        }
    }

    static String getArmorDurabilityStatus(ItemStack itemStack)
    {
        switch (ExtendedConfig.instance.equipmentStatus)
        {
        case DAMAGE_AND_MAX_DAMAGE:
        default:
            return itemStack.getMaxDamage() - itemStack.getDamage() + "/" + itemStack.getMaxDamage();
        case PERCENT:
            return HUDInfo.calculateItemDurabilityPercent(itemStack) + "%";
        case ONLY_DAMAGE:
            return String.valueOf(itemStack.getMaxDamage() - itemStack.getDamage());
        case NONE:
            return "";
        }
    }

    private static int calculateItemDurabilityPercent(ItemStack itemStack)
    {
        return itemStack.getMaxDamage() <= 0 ? 0 : 100 - itemStack.getDamage() * 100 / itemStack.getMaxDamage();
    }

    private static String getResponseTimeColor(int responseTime)
    {
        if (responseTime >= 200 && responseTime < 300)
        {
            return ColorUtils.stringToRGB(ExtendedConfig.instance.ping200And300Color).toColoredFont();
        }
        else if (responseTime >= 300 && responseTime < 500)
        {
            return ColorUtils.stringToRGB(ExtendedConfig.instance.ping300And500Color).toColoredFont();
        }
        else if (responseTime >= 500)
        {
            return ColorUtils.stringToRGB(ExtendedConfig.instance.pingMax500Color).toColoredFont();
        }
        else
        {
            return ColorUtils.stringToRGB(ExtendedConfig.instance.pingValueColor).toColoredFont();
        }
    }

    static void renderItem(ItemStack itemStack, int x, int y)
    {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(itemStack, x, y);
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        RenderHelper.disableStandardItemLighting();

        if (itemStack.isDamageable())
        {
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.disableLighting();
            GlStateManager.enableCull();
            Minecraft.getInstance().getItemRenderer().renderItemOverlays(ColorUtils.coloredFontRenderer, itemStack, x, y);
            GlStateManager.blendFunc(770, 771);
            GlStateManager.disableLighting();
        }
    }

    static String getInventoryItemCount(PlayerInventory inventory, ItemStack other)
    {
        int count = 0;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack playerItems = inventory.getStackInSlot(i);

            if (!playerItems.isEmpty() && playerItems.getItem() == other.getItem() && playerItems.getDamage() == other.getDamage() && ItemStack.areItemStackTagsEqual(playerItems, other))
            {
                count += playerItems.getCount();
            }
        }
        return String.valueOf(count);
    }

    static int getInventoryArrowCount(PlayerInventory inventory)
    {
        int arrowCount = 0;

        for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (!itemStack.isEmpty() && itemStack.getItem() instanceof ArrowItem)
            {
                arrowCount += itemStack.getCount();
            }
        }
        return arrowCount;
    }

    static String getItemStackCount(ItemStack itemStack, int count)
    {
        return count == 0 || count == 1 || count == 1 && itemStack.hasTag() && itemStack.getTag().getBoolean("Unbreakable") ? "" : String.valueOf(count);
    }

    static String getArrowStackCount(int count)
    {
        return count == 0 ? "" : String.valueOf(count);
    }
}