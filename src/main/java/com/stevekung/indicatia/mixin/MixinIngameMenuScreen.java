package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import com.stevekung.indicatia.config.IndicatiaConfig;
import com.stevekung.indicatia.gui.screen.DisconnectConfirmationScreen;

import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

@Mixin(IngameMenuScreen.class)
public class MixinIngameMenuScreen extends Screen
{
    private MixinIngameMenuScreen()
    {
        super(null);
    }

    @Redirect(method = "addButtons()V", slice = @Slice(from = @At(value = "INVOKE", target = "net/minecraft/server/integrated/IntegratedServer.getPublic()Z"), to = @At(value = "INVOKE", target = "net/minecraft/client/Minecraft.isIntegratedServerRunning()Z")), at = @At(value = "NEW", target = "net/minecraft/client/gui/widget/button/Button"))
    private Button replacedPressedAction(int x, int y, int width, int height, ITextComponent title, Button.IPressable pressedAction)
    {
        return new Button(x, y, width, height, title, IndicatiaConfig.GENERAL.enableConfirmToDisconnect.get() && !this.minecraft.isIntegratedServerRunning() ? button2 -> this.minecraft.displayGuiScreen(new DisconnectConfirmationScreen(this)) : pressedAction);
    }
}