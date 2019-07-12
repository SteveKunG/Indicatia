package stevekung.mods.indicatia.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import stevekung.mods.indicatia.config.ConfigManagerIN;

@Mixin(GuiPlayerTabOverlay.class)
public abstract class GuiPlayerTabOverlayMixin extends Gui
{
    @Shadow
    @Final
    private Minecraft mc;

    @Overwrite
    protected void drawPing(int x1, int x2, int y, NetworkPlayerInfo info)
    {
        int ping = info.getResponseTime();

        if (ConfigManagerIN.indicatia_general.enableCustomPlayerList)
        {
            TextFormatting color = TextFormatting.GREEN;

            if (ping >= 200 && ping < 300)
            {
                color = TextFormatting.YELLOW;
            }
            else if (ping >= 300 && ping < 500)
            {
                color = TextFormatting.RED;
            }
            else if (ping >= 500)
            {
                color = TextFormatting.DARK_RED;
            }
            String pingText = String.valueOf(ping);
            this.mc.fontRenderer.drawString(color + pingText, x1 + x2 - this.mc.fontRenderer.getStringWidth(pingText), y + 0.625F, 0, true);
        }
        else
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(Gui.ICONS);
            int state;

            if (ping < 0)
            {
                state = 5;
            }
            else if (ping < 150)
            {
                state = 0;
            }
            else if (ping < 300)
            {
                state = 1;
            }
            else if (ping < 600)
            {
                state = 2;
            }
            else if (ping < 1000)
            {
                state = 3;
            }
            else
            {
                state = 4;
            }
            this.zLevel += 100.0F;
            this.drawTexturedModalRect(x2 + x1 - 11, y, 0, 176 + state * 8, 10, 8);
            this.zLevel -= 100.0F;
        }
    }
}