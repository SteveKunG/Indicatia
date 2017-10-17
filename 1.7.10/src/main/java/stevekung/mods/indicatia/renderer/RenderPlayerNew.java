package stevekung.mods.indicatia.renderer;

import java.util.Random;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import stevekung.mods.indicatia.util.ModLogger;

@SideOnly(Side.CLIENT)
public class RenderPlayerNew extends RenderPlayer
{
    public RenderPlayerNew()
    {
        ModLogger.info("Replacing {} for better arrow rendering", RenderPlayer.class.getName());
    }

    @Override
    protected void renderEquippedItems(AbstractClientPlayer entity, float partialTicks)
    {
        RenderPlayerEvent.Specials.Pre event = new RenderPlayerEvent.Specials.Pre(entity, this, partialTicks);

        if (MinecraftForge.EVENT_BUS.post(event))
        {
            return;
        }

        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        this.renderArrowsStuckInEntity(entity, partialTicks);
        ItemStack itemStack = entity.inventory.armorItemInSlot(3);

        if (itemStack != null && event.renderHelmet)
        {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedHead.postRender(0.0625F);
            float f1;

            if (itemStack.getItem() instanceof ItemBlock)
            {
                IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemStack, IItemRenderer.ItemRenderType.EQUIPPED);
                boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.EQUIPPED, itemStack, IItemRenderer.ItemRendererHelper.BLOCK_3D);

                if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemStack.getItem()).getRenderType()))
                {
                    f1 = 0.625F;
                    GL11.glTranslatef(0.0F, -0.25F, 0.0F);
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glScalef(f1, -f1, -f1);
                }
                this.renderManager.itemRenderer.renderItem(entity, itemStack, 0);
            }
            else if (itemStack.getItem() == Items.skull)
            {
                f1 = 1.0625F;
                GL11.glScalef(f1, -f1, -f1);
                GameProfile profile = null;

                if (itemStack.hasTagCompound())
                {
                    NBTTagCompound nbttagcompound = itemStack.getTagCompound();

                    if (nbttagcompound.hasKey("SkullOwner", 10))
                    {
                        profile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
                    }
                    else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner")))
                    {
                        profile = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                    }
                }
                TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, itemStack.getItemDamage(), profile);
            }
            GL11.glPopMatrix();
        }

        float f2;

        if (entity.getCommandSenderName().equals("deadmau5") && entity.func_152123_o())
        {
            this.bindTexture(entity.getLocationSkin());

            for (int j = 0; j < 2; ++j)
            {
                float f9 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - (entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * partialTicks);
                float f10 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                GL11.glPushMatrix();
                GL11.glRotatef(f9, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(f10, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.375F * (j * 2 - 1), 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.375F, 0.0F);
                GL11.glRotatef(-f10, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-f9, 0.0F, 1.0F, 0.0F);
                f2 = 1.3333334F;
                GL11.glScalef(f2, f2, f2);
                this.modelBipedMain.renderEars(0.0625F);
                GL11.glPopMatrix();
            }
        }

        boolean flag = entity.func_152122_n();
        flag = event.renderCape && flag;
        float f4;

        if (flag && !entity.isInvisible() && !entity.getHideCape())
        {
            this.bindTexture(entity.getLocationCape());
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d3 = entity.field_71091_bM + (entity.field_71094_bP - entity.field_71091_bM) * partialTicks - (entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks);
            double d4 = entity.field_71096_bN + (entity.field_71095_bQ - entity.field_71096_bN) * partialTicks - (entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks);
            double d0 = entity.field_71097_bO + (entity.field_71085_bR - entity.field_71097_bO) * partialTicks - (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks);
            f4 = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * partialTicks;
            double d1 = MathHelper.sin(f4 * (float)Math.PI / 180.0F);
            double d2 = -MathHelper.cos(f4 * (float)Math.PI / 180.0F);
            float f5 = (float)d4 * 10.0F;

            if (f5 < -6.0F)
            {
                f5 = -6.0F;
            }
            if (f5 > 32.0F)
            {
                f5 = 32.0F;
            }

            float f6 = (float)(d3 * d1 + d0 * d2) * 100.0F;
            float f7 = (float)(d3 * d2 - d0 * d1) * 100.0F;

            if (f6 < 0.0F)
            {
                f6 = 0.0F;
            }

            float f8 = entity.prevCameraYaw + (entity.cameraYaw - entity.prevCameraYaw) * partialTicks;
            f5 += MathHelper.sin((entity.prevDistanceWalkedModified + (entity.distanceWalkedModified - entity.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f8;

            if (entity.isSneaking())
            {
                f5 += 25.0F;
            }
            GL11.glRotatef(6.0F + f6 / 2.0F + f5, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f7 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f7 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.modelBipedMain.renderCloak(0.0625F);
            GL11.glPopMatrix();
        }

        ItemStack itemStack1 = entity.inventory.getCurrentItem();

        if (itemStack1 != null && event.renderItem)
        {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            if (entity.fishEntity != null)
            {
                itemStack1 = new ItemStack(Items.stick);
            }

            EnumAction action = null;

            if (entity.getItemInUseCount() > 0)
            {
                action = itemStack1.getItemUseAction();
            }

            IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemStack1, IItemRenderer.ItemRenderType.EQUIPPED);
            boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.EQUIPPED, itemStack1, IItemRenderer.ItemRendererHelper.BLOCK_3D);

            if (is3D || itemStack1.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemStack1.getItem()).getRenderType()))
            {
                f2 = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f2 *= 0.75F;
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(-f2, -f2, f2);
            }
            else if (itemStack1.getItem() == Items.bow)
            {
                f2 = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f2, -f2, f2);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            }
            else if (itemStack1.getItem().isFull3D())
            {
                f2 = 0.625F;

                if (itemStack1.getItem().shouldRotateAroundWhenRendering())
                {
                    GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                }
                if (entity.getItemInUseCount() > 0 && action == EnumAction.block)
                {
                    GL11.glTranslatef(0.05F, 0.0F, -0.1F);
                    GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
                }
                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(f2, -f2, f2);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                f2 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(f2, f2, f2);
                GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
            }

            float f3;
            int k;
            float f12;

            if (itemStack1.getItem().requiresMultipleRenderPasses())
            {
                for (k = 0; k < itemStack1.getItem().getRenderPasses(itemStack1.getItemDamage()); ++k)
                {
                    int i = itemStack1.getItem().getColorFromItemStack(itemStack1, k);
                    f12 = (i >> 16 & 255) / 255.0F;
                    f3 = (i >> 8 & 255) / 255.0F;
                    f4 = (i & 255) / 255.0F;
                    GL11.glColor4f(f12, f3, f4, 1.0F);
                    this.renderManager.itemRenderer.renderItem(entity, itemStack1, k);
                }
            }
            else
            {
                k = itemStack1.getItem().getColorFromItemStack(itemStack1, 0);
                float f11 = (k >> 16 & 255) / 255.0F;
                f12 = (k >> 8 & 255) / 255.0F;
                f3 = (k & 255) / 255.0F;
                GL11.glColor4f(f11, f12, f3, 1.0F);
                this.renderManager.itemRenderer.renderItem(entity, itemStack1, 0);
            }
            GL11.glPopMatrix();
        }
        MinecraftForge.EVENT_BUS.post(new RenderPlayerEvent.Specials.Post(entity, this, partialTicks));
    }

    @Override
    protected void renderArrowsStuckInEntity(EntityLivingBase entity, float partialTicks)
    {
        int i = entity.getArrowCountInEntity();

        if (i > 0)
        {
            EntityArrow arrow = new EntityArrow(entity.worldObj, entity.posX, entity.posY, entity.posZ);
            Random rand = new Random(entity.getEntityId());

            for (int j = 0; j < i; ++j)
            {
                GL11.glPushMatrix();
                RenderPlayerNew.renderLight(false);
                ModelRenderer modelrenderer = this.mainModel.getRandomModelBox(rand);
                ModelBox modelbox = (ModelBox)modelrenderer.cubeList.get(rand.nextInt(modelrenderer.cubeList.size()));
                modelrenderer.postRender(0.0625F);
                float f1 = rand.nextFloat();
                float f2 = rand.nextFloat();
                float f3 = rand.nextFloat();
                float f4 = (modelbox.posX1 + (modelbox.posX2 - modelbox.posX1) * f1) / 16.0F;
                float f5 = (modelbox.posY1 + (modelbox.posY2 - modelbox.posY1) * f2) / 16.0F;
                float f6 = (modelbox.posZ1 + (modelbox.posZ2 - modelbox.posZ1) * f3) / 16.0F;
                GL11.glTranslatef(f4, f5, f6);
                f1 = f1 * 2.0F - 1.0F;
                f2 = f2 * 2.0F - 1.0F;
                f3 = f3 * 2.0F - 1.0F;
                f1 *= -1.0F;
                f2 *= -1.0F;
                f3 *= -1.0F;
                float f7 = MathHelper.sqrt_float(f1 * f1 + f3 * f3);
                arrow.prevRotationYaw = arrow.rotationYaw = (float)(Math.atan2(f1, f3) * 180.0D / Math.PI);
                arrow.prevRotationPitch = arrow.rotationPitch = (float)(Math.atan2(f2, f7) * 180.0D / Math.PI);
                double d0 = 0.0D;
                double d1 = 0.0D;
                double d2 = 0.0D;
                float f8 = 0.0F;
                this.renderManager.renderEntityWithPosYaw(arrow, d0, d1, d2, f8, partialTicks);
                GL11.glPopMatrix();
                RenderPlayerNew.renderLight(true);
            }
        }
    }

    private static void renderLight(boolean enable)
    {
        if (enable)
        {
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_LIGHT0);
            GL11.glEnable(GL11.GL_LIGHT1);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        }
        else
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_LIGHT0);
            GL11.glDisable(GL11.GL_LIGHT1);
            GL11.glDisable(GL11.GL_COLOR_MATERIAL);
        }
    }
}