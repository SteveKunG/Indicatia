package com.stevekung.indicatia.mixin.gui.screens;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import com.stevekung.indicatia.gui.screens.DisconnectConfirmationScreen;
import com.stevekung.indicatia.utils.PlatformConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Mixin(PauseScreen.class)
public class MixinPauseScreen extends Screen
{
    private MixinPauseScreen()
    {
        super(null);
    }

    @Redirect(method = "createPauseMenu()V", slice = @Slice(from = @At(value = "INVOKE", target = "net/minecraft/client/server/IntegratedServer.isPublished()Z"), to = @At(value = "INVOKE", target = "net/minecraft/client/Minecraft.isLocalServer()Z")), at = @At(value = "NEW", target = "net/minecraft/client/gui/components/Button"))
    private Button replacedOnPress(int x, int y, int width, int height, Component title, Button.OnPress onPress)
    {
        return new Button(x, y, width, height, title, PlatformConfig.getConfirmToDisconnect() && !this.minecraft.isLocalServer() ? button2 -> this.minecraft.setScreen(new DisconnectConfirmationScreen(this)) : onPress);
    }
}