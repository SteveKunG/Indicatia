package stevekung.mods.indicatia.renderer;

import java.lang.reflect.Method;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArmorLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.core.IndicatiaMod;

@OnlyIn(Dist.CLIENT)
public class ElytraLayerIN<T extends AbstractClientPlayerEntity, M extends EntityModel<T>> extends LayerRenderer<T, M>
{
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
    private final ElytraModel<T> modelElytra = new ElytraModel<>();

    public ElytraLayerIN(IEntityRenderer<T, M> renderer)
    {
        super(renderer);
    }

    @Override
    public void render(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        ItemStack itemStack = entity.getItemStackFromSlot(EquipmentSlotType.CHEST);

        if (itemStack.getItem() == Items.ELYTRA)
        {
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            if (entity.isPlayerInfoSet() && entity.getLocationElytra() != null)
            {
                this.bindTexture(entity.getLocationElytra());
            }
            else
            {
                this.renderOptifineElytra(itemStack);
            }

            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0F, 0.0F, 0.125F);
            this.modelElytra.setRotationAngles(entity, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, limbSwing);
            this.modelElytra.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            if (itemStack.isEnchanted())
            {
                ArmorLayer.func_215338_a(this::bindTexture, entity, this.modelElytra, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
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

    private void renderOptifineElytra(ItemStack itemStack)
    {
        ResourceLocation elytraTexture = TEXTURE_ELYTRA;

        if (IndicatiaMod.isOptiFineLoaded)
        {
            try
            {
                Class<?> configClass = Class.forName("Config");
                Class<?> customItemsClass = Class.forName("net.optifine.CustomItems");
                Method customItemsMethod = configClass.getDeclaredMethod("isCustomItems");
                Method getCustomElytraTextureMethod = customItemsClass.getDeclaredMethod("getCustomElytraTexture", ItemStack.class, ResourceLocation.class);
                boolean isCustomItems = (boolean) customItemsMethod.invoke(null);

                if (isCustomItems)
                {
                    elytraTexture = (ResourceLocation) getCustomElytraTextureMethod.invoke(null, itemStack, elytraTexture);
                }
            }
            catch (Exception e) {}
        }
        this.bindTexture(elytraTexture);
    }
}