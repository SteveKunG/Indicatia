package stevekung.mods.indicatia.gui.exconfig.screen;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import stevekung.mods.indicatia.config.Equipments;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.event.HUDRenderEventHandler;
import stevekung.mods.indicatia.renderer.HUDInfo;
import stevekung.mods.indicatia.renderer.KeystrokeRenderer;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.stevekungslib.client.event.ClientEventHandler;
import stevekung.mods.stevekungslib.utils.ColorUtils;
import stevekung.mods.stevekungslib.utils.JsonUtils;

@OnlyIn(Dist.CLIENT)
public class RenderPreviewScreen extends Screen
{
    private final Screen parent;
    private final String type;

    private static String overallTPS = "";
    private static String overworldTPS = "";
    private static String tps = "";
    private static List<String> allDimensionTPS = new LinkedList<>();

    RenderPreviewScreen(Screen parent, String type)
    {
        super(JsonUtils.create("Render Preview " + type));
        this.parent = parent;
        this.type = type;
    }

    @Override
    public void onClose()
    {
        this.minecraft.displayGuiScreen(this.parent);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if (this.type.equals("offset"))
        {
            KeystrokeRenderer.render(this.minecraft);
            HUDInfo.renderPotionHUD(this.minecraft);

            if (ExtendedConfig.instance.equipmentDirection == Equipments.Direction.VERTICAL)
            {
                HUDInfo.renderVerticalEquippedItems(this.minecraft);
            }
            else
            {
                HUDInfo.renderHorizontalEquippedItems(this.minecraft);
            }
        }
        if (this.type.equals("render_info"))
        {
            List<String> leftInfo = new LinkedList<>();
            List<String> rightInfo = new LinkedList<>();
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            HUDInfo.renderVerticalEquippedItems(this.minecraft);

            // left info
            if (!this.minecraft.isSingleplayer())
            {
                leftInfo.add(HUDInfo.getPing());
                leftInfo.add(HUDInfo.getPingToSecond());

                if (this.minecraft.isConnectedToRealms())
                {
                    leftInfo.add(HUDInfo.getRealmName(this.minecraft));
                }
                if (this.minecraft.getCurrentServerData() != null)
                {
                    leftInfo.add(HUDInfo.getServerIP(this.minecraft));
                }
            }

            leftInfo.add(HUDInfo.getFPS());
            leftInfo.add(HUDInfo.getXYZ(this.minecraft));

            if (this.minecraft.player.dimension == DimensionType.THE_NETHER)
            {
                leftInfo.add(HUDInfo.getOverworldXYZFromNether(this.minecraft));
            }

            leftInfo.add(HUDInfo.renderDirection(this.minecraft));
            leftInfo.add(HUDInfo.getBiome(this.minecraft));

            if (this.minecraft.player.dimension == DimensionType.OVERWORLD)
            {
                String isSlimeChunk = InfoUtils.INSTANCE.isSlimeChunk(this.minecraft.player.getPosition()) ? "Yes" : "No";
                leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.instance.slimeChunkColor).toColoredFont() + "Slime Chunk: " + ColorUtils.stringToRGB(ExtendedConfig.instance.slimeChunkValueColor).toColoredFont() + isSlimeChunk);
            }

            leftInfo.add(HUDInfo.getCPS());
            leftInfo.add(HUDInfo.getRCPS());

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

            // server tps
            if (ExtendedConfig.instance.tps && server != null)
            {
                if (ClientEventHandler.ticks % 50 == 0)
                {
                    double overallTPS = HUDRenderEventHandler.mean(server.tickTimeArray) * 1.0E-6D;
                    double overworldTPS = HUDRenderEventHandler.mean(server.getTickTime(DimensionType.OVERWORLD)) * 1.0E-6D;
                    double tps = Math.min(1000.0D / overallTPS, 20);

                    RenderPreviewScreen.overallTPS = HUDRenderEventHandler.tpsFormat.format(overallTPS);
                    RenderPreviewScreen.overworldTPS = "";
                    RenderPreviewScreen.allDimensionTPS.clear();

                    if (ExtendedConfig.instance.tpsAllDims)
                    {
                        for (DimensionType dimension : DimensionType.getAll())
                        {
                            long[] values = server.getTickTime(dimension);
                            String dimensionName = DimensionType.getKey(dimension).toString();

                            if (values == null)
                            {
                                return;
                            }
                            double dimensionTPS = HUDRenderEventHandler.mean(values) * 1.0E-6D;
                            RenderPreviewScreen.allDimensionTPS.add(ColorUtils.stringToRGB(ExtendedConfig.instance.tpsColor).toColoredFont() + "Dimension " + dimensionName.substring(dimensionName.indexOf(":") + 1) + ": " + ColorUtils.stringToRGB(ExtendedConfig.instance.tpsValueColor).toColoredFont() + HUDRenderEventHandler.tpsFormat.format(dimensionTPS));
                        }
                    }
                    else
                    {
                        RenderPreviewScreen.overworldTPS = HUDRenderEventHandler.tpsFormat.format(overworldTPS);
                    }
                    RenderPreviewScreen.tps = HUDRenderEventHandler.tpsFormat.format(tps);
                }
                // overall tps
                leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.instance.tpsColor).toColoredFont() + "Overall TPS: " + ColorUtils.stringToRGB(ExtendedConfig.instance.tpsValueColor).toColoredFont() + RenderPreviewScreen.overallTPS);
                // all dimension tps
                leftInfo.addAll(RenderPreviewScreen.allDimensionTPS);

                // overworld tps
                if (!RenderPreviewScreen.overworldTPS.isEmpty())
                {
                    leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.instance.tpsColor).toColoredFont() + "Overworld TPS: " + ColorUtils.stringToRGB(ExtendedConfig.instance.tpsValueColor).toColoredFont() + RenderPreviewScreen.overworldTPS);
                }

                // tps
                leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.instance.tpsColor).toColoredFont() + "TPS: " + ColorUtils.stringToRGB(ExtendedConfig.instance.tpsValueColor).toColoredFont() + RenderPreviewScreen.tps);
            }

            // right info
            rightInfo.add(HUDInfo.getCurrentTime());
            rightInfo.add(HUDInfo.getCurrentGameTime(this.minecraft));

            if (this.minecraft.world.isRaining())
            {
                rightInfo.add(HUDInfo.getGameWeather(this.minecraft));
            }

            rightInfo.add(InfoUtils.INSTANCE.getMoonPhase(this.minecraft));

            // left info
            for (int i = 0; i < leftInfo.size(); ++i)
            {
                String string = leftInfo.get(i);
                float fontHeight = ColorUtils.coloredFontRenderer.FONT_HEIGHT + 1;
                float yOffset = 3 + fontHeight * i;
                float xOffset = this.minecraft.mainWindow.getScaledWidth() - 2 - ColorUtils.coloredFontRenderer.getStringWidth(string);

                if (!StringUtils.isNullOrEmpty(string))
                {
                    ColorUtils.coloredFontRenderer.drawStringWithShadow(string, ExtendedConfig.instance.swapRenderInfo ? xOffset : 3.0625F, yOffset, 16777215);
                }
            }

            // right info
            for (int i = 0; i < rightInfo.size(); ++i)
            {
                String string = rightInfo.get(i);
                float fontHeight = ColorUtils.coloredFontRenderer.FONT_HEIGHT + 1;
                float yOffset = 3 + fontHeight * i;
                float xOffset = this.minecraft.mainWindow.getScaledWidth() - 2 - ColorUtils.coloredFontRenderer.getStringWidth(string);

                if (!StringUtils.isNullOrEmpty(string))
                {
                    ColorUtils.coloredFontRenderer.drawStringWithShadow(string, ExtendedConfig.instance.swapRenderInfo ? 3.0625F : xOffset, yOffset, 16777215);
                }
            }
        }
        if (this.type.equals("keystroke"))
        {
            KeystrokeRenderer.render(this.minecraft);
        }
    }

    @Override
    public boolean isPauseScreen()
    {
        return true;
    }
}