package com.stevekung.indicatia.mixin;

import java.net.UnknownHostException;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;
import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.ClientHooks;

@Mixin(ServerSelectionList.NormalEntry.class)
public abstract class MixinServerSelectionList_NormalEntry extends ServerSelectionList.Entry
{
    @Shadow
    @Final
    @Mutable
    private MultiplayerScreen owner;

    @Shadow
    @Final
    @Mutable
    private Minecraft mc;

    @Shadow
    @Final
    @Mutable
    private ServerData server;

    @Shadow
    @Final
    @Mutable
    private ResourceLocation serverIcon;

    @Shadow
    private String lastIconB64;

    @Shadow
    private DynamicTexture icon;

    @Shadow
    protected abstract void prepareServerIcon();

    @Shadow
    protected abstract void drawTextureAt(int x, int y, ResourceLocation resource);

    @Shadow
    protected abstract boolean canJoin();

    @Inject(method = "render(IIIIIIIZF)V", cancellable = true, at = @At("HEAD"))
    private void render(int slotIndex, int y, int x, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks, CallbackInfo info)
    {
        if (IndicatiaConfig.GENERAL.multiplayerScreenEnhancement.get())
        {
            if (!this.server.pinged)
            {
                this.server.pinged = true;
                this.server.pingToServer = -2L;
                this.server.serverMOTD = "";
                this.server.populationInfo = "";

                ServerSelectionList.field_214358_b.submit(() ->
                {
                    try
                    {
                        this.owner.getOldServerPinger().ping(this.server);
                    }
                    catch (UnknownHostException e)
                    {
                        this.server.pingToServer = -1L;
                        this.server.serverMOTD = TextFormatting.DARK_RED + LangUtils.translate("multiplayer.status.cannot_resolve");
                    }
                    catch (Exception e)
                    {
                        this.server.pingToServer = -1L;
                        this.server.serverMOTD = TextFormatting.DARK_RED + LangUtils.translate("multiplayer.status.cannot_connect");
                    }
                });
            }

            boolean flag = this.server.version > SharedConstants.getVersion().getProtocolVersion();
            boolean flag1 = this.server.version < SharedConstants.getVersion().getProtocolVersion();
            boolean flag2 = flag || flag1;
            this.mc.fontRenderer.drawString(this.server.serverName, x + 32 + 3, y + 1, 16777215);
            List<String> list = this.mc.fontRenderer.listFormattedStringToWidth(this.server.serverMOTD, listWidth - 50);

            for (int i = 0; i < Math.min(list.size(), 2); ++i)
            {
                this.mc.fontRenderer.drawString(list.get(i), x + 32 + 3, y + 12 + 9 * i, 8421504);
            }

            String ping = "";
            long responseTime = this.server.pingToServer;
            String responseTimeText = String.valueOf(responseTime);

            if (this.server.serverMOTD.contains(LangUtils.translate("multiplayer.status.cannot_connect")))
            {
                ping = TextFormatting.DARK_RED + LangUtils.translate("menu.failed_to_ping");
            }
            else if (responseTime < 0L)
            {
                ping = TextFormatting.GRAY + LangUtils.translate("multiplayer.status.pinging");
            }
            else if (responseTime >= 200 && responseTime < 300)
            {
                ping = TextFormatting.YELLOW + responseTimeText + "ms";
            }
            else if (responseTime >= 300 && responseTime < 500)
            {
                ping = TextFormatting.RED + responseTimeText + "ms";
            }
            else if (responseTime >= 500)
            {
                ping = TextFormatting.DARK_RED + responseTimeText + "ms";
            }
            else
            {
                ping = TextFormatting.GREEN + responseTimeText + "ms";
            }

            String s2 = flag2 ? TextFormatting.DARK_RED + this.server.gameVersion : this.server.populationInfo + " " + ping;
            int j = this.mc.fontRenderer.getStringWidth(s2);
            this.mc.fontRenderer.drawString(s2, x + listWidth - j - 6, y + 1, 8421504);
            String s = null;

            if (flag2)
            {
                s = this.server.playerList;
            }
            else if (this.server.pinged && this.server.pingToServer != -2L)
            {
                if (this.server.pingToServer > 0L)
                {
                    s = this.server.playerList;
                }
            }

            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            if (this.server.getBase64EncodedIconData() != null && !this.server.getBase64EncodedIconData().equals(this.lastIconB64))
            {
                this.lastIconB64 = this.server.getBase64EncodedIconData();
                this.prepareServerIcon();
                this.owner.getServerList().saveServerList();
            }

            if (this.icon != null)
            {
                this.drawTextureAt(x, y, this.serverIcon);
            }
            else
            {
                this.drawTextureAt(x, y, ServerSelectionList.field_214359_c);
            }

            int i1 = mouseX - x;
            int j1 = mouseY - y;

            if (i1 >= listWidth - j - 6 && i1 <= listWidth - 7 && j1 >= 0 && j1 <= 8)
            {
                this.owner.setHoveringText(s);
            }

            ClientHooks.drawForgePingInfo(this.owner, this.server, x, y, listWidth, i1, j1);

            if (this.mc.gameSettings.touchscreen || isSelected)
            {
                this.mc.getTextureManager().bindTexture(ServerSelectionList.field_214360_d);
                AbstractGui.fill(x, y, x + 32, y + 32, -1601138544);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                int k1 = mouseX - x;
                int l1 = mouseY - y;

                if (this.canJoin())
                {
                    if (k1 < 32 && k1 > 16)
                    {
                        AbstractGui.blit(x, y, 0.0F, 32.0F, 32, 32, 256, 256);
                    }
                    else
                    {
                        AbstractGui.blit(x, y, 0.0F, 0.0F, 32, 32, 256, 256);
                    }
                }

                if (slotIndex > 0)
                {
                    if (k1 < 16 && l1 < 16)
                    {
                        AbstractGui.blit(x, y, 96.0F, 32.0F, 32, 32, 256, 256);
                    }
                    else
                    {
                        AbstractGui.blit(x, y, 96.0F, 0.0F, 32, 32, 256, 256);
                    }
                }
                if (slotIndex < this.owner.getServerList().countServers() - 1)
                {
                    if (k1 < 16 && l1 > 16)
                    {
                        AbstractGui.blit(x, y, 64.0F, 32.0F, 32, 32, 256, 256);
                    }
                    else
                    {
                        AbstractGui.blit(x, y, 64.0F, 0.0F, 32, 32, 256, 256);
                    }
                }
            }
            info.cancel();
        }
    }
}