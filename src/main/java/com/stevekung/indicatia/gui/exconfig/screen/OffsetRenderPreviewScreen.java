package com.stevekung.indicatia.gui.exconfig.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.hud.EffectOverlays;
import com.stevekung.indicatia.hud.EquipmentOverlays;

import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;

public class OffsetRenderPreviewScreen extends Screen
{
    private final Screen parent;

    public OffsetRenderPreviewScreen(Screen parent)
    {
        super(NarratorChatListener.EMPTY);
        this.parent = parent;
    }

    @Override
    public void onClose()
    {
        this.minecraft.displayGuiScreen(this.parent);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        GlStateManager.enableBlend();
        EffectOverlays.renderPotionHUD(this.minecraft);

        if (ExtendedConfig.INSTANCE.equipmentDirection == Equipments.Direction.VERTICAL)
        {
            EquipmentOverlays.renderVerticalEquippedItems(this.minecraft);
        }
        else
        {
            EquipmentOverlays.renderHorizontalEquippedItems(this.minecraft);
        }
    }

    @Override
    public boolean isPauseScreen()
    {
        return true;
    }
}