package com.stevekung.indicatia.mixin;

import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.*;

import com.mojang.blaze3d.systems.RenderSystem;
import com.stevekung.indicatia.config.IndicatiaConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.ClientBossInfo;
import net.minecraft.client.gui.overlay.BossOverlayGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.BossInfo;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

@Mixin(BossOverlayGui.class)
public abstract class MixinBossOverlayGui extends AbstractGui
{
    @Shadow
    @Final
    @Mutable
    private Minecraft client;

    @Shadow
    @Final
    @Mutable
    private Map<UUID, ClientBossInfo> mapBossInfos;

    @Shadow
    @Final
    @Mutable
    private static ResourceLocation GUI_BARS_TEXTURES;

    @Shadow
    protected abstract void render(int x, int y, BossInfo info);

    @Overwrite
    public void render()
    {
        boolean render = IndicatiaConfig.GENERAL.enableBossHealthBarRender.get();

        if (!this.mapBossInfos.isEmpty())
        {
            int width = this.client.getMainWindow().getScaledWidth();
            int height = this.client.getMainWindow().getScaledHeight();
            int baseHeight = 12;

            for (ClientBossInfo bossInfo : this.mapBossInfos.values())
            {
                int realWidth = width / 2 - 91;
                RenderGameOverlayEvent.BossInfo event = ForgeHooksClient.bossBarRenderPre(this.client.getMainWindow(), bossInfo, realWidth, baseHeight, 10 + this.client.fontRenderer.FONT_HEIGHT);

                if (!event.isCanceled())
                {
                    if (render)
                    {
                        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                        this.client.getTextureManager().bindTexture(GUI_BARS_TEXTURES);
                        this.render(realWidth, baseHeight, bossInfo);
                    }
                    String bossName = bossInfo.getName().getFormattedText();
                    this.client.fontRenderer.drawStringWithShadow(bossName, width / 2 - this.client.fontRenderer.getStringWidth(bossName) / 2, baseHeight - 9, 16777215);
                }

                baseHeight += render ? event.getIncrement() : 12;
                ForgeHooksClient.bossBarRenderPost(this.client.getMainWindow());

                if (baseHeight >= height / (render ? 3.0D : 4.5D))
                {
                    break;
                }
            }
        }
    }
}