package com.stevekung.indicatia.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.stevekung.indicatia.config.IndicatiaConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.FishRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Mixin(FishRenderer.class)
public abstract class MixinFishRenderer extends EntityRenderer<Entity>
{
    @Shadow
    @Final
    @Mutable
    private static RenderType field_229103_e_;

    public MixinFishRenderer(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public void func_225623_a_(Entity entity, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int color)
    {
        PlayerEntity player = ((FishingBobberEntity)entity).getAngler();

        if (player != null)
        {
            stack.func_227860_a_();
            stack.func_227860_a_();
            stack.func_227862_a_(0.5F, 0.5F, 0.5F);
            stack.func_227863_a_(this.renderManager.func_229098_b_());
            stack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(180.0F));
            MatrixStack.Entry matrixstack$entry = stack.func_227866_c_();
            Matrix4f matrix4f = matrixstack$entry.func_227870_a_();
            Matrix3f matrix3f = matrixstack$entry.func_227872_b_();
            IVertexBuilder ivertexbuilder = buffer.getBuffer(field_229103_e_);
            MixinFishRenderer.func_229106_a_(ivertexbuilder, matrix4f, matrix3f, color, 0.0F, 0, 0, 1);
            MixinFishRenderer.func_229106_a_(ivertexbuilder, matrix4f, matrix3f, color, 1.0F, 0, 1, 1);
            MixinFishRenderer.func_229106_a_(ivertexbuilder, matrix4f, matrix3f, color, 1.0F, 1, 1, 0);
            MixinFishRenderer.func_229106_a_(ivertexbuilder, matrix4f, matrix3f, color, 0.0F, 1, 0, 0);
            stack.func_227865_b_();
            int i = player.getPrimaryHand() == HandSide.RIGHT ? 1 : -1;
            ItemStack itemstack = player.getHeldItemMainhand();

            if (!(itemstack.getItem() instanceof FishingRodItem))
            {
                i = -i;
            }

            float f = player.getSwingProgress(partialTicks);
            float f1 = MathHelper.sin(MathHelper.sqrt(f) * (float)Math.PI);
            float f2 = MathHelper.lerp(partialTicks, player.prevRenderYawOffset, player.renderYawOffset) * ((float)Math.PI / 180F);
            double d0 = MathHelper.sin(f2);
            double d1 = MathHelper.cos(f2);
            double d2 = i * 0.35D;
            double d4;
            double d5;
            double d6;
            float f3;
            float dz = 0.0F;

            if ((this.renderManager.options == null || this.renderManager.options.thirdPersonView <= 0) && player == Minecraft.getInstance().player)
            {
                double d7 = this.renderManager.options.fov;
                d7 = d7 / 100.0D;
                Vec3d vec3d = IndicatiaConfig.GENERAL.enableOldFishingRodRender.get() ? new Vec3d(i * -0.5D * d7, 0.025D * d7, 0.65D) : new Vec3d(i * -0.36D * d7, -0.045D * d7, 0.4D);
                vec3d = vec3d.rotatePitch(-MathHelper.lerp(partialTicks, player.prevRotationPitch, player.rotationPitch) * ((float)Math.PI / 180F));
                vec3d = vec3d.rotateYaw(-MathHelper.lerp(partialTicks, player.prevRotationYaw, player.rotationYaw) * ((float)Math.PI / 180F));
                vec3d = vec3d.rotateYaw(f1 * 0.5F);
                vec3d = vec3d.rotatePitch(-f1 * 0.7F);
                d4 = MathHelper.lerp(partialTicks, player.prevPosX, player.func_226277_ct_()) + vec3d.x;
                d5 = MathHelper.lerp(partialTicks, player.prevPosY, player.func_226278_cu_()) + vec3d.y;
                d6 = MathHelper.lerp(partialTicks, player.prevPosZ, player.func_226281_cx_()) + vec3d.z;
                f3 = player.getEyeHeight();
            }
            else
            {
                double xz = player.isCrouching() ? 0.775D : IndicatiaConfig.GENERAL.enableOldFishingRodRender.get() ? 0.9D : 0.8D;//TODO
                d4 = MathHelper.lerp(partialTicks, player.prevPosX, player.func_226277_ct_()) - d1 * d2 - d0 * xz;
                d5 = player.prevPosY + player.getEyeHeight() + (player.func_226278_cu_() - player.prevPosY) * partialTicks - (IndicatiaConfig.GENERAL.enableOldFishingRodRender.get() ? 0.4D : 0.45D);
                d6 = MathHelper.lerp(partialTicks, player.prevPosZ, player.func_226281_cx_()) - d0 * d2 + d1 * xz;
                f3 = player.isCrouching() ? IndicatiaConfig.GENERAL.enableOldFishingRodRender.get() ? -0.55F : -0.1875F : 0.0F;
                dz = IndicatiaConfig.GENERAL.enableOldFishingRodRender.get() && player.isCrouching() ? 0.01F : 0.0F;
            }

            double d9 = MathHelper.lerp(partialTicks, entity.prevPosX, entity.func_226277_ct_());
            double d10 = MathHelper.lerp(partialTicks, entity.prevPosY, entity.func_226278_cu_()) + 0.25D;
            double d8 = MathHelper.lerp(partialTicks, entity.prevPosZ, entity.func_226281_cx_());
            float f4 = (float)(d4 - d9) + dz;
            float f5 = (float)(d5 - d10) + f3;
            float f6 = (float)(d6 - d8);
            IVertexBuilder ivertexbuilder1 = buffer.getBuffer(RenderType.func_228659_m_());
            Matrix4f matrix4f1 = stack.func_227866_c_().func_227870_a_();

            for (int k = 0; k < 16; ++k)
            {
                MixinFishRenderer.func_229104_a_(f4, f5, f6, ivertexbuilder1, matrix4f1, MixinFishRenderer.func_229105_a_(k, 16));
                MixinFishRenderer.func_229104_a_(f4, f5, f6, ivertexbuilder1, matrix4f1, MixinFishRenderer.func_229105_a_(k + 1, 16));
            }
            stack.func_227865_b_();
            super.func_225623_a_(entity, yaw, partialTicks, stack, buffer, color);
        }
    }

    private static float func_229105_a_(int p_229105_0_, int p_229105_1_)
    {
        return (float)p_229105_0_ / (float)p_229105_1_;
    }

    private static void func_229106_a_(IVertexBuilder p_229106_0_, Matrix4f p_229106_1_, Matrix3f p_229106_2_, int p_229106_3_, float p_229106_4_, int p_229106_5_, int p_229106_6_, int p_229106_7_)
    {
        p_229106_0_.func_227888_a_(p_229106_1_, p_229106_4_ - 0.5F, p_229106_5_ - 0.5F, 0.0F).func_225586_a_(255, 255, 255, 255).func_225583_a_(p_229106_6_, p_229106_7_).func_227891_b_(OverlayTexture.field_229196_a_).func_227886_a_(p_229106_3_).func_227887_a_(p_229106_2_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void func_229104_a_(float p_229104_0_, float p_229104_1_, float p_229104_2_, IVertexBuilder p_229104_3_, Matrix4f p_229104_4_, float p_229104_5_)
    {
        p_229104_3_.func_227888_a_(p_229104_4_, p_229104_0_ * p_229104_5_, p_229104_1_ * (p_229104_5_ * p_229104_5_ + p_229104_5_) * 0.5F + 0.25F, p_229104_2_ * p_229104_5_).func_225586_a_(0, 0, 0, 255).endVertex();
    }
}