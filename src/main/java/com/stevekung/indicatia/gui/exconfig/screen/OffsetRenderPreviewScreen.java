package com.stevekung.indicatia.gui.exconfig.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.hud.EffectOverlays;
import com.stevekung.indicatia.hud.EquipmentOverlays;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

public class OffsetRenderPreviewScreen extends Screen
{
    private final Screen parent;

    public OffsetRenderPreviewScreen(Screen parent)
    {
        super(StringTextComponent.EMPTY);
        this.parent = parent;
    }

    @Override
    public void closeScreen()
    {
        this.minecraft.displayGuiScreen(this.parent);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        GlStateManager.enableBlend();
        EffectOverlays.renderPotionHUD(this.minecraft, matrixStack);

        if (ExtendedConfig.INSTANCE.equipmentDirection == Equipments.Direction.VERTICAL)
        {
            EquipmentOverlays.renderVerticalEquippedItems(this.minecraft, matrixStack);
        }
        else
        {
            EquipmentOverlays.renderHorizontalEquippedItems(this.minecraft, matrixStack);
        }
    }

    @Override
    public boolean isPauseScreen()
    {
        return true;
    }
}