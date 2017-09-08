package stevekung.mods.indicatia.gui;

import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.util.HideNameData;

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
            int i = this.getLineCount();
            int j = this.drawnChatLines.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (j > 0)
            {
                boolean flag = false;

                if (this.getChatOpen())
                {
                    flag = true;
                }

                float f1 = this.getChatScale();
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 8.0F, 0.0F);
                GlStateManager.scale(f1, f1, 1.0F);
                int l = 0;

                for (int i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; ++i1)
                {
                    ChatLine chatline = this.drawnChatLines.get(i1 + this.scrollPos);

                    if (chatline != null)
                    {
                        int j1 = updateCounter - chatline.getUpdatedCounter();

                        if (j1 < 200 || flag)
                        {
                            double d0 = j1 / 200.0D;
                            d0 = 1.0D - d0;
                            d0 = d0 * 10.0D;
                            d0 = MathHelper.clamp_double(d0, 0.0D, 1.0D);
                            d0 = d0 * d0;
                            int l1 = (int)(255.0D * d0);

                            if (flag)
                            {
                                l1 = 255;
                            }

                            l1 = (int)(l1 * f);
                            ++l;

                            if (l1 > 3)
                            {
                                int j2 = -i1 * 9;
                                int k = MathHelper.ceiling_float_int(this.getChatWidth() / f1);
                                String text = chatline.getChatComponent().getFormattedText();

                                if (!ConfigManager.enableFastChatRender)
                                {
                                    Gui.drawRect(-2, j2 - 9, 0 + k + 4, j2, l1 / 2 << 24);
                                }

                                for (String hide : HideNameData.getHideNameList())
                                {
                                    if (text.contains(hide))
                                    {
                                        text = text.replace(hide, TextFormatting.OBFUSCATED + hide + TextFormatting.RESET);
                                    }
                                }

                                GlStateManager.enableBlend();
                                this.mc.fontRendererObj.drawStringWithShadow(text, 0.0F, j2 - 8, 16777215 + (l1 << 24));
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }
                if (flag)
                {
                    int k2 = this.mc.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                    int l2 = j * k2 + j;
                    int i3 = l * k2 + l;
                    int j3 = this.scrollPos * i3 / j;
                    int k1 = i3 * i3 / l2;

                    if (l2 != i3)
                    {
                        int k3 = j3 > 0 ? 170 : 96;
                        int l3 = this.isScrolled ? 13382451 : 3355562;
                        Gui.drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
                        Gui.drawRect(2, -j3, 1, -j3 - k1, 13421772 + (k3 << 24));
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }
}