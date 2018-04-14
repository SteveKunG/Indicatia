package stevekung.mods.indicatia.gui.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import stevekung.mods.indicatia.config.ConfigManagerIN;

public class GuiBossOverlayNew extends GuiBossOverlay
{
    public GuiBossOverlayNew()
    {
        super(Minecraft.getMinecraft());
    }

    @Override
    public void renderBossHealth()
    {
        if (!this.client.ingameGUI.getBossOverlay().mapBossInfos.isEmpty())
        {
            ScaledResolution scaledresolution = new ScaledResolution(this.client);
            int i = scaledresolution.getScaledWidth();
            int j = 12;

            for (BossInfoClient bossInfo : this.client.ingameGUI.getBossOverlay().mapBossInfos.values())
            {
                int k = i / 2 - 91;
                RenderGameOverlayEvent.BossInfo event = ForgeHooksClient.bossBarRenderPre(scaledresolution, bossInfo, k, j, 10 + this.client.fontRenderer.FONT_HEIGHT);

                if (!event.isCanceled())
                {
                    if (ConfigManagerIN.indicatia_general.enableRenderBossHealthBar)
                    {
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                        this.client.getTextureManager().bindTexture(GuiBossOverlay.GUI_BARS_TEXTURES);
                        this.render(k, j, bossInfo);
                    }
                    String s = bossInfo.getName().getFormattedText();
                    this.client.fontRenderer.drawStringWithShadow(s, i / 2 - this.client.fontRenderer.getStringWidth(s) / 2, j - 9, 16777215);
                }

                j += !ConfigManagerIN.indicatia_general.enableRenderBossHealthBar ? 12 : event.getIncrement();
                ForgeHooksClient.bossBarRenderPost(scaledresolution);

                if (!ConfigManagerIN.indicatia_general.enableRenderBossHealthBar ? j >= scaledresolution.getScaledHeight() / 4.5D : j >= scaledresolution.getScaledHeight() / 3)
                {
                    break;
                }
            }
        }
    }
}