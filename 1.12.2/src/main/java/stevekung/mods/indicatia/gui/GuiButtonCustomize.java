package stevekung.mods.indicatia.gui;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.core.IndicatiaMod;

@SideOnly(Side.CLIENT)
public class GuiButtonCustomize extends GuiButton
{
    private static final ResourceLocation main = new ResourceLocation("indicatia:textures/gui/main_lobby.png");
    private static final ResourceLocation play = new ResourceLocation("indicatia:textures/gui/play_icon.png");
    private final boolean isPlay;
    private final GuiScreen parent;
    private final List<String> tooltips;
    public final String command;

    public GuiButtonCustomize(int buttonID, int xPos, int yPos, GuiScreen parent, List<String> tooltips, String command, boolean isPlay)
    {
        super(buttonID, xPos, yPos, 20, 20, "");
        this.isPlay = isPlay;
        this.parent = parent;
        this.tooltips = tooltips;
        this.command = isPlay ? "/play " + command : "/lobby " + command;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            mc.getTextureManager().bindTexture(this.isPlay ? GuiButtonCustomize.play : GuiButtonCustomize.main);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            Gui.drawModalRectWithCustomSizedTexture(this.x, this.y, flag ? 20 : 0, 0, this.width, this.height, 40, 20);
        }
    }

    public void drawRegion(int mouseX, int mouseY)
    {
        if (this.visible)
        {
            boolean isHover = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int state = this.getHoverState(isHover);

            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();

            if (this.tooltips != null && !this.tooltips.isEmpty() && isHover)
            {
                int k = 0;
                Iterator<String> iterator = this.tooltips.iterator();

                while (iterator.hasNext())
                {
                    String s = iterator.next();
                    int l = IndicatiaMod.MC.fontRenderer.getStringWidth(s);

                    if (l > k)
                    {
                        k = l;
                    }
                }

                int i1 = mouseX + 12;
                int j1 = mouseY - 12;
                int k1 = 8;

                if (this.tooltips.size() > 1)
                {
                    k1 += (this.tooltips.size() - 1) * 10;
                }
                if (i1 + k > this.parent.width)
                {
                    i1 -= 28 + k;
                }

                this.zLevel = 300.0F;
                int l1 = -267386864;
                this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
                this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
                this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
                this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
                this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
                int i2 = 1347420415;
                int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
                this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
                this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
                this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
                this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

                for (int k2 = 0; k2 < this.tooltips.size(); ++k2)
                {
                    String s1 = this.tooltips.get(k2);
                    IndicatiaMod.MC.fontRenderer.drawStringWithShadow(s1, i1, j1, -1);
                    j1 += 10;
                }
                this.zLevel = 0.0F;
            }
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
}