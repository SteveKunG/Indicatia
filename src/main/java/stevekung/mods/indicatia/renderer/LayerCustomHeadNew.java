package stevekung.mods.indicatia.renderer;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.core.IndicatiaMod;

@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class LayerCustomHeadNew implements LayerRenderer<EntityLivingBase>
{
    private final ModelRenderer modelRenderer;

    public LayerCustomHeadNew(ModelRenderer modelRenderer)
    {
        this.modelRenderer = modelRenderer;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        ItemStack itemStack = entity.getCurrentArmor(3);

        if (itemStack != null && itemStack.getItem() != null)
        {
            Item item = itemStack.getItem();
            GlStateManager.pushMatrix();

            if (entity.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            boolean flag = entity instanceof EntityZombie && ((EntityZombie)entity).isVillager();

            if (entity.isChild())
            {
                GlStateManager.translate(0.0F, 0.5F * scale, 0.0F);
                GlStateManager.scale(0.7F, 0.7F, 0.7F);
                GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            }

            this.modelRenderer.postRender(0.0625F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (item instanceof ItemBlock)
            {
                scale = 0.625F;
                GlStateManager.translate(0.0F, -0.25F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.scale(scale, -scale, -scale);

                if (flag)
                {
                    GlStateManager.translate(0.0F, 0.1875F, 0.0F);
                }
                IndicatiaMod.MC.getItemRenderer().renderItem(entity, itemStack, ItemCameraTransforms.TransformType.HEAD);
            }
            else if (item == Items.skull)
            {
                GlStateManager.scale(1.1875F, -1.1875F, -1.1875F);

                if (flag)
                {
                    GlStateManager.translate(0.0F, 0.0625F, 0.0F);
                }

                GameProfile profile = null;

                if (itemStack.hasTagCompound())
                {
                    NBTTagCompound nbt = itemStack.getTagCompound();

                    if (nbt.hasKey("SkullOwner", 10))
                    {
                        profile = NBTUtil.readGameProfileFromNBT(nbt.getCompoundTag("SkullOwner"));
                    }
                    else if (nbt.hasKey("SkullOwner", 8))
                    {
                        String s = nbt.getString("SkullOwner");

                        if (!StringUtils.isNullOrEmpty(s))
                        {
                            profile = TileEntitySkull.updateGameprofile(new GameProfile((UUID)null, s));
                            nbt.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), profile));
                        }
                    }
                }
                TileEntitySkullRendererNew.renderSkull(-0.5F, 0.0F, -0.5F, EnumFacing.UP, 180.0F, itemStack.getMetadata(), profile, -1, IndicatiaMod.MC.getTextureManager());
            }
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }
}