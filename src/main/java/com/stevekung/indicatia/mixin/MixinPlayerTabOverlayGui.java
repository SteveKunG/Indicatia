package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.config.IndicatiaSettings;
import com.stevekung.indicatia.config.PingMode;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.overlay.PlayerTabOverlayGui;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TextFormatting;

@Mixin(PlayerTabOverlayGui.class)
public abstract class MixinPlayerTabOverlayGui extends AbstractGui
{
    private int pingWidth;

    @Shadow
    @Final
    private Minecraft mc;

    @Redirect(method = "func_238523_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreObjective;)V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/FontRenderer.getStringPropertyWidth(Lnet/minecraft/util/text/ITextProperties;)I"))
    private int addPingWidth(FontRenderer font, ITextProperties properties)
    {
        return this.mc.fontRenderer.getStringPropertyWidth(properties) + this.pingWidth;
    }

    @Redirect(method = "func_238523_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreObjective;)V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/overlay/PlayerTabOverlayGui.getDisplayName(Lnet/minecraft/client/network/play/NetworkPlayerInfo;)Lnet/minecraft/util/text/ITextComponent;", ordinal = 0))
    private ITextComponent getPingPlayerInfo(PlayerTabOverlayGui overlay, NetworkPlayerInfo networkPlayerInfoIn)
    {
        boolean pingDelay = IndicatiaSettings.INSTANCE.pingMode == PingMode.PING_AND_DELAY;
        int ping = networkPlayerInfoIn.getResponseTime();
        IFormattableTextComponent pingText = TextComponentUtils.component(String.valueOf(ping));

        if (pingDelay)
        {
            pingText = pingText.appendString("/" + String.format("%.2f", ping / 1000.0F) + "s");
            pingText.setStyle(pingText.getStyle().setFontId(ClientUtils.UNICODE));
        }
        this.pingWidth = IndicatiaConfig.GENERAL.enableCustomPlayerList.get() ? this.mc.fontRenderer.getStringPropertyWidth(pingText) : 0;
        return overlay.getDisplayName(networkPlayerInfoIn);
    }

    @Inject(method = "func_238522_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;IIILnet/minecraft/client/network/play/NetworkPlayerInfo;)V", cancellable = true, at = @At("HEAD"))
    private void drawPing(MatrixStack matrixStack, int x1, int x2, int y, NetworkPlayerInfo playerInfo, CallbackInfo info)
    {
        boolean pingDelay = IndicatiaSettings.INSTANCE.pingMode == PingMode.PING_AND_DELAY;
        int ping = playerInfo.getResponseTime();

        if (IndicatiaConfig.GENERAL.enableCustomPlayerList.get())
        {
            TextFormatting color = TextFormatting.GREEN;

            if (ping >= 200 && ping < 300)
            {
                color = TextFormatting.YELLOW;
            }
            else if (ping >= 300 && ping < 500)
            {
                color = TextFormatting.RED;
            }
            else if (ping >= 500)
            {
                color = TextFormatting.DARK_RED;
            }

            IFormattableTextComponent pingText = TextComponentUtils.formatted(String.valueOf(ping), color);

            if (pingDelay)
            {
                pingText = TextComponentUtils.formatted(String.valueOf(ping) + "/" + String.format("%.2f", (float)ping / 1000) + "s", color);
                pingText.setStyle(pingText.getStyle().setFontId(ClientUtils.UNICODE));
            }
            this.mc.fontRenderer.func_243246_a(matrixStack, pingText, x1 + x2 - this.mc.fontRenderer.getStringPropertyWidth(pingText), y + 0.625F, 0);
            info.cancel();
        }
    }
}