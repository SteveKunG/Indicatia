package com.stevekung.indicatia.fabric.mixin.gui.screens.multiplayer;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Objects;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.fabric.core.IndicatiaFabric;
import com.stevekung.stevekunglib.utils.LangUtils;
import com.stevekung.stevekunglib.utils.TextComponentUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public abstract class MixinServerSelectionList_OnlineServerEntry
{
    @Shadow
    @Final
    JoinMultiplayerScreen screen;

    @Shadow
    @Final
    Minecraft minecraft;

    @Shadow
    @Final
    ServerData serverData;

    @Shadow
    @Final
    ResourceLocation iconLocation;

    @Shadow
    String lastIconB64;

    @Shadow
    DynamicTexture icon;

    @Shadow
    abstract void drawIcon(PoseStack poseStack, int x, int y, ResourceLocation resource);

    @Shadow
    abstract boolean uploadServerIcon(String icon);

    @Shadow
    abstract boolean canJoin();

    @SuppressWarnings("deprecation")
    @Inject(method = "render", cancellable = true, at = @At("HEAD"))
    private void indicatia$render(PoseStack poseStack, int slotIndex, int y, int x, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks, CallbackInfo info)
    {
        var entry = (ServerSelectionList.OnlineServerEntry) (Object) this;

        if (IndicatiaFabric.CONFIG.general.multiplayerScreenEnhancement)
        {
            if (!this.serverData.pinged)
            {
                this.serverData.pinged = true;
                this.serverData.ping = -2L;
                this.serverData.motd = TextComponent.EMPTY;
                this.serverData.status = TextComponent.EMPTY;

                ServerSelectionList.THREAD_POOL.submit(() ->
                {
                    try
                    {
                        this.screen.getPinger().pingServer(this.serverData, () -> this.minecraft.execute(entry::updateServerList));
                    }
                    catch (UnknownHostException e)
                    {
                        this.serverData.ping = -1L;
                        this.serverData.motd = LangUtils.formatted("multiplayer.status.cannot_resolve", ChatFormatting.DARK_RED);
                    }
                    catch (Exception e)
                    {
                        this.serverData.ping = -1L;
                        this.serverData.motd = LangUtils.formatted("multiplayer.status.cannot_connect", ChatFormatting.DARK_RED);
                    }
                });
            }

            var flag = this.serverData.protocol > SharedConstants.getCurrentVersion().getProtocolVersion();
            var flag1 = this.serverData.protocol < SharedConstants.getCurrentVersion().getProtocolVersion();
            var flag2 = flag || flag1;
            this.minecraft.font.draw(poseStack, this.serverData.name, x + 32 + 3, y + 1, 16777215);
            var list = this.minecraft.font.split(this.serverData.motd, listWidth - 50);

            for (var i = 0; i < Math.min(list.size(), 2); ++i)
            {
                this.minecraft.font.draw(poseStack, list.get(i), x + 35, y + 12 + 9 * i, 8421504);
            }

            Component ping;
            var responseTime = this.serverData.ping;
            var responseTimeText = String.valueOf(responseTime);

            if (this.serverData.motd.getString().contains(LangUtils.translateString("multiplayer.status.cannot_connect")))
            {
                ping = LangUtils.formatted("menu.failed_to_ping", ChatFormatting.DARK_RED);
            }
            else if (responseTime < 0L)
            {
                ping = LangUtils.formatted("multiplayer.status.pinging", ChatFormatting.GRAY);
            }
            else if (responseTime >= 200 && responseTime < 300)
            {
                ping = TextComponentUtils.formatted(responseTimeText + "ms", ChatFormatting.YELLOW);
            }
            else if (responseTime >= 300 && responseTime < 500)
            {
                ping = TextComponentUtils.formatted(responseTimeText + "ms", ChatFormatting.RED);
            }
            else if (responseTime >= 500)
            {
                ping = TextComponentUtils.formatted(responseTimeText + "ms", ChatFormatting.DARK_RED);
            }
            else
            {
                ping = TextComponentUtils.formatted(responseTimeText + "ms", ChatFormatting.GREEN);
            }

            var s2 = flag2 ? this.serverData.version.copy().withStyle(ChatFormatting.DARK_RED) : this.serverData.status.copy().append(" ").append(ping);
            var j = this.minecraft.font.width(s2);
            this.minecraft.font.draw(poseStack, s2, x + listWidth - j - 6, y + 1, 8421504);
            var s = Collections.<Component>emptyList();

            if (flag2)
            {
                s = this.serverData.playerList;
            }
            else if (this.serverData.pinged && this.serverData.ping != -2L)
            {
                if (this.serverData.ping > 0L)
                {
                    s = this.serverData.playerList;
                }
            }

            var icon = this.serverData.getIconB64();

            if (!Objects.equals(icon, this.lastIconB64))
            {
                if (this.uploadServerIcon(icon))
                {
                    this.lastIconB64 = icon;
                }
                else
                {
                    this.serverData.setIconB64(null);
                    entry.updateServerList();
                }
            }

            if (this.icon != null)
            {
                this.drawIcon(poseStack, x, y, this.iconLocation);
            }
            else
            {
                this.drawIcon(poseStack, x, y, ServerSelectionList.ICON_MISSING);
            }

            var i1 = mouseX - x;
            var j1 = mouseY - y;

            if (i1 >= listWidth - j - 6 && i1 <= listWidth - 7 && j1 >= 0 && j1 <= 8)
            {
                this.screen.setToolTip(s);
            }

            if (this.minecraft.options.touchscreen || isSelected)
            {
                RenderSystem.setShaderTexture(0, ServerSelectionList.ICON_OVERLAY_LOCATION);
                GuiComponent.fill(poseStack, x, y, x + 32, y + 32, -1601138544);
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                var k1 = mouseX - x;
                var l1 = mouseY - y;

                if (this.canJoin())
                {
                    if (k1 < 32 && k1 > 16)
                    {
                        GuiComponent.blit(poseStack, x, y, 0.0F, 32.0F, 32, 32, 256, 256);
                    }
                    else
                    {
                        GuiComponent.blit(poseStack, x, y, 0.0F, 0.0F, 32, 32, 256, 256);
                    }
                }

                if (slotIndex > 0)
                {
                    if (k1 < 16 && l1 < 16)
                    {
                        GuiComponent.blit(poseStack, x, y, 96.0F, 32.0F, 32, 32, 256, 256);
                    }
                    else
                    {
                        GuiComponent.blit(poseStack, x, y, 96.0F, 0.0F, 32, 32, 256, 256);
                    }
                }
                if (slotIndex < this.screen.getServers().size() - 1)
                {
                    if (k1 < 16 && l1 > 16)
                    {
                        GuiComponent.blit(poseStack, x, y, 64.0F, 32.0F, 32, 32, 256, 256);
                    }
                    else
                    {
                        GuiComponent.blit(poseStack, x, y, 64.0F, 0.0F, 32, 32, 256, 256);
                    }
                }
            }
            info.cancel();
        }
    }
}