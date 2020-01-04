package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;
import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

@Mixin(MultiplayerScreen.class)
public abstract class MixinMultiplayerScreen extends Screen
{
    public MixinMultiplayerScreen(ITextComponent title)
    {
        super(title);
    }

    @Inject(method = "render(IIF)V", at = @At("RETURN"))
    private void render(int mouseX, int mouseY, float partialTicks, CallbackInfo info)
    {
        if (IndicatiaConfig.GENERAL.enableCustomServerSelectionGui.get())
        {
            RenderSystem.disableLighting();
            this.minecraft.fontRenderer.drawString(LangUtils.translate("menu.multiplayer_screen_info_1"), 4, 3, ColorUtils.hexToRGB("#17F9DB").to32Bit());
            this.minecraft.fontRenderer.drawString(LangUtils.translate("menu.multiplayer_screen_info_2"), 4, 3 + this.minecraft.fontRenderer.FONT_HEIGHT + 1, ColorUtils.hexToRGB("#17F9DB").to32Bit());
            RenderSystem.enableLighting();
        }
    }
}