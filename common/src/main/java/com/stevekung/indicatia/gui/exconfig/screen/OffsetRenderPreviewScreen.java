package com.stevekung.indicatia.gui.exconfig.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.hud.EffectOverlays;
import com.stevekung.indicatia.hud.EquipmentOverlays;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class OffsetRenderPreviewScreen extends Screen
{
    private final Screen parent;

    public OffsetRenderPreviewScreen(Screen parent)
    {
        super(TextComponent.EMPTY);
        this.parent = parent;
    }

    @Override
    public void onClose()
    {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        GlStateManager._enableBlend();
        EffectOverlays.renderPotionHUD(this.minecraft, matrixStack);

        if (IndicatiaSettings.INSTANCE.equipmentDirection == Equipments.Direction.VERTICAL)
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