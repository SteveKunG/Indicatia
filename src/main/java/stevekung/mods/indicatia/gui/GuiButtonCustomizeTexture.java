package stevekung.mods.indicatia.gui;

import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiButtonCustomizeTexture extends Button
{
    private final String texture;
    private final Screen parent;
    private final List<String> tooltips;

    GuiButtonCustomizeTexture(int xPos, int yPos, Screen parent, List<String> tooltips, String texture, Button.IPressable button)
    {
        super(xPos, yPos, 20, 20, "", button);
        this.parent = parent;
        this.tooltips = tooltips;
        this.texture = texture;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("indicatia:textures/gui/" + this.texture + ".png"));
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            AbstractGui.blit(this.x, this.y, flag ? 20 : 0, 0, this.width, this.height, 40, 20);
        }
    }

    void drawRegion(int mouseX, int mouseY)
    {
        if (this.visible)
        {
            boolean isHover = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GlStateManager.disableDepthTest();

            if (this.tooltips != null && !this.tooltips.isEmpty() && isHover)
            {
                int k = 0;

                for (String s : this.tooltips)
                {
                    int l = Minecraft.getInstance().fontRenderer.getStringWidth(s);

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

                this.blitOffset = 300;
                int l1 = -267386864;
                this.blit(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
                this.blit(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
                this.blit(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
                this.blit(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
                this.blit(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
                int i2 = 1347420415;
                int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
                this.blit(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
                this.blit(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
                this.blit(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
                this.blit(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

                for (String s1 : this.tooltips)
                {
                    Minecraft.getInstance().fontRenderer.drawStringWithShadow(s1, i1, j1, -1);
                    j1 += 10;
                }
                this.blitOffset = 0;
            }
            GlStateManager.enableDepthTest();
        }
    }
}