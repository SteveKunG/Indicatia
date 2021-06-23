package com.stevekung.indicatia.event;

import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.core.IndicatiaForge;
import com.stevekung.indicatia.gui.components.MojangStatusButton;
import com.stevekung.indicatia.gui.exconfig.screens.ExtendedConfigScreen;
import com.stevekung.indicatia.gui.exconfig.screens.OffsetRenderPreviewScreen;
import com.stevekung.indicatia.gui.screens.MojangStatusScreen;
import com.stevekung.indicatia.handler.KeyBindingHandler;
import com.stevekung.indicatia.utils.AFKMode;
import com.stevekung.indicatia.utils.hud.HUDHelper;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.player.Input;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.ForgeIngameGui;
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
                    long now = Util.getMillis();

                    if (this.lastPinger == -1L || now - this.lastPinger > 5000L)
                    {
                        this.lastPinger = now;
                        HUDHelper.getRealTimeServerPing(this.mc.getCurrentServer());
                    }
                }

                for (UseAnim action : UseAnim.values())
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
        ForgeIngameGui.renderObjective = IndicatiaConfig.GENERAL.enableSidebarScoreboardRender.get();
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event)
    {
        Input movement = event.getMovementInput();

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
    public void onInitGui(GuiScreenEvent.InitGuiEvent.Post event)
    {
        Screen screen = event.getGui();

        if (screen instanceof TitleScreen)
        {
            int height = screen.height / 4 + 48;
            event.addWidget(new MojangStatusButton(screen.width / 2 + 104, height + 63, button -> this.mc.setScreen(new MojangStatusScreen(screen))));
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