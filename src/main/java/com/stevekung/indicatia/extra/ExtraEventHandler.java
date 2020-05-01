package com.stevekung.indicatia.extra;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;

public class ExtraEventHandler
{
    private final MinecraftClient mc;
    static String autoRightClickMode = "Normal";
    static boolean autoRightClick;
    static int autoRightClickTick = 0;

    public ExtraEventHandler()
    {
        this.mc = MinecraftClient.getInstance();
    }

    private void stopTicking()
    {
        ExtraEventHandler.autoRightClickMode = "Normal";
        ExtraEventHandler.autoRightClick = false;
        ExtraEventHandler.autoRightClickTick = 0;
    }

    public void onClientTick()
    {
        if (this.mc.currentScreen != null && this.mc.currentScreen instanceof TitleScreen)
        {
            this.stopTicking();
        }
        if (this.mc.world != null)
        {
            for (Entity entity : this.mc.world.getEntities())
            {
                if (entity.getDisplayName() != null)
                {
                    entity.setGlowing(ExtraExtendedConfig.entityDetectTarget.equalsIgnoreCase(entity.getDisplayName().asString()));
                }
                if (ExtraExtendedConfig.entityDetectTarget.equalsIgnoreCase("all"))
                {
                    entity.setGlowing(!(entity instanceof ClientPlayerEntity));
                }
                else if (ExtraExtendedConfig.entityDetectTarget.equalsIgnoreCase("mob"))
                {
                    entity.setGlowing(entity instanceof Monster);
                }
                else if (ExtraExtendedConfig.entityDetectTarget.equalsIgnoreCase("creature"))
                {
                    entity.setGlowing(entity instanceof AnimalEntity || entity instanceof SquidEntity);
                }
                else if (ExtraExtendedConfig.entityDetectTarget.equalsIgnoreCase("non_entity"))
                {
                    entity.setGlowing(entity instanceof ItemEntity || entity instanceof ArmorStandEntity || entity instanceof BoatEntity || entity instanceof MinecartEntity);
                }
                else if (ExtraExtendedConfig.entityDetectTarget.equalsIgnoreCase("player"))
                {
                    entity.setGlowing(entity instanceof OtherClientPlayerEntity);
                }
            }
        }

        if (this.mc.player != null)
        {
            if (ExtraEventHandler.autoRightClick)
            {
                ExtraEventHandler.autoRightClickTick++;

                if (ExtraEventHandler.autoRightClickMode.equals("Normal"))
                {
                    if (ExtraEventHandler.autoRightClickTick % 4 == 0)
                    {
                        ExtraEventHandler.rightClickMouse(this.mc);
                    }
                }
                else
                {
                    ExtraEventHandler.rightClickMouse(this.mc);
                }
            }
        }
    }

    private static void rightClickMouse(MinecraftClient mc)
    {
        if (!mc.interactionManager.isBreakingBlock())
        {
            if (!mc.player.isRiding())
            {
                for (Hand hand : Hand.values())
                {
                    ItemStack itemStack = mc.player.getStackInHand(hand);

                    if (mc.crosshairTarget != null)
                    {
                        switch (mc.crosshairTarget.getType())
                        {
                        case ENTITY:
                            EntityHitResult result = (EntityHitResult)mc.crosshairTarget;
                            Entity entity = result.getEntity();

                            if (mc.interactionManager.interactEntityAtLocation(mc.player, entity, result, hand) == ActionResult.SUCCESS)
                            {
                                return;
                            }
                            if (mc.interactionManager.interactEntity(mc.player, entity, hand) == ActionResult.SUCCESS)
                            {
                                return;
                            }
                            break;
                        case BLOCK:
                            BlockHitResult blockResult = (BlockHitResult)mc.crosshairTarget;
                            int count = itemStack.getCount();
                            ActionResult actionResult = mc.interactionManager.interactBlock(mc.player, mc.world, hand, blockResult);

                            if (actionResult == ActionResult.SUCCESS)
                            {
                                mc.player.swingHand(hand);

                                if (!itemStack.isEmpty() && (itemStack.getCount() != count || mc.interactionManager.hasCreativeInventory()))
                                {
                                    mc.gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
                                }
                                return;
                            }
                            if (actionResult == ActionResult.FAIL)
                            {
                                return;
                            }
                            break;
                        default:
                            break;
                        }
                    }

                    if (!itemStack.isEmpty())
                    {
                        ActionResult result = mc.interactionManager.interactItem(mc.player, mc.world, hand);

                        if (result.isAccepted())
                        {
                            if (result.shouldSwingHand())
                            {
                                mc.player.swingHand(hand);
                            }
                            mc.gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
                            return;
                        }
                    }
                }
            }
        }
    }
}