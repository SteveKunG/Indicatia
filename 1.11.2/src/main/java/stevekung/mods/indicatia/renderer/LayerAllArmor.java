package stevekung.mods.indicatia.renderer;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerAllArmor<E extends EntityLivingBase> extends LayerArmorBaseNew<ModelBiped, E>
{
    public LayerAllArmor(RenderLivingBase renderer, E entity)
    {
        super(renderer, entity);
    }

    @Override
    protected void initArmor(E entity)
    {
        if (entity instanceof AbstractClientPlayer)
        {
            this.modelLeggings = new ModelBiped(0.5F);
            this.modelArmor = new ModelBiped(1.0F);
        }
        else if ((entity instanceof EntityZombie || entity instanceof EntityGiantZombie) && !(entity instanceof EntityZombieVillager))
        {
            this.modelLeggings = new ModelZombie(0.5F, true);
            this.modelArmor = new ModelZombie(1.0F, true);
        }
        else if (entity instanceof AbstractSkeleton)
        {
            this.modelLeggings = new ModelSkeleton(0.5F, true);
            this.modelArmor = new ModelSkeleton(1.0F, true);
        }
        else if (entity instanceof EntityZombieVillager)
        {
            this.modelLeggings = new ModelZombieVillager(0.5F, 0.0F, true);
            this.modelArmor = new ModelZombieVillager(1.0F, 0.0F, true);
        }
    }

    @Override
    protected void setModelSlotVisible(ModelBiped model, EntityEquipmentSlot slot, E entity)
    {
        model.setInvisible(false);
        model.isChild = entity.isChild();

        switch (slot)
        {
        case HEAD:
        default:
            model.bipedHead.showModel = true;
            model.bipedHeadwear.showModel = true;
            break;
        case CHEST:
            model.bipedBody.showModel = true;
            model.bipedRightArm.showModel = true;
            model.bipedLeftArm.showModel = true;
            break;
        case LEGS:
            model.bipedBody.showModel = true;
            model.bipedRightLeg.showModel = true;
            model.bipedLeftLeg.showModel = true;
            break;
        case FEET:
            model.bipedRightLeg.showModel = true;
            model.bipedLeftLeg.showModel = true;
            break;
        }
    }
}