package com.stevekung.indicatia.forge.event;

import com.stevekung.indicatia.event.IndicatiaEventHandler;
import com.stevekung.indicatia.forge.config.IndicatiaConfig;
import com.stevekung.indicatia.forge.core.IndicatiaForge;
import com.stevekung.indicatia.gui.exconfig.screens.ExtendedConfigScreen;
import com.stevekung.indicatia.gui.exconfig.screens.OffsetRenderPreviewScreen;
import com.stevekung.indicatia.handler.KeyBindingHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class IndicatiaForgeEventHandler
{
    private final Minecraft mc;

    public IndicatiaForgeEventHandler()
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
            IndicatiaEventHandler.INSTANCE.onClientTick(this.mc);
        }
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