package stevekung.mods.indicatia.event;

import com.google.common.base.MoreObjects;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import stevekung.mods.indicatia.config.IndicatiaConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.gui.config.GuiRenderPreview;

public class BlockhitAnimationEventHandler
{
    private Minecraft mc;
    private KeyBinding zoomKey;

    public BlockhitAnimationEventHandler()
    {
        this.mc = Minecraft.getInstance();
        Arrays.stream(this.mc.gameSettings.keyBindings).filter(key -> key.getKeyDescription().contains("of.key.zoom")).forEach(key -> this.zoomKey = key);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderFirstHand(RenderHandEvent event)
    {
        if (this.mc.currentScreen instanceof GuiRenderPreview)
        {
            event.setCanceled(true);
            return;
        }
        if (IndicatiaConfig.GENERAL.enableBlockhitAnimation.get())
        {
            event.setCanceled(true);

            if (!this.isZoomed())
            {
                boolean isSleep = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();

                if (this.mc.gameSettings.thirdPersonView == 0 && !isSleep && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectatorMode())
                {
                    this.mc.entityRenderer.enableLightmap();
                    this.renderItemInFirstPerson(event.getPartialTicks());
                    this.mc.entityRenderer.disableLightmap();
                }
            }
        }
    }

    private void renderItemInFirstPerson(float partialTicks)
    {
        AbstractClientPlayer player = this.mc.player;
        float swingProgress = player.getSwingProgress(partialTicks);
        EnumHand hand = MoreObjects.firstNonNull(player.swingingHand, EnumHand.MAIN_HAND);
        float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
        float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;
        boolean mainHand = true;
        boolean offHand = true;

        if (player.isHandActive())
        {
            ItemStack itemStack = player.getActiveItemStack();

            if (!itemStack.isEmpty() && itemStack.getItem() == Items.BOW)
            {
                EnumHand activeHand = player.getActiveHand();
                mainHand = activeHand == EnumHand.MAIN_HAND;
                offHand = !mainHand;
            }
        }

        this.mc.getFirstPersonRenderer().rotateArroundXAndY(pitch, yaw);
        this.mc.getFirstPersonRenderer().setLightmap();
        this.mc.getFirstPersonRenderer().rotateArm(partialTicks);
        GlStateManager.enableRescaleNormal();

        if (mainHand)
        {
            float mainHandSwing = hand == EnumHand.MAIN_HAND ? swingProgress : 0.0F;
            float equipProgress = 1.0F - (this.mc.getFirstPersonRenderer().prevEquippedProgressMainHand + (this.mc.getFirstPersonRenderer().equippedProgressMainHand - this.mc.getFirstPersonRenderer().prevEquippedProgressMainHand) * partialTicks);
            this.renderItemInFirstPerson(player, partialTicks, pitch, EnumHand.MAIN_HAND, mainHandSwing, this.mc.getFirstPersonRenderer().itemStackMainHand, equipProgress);
        }
        if (offHand)
        {
            float offHandSwing = hand == EnumHand.OFF_HAND ? swingProgress : 0.0F;
            float equipProgress = 1.0F - (this.mc.getFirstPersonRenderer().prevEquippedProgressOffHand + (this.mc.getFirstPersonRenderer().equippedProgressOffHand - this.mc.getFirstPersonRenderer().prevEquippedProgressOffHand) * partialTicks);
            this.renderItemInFirstPerson(player, partialTicks, pitch, EnumHand.OFF_HAND, offHandSwing, this.mc.getFirstPersonRenderer().itemStackOffHand, equipProgress);
        }
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    private void renderItemInFirstPerson(AbstractClientPlayer player, float partialTicks, float rotationPitch, EnumHand hand, float swingProgress, ItemStack itemStack, float equipProgress)
    {
        boolean mainHand = hand == EnumHand.MAIN_HAND;
        EnumHandSide handSide = mainHand ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
        GlStateManager.pushMatrix();

        if (itemStack.isEmpty())
        {
            if (mainHand && !player.isInvisible())
            {
                this.mc.getFirstPersonRenderer().renderArmFirstPerson(equipProgress, swingProgress, handSide);
            }
        }
        else if (itemStack.getItem() instanceof ItemMap)
        {
            if (mainHand && this.mc.getFirstPersonRenderer().itemStackOffHand.isEmpty())
            {
                this.mc.getFirstPersonRenderer().renderMapFirstPerson(rotationPitch, equipProgress, swingProgress);
            }
            else
            {
                this.mc.getFirstPersonRenderer().renderMapFirstPersonSide(equipProgress, handSide, swingProgress, itemStack);
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
                float f5 = itemStack.getUseDuration() - (this.mc.player.getItemInUseCount() - partialTicks + 1.0F);

                switch (itemStack.getUseAction())
                {
                case NONE:
                    this.mc.getFirstPersonRenderer().transformSideFirstPerson(handSide, equipProgress);
                    this.swingHandOldAnimation(equipProgress, f, f1);
                    break;
                case EAT:
                case DRINK:
                    this.mc.getFirstPersonRenderer().transformEatFirstPerson(partialTicks, handSide, itemStack);
                    this.mc.getFirstPersonRenderer().transformSideFirstPerson(handSide, equipProgress);
                    this.swingHandOldAnimation(equipProgress, f, f1);
                    break;
                case BLOCK:
                    this.mc.getFirstPersonRenderer().transformSideFirstPerson(handSide, equipProgress);
                    this.swingHandOldAnimation(equipProgress, f, f1);
                    break;
                case BOW:
                    this.mc.getFirstPersonRenderer().transformSideFirstPerson(handSide, equipProgress);
                    this.swingHandOldAnimation(equipProgress, f, f1);
                    GlStateManager.translatef(handType * -0.2785682F, 0.18344387F, 0.15731531F);
                    GlStateManager.rotatef(-13.935F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotatef(handType * 35.3F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotatef(handType * -9.785F, 0.0F, 0.0F, 1.0F);
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
                        GlStateManager.translatef(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
                    }
                    GlStateManager.translatef(f6 * 0.0F, f6 * 0.0F, f6 * 0.04F);
                    GlStateManager.scalef(1.0F, 1.0F, 1.0F + f6 * 0.2F);
                    GlStateManager.rotatef(handType * 45.0F, 0.0F, -1.0F, 0.0F);
                    break;
                case SPEAR:
                    this.mc.getFirstPersonRenderer().transformSideFirstPerson(handSide, equipProgress);
                    GlStateManager.translatef(handType * -0.5F, 0.7F, 0.1F);
                    GlStateManager.rotatef(-55.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotatef(handType * 35.3F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotatef(handType * -9.785F, 0.0F, 0.0F, 1.0F);
                    float f7 = f5 / 10.0F;

                    if (f7 > 1.0F)
                    {
                        f7 = 1.0F;
                    }
                    if (f7 > 0.1F)
                    {
                        float f9 = MathHelper.sin((f5 - 0.1F) * 1.3F);
                        float f2 = f7 - 0.1F;
                        float f3 = f9 * f2;
                        GlStateManager.translatef(f3 * 0.0F, f3 * 0.004F, f3 * 0.0F);
                    }
                    GlStateManager.translatef(0.0F, 0.0F, f7 * 0.2F);
                    GlStateManager.scalef(1.0F, 1.0F, 1.0F + f7 * 0.2F);
                    GlStateManager.rotatef(handType * 45.0F, 0.0F, -1.0F, 0.0F);
                    break;
                }
            }
            else
            {
                float f = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float)Math.PI);
                float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * ((float)Math.PI * 2F));
                float f2 = -0.2F * MathHelper.sin(swingProgress * (float)Math.PI);
                int i = rightSide ? 1 : -1;
                GlStateManager.translatef(i * f, f1, f2);
                this.mc.getFirstPersonRenderer().transformSideFirstPerson(handSide, equipProgress);
                this.mc.getFirstPersonRenderer().transformFirstPerson(handSide, swingProgress);
            }
            this.mc.getFirstPersonRenderer().renderItemSide(player, itemStack, rightSide ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !rightSide);
        }
        GlStateManager.popMatrix();
    }

    private boolean isZoomed()
    {
        return IndicatiaMod.isOptiFineLoaded && this.zoomKey.isKeyDown();
    }

    private void swingHandOldAnimation(float equipProgress, float f, float f1)
    {
        GlStateManager.translatef(0.0F, equipProgress * 0.6F, 0.0F);
        GlStateManager.rotatef(0.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(f * 20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(f1 * 20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotatef(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
    }
}