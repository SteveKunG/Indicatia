package stevekung.mods.indicatia.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiButtonMojangStatus extends Button
{
    private static final ResourceLocation MOJANG_TEXTURES = new ResourceLocation("indicatia:textures/gui/mojang.png");

    public GuiButtonMojangStatus(int xPos, int yPos, Button.IPressable button)
    {
        super(xPos, yPos, 20, 20, "", button);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            Minecraft.getInstance().getTextureManager().bindTexture(GuiButtonMojangStatus.MOJANG_TEXTURES);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            AbstractGui.blit(this.x, this.y, flag ? 20 : 0, 0, this.width, this.height, 40, 20);
        }
    }
}