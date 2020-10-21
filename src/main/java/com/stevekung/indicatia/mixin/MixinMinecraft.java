package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.stevekung.indicatia.key.KeypadChatKey;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft
{
    @Redirect(method = "processKeyBinds()V", at = @At(value = "INVOKE", target = "net/minecraft/client/settings/KeyBinding.isPressed()Z", ordinal = 7))
    private boolean addAltChatKey(KeyBinding key)
    {
        return key.isPressed() || KeypadChatKey.isAltChatEnabled();
    }
}