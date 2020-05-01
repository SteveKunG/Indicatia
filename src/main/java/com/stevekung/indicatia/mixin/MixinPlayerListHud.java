package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Formatting;

@Mixin(PlayerListHud.class)
public abstract class MixinPlayerListHud extends DrawableHelper
{
    @Shadow
    @Final
    private MinecraftClient client;

    @Overwrite
    public void renderLatencyIcon(int x1, int x2, int y, PlayerListEntry entry)
    {
        int ping = entry.getLatency();
        Formatting color = Formatting.GREEN;

        if (ping >= 200 && ping < 300)
        {
            color = Formatting.YELLOW;
        }
        else if (ping >= 300 && ping < 500)
        {
            color = Formatting.RED;
        }
        else if (ping >= 500)
        {
            color = Formatting.DARK_RED;
        }
        String pingText = String.valueOf(ping);
        this.client.textRenderer.drawWithShadow(color + pingText, x1 + x2 - this.client.textRenderer.getStringWidth(pingText), y + 0.625F, 0);
    }
}