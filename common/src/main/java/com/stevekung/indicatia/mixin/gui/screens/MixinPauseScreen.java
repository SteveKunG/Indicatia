package com.stevekung.indicatia.mixin.gui.screens;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import com.stevekung.indicatia.Indicatia;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Mixin(PauseScreen.class)
public class MixinPauseScreen extends Screen
{
    MixinPauseScreen()
    {
        super(null);
    }

    @ModifyArg(method = "createPauseMenu", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/components/Button.builder(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button$Builder;", ordinal = 1), index = 1)
    private Button.OnPress indicatia$replaceDisconnectButton(Button.OnPress originalOnPress)
    {
        return Indicatia.CONFIG.confirmationOnDisconnect && !this.minecraft.isLocalServer() ? button -> this.minecraft.setScreen(new ConfirmScreen(yes ->
        {
            if (yes)
            {
                originalOnPress.onPress(button);
            }
            else
            {
                this.minecraft.setScreen(this);
            }
        }, Component.translatable("menu.confirm_disconnect"), Component.empty(), CommonComponents.GUI_YES, CommonComponents.GUI_CANCEL)) : originalOnPress;
    }
}