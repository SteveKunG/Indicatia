package stevekung.mods.indicatia.renderer;

import java.lang.reflect.Method;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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
    public void doRenderLayer(AbstractClientPlayer entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        ItemStack itemStack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

        if (itemStack.getItem() == Items.ELYTRA)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

            if (entity instanceof AbstractClientPlayer)
            {
                AbstractClientPlayer player = entity;

                if (player.isPlayerInfoSet() && player.getLocationElytra() != null)
                {
                    this.renderPlayer.bindTexture(player.getLocationElytra());
                }
                else
                {
                    LayerElytraNew.renderOptifineElytra(this.renderPlayer, itemStack);
                }
            }
            else
            {
                LayerElytraNew.renderOptifineElytra(this.renderPlayer, itemStack);
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 0.125F);
            this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
            this.modelElytra.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            if (itemStack.isItemEnchanted())
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

        try
        {
            Class<?> configClass = Class.forName("Config");
            Class<?> customItemsClass = Class.forName("CustomItems");
            Method customItemsMethod = configClass.getDeclaredMethod("isCustomItems");
            Method getCustomElytraTextureMethod = customItemsClass.getDeclaredMethod("getCustomElytraTexture", ItemStack.class, ResourceLocation.class);
            boolean isCustomItems = (boolean) customItemsMethod.invoke(null);

            if (FMLClientHandler.instance().hasOptifine())
            {
                if (isCustomItems)
                {
                    elytraTexture = (ResourceLocation) getCustomElytraTextureMethod.invoke(null, itemStack, elytraTexture);
                }
            }
        }
        catch (Exception e) {}
        renderPlayer.bindTexture(elytraTexture);
    }
}