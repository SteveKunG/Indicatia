package stevekung.mods.indicatia.gui;

import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import stevekung.mods.indicatia.core.IndicatiaMod;

@SideOnly(Side.CLIENT)
public class GuiChangeLogSlot extends GuiSlot
{
    private List<String> stringList;
    private GuiFullChangeLog parent;
    private boolean textureType;
    private static final ResourceLocation STONEBRICK = new ResourceLocation("textures/blocks/stonebrick.png");
    private static final ResourceLocation ANDESITE = new ResourceLocation("textures/blocks/stone_andesite_smooth.png");

    public GuiChangeLogSlot(Minecraft mc, GuiFullChangeLog parent, List<String> stringList, int width, int height, boolean texture)
    {
        super(mc, width, height, 32, height - 64, 13);
        this.stringList = stringList;
        this.parent = parent;
        this.textureType = texture;
        this.setShowSelectionBox(false);
    }

    @Override
    protected int getSize()
    {
        return this.stringList.size();
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {}

    @Override
    protected boolean isSelected(int slotIndex)
    {
        return false;
    }

    @Override
    protected int getContentHeight()
    {
        return 5 + this.getSize() * 13;
    }

    @Override
    protected void drawBackground() {}

    @Override
    protected void drawSlot(int entryID, int insideLeft, int yPos, int insideSlotHeight, Tessellator tessellator, int mouseXIn, int mouseYIn)
    {
        String test = this.stringList.get(entryID);
        this.mc.fontRenderer.drawStringWithShadow(test, insideLeft - 20, yPos + 2, 16777215);
    }

    @Override
    protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha)
    {
        this.parent.drawCenteredString(this.mc.fontRenderer, "Indicatia " + IndicatiaMod.VERSION + " Change Log", this.width / 2, 16, 16777215);
        Tessellator tessellator = Tessellator.instance;
        this.mc.getTextureManager().bindTexture(this.textureType ? GuiChangeLogSlot.STONEBRICK : GuiChangeLogSlot.ANDESITE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(4210752, endAlpha);
        tessellator.addVertexWithUV(this.left, endY, 0.0D, 0.0D, endY / f);
        tessellator.addVertexWithUV(this.left + this.width, endY, 0.0D, this.width / f, endY / f);
        tessellator.setColorRGBA_I(4210752, startAlpha);
        tessellator.addVertexWithUV(this.left + this.width, startY, 0.0D, this.width / f, startY / f);
        tessellator.addVertexWithUV(this.left, startY, 0.0D, 0.0D, startY / f);
        tessellator.draw();
    }

    @Override
    protected void drawContainerBackground(Tessellator tessellator) {}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        //        if (this.field_178041_q)
        //        {
        //            this.mouseX = mouseX;
        //            this.mouseY = mouseY;
        //            this.drawBackground();
        //            int i = this.getScrollBarX();
        //            int j = i + 6;
        //            this.bindAmountScrolled();
        //            GlStateManager.disableLighting();
        //            GlStateManager.disableFog();
        //            Tessellator tessellator = Tessellator.getInstance();
        //            WorldRenderer vertexbuffer = tessellator.getWorldRenderer();
        //            this.drawContainerBackground(tessellator);
        //            int k = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
        //            int l = this.top + 4 - (int)this.amountScrolled;
        //
        //            if (this.hasListHeader)
        //            {
        //                this.drawListHeader(k, l, tessellator);
        //            }
        //
        //            this.drawSelectionBox(k, l, mouseX, mouseY);
        //            GlStateManager.disableDepth();
        //            this.overlayBackground(0, this.top, 255, 255);
        //            this.overlayBackground(this.bottom, this.height, 255, 255);
        //            GlStateManager.enableBlend();
        //            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        //            GlStateManager.disableAlpha();
        //            GlStateManager.shadeModel(7425);
        //            GlStateManager.disableTexture2D();
        //            vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        //            vertexbuffer.pos(this.left, this.top + 4, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 0).endVertex();
        //            vertexbuffer.pos(this.right, this.top + 4, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 0).endVertex();
        //            vertexbuffer.pos(this.right, this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 100).endVertex();
        //            vertexbuffer.pos(this.left, this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 100).endVertex();
        //            tessellator.draw();
        //            vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        //            vertexbuffer.pos(this.left, this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 100).endVertex();
        //            vertexbuffer.pos(this.right, this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 100).endVertex();
        //            vertexbuffer.pos(this.right, this.bottom - 4, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 0).endVertex();
        //            vertexbuffer.pos(this.left, this.bottom - 4, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 0).endVertex();
        //            tessellator.draw();
        //            int j1 = this.func_148135_f();
        //
        //            if (j1 > 0)
        //            {
        //                int k1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
        //                k1 = MathHelper.clamp_int(k1, 32, this.bottom - this.top - 8);
        //                int l1 = (int)this.amountScrolled * (this.bottom - this.top - k1) / j1 + this.top;
        //
        //                if (l1 < this.top)
        //                {
        //                    l1 = this.top;
        //                }
        //                vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        //                vertexbuffer.pos(i, l1 + k1, 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 100).endVertex();
        //                vertexbuffer.pos(j, l1 + k1, 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 100).endVertex();
        //                vertexbuffer.pos(j, l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 100).endVertex();
        //                vertexbuffer.pos(i, l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 100).endVertex();
        //                tessellator.draw();
        //                vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        //                vertexbuffer.pos(i, l1 + k1 - 1, 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 100).endVertex();
        //                vertexbuffer.pos(j - 1, l1 + k1 - 1, 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 100).endVertex();
        //                vertexbuffer.pos(j - 1, l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 100).endVertex();
        //                vertexbuffer.pos(i, l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 100).endVertex();
        //                tessellator.draw();
        //            }
        //            this.func_148142_b(mouseX, mouseY);
        //            GlStateManager.enableTexture2D();
        //            GlStateManager.shadeModel(7424);
        //            GlStateManager.enableAlpha();
        //            GlStateManager.disableBlend();
        //        }

        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.drawBackground();
        int k = this.getSize();
        int l = this.getScrollBarX();
        int i1 = l + 6;
        int l1;
        int i2;
        int k2;
        int i3;

        if (mouseX > this.left && mouseX < this.right && mouseY > this.top && mouseY < this.bottom)
        {
            if (Mouse.isButtonDown(0) && this.func_148125_i())
            {
                if (this.initialClickY == -1.0F)
                {
                    boolean flag1 = true;

                    if (mouseY >= this.top && mouseY <= this.bottom)
                    {
                        int k1 = this.width / 2 - this.getListWidth() / 2;
                        l1 = this.width / 2 + this.getListWidth() / 2;
                        i2 = mouseY - this.top - this.headerPadding + this.getAmountScrolled() - 4;
                        int j2 = i2 / this.slotHeight;

                        if (mouseX >= k1 && mouseX <= l1 && j2 >= 0 && i2 >= 0 && j2 < k)
                        {
                            boolean flag = j2 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                            this.elementClicked(j2, flag, mouseX, mouseY);
                            this.selectedElement = j2;
                            this.lastClicked = Minecraft.getSystemTime();
                        }
                        else if (mouseX >= k1 && mouseX <= l1 && i2 < 0)
                        {
                            this.func_148132_a(mouseX - k1, mouseY - this.top + this.getAmountScrolled() - 4);
                            flag1 = false;
                        }

                        if (mouseX >= l && mouseX <= i1)
                        {
                            this.scrollMultiplier = -1.0F;
                            i3 = this.func_148135_f();

                            if (i3 < 1)
                            {
                                i3 = 1;
                            }

                            k2 = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getContentHeight());

                            if (k2 < 32)
                            {
                                k2 = 32;
                            }
                            if (k2 > this.bottom - this.top - 8)
                            {
                                k2 = this.bottom - this.top - 8;
                            }
                            this.scrollMultiplier /= (float)(this.bottom - this.top - k2) / (float)i3;
                        }
                        else
                        {
                            this.scrollMultiplier = 1.0F;
                        }

                        if (flag1)
                        {
                            this.initialClickY = mouseY;
                        }
                        else
                        {
                            this.initialClickY = -2.0F;
                        }
                    }
                    else
                    {
                        this.initialClickY = -2.0F;
                    }
                }
                else if (this.initialClickY >= 0.0F)
                {
                    this.amountScrolled -= (mouseY - this.initialClickY) * this.scrollMultiplier;
                    this.initialClickY = mouseY;
                }
            }
            else
            {
                for (; !this.mc.gameSettings.touchscreen && Mouse.next(); this.mc.currentScreen.handleMouseInput())
                {
                    int j1 = Mouse.getEventDWheel();

                    if (j1 != 0)
                    {
                        if (j1 > 0)
                        {
                            j1 = -1;
                        }
                        else if (j1 < 0)
                        {
                            j1 = 1;
                        }
                        this.amountScrolled += j1 * this.slotHeight / 2;
                    }
                }
                this.initialClickY = -1.0F;
            }
        }

        this.bindAmountScrolled();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        this.drawContainerBackground(tessellator);
        l1 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
        i2 = this.top + 4 - this.getAmountScrolled();

        if (this.hasListHeader)
        {
            this.drawListHeader(l1, i2, tessellator);
        }

        this.drawSelectionBox(l1, i2, mouseX, mouseY);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        byte b0 = 4;
        this.overlayBackground(0, this.top, 255, 255);
        this.overlayBackground(this.bottom, this.height, 255, 255);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 0, 1);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV(this.left, this.top + b0, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(this.right, this.top + b0, 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV(this.right, this.top, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(this.left, this.top, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV(this.left, this.bottom, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(this.right, this.bottom, 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV(this.right, this.bottom - b0, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(this.left, this.bottom - b0, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        i3 = this.func_148135_f();

        if (i3 > 0)
        {
            k2 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();

            if (k2 < 32)
            {
                k2 = 32;
            }
            if (k2 > this.bottom - this.top - 8)
            {
                k2 = this.bottom - this.top - 8;
            }

            int l2 = this.getAmountScrolled() * (this.bottom - this.top - k2) / i3 + this.top;

            if (l2 < this.top)
            {
                l2 = this.top;
            }
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0, 255);
            tessellator.addVertexWithUV(l, this.bottom, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(i1, this.bottom, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(i1, this.top, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(l, this.top, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(8421504, 255);
            tessellator.addVertexWithUV(l, l2 + k2, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(i1, l2 + k2, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(i1, l2, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(l, l2, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(12632256, 255);
            tessellator.addVertexWithUV(l, l2 + k2 - 1, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(i1 - 1, l2 + k2 - 1, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(i1 - 1, l2, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(l, l2, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
        }
        this.func_148142_b(mouseX, mouseY);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }
}