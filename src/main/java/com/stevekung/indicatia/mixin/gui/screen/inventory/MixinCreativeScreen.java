package com.stevekung.indicatia.mixin.gui.screen.inventory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.stevekung.indicatia.key.KeypadChatKey;

import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.settings.KeyBinding;

@Mixin(CreativeScreen.class)
public class MixinCreativeScreen
{
    @Redirect(method = "keyPressed(III)Z", at = @At(value = "INVOKE", target = "net/minecraft/client/settings/KeyBinding.matchesKey(II)Z"))
    private boolean addAltChatKey(KeyBinding key, int keysym, int scancode)
    {
        return key.matchesKey(keysym, scancode) || KeypadChatKey.isAltChatMatches(keysym, scancode);
    }
}