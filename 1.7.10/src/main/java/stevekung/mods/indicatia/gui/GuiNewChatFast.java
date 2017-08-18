package stevekung.mods.indicatia.gui;

import java.util.Iterator;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.core.IndicatiaMod;

@SideOnly(Side.CLIENT)
public class GuiNewChatFast extends GuiNewChat
{
    public GuiNewChatFast()
    {
        super(IndicatiaMod.MC);
    }

    @Override
    public void drawChat(int updateCounter)
    {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN)
        {
            int j = this.func_146232_i();
            boolean flag = false;
            int k = 0;
            int l = this.field_146253_i.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (l > 0)
            {
                if (this.getChatOpen())
                {
                    flag = true;
                }

                float f1 = this.func_146244_h();
                int i1 = MathHelper.ceiling_float_int(this.func_146228_f() / f1);
                GL11.glPushMatrix();
                GL11.glTranslatef(2.0F, 8.0F, 0.0F);
                GL11.glScalef(f1, f1, 1.0F);
                int j1;
                int k1;
                int i2;

                for (j1 = 0; j1 + this.field_146250_j < this.field_146253_i.size() && j1 < j; ++j1)
                {
                    ChatLine chatline = (ChatLine)this.field_146253_i.get(j1 + this.field_146250_j);

                    if (chatline != null)
                    {
                        k1 = updateCounter - chatline.getUpdatedCounter();

                        if (k1 < 200 || flag)
                        {
                            double d0 = k1 / 200.0D;
                            d0 = 1.0D - d0;
                            d0 *= 10.0D;

                            if (d0 < 0.0D)
                            {
                                d0 = 0.0D;
                            }
                            if (d0 > 1.0D)
                            {
                                d0 = 1.0D;
                            }

                            d0 *= d0;
                            i2 = (int)(255.0D * d0);

                            if (flag)
                            {
                                i2 = 255;
                            }

                            i2 = (int)(i2 * f);
                            ++k;

                            if (i2 > 3)
                            {
                                byte b0 = 0;
                                int j2 = -j1 * 9;

                                if (!ConfigManager.enableFastChatRender)
                                {
                                    Gui.drawRect(b0, j2 - 9, b0 + i1 + 4, j2, i2 / 2 << 24);
                                }

                                GL11.glEnable(GL11.GL_BLEND);
                                String s = chatline.func_151461_a().getFormattedText();
                                this.mc.fontRenderer.drawStringWithShadow(s, b0, j2 - 8, 16777215 + (i2 << 24));
                                GL11.glDisable(GL11.GL_ALPHA_TEST);
                            }
                        }
                    }
                }
                if (flag)
                {
                    j1 = this.mc.fontRenderer.FONT_HEIGHT;
                    GL11.glTranslatef(-3.0F, 0.0F, 0.0F);
                    int k2 = l * j1 + l;
                    k1 = k * j1 + k;
                    int l2 = this.field_146250_j * k1 / l;
                    int l1 = k1 * k1 / k2;

                    if (k2 != k1)
                    {
                        i2 = l2 > 0 ? 170 : 96;
                        int i3 = this.field_146251_k ? 13382451 : 3355562;
                        Gui.drawRect(0, -l2, 2, -l2 - l1, i3 + (i2 << 24));
                        Gui.drawRect(2, -l2, 1, -l2 - l1, 13421772 + (i2 << 24));
                    }
                }
                GL11.glPopMatrix();
            }
        }
    }

    @Override
    @Nullable
    public IChatComponent func_146236_a(int mouseX, int mouseY)
    {
        if (!this.getChatOpen())
        {
            return null;
        }
        else
        {
            ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int i = scaledresolution.getScaleFactor();
            float f = this.func_146244_h();
            int j = mouseX / i - 2;
            int k = mouseY / i - 40;
            j = MathHelper.floor_float(j / f);
            k = MathHelper.floor_float(k / f);

            if (j >= 0 && k >= 0)
            {
                int l = Math.min(this.func_146232_i(), this.field_146253_i.size());

                if (j <= MathHelper.floor_float(this.func_146228_f() / this.func_146244_h()) && k < this.mc.fontRenderer.FONT_HEIGHT * l + l)
                {
                    int i1 = k / this.mc.fontRenderer.FONT_HEIGHT + this.field_146250_j;

                    if (i1 >= 0 && i1 < this.field_146253_i.size())
                    {
                        ChatLine chatline = (ChatLine) this.field_146253_i.get(i1);
                        int j1 = 0;
                        Iterator iterator = chatline.func_151461_a().iterator();

                        while (iterator.hasNext())
                        {
                            IChatComponent ichatcomponent = (IChatComponent)iterator.next();

                            if (ichatcomponent instanceof ChatComponentText)
                            {
                                j1 += this.mc.fontRenderer.getStringWidth(this.func_146235_b(((ChatComponentText)ichatcomponent).getChatComponentText_TextValue()));

                                if (j1 > l)
                                {
                                    return ichatcomponent;
                                }
                            }
                        }
                    }
                    return null;
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
    }
}