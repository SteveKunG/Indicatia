package com.stevekung.indicatia.event;

import com.google.common.collect.Ordering;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.Equipments;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.gui.exconfig.screens.OffsetRenderPreviewScreen;
import com.stevekung.indicatia.hud.EffectOverlays;
import com.stevekung.indicatia.hud.EquipmentOverlays;
import com.stevekung.indicatia.hud.InfoOverlays;
import com.stevekung.indicatia.utils.PlatformConfig;
import com.stevekung.indicatia.utils.hud.HUDHelper;
import com.stevekung.indicatia.utils.hud.InfoOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;

public class HUDRenderEventHandler
{
    public static final HUDRenderEventHandler INSTANCE = new HUDRenderEventHandler();

    public void onClientTick(ServerLevel level)
    {
        InfoOverlays.getTPS(level.getServer());
    }

    public void onPreInfoRender(Minecraft mc, PoseStack poseStack)
    {
        if (!mc.options.renderDebug && !mc.options.hideGui)
        {
            if (PlatformConfig.getRenderInfo() && mc.player != null && mc.level != null && !(mc.screen instanceof OffsetRenderPreviewScreen))
            {
                var iLeft = 0;
                var iRight = 0;

                for (var info : HUDHelper.getInfoOverlays(mc))
                {
                    if (info == null || info.isEmpty())
                    {
                        continue;
                    }

                    var collection = mc.player.getActiveEffects();
                    var goodCount = (int) Ordering.natural().reverse().sortedCopy(collection).stream().filter(mobEffectInstance -> mobEffectInstance.showIcon() && (mobEffectInstance.getEffect().isBeneficial() || mobEffectInstance.getEffect().getCategory() == MobEffectCategory.NEUTRAL)).count();
                    var badCount = (int) Ordering.natural().reverse().sortedCopy(collection).stream().filter(mobEffectInstance -> mobEffectInstance.showIcon() && !mobEffectInstance.getEffect().isBeneficial()).count();
                    var state = 0;

                    if (goodCount > 0)
                    {
                        state = 1;
                    }
                    if (badCount > 0)
                    {
                        state = 2;
                    }

                    var value = info.toFormatted();
                    var pos = info.getPos();
                    var defaultPos = 3.0625F;
                    var fontHeight = mc.font.lineHeight + 1;
                    var yOffset = 3 + fontHeight * (pos == InfoOverlay.Position.LEFT ? iLeft : iRight);

                    if (pos == InfoOverlay.Position.RIGHT && !IndicatiaSettings.INSTANCE.swapRenderInfo || pos == InfoOverlay.Position.LEFT && IndicatiaSettings.INSTANCE.swapRenderInfo)
                    {
                        yOffset += state == 1 ? 24 : state == 2 ? 49 : 0;
                    }

                    var xOffset = mc.getWindow().getGuiScaledWidth() - 2 - mc.font.width(value.getString());
                    mc.font.drawShadow(poseStack, value, pos == InfoOverlay.Position.LEFT ? !IndicatiaSettings.INSTANCE.swapRenderInfo ? defaultPos : xOffset : pos == InfoOverlay.Position.RIGHT ? !IndicatiaSettings.INSTANCE.swapRenderInfo ? xOffset : defaultPos : defaultPos, yOffset, 16777215);

                    if (pos == InfoOverlay.Position.LEFT)
                    {
                        ++iLeft;
                    }
                    else
                    {
                        ++iRight;
                    }
                }
            }

            if (!mc.player.isSpectator() && IndicatiaSettings.INSTANCE.equipmentHUD)
            {
                if (IndicatiaSettings.INSTANCE.equipmentPosition == Equipments.Position.HOTBAR)
                {
                    EquipmentOverlays.renderHotbarEquippedItems(mc, poseStack);
                }
                else
                {
                    if (IndicatiaSettings.INSTANCE.equipmentDirection == Equipments.Direction.VERTICAL)
                    {
                        EquipmentOverlays.renderVerticalEquippedItems(mc, poseStack);
                    }
                    else
                    {
                        EquipmentOverlays.renderHorizontalEquippedItems(mc, poseStack);
                    }
                }
            }

            if (IndicatiaSettings.INSTANCE.potionHUD)
            {
                EffectOverlays.renderPotionHUD(mc, poseStack);
            }
        }
    }

    public void onLoggedOut()
    {
        InfoOverlays.OVERALL_TPS = InfoOverlay.empty();
        InfoOverlays.OVERWORLD_TPS = InfoOverlay.empty();
        InfoOverlays.TPS = InfoOverlay.empty();
        InfoOverlays.ALL_TPS.clear();
    }
}