package com.stevekung.indicatia.mixin.gui.screens;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

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

    @ModifyArg(method = "createPauseMenu", at = @At(
            value = "INVOKE",
            target = "net/minecraft/client/gui/components/Button.builder(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button$Builder;",
            ordinal = 0),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "net/minecraft/network/chat/CommonComponents.disconnectButtonLabel(Z)Lnet/minecraft/network/chat/Component;",
                            shift = At.Shift.BEFORE)),
            index = 1)
    private Button.OnPress indicatia$replaceDisconnectButton(Button.OnPress originalOnPress)
    {
        return Indicatia.CONFIG.confirmationOnDisconnect && !this.minecraft.isLocalServer() ? button -> this.minecraft.gui.setScreen(new ConfirmScreen(yes ->
        {
            if (yes)
            {
                originalOnPress.onPress(button);
            }
            else
            {
                this.minecraft.gui.setScreen(this);
            }
        }, Component.translatable("menu.confirm_disconnect"), Component.empty(), CommonComponents.GUI_YES, CommonComponents.GUI_CANCEL)) : originalOnPress;
    }
}