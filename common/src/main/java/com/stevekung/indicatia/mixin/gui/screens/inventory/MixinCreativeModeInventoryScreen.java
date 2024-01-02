package com.stevekung.indicatia.mixin.gui.screens.inventory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.stevekung.indicatia.utils.PlatformKeyInput;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;

@Mixin(CreativeModeInventoryScreen.class)
public class MixinCreativeModeInventoryScreen
{
    @SuppressWarnings("ConstantValue")
    @ModifyExpressionValue(method = "keyPressed", at = @At(value = "INVOKE", target = "net/minecraft/client/KeyMapping.matches(II)Z"))
    private boolean indicatia$addAltChatKey(boolean original, int keyCode, int scanCode)
    {
        return original || PlatformKeyInput.isAltChatMatches(keyCode, scanCode);
    }
}