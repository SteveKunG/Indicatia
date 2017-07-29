package stevekung.mods.indicatia.handler;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMap;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stevekung.mods.indicatia.config.ConfigManager;

public class BlockhitAnimationHandler
{
    private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
    private final Minecraft mc;
    private KeyBinding zoomKey;

    public BlockhitAnimationHandler(Minecraft mc)
    {
        this.mc = mc;

        for (KeyBinding key : this.mc.gameSettings.keyBindings)
        {
            if (key.getKeyDescription().contains("of.key.zoom"))
            {
                this.zoomKey = key;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderFirstHand(RenderHandEvent event)
    {
        event.setCanceled(true);

        if (!this.isZoomed())
        {
            this.renderHand(event.partialTicks, event.renderPass);
            this.mc.entityRenderer.renderWorldDirections(event.partialTicks);
        }
    }

    private void renderHand(float partialTicks, int pass)
    {
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();

        if (this.mc.gameSettings.anaglyph)
        {
            GlStateManager.translate(-(pass * 2 - 1) * 0.07F, 0.0F, 0.0F);
        }

        Project.gluPerspective(this.mc.entityRenderer.getFOVModifier(partialTicks, false), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.mc.entityRenderer.farPlaneDistance * 2.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();

        if (this.mc.gameSettings.anaglyph)
        {
            GlStateManager.translate((pass * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        GlStateManager.pushMatrix();
        this.mc.entityRenderer.hurtCameraEffect(partialTicks);

        if (this.mc.gameSettings.viewBobbing)
        {
            this.mc.entityRenderer.setupViewBobbing(partialTicks);
        }

        boolean isSleep = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();

        if (this.mc.gameSettings.thirdPersonView == 0 && !isSleep && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator())
        {
            this.mc.entityRenderer.enableLightmap();
            this.renderItemInFirstPerson(partialTicks);
            this.mc.entityRenderer.disableLightmap();
        }

        GlStateManager.popMatrix();

        if (this.mc.gameSettings.thirdPersonView == 0 && !isSleep)
        {
            this.mc.getItemRenderer().renderOverlays(partialTicks);
            this.mc.entityRenderer.hurtCameraEffect(partialTicks);
        }
        if (this.mc.gameSettings.viewBobbing)
        {
            this.mc.entityRenderer.setupViewBobbing(partialTicks);
        }
    }

    private void renderItemInFirstPerson(float partialTicks)
    {
        float prevSwingProgress = 1.0F - (this.mc.getItemRenderer().prevEquippedProgress + (this.mc.getItemRenderer().equippedProgress - this.mc.getItemRenderer().prevEquippedProgress) * partialTicks);
        AbstractClientPlayer player = this.mc.thePlayer;
        float swingProgress = player.getSwingProgress(partialTicks);
        float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
        float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;
        this.mc.getItemRenderer().rotateArroundXAndY(pitch, yaw);
        this.mc.getItemRenderer().setLightMapFromPlayer(player);
        this.mc.getItemRenderer().rotateWithPlayerRotations((EntityPlayerSP)player, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if (this.mc.getItemRenderer().itemToRender != null)
        {
            if (this.mc.getItemRenderer().itemToRender.getItem() instanceof ItemMap)
            {
                this.renderItemMap(player, pitch, prevSwingProgress, swingProgress);
            }
            else if (player.getItemInUseCount() > 0)
            {
                EnumAction action = this.mc.getItemRenderer().itemToRender.getItemUseAction();
                float swing = ConfigManager.enableBlockhitAnimation ? swingProgress : 0.0F;

                switch (action)
                {
                case NONE:
                    this.mc.getItemRenderer().transformFirstPersonItem(prevSwingProgress, swing);
                    break;
                case EAT:
                case DRINK:
                    this.mc.getItemRenderer().performDrinking(player, partialTicks);
                    this.mc.getItemRenderer().transformFirstPersonItem(prevSwingProgress, swing);
                    break;
                case BLOCK:
                    this.mc.getItemRenderer().transformFirstPersonItem(prevSwingProgress, swing);
                    this.mc.getItemRenderer().doBlockTransformations();
                    break;
                case BOW:
                    this.mc.getItemRenderer().transformFirstPersonItem(prevSwingProgress, swing);
                    this.mc.getItemRenderer().doBowTransformations(partialTicks, player);
                }
            }
            else
            {
                this.mc.getItemRenderer().doItemUsedTransformations(swingProgress);
                this.mc.getItemRenderer().transformFirstPersonItem(prevSwingProgress, swingProgress);
            }
            this.mc.getItemRenderer().renderItem(player, this.mc.getItemRenderer().itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        }
        else if (!player.isInvisible())
        {
            this.renderPlayerArm(player, prevSwingProgress, swingProgress);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    private void renderPlayerArm(AbstractClientPlayer player, float equipProgress, float swingProgress)
    {
        float f = -0.3F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        float f1 = 0.4F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI * 2.0F);
        float f2 = -0.4F * MathHelper.sin(swingProgress * (float)Math.PI);
        GlStateManager.translate(f, f1, f2);
        GlStateManager.translate(0.64000005F, -0.6F, -0.71999997F);
        GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f3 = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f4 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(f4 * 70.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f3 * -20.0F, 0.0F, 0.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(player.getLocationSkin());
        GlStateManager.translate(-1.0F, 3.6F, 3.5F);
        GlStateManager.rotate(120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        GlStateManager.translate(5.6F, 0.0F, 0.0F);
        Render<AbstractClientPlayer> render = this.mc.getRenderManager().getEntityRenderObject(this.mc.thePlayer);
        GlStateManager.disableCull();
        RenderPlayer renderplayer = (RenderPlayer)render;
        if (ConfigManager.enableAlternatePlayerModel)
        {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
        }
        renderplayer.renderRightArm(this.mc.thePlayer);
        if (ConfigManager.enableAlternatePlayerModel)
        {
            GlStateManager.disableBlend();
        }
        GlStateManager.enableCull();
    }

    private void renderItemMap(AbstractClientPlayer player, float pitch, float equipmentProgress, float swingProgress)
    {
        float f = -0.4F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI * 2.0F);
        float f2 = -0.2F * MathHelper.sin(swingProgress * (float)Math.PI);
        GlStateManager.translate(f, f1, f2);
        float f3 = this.mc.getItemRenderer().getMapAngleFromPitch(pitch);
        GlStateManager.translate(0.0F, 0.04F, -0.72F);
        GlStateManager.translate(0.0F, equipmentProgress * -1.2F, 0.0F);
        GlStateManager.translate(0.0F, f3 * -0.5F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f3 * -85.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
        this.renderPlayerArms(player);
        float f4 = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f5 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(f4 * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f5 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f5 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.38F, 0.38F, 0.38F);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(-1.0F, -1.0F, 0.0F);
        GlStateManager.scale(0.015625F, 0.015625F, 0.015625F);
        this.mc.getTextureManager().bindTexture(BlockhitAnimationHandler.RES_MAP_BACKGROUND);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0D, 135.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos(135.0D, 135.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos(135.0D, -7.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos(-7.0D, -7.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        MapData mapdata = Items.filled_map.getMapData(this.mc.getItemRenderer().itemToRender, this.mc.theWorld);

        if (mapdata != null)
        {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
    }

    private void renderPlayerArms(AbstractClientPlayer player)
    {
        this.mc.getTextureManager().bindTexture(player.getLocationSkin());
        Render<AbstractClientPlayer> render = this.mc.getRenderManager().getEntityRenderObject(this.mc.thePlayer);
        RenderPlayer renderplayer = (RenderPlayer)render;

        if (!player.isInvisible())
        {
            GlStateManager.disableCull();
            this.renderRightArm(renderplayer);
            this.renderLeftArm(renderplayer);
            GlStateManager.enableCull();
        }
    }

    private void renderRightArm(RenderPlayer renderPlayer)
    {
        GlStateManager.pushMatrix();
        if (ConfigManager.enableAlternatePlayerModel)
        {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
        }
        GlStateManager.rotate(54.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(64.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-62.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(0.25F, -0.85F, 0.75F);
        renderPlayer.renderRightArm(this.mc.thePlayer);
        if (ConfigManager.enableAlternatePlayerModel)
        {
            GlStateManager.disableBlend();
        }
        GlStateManager.popMatrix();
    }

    private void renderLeftArm(RenderPlayer renderPlayer)
    {
        GlStateManager.pushMatrix();
        if (ConfigManager.enableAlternatePlayerModel)
        {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
        }
        GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(41.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(-0.3F, -1.1F, 0.45F);
        renderPlayer.renderLeftArm(this.mc.thePlayer);
        if (ConfigManager.enableAlternatePlayerModel)
        {
            GlStateManager.disableBlend();
        }
        GlStateManager.popMatrix();
    }

    private boolean isZoomed()
    {
        if (FMLClientHandler.instance().hasOptifine() && this.zoomKey.isKeyDown())
        {
            return true;
        }
        return false;
    }
}