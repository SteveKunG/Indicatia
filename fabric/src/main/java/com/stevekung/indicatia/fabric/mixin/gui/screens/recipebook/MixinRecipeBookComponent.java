package com.stevekung.indicatia.fabric.mixin.gui.screens.recipebook;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.stevekung.indicatia.fabric.utils.KeypadHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;

@Mixin(RecipeBookComponent.class)
public class MixinRecipeBookComponent
{
    @Redirect(method = "keyPressed", at = @At(value = "INVOKE", target = "net/minecraft/client/KeyMapping.matches(II)Z"))
    private boolean indicatia$addAltChatKey(KeyMapping key, int keysym, int scancode)
    {
        return key.matches(keysym, scancode) || KeypadHandler.isAltChatMatches(keysym, scancode);
    }
}