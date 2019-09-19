package stevekung.mods.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Style;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.config.HealthStatusMode;
import stevekung.mods.indicatia.utils.InfoUtils;
import stevekung.mods.indicatia.utils.RenderUtilsIN;
import stevekung.mods.stevekungslib.utils.JsonUtils;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity>
{
    @Inject(at = @At("RETURN"), method = "method_4041(Lnet/minecraft/entity/LivingEntity;DDD)V")
    public void draw(T entity, double x, double y, double z, CallbackInfo info)
    {
        MinecraftClient mc = MinecraftClient.getInstance();
        float health = entity.getHealth();
        boolean halfHealth = health <= entity.getMaximumHealth() / 2F;
        boolean halfHealth1 = health <= entity.getMaximumHealth() / 4F;
        double range = 32.0D;
        double distance = entity.squaredDistanceTo(mc.getCameraEntity());

        if (entity.isSneaking())
        {
            distance /= 2;
        }

        HealthStatusMode mode = ExtendedConfig.instance.healthStatusMode;
        boolean flag = mode != HealthStatusMode.DISABLED && (mode != HealthStatusMode.POINTED || entity == InfoUtils.INSTANCE.extendedPointedEntity);
        Style color = halfHealth ? JsonUtils.red() : halfHealth1 ? JsonUtils.darkRed() : JsonUtils.green();

        if (distance < range * range)
        {
            if (!mc.options.hudHidden && !entity.isInvisible() && flag && !(entity instanceof ClientPlayerEntity || entity instanceof ArmorStandEntity) && !InfoUtils.INSTANCE.isHypixel())
            {
                String heart = JsonUtils.create("\u2764 ").setStyle(color).asFormattedString();
                RenderUtilsIN.renderEntityHealth(entity, heart + String.format("%.1f", health), x, y, z);
            }
        }
    }
}