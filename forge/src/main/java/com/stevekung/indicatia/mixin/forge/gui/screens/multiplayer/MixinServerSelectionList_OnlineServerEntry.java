package com.stevekung.indicatia.mixin.forge.gui.screens.multiplayer;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.stevekungslib.utils.LangUtils;
import com.stevekung.stevekungslib.utils.TextComponentUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.fml.client.ClientHooks;

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
    abstract void drawIcon(PoseStack matrixStack, int x, int y, ResourceLocation resource);

    @Shadow
    abstract boolean uploadServerIcon(String icon);

    @Shadow
    abstract boolean canJoin();

    @SuppressWarnings("deprecation")
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIIIIZF)V", cancellable = true, at = @At("HEAD"))
    private void render(PoseStack matrixStack, int slotIndex, int y, int x, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks, CallbackInfo info)
    {
        ServerSelectionList.OnlineServerEntry entry = (ServerSelectionList.OnlineServerEntry) (Object) this;

        if (IndicatiaConfig.GENERAL.multiplayerScreenEnhancement.get())
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

            boolean flag = this.serverData.protocol > SharedConstants.getCurrentVersion().getProtocolVersion();
            boolean flag1 = this.serverData.protocol < SharedConstants.getCurrentVersion().getProtocolVersion();
            boolean flag2 = flag || flag1;
            this.minecraft.font.draw(matrixStack, this.serverData.name, x + 32 + 3, y + 1, 16777215);
            List<FormattedCharSequence> list = this.minecraft.font.split(this.serverData.motd, listWidth - 50);

            for (int i = 0; i < Math.min(list.size(), 2); ++i)
            {
                this.minecraft.font.draw(matrixStack, list.get(i), x + 35, y + 12 + 9 * i, 8421504);
            }

            Component ping;
            long responseTime = this.serverData.ping;
            String responseTimeText = String.valueOf(responseTime);

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

            Component s2 = flag2 ? this.serverData.version.copy().withStyle(ChatFormatting.DARK_RED) : this.serverData.status.copy().append(" ").append(ping);
            int j = this.minecraft.font.width(s2);
            this.minecraft.font.draw(matrixStack, s2, x + listWidth - j - 6, y + 1, 8421504);
            List<Component> s = Collections.emptyList();

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

            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            String icon = this.serverData.getIconB64();

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
                this.drawIcon(matrixStack, x, y, this.iconLocation);
            }
            else
            {
                this.drawIcon(matrixStack, x, y, ServerSelectionList.ICON_MISSING);
            }

            int i1 = mouseX - x;
            int j1 = mouseY - y;

            if (i1 >= listWidth - j - 6 && i1 <= listWidth - 7 && j1 >= 0 && j1 <= 8)
            {
                this.screen.setToolTip(s);
            }

            ClientHooks.drawForgePingInfo(this.screen, this.serverData, matrixStack, x, y, listWidth, i1, j1);

            if (this.minecraft.options.touchscreen || isSelected)
            {
                this.minecraft.getTextureManager().bind(ServerSelectionList.ICON_OVERLAY_LOCATION);
                GuiComponent.fill(matrixStack, x, y, x + 32, y + 32, -1601138544);
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                int k1 = mouseX - x;
                int l1 = mouseY - y;

                if (this.canJoin())
                {
                    if (k1 < 32 && k1 > 16)
                    {
                        GuiComponent.blit(matrixStack, x, y, 0.0F, 32.0F, 32, 32, 256, 256);
                    }
                    else
                    {
                        GuiComponent.blit(matrixStack, x, y, 0.0F, 0.0F, 32, 32, 256, 256);
                    }
                }

                if (slotIndex > 0)
                {
                    if (k1 < 16 && l1 < 16)
                    {
                        GuiComponent.blit(matrixStack, x, y, 96.0F, 32.0F, 32, 32, 256, 256);
                    }
                    else
                    {
                        GuiComponent.blit(matrixStack, x, y, 96.0F, 0.0F, 32, 32, 256, 256);
                    }
                }
                if (slotIndex < this.screen.getServers().size() - 1)
                {
                    if (k1 < 16 && l1 > 16)
                    {
                        GuiComponent.blit(matrixStack, x, y, 64.0F, 32.0F, 32, 32, 256, 256);
                    }
                    else
                    {
                        GuiComponent.blit(matrixStack, x, y, 64.0F, 0.0F, 32, 32, 256, 256);
                    }
                }
            }
            info.cancel();
        }
    }
}