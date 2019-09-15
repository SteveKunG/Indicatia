//package stevekung.mods.indicatia.mixin;
//
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import net.minecraft.client.entity.AbstractClientPlayer;
//import net.minecraft.client.model.ModelPlayer;
//import net.minecraft.client.renderer.entity.RenderLivingBase;
//import net.minecraft.client.renderer.entity.RenderManager;
//import net.minecraft.client.renderer.entity.RenderPlayer;
//import stevekung.mods.indicatia.renderer.LayerCustomCape;
//
//@Mixin(RenderPlayer.class)
//public abstract class RenderPlayerMixin extends RenderLivingBase<AbstractClientPlayer>
//{
//    private final RenderPlayer that = (RenderPlayer) (Object) this;
//
//    public RenderPlayerMixin(RenderManager manager, boolean useSmallArms)
//    {
//        super(manager, new ModelPlayer(0.0F, useSmallArms), 0.5F);
//    }
//
//    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/RenderManager;Z)V", at = @At("RETURN"))
//    private void init(CallbackInfo info)
//    {
//        this.that.addLayer(new LayerCustomCape(this.that));
//    }
//}