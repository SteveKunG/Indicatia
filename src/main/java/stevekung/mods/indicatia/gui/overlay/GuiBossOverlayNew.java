package stevekung.mods.indicatia.gui.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import stevekung.mods.indicatia.core.IndicatiaMod;

public class GuiBossOverlayNew extends GuiBossOverlay
{
    private final Minecraft mc;
    private static final ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");

    public GuiBossOverlayNew()
    {
        super(Minecraft.getInstance());
        this.mc = Minecraft.getInstance();
    }

    @Override
    public void renderBossHealth()
    {
        if (!this.mc.ingameGUI.getBossOverlay().mapBossInfos.isEmpty())
        {
            int i = this.mc.mainWindow.getScaledWidth();
            int j = 12;

            for (BossInfoClient bossInfo : this.mc.ingameGUI.getBossOverlay().mapBossInfos.values())
            {
                int k = i / 2 - 91;
                RenderGameOverlayEvent.BossInfo event = ForgeHooksClient.bossBarRenderPre(this.mc.mainWindow, bossInfo, k, j, 10 + this.mc.fontRenderer.FONT_HEIGHT);

                if (!event.isCanceled())
                {
                    if (IndicatiaMod.INSTANCE.getConfig().getOrElse("enableBossHealthBarRender", true))
                    {
                        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                        this.mc.getTextureManager().bindTexture(GUI_BARS_TEXTURES);
                        this.render(k, j, bossInfo);
                    }
                    String s = bossInfo.getName().getFormattedText();
                    this.mc.fontRenderer.drawStringWithShadow(s, i / 2 - this.mc.fontRenderer.getStringWidth(s) / 2, j - 9, 16777215);
                }

                j += !IndicatiaMod.INSTANCE.getConfig().getOrElse("enableBossHealthBarRender", true) ? 12 : event.getIncrement();
                ForgeHooksClient.bossBarRenderPost(this.mc.mainWindow);

                if (!IndicatiaMod.INSTANCE.getConfig().getOrElse("enableBossHealthBarRender", true) ? j >= this.mc.mainWindow.getScaledHeight() / 4.5D : j >= this.mc.mainWindow.getScaledHeight() / 3)
                {
                    break;
                }
            }
        }
    }
}