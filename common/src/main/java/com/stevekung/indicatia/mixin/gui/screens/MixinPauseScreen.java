package com.stevekung.indicatia.mixin.gui.screens;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import com.stevekung.indicatia.core.Indicatia;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Mixin(PauseScreen.class)
public class MixinPauseScreen extends Screen
{
    private static final Component TITLE = Component.translatable("menu.confirm_disconnect");

    MixinPauseScreen()
    {
        super(null);
    }

    @Redirect(method = "createPauseMenu", slice = @Slice(from = @At(value = "INVOKE", target = "net/minecraft/client/server/IntegratedServer.isPublished()Z"), to = @At("TAIL")), at = @At(value = "NEW", target = "net/minecraft/client/gui/components/Button"))
    private Button indicatia$replaceDisconnectButton(int x, int y, int width, int height, Component title, Button.OnPress onPress)
    {
        return new Button(x, y, width, height, title, Indicatia.CONFIG.confirmationOnDisconnect && !this.minecraft.isLocalServer() ? button -> this.minecraft.setScreen(new ConfirmScreen(yes ->
        {
            if (yes)
            {
                onPress.onPress(button);
            }
            else
            {
                this.minecraft.setScreen(this);
            }
        }, TITLE, Component.empty(), CommonComponents.GUI_YES, CommonComponents.GUI_CANCEL)) : onPress);
    }
}