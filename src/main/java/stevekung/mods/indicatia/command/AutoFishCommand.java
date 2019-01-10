package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.ItemFishingRod;
import stevekung.mods.indicatia.event.IndicatiaEventHandler;
import stevekung.mods.stevekunglib.utils.JsonUtils;
import stevekung.mods.stevekunglib.utils.LangUtils;

public class AutoFishCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("autofish").requires(requirement -> requirement.hasPermissionLevel(0)).then(Commands.literal("enable").executes(requirement -> AutoFishCommand.startAutoFish(requirement.getSource()))).then(Commands.literal("disable").executes(requirement -> AutoFishCommand.stopAutoFish(requirement.getSource()))));
    }

    private static int startAutoFish(CommandSource source)
    {
        if (!IndicatiaEventHandler.autoFish)
        {
            EntityPlayerSP player = Minecraft.getInstance().player;
            boolean mainHand = player.getHeldItemMainhand().getItem() instanceof ItemFishingRod;
            boolean offHand = player.getHeldItemOffhand().getItem() instanceof ItemFishingRod;

            if (player.getHeldItemMainhand().getItem() instanceof ItemFishingRod)
            {
                offHand = false;
            }

            if (mainHand || offHand)
            {
                IndicatiaEventHandler.autoFish = true;
                source.sendFeedback(LangUtils.translateComponent("message.auto_fish_enabled"), false);
            }
            else
            {
                source.sendFeedback(LangUtils.translateComponent("message.not_held_fishing_rod").setStyle(JsonUtils.red()), false);
            }
        }
        else
        {
            source.sendFeedback(LangUtils.translateComponent("message.already_start_autofish").setStyle(JsonUtils.red()), false);
        }
        return 0;
    }

    private static int stopAutoFish(CommandSource source)
    {
        if (IndicatiaEventHandler.autoFish)
        {
            IndicatiaEventHandler.autoFish = false;
            source.sendFeedback(LangUtils.translateComponent("message.auto_fish_disabled"), false);
        }
        else
        {
            source.sendFeedback(LangUtils.translateComponent("message.not_start_autofish").setStyle(JsonUtils.red()), false);
        }
        return 0;
    }
}