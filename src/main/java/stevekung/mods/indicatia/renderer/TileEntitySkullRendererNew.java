package stevekung.mods.indicatia.renderer;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatia.config.ConfigManager;
import stevekung.mods.indicatia.core.IndicatiaMod;

@SideOnly(Side.CLIENT)
public class TileEntitySkullRendererNew extends TileEntitySpecialRenderer<TileEntitySkull>
{
    private static final ResourceLocation SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/skeleton.png");
    private static final ResourceLocation WITHER_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
    private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
    private static final ResourceLocation CREEPER_TEXTURES = new ResourceLocation("textures/entity/creeper/creeper.png");
    private static final ModelSkeletonHead skeletonHead = new ModelSkeletonHead(0, 0, 64, 32);
    private static final ModelSkeletonHead humanoidHead = new ModelHumanoidHead();

    @Override
    public void renderTileEntityAt(TileEntitySkull tile, double x, double y, double z, float partialTicks, int destroyStage)
    {
        EnumFacing facing = EnumFacing.getFront(tile.getBlockMetadata() & 7);
        TileEntitySkullRendererNew.renderSkull((float)x, (float)y, (float)z, facing, tile.getSkullRotation() * 360 / 16.0F, tile.getSkullType(), tile.getPlayerProfile(), destroyStage, IndicatiaMod.MC.getTextureManager());
    }

    public static void renderSkull(float x, float y, float z, EnumFacing facing, float netHeadYaw, int skullType, @Nullable GameProfile profile, int destroyStage, TextureManager texture)
    {
        ModelBase model = TileEntitySkullRendererNew.skeletonHead;

        if (destroyStage >= 0)
        {
            texture.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 2.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
            switch (skullType)
            {
            case 0:
            default:
                texture.bindTexture(SKELETON_TEXTURES);
                break;
            case 1:
                texture.bindTexture(WITHER_SKELETON_TEXTURES);
                break;
            case 2:
                texture.bindTexture(ZOMBIE_TEXTURES);
                model = TileEntitySkullRendererNew.humanoidHead;
                break;
            case 3:
                model = TileEntitySkullRendererNew.humanoidHead;
                ResourceLocation resource = DefaultPlayerSkin.getDefaultSkinLegacy();

                if (profile != null)
                {
                    Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = IndicatiaMod.MC.getSkinManager().loadSkinFromCache(profile);
                    MinecraftProfileTexture.Type typeSkin = MinecraftProfileTexture.Type.SKIN;

                    if (map.containsKey(typeSkin))
                    {
                        resource = IndicatiaMod.MC.getSkinManager().loadSkin(map.get(typeSkin), typeSkin);
                    }
                    else
                    {
                        UUID uuid = EntityPlayer.getUUID(profile);
                        resource = DefaultPlayerSkin.getDefaultSkin(uuid);
                    }
                }
                texture.bindTexture(resource);
                break;
            case 4:
                texture.bindTexture(CREEPER_TEXTURES);
                break;
            }
        }

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();

        if (facing == EnumFacing.UP)
        {
            GlStateManager.translate(x + 0.5F, y, z + 0.5F);
        }
        else
        {
            switch (facing)
            {
            case NORTH:
                GlStateManager.translate(x + 0.5F, y + 0.25F, z + 0.74F);
                break;
            case SOUTH:
                GlStateManager.translate(x + 0.5F, y + 0.25F, z + 0.26F);
                netHeadYaw = 180.0F;
                break;
            case WEST:
                GlStateManager.translate(x + 0.74F, y + 0.25F, z + 0.5F);
                netHeadYaw = 270.0F;
                break;
            case EAST:
            default:
                GlStateManager.translate(x + 0.26F, y + 0.25F, z + 0.5F);
                netHeadYaw = 90.0F;
            }
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();

        if (skullType == 3 && ConfigManager.enableTransparentSkullRender)
        {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }

        model.render(null, 0.0F, 0.0F, 0.0F, netHeadYaw, 0.0F, 0.0625F);

        if (skullType == 3 && ConfigManager.enableTransparentSkullRender)
        {
            GlStateManager.disableBlend();
        }

        GlStateManager.popMatrix();

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}