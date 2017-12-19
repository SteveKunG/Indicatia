package stevekung.mods.indicatia.handler;

import org.lwjgl.util.glu.Project;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMap;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stevekung.mods.indicatia.config.ConfigManager;

@SuppressWarnings("deprecation")
public class BlockhitAnimationHandler
{
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderFirstHand(RenderHandEvent event)
    {
        if (ConfigManager.enableBlockhitAnimation)
        {
            event.setCanceled(true);

            if (!this.isZoomed())
            {
                if (ConfigManager.enableAlternatePlayerModel)
                {
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(770, 771);
                }
                this.renderHand(event.partialTicks, event.renderPass);
                if (ConfigManager.enableAlternatePlayerModel)
                {
                    GlStateManager.disableBlend();
                }
                this.mc.entityRenderer.renderWorldDirections(event.partialTicks);
            }
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
        this.mc.getItemRenderer().func_178101_a(pitch, yaw);
        this.mc.getItemRenderer().func_178109_a(player);
        this.mc.getItemRenderer().func_178110_a((EntityPlayerSP)player, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if (this.mc.getItemRenderer().itemToRender != null)
        {
            if (this.mc.getItemRenderer().itemToRender.getItem() instanceof ItemMap)
            {
                this.mc.getItemRenderer().renderItemMap(player, pitch, prevSwingProgress, swingProgress);
            }
            else if (player.getItemInUseCount() > 0)
            {
                EnumAction action = this.mc.getItemRenderer().itemToRender.getItemUseAction();

                switch (action)
                {
                case NONE:
                    this.mc.getItemRenderer().transformFirstPersonItem(prevSwingProgress, swingProgress);
                    break;
                case EAT:
                case DRINK:
                    this.mc.getItemRenderer().func_178104_a(player, partialTicks);
                    this.mc.getItemRenderer().transformFirstPersonItem(prevSwingProgress, swingProgress);
                    break;
                case BLOCK:
                    this.mc.getItemRenderer().transformFirstPersonItem(prevSwingProgress, swingProgress);
                    this.mc.getItemRenderer().func_178103_d();
                    break;
                case BOW:
                    this.mc.getItemRenderer().transformFirstPersonItem(prevSwingProgress, swingProgress);
                    this.mc.getItemRenderer().func_178098_a(partialTicks, player);
                }
            }
            else
            {
                this.mc.getItemRenderer().func_178105_d(swingProgress);
                this.mc.getItemRenderer().transformFirstPersonItem(prevSwingProgress, swingProgress);
            }
            this.mc.getItemRenderer().renderItem(player, this.mc.getItemRenderer().itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        }
        else if (!player.isInvisible())
        {
            this.mc.getItemRenderer().func_178095_a(player, prevSwingProgress, swingProgress);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
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