package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.stevekung.indicatia.key.KeypadChatKey;

import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.settings.KeyBinding;

@Mixin(RecipeBookGui.class)
public abstract class MixinRecipeBookGui
{
    @Redirect(method = "keyPressed(III)Z", at = @At(value = "INVOKE", target = "net/minecraft/client/settings/KeyBinding.matchesKey(II)Z"))
    private boolean addAltChatKey(KeyBinding key, int keysym, int scancode)
    {
        return key.matchesKey(keysym, scancode) || KeypadChatKey.isAltChatMatches(keysym, scancode);
    }
}