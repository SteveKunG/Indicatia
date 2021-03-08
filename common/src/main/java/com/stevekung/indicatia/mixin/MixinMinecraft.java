package com.stevekung.indicatia.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import com.stevekung.indicatia.key.KeypadChatKey;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

@Mixin(Minecraft.class)
public class MixinMinecraft
{
    @Redirect(method = "processKeyBinds()V", slice = @Slice(from = @At(value = "FIELD", target = "net/minecraft/client/GameSettings.keyBindChat:Lnet/minecraft/client/settings/KeyBinding;", opcode = Opcodes.GETFIELD), to = @At(value = "INVOKE", target = "net/minecraft/client/Minecraft.openChatScreen(Ljava/lang/String;)V", ordinal = 0)), at = @At(value = "INVOKE", target = "net/minecraft/client/settings/KeyBinding.isPressed()Z"))
    private boolean addAltChatKey(KeyBinding key)
    {
        return key.isPressed() || KeypadChatKey.isAltChatEnabled();
    }
}