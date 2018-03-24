package stevekung.mods.indicatia.renderer;

import java.lang.reflect.Field;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerAllArmor extends LayerBipedArmor
{
    private static Field renderRainbow;

    static
    {
        try
        {
            Class<?> clazz = Class.forName("stevekung.mods.indicatia.internal.InternalEventHandler");
            LayerAllArmor.renderRainbow = clazz.getDeclaredField("renderRainbow");
            LayerAllArmor.renderRainbow.setAccessible(true);
        }
        catch (Exception e) {}
    }

    public LayerAllArmor(RenderLivingBase<?> renderer)
    {
        super(renderer);
    }

    @Override
    protected void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn)
    {
        try
        {
            if (LayerAllArmor.renderRainbow.getBoolean(null))
            {
                GlStateManager.disableColorMaterial();
            }
        }
        catch (Exception e) {}
        super.renderArmorLayer(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, slotIn);
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return true;
    }
}