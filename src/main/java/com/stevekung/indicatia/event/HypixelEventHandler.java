package com.stevekung.indicatia.event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.glfw.GLFW;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.utils.InfoUtils;
import com.stevekung.stevekungslib.utils.JsonUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ChatType;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HypixelEventHandler
{
    private static final Pattern NICK_PATTERN = Pattern.compile("^You are now nicked as (?<nick>\\w+)!");
    private final Minecraft mc;

    public HypixelEventHandler()
    {
        this.mc = Minecraft.getInstance();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (this.mc.player != null)
        {
            if (event.phase == TickEvent.Phase.START)
            {
                HypixelEventHandler.getHypixelNickedPlayer(this.mc);
            }
        }
    }

    @SubscribeEvent
    public void onMouseClick(InputEvent.MouseInputEvent event)
    {
        if (event.getButton() == GLFW.GLFW_PRESS && event.getAction() == GLFW.GLFW_MOUSE_BUTTON_2 && InfoUtils.INSTANCE.isHypixel() && ExtendedConfig.INSTANCE.rightClickToAddParty)
        {
            HypixelEventHandler.rightClickAddParty(this.mc);
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event)
    {
        if (event.getMessage() == null)
        {
            return;
        }

        String unformattedText = event.getMessage().getUnformattedComponentText();

        if (InfoUtils.INSTANCE.isHypixel())
        {
            Matcher nickMatcher = HypixelEventHandler.NICK_PATTERN.matcher(unformattedText);

            if (event.getType() == ChatType.CHAT)
            {
                if (unformattedText.contains("Illegal characters in chat") || unformattedText.contains("A kick occurred in your connection, so you have been routed to limbo!") || unformattedText.contains("A kick occurred in your connection, so you were put in the"))
                {
                    event.setMessage(null);
                }
                else if (unformattedText.contains("You were spawned in Limbo."))
                {
                    event.setMessage(JsonUtils.create("You were spawned in Limbo.").setStyle(JsonUtils.GREEN));
                }
                else if (unformattedText.contains("Your nick has been reset!"))
                {
                    ExtendedConfig.INSTANCE.hypixelNickName = "";
                    ExtendedConfig.INSTANCE.save();
                }

                if (nickMatcher.matches())
                {
                    ExtendedConfig.INSTANCE.hypixelNickName = nickMatcher.group("nick");
                    ExtendedConfig.INSTANCE.save();
                }
            }
        }
    }

    private static void getHypixelNickedPlayer(Minecraft mc)
    {
        if (InfoUtils.INSTANCE.isHypixel() && mc.currentScreen instanceof EditSignScreen)
        {
            EditSignScreen gui = (EditSignScreen) mc.currentScreen;

            if (gui.tileSign != null)
            {
                ExtendedConfig.INSTANCE.hypixelNickName = gui.tileSign.signText[0].getUnformattedComponentText();

                if (mc.player.ticksExisted % 40 == 0)
                {
                    ExtendedConfig.INSTANCE.save();
                }
            }
        }
    }

    public static void rightClickAddParty(Minecraft mc)
    {
        if (mc.objectMouseOver != null && mc.objectMouseOver.getType() == RayTraceResult.Type.ENTITY)
        {
            EntityRayTraceResult result = (EntityRayTraceResult)mc.objectMouseOver;

            if (mc.player.getHeldItemMainhand().isEmpty() && result.getEntity() instanceof RemoteClientPlayerEntity)
            {
                RemoteClientPlayerEntity player = (RemoteClientPlayerEntity) result.getEntity();
                mc.player.sendChatMessage("/p " + player.getName());
            }
        }
    }
}