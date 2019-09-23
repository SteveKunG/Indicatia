package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.stevekungslib.utils.ColorUtils;
import com.stevekung.stevekungslib.utils.client.RenderUtils;

import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

@Mixin(MultiplayerScreen.class)
public abstract class MixinMultiplayerScreen extends Screen
{
    protected MixinMultiplayerScreen(ITextComponent title)
    {
        super(title);
    }

    @Inject(method = "render(IIF)V", at = @At("RETURN"))
    private void renderInfo(int mouseX, int mouseY, float partialTicks, CallbackInfo info)
    {
        if (IndicatiaConfig.GENERAL.enableCustomServerSelectionGui.get())
        {
            RenderUtils.disableLighting();
            String info1 = "Press <SHIFT> for";
            String info2 = "server version info";
            this.minecraft.fontRenderer.drawString(info1, 4, 3, ColorUtils.hexToRGB("#17F9DB").to32Bit());
            this.minecraft.fontRenderer.drawString(info2, 4, 3 + this.minecraft.fontRenderer.FONT_HEIGHT + 1, ColorUtils.hexToRGB("#17F9DB").to32Bit());
            RenderUtils.enableLighting();
        }
    }
}