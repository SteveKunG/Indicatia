package com.stevekung.indicatia.gui.widget;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.stevekung.stevekungslib.utils.JsonUtils;
import com.stevekung.stevekungslib.utils.client.RenderUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;

public class MinigameButton extends Button
{
    private static final ResourceLocation BLANK = new ResourceLocation("indicatia:textures/gui/blank.png");
    private static final ResourceLocation MAIN = new ResourceLocation("indicatia:textures/gui/main_lobby.png");
    private static final ResourceLocation PLAY = new ResourceLocation("indicatia:textures/gui/play_icon.png");
    private final Minecraft mc;
    private final boolean isPlay;
    private final String tooltips;
    private final ItemStack head;

    public MinigameButton(int parentWidth, String tooltips, boolean isPlay, Button.IPressable pressable, ItemStack head)
    {
        super(parentWidth - 130, 20, 20, 20, JsonUtils.create("Minigame Button"), pressable);
        this.isPlay = isPlay;
        this.tooltips = tooltips;
        this.mc = Minecraft.getInstance();
        this.head = head;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            RenderUtils.bindTexture(this.head.isEmpty() ? this.isPlay ? PLAY : MAIN : BLANK);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            AbstractGui.blit(matrixStack, this.x, this.y, flag ? 20 : 0, 0, this.width, this.height, 40, 20);

            if (!this.head.isEmpty())
            {
                RenderSystem.enableDepthTest();
                RenderSystem.enableRescaleNormal();
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(770, 771, 1, 0);
                RenderHelper.enableStandardItemLighting();
                RenderSystem.enableLighting();
                this.mc.getItemRenderer().renderItemAndEffectIntoGUI(this.head, this.x + 2, this.y + 2);
            }
        }
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY)
    {
        if (this.visible && this.isMouseOver(mouseX, mouseY))
        {
            this.renderToolTip(matrixStack, Lists.transform(Collections.singletonList(JsonUtils.create(this.tooltips)), ITextComponent::func_241878_f), mouseX, mouseY);//TODO
            //GuiUtils.drawHoveringText(matrixStack, Collections.singletonList(JsonUtils.create(this.tooltips)), mouseX, mouseY, this.mc.currentScreen.width, this.mc.currentScreen.height, 128, this.mc.fontRenderer);
            RenderSystem.disableLighting();
        }
    }

    @Deprecated
    private void renderToolTip(MatrixStack p_238654_1_, List<? extends IReorderingProcessor> p_238654_2_, int p_238654_3_, int p_238654_4_)
    {
        if (!p_238654_2_.isEmpty()) {
            int i = 0;

            for(IReorderingProcessor ireorderingprocessor : p_238654_2_) {
                int j = this.mc.fontRenderer.func_243245_a(ireorderingprocessor);
                if (j > i) {
                    i = j;
                }
            }

            int i2 = p_238654_3_ + 12;
            int j2 = p_238654_4_ - 12;
            int k = 8;
            if (p_238654_2_.size() > 1) {
                k += 2 + (p_238654_2_.size() - 1) * 10;
            }

            if (i2 + i > this.width) {
                i2 -= 28 + i;
            }

            if (j2 + k + 6 > this.height) {
                j2 = this.height - k - 6;
            }

            p_238654_1_.push();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            Matrix4f matrix4f = p_238654_1_.getLast().getMatrix();
            fillGradient(matrix4f, bufferbuilder, i2 - 3, j2 - 4, i2 + i + 3, j2 - 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferbuilder, i2 - 3, j2 + k + 3, i2 + i + 3, j2 + k + 4, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferbuilder, i2 - 3, j2 - 3, i2 + i + 3, j2 + k + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferbuilder, i2 - 4, j2 - 3, i2 - 3, j2 + k + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferbuilder, i2 + i + 3, j2 - 3, i2 + i + 4, j2 + k + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferbuilder, i2 - 3, j2 - 3 + 1, i2 - 3 + 1, j2 + k + 3 - 1, 400, 1347420415, 1344798847);
            fillGradient(matrix4f, bufferbuilder, i2 + i + 2, j2 - 3 + 1, i2 + i + 3, j2 + k + 3 - 1, 400, 1347420415, 1344798847);
            fillGradient(matrix4f, bufferbuilder, i2 - 3, j2 - 3, i2 + i + 3, j2 - 3 + 1, 400, 1347420415, 1347420415);
            fillGradient(matrix4f, bufferbuilder, i2 - 3, j2 + k + 2, i2 + i + 3, j2 + k + 3, 400, 1344798847, 1344798847);
            RenderSystem.enableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.shadeModel(7425);
            bufferbuilder.finishDrawing();
            WorldVertexBufferUploader.draw(bufferbuilder);
            RenderSystem.shadeModel(7424);
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();
            IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
            p_238654_1_.translate(0.0D, 0.0D, 400.0D);

            for(int l1 = 0; l1 < p_238654_2_.size(); ++l1) {
                IReorderingProcessor ireorderingprocessor1 = p_238654_2_.get(l1);
                if (ireorderingprocessor1 != null) {
                    this.mc.fontRenderer.func_238416_a_(ireorderingprocessor1, i2, j2, -1, true, matrix4f, irendertypebuffer$impl, false, 0, 15728880);
                }

                if (l1 == 0) {
                    j2 += 2;
                }

                j2 += 10;
            }

            irendertypebuffer$impl.finish();
            p_238654_1_.pop();
        }
    }
}