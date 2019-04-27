package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.Feedback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.server.command.CommandSource;
import stevekung.mods.indicatia.event.IndicatiaEventHandler;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class AutoFishCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("autofish").requires(requirement -> requirement.hasPermissionLevel(0)).then(ArgumentBuilders.literal("enable").executes(requirement -> AutoFishCommand.startAutoFish())).then(ArgumentBuilders.literal("disable").executes(requirement -> AutoFishCommand.stopAutoFish())));
    }

    private static int startAutoFish()
    {
        if (!IndicatiaEventHandler.autoFish)
        {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            boolean mainHand = player.getMainHandStack().getItem() instanceof FishingRodItem;
            boolean offHand = player.getOffHandStack().getItem() instanceof FishingRodItem;

            if (player.getMainHandStack().getItem() instanceof FishingRodItem)
            {
                offHand = false;
            }

            if (mainHand || offHand)
            {
                IndicatiaEventHandler.autoFish = true;
                Feedback.sendFeedback(LangUtils.translateComponent("commands.auto_fish.enable"));
            }
            else
            {
                Feedback.sendError(LangUtils.translateComponent("commands.auto_fish.not_equipped_fishing_rod"));
            }
        }
        else
        {
            Feedback.sendError(LangUtils.translateComponent("commands.auto_fish.auto_fish_started"));
        }
        return 1;
    }

    private static int stopAutoFish()
    {
        if (IndicatiaEventHandler.autoFish)
        {
            IndicatiaEventHandler.autoFish = false;
            Feedback.sendFeedback(LangUtils.translateComponent("commands.auto_fish.disable"));
        }
        else
        {
            Feedback.sendError(LangUtils.translateComponent("commands.auto_fish.auto_fish_not_started"));
        }
        return 1;
    }
}