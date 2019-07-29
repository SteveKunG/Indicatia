package stevekung.mods.indicatia.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.model.Box;
import net.minecraft.client.model.Cuboid;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.StuckArrowsFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.MathHelper;
import stevekung.mods.stevekungslib.utils.client.RenderUtils;

@Mixin(StuckArrowsFeatureRenderer.class)
public abstract class StuckArrowsFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M>
{
    @Shadow
    @Final
    private EntityRenderDispatcher field_17153;

    public StuckArrowsFeatureRendererMixin(LivingEntityRenderer<T, M> renderer)
    {
        super(renderer);
    }

    @Overwrite
    public void method_17158(LivingEntity living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        int count = living.getStuckArrows();

        if (count > 0)
        {
            Entity entity = new ArrowEntity(living.world, living.x, living.y, living.z);
            Random rand = new Random(living.getEntityId());
            RenderUtils.disableLighting();

            for (int i = 0; i < count; ++i)
            {
                GlStateManager.pushMatrix();
                Cuboid cuboid_1 = this.getModel().getRandomCuboid(rand);
                Box box_1 = cuboid_1.boxes.get(rand.nextInt(cuboid_1.boxes.size()));
                cuboid_1.applyTransform(0.0625F);
                float float_8 = rand.nextFloat();
                float float_9 = rand.nextFloat();
                float float_10 = rand.nextFloat();
                float float_11 = MathHelper.lerp(float_8, box_1.xMin, box_1.xMax) / 16.0F;
                float float_12 = MathHelper.lerp(float_9, box_1.yMin, box_1.yMax) / 16.0F;
                float float_13 = MathHelper.lerp(float_10, box_1.zMin, box_1.zMax) / 16.0F;
                GlStateManager.translatef(float_11, float_12, float_13);
                float_8 = float_8 * 2.0F - 1.0F;
                float_9 = float_9 * 2.0F - 1.0F;
                float_10 = float_10 * 2.0F - 1.0F;
                float_8 *= -1.0F;
                float_9 *= -1.0F;
                float_10 *= -1.0F;
                float float_14 = MathHelper.sqrt(float_8 * float_8 + float_10 * float_10);
                entity.yaw = (float)(Math.atan2(float_8, float_10) * 57.2957763671875D);
                entity.pitch = (float)(Math.atan2(float_9, float_14) * 57.2957763671875D);
                entity.prevYaw = entity.yaw;
                entity.prevPitch = entity.pitch;
                this.field_17153.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
                GlStateManager.popMatrix();
            }
            RenderUtils.enableLighting();
        }
    }
}