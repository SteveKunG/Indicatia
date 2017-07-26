package stevekung.mods.indicatia.renderer;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class LayerArmorBaseNew<T extends ModelBase, E extends EntityLivingBase> implements LayerRenderer<E>
{
    private static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    protected T modelLeggings;
    protected T modelArmor;
    private final RendererLivingEntity renderer;
    private float alpha = 1.0F;
    private float colorR = 1.0F;
    private float colorG = 1.0F;
    private float colorB = 1.0F;
    private boolean skipRenderGlint;
    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

    public LayerArmorBaseNew(RendererLivingEntity renderer, E entity)
    {
        this.renderer = renderer;
        this.initArmor(entity);
    }

    @Override
    public void doRenderLayer(E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.renderArmorLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, 4);
        this.renderArmorLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, 3);
        this.renderArmorLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, 2);
        this.renderArmorLayer(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, 1);
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return true;
    }

    private void renderArmorLayer(E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, int slot)
    {
        ItemStack itemStack = this.getCurrentArmor(entity, slot);

        if (itemStack != null && itemStack.getItem() instanceof ItemArmor)
        {
            ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
            T t = this.getModelFromSlot(slot);
            t.setModelAttributes(this.renderer.getMainModel());
            t.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
            this.setModelSlotVisible(t, slot, entity);
            boolean flag = this.isLegSlot(slot);
            this.renderer.bindTexture(this.getArmorResource(entity, itemStack, flag ? 2 : 1, null));
            int i = itemArmor.getColor(itemStack);

            if (i != -1)
            {
                float f = (i >> 16 & 255) / 255.0F;
                float f1 = (i >> 8 & 255) / 255.0F;
                float f2 = (i & 255) / 255.0F;
                GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
                t.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                this.renderer.bindTexture(this.getArmorResource(entity, itemStack, flag ? 2 : 1, "overlay"));
            }

            GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
            t.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            if (!this.skipRenderGlint && itemStack.hasEffect())
            {
                this.renderEnchantedGlint(entity, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }
    }

    private T getModelFromSlot(int slot)
    {
        return this.isLegSlot(slot) ? this.modelLeggings : this.modelArmor;
    }

    private ItemStack getCurrentArmor(EntityLivingBase entity, int slot)
    {
        return entity.getCurrentArmor(slot - 1);
    }

    private boolean isLegSlot(int slot)
    {
        return slot == 2;
    }

    private void renderEnchantedGlint(EntityLivingBase entity, T model, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        float f = entity.ticksExisted + partialTicks;
        this.renderer.bindTexture(LayerArmorBaseNew.ENCHANTED_ITEM_GLINT_RES);
        GlStateManager.enableBlend();
        GlStateManager.depthFunc(514);
        GlStateManager.depthMask(false);
        float f1 = 0.5F;
        GlStateManager.color(f1, f1, f1, 1.0F);

        for (int i = 0; i < 2; ++i)
        {
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(768, 1);
            float f2 = 0.76F;
            GlStateManager.color(0.5F * f2, 0.25F * f2, 0.8F * f2, 1.0F);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f3 = 0.33333334F;
            GlStateManager.scale(f3, f3, f3);
            GlStateManager.rotate(30.0F - i * 60.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(0.0F, f * (0.001F + i * 0.003F) * 20.0F, 0.0F);
            GlStateManager.matrixMode(5888);
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
        GlStateManager.disableBlend();
    }

    private ResourceLocation getArmorResource(Entity entity, ItemStack itemStack, int slot, String type)
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

    protected abstract void setModelSlotVisible(T model, int slot, E entity);
}