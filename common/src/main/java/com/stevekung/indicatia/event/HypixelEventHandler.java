package com.stevekung.indicatia.event;

import org.lwjgl.glfw.GLFW;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.hud.InfoUtils;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.RemotePlayer;

public class HypixelEventHandler
{
    public HypixelEventHandler()
    {
        ClientRawInputEvent.MOUSE_CLICKED_PRE.register(this::onMouseClick);
    }

    private EventResult onMouseClick(Minecraft mc, int button, int action, int mods)
    {
        if (button == GLFW.GLFW_PRESS && action == GLFW.GLFW_MOUSE_BUTTON_2 && mc.crosshairPickEntity instanceof RemotePlayer player && !mc.player.isCrouching() && mc.player.getMainHandItem().isEmpty() && InfoUtils.INSTANCE.isHypixel() && IndicatiaSettings.INSTANCE.rightClickToAddParty)
        {
            if (mc.player.connection.getOnlinePlayers().stream().anyMatch(info -> info.getProfile().getName().equals(player.getGameProfile().getName())))
            {
                mc.player.chat("/p " + player.getName());
                return EventResult.interruptFalse();
            }
        }
        return EventResult.pass();
    }
}