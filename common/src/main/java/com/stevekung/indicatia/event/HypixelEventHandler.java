package com.stevekung.indicatia.event;

import org.lwjgl.glfw.GLFW;

import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.hud.InfoUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HypixelEventHandler
{
    private final Minecraft mc;

    public HypixelEventHandler()
    {
        this.mc = Minecraft.getInstance();
    }

    @SubscribeEvent
    public void onMouseClick(InputEvent.MouseInputEvent event)
    {
        if (event.getButton() == GLFW.GLFW_PRESS && event.getAction() == GLFW.GLFW_MOUSE_BUTTON_2 && this.mc.pointedEntity != null && this.mc.pointedEntity instanceof RemoteClientPlayerEntity && !this.mc.player.isCrouching() && this.mc.player.getHeldItemMainhand().isEmpty() && InfoUtils.INSTANCE.isHypixel() && IndicatiaSettings.INSTANCE.rightClickToAddParty)
        {
            RemoteClientPlayerEntity player = (RemoteClientPlayerEntity)this.mc.pointedEntity;

            if (this.mc.player.connection.getPlayerInfoMap().stream().anyMatch(info -> info.getGameProfile().getName().equals(player.getGameProfile().getName())))
            {
                this.mc.player.sendChatMessage("/p " + player.getName());
                event.setCanceled(true);
            }
        }
    }
}