package stevekung.mods.indicatia.renderer;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class LayerArmorBaseNew<T extends ModelBase, E extends EntityLivingBase> implements LayerRenderer<E>
{
    protected T modelLeggings;
    protected T modelArmor;
    private final RenderLivingBase renderer;
    private float alpha = 1.0F;
    private float colorR = 1.0F;
    private float colorG = 1.0F;
    private float colorB = 1.0F;
    private boolean skipRenderGlint;
    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = new HashMap<>();

    public LayerArmorBaseNew(RenderLivingBase renderer, E entity)
    {
        this.renderer = renderer;
        this.initArmor(entity);
    }

    @Override
    public void doRenderLayer(E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.renderArmorLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST);
        this.renderArmorLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS);
        this.renderArmorLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET);
        this.renderArmorLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD);
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return true;
    }

    private void renderArmorLayer(E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slot)
    {
        ItemStack itemStack = entity.getItemStackFromSlot(slot);

        if (itemStack.getItem() instanceof ItemArmor)
        {
            ItemArmor itemArmor = (ItemArmor)itemStack.getItem();

            if (itemArmor.getEquipmentSlot() == slot)
            {
                T t = this.getModelFromSlot(slot);
                t = this.getArmorModelHook(entity, itemStack, slot, t);
                t.setModelAttributes(this.renderer.getMainModel());
                t.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
                this.setModelSlotVisible(t, slot, entity);
                this.isLegSlot(slot);
                this.renderer.bindTexture(this.getArmorResource(entity, itemStack, slot, null));

                if (itemArmor.hasOverlay(itemStack))
                {
                    int i = itemArmor.getColor(itemStack);
                    float f = (i >> 16 & 255) / 255.0F;
                    float f1 = (i >> 8 & 255) / 255.0F;
                    float f2 = (i & 255) / 255.0F;
                    GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
                    t.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                    this.renderer.bindTexture(this.getArmorResource(entity, itemStack, slot, "overlay"));
                }

                GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
                t.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

                if (!this.skipRenderGlint && itemStack.hasEffect())
                {
                    LayerArmorBase.renderEnchantedGlint(this.renderer, entity, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }
        }
    }

    private T getModelFromSlot(EntityEquipmentSlot slot)
    {
        return this.isLegSlot(slot) ? this.modelLeggings : this.modelArmor;
    }

    private boolean isLegSlot(EntityEquipmentSlot slot)
    {
        return slot == EntityEquipmentSlot.LEGS;
    }

    private ResourceLocation getArmorResource(Entity entity, ItemStack itemStack, EntityEquipmentSlot slot, String type)
    {
        ItemArmor item = (ItemArmor)itemStack.getItem();
        String texture = item.getArmorMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');

        if (idx != -1)
        {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }

        String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, this.isLegSlot(slot) ? 2 : 1, type == null ? "" : String.format("_%s", type));
        ResourceLocation resourcelocation = ARMOR_TEXTURE_RES_MAP.get(s1);

        if (resourcelocation == null)
        {
            resourcelocation = new ResourceLocation(s1);
            ARMOR_TEXTURE_RES_MAP.put(s1, resourcelocation);
        }
        return resourcelocation;
    }

    protected abstract void initArmor(E entity);

    protected abstract void setModelSlotVisible(T model, EntityEquipmentSlot slot, E entity);

    protected T getArmorModelHook(EntityLivingBase entity, ItemStack itemStack, EntityEquipmentSlot slot, T model)
    {
        return model;
    }
}