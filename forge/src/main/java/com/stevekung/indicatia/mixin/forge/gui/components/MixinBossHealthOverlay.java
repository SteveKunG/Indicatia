package com.stevekung.indicatia.mixin.forge.gui.components;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.stevekung.indicatia.config.IndicatiaConfig;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

@Mixin(BossHealthOverlay.class)
public class MixinBossHealthOverlay
{
    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At(value = "INVOKE", target = "net/minecraftforge/client/event/RenderGameOverlayEvent$BossInfo.getIncrement()I", remap = false))
    private int modifyIncrement(RenderGameOverlayEvent.BossInfo event)
    {
        return IndicatiaConfig.GENERAL.enableBossHealthBarRender.get() ? event.getIncrement() : 12;
    }
}