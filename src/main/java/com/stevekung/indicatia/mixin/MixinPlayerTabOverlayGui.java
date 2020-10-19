package com.stevekung.indicatia.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Ordering;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.stevekung.indicatia.config.ExtendedConfig;
import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.config.PingMode;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import com.stevekung.stevekungslib.utils.client.ClientUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.overlay.PlayerTabOverlayGui;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;

@Mixin(PlayerTabOverlayGui.class)
public abstract class MixinPlayerTabOverlayGui extends AbstractGui
{
    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    @Final
    private static Ordering<NetworkPlayerInfo> ENTRY_ORDERING;

    @Redirect(method = "func_238523_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreObjective;)V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/FontRenderer.getStringWidth(Ljava/lang/String;)I", ordinal = 0))
    private int addStringWidth(FontRenderer fontRenderer, String text, MatrixStack matrixStack, int width, Scoreboard scoreboard, @Nullable ScoreObjective scoreObjective)
    {
        boolean pingDelay = ExtendedConfig.INSTANCE.pingMode == PingMode.PING_AND_DELAY;
        int pingWidth = 0;

        for (NetworkPlayerInfo info : ENTRY_ORDERING.sortedCopy(this.mc.player.connection.getPlayerInfoMap()))
        {
            int ping = info.getResponseTime();
            String pingText = String.valueOf(ping);

            if (pingDelay)
            {
                pingText = pingText + "/" + String.format("%.2f", ping / 1000.0F) + "s";
            }
            //TODO
//            pingWidth = IndicatiaConfig.GENERAL.enableCustomPlayerList.get() ? pingDelay ? ClientUtils.unicodeFontRenderer.getStringWidth(pingText) : this.mc.fontRenderer.getStringWidth(pingText) : 0;
        }
        return fontRenderer.getStringWidth(text) + (scoreObjective != null && scoreObjective.getRenderType() == ScoreCriteria.RenderType.HEARTS ? 0 : pingWidth);
    }

    @Inject(method = "func_238522_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;IIILnet/minecraft/client/network/play/NetworkPlayerInfo;)V", cancellable = true, at = @At("HEAD"))
    private void drawPing(MatrixStack matrixStack, int x1, int x2, int y, NetworkPlayerInfo playerInfo, CallbackInfo info)
    {
        boolean pingDelay = ExtendedConfig.INSTANCE.pingMode == PingMode.PING_AND_DELAY;
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