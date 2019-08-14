package stevekung.mods.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.GuiIngameForge;

@Mixin(GuiIngameForge.class)
public abstract class GuiIngameForgeMixin extends GuiIngame
{
    public GuiIngameForgeMixin(Minecraft mc)
    {
        super(mc);
    }

    @Inject(method = "renderChat(II)V", cancellable = true, at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.translate(FFF)V", shift = At.Shift.AFTER))
    private void renderChatBefore(int width, int height, CallbackInfo ci)
    {
        GlStateManager.disableDepth();
    }

    @Inject(method = "renderChat(II)V", cancellable = true, at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.popMatrix()V", shift = At.Shift.BEFORE))
    private void renderChatAfter(int width, int height, CallbackInfo ci)
    {
        GlStateManager.enableDepth();
    }

    @Inject(method = "renderGameOverlay(F)V", cancellable = true, at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.color(FFFF)V", shift = At.Shift.AFTER, ordinal = 1))
    private void fixEmptyBossBarRender(float partialTicks, CallbackInfo ci)
    {
        if (!GuiIngameForge.renderBossHealth)
        {
            GlStateManager.enableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }
    }
}