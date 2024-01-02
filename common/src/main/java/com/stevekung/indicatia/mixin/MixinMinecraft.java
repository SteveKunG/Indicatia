package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.stevekung.indicatia.utils.PlatformKeyInput;
import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MixinMinecraft
{
    @SuppressWarnings("ConstantValue")
    @ModifyExpressionValue(method = "handleKeybinds", slice = @Slice(from = @At(value = "FIELD", target = "net/minecraft/client/Options.keyChat:Lnet/minecraft/client/KeyMapping;"), to = @At(value = "INVOKE", target = "net/minecraft/client/Minecraft.openChatScreen(Ljava/lang/String;)V", ordinal = 0)), at = @At(value = "INVOKE", target = "net/minecraft/client/KeyMapping.consumeClick()Z"))
    private boolean indicatia$addAltChatKey(boolean original)
    {
        return original || PlatformKeyInput.isAltChatEnabled();
    }
}