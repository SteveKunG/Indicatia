package stevekung.mods.indicatia.gui.overlay;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ClientBossInfo;
import net.minecraft.client.gui.overlay.BossOverlayGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import stevekung.mods.indicatia.config.IndicatiaConfig;

@OnlyIn(Dist.CLIENT)
public class GuiBossOverlayNew extends BossOverlayGui
{
    private final Minecraft mc;
    private static final ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");

    public GuiBossOverlayNew()
    {
        super(Minecraft.getInstance());
        this.mc = Minecraft.getInstance();
    }

    @Override
    public void render()
    {
        if (!this.mc.field_71456_v.getBossOverlay().mapBossInfos.isEmpty())
        {
            int i = this.mc.mainWindow.getScaledWidth();
            int j = 12;

            for (ClientBossInfo bossInfo : this.mc.field_71456_v.getBossOverlay().mapBossInfos.values())
            {
                int k = i / 2 - 91;
                RenderGameOverlayEvent.BossInfo event = ForgeHooksClient.bossBarRenderPre(this.mc.mainWindow, bossInfo, k, j, 10 + this.mc.fontRenderer.FONT_HEIGHT);

                if (!event.isCanceled())
                {
                    if (IndicatiaConfig.GENERAL.enableBossHealthBarRender.get())
                    {
                        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                        this.mc.getTextureManager().bindTexture(GUI_BARS_TEXTURES);
                        this.render(k, j, bossInfo);
                    }
                    String s = bossInfo.getName().getFormattedText();
                    this.mc.fontRenderer.drawStringWithShadow(s, i / 2 - this.mc.fontRenderer.getStringWidth(s) / 2, j - 9, 16777215);
                }

                j += !IndicatiaConfig.GENERAL.enableBossHealthBarRender.get() ? 12 : event.getIncrement();
                ForgeHooksClient.bossBarRenderPost(this.mc.mainWindow);

                if (!IndicatiaConfig.GENERAL.enableBossHealthBarRender.get() ? j >= this.mc.mainWindow.getScaledHeight() / 4.5D : j >= this.mc.mainWindow.getScaledHeight() / 3)
                {
                    break;
                }
            }
        }
    }
}