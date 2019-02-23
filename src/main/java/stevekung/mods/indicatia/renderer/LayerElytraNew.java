package stevekung.mods.indicatia.renderer;

import java.lang.reflect.Method;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.ModelElytra;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import stevekung.mods.indicatia.core.IndicatiaMod;

@OnlyIn(Dist.CLIENT)
public class LayerElytraNew implements LayerRenderer<AbstractClientPlayer>
{
    private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
    private final RenderPlayer renderPlayer;
    private final ModelElytra modelElytra = new ModelElytra();

    public LayerElytraNew(RenderPlayer renderPlayer)
    {
        this.renderPlayer = renderPlayer;
    }

    @Override
    public void render(AbstractClientPlayer entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        ItemStack itemStack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

        if (itemStack.getItem() == Items.ELYTRA)
        {
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            if (entity.isPlayerInfoSet() && entity.getLocationElytra() != null)
            {
                this.renderPlayer.bindTexture(entity.getLocationElytra());
            }
            else
            {
                LayerElytraNew.renderOptifineElytra(this.renderPlayer, itemStack);
            }

            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0F, 0.0F, 0.125F);
            this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
            this.modelElytra.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            if (itemStack.isEnchanted())
            {
                LayerArmorBase.renderEnchantedGlint(this.renderPlayer, entity, this.modelElytra, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
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

    private static void renderOptifineElytra(RenderPlayer renderPlayer, ItemStack itemStack)
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
        renderPlayer.bindTexture(elytraTexture);
    }
}