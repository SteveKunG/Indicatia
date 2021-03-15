package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.gui.screens.TitleScreen;

@Mixin(TitleScreen.class)
public interface InvokerTitleScreen
{
    @Accessor("fading")
    boolean getFading();

    @Accessor("fadeInStart")
    long getFadeInStart();
}