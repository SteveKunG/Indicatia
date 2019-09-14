package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;
import com.stevekung.stevekungslib.utils.LangUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.FishingRodItem;
import stevekung.mods.indicatia.event.IndicatiaEventHandler;

public class AutoFishCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("autofish").requires(requirement -> requirement.hasPermissionLevel(0)).executes(requirement -> AutoFishCommand.doAutofish(requirement.getSource())));
    }

    private static int doAutofish(CommandSource source)
    {
        if (!IndicatiaEventHandler.autoFish)
        {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            boolean mainHand = player.getHeldItemMainhand().getItem() instanceof FishingRodItem;
            boolean offHand = player.getHeldItemOffhand().getItem() instanceof FishingRodItem;

            if (player.getHeldItemMainhand().getItem() instanceof FishingRodItem)
            {
                offHand = false;
            }

            if (mainHand || offHand)
            {
                IndicatiaEventHandler.autoFish = true;
                source.sendFeedback(LangUtils.translateComponent("commands.auto_fish.enable"), false);
                return 1;
            }
            else
            {
                source.sendErrorMessage(LangUtils.translateComponent("commands.auto_fish.not_equipped_fishing_rod"));
                return 0;
            }
        }
        else
        {
            IndicatiaEventHandler.autoFish = false;
            source.sendFeedback(LangUtils.translateComponent("commands.auto_fish.disable"), false);
            return 1;
        }
    }
}