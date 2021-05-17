package com.stevekung.indicatia.mixin.gui.components;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.config.PingMode;
import com.stevekung.indicatia.utils.PlatformConfig;
import com.stevekung.indicatia.utils.hud.HUDHelper;
import com.stevekung.stevekungslib.utils.GameProfileUtils;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;

@Mixin(PlayerTabOverlay.class)
public class MixinPlayerTabOverlay
{
    @Unique
    private int pingWidth;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;ILnet/minecraft/world/scores/Scoreboard;Lnet/minecraft/world/scores/Objective;)V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/Font.width(Lnet/minecraft/network/chat/FormattedText;)I"))
    private int addPingWidth(Font font, FormattedText formattedText)
    {
        return this.minecraft.font.width(formattedText) + this.pingWidth;
    }

    @Redirect(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;ILnet/minecraft/world/scores/Scoreboard;Lnet/minecraft/world/scores/Objective;)V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/components/PlayerTabOverlay.getNameForDisplay(Lnet/minecraft/client/multiplayer/PlayerInfo;)Lnet/minecraft/network/chat/Component;", ordinal = 0))
    private Component getPingPlayerInfo(PlayerTabOverlay overlay, PlayerInfo playerInfo)
    {
        if (PlatformConfig.getCustomPlayerList())
        {
            boolean pingDelay = IndicatiaSettings.INSTANCE.pingMode == PingMode.PING_AND_DELAY;
            int ping = playerInfo.getLatency();
            MutableComponent pingText = TextComponentUtils.component(String.valueOf(ping));

            if (pingDelay)
            {
                pingText = pingText.append("/" + String.format("%.2f", ping / 1000.0F) + "s");
                pingText.setStyle(pingText.getStyle().withFont(ClientUtils.UNICODE));
            }
            this.pingWidth = PlatformConfig.getCustomPlayerList() ? this.minecraft.font.width(pingText) : 0;
        }
        return overlay.getNameForDisplay(playerInfo);
    }

    @Inject(method = "renderPingIcon(Lcom/mojang/blaze3d/vertex/PoseStack;IIILnet/minecraft/client/multiplayer/PlayerInfo;)V", cancellable = true, at = @At("HEAD"))
    private void drawPing(PoseStack poseStack, int x1, int x2, int y, PlayerInfo playerInfo, CallbackInfo info)
    {
        if (PlatformConfig.getCustomPlayerList())
        {
            boolean pingDelay = IndicatiaSettings.INSTANCE.pingMode == PingMode.PING_AND_DELAY;
            int ping = playerInfo.getLatency();
            ChatFormatting color = ChatFormatting.GREEN;

            if (GameProfileUtils.getUsername().equals(playerInfo.getProfile().getName()) || playerInfo.getTabListDisplayName() != null && GameProfileUtils.getUsername().equals(playerInfo.getTabListDisplayName().getString()))
            {
                if (ping <= 1)
                {
                    ping = HUDHelper.currentServerPing;
                }
            }

            if (ping >= 200 && ping < 300)
            {
                color = ChatFormatting.YELLOW;
            }
            else if (ping >= 300 && ping < 500)
            {
                color = ChatFormatting.RED;
            }
            else if (ping >= 500)
            {
                color = ChatFormatting.DARK_RED;
            }

            MutableComponent pingText = TextComponentUtils.formatted(String.valueOf(ping), color);

            if (pingDelay)
            {
                pingText = TextComponentUtils.formatted(ping + "/" + String.format("%.2f", (float) ping / 1000) + "s", color);
                pingText.setStyle(pingText.getStyle().withFont(ClientUtils.UNICODE));
            }
            this.minecraft.font.drawShadow(poseStack, pingText, x1 + x2 - this.minecraft.font.width(pingText), y + 0.625F, 0);
            info.cancel();
        }
    }
}