package com.stevekung.indicatia.gui.exconfig.screens;

import com.mojang.blaze3d.systems.RenderSystem;
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
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        RenderSystem.enableBlend();
        EffectOverlays.renderPotionHUD(this.minecraft, poseStack);

        if (IndicatiaSettings.INSTANCE.equipmentDirection == Equipments.Direction.VERTICAL)
        {
            EquipmentOverlays.renderVerticalEquippedItems(this.minecraft, poseStack);
        }
        else
        {
            EquipmentOverlays.renderHorizontalEquippedItems(this.minecraft, poseStack);
        }
    }

    @Override
    public boolean isPauseScreen()
    {
        return true;
    }
}