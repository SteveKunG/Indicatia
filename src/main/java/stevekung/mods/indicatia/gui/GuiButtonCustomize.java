package stevekung.mods.indicatia.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
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
    private final String tooltips;
    public final String command;
    public String group;
    private static int buttonId = 1000;

    public GuiButtonCustomize(int xPos, int yPos, GuiScreen parent, String tooltips, String command, String group, boolean isPlay)
    {
        super(buttonId++, xPos, yPos, 20, 20, "");
        this.isPlay = isPlay;
        this.parent = parent;
        this.tooltips = tooltips;
        this.group = group;
        this.command = isPlay ? "/play " + command : "/lobby " + command;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            mc.getTextureManager().bindTexture(this.isPlay ? GuiButtonCustomize.play : GuiButtonCustomize.main);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            Gui.drawModalRectWithCustomSizedTexture(this.xPosition, this.yPosition, flag ? 20 : 0, 0, this.width, this.height, 40, 20);
        }
    }

    public void drawRegion(int mouseX, int mouseY)
    {
        if (this.visible)
        {
            boolean isHover = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            GlStateManager.disableDepth();

            if (isHover)
            {
                int k = 0;
                int l = IndicatiaMod.MC.fontRendererObj.getStringWidth(this.tooltips);
                k = l;
                int i1 = mouseX + 12;
                int j1 = mouseY - 12;
                int k1 = 8;
                int l1 = -267386864;
                int i2 = 1347420415;
                int i3 = i2 & 16711422;
                int i4 = i2 & -16777216;
                int j2 = i3 >> 1 | i4;

            if (i1 + k > this.parent.width)
            {
                i1 -= 28 + k;
            }

            this.zLevel = 300.0F;
            this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
            this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
            this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
            this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
            this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
            this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
            this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
            this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
            this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);
            IndicatiaMod.MC.fontRendererObj.drawStringWithShadow(this.tooltips, i1, j1, -1);
            this.zLevel = 0.0F;
            GlStateManager.enableDepth();
            }
        }
    }
}