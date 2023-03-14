package com.stevekung.indicatia.mixin.gui.screens;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.stevekung.indicatia.Indicatia;
import com.stevekung.indicatia.utils.OpenFromParent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Mixin(PauseScreen.class)
public class MixinPauseScreen extends Screen
{
    MixinPauseScreen()
    {
        super(null);
    }

    @Redirect(method = "createPauseMenu", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/components/Button.builder(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button$Builder;", ordinal = 1))
    private Button.Builder indicatia$replaceDisconnectButton(Component title, Button.OnPress onPress)
    {
        return Button.builder(title, Indicatia.CONFIG.confirmationOnDisconnect && !this.minecraft.isLocalServer() ? button -> this.minecraft.setScreen(new ConfirmScreen(yes ->
        {
            if (yes)
            {
                onPress.onPress(button);
            }
            else
            {
                this.minecraft.setScreen(this);
            }
        }, Component.translatable("menu.confirm_disconnect"), Component.empty(), CommonComponents.GUI_YES, CommonComponents.GUI_CANCEL)) : onPress);
    }

    @SuppressWarnings("target")
    @Redirect(method = "method_47896()Lnet/minecraft/client/gui/screens/Screen;", at = @At(value = "NEW", target = "net/minecraft/client/gui/screens/advancements/AdvancementsScreen"))
    private AdvancementsScreen indicatia$setParentScreen(ClientAdvancements advancements)
    {
        var screen = new AdvancementsScreen(advancements);
        ((OpenFromParent)screen).setParent(this);
        return screen;
    }
}