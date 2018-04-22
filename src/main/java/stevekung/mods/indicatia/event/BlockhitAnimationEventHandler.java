package stevekung.mods.indicatia.event;

import java.util.Arrays;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
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
import stevekung.mods.indicatia.config.ConfigManagerIN;
import stevekung.mods.indicatia.gui.config.GuiRenderPreview;

public class BlockhitAnimationEventHandler
{
    private Minecraft mc;
    private KeyBinding zoomKey;
    private ItemRenderer itemRenderer;

    public BlockhitAnimationEventHandler()
    {
        this.mc = Minecraft.getMinecraft();
        this.itemRenderer = this.mc.getItemRenderer();

        Arrays.stream(this.mc.gameSettings.keyBindings).filter(key -> key.getKeyDescription().contains("of.key.zoom")).forEach(key ->
        {
            this.zoomKey = key;
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderFirstHand(RenderHandEvent event)
    {
        if (this.mc.currentScreen instanceof GuiRenderPreview)
        {
            event.setCanceled(true);
            return;
        }
        if (ConfigManagerIN.indicatia_general.enableBlockhitAnimation)
        {
            event.setCanceled(true);

            if (!this.isZoomed())
            {
                boolean isSleep = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();

                if (this.mc.gameSettings.thirdPersonView == 0 && !isSleep && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator())
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
            ItemStack itemstack = player.getActiveItemStack();

            if (!itemstack.isEmpty() && itemstack.getItem() == Items.BOW)
            {
                EnumHand enumhand1 = player.getActiveHand();
                mainHand = enumhand1 == EnumHand.MAIN_HAND;
                offHand = !mainHand;
            }
        }

        this.itemRenderer.rotateArroundXAndY(pitch, yaw);
        this.itemRenderer.setLightmap();
        this.itemRenderer.rotateArm(partialTicks);
        GlStateManager.enableRescaleNormal();

        if (mainHand)
        {
            float mainHandSwing = hand == EnumHand.MAIN_HAND ? swingProgress : 0.0F;
            float equipProgress = 1.0F - (this.itemRenderer.prevEquippedProgressMainHand + (this.itemRenderer.equippedProgressMainHand - this.itemRenderer.prevEquippedProgressMainHand) * partialTicks);
            this.renderItemInFirstPerson(player, partialTicks, pitch, EnumHand.MAIN_HAND, mainHandSwing, this.itemRenderer.itemStackMainHand, equipProgress);
        }
        if (offHand)
        {
            float offHandSwing = hand == EnumHand.OFF_HAND ? swingProgress : 0.0F;
            float equipProgress = 1.0F - (this.itemRenderer.prevEquippedProgressOffHand + (this.itemRenderer.equippedProgressOffHand - this.itemRenderer.prevEquippedProgressOffHand) * partialTicks);
            this.renderItemInFirstPerson(player, partialTicks, pitch, EnumHand.OFF_HAND, offHandSwing, this.itemRenderer.itemStackOffHand, equipProgress);
        }
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    private void renderItemInFirstPerson(AbstractClientPlayer player, float partialTicks, float rotationPitch, EnumHand hand, float swingProgress, @Nullable ItemStack itemStack, float equipProgress)
    {
        boolean mainHand = hand == EnumHand.MAIN_HAND;
        EnumHandSide handSide = mainHand ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
        GlStateManager.pushMatrix();

        if (itemStack.isEmpty())
        {
            if (mainHand && !player.isInvisible())
            {
                this.itemRenderer.renderArmFirstPerson(equipProgress, swingProgress, handSide);
            }
        }
        else if (itemStack.getItem() instanceof ItemMap)
        {
            if (mainHand && this.itemRenderer.itemStackOffHand.isEmpty())
            {
                this.itemRenderer.renderMapFirstPerson(rotationPitch, equipProgress, swingProgress);
            }
            else
            {
                this.itemRenderer.renderMapFirstPersonSide(equipProgress, handSide, swingProgress, itemStack);
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
                    this.itemRenderer.transformSideFirstPerson(handSide, equipProgress);
                    this.swingHandOldAnimation(equipProgress, f, f1);
                    break;
                case EAT:
                case DRINK:
                    this.itemRenderer.transformEatFirstPerson(partialTicks, handSide, itemStack);
                    this.itemRenderer.transformSideFirstPerson(handSide, equipProgress);
                    this.swingHandOldAnimation(equipProgress, f, f1);
                    break;
                case BLOCK:
                    this.itemRenderer.transformSideFirstPerson(handSide, equipProgress);
                    this.swingHandOldAnimation(equipProgress, f, f1);
                    break;
                case BOW:
                    this.itemRenderer.transformSideFirstPerson(handSide, equipProgress);
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
                this.itemRenderer.transformSideFirstPerson(handSide, equipProgress);
                this.itemRenderer.transformFirstPerson(handSide, swingProgress);
            }
            this.itemRenderer.renderItemSide(player, itemStack, rightSide ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !rightSide);
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