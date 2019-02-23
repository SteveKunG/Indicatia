package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.ItemFishingRod;
import stevekung.mods.indicatia.event.IndicatiaEventHandler;
import stevekung.mods.stevekungslib.utils.LangUtils;

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
                source.sendFeedback(LangUtils.translateComponent("commands.auto_fish.enable"), false);
            }
            else
            {
                source.sendErrorMessage(LangUtils.translateComponent("commands.auto_fish.not_equipped_fishing_rod"));
            }
        }
        else
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.auto_fish.auto_fish_started"));
        }
        return 0;
    }

    private static int stopAutoFish(CommandSource source)
    {
        if (IndicatiaEventHandler.autoFish)
        {
            IndicatiaEventHandler.autoFish = false;
            source.sendFeedback(LangUtils.translateComponent("commands.auto_fish.disable"), false);
        }
        else
        {
            source.sendErrorMessage(LangUtils.translateComponent("commands.auto_fish.auto_fish_not_started"));
        }
        return 0;
    }
}