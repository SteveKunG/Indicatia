package stevekung.mods.indicatia.handler;

import org.lwjgl.util.glu.Project;

import com.google.common.base.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stevekung.mods.indicatia.config.ConfigManager;

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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderFirstHand(RenderHandEvent event)
    {
        event.setCanceled(true);

        if (!this.isZoomed())
        {
            this.renderHand(event.getPartialTicks(), event.getRenderPass());
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
            this.mc.entityRenderer.applyBobbing(partialTicks);
        }

        boolean isSleep = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();

        if (this.mc.gameSettings.thirdPersonView == 0 && !isSleep && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator())
        {
            this.mc.entityRenderer.enableLightmap();

            if (ConfigManager.enableBlockhitAnimation)
            {
                this.renderItemInFirstPerson(partialTicks);
            }
            else
            {
                this.mc.getItemRenderer().renderItemInFirstPerson(partialTicks);
            }
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
            this.mc.entityRenderer.applyBobbing(partialTicks);
        }
    }

    private void renderItemInFirstPerson(float partialTicks)
    {
        AbstractClientPlayer player = this.mc.player;
        float swingProgress = player.getSwingProgress(partialTicks);
        EnumHand hand = Objects.firstNonNull(player.swingingHand, EnumHand.MAIN_HAND);
        float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
        float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;
        boolean mainHand = true;
        boolean offHand = true;

        if (player.isHandActive())
        {
            ItemStack itemstack = player.getActiveItemStack();

            if (itemstack != null && itemstack.getItem() == Items.BOW)
            {
                EnumHand enumhand1 = player.getActiveHand();
                mainHand = enumhand1 == EnumHand.MAIN_HAND;
                offHand = !mainHand;
            }
        }

        this.mc.getItemRenderer().rotateArroundXAndY(pitch, yaw);
        this.mc.getItemRenderer().setLightmap();
        this.mc.getItemRenderer().rotateArm(partialTicks);
        GlStateManager.enableRescaleNormal();

        if (mainHand)
        {
            float mainHandSwing = hand == EnumHand.MAIN_HAND ? swingProgress : 0;
            float equipProgress = 1.0F - (this.mc.getItemRenderer().prevEquippedProgressMainHand + (this.mc.getItemRenderer().equippedProgressMainHand - this.mc.getItemRenderer().prevEquippedProgressMainHand) * partialTicks);
            this.renderItemInFirstPerson(player, partialTicks, pitch, EnumHand.MAIN_HAND, mainHandSwing, this.mc.getItemRenderer().itemStackMainHand, equipProgress);
        }
        if (offHand)
        {
            float offHandSwing = hand == EnumHand.OFF_HAND ? swingProgress : 0;
            float equipProgress = 1.0F - (this.mc.getItemRenderer().prevEquippedProgressOffHand + (this.mc.getItemRenderer().equippedProgressOffHand - this.mc.getItemRenderer().prevEquippedProgressOffHand) * partialTicks);
            this.renderItemInFirstPerson(player, partialTicks, pitch, EnumHand.OFF_HAND, offHandSwing, this.mc.getItemRenderer().itemStackOffHand, equipProgress);
        }
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    private void renderItemInFirstPerson(AbstractClientPlayer player, float partialTicks, float rotationPitch, EnumHand hand, float swingProgress, ItemStack itemStack, float equipProgress)
    {
        boolean mainHand = hand == EnumHand.MAIN_HAND;
        EnumHandSide handSide = mainHand ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
        GlStateManager.pushMatrix();

        if (itemStack == null)
        {
            if (mainHand && !player.isInvisible())
            {
                this.mc.getItemRenderer().renderArmFirstPerson(equipProgress, swingProgress, handSide);
            }
        }
        else if (itemStack.getItem() instanceof ItemMap)
        {
            if (mainHand && this.mc.getItemRenderer().itemStackOffHand == null)
            {
                this.mc.getItemRenderer().renderMapFirstPerson(rotationPitch, equipProgress, swingProgress);
            }
            else
            {
                this.mc.getItemRenderer().renderMapFirstPersonSide(equipProgress, handSide, swingProgress, itemStack);
            }
        }
        else
        {
            boolean rightSide = handSide == EnumHandSide.RIGHT;

            if (player.isHandActive() && player.getItemInUseCount() > 0 && player.getActiveHand() == hand)
            {
                int handType = rightSide ? 1 : -1;
                float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
                float f1 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float)Math.PI);

                switch (itemStack.getItemUseAction())
                {
                case NONE:
                    this.mc.getItemRenderer().transformSideFirstPerson(handSide, equipProgress);
                    this.swingHandOldAnimation(equipProgress, f, f1);
                    break;
                case EAT:
                case DRINK:
                    this.mc.getItemRenderer().transformEatFirstPerson(partialTicks, handSide, itemStack);
                    this.mc.getItemRenderer().transformSideFirstPerson(handSide, equipProgress);
                    this.swingHandOldAnimation(equipProgress, f, f1);
                    break;
                case BLOCK:
                    this.mc.getItemRenderer().transformSideFirstPerson(handSide, equipProgress);
                    this.swingHandOldAnimation(equipProgress, f, f1);
                    break;
                case BOW:
                    this.mc.getItemRenderer().transformSideFirstPerson(handSide, equipProgress);
                    this.swingHandOldAnimation(equipProgress, f, f1);
                    GlStateManager.translate(handType * -0.2785682F, 0.18344387F, 0.15731531F);
                    GlStateManager.rotate(-13.935F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(handType * 35.3F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(handType * -9.785F, 0.0F, 0.0F, 1.0F);
                    float f5 = itemStack.getMaxItemUseDuration() - (this.mc.player.getItemInUseCount() - partialTicks + 1.0F);
                    float f6 = f5 / 20.0F;
                    f6 = (f6 * f6 + f6 * 2.0F) / 3.0F;

                    if (f6 > 1.0F)
                    {
                        f6 = 1.0F;
                    }
                    if (f6 > 0.1F)
                    {
                        float f7 = MathHelper.sin((f5 - 0.1F) * 1.3F);
                        float f3 = f6 - 0.1F;
                        float f4 = f7 * f3;
                        GlStateManager.translate(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
                    }
                    GlStateManager.translate(f6 * 0.0F, f6 * 0.0F, f6 * 0.04F);
                    GlStateManager.scale(1.0F, 1.0F, 1.0F + f6 * 0.2F);
                    GlStateManager.rotate(handType * 45.0F, 0.0F, -1.0F, 0.0F);
                }
            }
            else
            {
                float f = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float)Math.PI);
                float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * ((float)Math.PI * 2F));
                float f2 = -0.2F * MathHelper.sin(swingProgress * (float)Math.PI);
                int i = rightSide ? 1 : -1;
                GlStateManager.translate(i * f, f1, f2);
                this.mc.getItemRenderer().transformSideFirstPerson(handSide, equipProgress);
                this.mc.getItemRenderer().transformFirstPerson(handSide, swingProgress);
            }
            this.mc.getItemRenderer().renderItemSide(player, itemStack, rightSide ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !rightSide);
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

    private void swingHandOldAnimation(float equipProgress, float f, float f1)
    {
        GlStateManager.translate(0.0F, equipProgress * 0.6F, 0.0F);
        GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f * 20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f1 * 20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
    }
}