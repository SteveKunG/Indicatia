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
        dispatcher.register(ArgumentBuilders.literal("autofish").requires(requirement -> requirement.hasPermissionLevel(0)).executes(requirement -> AutoFishCommand.doAutofish()));
    }

    private static int doAutofish()
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
                return 1;
            }
            else
            {
                Feedback.sendError(LangUtils.translateComponent("commands.auto_fish.not_equipped_fishing_rod"));
                return 0;
            }
        }
        else
        {
            IndicatiaEventHandler.autoFish = false;
            Feedback.sendFeedback(LangUtils.translateComponent("commands.auto_fish.disable"));
            return 1;
        }
    }
}