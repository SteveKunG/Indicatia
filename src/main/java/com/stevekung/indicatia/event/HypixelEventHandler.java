package com.stevekung.indicatia.event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.glfw.GLFW;

import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.hud.InfoUtils;
import com.stevekung.stevekungslib.utils.JsonUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.util.text.ChatType;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HypixelEventHandler
{
    private static final Pattern NICK_PATTERN = Pattern.compile("You are now nicked as (?<nick>\\w+)!");
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
        if (event.getButton() == GLFW.GLFW_PRESS && event.getAction() == GLFW.GLFW_MOUSE_BUTTON_2 && this.mc.pointedEntity != null && this.mc.pointedEntity instanceof RemoteClientPlayerEntity && !this.mc.player.isSneaking() && this.mc.player.getHeldItemMainhand().isEmpty() && InfoUtils.INSTANCE.isHypixel() && ExtendedConfig.INSTANCE.rightClickToAddParty)
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
            Matcher nickMatcher = HypixelEventHandler.NICK_PATTERN.matcher(message);

            if (event.getType() == ChatType.CHAT)
            {
                if (message.contains("Illegal characters in chat") || message.contains("A kick occurred in your connection"))
                {
                    event.setMessage(null);
                }
                else if (message.contains("You were spawned in Limbo."))
                {
                    event.setMessage(JsonUtils.create("You were spawned in Limbo.").setStyle(JsonUtils.GREEN));
                }
                else if (message.contains("Your nick has been reset!"))
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
            EditSignScreen gui = (EditSignScreen)mc.currentScreen;

            if (gui.tileSign != null)
            {
                if (!(gui.tileSign.signText[2].getUnformattedComponentText().contains("Enter your") && gui.tileSign.signText[3].getUnformattedComponentText().contains("username here")))
                {
                    return;
                }

                ExtendedConfig.INSTANCE.hypixelNickName = gui.tileSign.signText[0].getUnformattedComponentText();

                if (mc.player.ticksExisted % 40 == 0)
                {
                    ExtendedConfig.INSTANCE.save();
                }
            }
        }
    }
}