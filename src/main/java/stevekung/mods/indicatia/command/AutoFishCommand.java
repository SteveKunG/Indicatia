package stevekung.mods.indicatia.command;

import com.mojang.brigadier.CommandDispatcher;

import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.FishingRodItem;
import stevekung.mods.indicatia.event.IndicatiaEventHandler;
import stevekung.mods.stevekungslib.utils.LangUtils;

public class AutoFishCommand implements ClientCommandPlugin
{
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher)
    {
        dispatcher.register(ArgumentBuilders.literal("autofish").requires(requirement -> requirement.hasPermissionLevel(0)).executes(requirement -> AutoFishCommand.doAutofish(requirement.getSource())));
    }

    private static int doAutofish(CottonClientCommandSource source)
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
                source.sendFeedback(LangUtils.translateComponent("commands.auto_fish.enable"));
                return 1;
            }
            else
            {
                source.sendError(LangUtils.translateComponent("commands.auto_fish.not_equipped_fishing_rod"));
                return 1;
            }
        }
        else
        {
            IndicatiaEventHandler.autoFish = false;
            source.sendFeedback(LangUtils.translateComponent("commands.auto_fish.disable"));
            return 1;
        }
    }
}