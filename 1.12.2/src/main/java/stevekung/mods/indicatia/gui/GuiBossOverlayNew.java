package stevekung.mods.indicatia.gui;

import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.core.IndicatiaMod;

public class GuiBossOverlayNew extends GuiBossOverlay
{
    public GuiBossOverlayNew()
    {
        super(IndicatiaMod.MC);
    }

    @Override
    public void renderBossHealth()
    {
        if (!this.client.ingameGUI.getBossOverlay().mapBossInfos.isEmpty())
        {
            ScaledResolution scaledresolution = new ScaledResolution(this.client);
            int i = scaledresolution.getScaledWidth();
            int j = 12;

            for (BossInfoClient bossinfolerping : this.client.ingameGUI.getBossOverlay().mapBossInfos.values())
            {
                int k = i / 2 - 91;
                RenderGameOverlayEvent.BossInfo event = ForgeHooksClient.bossBarRenderPre(scaledresolution, bossinfolerping, k, j, 10 + this.client.fontRenderer.FONT_HEIGHT);

                if (!event.isCanceled())
                {
                    if (ConfigManager.enableRenderBossHealthBar)
                    {
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                        this.client.getTextureManager().bindTexture(GuiBossOverlay.GUI_BARS_TEXTURES);
                        this.render(k, j, bossinfolerping);
                    }
                    String s = bossinfolerping.getName().getFormattedText();
                    this.client.fontRenderer.drawStringWithShadow(s, i / 2 - this.client.fontRenderer.getStringWidth(s) / 2, j - 9, 16777215);
                }

                j += !ConfigManager.enableRenderBossHealthBar ? 12 : event.getIncrement();
                ForgeHooksClient.bossBarRenderPost(scaledresolution);

                if (!ConfigManager.enableRenderBossHealthBar ? j >= scaledresolution.getScaledHeight() / 4.5D : j >= scaledresolution.getScaledHeight() / 3)
                {
                    break;
                }
            }
        }
    }
}