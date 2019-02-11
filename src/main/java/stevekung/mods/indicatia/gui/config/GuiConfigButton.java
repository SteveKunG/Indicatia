package stevekung.mods.indicatia.gui.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class GuiConfigButton extends GuiButton
{
    private String comment;

    GuiConfigButton(int id, int x, int y, int width, String text)
    {
        super(id, x, y, width, 20, text);
    }

    GuiConfigButton(int id, int x, int y, int width, String text, String comment)
    {
        super(id, x, y, width, 20, text);
        this.comment = comment;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            Minecraft mc = Minecraft.getInstance();
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int state = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + state * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + state * 20, this.width / 2, this.height);
            this.renderBg(mc, mouseX, mouseY);
            int color = 14737632;

            if (this.packedFGColor != 0)
            {
                color = this.packedFGColor;
            }
            else
            {
                if (!this.enabled)
                {
                    color = 10526880;
                }
                else if (this.hovered)
                {
                    color = 16777120;
                }
            }

            if (this.displayString.length() > 24)
            {
                //TODO
                //mc.getFontResourceManager().forceUnicodeFont = true;
            }

            this.drawCenteredString(mc.fontRenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, color);

            if (this.displayString.length() > 24)
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