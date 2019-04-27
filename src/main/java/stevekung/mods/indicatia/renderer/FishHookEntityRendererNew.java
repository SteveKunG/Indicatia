package stevekung.mods.indicatia.renderer;

import com.mojang.blaze3d.platform.GlStateManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishHookEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.AbsoluteHand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class FishHookEntityRendererNew extends EntityRenderer<FishHookEntity>
{
    private static final Identifier FISH_PARTICLES = new Identifier("textures/particle/particles.png");

    public FishHookEntityRendererNew(EntityRenderDispatcher manager)
    {
        super(manager);
    }

    public void method_3974(FishHookEntity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        PlayerEntity player = entity.getOwner();

        if (player != null && !this.renderOutlines)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translatef((float)x, (float)y, (float)z);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            this.bindEntityTexture(entity);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBufferBuilder();
            GlStateManager.rotatef(180.0F - this.renderManager.cameraYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotatef((this.renderManager.gameOptions.perspective == 2 ? -1 : 1) * -this.renderManager.cameraPitch, 1.0F, 0.0F, 0.0F);

            if (this.renderOutlines)
            {
                GlStateManager.enableColorMaterial();
                GlStateManager.setupSolidRenderingTextureCombine(this.getOutlineColor(entity));
            }

            bufferBuilder.begin(7, VertexFormats.POSITION_UV_NORMAL);
            bufferBuilder.vertex(-0.5D, -0.5D, 0.0D).texture(0.0625D, 0.1875D).normal(0.0F, 1.0F, 0.0F).next();
            bufferBuilder.vertex(0.5D, -0.5D, 0.0D).texture(0.125D, 0.1875D).normal(0.0F, 1.0F, 0.0F).next();
            bufferBuilder.vertex(0.5D, 0.5D, 0.0D).texture(0.125D, 0.125D).normal(0.0F, 1.0F, 0.0F).next();
            bufferBuilder.vertex(-0.5D, 0.5D, 0.0D).texture(0.0625D, 0.125D).normal(0.0F, 1.0F, 0.0F).next();
            tessellator.draw();

            if (this.renderOutlines)
            {
                GlStateManager.tearDownSolidRenderingTextureCombine();
                GlStateManager.disableColorMaterial();
            }

            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            int k = player.getMainHand() == AbsoluteHand.RIGHT ? 1 : -1;
            ItemStack itemstack = player.getMainHandStack();

            if (itemstack.getItem() != Items.FISHING_ROD)
            {
                k = -k;
            }

            float f7 = player.getHandSwingProgress(partialTicks);
            float f8 = MathHelper.sin(MathHelper.sqrt(f7) * (float)Math.PI);
            float f9 = MathHelper.lerp(partialTicks, player.field_6220, player.field_6283) * 0.017453292F;//renderYawOffset - prevRenderYawOffset
            double d0 = MathHelper.sin(f9);
            double d1 = MathHelper.cos(f9);
            double d2 = k * 0.35D;
            double d4;
            double d5;
            double d6;
            double d7;
            double dz = 0.0D;

            if ((this.renderManager.gameOptions == null || this.renderManager.gameOptions.perspective <= 0) && player == MinecraftClient.getInstance().player)
            {
                double f10 = this.renderManager.gameOptions.fov;
                f10 = f10 / 100.0F;
                Vec3d vec3d = new Vec3d(k * -0.5D * f10, 0.025D * f10, 0.65D);
                vec3d = vec3d.rotateX(-MathHelper.lerp(partialTicks, player.prevPitch, player.pitch) * 0.017453292F);
                vec3d = vec3d.rotateY(-MathHelper.lerp(partialTicks, player.prevYaw, player.yaw) * 0.017453292F);
                vec3d = vec3d.rotateY(f8 * 0.5F);
                vec3d = vec3d.rotateX(-f8 * 0.7F);
                d4 = player.prevX + (player.x - player.prevX) * partialTicks + vec3d.x;
                d5 = player.prevY + (player.y - player.prevY) * partialTicks + vec3d.y;
                d6 = player.prevZ + (player.z - player.prevZ) * partialTicks + vec3d.z;
                d7 = player.getStandingEyeHeight();
            }
            else
            {
                double xz = player.isSneaking() ? 0.775D : 0.9D;
                d4 = player.prevX + (player.x - player.prevX) * partialTicks - d1 * d2 - d0 * xz;
                d5 = player.prevY + player.getStandingEyeHeight() + (player.y - player.prevY) * partialTicks - 0.4D;
                d6 = player.prevZ + (player.z - player.prevZ) * partialTicks - d0 * d2 + d1 * xz;
                d7 = player.isSneaking() ? -0.55D : 0.0D;
                dz = player.isSneaking() ? 0.01D : 0.0D;
            }

            double d13 = entity.prevX + (entity.x - entity.prevX) * partialTicks;
            double d8 = entity.prevY + (entity.y - entity.prevY) * partialTicks + 0.25D;
            double d9 = entity.prevZ + (entity.z - entity.prevZ) * partialTicks;
            double d10 = (float)(d4 - d13) + dz;
            double d11 = (float)(d5 - d8) + d7;
            double d12 = (float)(d6 - d9);
            GlStateManager.disableTexture();
            GlStateManager.disableLighting();
            bufferBuilder.begin(3, VertexFormats.POSITION_COLOR);

            for (int i1 = 0; i1 <= 16; ++i1)
            {
                float f11 = i1 / 16.0F;
                bufferBuilder.vertex(x + d10 * f11, y + d11 * (f11 * f11 + f11) * 0.5D + 0.25D, z + d12 * f11).color(0, 0, 0, 255).next();
            }
            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture();
            super.render(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    protected Identifier getTexture(FishHookEntity entity)
    {
        return FishHookEntityRendererNew.FISH_PARTICLES;
    }
}