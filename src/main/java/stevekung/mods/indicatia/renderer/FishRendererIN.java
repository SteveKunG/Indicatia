package stevekung.mods.indicatia.renderer;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.stevekungslib.utils.client.GLConstants;

@OnlyIn(Dist.CLIENT)
public class FishRendererIN extends EntityRenderer<FishingBobberEntity>
{
    private static final ResourceLocation FISH_PARTICLES = new ResourceLocation("textures/particle/particles.png");

    public FishRendererIN(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public void doRender(FishingBobberEntity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        PlayerEntity player = entity.getAngler();

        if (player != null && !this.renderOutlines)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translatef((float)x, (float)y, (float)z);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            this.bindEntityTexture(entity);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder vertexbuffer = tessellator.getBuffer();
            GlStateManager.rotatef(180.0F - this.field_76990_c.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef((this.field_76990_c.options.thirdPersonView == 2 ? -1 : 1) * -this.field_76990_c.playerViewX, 1.0F, 0.0F, 0.0F);

            if (this.renderOutlines)
            {
                GlStateManager.enableColorMaterial();
                GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
            }

            vertexbuffer.begin(GLConstants.QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
            vertexbuffer.pos(-0.5D, -0.5D, 0.0D).tex(0.0625D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
            vertexbuffer.pos(0.5D, -0.5D, 0.0D).tex(0.125D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
            vertexbuffer.pos(0.5D, 0.5D, 0.0D).tex(0.125D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
            vertexbuffer.pos(-0.5D, 0.5D, 0.0D).tex(0.0625D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
            tessellator.draw();

            if (this.renderOutlines)
            {
                GlStateManager.tearDownSolidRenderingTextureCombine();
                GlStateManager.disableColorMaterial();
            }

            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            int k = player.getPrimaryHand() == HandSide.RIGHT ? 1 : -1;
            ItemStack itemstack = player.getHeldItemMainhand();

            if (itemstack.getItem() != Items.FISHING_ROD)
            {
                k = -k;
            }

            float f7 = player.getSwingProgress(partialTicks);
            float f8 = MathHelper.sin(MathHelper.sqrt(f7) * (float)Math.PI);
            float f9 = (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks) * 0.017453292F;
            double d0 = MathHelper.sin(f9);
            double d1 = MathHelper.cos(f9);
            double d2 = k * 0.35D;
            double d4;
            double d5;
            double d6;
            double d7;
            double dz = 0.0D;

            if ((this.field_76990_c.options == null || this.field_76990_c.options.thirdPersonView <= 0) && player == Minecraft.getInstance().player)
            {
                double f10 = this.field_76990_c.options.fovSetting;
                f10 = f10 / 100.0F;
                Vec3d vec3d = new Vec3d(k * -0.5D * f10, 0.025D * f10, 0.65D);
                vec3d = vec3d.rotatePitch(-(player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks) * 0.017453292F);
                vec3d = vec3d.rotateYaw(-(player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks) * 0.017453292F);
                vec3d = vec3d.rotateYaw(f8 * 0.5F);
                vec3d = vec3d.rotatePitch(-f8 * 0.7F);
                d4 = player.prevPosX + (player.posX - player.prevPosX) * partialTicks + vec3d.x;
                d5 = player.prevPosY + (player.posY - player.prevPosY) * partialTicks + vec3d.y;
                d6 = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks + vec3d.z;
                d7 = player.getEyeHeight();
            }
            else
            {
                double xz = player.isSneaking() ? 0.775D : 0.9D;
                d4 = player.prevPosX + (player.posX - player.prevPosX) * partialTicks - d1 * d2 - d0 * xz;
                d5 = player.prevPosY + player.getEyeHeight() + (player.posY - player.prevPosY) * partialTicks - 0.4D;
                d6 = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks - d0 * d2 + d1 * xz;
                d7 = player.isSneaking() ? -0.55D : 0.0D;
                dz = player.isSneaking() ? 0.01D : 0.0D;
            }

            double d13 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            double d8 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + 0.25D;
            double d9 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
            double d10 = (float)(d4 - d13) + dz;
            double d11 = (float)(d5 - d8) + d7;
            double d12 = (float)(d6 - d9);
            GlStateManager.disableTexture();
            GlStateManager.disableLighting();
            vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);

            for (int i1 = 0; i1 <= 16; ++i1)
            {
                float f11 = i1 / 16.0F;
                vertexbuffer.pos(x + d10 * f11, y + d11 * (f11 * f11 + f11) * 0.5D + 0.25D, z + d12 * f11).color(0, 0, 0, 255).endVertex();
            }
            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(FishingBobberEntity entity)
    {
        return FishRendererIN.FISH_PARTICLES;
    }
}