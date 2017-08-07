package stevekung.mods.indicatia.renderer;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerElytraNew implements LayerRenderer<EntityLivingBase>
{
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
    protected final RenderLivingBase render;
    private final ModelElytra modelElytra = new ModelElytra();

    public LayerElytraNew(RenderLivingBase render)
    {
        this.render = render;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        ItemStack itemStack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

        if (itemStack.getItem() == Items.ELYTRA)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            if (entity instanceof AbstractClientPlayer)
            {
                AbstractClientPlayer player = (AbstractClientPlayer)entity;

                if (player.isPlayerInfoSet() && player.getLocationElytra() != null)
                {
                    this.render.bindTexture(player.getLocationElytra());
                }
                else if (player.hasPlayerInfo() && player.getLocationCape() != null && player.isWearing(EnumPlayerModelParts.CAPE))
                {
                    this.render.bindTexture(player.getLocationCape());
                }
                else
                {
                    this.render.bindTexture(TEXTURE_ELYTRA);
                }
            }
            else
            {
                this.render.bindTexture(TEXTURE_ELYTRA);
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 0.125F);
            this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
            this.modelElytra.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            if (itemStack.isItemEnchanted())
            {
                LayerArmorBase.renderEnchantedGlint(this.render, entity, this.modelElytra, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return true;
    }
}