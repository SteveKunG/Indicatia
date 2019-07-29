package stevekung.mods.indicatia.gui.exconfig.screen;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.world.dimension.DimensionType;
import stevekung.mods.indicatia.config.Equipments;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.renderer.HUDInfo;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.stevekungslib.utils.ColorUtils;
import stevekung.mods.stevekungslib.utils.JsonUtils;

@Environment(EnvType.CLIENT)
public class RenderPreviewScreen extends Screen
{
    private final Screen parent;
    private final String type;

    RenderPreviewScreen(Screen parent, String type)
    {
        super(JsonUtils.create("Render Preview " + type));
        this.parent = parent;
        this.type = type;
    }

    @Override
    public void onClose()
    {
        this.minecraft.openScreen(this.parent);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if (this.type.equals("offset"))
        {
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
            HUDInfo.renderVerticalEquippedItems(this.minecraft);

            // left info
            if (!this.minecraft.isInSingleplayer())
            {
                leftInfo.add(HUDInfo.getPing());
                leftInfo.add(HUDInfo.getPingToSecond());

                if (this.minecraft.getServer() != null)
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
                String isSlimeChunk = InfoUtils.INSTANCE.isSlimeChunk(this.minecraft.player.getBlockPos()) ? "Yes" : "No";
                leftInfo.add(ColorUtils.stringToRGB(ExtendedConfig.instance.slimeChunkColor).toColoredFont() + "Slime Chunk: " + ColorUtils.stringToRGB(ExtendedConfig.instance.slimeChunkValueColor).toColoredFont() + isSlimeChunk);
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
                float fontHeight = this.minecraft.textRenderer.fontHeight + 1;
                float yOffset = 3 + fontHeight * i;
                float xOffset = this.minecraft.window.getScaledWidth() - 2 - this.minecraft.textRenderer.getStringWidth(string);

                if (!StringUtils.isEmpty(string))
                {
                    this.minecraft.textRenderer.drawWithShadow(string, ExtendedConfig.instance.swapRenderInfo ? xOffset : 3.0625F, yOffset, 16777215);
                }
            }

            // right info
            for (int i = 0; i < rightInfo.size(); ++i)
            {
                String string = rightInfo.get(i);
                float fontHeight = this.minecraft.textRenderer.fontHeight + 1;
                float yOffset = 3 + fontHeight * i;
                float xOffset = this.minecraft.window.getScaledWidth() - 2 - this.minecraft.textRenderer.getStringWidth(string);

                if (!StringUtils.isEmpty(string))
                {
                    this.minecraft.textRenderer.drawWithShadow(string, ExtendedConfig.instance.swapRenderInfo ? 3.0625F : xOffset, yOffset, 16777215);
                }
            }
        }
    }

    @Override
    public boolean isPauseScreen()
    {
        return true;
    }
}