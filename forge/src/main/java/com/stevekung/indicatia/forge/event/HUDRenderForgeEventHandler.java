package com.stevekung.indicatia.forge.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.stevekung.indicatia.event.HUDRenderEventHandler;
import com.stevekung.indicatia.forge.config.IndicatiaConfig;
import com.stevekung.indicatia.gui.exconfig.screens.OffsetRenderPreviewScreen;
import com.stevekung.indicatia.hud.InfoOverlays;
import com.stevekung.indicatia.utils.hud.InfoOverlay;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class HUDRenderForgeEventHandler
{
    private final Minecraft mc;

    public HUDRenderForgeEventHandler()
    {
        this.mc = Minecraft.getInstance();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            var server = ServerLifecycleHooks.getCurrentServer();

            if (server != null)
            {
                InfoOverlays.getTPS(server);
            }
        }
    }

    @SubscribeEvent
    public void onPreInfoRender(RenderGameOverlayEvent.Pre event)
    {
        var poseStack = event.getMatrixStack();

        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT)
        {
            HUDRenderEventHandler.INSTANCE.onPreInfoRender(this.mc, poseStack);
        }
        else
        {
            if (this.mc.screen instanceof OffsetRenderPreviewScreen)
            {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onRenderPreLayer(RenderGameOverlayEvent.PreLayer event)
    {
        if (event.getOverlay() == ForgeIngameGui.SCOREBOARD_ELEMENT && !IndicatiaConfig.GENERAL.enableSidebarScoreboardRender.get())
        {
            event.setCanceled(true);
        }
        if (event.getOverlay() == ForgeIngameGui.BOSS_HEALTH_ELEMENT && IndicatiaConfig.GENERAL.enableRenderBossHealthStatus.get())
        {
            event.setCanceled(true);
            RenderSystem.enableDepthTest();
            RenderSystem.defaultBlendFunc();
        }
        if (event.getOverlay() == ForgeIngameGui.POTION_ICONS_ELEMENT && !IndicatiaConfig.GENERAL.enableVanillaPotionHUD.get())
        {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event)
    {
        InfoOverlays.OVERALL_TPS = InfoOverlay.empty();
        InfoOverlays.OVERWORLD_TPS = InfoOverlay.empty();
        InfoOverlays.TPS = InfoOverlay.empty();
        InfoOverlays.ALL_TPS.clear();
    }
}