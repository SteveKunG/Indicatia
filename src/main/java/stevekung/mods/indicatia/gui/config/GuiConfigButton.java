package stevekung.mods.indicatia.gui.config;

import com.mojang.blaze3d.platform.GlStateManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;

@Environment(EnvType.CLIENT)
public abstract class GuiConfigButton extends ButtonWidget
{
    private String comment;

    GuiConfigButton(int x, int y, int width, String text)
    {
        super(x, y, width, 20, text, null);
    }

    GuiConfigButton(int x, int y, int width, String text, String comment)
    {
        super(x, y, width, 20, text, null);
        this.comment = comment;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            MinecraftClient mc = MinecraftClient.getInstance();
            mc.getTextureManager().bindTexture(WIDGETS_LOCATION);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int state = this.getYImage(this.isHovered);
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.blit(this.x, this.y, 0, 46 + state * 20, this.width / 2, this.height);
            this.blit(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + state * 20, this.width / 2, this.height);
            this.renderBg(mc, mouseX, mouseY);
            int color = 14737632;

            if (!this.active)
            {
                color = 10526880;
            }
            else if (this.isHovered)
            {
                color = 16777120;
            }

            if (this.getMessage().length() > 24)
            {
                //TODO
                //mc.getFontResourceManager().forceUnicodeFont = true;
            }

            this.drawCenteredString(mc.textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, color);

            if (this.getMessage().length() > 24)
            {
                //mc.getFontResourceManager().forceUnicodeFont = false;
            }
        }
    }

    String getComment()
    {
        return this.comment;
    }
}