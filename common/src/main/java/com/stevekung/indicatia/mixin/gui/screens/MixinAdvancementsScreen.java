package com.stevekung.indicatia.mixin.gui.screens;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.stevekung.indicatia.utils.OpenFromParent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;

@Mixin(AdvancementsScreen.class)
public class MixinAdvancementsScreen extends Screen implements OpenFromParent
{
    @Unique
    private Screen parent;

    MixinAdvancementsScreen()
    {
        super(null);
    }

    @Inject(method = "keyPressed", cancellable = true, at = @At("HEAD"))
    private void indicatia$openParentScreen(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> info)
    {
        if (this.parent != null)
        {
            this.minecraft.setScreen(this.parent);
            info.setReturnValue(true);
        }
    }

    @Override
    public void setParent(Screen parent)
    {
        this.parent = parent;
    }
}