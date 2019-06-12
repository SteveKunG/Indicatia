package stevekung.mods.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.PlayerListHud;
import stevekung.mods.indicatia.gui.overlay.PlayerListHudNew;

@Mixin(InGameHud.class)
public class CustomPlayerListHUD
{
    @Shadow
    private PlayerListHud playerListHud;

    @Inject(at = @At("RETURN"), method = "(Lnet/minecraft/client/MinecraftClient;)V")
    public void draw(CallbackInfo info)
    {
        this.playerListHud = new PlayerListHudNew();
    }
}