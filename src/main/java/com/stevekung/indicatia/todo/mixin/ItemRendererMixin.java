//package stevekung.mods.indicatia.mixin;
//
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import net.minecraft.client.entity.AbstractClientPlayer;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.ItemRenderer;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.EnumHand;
//import net.minecraft.util.math.MathHelper;
//import stevekung.mods.indicatia.config.ConfigManagerIN;
//
//@Mixin(ItemRenderer.class)
//public abstract class ItemRendererMixin
//{
//    @Inject(method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V", cancellable = true, at =
//        {
//                @At(value = "INVOKE", target = "net/minecraft/client/renderer/ItemRenderer.transformSideFirstPerson(Lnet/minecraft/util/EnumHandSide;F)V", shift = At.Shift.AFTER, ordinal = 0),
//                @At(value = "INVOKE", target = "net/minecraft/client/renderer/ItemRenderer.transformSideFirstPerson(Lnet/minecraft/util/EnumHandSide;F)V", shift = At.Shift.AFTER, ordinal = 1),
//                @At(value = "INVOKE", target = "net/minecraft/client/renderer/ItemRenderer.transformSideFirstPerson(Lnet/minecraft/util/EnumHandSide;F)V", shift = At.Shift.AFTER, ordinal = 2),
//                @At(value = "INVOKE", target = "net/minecraft/client/renderer/ItemRenderer.transformSideFirstPerson(Lnet/minecraft/util/EnumHandSide;F)V", shift = At.Shift.AFTER, ordinal = 3)
//        })
//    private void onItemUse(AbstractClientPlayer player, float partialTicks, float rotationPitch, EnumHand hand, float swingProgress, ItemStack itemStack, float equipProgress, CallbackInfo ci)
//    {
//        float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
//        float f1 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float)Math.PI);
//        this.swingHandOldAnimation(equipProgress, f, f1);
//    }
//
//    private void swingHandOldAnimation(float equipProgress, float f, float f1)
//    {
//        if (!ConfigManagerIN.indicatia_general.enableBlockhitAnimation)
//        {
//            return;
//        }
//        GlStateManager.translate(0.0F, equipProgress * 0.6F, 0.0F);
//        GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotate(f * 20.0F, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotate(f1 * 20.0F, 0.0F, 0.0F, 1.0F);
//        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
//    }
//}