package stevekung.mods.indicatia.event;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import stevekung.mods.indicatia.config.ExtendedConfig;
import stevekung.mods.indicatia.core.IndicatiaMod;
import stevekung.mods.indicatia.extra.WeatherRegionScreen;
import stevekung.mods.indicatia.gui.exconfig.screen.ExtendedConfigScreen;
import stevekung.mods.indicatia.handler.KeyBindingHandler;
import stevekung.mods.stevekungslib.utils.CommonUtils;
import stevekung.mods.stevekungslib.utils.JsonUtils;
import stevekung.mods.stevekungslib.utils.LangUtils;
import stevekung.mods.stevekungslib.utils.client.ClientUtils;
import stevekung.mods.stevekungslib.utils.enums.CachedEnum;

public class IndicatiaEventHandler
{
    private MinecraftClient mc;

    public static boolean isAFK;
    public static String afkMode = "idle";
    public static String afkReason;
    public static int afkMoveTicks;
    public static int afkTicks;

    public static boolean autoFish;
    private static int autoFishTick;

    public IndicatiaEventHandler()
    {
        this.mc = MinecraftClient.getInstance();
    }

    public void onClientTick()
    {
        if (this.mc.player != null)
        {
            IndicatiaEventHandler.runAFK(this.mc.player);
            IndicatiaEventHandler.processAutoFish(this.mc);
            this.onPressKey();
        }
        if (this.mc.currentScreen != null && this.mc.currentScreen instanceof TitleScreen)
        {
            IndicatiaEventHandler.stopCommandTicks();
        }
    }

    private void onPressKey()
    {
        if (KeyBindingHandler.CONFIG.isPressed())
        {
            ExtendedConfigScreen config = new ExtendedConfigScreen();
            this.mc.openScreen(config);
        }
        if (KeyBindingHandler.WEATHER.isPressed())
        {
            WeatherRegionScreen weather = new WeatherRegionScreen();
            this.mc.openScreen(weather);
        }
        if (/*IndicatiaConfig.GENERAL.enableCustomCape.get() && */KeyBindingHandler.CUSTOM_CAPE.isPressed())
        {
            /*GuiCustomCape customCapeGui = new GuiCustomCape();
            this.mc.displayGuiScreen(customCapeGui);*/
        }
        if (ExtendedConfig.instance.toggleSprintUseMode.equals("key_binding") && InputUtil.isKeyPressed(this.mc.window.getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) && KeyBindingHandler.TOGGLE_SPRINT.isPressed())
        {
            ExtendedConfig.instance.toggleSprint = !ExtendedConfig.instance.toggleSprint;
            ClientUtils.setOverlayMessage(JsonUtils.create(ExtendedConfig.instance.toggleSprint ? LangUtils.translate("commands.indicatia.toggle_sprint.enable") : LangUtils.translate("commands.indicatia.toggle_sprint.disable")).asFormattedString());
            ExtendedConfig.instance.save();
        }
        if (ExtendedConfig.instance.toggleSneakUseMode.equals("key_binding") && InputUtil.isKeyPressed(this.mc.window.getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) && KeyBindingHandler.TOGGLE_SNEAK.isPressed())
        {
            ExtendedConfig.instance.toggleSneak = !ExtendedConfig.instance.toggleSneak;
            ClientUtils.setOverlayMessage(JsonUtils.create(ExtendedConfig.instance.toggleSneak ? LangUtils.translate("commands.indicatia.toggle_sneak.enable") : LangUtils.translate("commands.indicatia.toggle_sneak.disable")).asFormattedString());
            ExtendedConfig.instance.save();
        }
    }

    private static void runAFK(ClientPlayerEntity player)
    {
        if (IndicatiaEventHandler.isAFK)
        {
            IndicatiaEventHandler.afkTicks++;
            int tick = IndicatiaEventHandler.afkTicks;
            int messageMin = 1200 * 5;//TODO IndicatiaConfig.GENERAL.afkMessageTime.get()
            String s = "s";
            float angle = tick % 2 == 0 ? 0.0001F : -0.0001F;

            if (tick == 0)
            {
                s = "";
            }
            //if (IndicatiaConfig.GENERAL.enableAFKMessage.get()) TODO
            {
                if (tick % messageMin == 0)
                {
                    String reason = IndicatiaEventHandler.afkReason;
                    reason = reason.isEmpty() ? "" : ", Reason : " + reason;
                    player.sendChatMessage("AFK : " + CommonUtils.ticksToElapsedTime(tick) + " minute" + s + reason);
                }
            }

            switch (IndicatiaEventHandler.afkMode)
            {
            case "idle":
                player.changeLookDirection(angle, angle);
                break;
            case "360":
                player.changeLookDirection(1.0F, 0.0F);
                break;
            case "360_move":
                player.changeLookDirection(1.0F, 0.0F);
                IndicatiaEventHandler.afkMoveTicks++;
                IndicatiaEventHandler.afkMoveTicks %= 8;
                break;
            case "move":
                player.changeLookDirection(angle, angle);
                IndicatiaEventHandler.afkMoveTicks++;
                IndicatiaEventHandler.afkMoveTicks %= 8;
                break;
            }
        }
        else
        {
            IndicatiaEventHandler.afkTicks = 0;
        }
    }

    private static void stopCommandTicks()
    {
        if (IndicatiaEventHandler.isAFK)
        {
            IndicatiaEventHandler.isAFK = false;
            IndicatiaEventHandler.afkReason = "";
            IndicatiaEventHandler.afkTicks = 0;
            IndicatiaEventHandler.afkMoveTicks = 0;
            IndicatiaEventHandler.afkMode = "idle";
            IndicatiaMod.LOGGER.info("Stopping AFK Command");
        }
        if (IndicatiaEventHandler.autoFish)
        {
            IndicatiaEventHandler.autoFish = false;
            IndicatiaEventHandler.autoFishTick = 0;
            IndicatiaMod.LOGGER.info("Stopping Autofish Command");
        }
    }

    private static void processAutoFish(MinecraftClient mc)
    {
        if (IndicatiaEventHandler.autoFish)
        {
            ++IndicatiaEventHandler.autoFishTick;
            IndicatiaEventHandler.autoFishTick %= 4;

            if (mc.hitResult != null && mc.world != null && mc.interactionManager != null && mc.getEntityRenderManager() != null)
            {
                if (IndicatiaEventHandler.autoFishTick % 4 == 0)
                {
                    for (Hand hand : CachedEnum.handValues)
                    {
                        ItemStack itemStack = mc.player.getStackInHand(hand);
                        boolean fishingRod = mc.player.getStackInHand(hand).getItem() instanceof FishingRodItem;

                        if (fishingRod)
                        {
                            if (mc.hitResult.getType() == HitResult.Type.BLOCK)
                            {
                                BlockHitResult hitResult = (BlockHitResult)mc.hitResult;
                                int amount = itemStack.getCount();
                                ActionResult actionResult = mc.interactionManager.interactBlock(mc.player, mc.world, hand, hitResult);

                                if (actionResult == ActionResult.SUCCESS)
                                {
                                    mc.player.swingHand(hand);

                                    if (!itemStack.isEmpty() && (itemStack.getCount() != amount || mc.interactionManager.hasCreativeInventory()))
                                    {
                                        mc.gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
                                    }
                                    return;
                                }
                                if (actionResult == ActionResult.FAIL)
                                {
                                    return;
                                }
                            }
                        }
                        else
                        {
                            IndicatiaEventHandler.autoFish = false;
                            IndicatiaEventHandler.autoFishTick = 0;
                            mc.player.sendMessage(JsonUtils.create(LangUtils.translate("commands.auto_fish.not_equipped_fishing_rod")).setStyle(JsonUtils.red()));
                            return;
                        }

                        if (!itemStack.isEmpty())
                        {
                            TypedActionResult<ItemStack> result = mc.interactionManager.interactItem(mc.player, mc.world, hand);

                            if (result.getResult() == ActionResult.SUCCESS)
                            {
                                if (result.method_22429())
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
        else
        {
            IndicatiaEventHandler.autoFishTick = 0;
        }
    }
}