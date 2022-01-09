package com.stevekung.indicatia.forge.mixin.gui.screens;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import com.stevekung.indicatia.forge.config.IndicatiaConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

@Mixin(PauseScreen.class)
public class MixinPauseScreen extends Screen
{
    private static final Component TITLE = new TranslatableComponent("menu.confirm_disconnect");

    MixinPauseScreen()
    {
        super(null);
    }

    @Redirect(method = "createPauseMenu", slice = @Slice(from = @At(value = "INVOKE", target = "net/minecraft/client/server/IntegratedServer.isPublished()Z"), to = @At("TAIL")), at = @At(value = "NEW", target = "net/minecraft/client/gui/components/Button"))
    private Button indicatia$replaceDisconnectButton(int x, int y, int width, int height, Component title, Button.OnPress onPress)
    {
        return new Button(x, y, width, height, title, IndicatiaConfig.CONFIG.confirmationOnDisconnect.get() && !this.minecraft.isLocalServer() ? button -> this.minecraft.setScreen(new ConfirmScreen(yes ->
        {
            if (yes)
            {
                onPress.onPress(button);
            }
        }, TITLE, TextComponent.EMPTY, CommonComponents.GUI_YES, CommonComponents.GUI_CANCEL)) : onPress);
    }
}