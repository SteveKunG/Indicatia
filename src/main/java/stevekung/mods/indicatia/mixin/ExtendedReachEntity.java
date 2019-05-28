package stevekung.mods.indicatia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.config.HealthStatusMode;
import stevekung.mods.indicatia.utils.InfoUtils;

@Mixin(MinecraftClient.class)
public class ExtendedReachEntity<T extends LivingEntity>
{
    @Inject(at = @At("RETURN"), method = "render(Z)V")
    public void draw(boolean render, CallbackInfo info)
    {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (!mc.skipGameRender && ExtendedConfig.instance.healthStatusMode == HealthStatusMode.POINTED)
        {
            InfoUtils.INSTANCE.processMouseOverEntity(mc);
        }
    }
}