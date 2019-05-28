package stevekung.mods.indicatia.renderer;

import java.text.DateFormat;
import java.util.*;

import com.google.common.collect.Ordering;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;
import stevekung.mods.indicatia.config.Equipments;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.config.StatusEffects;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.stevekungslib.utils.ColorUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class HUDInfo
{
    public static String getFPS()
    {
        int fps = MinecraftClient.getCurrentFps();
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

    public static String getXYZ(MinecraftClient mc)
    {
        BlockPos pos = new BlockPos(mc.getCameraEntity().x, mc.getCameraEntity().getBoundingBox().minY, mc.getCameraEntity().z);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        String nether = mc.player.dimension == DimensionType.THE_NETHER ? "Nether " : "";
        return ColorUtils.stringToRGB(ExtendedConfig.instance.xyzColor).toColoredFont() + nether + "XYZ: " + ColorUtils.stringToRGB(ExtendedConfig.instance.xyzValueColor).toColoredFont() + x + " " + y + " " + z;
    }

    public static String getOverworldXYZFromNether(MinecraftClient mc)
    {
        BlockPos pos = new BlockPos(mc.getCameraEntity().x, mc.getCameraEntity().getBoundingBox().minY, mc.getCameraEntity().z);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return ColorUtils.stringToRGB(ExtendedConfig.instance.xyzColor).toColoredFont() + "Overworld XYZ: " + ColorUtils.stringToRGB(ExtendedConfig.instance.xyzValueColor).toColoredFont() + x * 8 + " " + y + " " + z * 8;
    }

    public static String getBiome(MinecraftClient mc)
    {
        BlockPos blockPos = new BlockPos(mc.getCameraEntity().x, mc.getCameraEntity().getBoundingBox().minY, mc.getCameraEntity().z);
        ChunkPos chunkPos = new ChunkPos(blockPos);
        WorldChunk worldChunk = mc.world.method_8497(chunkPos.x, chunkPos.z);//TODO getWorldChunk

        if (worldChunk.isEmpty())
        {
            return "Waiting for chunk...";
        }
        else
        {
            String biomeName = worldChunk.getBiome(blockPos).getTextComponent().getFormattedText().replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2");
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

    public static String getServerIP(MinecraftClient mc)
    {
        String ip = ColorUtils.stringToRGB(ExtendedConfig.instance.serverIPColor).toColoredFont() + "IP: " + "" + ColorUtils.stringToRGB(ExtendedConfig.instance.serverIPValueColor).toColoredFont() + mc.getServer().getServerIp();

        if (ExtendedConfig.instance.serverIPMCVersion)
        {
            ip = ip + "/" + mc.getGameVersion();
        }
        return ip;
    }

    /*public static String getRealmName(MinecraftClient mc)
    {
        String text = "Realms Server";
        GuiScreen screen = mc.getConnection().guiScreenServer;
        GuiScreenRealmsProxy screenProxy = (GuiScreenRealmsProxy) screen;
        RealmsScreen realmsScreen = screenProxy.getProxy();

        if (!(realmsScreen instanceof RealmsMainScreen))
        {
            return text;
        }

        RealmsMainScreen realmsMainScreen = (RealmsMainScreen) realmsScreen;
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
        return ColorUtils.stringToRGB(ExtendedConfig.instance.serverIPColor).toColoredFont() + "Realms: " + "" + ColorUtils.stringToRGB(ExtendedConfig.instance.serverIPValueColor).toColoredFont() + realmsServer.getTranslationKey();
    }*/

    public static String renderDirection(MinecraftClient mc)
    {
        Entity entity = mc.getCameraEntity();
        int yaw = (int)entity.yaw + 22;
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

    public static String getCurrentGameTime(MinecraftClient mc)
    {
        /*boolean isSpace = false;

        try
        {
            Class<?> worldProvider = mc.player.world.dimension.getClass();
            Class<?> spaceWorld = Class.forName("micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace");
            isSpace = IndicatiaMod.isGalacticraftLoaded && spaceWorld.isAssignableFrom(worldProvider);
        }
        catch (Exception e) {}
        return isSpace ? GalacticraftPlanetTime.getTime(mc) : InfoUtils.INSTANCE.getCurrentGameTime(mc.world.getTimeOfDay() % 24000);*/
        return InfoUtils.INSTANCE.getCurrentGameTime(mc.world.getTimeOfDay() % 24000);
    }

    public static String getGameWeather(MinecraftClient mc)
    {
        String weather = mc.world.isRaining() && !mc.world.isThundering() ? "Raining" : mc.world.isRaining() && mc.world.isThundering() ? "Thunder" : "";
        return ColorUtils.stringToRGB(ExtendedConfig.instance.gameWeatherColor).toColoredFont() + "Weather: " + ColorUtils.stringToRGB(ExtendedConfig.instance.gameWeatherValueColor).toColoredFont() + weather;
    }

    public static void renderHorizontalEquippedItems(MinecraftClient mc)
    {
        boolean isRightSide = ExtendedConfig.instance.equipmentPosition == Equipments.Position.RIGHT;
        int baseXOffset = 2;
        int baseYOffset = ExtendedConfig.instance.armorHUDYOffset;
        ItemStack mainHandItem = mc.player.getMainHandStack();
        ItemStack offHandItem = mc.player.getOffHandStack();
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
                if (!mc.player.inventory.armor.get(i).isEmpty())
                {
                    element.add(new HorizontalEquipment(mc.player.inventory.armor.get(i), mc.player.inventory.armor.get(i).hasDurability()));
                }
            }
            break;
        case REVERSE:
            for (int i = 0; i <= 3; i++)
            {
                if (!mc.player.inventory.armor.get(i).isEmpty())
                {
                    element.add(new HorizontalEquipment(mc.player.inventory.armor.get(i), mc.player.inventory.armor.get(i).hasDurability()));
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
            int xBaseRight = mc.window.getScaledWidth() - rightWidth - baseXOffset;
            equipment.render(isRightSide ? xBaseRight + prevX + equipment.getWidth() : baseXOffset + prevX, baseYOffset);
            prevX += equipment.getWidth();
        }
    }

    public static void renderVerticalEquippedItems(MinecraftClient mc)
    {
        List<ItemStack> itemStackList = new LinkedList<>();
        List<String> itemStatusList = new LinkedList<>();
        List<String> arrowCountList = new LinkedList<>();
        boolean isRightSide = ExtendedConfig.instance.equipmentPosition == Equipments.Position.RIGHT;
        int baseXOffset = isRightSide ? mc.window.getScaledWidth() - 18 : 2;
        int baseYOffset = ExtendedConfig.instance.armorHUDYOffset;
        ItemStack mainHandItem = mc.player.getMainHandStack();
        ItemStack offHandItem = mc.player.getOffHandStack();
        int arrowCount = HUDInfo.getInventoryArrowCount(mc.player.inventory);

        // held item stuff
        if (ExtendedConfig.instance.equipmentOrdering == Equipments.Ordering.REVERSE)
        {
            if (!mainHandItem.isEmpty())
            {
                itemStackList.add(mainHandItem);
                String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mainHandItem);
                itemStatusList.add(mainHandItem.hasDurability() ? HUDInfo.getArmorDurabilityStatus(mainHandItem) : ExtendedConfig.instance.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(mainHandItem, Integer.parseInt(itemCount)));

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
                itemStatusList.add(offHandItem.hasDurability() ? HUDInfo.getArmorDurabilityStatus(offHandItem) : ExtendedConfig.instance.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(offHandItem, Integer.parseInt(itemCount)));

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
                if (!mc.player.inventory.armor.get(i).isEmpty())
                {
                    String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mc.player.inventory.armor.get(i));
                    itemStackList.add(mc.player.inventory.armor.get(i));
                    itemStatusList.add(mc.player.inventory.armor.get(i).hasDurability() ? HUDInfo.getArmorDurabilityStatus(mc.player.inventory.armor.get(i)) : HUDInfo.getItemStackCount(mc.player.inventory.armor.get(i), Integer.parseInt(itemCount)));
                    arrowCountList.add(""); // dummy bow arrow count list size
                }
            }
            break;
        case REVERSE:
            for (int i = 0; i <= 3; i++)
            {
                if (!mc.player.inventory.armor.get(i).isEmpty())
                {
                    String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mc.player.inventory.armor.get(i));
                    itemStackList.add(mc.player.inventory.armor.get(i));
                    itemStatusList.add(mc.player.inventory.armor.get(i).hasDurability() ? HUDInfo.getArmorDurabilityStatus(mc.player.inventory.armor.get(i)) : HUDInfo.getItemStackCount(mc.player.inventory.armor.get(i), Integer.parseInt(itemCount)));
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
                itemStatusList.add(mainHandItem.hasDurability() ? HUDInfo.getArmorDurabilityStatus(mainHandItem) : ExtendedConfig.instance.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(mainHandItem, Integer.parseInt(itemCount)));

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
                itemStatusList.add(offHandItem.hasDurability() ? HUDInfo.getArmorDurabilityStatus(offHandItem) : ExtendedConfig.instance.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(offHandItem, Integer.parseInt(itemCount)));

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
            fontHeight = ColorUtils.coloredFontRenderer.fontHeight + 7.0625F;

            if (!string.isEmpty())
            {
                yOffset = baseYOffset + 4 + fontHeight * i;
                float xOffset = isRightSide ? mc.window.getScaledWidth() - ColorUtils.coloredFontRenderer.getStringWidth(string) - 20.0625F : baseXOffset + 18.0625F;
                ColorUtils.coloredFontRenderer.drawWithShadow(ColorUtils.stringToRGB(ExtendedConfig.instance.equipmentStatusColor).toColoredFont() + string, xOffset, yOffset, 16777215);
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
                ColorUtils.coloredFontRendererUnicode.drawWithShadow(ColorUtils.stringToRGB(ExtendedConfig.instance.arrowCountColor).toColoredFont() + string, isRightSide ? mc.window.getScaledWidth() - ColorUtils.coloredFontRendererUnicode.getStringWidth(string) - 2.0625F : baseXOffset + 8.0625F, yOffset, 16777215);
                GlStateManager.enableDepthTest();
            }
        }
    }

    public static void renderHotbarEquippedItems(MinecraftClient mc)
    {
        List<ItemStack> leftItemStackList = new LinkedList<>();
        List<String> leftItemStatusList = new LinkedList<>();
        List<String> leftArrowCountList = new LinkedList<>();
        List<ItemStack> rightItemStackList = new LinkedList<>();
        List<String> rightItemStatusList = new LinkedList<>();
        List<String> rightArrowCountList = new LinkedList<>();
        ItemStack mainHandItem = mc.player.getMainHandStack();
        ItemStack offHandItem = mc.player.getOffHandStack();
        int arrowCount = HUDInfo.getInventoryArrowCount(mc.player.inventory);

        for (int i = 2; i <= 3; i++)
        {
            if (!mc.player.inventory.armor.get(i).isEmpty())
            {
                String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mc.player.inventory.armor.get(i));
                leftItemStackList.add(mc.player.inventory.armor.get(i));
                leftItemStatusList.add(mc.player.inventory.armor.get(i).hasDurability() ? HUDInfo.getArmorDurabilityStatus(mc.player.inventory.armor.get(i)) : HUDInfo.getItemStackCount(mc.player.inventory.armor.get(i), Integer.parseInt(itemCount)));
                leftArrowCountList.add(""); // dummy bow arrow count list size
            }
        }

        for (int i = 0; i <= 1; i++)
        {
            if (!mc.player.inventory.armor.get(i).isEmpty())
            {
                String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mc.player.inventory.armor.get(i));
                rightItemStackList.add(mc.player.inventory.armor.get(i));
                rightItemStatusList.add(mc.player.inventory.armor.get(i).hasDurability() ? HUDInfo.getArmorDurabilityStatus(mc.player.inventory.armor.get(i)) : HUDInfo.getItemStackCount(mc.player.inventory.armor.get(i), Integer.parseInt(itemCount)));
                rightArrowCountList.add(""); // dummy bow arrow count list size
            }
        }

        if (!mainHandItem.isEmpty())
        {
            leftItemStackList.add(mainHandItem);
            String itemCount = HUDInfo.getInventoryItemCount(mc.player.inventory, mainHandItem);
            leftItemStatusList.add(mainHandItem.hasDurability() ? HUDInfo.getArmorDurabilityStatus(mainHandItem) : ExtendedConfig.instance.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(mainHandItem, Integer.parseInt(itemCount)));

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
            rightItemStatusList.add(offHandItem.hasDurability() ? HUDInfo.getArmorDurabilityStatus(offHandItem) : ExtendedConfig.instance.equipmentStatus == Equipments.Status.NONE ? "" : HUDInfo.getItemStackCount(offHandItem, Integer.parseInt(itemCount)));

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
                int baseXOffset = mc.window.getScaledWidth() / 2 - 91 - 20;
                int yOffset = mc.window.getScaledHeight() - 16 * i - 40;
                HUDInfo.renderItem(itemStack, baseXOffset, yOffset);
            }
        }

        // right item render stuff
        for (int i = 0; i < rightItemStackList.size(); ++i)
        {
            ItemStack itemStack = rightItemStackList.get(i);

            if (!rightItemStackList.isEmpty())
            {
                int baseXOffset = mc.window.getScaledWidth() / 2 + 95;
                int yOffset = mc.window.getScaledHeight() - 16 * i - 40;
                HUDInfo.renderItem(itemStack, baseXOffset, yOffset);
            }
        }

        // left durability/item count stuff
        for (int i = 0; i < leftItemStatusList.size(); ++i)
        {
            String string = leftItemStatusList.get(i);
            int stringWidth = ColorUtils.coloredFontRenderer.getStringWidth(string);
            float xOffset = mc.window.getScaledWidth() / 2 - 114 - stringWidth;
            int yOffset = mc.window.getScaledHeight() - 16 * i - 36;
            ColorUtils.coloredFontRenderer.drawWithShadow(ColorUtils.stringToRGB(ExtendedConfig.instance.equipmentStatusColor).toColoredFont() + string, xOffset, yOffset, 16777215);
        }

        // right durability/item count stuff
        for (int i = 0; i < rightItemStatusList.size(); ++i)
        {
            String string = rightItemStatusList.get(i);
            float xOffset = mc.window.getScaledWidth() / 2 + 114;
            int yOffset = mc.window.getScaledHeight() - 16 * i - 36;
            ColorUtils.coloredFontRenderer.drawWithShadow(ColorUtils.stringToRGB(ExtendedConfig.instance.equipmentStatusColor).toColoredFont() + string, xOffset, yOffset, 16777215);
        }

        // left arrow count stuff
        for (int i = 0; i < leftArrowCountList.size(); ++i)
        {
            String string = leftArrowCountList.get(i);
            int stringWidth = ColorUtils.coloredFontRendererUnicode.getStringWidth(string);
            float xOffset = mc.window.getScaledWidth() / 2 - 90 - stringWidth;
            int yOffset = mc.window.getScaledHeight() - 16 * i - 32;

            if (!string.isEmpty())
            {
                GlStateManager.disableDepthTest();
                ColorUtils.coloredFontRendererUnicode.drawWithShadow(ColorUtils.stringToRGB(ExtendedConfig.instance.arrowCountColor).toColoredFont() + string, xOffset, yOffset, 16777215);
                GlStateManager.enableDepthTest();
            }
        }

        // right arrow count stuff
        for (int i = 0; i < rightArrowCountList.size(); ++i)
        {
            String string = rightArrowCountList.get(i);
            float xOffset = mc.window.getScaledWidth() / 2 + 104;
            int yOffset = mc.window.getScaledHeight() - 16 * i - 32;

            if (!string.isEmpty())
            {
                GlStateManager.disableDepthTest();
                ColorUtils.coloredFontRendererUnicode.drawWithShadow(ColorUtils.stringToRGB(ExtendedConfig.instance.arrowCountColor).toColoredFont() + string, xOffset, yOffset, 16777215);
                GlStateManager.enableDepthTest();
            }
        }
    }

    public static void renderPotionHUD(MinecraftClient mc)
    {
        boolean iconAndTime = ExtendedConfig.instance.potionHUDStyle == StatusEffects.Style.ICON_AND_TIME;
        boolean right = ExtendedConfig.instance.potionHUDPosition == StatusEffects.Position.RIGHT;
        boolean showIcon = ExtendedConfig.instance.potionHUDIcon;
        int size = ExtendedConfig.instance.maximumPotionDisplay;
        int length = ExtendedConfig.instance.potionLengthYOffset;
        int lengthOverlap = ExtendedConfig.instance.potionLengthYOffsetOverlap;
        Collection<StatusEffectInstance> collection = mc.player.getStatusEffects();
        int xPotion;
        int yPotion;

        if (ExtendedConfig.instance.potionHUDPosition == StatusEffects.Position.HOTBAR_LEFT)
        {
            xPotion = mc.window.getScaledWidth() / 2 - 91 - 35;
            yPotion = mc.window.getScaledHeight() - 46;
        }
        else if (ExtendedConfig.instance.potionHUDPosition == StatusEffects.Position.HOTBAR_RIGHT)
        {
            xPotion = mc.window.getScaledWidth() / 2 + 91 - 20;
            yPotion = mc.window.getScaledHeight() - 42;
        }
        else
        {
            xPotion = right ? mc.window.getScaledWidth() - 32 : -24;
            yPotion = mc.window.getScaledHeight() - 220 + ExtendedConfig.instance.potionHUDYOffset + 90;
        }

        if (!collection.isEmpty())
        {
            if (collection.size() > size)
            {
                length = lengthOverlap / (collection.size() - 1);
            }

            for (StatusEffectInstance potioneffect : Ordering.natural().sortedCopy(collection))
            {
                float alpha = 1.0F;
                StatusEffect potion = potioneffect.getEffectType();
                String s = StatusEffectUtil.durationToString(potioneffect, 1.0F);
                String s1 = LangUtils.translate(potion.getTranslationKey());

                if (!potioneffect.isAmbient() && potioneffect.getDuration() <= 200)
                {
                    int j1 = 10 - potioneffect.getDuration() / 20;
                    alpha = MathHelper.clamp(potioneffect.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos(potioneffect.getDuration() * (float)Math.PI / 5.0F) * MathHelper.clamp(j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                }

                GlStateManager.enableBlend();
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, alpha);

                if (showIcon)
                {
                    method_18642(mc, xPotion, yPotion, collection);//TODO
                    method_18643(mc, xPotion, yPotion, collection);

                    //                    //potion.renderHUDEffect(potioneffect, mc.inGameHud, xPotion, yPotion, mc.inGameHud.zLevel, alpha);TODO
                    //                    potion.renderHUDEffect(xPotion, yPotion, potioneffect, mc, alpha);
                    //                    int index = potion.getStatusIconIndex();
                    //
                    //                    if (potionPos.equalsIgnoreCase("hotbar_left"))
                    //                    {
                    //                        mc.inGameHud.drawTexturedModalRect(xPotion + 12, yPotion + 6, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
                    //                    }
                    //                    else if (potionPos.equalsIgnoreCase("hotbar_right"))
                    //                    {
                    //                        mc.inGameHud.drawTexturedModalRect(xPotion + 24, yPotion + 6, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
                    //                    }
                    //                    else
                    //                    {
                    //                        mc.inGameHud.drawTexturedModalRect(right ? xPotion + 12 : xPotion + 28, yPotion + 6, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
                    //                    }
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
                        ColorUtils.coloredFontRenderer.drawWithShadow(s1, xPotion + xOffset - stringwidth2, yPotion + 6, ExtendedConfig.instance.alternatePotionHUDTextColor ? potion.getColor() : 16777215);
                    }
                    ColorUtils.coloredFontRenderer.drawWithShadow(s, xPotion + xOffset - stringwidth1, yPotion + yOffset, ExtendedConfig.instance.alternatePotionHUDTextColor ? potion.getColor() : 16777215);
                }
                else if (ExtendedConfig.instance.potionHUDPosition == StatusEffects.Position.HOTBAR_RIGHT)
                {
                    int xOffset = showIcon ? 46 : 28;

                    if (!iconAndTime)
                    {
                        ColorUtils.coloredFontRenderer.drawWithShadow(s1, xPotion + xOffset, yPotion + 6, ExtendedConfig.instance.alternatePotionHUDTextColor ? potion.getColor() : 16777215);
                    }
                    ColorUtils.coloredFontRenderer.drawWithShadow(s, xPotion + xOffset, yPotion + yOffset, ExtendedConfig.instance.alternatePotionHUDTextColor ? potion.getColor() : 16777215);
                }
                else
                {
                    int leftXOffset = showIcon ? 50 : 28;
                    int rightXOffset = showIcon ? 8 : 28;

                    if (!iconAndTime)
                    {
                        ColorUtils.coloredFontRenderer.drawWithShadow(s1, right ? xPotion + rightXOffset - stringwidth2 : xPotion + leftXOffset, yPotion + 6, ExtendedConfig.instance.alternatePotionHUDTextColor ? potion.getColor() : 16777215);
                    }
                    ColorUtils.coloredFontRenderer.drawWithShadow(s, right ? xPotion + rightXOffset - stringwidth1 : xPotion + leftXOffset, yPotion + yOffset, ExtendedConfig.instance.alternatePotionHUDTextColor ? potion.getColor() : 16777215);
                }
                yPotion -= length;
            }
        }
    }

    private static void method_18642(MinecraftClient mc, int int_1, int int_2, Iterable<StatusEffectInstance> iterable_1)
    {
        mc.getTextureManager().bindTexture(AbstractContainerScreen.BACKGROUND_TEXTURE);
        int int_3 = 0;

        for (Iterator<StatusEffectInstance> var5 = iterable_1.iterator(); var5.hasNext(); int_3 += int_2)
        {
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            DrawableHelper.blit(int_1, int_3, 0, 166, 140, 32, 256, 256);
        }
    }

    private static void method_18643(MinecraftClient mc, int int_1, int int_2, Iterable<StatusEffectInstance> iterable_1)
    {
        mc.getTextureManager().bindTexture(SpriteAtlasTexture.STATUS_EFFECT_ATLAS_TEX);
        StatusEffectSpriteManager sprite = mc.getStatusEffectSpriteManager();
        int int_3 = 0;
        int blitOffset = 0;

        for (Iterator<StatusEffectInstance> var6 = iterable_1.iterator(); var6.hasNext(); int_3 += int_2)
        {
            StatusEffectInstance statusEffectInstance_1 = var6.next();
            StatusEffect statusEffect_1 = statusEffectInstance_1.getEffectType();
            DrawableHelper.blit(int_1 + 6, int_3 + 7, blitOffset, 18, 18, sprite.getSprite(statusEffect_1));
        }
    }

    static String getArmorDurabilityStatus(ItemStack itemStack)
    {
        switch (ExtendedConfig.instance.equipmentStatus)
        {
        case DAMAGE_AND_MAX_DAMAGE:
        default:
            return itemStack.getDurability() - itemStack.getDamage() + "/" + itemStack.getDurability();
        case PERCENT:
            return HUDInfo.calculateItemDurabilityPercent(itemStack) + "%";
        case ONLY_DAMAGE:
            return String.valueOf(itemStack.getDurability() - itemStack.getDamage());
        case NONE:
            return "";
        }
    }

    private static int calculateItemDurabilityPercent(ItemStack itemStack)
    {
        return itemStack.getDurability() <= 0 ? 0 : 100 - itemStack.getDamage() * 100 / itemStack.getDurability();
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
        GuiLighting.enableForItems();
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        MinecraftClient.getInstance().getItemRenderer().renderGuiItem(itemStack, x, y);
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GuiLighting.disable();

        if (itemStack.hasDurability())
        {
            GuiLighting.enableForItems();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.disableLighting();
            GlStateManager.enableCull();
            MinecraftClient.getInstance().getItemRenderer().renderGuiItemOverlay(ColorUtils.coloredFontRenderer, itemStack, x, y);
            GlStateManager.blendFunc(770, 771);
            GlStateManager.disableLighting();
        }
    }

    static String getInventoryItemCount(PlayerInventory inventory, ItemStack other)
    {
        int count = 0;

        for (int i = 0; i < inventory.getInvSize(); i++)
        {
            ItemStack playerItems = inventory.getInvStack(i);

            if (!playerItems.isEmpty() && playerItems.getItem() == other.getItem() && playerItems.getDamage() == other.getDamage() && ItemStack.areTagsEqual(playerItems, other))
            {
                count += playerItems.getAmount();
            }
        }
        return String.valueOf(count);
    }

    static int getInventoryArrowCount(PlayerInventory inventory)
    {
        int arrowCount = 0;

        for (int i = 0; i < inventory.getInvSize(); ++i)
        {
            ItemStack itemStack = inventory.getInvStack(i);

            if (!itemStack.isEmpty() && itemStack.getItem() instanceof ArrowItem)
            {
                arrowCount += itemStack.getAmount();
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