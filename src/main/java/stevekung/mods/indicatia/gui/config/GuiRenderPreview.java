package stevekung.mods.indicatia.gui.config;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import stevekung.mods.indicatia.config.EnumEquipment;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.event.HUDRenderEventHandler;
import stevekung.mods.indicatia.renderer.HUDInfo;
import stevekung.mods.indicatia.renderer.KeystrokeRenderer;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.stevekungslib.utils.ColorUtils;

@OnlyIn(Dist.CLIENT)
public class GuiRenderPreview extends GuiScreen
{
    private final GuiScreen parent;
    private final String type;

    GuiRenderPreview(GuiScreen parent, String type)
    {
        this.parent = parent;
        this.type = type;
    }

    @Override
    public void close()
    {
        this.mc.displayGuiScreen(this.parent);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if (this.type.equals("offset"))
        {
            KeystrokeRenderer.render(this.mc);
            HUDInfo.renderPotionHUD(this.mc);

            if (EnumEquipment.Direction.getById(ExtendedConfig.equipmentDirection).equalsIgnoreCase("vertical"))
            {
                HUDInfo.renderVerticalEquippedItems(this.mc);
            }
            else
            {
                HUDInfo.renderHorizontalEquippedItems(this.mc);
            }
        }
        if (this.type.equals("render_info"))
        {
            List<String> leftInfo = new LinkedList<>();
            List<String> rightInfo = new LinkedList<>();
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            HUDInfo.renderVerticalEquippedItems(this.mc);

            // left info
            if (!this.mc.isSingleplayer())
            {
                leftInfo.add(HUDInfo.getPing());
                leftInfo.add(HUDInfo.getPingToSecond());

                if (this.mc.isConnectedToRealms())
                {
                    leftInfo.add(HUDInfo.getRealmName(this.mc));
                }
                if (this.mc.getCurrentServerData() != null)
                {
                    leftInfo.add(HUDInfo.getServerIP(this.mc));
                }
            }

            leftInfo.add(HUDInfo.getFPS());
            leftInfo.add(HUDInfo.getXYZ(this.mc));

            if (this.mc.player.dimension == DimensionType.NETHER)
            {
                leftInfo.add(HUDInfo.getOverworldXYZFromNether(this.mc));
            }

            leftInfo.add(HUDInfo.renderDirection(this.mc));
            leftInfo.add(HUDInfo.getBiome(this.mc));

            if (this.mc.player.dimension == DimensionType.OVERWORLD)
            {
                String isSlimeChunk = InfoUtils.INSTANCE.isSlimeChunk(this.mc.player.getPosition()) ? "Yes" : "No";
                leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.slimeChunkColor).toColoredFont() + "Slime Chunk: " + ColorUtils.stringToRGB(ExtendedConfig.slimeChunkValueColor).toColoredFont() + isSlimeChunk);
            }

            leftInfo.add(HUDInfo.getCPS());
            leftInfo.add(HUDInfo.getRCPS());

            if (!HUDRenderEventHandler.topDonator.isEmpty())
            {
                String text = ExtendedConfig.topDonatorText.isEmpty() ? "" : ExtendedConfig.topDonatorText + TextFormatting.RESET + " ";
                leftInfo.add(text + HUDRenderEventHandler.topDonator);
            }
            if (!HUDRenderEventHandler.recentDonator.isEmpty())
            {
                String text = ExtendedConfig.recentDonatorText.isEmpty() ? "" : ExtendedConfig.recentDonatorText + TextFormatting.RESET + " ";
                leftInfo.add(text + HUDRenderEventHandler.recentDonator);
            }

            // server tps
            if (server != null)
            {
                double overallTPS = HUDRenderEventHandler.mean(server.tickTimeArray) * 1.0E-6D;
                double tps = Math.min(1000.0D / overallTPS, 20);

                leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.tpsColor).toColoredFont() + "Overall TPS: " + ColorUtils.stringToRGB(ExtendedConfig.tpsValueColor).toColoredFont() + HUDRenderEventHandler.tpsFormat.format(overallTPS));

                for (DimensionType dimension : DimensionType.func_212681_b())
                {
                    long[] values = server.getTickTime(dimension);

                    if (values == null)
                    {
                        return;
                    }
                    double dimensionTPS = HUDRenderEventHandler.mean(values) * 1.0E-6D;
                    leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.tpsColor).toColoredFont() + "Dimension " + server.getWorld(dimension).dimension.getType().toString() + ": " + ColorUtils.stringToRGB(ExtendedConfig.tpsValueColor).toColoredFont() + HUDRenderEventHandler.tpsFormat.format(dimensionTPS));
                }
                leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.tpsColor).toColoredFont() + "TPS: " + ColorUtils.stringToRGB(ExtendedConfig.tpsValueColor).toColoredFont() + HUDRenderEventHandler.tpsFormat.format(tps));
            }

            // right info
            rightInfo.add(HUDInfo.getCurrentTime());
            rightInfo.add(HUDInfo.getCurrentGameTime(this.mc));

            if (this.mc.world.isRaining())
            {
                rightInfo.add(HUDInfo.getGameWeather(this.mc));
            }

            rightInfo.add(InfoUtils.INSTANCE.getMoonPhase(this.mc));

            // left info
            for (int i = 0; i < leftInfo.size(); ++i)
            {
                String string = leftInfo.get(i);
                float fontHeight = ColorUtils.coloredFontRenderer.FONT_HEIGHT + 1;
                float yOffset = 3 + fontHeight * i;
                float xOffset = this.mc.mainWindow.getScaledWidth() - 2 - ColorUtils.coloredFontRenderer.getStringWidth(string);

                if (!StringUtils.isNullOrEmpty(string))
                {
                    ColorUtils.coloredFontRenderer.drawStringWithShadow(string, ExtendedConfig.swapRenderInfo ? xOffset : 3.0625F, yOffset, 16777215);
                }
            }

            // right info
            for (int i = 0; i < rightInfo.size(); ++i)
            {
                String string = rightInfo.get(i);
                float fontHeight = ColorUtils.coloredFontRenderer.FONT_HEIGHT + 1;
                float yOffset = 3 + fontHeight * i;
                float xOffset = this.mc.mainWindow.getScaledWidth() - 2 - ColorUtils.coloredFontRenderer.getStringWidth(string);

                if (!StringUtils.isNullOrEmpty(string))
                {
                    ColorUtils.coloredFontRenderer.drawStringWithShadow(string, ExtendedConfig.swapRenderInfo ? 3.0625F : xOffset, yOffset, 16777215);
                }
            }
        }
        if (this.type.equals("keystroke"))
        {
            KeystrokeRenderer.render(this.mc);
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return true;
    }
}