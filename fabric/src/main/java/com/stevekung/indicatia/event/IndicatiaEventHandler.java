package com.stevekung.indicatia.event;

import com.stevekung.indicatia.gui.components.MojangStatusButton;
import com.stevekung.indicatia.gui.screens.MojangStatusScreen;
import com.stevekung.indicatia.utils.PlatformConfig;
import com.stevekung.indicatia.utils.hud.HUDHelper;
import dev.architectury.platform.Platform;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
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
            HUDHelper.afkTick(mc.player);
            HUDHelper.autoFishTick(mc);

            if (mc.getCurrentServer() != null)
            {
                long now = Util.getMillis();

                if (this.lastPinger == -1L || now - this.lastPinger > 5000L)
                {
                    this.lastPinger = now;
                    HUDHelper.getRealTimeServerPing(mc.getCurrentServer());
                }
            }

            for (UseAnim action : UseAnim.values())
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

    public void onInitGui(Minecraft mc, Screen screen)
    {
        if (screen instanceof TitleScreen)
        {
            int height = screen.height / 4 + 48;
            Screens.getButtons(screen).add(new MojangStatusButton(screen.width / 2 + 104, height + (Platform.isFabric() && Platform.isModLoaded("modmenu") ? 75 : 63), button -> mc.setScreen(new MojangStatusScreen(screen))));
        }
    }
}