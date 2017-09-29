package stevekung.mods.indicatia.renderer;

import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelArmorStandArmor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderArmorStandNew extends RendererLivingEntity<EntityArmorStand>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/armorstand/wood.png");

    public RenderArmorStandNew(RenderManager manager)
    {
        super(manager, new ModelArmorStand(), 0.0F);
        LayerBipedArmor layerArmor = new LayerBipedArmor(this)
        {
            @Override
            protected void initArmor()
            {
                this.field_177189_c = new ModelArmorStandArmor(0.5F);
                this.field_177186_d = new ModelArmorStandArmor(1.0F);
            }
        };
        this.addLayer(layerArmor);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerCustomHeadNew(this.getMainModel().bipedHead));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityArmorStand entity)
    {
        return RenderArmorStandNew.TEXTURE;
    }

    @Override
    public ModelArmorStand getMainModel()
    {
        return (ModelArmorStand)super.getMainModel();
    }

    @Override
    protected void rotateCorpse(EntityArmorStand entity, float ticksExisted, float rotationYaw, float partialTicks)
    {
        GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
    }

    @Override
    protected boolean canRenderName(EntityArmorStand entity)
    {
        return entity.getAlwaysRenderNameTag();
    }
}