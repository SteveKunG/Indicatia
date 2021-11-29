package com.stevekung.indicatia.fabric.event;

import com.stevekung.indicatia.utils.PlatformConfig;
import com.stevekung.indicatia.utils.hud.HUDHelper;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.HitResult;

public class IndicatiaEventHandler
{
    private long lastPinger = -1L;

    public static final IndicatiaEventHandler INSTANCE = new IndicatiaEventHandler();

    public void onClientTick(Minecraft mc)
    {
        if (mc.player != null)
        {
            if (mc.getCurrentServer() != null)
            {
                var now = Util.getMillis();

                if (this.lastPinger == -1L || now - this.lastPinger > 5000L)
                {
                    this.lastPinger = now;
                    HUDHelper.getRealTimeServerPing(mc.getCurrentServer());
                }
            }

            for (var action : UseAnim.values())
            {
                if (action != UseAnim.NONE)
                {
                    if (PlatformConfig.getBlockHitAnimation() && mc.options.keyAttack.isDown() && mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK && !mc.player.getMainHandItem().isEmpty() && mc.player.getMainHandItem().getUseAnimation() == action)
                    {
                        mc.player.swing(InteractionHand.MAIN_HAND);
                    }
                }
            }
        }
    }
}