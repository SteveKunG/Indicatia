package com.stevekung.indicatia.forge.event;

import com.stevekung.indicatia.forge.config.IndicatiaConfig;
import com.stevekung.indicatia.forge.core.IndicatiaForge;
import com.stevekung.indicatia.gui.exconfig.screens.ExtendedConfigScreen;
import com.stevekung.indicatia.gui.exconfig.screens.OffsetRenderPreviewScreen;
import com.stevekung.indicatia.handler.KeyBindingHandler;
import com.stevekung.indicatia.utils.AFKMode;
import com.stevekung.indicatia.utils.hud.HUDHelper;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class IndicatiaEventHandler
{
    private final Minecraft mc;
    private long lastPinger = -1L;

    public IndicatiaEventHandler()
    {
        this.mc = Minecraft.getInstance();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (this.mc.player != null)
        {
            if (IndicatiaConfig.GENERAL.enableVersionChecker.get())
            {
                if (!IndicatiaForge.CHECKER.hasChecked())
                {
                    IndicatiaForge.CHECKER.checkFail();
                    IndicatiaForge.CHECKER.printInfo();
                    IndicatiaForge.CHECKER.setChecked(true);
                }
            }

            if (event.phase == TickEvent.Phase.START)
            {
                HUDHelper.afkTick(this.mc.player);
                HUDHelper.autoFishTick(this.mc);

                if (this.mc.getCurrentServer() != null)
                {
                    var now = Util.getMillis();

                    if (this.lastPinger == -1L || now - this.lastPinger > 5000L)
                    {
                        this.lastPinger = now;
                        HUDHelper.getRealTimeServerPing(this.mc.getCurrentServer());
                    }
                }

                for (var action : UseAnim.values())
                {
                    if (action != UseAnim.NONE)
                    {
                        if (IndicatiaConfig.GENERAL.enableBlockhitAnimation.get() && this.mc.options.keyAttack.isDown() && this.mc.hitResult != null && this.mc.hitResult.getType() == HitResult.Type.BLOCK && !this.mc.player.getMainHandItem().isEmpty() && this.mc.player.getMainHandItem().getUseAnimation() == action)
                        {
                            this.mc.player.swing(InteractionHand.MAIN_HAND);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event)
    {
        var movement = event.getMovementInput();

        // afk stuff
        if (HUDHelper.AFK_MODE == AFKMode.RANDOM_MOVE_360)
        {
            int afkMoveTick = HUDHelper.afkMoveTicks;

            if (afkMoveTick == 1)
            {
                movement.forwardImpulse += Math.random();
                movement.up = true;
            }
            else if (afkMoveTick == 3)
            {
                movement.leftImpulse += Math.random();
                movement.left = true;
            }
            else if (afkMoveTick == 5)
            {
                movement.forwardImpulse -= Math.random();
                movement.down = true;
            }
            else if (afkMoveTick == 7)
            {
                movement.leftImpulse -= Math.random();
                movement.right = true;
            }
        }
    }

    @SubscribeEvent
    public void onLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event)
    {
        HUDHelper.stopCommandTicks();
    }

    @SubscribeEvent
    public void onPressKey(InputEvent.KeyInputEvent event)
    {
        if (KeyBindingHandler.KEY_QUICK_CONFIG.isDown())
        {
            this.mc.setScreen(new ExtendedConfigScreen());
        }
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event)
    {
        if (this.mc.screen instanceof OffsetRenderPreviewScreen)
        {
            event.setCanceled(true);
        }
    }
}