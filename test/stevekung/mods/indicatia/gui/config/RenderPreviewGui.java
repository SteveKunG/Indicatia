package stevekung.mods.indicatia.gui.config;

import com.google.common.base.Strings;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.dimension.DimensionType;
import stevekung.mods.indicatia.config.EnumEquipment;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.renderer.HUDInfo;
import stevekung.mods.indicatia.renderer.KeystrokeRenderer;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.stevekunglib.utils.ColorUtils;

import java.util.LinkedList;
import java.util.List;

public class RenderPreviewGui extends Gui
{
    private final Gui parent;
    private final String type;

    RenderPreviewGui(Gui parent, String type)
    {
        this.parent = parent;
        this.type = type;
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode)
    {
        if (keyCode == 1)
        {
            this.client.openGui(this.parent);
        }
        return super.charTyped(typedChar, keyCode);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks)
    {
        if (this.type.equals("offset"))
        {
            KeystrokeRenderer.render(this.client);
            HUDInfo.renderPotionHUD(this.client);

            if (EnumEquipment.Direction.getById(ExtendedConfig.equipmentDirection).equalsIgnoreCase("vertical"))
            {
                HUDInfo.renderVerticalEquippedItems(this.client);
            }
            else
            {
                HUDInfo.renderHorizontalEquippedItems(this.client);
            }
        }
        if (this.type.equals("render_info"))
        {
            List<String> leftInfo = new LinkedList<>();
            List<String> rightInfo = new LinkedList<>();
//            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            HUDInfo.renderVerticalEquippedItems(this.client);

            // left info
            if (!this.client.method_1496())
            {
                leftInfo.add(HUDInfo.getPing());
                leftInfo.add(HUDInfo.getPingToSecond());

//                if (this.client.isConnectedToRealms())
//                {
//                    leftInfo.add(HUDInfo.getRealmName(this.client));
//                }
                if (this.client.getServer() != null)
                {
                    leftInfo.add(HUDInfo.getServerIP(this.client));
                }
            }

            leftInfo.add(HUDInfo.getFPS());
            leftInfo.add(HUDInfo.getXYZ(this.client));

            if (this.client.player.dimension == DimensionType.THE_NETHER)
            {
                leftInfo.add(HUDInfo.getOverworldXYZFromNether(this.client));
            }

            leftInfo.add(HUDInfo.renderDirection(this.client));
            leftInfo.add(HUDInfo.getBiome(this.client));

            if (this.client.player.dimension == DimensionType.OVERWORLD)
            {
                String isSlimeChunk = InfoUtils.INSTANCE.isSlimeChunk(this.client.player.getPos()) ? "Yes" : "No";
                leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.slimeChunkColor).toColoredFont() + "Slime Chunk: " + ColorUtils.stringToRGB(ExtendedConfig.slimeChunkValueColor).toColoredFont() + isSlimeChunk);
            }

            leftInfo.add(HUDInfo.getCPS());
            leftInfo.add(HUDInfo.getRCPS());

//            if (!HUDRenderEventHandler.topDonator.isEmpty())TODO
//            {
//                String text = ExtendedConfig.topDonatorText.isEmpty() ? "" : ExtendedConfig.topDonatorText + TextFormatting.RESET + " ";
//                leftInfo.add(text + HUDRenderEventHandler.topDonator);
//            }
//            if (!HUDRenderEventHandler.recentDonator.isEmpty())
//            {
//                String text = ExtendedConfig.recentDonatorText.isEmpty() ? "" : ExtendedConfig.recentDonatorText + TextFormatting.RESET + " ";
//                leftInfo.add(text + HUDRenderEventHandler.recentDonator);
//            }

//            // server tps TODO
//            if (server != null)
//            {
//                double overallTPS = HUDRenderEventHandler.mean(server.tickTimeArray) * 1.0E-6D;
//                double tps = Math.min(1000.0D / overallTPS, 20);
//
//                leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.tpsColor).toColoredFont() + "Overall TPS: " + ColorUtils.stringToRGB(ExtendedConfig.tpsValueColor).toColoredFont() + HUDRenderEventHandler.tpsFormat.format(overallTPS));
//
//                for (Integer dimensionIds : DimensionManager.getIDs())
//                {
//                    double dimensionTPS = HUDRenderEventHandler.mean(server.worldTickTimes.get(dimensionIds)) * 1.0E-6D;
//                    leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.tpsColor).toColoredFont() + "Dimension " + server.getWorld(dimensionIds).provider.getDimensionType().getName() + " " + dimensionIds + ": " + ColorUtils.stringToRGB(ExtendedConfig.tpsValueColor).toColoredFont() + HUDRenderEventHandler.tpsFormat.format(dimensionTPS));
//                }
//                leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.tpsColor).toColoredFont() + "TPS: " + ColorUtils.stringToRGB(ExtendedConfig.tpsValueColor).toColoredFont() + HUDRenderEventHandler.tpsFormat.format(tps));
//            }

            // right info
            rightInfo.add(HUDInfo.getCurrentTime());
            rightInfo.add(HUDInfo.getCurrentGameTime(this.client));

            if (this.client.world.isRaining())
            {
                rightInfo.add(HUDInfo.getGameWeather(this.client));
            }

            rightInfo.add(InfoUtils.INSTANCE.getMoonPhase(this.client));

            // left info
            for (int i = 0; i < leftInfo.size(); ++i)
            {
                String string = leftInfo.get(i);
                float fontHeight = ColorUtils.coloredFontRenderer.fontHeight + 1;
                float yOffset = 3 + fontHeight * i;
                float xOffset = this.client.window.getScaledWidth() - 2 - ColorUtils.coloredFontRenderer.getStringWidth(string);

                if (!Strings.isNullOrEmpty(string))
                {
                    ColorUtils.coloredFontRenderer.drawWithShadow(string, ExtendedConfig.swapRenderInfo ? xOffset : 3.0625F, yOffset, 16777215);
                }
            }

            // right info
            for (int i = 0; i < rightInfo.size(); ++i)
            {
                String string = rightInfo.get(i);
                float fontHeight = ColorUtils.coloredFontRenderer.fontHeight + 1;
                float yOffset = 3 + fontHeight * i;
                float xOffset = this.client.window.getScaledWidth() - 2 - ColorUtils.coloredFontRenderer.getStringWidth(string);

                if (!Strings.isNullOrEmpty(string))
                {
                    ColorUtils.coloredFontRenderer.drawWithShadow(string, ExtendedConfig.swapRenderInfo ? 3.0625F : xOffset, yOffset, 16777215);
                }
            }
        }
        if (this.type.equals("keystroke"))
        {
            KeystrokeRenderer.render(this.client);
        }
    }

    @Override
    public boolean isPauseScreen()
    {
        return true;
    }
}