package com.stevekung.indicatia.gui.exconfig.screen;

import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.hud.EffectOverlays;
import com.stevekung.indicatia.hud.EquipmentOverlays;
import com.stevekung.stevekungslib.utils.JsonUtils;

import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OffsetRenderPreviewScreen extends Screen
{
    private final Screen parent;

    public OffsetRenderPreviewScreen(Screen parent)
    {
        super(JsonUtils.create("Render Preview"));
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