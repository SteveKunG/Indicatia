package com.stevekung.indicatia.event;

import org.lwjgl.glfw.GLFW;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.hud.InfoUtils;
import me.shedaniel.architectury.event.events.client.ClientRawInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.InteractionResult;

public class HypixelEventHandler
{
    public HypixelEventHandler()
    {
        ClientRawInputEvent.MOUSE_CLICKED_PRE.register(this::onMouseClick);
    }

    private InteractionResult onMouseClick(Minecraft mc, int button, int action, int mods)
    {
        if (button == GLFW.GLFW_PRESS && action == GLFW.GLFW_MOUSE_BUTTON_2 && mc.crosshairPickEntity instanceof RemotePlayer && !mc.player.isCrouching() && mc.player.getMainHandItem().isEmpty() && InfoUtils.INSTANCE.isHypixel() && IndicatiaSettings.INSTANCE.rightClickToAddParty)
        {
            RemotePlayer player = (RemotePlayer) mc.crosshairPickEntity;

            if (mc.player.connection.getOnlinePlayers().stream().anyMatch(info -> info.getProfile().getName().equals(player.getGameProfile().getName())))
            {
                mc.player.chat("/p " + player.getName());
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}