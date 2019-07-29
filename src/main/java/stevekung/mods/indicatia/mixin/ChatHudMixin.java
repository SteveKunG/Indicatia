package stevekung.mods.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.hud.ChatHud;

@Mixin(ChatHud.class)
public class ChatHudMixin
{
    @Inject(at = @At("HEAD"), method = "render(I)V")
    public void renderBefore(int ticks, CallbackInfo info)
    {
        GlStateManager.disableDepthTest();
    }

    @Inject(at = @At("RETURN"), method = "render(I)V")
    public void renderAfter(int ticks, CallbackInfo info)
    {
        GlStateManager.enableDepthTest();
    }
}