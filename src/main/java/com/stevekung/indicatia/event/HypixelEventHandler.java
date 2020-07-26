package com.stevekung.indicatia.event;

import org.lwjgl.glfw.GLFW;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.hud.InfoUtils;
import com.stevekung.stevekungslib.utils.JsonUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
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
        if (event.getButton() == GLFW.GLFW_PRESS && event.getAction() == GLFW.GLFW_MOUSE_BUTTON_2 && this.mc.pointedEntity != null && this.mc.pointedEntity instanceof RemoteClientPlayerEntity && !this.mc.player.isCrouching() && this.mc.player.getHeldItemMainhand().isEmpty() && InfoUtils.INSTANCE.isHypixel() && ExtendedConfig.INSTANCE.rightClickToAddParty)
        {
            RemoteClientPlayerEntity player = (RemoteClientPlayerEntity)this.mc.pointedEntity;

            if (this.mc.player.connection.getPlayerInfoMap().stream().anyMatch(info -> info.getGameProfile().getName().equals(player.getGameProfile().getName())))
            {
                this.mc.player.sendChatMessage("/p " + player.getName());
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event)
    {
        String message = event.getMessage().getUnformattedComponentText();

        if (InfoUtils.INSTANCE.isHypixel())
        {
            if (event.getType() == ChatType.CHAT)
            {
                if (message.contains("Illegal characters in chat") || message.contains("A kick occurred in your connection"))
                {
                    event.setMessage(null);
                }
                else if (message.contains("You were spawned in Limbo."))
                {
                    event.setMessage(JsonUtils.create("You were spawned in Limbo.").mergeStyle(TextFormatting.GREEN));
                }
            }
        }
    }
}