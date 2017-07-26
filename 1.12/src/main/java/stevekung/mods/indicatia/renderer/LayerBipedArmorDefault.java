package stevekung.mods.indicatia.renderer;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerBipedArmorDefault extends LayerArmorBase<ModelBiped>
{
    public LayerBipedArmorDefault(RenderLivingBase renderer)
    {
        super(renderer);
    }

    @Override
    protected void initArmor()
    {
        this.modelLeggings = new ModelBiped(0.5F);
        this.modelArmor = new ModelBiped(1.0F);
    }

    @Override
    protected void setModelSlotVisible(ModelBiped model, EntityEquipmentSlot slot)
    {
        model.setVisible(false);

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

    @Override
    protected ModelBiped getArmorModelHook(EntityLivingBase entity, ItemStack itemStack, EntityEquipmentSlot slot, ModelBiped model)
    {
        return ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
    }
}