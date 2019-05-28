package stevekung.mods.indicatia.mixin;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.world.dimension.DimensionType;
import stevekung.mods.indicatia.config.CPSPosition;
import stevekung.mods.indicatia.config.Equipments;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.renderer.HUDInfo;
import stevekung.mods.indicatia.renderer.KeystrokeRenderer;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.stevekungslib.utils.ColorUtils;

@Mixin(InGameHud.class)
public class IngameHUDRenderer
{
    @Inject(at = @At("RETURN"), method = "draw(F)V")
    public void draw(CallbackInfo info)
    {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (/*IndicatiaConfig.GENERAL.enableRenderInfo.get() && */!mc.options.hudHidden && !mc.options.debugEnabled && mc.player != null && mc.world != null/* && !(mc.currentScreen instanceof GuiRenderPreview)*/)
        {
            List<String> leftInfo = new LinkedList<>();
            List<String> rightInfo = new LinkedList<>();

            // left info
            if (ExtendedConfig.instance.ping && !mc.isInSingleplayer())
            {
                leftInfo.add(HUDInfo.getPing());

                if (ExtendedConfig.instance.pingToSecond)
                {
                    leftInfo.add(HUDInfo.getPingToSecond());
                }
            }
            if (ExtendedConfig.instance.serverIP && !mc.isInSingleplayer())
            {
                if (mc.getServer() != null)
                {
                    leftInfo.add(HUDInfo.getServerIP(mc));
                }
            }
            if (ExtendedConfig.instance.fps)
            {
                leftInfo.add(HUDInfo.getFPS());
            }
            if (ExtendedConfig.instance.xyz)
            {
                leftInfo.add(HUDInfo.getXYZ(mc));

                if (mc.player.dimension == DimensionType.THE_NETHER)
                {
                    leftInfo.add(HUDInfo.getOverworldXYZFromNether(mc));
                }
            }
            if (ExtendedConfig.instance.direction)
            {
                leftInfo.add(HUDInfo.renderDirection(mc));
            }
            if (ExtendedConfig.instance.biome)
            {
                leftInfo.add(HUDInfo.getBiome(mc));
            }
            if (ExtendedConfig.instance.slimeChunkFinder && mc.player.dimension == DimensionType.OVERWORLD)
            {
                String isSlimeChunk = InfoUtils.INSTANCE.isSlimeChunk(mc.player.getBlockPos()) ? "Yes" : "No";
                leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.instance.slimeChunkColor).toColoredFont() + "Slime Chunk: " + ColorUtils.stringToRGB(ExtendedConfig.instance.slimeChunkValueColor).toColoredFont() + isSlimeChunk);
            }
            if (ExtendedConfig.instance.cpsPosition == CPSPosition.LEFT)
            {
                if (ExtendedConfig.instance.cps)
                {
                    leftInfo.add(HUDInfo.getCPS());
                }
                if (ExtendedConfig.instance.rcps)
                {
                    leftInfo.add(HUDInfo.getRCPS());
                }
            }
            /*if (IndicatiaConfig.GENERAL.donatorMessagePosition.get() == IndicatiaConfig.DonatorMessagePos.LEFT)
        {
            if (!HUDRenderEventHandler.topDonator.isEmpty())
            {
                String text = ExtendedConfig.instance.topDonatorText.isEmpty() ? "" : ExtendedConfig.instance.topDonatorText + TextFormatting.RESET + " ";
                leftInfo.add(text + HUDRenderEventHandler.topDonator);
            }
            if (!HUDRenderEventHandler.recentDonator.isEmpty())
            {
                String text = ExtendedConfig.instance.recentDonatorText.isEmpty() ? "" : ExtendedConfig.instance.recentDonatorText + TextFormatting.RESET + " ";
                leftInfo.add(text + HUDRenderEventHandler.recentDonator);
            }
        }*/

            // right info
            if (ExtendedConfig.instance.realTime)
            {
                rightInfo.add(HUDInfo.getCurrentTime());
            }
            if (ExtendedConfig.instance.gameTime)
            {
                rightInfo.add(HUDInfo.getCurrentGameTime(mc));
            }
            if (ExtendedConfig.instance.gameWeather && mc.world.isRaining())
            {
                rightInfo.add(HUDInfo.getGameWeather(mc));
            }
            if (ExtendedConfig.instance.moonPhase)
            {
                rightInfo.add(InfoUtils.INSTANCE.getMoonPhase(mc));
            }
            if (ExtendedConfig.instance.cpsPosition == CPSPosition.RIGHT)
            {
                if (ExtendedConfig.instance.cps)
                {
                    rightInfo.add(HUDInfo.getCPS());
                }
                if (ExtendedConfig.instance.rcps)
                {
                    rightInfo.add(HUDInfo.getRCPS());
                }
            }
            /*if (IndicatiaConfig.GENERAL.donatorMessagePosition.get() == IndicatiaConfig.DonatorMessagePos.RIGHT)
        {
            if (!HUDRenderEventHandler.topDonator.isEmpty())
            {
                String text = ExtendedConfig.instance.topDonatorText.isEmpty() ? "" : ExtendedConfig.instance.topDonatorText + TextFormatting.RESET + " ";
                rightInfo.add(text + HUDRenderEventHandler.topDonator);
            }
            if (!HUDRenderEventHandler.recentDonator.isEmpty())
            {
                String text = ExtendedConfig.instance.recentDonatorText.isEmpty() ? "" : ExtendedConfig.instance.recentDonatorText + TextFormatting.RESET + " ";
                rightInfo.add(text + HUDRenderEventHandler.recentDonator);
            }
        }
        if (IndicatiaMod.isYoutubeChatLoaded && !HUDRenderEventHandler.currentLiveViewCount.isEmpty())
        {
            rightInfo.add(ColorUtils.stringToRGB(ExtendedConfig.instance.ytChatViewCountColor).toColoredFont() + "Current watched: " + ColorUtils.stringToRGB(ExtendedConfig.instance.ytChatViewCountValueColor).toColoredFont() + HUDRenderEventHandler.currentLiveViewCount);
        }*/

            // equipments
            if (!mc.player.isSpectator() && ExtendedConfig.instance.equipmentHUD)
            {
                if (ExtendedConfig.instance.equipmentPosition == Equipments.Position.HOTBAR)
                {
                    HUDInfo.renderHotbarEquippedItems(mc);
                }
                else
                {
                    if (ExtendedConfig.instance.equipmentDirection == Equipments.Direction.VERTICAL)
                    {
                        HUDInfo.renderVerticalEquippedItems(mc);
                    }
                    else
                    {
                        HUDInfo.renderHorizontalEquippedItems(mc);
                    }
                }
            }

            if (ExtendedConfig.instance.potionHUD)
            {
                HUDInfo.renderPotionHUD(mc);
            }

            // left info
            for (int i = 0; i < leftInfo.size(); ++i)
            {
                String string = leftInfo.get(i);
                float fontHeight = ColorUtils.coloredFontRenderer.fontHeight + 1;
                float yOffset = 3 + fontHeight * i;
                float xOffset = mc.window.getScaledWidth() - 2 - ColorUtils.coloredFontRenderer.getStringWidth(string);

                if (!StringUtils.isEmpty(string))
                {
                    ColorUtils.coloredFontRenderer.drawWithShadow(string, ExtendedConfig.instance.swapRenderInfo ? xOffset : 3.0625F, yOffset, 16777215);
                }
            }

            // right info
            for (int i = 0; i < rightInfo.size(); ++i)
            {
                String string = rightInfo.get(i);
                float fontHeight = ColorUtils.coloredFontRenderer.fontHeight + 1;
                float yOffset = 3 + fontHeight * i;
                float xOffset = mc.window.getScaledWidth() - 2 - ColorUtils.coloredFontRenderer.getStringWidth(string);

                if (!StringUtils.isEmpty(string))
                {
                    ColorUtils.coloredFontRenderer.drawWithShadow(string, ExtendedConfig.instance.swapRenderInfo ? 3.0625F : xOffset, yOffset, 16777215);
                }
            }

            /*if (HUDRenderEventHandler.recordEnable)
        {
            int color = 16777215;

            if (this.recTick % 24 >= 0 && this.recTick % 24 <= 12)
            {
                color = 16733525;
            }
            ColorUtils.coloredFontRenderer.drawStringWithShadow("REC: " + StringUtils.ticksToElapsedTime(this.recTick), mc.window.getScaledWidth() - ColorUtils.coloredFontRenderer.getStringWidth("REC: " + StringUtils.ticksToElapsedTime(this.recTick)) - 2, mc.window.getScaledHeight() - 10, color);
        }*/

            if (!mc.options.hudHidden && !mc.options.debugEnabled)
            {
                if (ExtendedConfig.instance.keystroke)
                {
                    if (mc.currentScreen == null || mc.currentScreen instanceof ChatScreen)
                    {
                        KeystrokeRenderer.render(mc);
                    }
                }
                /*if (IndicatiaConfig.GENERAL.enableRenderInfo.get() && ExtendedConfig.instance.cps && CPSPosition.getById(ExtendedConfig.instance.cpsPosition).equalsIgnoreCase("custom") && (mc.currentScreen == null || mc.currentScreen instanceof GuiChat))
            {
                String rcps = ExtendedConfig.instance.rcps ? " " + HUDInfo.getRCPS() : "";
                RenderUtilsIN.drawRect(ExtendedConfig.instance.cpsCustomXOffset, ExtendedConfig.instance.cpsCustomYOffset, ExtendedConfig.instance.cpsCustomXOffset + mc.fontRenderer.getStringWidth(HUDInfo.getCPS() + rcps) + 4, ExtendedConfig.instance.cpsCustomYOffset + 11, 16777216, (float)ExtendedConfig.instance.cpsOpacity / 100.0F);
                mc.fontRenderer.drawStringWithShadow(HUDInfo.getCPS() + rcps, ExtendedConfig.instance.cpsCustomXOffset + 2, ExtendedConfig.instance.cpsCustomYOffset + 2, 16777215);
            }*/
            }
        }
    }
}

//        MinecraftClient mc = MinecraftClient.getInstance();
//        List<String> leftInfo = new LinkedList<>();
//        List<String> rightInfo = new LinkedList<>();
//
//        if (!mc.options.field_1842 && !mc.options.debugEnabled && mc.player != null && mc.world != null)
//        {
//            // left info
//            if (ExtendedConfig.instance.ping && !mc.method_1496())
//            {
//                leftInfo.add(HUDInfo.getPing());
//
//                if (ExtendedConfig.instance.pingToSecond)
//                {
//                    leftInfo.add(HUDInfo.getPingToSecond());
//                }
//            }
//            if (ExtendedConfig.instance.serverIP && !mc.method_1496())
//            {
//                //                    if (mc.isConnectedToRealms())
//                //                    {
//                //                        leftInfo.add(HUDInfo.getRealmName(mc));
//                //                    }
//                if (mc.getServer() != null)
//                {
//                    leftInfo.add(HUDInfo.getServerIP(mc));
//                }
//            }
//            if (ExtendedConfig.instance.fps)
//            {
//                leftInfo.add(HUDInfo.getFPS());
//            }
//            if (ExtendedConfig.instance.xyz)
//            {
//                leftInfo.add(HUDInfo.getXYZ(mc));
//
//                if (mc.player.dimension == DimensionType.THE_NETHER)
//                {
//                    leftInfo.add(HUDInfo.getOverworldXYZFromNether(mc));
//                }
//            }
//            if (ExtendedConfig.instance.direction)
//            {
//                leftInfo.add(HUDInfo.renderDirection(mc));
//            }
//            if (ExtendedConfig.instance.biome)
//            {
//                leftInfo.add(HUDInfo.getBiome(mc));
//            }
//            if (ExtendedConfig.instance.slimeChunkFinder && mc.player.dimension == DimensionType.OVERWORLD)
//            {
//                String isSlimeChunk = InfoUtils.INSTANCE.isSlimeChunk(mc.player.getPos()) ? "Yes" : "No";
//                leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.instance.slimeChunkColor).toColoredFont() + "Slime Chunk: " + ColorUtils.stringToRGB(ExtendedConfig.instance.slimeChunkValueColor).toColoredFont() + isSlimeChunk);
//            }
//            if (CPSPosition.getById(ExtendedConfig.instance.cpsPosition).equalsIgnoreCase("left"))
//            {
//                if (ExtendedConfig.instance.cps)
//                {
//                    leftInfo.add(HUDInfo.getCPS());
//                }
//                if (ExtendedConfig.instance.rcps)
//                {
//                    leftInfo.add(HUDInfo.getRCPS());
//                }
//            }
//            //if (ConfigManagerIN.indicatia_donation.donatorMessagePosition == ConfigManagerIN.Donation.DonatorMessagePos.LEFT)TODO
//            //        {
//            //            if (!HUDRenderEventHandler.topDonator.isEmpty())
//            //            {
//            //                String text = ExtendedConfig.instance.topDonatorText.isEmpty() ? "" : ExtendedConfig.instance.topDonatorText + TextFormat.RESET + " ";
//            //                leftInfo.add(text + HUDRenderEventHandler.topDonator);
//            //            }
//            //            if (!HUDRenderEventHandler.recentDonator.isEmpty())
//            //            {
//            //                String text = ExtendedConfig.instance.recentDonatorText.isEmpty() ? "" : ExtendedConfig.instance.recentDonatorText + TextFormat.RESET + " ";
//            //                leftInfo.add(text + HUDRenderEventHandler.recentDonator);
//            //            }
//            //        }
//            // right info
//            if (ExtendedConfig.instance.realTime)
//            {
//                rightInfo.add(HUDInfo.getCurrentTime());
//            }
//            if (ExtendedConfig.instance.gameTime)
//            {
//                rightInfo.add(HUDInfo.getCurrentGameTime(mc));
//            }
//            if (ExtendedConfig.instance.gameWeather && mc.world.isRaining())
//            {
//                rightInfo.add(HUDInfo.getGameWeather(mc));
//            }
//            if (ExtendedConfig.instance.moonPhase)
//            {
//                rightInfo.add(InfoUtils.INSTANCE.getMoonPhase(mc));
//            }
//            if (CPSPosition.getById(ExtendedConfig.instance.cpsPosition).equalsIgnoreCase("right"))
//            {
//                if (ExtendedConfig.instance.cps)
//                {
//                    rightInfo.add(HUDInfo.getCPS());
//                }
//                if (ExtendedConfig.instance.rcps)
//                {
//                    rightInfo.add(HUDInfo.getRCPS());
//                }
//            }
//
//            // equipments
//            if (!mc.player.isSpectator() && ExtendedConfig.instance.equipmentHUD)
//            {
//                if (EnumEquipment.Position.getById(ExtendedConfig.instance.equipmentPosition).equalsIgnoreCase("hotbar"))
//                {
//                    HUDInfo.renderHotbarEquippedItems(mc);
//                }
//                else
//                {
//                    if (EnumEquipment.Direction.getById(ExtendedConfig.instance.equipmentDirection).equalsIgnoreCase("vertical"))
//                    {
//                        HUDInfo.renderVerticalEquippedItems(mc);
//                    }
//                    else
//                    {
//                        HUDInfo.renderHorizontalEquippedItems(mc);
//                    }
//                }
//            }
//
//            if (ExtendedConfig.instance.potionHUD)
//            {
//                HUDInfo.renderPotionHUD(mc);
//            }
//
//            // left info
//            for (int i = 0; i < leftInfo.size(); ++i)
//            {
//                String string = leftInfo.get(i);
//                float fontHeight = ColorUtils.coloredFontRenderer.fontHeight + 1;
//                float yOffset = 3 + fontHeight * i;
//                float xOffset = mc.window.getScaledWidth() - 2 - ColorUtils.coloredFontRenderer.getStringWidth(string);
//
//                if (!Strings.isNullOrEmpty(string))
//                {
//                    ColorUtils.coloredFontRenderer.drawWithShadow(string, ExtendedConfig.instance.swapRenderInfo ? xOffset : 3.0625F, yOffset, 16777215);
//                }
//            }
//
//            // right info
//            for (int i = 0; i < rightInfo.size(); ++i)
//            {
//                String string = rightInfo.get(i);
//                float fontHeight = ColorUtils.coloredFontRenderer.fontHeight + 1;
//                float yOffset = 3 + fontHeight * i;
//                float xOffset = mc.window.getScaledWidth() - 2 - ColorUtils.coloredFontRenderer.getStringWidth(string);
//
//                if (!Strings.isNullOrEmpty(string))
//                {
//                    ColorUtils.coloredFontRenderer.drawWithShadow(string, ExtendedConfig.instance.swapRenderInfo ? 3.0625F : xOffset, yOffset, 16777215);
//                }
//            }
//        }
