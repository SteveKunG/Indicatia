package com.stevekung.indicatia.mixin.player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.stevekung.indicatia.utils.AFKMode;
import com.stevekung.indicatia.utils.hud.HUDHelper;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer
{
    @Inject(method = "aiStep()V", at = @At(value = "INVOKE", target = "net/minecraft/client/player/Input.tick(Z)V"))
    private void tickMovement(CallbackInfo info)
    {
        Input input = ((LocalPlayer)(Object)this).input;

        // afk stuff
        if (HUDHelper.AFK_MODE == AFKMode.RANDOM_MOVE_360)
        {
            int afkMoveTick = HUDHelper.afkMoveTicks;

            if (afkMoveTick == 1)
            {
                input.forwardImpulse += Math.random();
                input.up = true;
            }
            else if (afkMoveTick == 3)
            {
                input.leftImpulse += Math.random();
                input.left = true;
            }
            else if (afkMoveTick == 5)
            {
                input.forwardImpulse -= Math.random();
                input.down = true;
            }
            else if (afkMoveTick == 7)
            {
                input.leftImpulse -= Math.random();
                input.right = true;
            }
        }
    }
}