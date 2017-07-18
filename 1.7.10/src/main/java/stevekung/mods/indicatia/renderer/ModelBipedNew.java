package stevekung.mods.indicatia.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

@SideOnly(Side.CLIENT)
public class ModelBipedNew extends ModelBiped
{
    private boolean renderingEnchantment;

    public ModelBipedNew(float modelSize)
    {
        super(modelSize);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks)
    {
        super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        this.renderingEnchantment = false;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.actualRender(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.renderingEnchantment = true;
    }

    private void actualRender(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

        if (!this.renderingEnchantment)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        GL11.glPushMatrix();

        if (!this.renderingEnchantment)
        {
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        }
        if (this.isChild)
        {
            GL11.glScalef(0.75F, 0.75F, 0.75F);
            GL11.glTranslatef(0.0F, 16.0F * scale, 0.0F);
            this.bipedHead.render(scale);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GL11.glTranslatef(0.0F, 24.0F * scale, 0.0F);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
        }
        else
        {
            if (entity.isSneaking())
            {
                GL11.glTranslatef(0.0F, 0.2F, 0.0F);
            }
            this.bipedHead.render(scale);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
        }
        if (!this.renderingEnchantment)
        {
            GL11.glDisable(GL11.GL_BLEND);
        }
        GL11.glPopMatrix();
    }
}