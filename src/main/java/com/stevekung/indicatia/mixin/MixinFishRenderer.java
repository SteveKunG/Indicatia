package com.stevekung.indicatia.mixin;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.stevekung.indicatia.config.IndicatiaConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.FishRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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
    public MixinFishRenderer(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        PlayerEntity player = ((FishingBobberEntity)entity).getAngler();

        if (player != null && !this.renderOutlines)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translatef((float)x, (float)y, (float)z);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            this.bindEntityTexture(entity);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            GlStateManager.rotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef((this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

            if (this.renderOutlines)
            {
                GlStateManager.enableColorMaterial();
                GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
            }

            bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
            bufferbuilder.pos(-0.5D, -0.5D, 0.0D).tex(0.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos(0.5D, -0.5D, 0.0D).tex(1.0D, 1.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos(0.5D, 0.5D, 0.0D).tex(1.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.pos(-0.5D, 0.5D, 0.0D).tex(0.0D, 0.0D).normal(0.0F, 1.0F, 0.0F).endVertex();
            tessellator.draw();

            if (this.renderOutlines)
            {
                GlStateManager.tearDownSolidRenderingTextureCombine();
                GlStateManager.disableColorMaterial();
            }

            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            int i = player.getPrimaryHand() == HandSide.RIGHT ? 1 : -1;
            ItemStack itemStack = player.getHeldItemMainhand();

            if (!(itemStack.getItem() instanceof FishingRodItem))
            {
                i = -i;
            }

            float f3 = player.getSwingProgress(partialTicks);
            float f4 = MathHelper.sin(MathHelper.sqrt(f3) * (float)Math.PI);
            float f5 = MathHelper.lerp(partialTicks, player.prevRenderYawOffset, player.renderYawOffset) * ((float)Math.PI / 180F);
            double d0 = MathHelper.sin(f5);
            double d1 = MathHelper.cos(f5);
            double d2 = i * 0.35D;
            double d4;
            double d5;
            double d6;
            double d7;
            double dz = 0.0D;

            if ((this.renderManager.options == null || this.renderManager.options.thirdPersonView <= 0) && player == Minecraft.getInstance().player)
            {
                double d8 = this.renderManager.options.fov;
                d8 = d8 / 100.0D;
                Vec3d vec3d = IndicatiaConfig.GENERAL.enableOldFishingRodRender.get() ? new Vec3d(i * -0.5D * d8, 0.025D * d8, 0.65D) : new Vec3d(i * -0.36D * d8, -0.045D * d8, 0.4D);
                vec3d = vec3d.rotatePitch(-MathHelper.lerp(partialTicks, player.prevRotationPitch, player.rotationPitch) * ((float)Math.PI / 180F));
                vec3d = vec3d.rotateYaw(-MathHelper.lerp(partialTicks, player.prevRotationYaw, player.rotationYaw) * ((float)Math.PI / 180F));
                vec3d = vec3d.rotateYaw(f4 * 0.5F);
                vec3d = vec3d.rotatePitch(-f4 * 0.7F);
                d4 = MathHelper.lerp(partialTicks, player.prevPosX, player.posX) + vec3d.x;
                d5 = MathHelper.lerp(partialTicks, player.prevPosY, player.posY) + vec3d.y;
                d6 = MathHelper.lerp(partialTicks, player.prevPosZ, player.posZ) + vec3d.z;
                d7 = player.getEyeHeight();
            }
            else
            {
                double xz = player.isSneaking() ? 0.775D : IndicatiaConfig.GENERAL.enableOldFishingRodRender.get() ? 0.9D : 0.8D;
                d4 = MathHelper.lerp(partialTicks, player.prevPosX, player.posX) - d1 * d2 - d0 * xz;
                d5 = player.prevPosY + player.getEyeHeight() + (player.posY - player.prevPosY) * partialTicks - (IndicatiaConfig.GENERAL.enableOldFishingRodRender.get() ? 0.4D : 0.45D);
                d6 = MathHelper.lerp(partialTicks, player.prevPosZ, player.posZ) - d0 * d2 + d1 * xz;
                d7 = player.shouldRenderSneaking() ? IndicatiaConfig.GENERAL.enableOldFishingRodRender.get() ? -0.55D : -0.1875D : 0.0D;
                dz = IndicatiaConfig.GENERAL.enableOldFishingRodRender.get() && player.isSneaking() ? 0.01D : 0.0D;
            }

            double d13 = MathHelper.lerp(partialTicks, entity.prevPosX, entity.posX);
            double d14 = MathHelper.lerp(partialTicks, entity.prevPosY, entity.posY) + 0.25D;
            double d9 = MathHelper.lerp(partialTicks, entity.prevPosZ, entity.posZ);
            double d10 = (float)(d4 - d13) + dz;
            double d11 = (float)(d5 - d14) + d7;
            double d12 = (float)(d6 - d9);
            GlStateManager.disableTexture();
            GlStateManager.disableLighting();
            bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);

            for (int k = 0; k <= 16; ++k)
            {
                float f6 = k / 16.0F;
                bufferbuilder.pos(x + d10 * f6, y + d11 * (f6 * f6 + f6) * 0.5D + 0.25D, z + d12 * f6).color(0, 0, 0, 255).endVertex();
            }

            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }
}