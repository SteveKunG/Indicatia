package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public interface InvokerMinecraft
{
    @Accessor("fps")
    static int getFPS()
    {
        return 0;
    }
}